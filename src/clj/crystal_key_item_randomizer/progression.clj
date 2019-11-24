(ns crystal-key-item-randomizer.progression
  "The code in this namespace uses what we know about the game and how
  progress can be made, along with the particular items and progress
  the player has, to determine further progress."
  (:require [clojure.set :as cset]
            [crystal-key-item-randomizer.logic :refer :all]))

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
          (badge-prereqs)))

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

(defn analyze-items [result swaps]
  (reduce (partial can-satisfy-item-prereqs? swaps)
          result
          item-prereqs))

;;;;;;;;;;;;;;;;
;; conditions ;;
;;;;;;;;;;;;;;;;

(defn can-satisfy-condition-prereqs? [{player-conditions-met :conditions-met
                                       player-items-obtained :items-obtained
                                       :as result}
                                      {condition :condition
                                       {:keys [conditions-met items-obtained]} :prereqs}]
  (if (player-conditions-met condition)
    result
    (let [conditions-satisfied? (every? player-conditions-met
                                        (or conditions-met #{}))
          items-satisfied? (every? player-items-obtained
                                   (or items-obtained #{}))
          satisfied? (and conditions-satisfied?
                          items-satisfied?)]
      (if satisfied?
        (assoc result :conditions-met (conj player-conditions-met condition))
        result))))

(defn analyze-conditions [result]
  (reduce can-satisfy-condition-prereqs?
          result
          (condition-prereqs)))

;;;;;;;;;;;;;;;;;
;; badge count ;;
;;;;;;;;;;;;;;;;;

(defn analyze-badge-count [{:keys [badges conditions-met] :as result}]
  (let [badge-count (count badges)]
    (cond (= badge-count 16) (assoc result :conditions-met (cset/union conditions-met #{:seven-badges :eight-badges :sixteen-badges}))
          (>= badge-count 8) (assoc result :conditions-met (cset/union conditions-met #{:seven-badges :eight-badges}))
          (>= badge-count 7) (assoc result :conditions-met (cset/union conditions-met #{:seven-badges}))
          :else result)))

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

(defn analyze [result {:keys [item-swaps badge-swaps]}]
  (-> result
      (analyze-items item-swaps)
      analyze-conditions
      (analyze-badges badge-swaps) 
      analyze-badge-count
      analyze-hm-use))

(defn beatable?
  ([swaps]
   (beatable? swaps {:speedchoice? true}))
  ([swaps
    {:keys [goal-condition speedchoice?] :or {speedchoice? true goal-condition :defeat-red}}]
   (if (not speedchoice?)
     {:beatable? false
      :error "only speedchoice is currently supported."}
     (let [result (loop [previous-result {}
                         result (analyze {:items-obtained #{}
                                          :conditions-met #{}
                                          :badges #{}}
                                         swaps)]
                    (if (= (select-keys previous-result [:items-obtained :conditions-met :badges])
                           (select-keys result [:items-obtained :conditions-met :badges]))
                      result
                      (recur result
                             (analyze result swaps))))]
       (-> (conj result swaps)
           (assoc :beatable? (contains? (result :conditions-met) goal-condition)))))))
