(ns crystal-key-item-randomizer.progression
  (:require [clojure.set :as cset]))

;; The code in this namespace uses what we know about the game and how
;; progress can be made, along with the particular items and progress
;; the player has, to determine further progress.

(def guaranteed-items [:MYSTERY_EGG
                       :HM_FLASH
                       :OLD_ROD
                       :HM_CUT])

(def goldenrod-items [:BICYCLE
                       :BLUE_CARD
                       :COIN_CASE
                       :SQUIRTBOTTLE])

(def ecruteak-and-olivine-items [:HM_SURF
                                 :ITEMFINDER
                                 :GOOD_ROD
                                 :HM_STRENGTH])

(def surf-required-items [:HM_FLY       ; Cianwood
                          :SECRETPOTION ; Cianwood

                          :RED_SCALE    ; Lake of Rage/Mahogany Rockets sidequest
                          :HM_WHIRLPOOL ; Lake of Rage/Mahogany Rockets sidequest

                          :BASEMENT_KEY ; Rockets in the Radio Tower

                          :HM_WATERFALL]) ; Ice Path after defeating Pryce

(def badge-acquisition-conditions [{:badge :ZEPHYRBADGE}
                                   {:badge :HIVEBADGE}
                                   {:badge :PLAINBADGE
                                    :conditions-met #{:goldenrod}}
                                   {:badge :FOGBADGE
                                    :conditions-met #{:ecruteak}}
                                   {:badge :STORMBADGE
                                    :conditions-met #{:can-surf}}
                                   {:badge :MINERALBADGE
                                    :conditions-met #{:ecruteak}
                                    :items-obtained #{:SECRETPOTION}}
                                   {:badge :GLACIERBADGE
                                    :conditions-met #{:underground-warehouse}}
                                   {:badge :RISINGBADGE
                                    :conditions-met #{:underground-warehouse}}

                                   {:badge :BOULDERBADGE
                                    :conditions-met #{:fix-power-plant}}
                                   {:badge :CASCADEBADGE
                                    :conditions-met #{:kanto}}
                                   {:badge :THUNDERBADGE
                                    :conditions-met #{:kanto}
                                    :items-obtained #{:HM_CUT}}
                                   {:badge :RAINBOWBADGE
                                    :conditions-met #{:kanto}
                                    :items-obtained #{:HM_CUT}}
                                   {:badge :SOULBADGE
                                    :conditions-met #{:kanto}}
                                   {:badge :MARSHBADGE
                                    :conditions-met #{:kanto}}
                                   {:badge :VOLCANOBADGE
                                    :conditions-met #{:fix-power-plant}}
                                   {:badge :EARTHBADGE
                                    :conditions-met #{:fix-power-plant}}])

(defn any? [pred col]
  (not (not-any? pred col)))

(defn get-swaps [swaps originals]
  (-> swaps
      (select-keys originals)
      (vals)
      (into #{})))

(defn can-reach-goldenrod? [{:keys [swaps items-obtained]}]
  (if (items-obtained :HM_CUT)
    {:swaps swaps
     :items-obtained (cset/union items-obtained (get-swaps swaps goldenrod-items))
     :conditions-met #{:goldenrod}}
    {:swaps swaps
     :items-obtained items-obtained
     :conditions-met #{}
     :reasons ["goldenrod: cannot reach without HM_CUT"]}))

(defn can-read-ecruteak-with-copycats-reward? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  ;; FIXME: if you get the LOST_ITEM this early then i don't think you
  ;; can get anything from the guy in Vermillion; see
  ;; PokemonFanClubClefairyGuyScript.FoundClefairyDoll in
  ;; PokemonFanClub.asm. to rectify this, we'd need to have the
  ;; ClefairyGuy check EVENT_RETURNED_MACHINE_PART (or something)
  ;; instead.
  (if (items-obtained :LOST_ITEM)
    (let [pass-swap (swaps :PASS)
          items-obtained' (cset/union items-obtained pass-swap)]
      (if (contains? '(:SQUIRTBOTTLE :S_S_TICKET) pass-swap)
        {:swaps swaps
         :items-obtained items-obtained'
         :conditions-met (conj conditions-met :ecruteak :copycat-item)}
        {:swaps swaps
         :items-obtained items-obtained'
         :conditions-met (conj conditions-met :copycat-item)
         :reasons (conj reasons "ecruteak: cannot reach without SQUIRTBOTTLE or S_S_TICKET")}))
    {:swaps swaps
     :items-obtained items-obtained
     :conditions-met conditions-met
     :reasons (conj reasons "ecruteak: cannot reach without SQUIRTBOTTLE or S_S_TICKET")}))

(defn can-reach-ecruteak-via-saffron-detour? [{:keys [swaps items-obtained conditions-met] :as args}]
  (if (any? #(items-obtained %1) '(:SQUIRTBOTTLE :S_S_TICKET))
    {:swaps swaps
     :items-obtained items-obtained
     :conditions-met (conj conditions-met :ecruteak)}
    (can-read-ecruteak-with-copycats-reward? {:swaps swaps
                                              :items-obtained items-obtained
                                              :conditions-met conditions-met})))

(defn can-reach-ecruteak? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (not (conditions-met :goldenrod))
    args
    (if (items-obtained :SQUIRTBOTTLE)
      {:swaps swaps
       :items-obtained (cset/union items-obtained (get-swaps swaps ecruteak-and-olivine-items))
       :conditions-met (conj conditions-met :ecruteak)}
      (if (items-obtained :PASS)
        (let [result (can-reach-ecruteak-via-saffron-detour? {:swaps swaps
                                                              :items-obtained (cset/union items-obtained
                                                                                          (get-swaps swaps [:SUPER_ROD :MACHINE_PART]))
                                                              :conditions-met (conj conditions-met :kanto)})]
          (if (contains? (result :conditions-met) :ecruteak)
            (assoc result :items-obtained (concat (result :items-obtained)
                                                  (get-swaps swaps ecruteak-and-olivine-items)))
            result))
        (-> args
            (assoc :items-obtained items-obtained)
            (assoc :reasons (conj reasons "ecruteak: cannot reach without PASS or SQUIRTBOTTLE")))))))

(defn can-surf? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :can-surf)
    args
    (if (and (conditions-met :ecruteak)
             (items-obtained :HM_SURF))
      (-> args
          (assoc :items-obtained (cset/union items-obtained (get-swaps swaps surf-required-items)))
          (assoc :conditions-met (conj conditions-met :can-surf)))
      args)))

(defn can-reach-underground-warehouse? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  ;; FIXME: warn players to avoid the underground warehouse until
  ;; they've defeated the Mahogany Rockets, even if they have the
  ;; BASEMENT_KEY
  (if (conditions-met :underground-warehouse)
    args
    (if (not (conditions-met :can-surf))
      (assoc args :reasons
             (conj reasons "underground-warehouse: cannot reach without being able to surf"))
      (if (items-obtained :BASEMENT_KEY)
        (-> args
            (assoc :items-obtained (conj items-obtained (swaps :CARD_KEY)))
            (assoc :conditions-met (conj conditions-met :underground-warehouse)))
        (assoc args :reasons
               (conj reasons "underground-warehouse: cannot reach without BASEMENT_KEY"))))))

(defn can-defeat-team-rocket? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :defeat-team-rocket)
    args
    (if (not (conditions-met :underground-warehouse))
      (assoc args :reason
             (conj reasons "defeat-team-rocket: cannot reach without having reached underground-warehouse"))
      (if (items-obtained :CARD_KEY)
        (-> args
            (assoc :items-obtained (conj items-obtained (swaps :CLEAR_BELL)))
            (assoc :conditions-met (conj conditions-met :defeat-team-rocket)))
        (assoc args :reasons
               (conj reasons "defeat-team-rocket: cannot reach without CARD_KEY"))))))

(defn can-reach-kanto? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :kanto)
    args
    (if (or (items-obtained :PASS) (items-obtained :S_S_TICKET))
      (-> args
          (assoc :items-obtained (cset/union items-obtained
                                                           (get-swaps swaps [:SUPER_ROD :MACHINE_PART])))
          (assoc :conditions-met (conj conditions-met :kanto)))
      (-> args
          (assoc :items-obtained items-obtained)
          (assoc :reasons (conj reasons "kanto: cannot reach without PASS or S_S_TICKET"))))))

(defn can-fix-power-plant? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :fix-power-plant)
    args
    (if (and (conditions-met :can-surf) (conditions-met :kanto))
      ;; FIXME: let players know that they can get the LOST_ITEM
      ;; any time after fixing power plant, regardless of talking
      ;; to copycat or already giving her the real LOST_ITEM
      (-> args
          (assoc :items-obtained (cset/union items-obtained (swaps :LOST_ITEM))
                 :conditions-met (conj conditions-met :fix-power-plant)))
      (-> args
          (assoc :reasons (conj reasons "fix-power-plant: cannot reach without being able to surf and having reached kanto"))))))

(defn can-get-copycat-item? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :copycat-item)
    args
    (if (and (conditions-met :kanto) (items-obtained :LOST_ITEM))
      (-> args
          (assoc :items-obtained (cset/union items-obtained (swaps :PASS)))
          (assoc :conditions-met (conj conditions-met :copycat-item)))
      (-> args
          (assoc :reasons (conj reasons "copycat-item: cannot reach without LOST_ITEM"))
          (assoc :conditions-met (conj conditions-met :copycat-item))))))

;;;;;;;;;;;;
;; badges ;;
;;;;;;;;;;;;

(defn can-satisfy-badge-condition? [{player-conditions-met :conditions-met 
                                     player-items-obtained :items-obtained
                                     badges :badges
                                     :as args}
                                    {:keys [badge conditions-met items-obtained]}]
  (let [conditions-satisfied? (every? player-conditions-met (or conditions-met #{}))
        items-satisfied? (every? player-items-obtained (or items-obtained #{}))
        satisfied? (and conditions-satisfied? items-satisfied?)]
    (if satisfied?
      (assoc args :badges (conj badges badge))
      args)))      

(defn can-collect-eight-badges? [args]
  (reduce can-satisfy-badge-condition?
          (assoc args :badges #{})
          badge-acquisition-conditions))

(defn beatable? [swaps]
  (let [initial-items (into #{} (get-swaps swaps guaranteed-items))
        early-linear-progression-result (->> {:swaps swaps :items-obtained initial-items}
                                             can-reach-goldenrod?
                                             can-reach-ecruteak?)]
    ;; we need to be strategic about further analysis, because
    ;; progression is necessarily nonlinear. try the remaining
    ;; functions in loop, breaking if there was no change after the
    ;; last round of checks.
    (loop [previous-result nil
           result early-linear-progression-result]
      (if (= (select-keys previous-result [:items-obtained :conditions-met])
             (select-keys result [:items-obtained :conditions-met]))
        result
        (recur result
               (->> result
                    can-surf?
                    can-reach-underground-warehouse?
                    can-defeat-team-rocket?
                    can-reach-kanto?
                    can-fix-power-plant?
                    can-get-copycat-item?))))))
