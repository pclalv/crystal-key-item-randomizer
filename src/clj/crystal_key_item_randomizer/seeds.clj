(ns crystal-key-item-randomizer.seeds
  (:require [crystal-key-item-randomizer.patches :as patches])
  (:use [crystal-key-item-randomizer.randomizer :only [all-items]]
        [crystal-key-item-randomizer.progression :only [beatable?]]))

(defn deterministic-shuffle [^java.util.Collection coll seed]
  (let [al (java.util.ArrayList. coll)
        rng (java.util.Random. seed)]
    (java.util.Collections/shuffle al rng)
    (clojure.lang.RT/vector (.toArray al))))

(defn generate [seed-id {:keys [speedchoice?]}]
  (let [swaps (zipmap all-items
                      (deterministic-shuffle all-items seed-id))
        progression-results (beatable? swaps {:speedchoice? speedchoice?})]
    (if (progression-results :beatable?)
      {:seed (-> progression-results
                 (assoc :patches (patches/generate swaps))
                 (assoc :id seed-id))}
      {:error (str "Unbeatable seed: " seed-id)})))

(defn generate-beatable [opts]
  (loop [seed-id (-> (new java.util.Random
                          .nextLong
                          java.lang.Math/abs))
         {:keys [seed error]} (generate seed-id opts)]
    (if error
      (recur)
      {:seed seed})))
