(ns crystal-key-item-randomizer.seeds
  (:use [crystal-key-item-randomizer.randomizer :only [all-items]]
        [crystal-key-item-randomizer.progression :only [beatable?]]))

(defn deterministic-shuffle [^java.util.Collection coll seed]
  (let [al (java.util.ArrayList. coll)
        rng (java.util.Random. seed)]
    (java.util.Collections/shuffle al rng)
    (clojure.lang.RT/vector (.toArray al))))

(defn generate [seed]
  (let [swaps (zipmap all-items
                      (deterministic-shuffle all-items seed))
        progression-results (beatable? swaps)]
    (if (progression-results :beatable?)
      (assoc progression-results :seed-id seed)
      ;; TODO: handle this error
      {:seed-id seed :error "not beatable"})))
