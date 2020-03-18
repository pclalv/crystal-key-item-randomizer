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

;; alternatively, is there some way of telling if the player would
;; have access to a high level pokemon before facing down high-level
;; gym leaders like Sabrina? maybe Super Rod, or Ecruteak access (for)
;; roamers) would have to be a requirement for those leaders.
(defn early-sabrina? [badge-swaps]
  (contains? early-badges (badge-swaps :MARSHBADGE)))

;; (defrecord swaps-options [randomize-badges? early-bicycle? no-early-sabrina? no-early-super-rod? randomize-copycat-item?])

(defn lance-gives-useful-items? [swaps]
  (let [lance-swaps (set (get-swaps swaps lance-items))]
    (seq (cset/intersection useful-items lance-swaps))))

(defn generate-swaps
  [{:keys [randomize-badges? early-bicycle? no-early-sabrina? no-early-super-rod? randomize-copycat-item?] :as opts}
   {:keys [rockets] :as seed-options}]
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
                        (zipmap badges badges))
          copycat-item (if randomize-copycat-item?
                         (deterministic-pick key-items/non-required-items seed-id)
                         :LOST_ITEM)]
      (cond (and early-bicycle? (not (gives-early? :BICYCLE item-swaps opts)))
            #_=> (recur rng)

            (and no-early-super-rod? (gives-early? :SUPER_ROD item-swaps opts))
            #_=> (recur rng)

            (and no-early-sabrina? (early-sabrina? badge-swaps))
            #_=> (recur rng)

            (and (= :rocketless rockets) (lance-gives-useful-items? item-swaps))
            #_=> (recur rng)

            :else
            #_=> {:item-swaps item-swaps
                  :badge-swaps badge-swaps
                  :copycat-item copycat-item
                  :seed-id seed-id
                  :options opts}))))

(s/fdef generate-swaps
  :args (s/cat :crystal-key-item-randomizer.specs/swaps-options
               :crystal-key-item-randomizer.specs/seed-options))

(def default-generate-options
  "Would be nice if we could use this variable in the generate-random
  and generate function signatures, namely in the arg list."
  '{seed-options {:endgame-condition :defeat-elite-4
                  :randomize-janine? false
                  :rockets :normal}
    swaps-options {:randomize-badges? false
                   :early-bicycle? false
                   :no-early-sabrina? false
                   :no-early-super-rod? false
                   :randomize-copycat-item? false}})

(defn generate-random [{:keys [seed-options swaps-options]
                        :or {swaps-options {}
                             seed-options {:endgame-condition :defeat-elite-4
                                           :rockets :normal}}}]
  (loop [iterations 1]
    (let [swaps (generate-swaps swaps-options seed-options)
          progression-results (beatable? swaps seed-options)]
      (if (progression-results :beatable?)
        {:seed (-> progression-results
                   (assoc :patches (patches/generate swaps seed-options))
                   (assoc :id (str (:seed-id swaps)))
                   (assoc :options seed-options))
         :iterations iterations}
        (recur (inc iterations))))))

(s/def ::generate-options
  (s/keys :req-un [:crystal-key-item-randomizer.specs/swaps-options
                   :crystal-key-item-randomizer.specs/seed-options]))

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
   (generate seed-id {}))
  ([seed-id {:keys [swaps-options seed-options]
             :or {swaps-options {}
                  seed-options {:endgame-condition :defeat-elite-4
                                :rockets :normal}}}]
   (let [item-swaps (zipmap all-items (deterministic-shuffle all-items seed-id))
         badge-swaps (if (:randomize-badges? swaps-options)
                       (zipmap badges (deterministic-shuffle badges seed-id))
                       (zipmap badges badges))
         copycat-item (if (:randomize-copycat-item? swaps-options)
                        (deterministic-pick key-items/non-required-items seed-id)
                        :LOST_ITEM)
         swaps {:item-swaps item-swaps
                :badge-swaps badge-swaps
                :copycat-item copycat-item
                :seed-id seed-id}
         progression-results (beatable? swaps seed-options)]
     (if (progression-results :beatable?)
       {:seed (-> progression-results
                  (assoc :id (str seed-id))
                  (assoc :patches (patches/generate swaps seed-options))
                  (assoc :options seed-options))}
       (assoc progression-results :error (str "Unbeatable seed: " seed-id))))))

(s/fdef generate
  :args (s/alt :unary (s/cat :seed-id int?)
               :with-options (s/cat :seed-id int?
                                    :options ::generate-options))

  :ret ::generate-result)
