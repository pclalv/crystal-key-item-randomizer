(ns crystal-key-item-randomizer.patches.randomize-janine
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.spec.alpha :as s]
            [crystal-key-item-randomizer.specs])
  (:use [crystal-key-item-randomizer.seeds :only [deterministic-pick]]))

(def speedchoice-fuchsia-gym-trainers
  "These patches aren't the full person_events, but rather just the positions. "
  (-> "fuchsia-gym-trainers.edn" io/resource slurp edn/read-string))

;; TODO: write a test
(defn generate
  "Randomize which Janine is the real Janine in Fuchsia Gym."
  [{:keys [randomize-janine?]
    :or {randomize-janine? false}}
   & opts]
  (if-not randomize-janine?
    []
    (let [rng (or (:rng opts)
                  ;; TODO: use CSPRG? https://docs.oracle.com/javase/7/docs/api/java/security/SecureRandom.html
                  (new java.util.Random))
          seed-id (-> rng .nextLong java.lang.Math/abs)
          trainers speedchoice-fuchsia-gym-trainers
          janine-label "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_JANINE_at_10_1"
          janine (->> trainers
                      (filter #(= janine-label (% :label)))
                      first)
          swap-trainer (deterministic-pick (->> trainers
                                                (filter #(not= janine-label (% :label))))
                                           seed-id)
          janine' (-> janine
                      (assoc :integer_values {:new (:integer_values swap-trainer)
                                              :old (:integer_values janine)}))
          swap-trainer' (-> swap-trainer
                            (assoc :integer_values {:new (:integer_values janine)
                                                    :old (:integer_values swap-trainer)}))]
      [janine' swap-trainer'])))

(s/fdef generate
  :args (s/cat :options :crystal-key-item-randomizer.specs/seed-options)
  :ret (s/coll-of ::patch :kind? vector))
