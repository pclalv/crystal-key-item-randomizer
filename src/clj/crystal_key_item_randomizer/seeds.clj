(ns crystal-key-item-randomizer.seeds
  (:require [crystal-key-item-randomizer.patches :as patches])
  (:use [crystal-key-item-randomizer.randomizer :only [all-items]]
        [crystal-key-item-randomizer.progression :only [beatable?]]))

(defn deterministic-shuffle [^java.util.Collection coll seed]
  (let [al (java.util.ArrayList. coll)
        rng (java.util.Random. seed)]
    (java.util.Collections/shuffle al rng)
    (clojure.lang.RT/vector (.toArray al))))

(defn generate-swaps [{:keys [early-bicycle? early-super-rod?]}]
  (zipmap all-items
          (deterministic-shuffle all-items seed-id)))

(defn generate [seed-id {:keys [early-bicycle? early-super-rod?] :as options}]
  (let [swaps (generate-swaps options)
        progression-results (beatable? swaps {:speedchoice? true})]
    (if (progression-results :beatable?)
      {:seed (-> progression-results
                 (assoc :patches (patches/generate swaps {:speedchoice? speedchoice?}))
                 (assoc :id (str seed-id)))}
      {:error (str "Unbeatable seed: " seed-id)})))

(defn generate-beatable [opts]
  (loop []
    (let [seed-id (-> (new java.util.Random)
                      .nextLong
                      java.lang.Math/abs)
          {:keys [seed error]} (generate seed-id opts)]
      (if error
        (recur)
        {:seed seed}))))
