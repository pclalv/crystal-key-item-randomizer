(ns crystal-key-item-randomizer.seeds
  (:require 
   [crystal-key-item-randomizer.patches :as patches]
   [crystal-key-item-randomizer.key-items :as key-items]
   [crystal-key-item-randomizer.badges :as badges]
   [clojure.spec.alpha :as s]
   [crystal-key-item-randomizer.specs])
  (:use [crystal-key-item-randomizer.progression :only [beatable? get-swaps]]))

(def all-items (vec (keys key-items/speedchoice)))
(def badges (vec (keys badges/speedchoice)))

(def early-badges #{:ZEPHYRBADGE :HIVEBADGE:PLAINBADGE :FOGBADGE})

(defn early-items
  "All items that the player is guaranteed to get early on."
  [{:keys [include-squirtbottle?]}]
  (->> #{:MYSTERY_EGG :HM_FLASH :OLD_ROD :HM_CUT
         :BICYCLE :BLUE_CARD :COIN_CASE (when include-squirtbottle?
                                          :SQUIRTBOTTLE)}
       (remove nil?)
       (into #{})))

(defn deterministic-shuffle [^java.util.Collection coll seed]
  (let [al (java.util.ArrayList. coll)
        rng (java.util.Random. seed)]
    (java.util.Collections/shuffle al rng)
    (clojure.lang.RT/vector (.toArray al))))

(defn gives-early? [item swaps {:keys [randomize-badges?] :as opts}]
  (let [early-swaps (crystal-key-item-randomizer.progression/get-swaps swaps
                                                                       ;; if badges are randomized, then we cannot guarantee that the SQUIRTBOTTLE will be an early item;
                                                                       ;; in other words, we don't want to the BICYCLE to be locked behind the PLAINBADGE in badge rando.
                                                                       (early-items {:include-squirtbottle? (not (and randomize-badges?
                                                                                                                      (= item :BICYCLE)))}))]
    (-> early-swaps
        (contains? item))))

(defn early-sabrina? [badge-swaps]
  (let [early-swaps (crystal-key-item-randomizer.progression/get-swaps badge-swaps early-badges)]
    (-> early-swaps
        (contains? :MARSHBADGE))))

(defn generate-swaps [{:keys [randomize-badges? early-bicycle? no-early-sabrina? no-early-super-rod?] :as opts}]
  (loop [rng (or (:rng opts)
                 ;; TODO: use CSPRG? https://docs.oracle.com/javase/7/docs/api/java/security/SecureRandom.html
                 (new java.util.Random))]
    (let [seed-id (-> rng
                      .nextLong
                      java.lang.Math/abs)
          item-swaps (zipmap all-items
                             (deterministic-shuffle all-items seed-id))
          badge-swaps (if randomize-badges?
                        (zipmap badges (deterministic-shuffle badges seed-id))
                        (zipmap badges badges))]
      (cond (and early-bicycle? (not (gives-early? :BICYCLE item-swaps opts))) (recur rng)
            (and no-early-super-rod? (gives-early? :SUPER_ROD item-swaps opts)) (recur rng)
            (and no-early-sabrina? (early-sabrina? badge-swaps)) (recur rng)
            :else {:item-swaps item-swaps
                   :badge-swaps badge-swaps
                   :seed-id seed-id}))))

(defn generate-random [{:keys [endgame-condition swaps-options early-rockets?]
                        :or {endgame-condition :defeat-elite-4
                             swaps-options {:randomize-badges? false}
                             early-rockets? false}}]
  (loop [iterations 1]
    (let [{:keys [item-swaps badge-swaps seed-id]} (generate-swaps swaps-options)
          progression-results (beatable? {:item-swaps item-swaps
                                          :badge-swaps badge-swaps}
                                         {:endgame-condition endgame-condition
                                          :early-rockets? early-rockets?})]
      (if (progression-results :beatable?)
        {:seed (-> progression-results
                   (assoc :patches (patches/generate {:item-swaps item-swaps
                                                      :badge-swaps badge-swaps}
                                                     {:speedchoice? true
                                                      :early-rockets? early-rockets?}))
                   (assoc :id (str seed-id)))
         :iterations iterations}
        (recur (inc iterations))))))

(s/def ::error string?)
(s/def ::iterations int?)

;; is this wrapping useless? is this name bad?
(s/def ::generate-result
  (s/or :ok (s/keys :req-un [:crystal-key-item-randomizer.specs/seed])
        :err (s/keys :req-un [::error])))

(s/fdef generate-random
  :args (s/cat :options map?)
  :ret ::generate-result)

(defn generate
  ([seed-id]
   (generate seed-id {:endgame-condition :defeat-elite-4
                      :swaps-options {:randomize-badges? false}
                      :early-rockets? false}))
  ([seed-id {:keys [swaps-options endgame-condition early-rockets?]}]
   (let [item-swaps (zipmap all-items (deterministic-shuffle all-items seed-id))
         badge-swaps (if (:randomize-badges? swaps-options)
                       (zipmap badges (deterministic-shuffle badges seed-id))
                       (zipmap badges badges))
         progression-results (beatable? {:item-swaps item-swaps
                                         :badge-swaps badge-swaps}
                                        {:endgame-condition endgame-condition
                                         :early-rockets? early-rockets?})]
     (if (progression-results :beatable?)
       {:seed (-> progression-results
                  (assoc :patches (patches/generate {:item-swaps item-swaps
                                                     :badge-swaps badge-swaps}
                                                    {:speedchoice? true
                                                     :early-rockets? early-rockets?}))
                  (assoc :id (str seed-id)))}
       {:error (str "Unbeatable seed: " seed-id)
        :seed (-> progression-results
                  (assoc :id (str seed-id)))}))))

(s/fdef generate
  :args (s/alt :unary (s/cat :seed-id int?)
               :with-options (s/cat :seed-id int?
                                    :options map?))

  :ret ::generate-result)
