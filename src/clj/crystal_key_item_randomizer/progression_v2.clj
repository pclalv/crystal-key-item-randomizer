(ns crystal-key-item-randomizer.progression-v2
  "The code in this namespace uses what we know about the game and how
  progress can be made, along with the particular items and progress
  the player has, to determine further progress."
  (:require [clojure.set :as cset]
            [crystal-key-item-randomizer.conditions :refer :all]))

(defn any? [pred col]
  (not (not-any? pred col)))

(defn get-swaps [swaps originals]
  (into #{} (-> swaps
                (select-keys originals)
                (vals))))

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

(defn analyze-badges [result]
  (reduce can-satisfy-badge-prereq?
          result
          badge-prereqs))

;;;;;;;;;;;
;; items ;;
;;;;;;;;;;;

(defn can-satisfy-item-prereqs? [swaps
                                 {player-conditions-met :conditions-met
                                  player-items-obtained :items-obtained
                                  player-badges :badges
                                  :as args}
                                 {:keys [badge-count conditions-met items-obtained grants]}]
  (let [badge-count-satisfied (>= (count player-badges)
                                  badge-count)
        conditions-satisfied? (every? player-conditions-met
                                      (or conditions-met #{}))
        items-satisfied? (every? player-items-obtained
                                 (or items-obtained #{}))
        satisfied? (and badge-count-satisfied
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

(defn can-satisfy-condition-prereq? [{player-conditions-met :conditions-met
                                      player-items-obtained :items-obtained
                                      player-badges :badges
                                      :as args}
                                     {:keys [badge-count badges conditions-met items-obtained]}]
  (let [badge-count-satisfied (>= (count player-badges)
                                  badge-count)
        badges-satisfied? (every? player-badges
                                  (or badges #{}))
        conditions-satisfied? (every? player-conditions-met
                                      (or conditions-met #{}))
        items-satisfied? (every? player-items-obtained
                                 (or items-obtained #{}))]
    (and badge-count-satisfied
         badges-satisfied?
         conditions-satisfied?
         items-satisfied?)))

(defn can-satisfy-condition-prereqs? [{player-conditions-met :conditions-met :as result}
                                      {:keys [condition prereqs]}]
  (if (player-conditions-met condition)
    result
    (if (any? #(can-satisfy-condition-prereq? result %) prereqs)
      (assoc result :conditions-met (conj player-conditions-met condition))
      result)))

(defn analyze-conditions [result]
  (reduce can-satisfy-condition-prereqs?
          result
          condition-prereqs))

(defn analyze [result swaps]
  (-> result
      analyze-badges
      analyze-conditions
      (analyze-items swaps)))

(defn beatable? [swaps {:keys [speedchoice?] :or {speedchoice? true}}]
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
      (assoc result :beatable? (contains? (result :conditions-met) :defeat-red)))))
