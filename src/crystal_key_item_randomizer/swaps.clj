(ns crystal-key-item-randomizer.swaps
  (:use [crystal-key-item-randomizer.randomizer :only [all-items]]
        [crystal-key-item-randomizer.progression :only [beatable?]]))

(defn generate []
  (let [swaps (zipmap all-items
                      (shuffle all-items))]
    (if (beatable? swaps)
      swaps
      (recur))))
