(ns crystal-key-item-randomizer.seeds
  (:require [crystal-key-item-randomizer.patches :as patches])
  (:use [crystal-key-item-randomizer.progression :only [beatable? get-swaps]]))

(def all-items (vec (keys crystal-key-item-randomizer.key-items/speedchoice)))

(def early-items #{:MYSTERY_EGG
                   :HM_FLASH
                   :OLD_ROD
                   :HM_CUT
                   ;; goldenrod
                   :BICYCLE
                   :BLUE_CARD
                   :COIN_CASE
                   :SQUIRTBOTTLE})

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
    (let [{:keys [item-swaps seed-id]} (generate-item-swaps options)
          progression-results (beatable? item-swaps)]
      (if (progression-results :beatable?)
        {:seed (-> progression-results
                   (assoc :patches (patches/generate item-swaps {:speedchoice? true}))
                   (assoc :id (str seed-id)))}
        (recur)))))

(defn generate [seed-id]
  (let [item-swaps (zipmap all-items
                      (deterministic-shuffle all-items seed-id))
        progression-results (beatable? item-swaps :speedchoice? true)]
    (if (progression-results :beatable?)
      {:seed (-> progression-results
                 (assoc :patches (patches/generate item-swaps {:speedchoice? true}))
                 (assoc :id (str seed-id)))}
      {:error (str "Unbeatable seed: " seed-id)
       :seed (-> progression-results
                 (assoc :id (str seed-id)))})))
