(ns crystal-key-item-randomizer.patches.text)

(defn pad [n val coll]
  (take n (concat coll (repeat val))))
