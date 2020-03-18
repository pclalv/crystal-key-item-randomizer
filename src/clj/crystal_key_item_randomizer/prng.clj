(ns crystal-key-item-randomizer.prng
  "pseudorandom")

;; TODO: reverse the argument order
(defn deterministic-shuffle [^java.util.Collection coll seed]
  (let [al (java.util.ArrayList. coll)
        rng (java.util.Random. seed)]
    (java.util.Collections/shuffle al rng)
    (clojure.lang.RT/vector (.toArray al))))

;; TODO: reverse the argument order
(defn deterministic-pick [coll seed]
  (first (deterministic-shuffle coll seed)))
