(ns crystal-key-item-randomizer.seeds
  (:require 
   [crystal-key-item-randomizer.patches :as patches]
   [crystal-key-item-randomizer.key-items :as key-items]
   [crystal-key-item-randomizer.badges :as badges])
  (:use [crystal-key-item-randomizer.progression :only [beatable? get-swaps]]))

(def all-items (vec (keys key-items/speedchoice)))
(def badges (vec (keys badges/speedchoice)))

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

(defn generate-swaps [{:keys [randomize-badges? early-bicycle? no-early-super-rod?] :as opts}]
  (println "randomize-badges?" randomize-badges?)
  (loop [rng (or (:rng opts)
                 (new java.util.Random))]
    (let [seed-id (-> rng
                      .nextLong
                      java.lang.Math/abs)
          item-swaps (zipmap all-items
                             (deterministic-shuffle all-items seed-id))
          badge-swaps (if randomize-badges?
                        (throw (Exception. "FIXME: actually randomize badges"))
                        (zipmap badges badges))]
      (cond (and early-bicycle?
                 (not (gives-early? :BICYCLE item-swaps))) (recur rng)
            (and no-early-super-rod?
                 (gives-early? :SUPER_ROD item-swaps)) (recur rng)
            :else {:item-swaps item-swaps
                   :badge-swaps badge-swaps
                   :seed-id seed-id}))))

(defn generate-random [options]
  (loop []
    (let [{:keys [item-swaps badge-swaps seed-id]} (generate-swaps options)
          progression-results (beatable? item-swaps)]
      (if (progression-results :beatable?)
        {:seed (-> progression-results
                   (assoc :patches (patches/generate item-swaps {:speedchoice? true}))
                   (assoc :id (str seed-id)))}
        (recur)))))

(defn generate
  ([seed-id]
   (generate seed-id {:randomize-badges? false}))
  ([seed-id {:keys [randomize-badges?] :as options}]
   (let [item-swaps (zipmap all-items
                            (deterministic-shuffle all-items seed-id))
         badge-swaps (if randomize-badges?
                       (throw (Exception. "FIXME: actually randomize badges"))
                       (zipmap badges badges))
         progression-results (beatable? item-swaps)]
     (if (progression-results :beatable?)
       {:seed (-> progression-results
                  (assoc :patches (patches/generate item-swaps {:speedchoice? true}))
                  (assoc :id (str seed-id)))}
       {:error (str "Unbeatable seed: " seed-id)
        :seed (-> progression-results
                  (assoc :id (str seed-id)))}))))
