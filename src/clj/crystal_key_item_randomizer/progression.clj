(ns crystal-key-item-randomizer.progression
  "The code in this namespace uses what we know about the game and how
  progress can be made, along with the particular items and progress
  the player has, to determine further progress."
  (:require [clojure.set :as cset]
            [crystal-key-item-randomizer.conditions :refer :all]))

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
  [;; Cianwood
   :SECRETPOTION

   ;; Lake of Rage/Mahogany Rockets sidequest
   :RED_SCALE    
   :HM_WHIRLPOOL])

(defn any? [pred col]
  (not (not-any? pred col)))

(defn get-swaps [swaps originals]
  (into #{} (-> swaps
                (select-keys originals)
                (vals))))

(defn can-reach-goldenrod? [{:keys [swaps items-obtained conditions-met] :as args}]
  ;; the cuttable tree in Ilex Forest is removed by the randomizer, so
  ;; goldenrod is always accessible
  (-> args
      (assoc :items-obtained (cset/union items-obtained (get-swaps swaps goldenrod-items))
             :conditions-met (conj conditions-met :goldenrod))))

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

(defn can-cut? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :can-cut) args
        (and (items-obtained :HM_CUT)
             (badges :HIVEBADGE)) (assoc args :conditions-met (conj conditions-met :can-cut))
        :else (assoc args :reasons
                     (conj reasons "can-cut: cannot without HM_CUT and HIVEBADGE"))))

(defn can-strength? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :can-strength) args
        (and (items-obtained :HM_STRENGTH)
             (badges :PLAINBADGE)) (assoc args :conditions-met (conj conditions-met :can-strength))
        :else (assoc args :reasons
                     (conj reasons "can-strength: cannot without both PLAINBADGE and HM_STRENGTH"))))

(defn can-surf? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :can-surf) args
        (and (badges :FOGBADGE)
             (items-obtained :HM_SURF)) (assoc args :conditions-met (conj conditions-met :can-surf))
        :else (assoc args :reasons
                     (conj reasons "can-surf: cannot without both FOGBADGE and HM_SURF"))))

(defn can-whirlpool? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :can-whirlpool) args
        (and (badges :GLACIERBADGE)
             (items-obtained :HM_WHIRLPOOL)) (assoc args :conditions-met
                                                    (conj conditions-met :can-whirlpool))
        :else (assoc args :reasons
                     (conj reasons "can-whirlpool: cannot without both GLACIERBADGE and HM_WHIRLPOOL"))))

(defn can-waterfall? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :can-waterfall) args
        (and (badges :RISINGBADGE)
             (items-obtained :HM_WATERFALL)) (assoc args :conditions-met
                                                    (conj conditions-met :can-waterfall))
        :else (assoc args :reasons
                     (conj reasons "can-waterfall: cannot without both RISINGBADGE and HM_WATERFALL"))))

(defn can-get-chucks-wifes-item?[{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (items-obtained (swaps :HM_FLY)) args
        ;; can-surf currently implies that the player can reach
        ;; ecruteak, but that would not be guaranteed if badges were
        ;; randomized.
        (and (conditions-met :ecruteak)
             (conditions-met :can-surf)
             (conditions-met :can-strength)) (assoc args :items-obtained (conj items-obtained (swaps :HM_FLY)))
        :else (assoc args :reasons
                     (conj reasons "HM_FLY: cannot obtained with out being able to surf and being able to strength"))))

(defn has-seven-badges? [badges]
  (<= 7 (count badges)))

(defn can-defeat-red-gyarados? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :defeat-red-gyarados) args
        (and (conditions-met :can-surf)
             (conditions-met :ecruteak)) (assoc args
                                                :items-obtained (cset/union items-obtained (get-swaps swaps surf-required-items))
                                                :conditions-met (conj conditions-met :defeat-red-gyarados))
        :else (assoc args :reasons
                     (conj reasons "defeat-red-gyarados: cannot without both surf and reaching ecruteak"))))

(defn can-get-ice-path-item? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (items-obtained (swaps :HM_WATERFALL)) args
        (and (conditions-met :trigger-radio-tower-takeover)
             (conditions-met :ecruteak)) (assoc args :items-obtained (conj items-obtained (swaps :HM_WATERFALL)))
        :else (assoc args :reasons
                     (conj reasons "ice-path-item: cannot obtain without 7 badges and reaching ecruteak"))))

(defn can-trigger-radio-tower-takeover? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :trigger-radio-tower-takeover) args
        (has-seven-badges? badges) (-> args
                                       (assoc :items-obtained (conj items-obtained (swaps :BASEMENT_KEY))
                                              :conditions-met (conj conditions-met :trigger-radio-tower-takeover)))
        :else (assoc args :reasons
                     (conj reasons "trigger-radio-tower-takeover: cannot reach without 7 badges"))))

(defn can-reach-underground-warehouse? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :underground-warehouse) args
        (not (conditions-met :trigger-radio-tower-takeover)) (assoc args :reasons
                                                                    (conj reasons "underground-warehouse: cannot reach without having triggered radio tower takeover"))
        (not (items-obtained :BASEMENT_KEY)) (assoc args :reasons
                                                    (conj reasons "underground-warehouse: cannot reach without BASEMENT_KEY"))
        (and (conditions-met :trigger-radio-tower-takeover)
             (items-obtained :BASEMENT_KEY)) (-> args
                                                 (assoc :items-obtained (conj items-obtained (swaps :CARD_KEY))
                                                        :conditions-met (conj conditions-met :underground-warehouse)))))

(defn can-defeat-team-rocket? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :defeat-team-rocket) args
        (and (items-obtained :CARD_KEY)
             (conditions-met :trigger-radio-tower-takeover)) (-> args
                                                                 (assoc :items-obtained (conj items-obtained (swaps :CLEAR_BELL))
                                                                        :conditions-met (conj conditions-met :defeat-team-rocket)))
        :else (assoc args :reasons
                     (conj reasons "defeat-team-rocket: cannot reach without having triggered the radio tower takeover and having obtained CARD_KEY"))))

(defn can-reach-blackthorn? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :blackthorn) args
        (and (conditions-met :ecruteak)
             (conditions-met :can-strength)
             (conditions-met :trigger-radio-tower-takeover)) (assoc args :conditions-met (conj conditions-met :blackthorn))
        :else (assoc args :reasons
                     (conj reasons "blackthorn: cannot reach without having defeated team rocket and being able to use strength"))))

(defn can-reach-kanto? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (if (conditions-met :kanto)
    args
    (let [kanto-via-train? (and (conditions-met :goldenrod)
                                (items-obtained :PASS))
          kanto-via-boat? (and (conditions-met :ecruteak)
                               (items-obtained :S_S_TICKET))]
      (if (or kanto-via-train? kanto-via-boat?)
        (-> args (assoc :items-obtained (conj items-obtained (swaps :SUPER_ROD))
                        :conditions-met (conj conditions-met :kanto)))
        (assoc args :reasons (conj reasons "kanto: cannot reach without PASS or S_S_TICKET"))))))

(defn can-talk-to-power-plant-manager? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (cond (conditions-met :talk-to-power-plant-manager) args
        (and (conditions-met :can-surf)
             (conditions-met :kanto)) (-> args (assoc :items-obtained (conj items-obtained (swaps :MACHINE_PART))
                                                      :conditions-met (conj conditions-met :talk-to-power-plant-manager)))
        :else (assoc args :reasons
                     (conj reasons "talk-to-power-plant-manager: cannot reach without being able to surf and without having reached kanto"))))

(defn can-fix-power-plant? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (cond (conditions-met :fix-power-plant) args
        (and (items-obtained :MACHINE_PART)
             (conditions-met :talk-to-power-plant-manager)) (-> args (assoc :items-obtained (conj items-obtained :LOST_ITEM)
                                                                            :conditions-met (conj conditions-met :fix-power-plant)))
        :else (assoc args :reasons
                     (conj reasons "fix-power-plant: cannot reach without having MACHINE_PART and being able to talk to the Power Plant manager"))))

(defn can-reach-pewter? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (cond (conditions-met :pewter) args
        (and (conditions-met :can-cut)
             (conditions-met :fix-power-plant)) (assoc args
                                                       :conditions-met (conj conditions-met :pewter)
                                                       :items-obtained (conj items-obtained :SILVER_WING))
        :else (assoc args :reasons
                     (conj reasons "pewter: cannot reach obtain without having fixed power plant and being able to use cut"))))

(defn can-get-copycat-item? [{:keys [swaps items-obtained conditions-met reasons] :as args}]
  (cond (conditions-met :copycat-item) args
        (and (conditions-met :kanto)
             (items-obtained :LOST_ITEM)) (-> args
                                              (assoc :items-obtained (conj items-obtained (swaps :PASS))
                                                     :conditions-met (conj conditions-met :copycat-item)))
        :else (assoc args :reasons
                     (conj reasons "copycat-item: cannot reach without LOST_ITEM"))))

(defn can-defeat-elite-4? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :defeat-elite-4) args
        (and (>= (count badges) 8) (or ;; via Viridian
                                    (and (conditions-met :fix-power-plant)
                                         (conditions-met :can-cut))
                                    ;; via Tohjo Falls
                                    (and (conditions-met :can-surf)
                                         (conditions-met :can-waterfall)))) (-> args
                                                                                (assoc :items-obtained
                                                                                       (conj items-obtained (swaps :S_S_TICKET)))
                                                                                (assoc :conditions-met
                                                                                       (conj conditions-met :defeat-elite-4)))
        :else (assoc args :reasons
                     (conj reasons "defeat-elite-4: can't without Victory Road access via Viridian or Tohjo Falls"))))

(defn can-defeat-red? [{:keys [swaps items-obtained conditions-met badges reasons] :as args}]
  (cond (conditions-met :defeat-red) args
        ;; is this check too lazy?
        (= (count badges) 16) (assoc args :conditions-met (conj conditions-met :defeat-red))
        :else (assoc args :reasons (conj reasons "defeat-red: cannot without 16 badges"))))

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

(defn beatable? [swaps {:keys [speedchoice?]}]
  (let [initial-items (get-swaps swaps guaranteed-items)
        ;; in vanilla, the player needs whirlpool only to get the
        ;; RISINGBADGE.  in speedchoice, the player doesn't need
        ;; whirlpool at all.  so that we don't have to make more
        ;; complex logical changes to account for this minor
        ;; difference, just asusme that the user can-whirlpool from
        ;; the get-go if we're dealing with speedchoice.
        initial-conditions (if speedchoice? #{:can-whirlpool} #{})
        early-linear-progression-result (->> {:swaps swaps :items-obtained initial-items :conditions-met initial-conditions}
                                             can-reach-goldenrod?
                                             can-reach-ecruteak?)]
    (if (not (-> early-linear-progression-result :conditions-met :ecruteak))
      (assoc early-linear-progression-result :beatable? false)
      (let [;; we need to be strategic about further analysis, because
            ;; progression is necessarily nonlinear. try the remaining
            ;; functions in loop, breaking if there was no change
            ;; after the last round of checks.
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
                                                     can-cut?
                                                     can-strength?
                                                     can-surf?
                                                     can-whirlpool?
                                                     can-waterfall?
                                                     can-defeat-red-gyarados?
                                                     can-get-chucks-wifes-item?
                                                     can-trigger-radio-tower-takeover?
                                                     can-get-ice-path-item?
                                                     can-reach-underground-warehouse?
                                                     can-defeat-team-rocket?
                                                     can-reach-blackthorn?
                                                     can-reach-kanto?
                                                     can-talk-to-power-plant-manager?
                                                     can-fix-power-plant?
                                                     can-get-copycat-item?
                                                     can-reach-pewter?
                                                     can-defeat-elite-4?
                                                     can-defeat-red?))))]
        (assoc final-progression-result :beatable? (if (not speedchoice?)
                                                     ;; there's some not-straightforward stuff we'd need to do to
                                                     ;; support vanilla. we'd need to either change the logic around
                                                     ;; collecting 7 badges, or otherwise patch the rom with new code
                                                     ;; so taht the Team Rocket Radio Tower takeover can be activated
                                                     ;; after collecting any 7 badges.
                                                     false
                                                     (contains? (final-progression-result :conditions-met) :defeat-red)))))))
