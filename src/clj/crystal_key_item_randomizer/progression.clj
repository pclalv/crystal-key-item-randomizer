(ns crystal-key-item-randomizer.progression
  "Uses what we know about the game and how progress can be made, along
  with the particular items and progress the player has, to determine
  further progress, and ultimately whether an endgame conditon can be
  met."
  (:require [clojure.set :as cset]
            [crystal-key-item-randomizer.logic :refer :all]
            [clojure.spec.alpha :as s]))

(defn any? [pred col]
  (not (not-any? pred col)))

(defn get-swaps [swaps originals]
  (into #{} (-> swaps
                (select-keys originals)
                (vals))))

;;;;;;;;;;;;
;; badges ;;
;;;;;;;;;;;;

(defn can-satisfy-badge-prereq? [badge-swaps
                                 {player-conditions-met :conditions-met
                                  player-items-obtained :items-obtained
                                  player-badges :badges
                                  :as args}
                                 {:keys [badge conditions-met items-obtained]}]
  (let [badge-to-grant (badge-swaps badge)]
    (if (player-badges badge-to-grant)
      args
      (let [conditions-satisfied? (every? player-conditions-met (or conditions-met #{}))
            items-satisfied? (every? player-items-obtained (or items-obtained #{}))
            satisfied? (and conditions-satisfied? items-satisfied?)]
        (if satisfied?
          (assoc args :badges (conj player-badges badge-to-grant))
          args)))))

(defn analyze-badges [result badge-swaps]
  (reduce (partial can-satisfy-badge-prereq? badge-swaps)
          result
          (badge-prereqs {})))

;;;;;;;;;;;
;; items ;;
;;;;;;;;;;;

(defn can-satisfy-item-prereqs? [swaps
                                 {player-badges :badges
                                  player-conditions-met :conditions-met
                                  player-items-obtained :items-obtained
                                  :as args}
                                 {:keys [badges conditions-met items-obtained grants] :or {badges #{}
                                                                                           conditions-met #{}
                                                                                           items-obtained #{}}}]
  (let [badges-satisfied? (every? player-badges
                                  (or badges #{}))
        conditions-satisfied? (every? player-conditions-met
                                      (or conditions-met #{}))
        items-satisfied? (every? player-items-obtained
                                 (or items-obtained #{}))
        satisfied? (and badges-satisfied?
                        conditions-satisfied?
                        items-satisfied?)]
    (if satisfied?
      (assoc args :items-obtained (cset/union player-items-obtained
                                              (get-swaps swaps grants)))
      args)))

(defn analyze-items [result swaps copycat-item {:keys [rockets] :as seed-options}]
  (reduce (partial can-satisfy-item-prereqs? swaps)
          result
          (item-prereqs {:copycat-item copycat-item :rockets rockets})))

;;;;;;;;;;;;;;;;
;; conditions ;;
;;;;;;;;;;;;;;;;

(defn can-satisfy-condition-prereqs? [{player-conditions-met :conditions-met
                                       player-items-obtained :items-obtained
                                       player-pokegear-cards :pokegear-cards
                                       :as result}
                                      {condition :condition
                                       {:keys [conditions-met items-obtained pokegear-cards]} :prereqs}]
  (if (player-conditions-met condition)
    result
    (let [conditions-satisfied? (every? player-conditions-met
                                        (or conditions-met #{}))
          items-satisfied? (every? player-items-obtained
                                   (or items-obtained #{}))
          pokegear-cards-satisfied? (every? player-pokegear-cards
                                            (or pokegear-cards #{}))
          satisfied? (and conditions-satisfied?
                          items-satisfied?
                          pokegear-cards-satisfied?)]
      (if satisfied?
        (assoc result :conditions-met (conj player-conditions-met condition))
        result))))

(defn analyze-conditions [result condition-options]
  (reduce can-satisfy-condition-prereqs?
          result
          (condition-prereqs condition-options)))

;;;;;;;;;;;;;;;;;
;; badge count ;;
;;;;;;;;;;;;;;;;;

(defn analyze-badge-count [{:keys [badges conditions-met] :as result}]
  (let [badge-count (count badges)]
    (cond (= badge-count 16)
          #_=> (assoc result :conditions-met (cset/union conditions-met
                                                         #{:four-badges :seven-badges :eight-badges :sixteen-badges}))

          (>= badge-count 8)
          #_=> (assoc result :conditions-met (cset/union conditions-met
                                                         #{:four-badges :seven-badges :eight-badges}))

          (>= badge-count 7)
          #_=> (assoc result :conditions-met (cset/union conditions-met
                                                         #{:four-badges :seven-badges}))

          ;; for early-rockets
          (>= badge-count 4)
          #_=> (assoc result :conditions-met (cset/union conditions-met
                                                         #{:four-badges}))

          :else
          #_=> result)))

;;;;;;;;;;;;
;; HM use ;;
;;;;;;;;;;;;

(defn can-satisfy-hm-use-prereq? [{player-conditions-met :conditions-met
                                   player-items-obtained :items-obtained
                                   player-badges :badges
                                   :as result}
                                  {condition :condition
                                   {:keys [badges items-obtained]} :prereqs}]
  (let [badges-satisfied? (every? player-badges
                                  (or badges #{}))
        items-satisfied? (every? player-items-obtained
                                 (or items-obtained #{}))
        satisfied? (and badges-satisfied?
                        items-satisfied?)]
    (if satisfied?
      (assoc result :conditions-met (conj player-conditions-met condition))
      result)))

(defn analyze-hm-use [result]
  (reduce can-satisfy-hm-use-prereq?
          result
          hm-use-prereqs))

;;;;;;;;;;;;;;;;;;;;
;; pokegear cards ;;
;;;;;;;;;;;;;;;;;;;;

(defn satisfies-pokegear-card-prereq? [{player-conditions-met :conditions-met
                                        pokegear-cards :pokegear-cards
                                        :as result}
                                       {pokegear-card :pokegear-card
                                        {conditions-met :conditions-met} :prereqs}]
  (let [conditions-satisfied? (every? player-conditions-met
                                      (or conditions-met #{}))]
    (if (not conditions-satisfied?)
      result
      (assoc result :pokegear-cards (conj pokegear-cards pokegear-card)))))

(defn analyze-pokegear-cards [result]
  (reduce satisfies-pokegear-card-prereq?
          result
          pokegear-card-prereqs))

(defn analyze [result {:keys [item-swaps badge-swaps copycat-item]} {:keys [condition-options]}]
  (-> result
      (analyze-items item-swaps copycat-item condition-options)
      (analyze-conditions condition-options)
      (analyze-badges badge-swaps) 
      analyze-badge-count
      analyze-hm-use
      analyze-pokegear-cards))

(defn beatable?
  ([swaps]
   (beatable? swaps {}))
  ([swaps {:keys [endgame-condition speedchoice? rockets]
           :or {speedchoice? true
                endgame-condition :defeat-red
                rockets :normal}}]
   (if (not speedchoice?)
     {:beatable? false
      :error "only speedchoice is currently supported."}
     (let [condition-options {:rockets rockets
                              :no-blind-rock-tunnel? true}
           result (loop [previous-result {}
                         result (analyze {:items-obtained #{}
                                          :conditions-met #{}
                                          :badges #{}
                                          :pokegear-cards #{}}
                                         swaps
                                         {:condition-options condition-options})]
                    (if (= (select-keys previous-result [:items-obtained :conditions-met :badges])
                           (select-keys result [:items-obtained :conditions-met :badges]))
                      result
                      (recur result
                             (analyze result swaps {:condition-options condition-options}))))]
       (-> (conj result swaps)
           (assoc :beatable? (contains? (result :conditions-met) endgame-condition)))))))

(s/def ::conditions-met
  (s/coll-of keyword? :kind set?))
(s/def ::badges
  (s/coll-of keyword? :kind set?))
(s/def ::items-obtained
  (s/coll-of keyword? :kind set?))
(s/def ::swaps (s/keys :req-in [::item-swaps ::badge-swaps]))
(s/def ::beatable? boolean?)
(s/def ::progression-result
  (s/keys :req-un [::beatable? ::swaps ::items-obtained ::conditions-met ::badges]))

(s/fdef beatable?
  ;; :args (s/cat :just-an-int int?)
  :ret ::progression-result)
