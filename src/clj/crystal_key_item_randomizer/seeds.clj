(ns crystal-key-item-randomizer.seeds
  (:require
   [crystal-key-item-randomizer.patches :as patches]
   [crystal-key-item-randomizer.key-items :as key-items]
   [crystal-key-item-randomizer.badges :as badges]
   [clojure.spec.alpha :as s]
   [crystal-key-item-randomizer.specs]
   [clojure.set :as cset])
  (:use [crystal-key-item-randomizer.progression :only [beatable? get-swaps]]
        [crystal-key-item-randomizer.prng]))

(def all-items (-> (key-items/speedchoice) keys vec))
(def badges (-> badges/speedchoice keys vec))

(def early-badges #{:ZEPHYRBADGE :HIVEBADGE :PLAINBADGE :FOGBADGE})

(def lance-items #{:BASEMENT_KEY :CARD_KEY :CLEAR_BELL :HM_WHIRLPOOL})
(def useless-items #{:BASEMENT_KEY :BLUE_CARD :CARD_KEY :CLEAR_BELL
                     :COIN_CASE :GOOD_ROD :HM_WHIRLPOOL :ITEMFINDER
                     :MYSTERY_EGG :OLD_ROD :RED_SCALE :SILVER_WING})
(def useful-items (cset/difference (set all-items) useless-items))

(defn early-items
  "All items that the player is guaranteed to get early on.

  If badges are randomized, then we cannot guarantee that the
  SQUIRTBOTTLE will be an early item; in other words, we don't want to
  the BICYCLE to be locked behind the PLAINBADGE in badge rando."
  [{:keys [include-squirtbottle?]}]
  (->> #{:MYSTERY_EGG :HM_FLASH :OLD_ROD :HM_CUT
         :BICYCLE :BLUE_CARD :COIN_CASE (when include-squirtbottle?
                                          :SQUIRTBOTTLE)}
       (remove nil?)
       (into #{})))

(defn gives-early? [item swaps {:keys [randomize-badges?] :as opts}]
  (let [early-swaps (get-swaps swaps
                               (early-items {:include-squirtbottle? (not (and randomize-badges?
                                                                              (= item :BICYCLE)))}))]
    (-> early-swaps
        (contains? item))))

;; (defrecord swaps-options [randomize-badges? early-bicycle? no-early-super-rod? randomize-copycat-item?])

(defn lance-gives-useful-items? [swaps]
  (let [lance-swaps (set (get-swaps swaps lance-items))]
    (seq (cset/intersection useful-items lance-swaps))))

(defn generate-swaps*
  "Generate any set of swaps using the provided RNG."
  [seed-id {:keys [randomize-badges? randomize-copycat-item?] :as swaps-options}]
  (let [rng (new java.util.Random seed-id)
        item-swaps (zipmap all-items
                           (deterministic-shuffle all-items (-> rng
                                                                .nextLong
                                                                java.lang.Math/abs)))
        badge-swaps (zipmap badges (if randomize-badges?
                                     (deterministic-shuffle badges (-> rng
                                                                       .nextLong
                                                                       java.lang.Math/abs))
                                     badges))
        copycat-item (if randomize-copycat-item?
                       ;; FIXME: there are even more useless,
                       ;; non-required items in the rocketless mode
                       (deterministic-pick key-items/non-required-items (-> rng
                                                                            .nextLong
                                                                            java.lang.Math/abs))
                       :LOST_ITEM)]
    {:item-swaps item-swaps
     :badge-swaps badge-swaps
     :copycat-item copycat-item
     :seed-id seed-id
     :options swaps-options}))

(defn generate-swaps
  "Generate a set of swaps that fit the criteria; if the generated swaps
  don't meet the criteria, then try again."
  [{:keys [early-bicycle? no-early-super-rod?] :as swaps-options}
   {:keys [rockets] :as logic-options}]
  (let [rng (or (:rng swaps-options)
                ;; TODO: use CSPRG? https://docs.oracle.com/javase/7/docs/api/java/security/SecureRandom.html
                (new java.util.Random))]
    (loop [seed-id (-> rng
                       .nextLong
                       java.lang.Math/abs)]
      (let [{:keys [item-swaps] :as swaps} (generate-swaps* seed-id swaps-options)
            next-seed-id (-> rng
                             .nextLong
                             java.lang.Math/abs)]
        (cond (and early-bicycle? (not (gives-early? :BICYCLE item-swaps swaps-options)))
              #_=> (recur next-seed-id)

              (and no-early-super-rod? (gives-early? :SUPER_ROD item-swaps swaps-options))
              #_=> (recur next-seed-id)

              (and (= :rocketless rockets) (lance-gives-useful-items? item-swaps))
              #_=> (recur next-seed-id)

              :else
              #_=> swaps)))))

(s/fdef generate-swaps
  :args (s/cat :swaps-options :crystal-key-item-randomizer.specs/swaps-options
               :logic-options :crystal-key-item-randomizer.specs/logic-options))

(defn generate-random [{:keys [logic-options swaps-options]
                        :or {swaps-options {}
                             logic-options {:endgame-condition :defeat-red
                                            :rockets :normal}}}]
  (loop [iterations 1]
    (let [swaps (generate-swaps swaps-options logic-options)
          progression-results (beatable? swaps logic-options)]
      (if (progression-results :beatable?)
        {:seed (-> progression-results
                   (assoc :patches (patches/generate swaps logic-options))
                   (assoc :id (str (:seed-id swaps)))
                   (assoc :options logic-options))
         :iterations iterations}
        (recur (inc iterations))))))

(s/def ::generate-options
  (s/keys :req-un [:crystal-key-item-randomizer.specs/swaps-options
                   :crystal-key-item-randomizer.specs/logic-options]))

(s/def ::error string?)
(s/def ::iterations int?)

;; is this wrapping useless? is this name bad?
(s/def ::generate-result
  (s/or :ok (s/keys :req-un [:crystal-key-item-randomizer.specs/seed]
                    :opt-un [::iterations])
        :err (s/keys :req-un [::error])))

(s/fdef generate-random
  :args (s/cat :opts ::generate-options)
  :ret ::generate-result)

(defn generate
  ([seed-id]
   (generate seed-id {:logic-options {:endgame-condition :defeat-red
                                      :fly-by :none
                                      :rockets :normal}
                      :swaps-options {}}))
  ([seed-id {:keys [swaps-options logic-options]
             :or {swaps-options {}
                  logic-options {}}}]
   (let [swaps-options (-> swaps-options
                           ;; disable these options; we're generating a particular seed,
                           ;; so there's no sense in attempting to control these aspects.
                           (assoc :early-bicycle? false
                                  :no-early-super-rod? false))
         logic-options (-> logic-options
                           ;; likewise, and for lack of any better idea of how to handle this,
                           ;; we don't care about whatever rockets settings the user picked.
                           (assoc :rockets :normal))
         swaps (generate-swaps* seed-id swaps-options)
         progression-results (beatable? swaps logic-options)]
     (if (progression-results :beatable?)
       {:seed (-> progression-results
                  (assoc :id (str seed-id))
                  (assoc :patches (patches/generate swaps logic-options))
                  (assoc :options logic-options))}
       {:error (str "Unbeatable seed: " seed-id)
        :seed progression-results}))))

(s/fdef generate
  :args (s/alt :unary (s/cat :seed-id int?)
               :with-options (s/cat :seed-id int?
                                    :options ::generate-options))

  :ret ::generate-result)

;; (defn beatability-stats [n]
;;   (let [{:keys [beatable not-beatable]} (->> (take n (repeatedly #(-> java.util.Random
;;                                                                       new
;;                                                                       .nextLong
;;                                                                       java.lang.Math/abs)))
;;                                              (map #(crystal-key-item-randomizer.seeds/generate %))
;;                                              (group-by #(if (-> % :seed :beatable?)
;;                                                           :beatable
;;                                                           :not-beatable)))]
;;     {:beatable (count beatable)
;;      :not-beatable (count not-beatable)
;;      :ratio (float (/ (count beatable)
;;                       n))}))
