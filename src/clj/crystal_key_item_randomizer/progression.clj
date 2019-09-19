(ns crystal-key-item-randomizer.progression
  "The code in this namespace uses what we know about the game and how
  progress can be made, along with the particular items and progress
  the player has, to determine further progress."
  (:require [clojure.set :as cset]))

(def vanilla-swaps
  "A map of key items where the key and value are always equal."
  (zipmap crystal-key-item-randomizer.randomizer/all-items
          crystal-key-item-randomizer.randomizer/all-items))

(def guaranteed-items
  "Items that obtainable in every possible randomization."
  [:MYSTERY_EGG
   :HM_FLASH
   :OLD_ROD
   :HM_CUT])

(def goldenrod-items
  "Items that are obtainable if the player can arrive in Goldenrod."
  [:BICYCLE
   :BLUE_CARD
   :COIN_CASE
   :SQUIRTBOTTLE])

(def ecruteak-and-olivine-items
  "Items that are obtainable if the player can arrive in Ecruteak (e.g.,
  by defeating the Sudowoodo, or by train and boat via Kanto)."
  [:HM_SURF
   :ITEMFINDER
   :GOOD_ROD
   :HM_STRENGTH])

(def surf-required-items
  "Items that are unlocked merely by being able to surf."
  [:HM_FLY       ; Cianwood
   :SECRETPOTION ; Cianwood

   :RED_SCALE    ; Lake of Rage/Mahogany Rockets sidequest
   :HM_WHIRLPOOL ; Lake of Rage/Mahogany Rockets sidequest

   :BASEMENT_KEY ; Rockets in the Radio Tower

   :HM_WATERFALL]) ; Ice Path after defeating Pryce

(def item-conditions
  "A map from items to the conditions that must be satisfied in order
  for the player to obtain that item in the vanilla game.

  An empty array means that the item is obtainable without meeting any
  prerequisite.

  Any condition in the conditions array may be satisfied in order to
  obtain the item."
  ;; TODO: flip this so as to not repeat conditions so much.

  ;; "Describes the items that are obtained upon meeting some
  ;; conditions/obtaining an item/acquiring a badge in the vanilla
  ;; game."
  {:MYSTERY_EGG []
   :HM_FLASH []
   :OLD_ROD []
   :HM_CUT []

   ;; goldenrod
   :BICYCLE [{:conditions-met #{:goldenrod}}]
   :BLUE_CARD [{:conditions-met #{:goldenrod}}]
   :COIN_CASE [{:conditions-met #{:goldenrod}}]
   :SQUIRTBOTTLE [{:conditions-met #{:goldenrod}}]

   ;; ecruteak
   :ITEMFINDER [{:conditions-met #{:ecruteak}}]
   :HM_SURF [{:conditions-met #{:ecruteak}}]

   ;; olivine
   :GOOD_ROD [{:conditions-met #{:ecruteak}}]
   :HM_STRENGTH [{:conditions-met #{:ecruteak}}]

   ;; cianwood
   :HM_FLY [{:conditions-met #{:can-surf}}]
   :SECRETPOTION [{:conditions-met #{:can-surf}}]

   ;; mahogany
   :RED_SCALE [{:conditions-met #{:can-surf}}]
   :BASEMENT_KEY [{:conditions-met #{:can-surf}}]
   :HM_WHIRLPOOL [{:conditions-met #{:can-surf}}]

   ;; ice path
   :HM_WATERFALL [{:conditions-met #{:can-surf}}]

   :CARD_KEY [{:items-obtained #{:BASEMENT_KEY}}]
   :CLEAR_BELL [{:items-obtained #{:CARD_KEY}}]

   :SUPER_ROD [{:conditions-met #{:kanto}}]
   :MACHINE_PART [{:conditions-met #{:can-surf}}]
   :PASS [{:items-obtained #{:LOST_ITEM}}]
   :LOST_ITEM [{:conditions-met #{:fix-power-plant}}]
   :SILVER_WING [{:conditions-met #{:fix-power-plant}}]})

   ;; this item is not obtainable.
   ;; :S_S_TICKET [{:conditions-met #{:impossible}}]

(def badge-prereqs [{:badge :ZEPHYRBADGE}
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
                     :conditions-met #{:can-surf}}

                    {:badge :RISINGBADGE
                     :conditions-met #{:defeat-team-rocket}}
                    ;; the supernerd blocking the gym
                    ;; doesn't move until the player
                    ;; defeats Team Rocket

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
  (into #{} (-> swaps
                (select-keys originals)
                (vals))))

(defn can-reach-goldenrod? [{:keys [swaps items-obtained options] :as args}]
  ;; the cuttable tree in Ilex Forest is removed by the randomizer, so
  ;; goldenrod is always accessible
  (-> args
      (assoc :items-obtained (cset/union items-obtained (get-swaps swaps goldenrod-items)))
      (assoc :conditions-met #{:goldenrod})))

(defn can-reach-ecruteak-with-copycats-reward? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  ;; note, it's okay to get the Copycat's reward at any time because
  ;; we patch the game so that you can get tHE LOST_ITEM at any time
  ;; after fixing the Power Plant.
  (if (items-obtained :LOST_ITEM)
    (let [pass-swap (swaps :PASS)
          items-obtained' (conj items-obtained pass-swap)]
      (if (contains? #{:SQUIRTBOTTLE :S_S_TICKET} pass-swap)
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
    (can-reach-ecruteak-with-copycats-reward? {:swaps swaps
                                               :items-obtained items-obtained
                                               :conditions-met conditions-met})))

(defn can-reach-ecruteak? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (items-obtained :SQUIRTBOTTLE)
    {:swaps swaps
     :items-obtained (cset/union items-obtained (get-swaps swaps ecruteak-and-olivine-items))
     :conditions-met (conj conditions-met :ecruteak)}
    (if (and (conditions-met :goldenrod) (items-obtained :PASS))
      (let [result (can-reach-ecruteak-via-saffron-detour? {:swaps swaps
                                                            :items-obtained (conj items-obtained (swaps :SUPER_ROD))
                                                            :conditions-met (conj conditions-met :kanto)})]
        (if (contains? (result :conditions-met) :ecruteak)
          (assoc result :items-obtained (cset/union (result :items-obtained)
                                                    (get-swaps swaps ecruteak-and-olivine-items)))
          result))
      (assoc args :reasons (conj reasons "ecruteak: cannot reach without PASS or SQUIRTBOTTLE")))))

(defn can-surf? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :can-surf)
    args
    (if (and (conditions-met :ecruteak)
             (items-obtained :HM_SURF))
      (-> args
          (assoc :items-obtained (cset/union items-obtained (get-swaps swaps surf-required-items)))
          (assoc :conditions-met (conj conditions-met :can-surf)))
      args)))

(defn can-reach-underground-warehouse? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  ;; FIXME: warn players to avoid the underground warehouse until
  ;; they've defeated the Mahogany Rockets, even if they have the
  ;; BASEMENT_KEY

  ;; it'd be great if we could put an NPC into the game to block the
  ;; player.
  (if (conditions-met :underground-warehouse)
    args
    ;; FIXME: the player is probably screwed if their 7th badge isn't
    ;; one of the first 7 johto badges.
    (if (not (<= 7 (count badges)))
      (assoc args :reasons
             (conj reasons "underground-warehouse: cannot reach without having at least 7 badges"))
      (if (items-obtained :BASEMENT_KEY)
        (-> args
            (assoc :items-obtained (conj items-obtained (swaps :CARD_KEY)))
            (assoc :conditions-met (conj conditions-met :underground-warehouse)))
        (assoc args :reasons
               (conj reasons "underground-warehouse: cannot reach without BASEMENT_KEY"))))))

(defn can-defeat-team-rocket? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  ;; i'm thinking that you can do this gym as long as you've got 7
  ;; badges and have the CARD KEY. after that, there are two cases:

  ;; * Goldenrod Underground item is vanilla (BASEMENT_KEY)

  ;; * the player (should have) gotten the BASEMENT_KEY already

  (if (conditions-met :defeat-team-rocket)
    args
    (if (and (items-obtained :CARD_KEY) (<= 7 (count badges)))
      (assoc args :reasons
             (conj reasons "defeat-team-rocket: cannot reach without having obtained CARD_KEY and at least 7 badges"))
      ;; the player CANNOT defeat team rocket before getting the
      ;; CARD_KEY, because they'd never be able to get the CARD_KEY if
      ;; they did. however, this doesn't matter if the CARD_KEY isn't
      ;; required. (see GoldenrodUndergroundWarehouseDirectorScript,
      ;; EVENT_RADIO_TOWER_ROCKET_TAKEOVER in the ASM)
      (if (or (items-obtained :BASEMENT_KEY)
              ;; TODO: this next line a bit of a reach goal.
              ;; (not (basement-key-required? sawps))
              (= :BASEMENT_KEY (swaps :BASEMENT_KEY)))
        (-> args
            (assoc :items-obtained (conj items-obtained (swaps :CLEAR_BELL)))
            (assoc :conditions-met (conj conditions-met :defeat-team-rocket)))
        (assoc args :reasons
               (conj reasons "defeat-team-rocket: cannot reach without being guaranteed the BASEMENT_KEY"))))))

(defn can-reach-kanto? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :kanto)
    args
    (let [kanto-via-train? (and (conditions-met :goldenrod)
                                (items-obtained :PASS))
          kanto-via-boat? (and (conditions-met :ecruteak)
                               (items-obtained :S_S_TICKET))]
      (if (or kanto-via-train? kanto-via-boat?)
        (-> args
            (assoc :items-obtained (cset/union items-obtained
                                               (get-swaps swaps [:SUPER_ROD :MACHINE_PART])))
            (assoc :conditions-met (conj conditions-met :kanto)))
        (assoc args :reasons (conj reasons "kanto: cannot reach without PASS or S_S_TICKET"))))))

(defn can-fix-power-plant? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :fix-power-plant)
    args
    (if (and (conditions-met :can-surf) (conditions-met :kanto))
      (-> args
          (assoc :items-obtained (cset/union items-obtained
                                             (get-swaps swaps [:MACHINE_PART :LOST_ITEM :SILVER_WING]))
                 :conditions-met (conj conditions-met :fix-power-plant)))
      (-> args
          (assoc :reasons (conj reasons "fix-power-plant: cannot reach without being able to surf and having reached kanto"))))))

(defn can-get-copycat-item? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :copycat-item)
    args
    (if (and (conditions-met :kanto) (items-obtained :LOST_ITEM))
      (-> args
          (assoc :items-obtained (conj items-obtained (swaps :PASS)))
          (assoc :conditions-met (conj conditions-met :copycat-item)))
      (-> args
          (assoc :reasons (conj reasons "copycat-item: cannot reach without LOST_ITEM"))
          (assoc :conditions-met (conj conditions-met :copycat-item))))))

;;;;;;;;;;;;
;; badges ;;
;;;;;;;;;;;;

(defn can-satisfy-badge-prereq? [{player-conditions-met :conditions-met
                                  player-items-obtained :items-obtained
                                  badges :badges
                                  :as args}
                                 {:keys [badge conditions-met items-obtained]}]
  (if (badges badge)
    args
    (let [conditions-satisfied? (every? player-conditions-met (or conditions-met #{}))
          items-satisfied? (every? player-items-obtained (or items-obtained #{}))
          satisfied? (and conditions-satisfied? items-satisfied?)]
      (if satisfied?
        (assoc args :badges (conj badges badge))
        args))))

(defn can-collect-badges? [args]
  (reduce can-satisfy-badge-prereq?
          (assoc args :badges #{})
          badge-prereqs))

(defn beatable? [swaps]
  (let [initial-items (into #{} (get-swaps swaps guaranteed-items))
        early-linear-progression-result (->> {:swaps swaps :items-obtained initial-items}
                                             can-reach-goldenrod?
                                             can-reach-ecruteak?)]
    (if (not (-> early-linear-progression-result :conditions-met :ecruteak))
      (assoc early-linear-progression-result :beatable? false)
      (let [;; we need to be strategic about further analysis, because
            ;; progression is necessarily nonlinear. try the remaining
            ;; functions in loop, breaking if there was no change after the
            ;; last round of checks.
            final-progression-result (loop [previous-result nil
                                            result early-linear-progression-result]
                                       (if (= (select-keys previous-result
                                                           [:items-obtained :conditions-met :badges])
                                              (select-keys result
                                                           [:items-obtained :conditions-met :badges]))
                                         result
                                         (recur result
                                                (->> result
                                                     can-collect-badges?
                                                     can-surf?
                                                     can-reach-underground-warehouse?
                                                     can-defeat-team-rocket?
                                                     can-reach-kanto?
                                                     can-fix-power-plant?
                                                     can-get-copycat-item?))))
            badge-count (->> final-progression-result
                             :badges
                             count)]
        (assoc final-progression-result :beatable? (>= badge-count 8))))))

(comment
  ;; TODO: try to implement these?
  (defn basement-key-required? [swaps]
    (let [goldenrod-underground-item (swaps :CARD_KEY)]
      (required? goldenrod-underground-item)))

  (defn required? [swaps item]
    (cond (required-items item) true
          (non-required-items item) false
          ;; we know it's maybe-required preerq.
          (reduce (fn [required?' [prereq maybe-required-orig]]
                    (if (not= prereq item)
                      (or required?' false)
                      ;; this is the hard part.
                      false))
                  false
                  maybe-reqiured-items))))
