(ns crystal-key-item-randomizer.seeds
  (:require [crystal-key-item-randomizer.patches :as patches])
  (:use [crystal-key-item-randomizer.randomizer :only [all-items]]
        [crystal-key-item-randomizer.progression :only [beatable? get-swaps]]))

(def early-items (set (apply conj
                             crystal-key-item-randomizer.progression/guaranteed-items
                             crystal-key-item-randomizer.progression/goldenrod-items)))

(defn deterministic-shuffle [^java.util.Collection coll seed]
  (let [al (java.util.ArrayList. coll)
        rng (java.util.Random. seed)]
    (java.util.Collections/shuffle al rng)
    (clojure.lang.RT/vector (.toArray al))))

(defn gives-early? [item swaps]
  (let [early-swaps (crystal-key-item-randomizer.progression/get-swaps swaps early-items)]
    (contains? early-swaps item)))

(defn generate-swaps [{:keys [early-bicycle? no-early-super-rod?] :as opts}]
  (loop [rng (or (:rng opts)
                 (new java.util.Random))]
    (let [seed-id (-> rng
                      .nextLong
                      java.lang.Math/abs)
          swaps (zipmap all-items
                        (deterministic-shuffle all-items seed-id))]
      (cond (and early-bicycle? complement
                 (not (gives-early? :BICYCLE swaps))) (recur rng)
            (and no-early-super-rod?
                 (gives-early? :SUPER_ROD swaps)) (recur rng)
            :else {:swaps swaps :seed-id seed-id}))))

(defn generate-random [options]
  (loop []
    (let [{:keys [swaps seed-id]} (generate-swaps options)
          progression-results (beatable? swaps {:speedchoice? true})]
      (if (progression-results :beatable?)
        {:seed (-> progression-results
                   (assoc :patches (patches/generate swaps {:speedchoice? true}))
                   (assoc :id (str seed-id)))}
        (recur)))))

(defn generate [seed-id]
  (let [swaps (zipmap all-items
                      (deterministic-shuffle all-items seed-id))
        progression-results (beatable? swaps {:speedchoice? true})]
    (if (progression-results :beatable?)
      {:seed (-> progression-results
                 (assoc :patches (patches/generate swaps {:speedchoice? true}))
                 (assoc :id (str seed-id)))}
      {:error (str "Unbeatable seed: " seed-id)})))
