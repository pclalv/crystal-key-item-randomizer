(ns crystal-key-item-randomizer.patches.randomize-janine
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.spec.alpha :as s]
            [crystal-key-item-randomizer.specs])
  (:use [crystal-key-item-randomizer.prng :only [deterministic-pick]]
        [crystal-key-item-randomizer.patches.data :only [get-patch]]))

(def y
  "For an array of bytes defining a pokecrystal person_event, the Y coord index"
  1)
(def x
  "For an array of bytes defining a pokecrystal person_event, the X coord index"
  2)

;; read from JSON
(def speedchoice-fuchsia-gym-trainers
  (map get-patch ["FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_FUCHSIA_GYM_1_at_7_5"
                  "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_FUCHSIA_GYM_2_at_11_5"
                  "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_FUCHSIA_GYM_3_at_4_9"
                  "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_FUCHSIA_GYM_4_at_2_4"]))

(def janine
  (get-patch "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_JANINE_at_10_1"))

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
          swap-trainer (deterministic-pick speedchoice-fuchsia-gym-trainers
                                           seed-id)
          janine' (-> janine
                      ;; the janine person_event's new values are
                      ;; basically the same as the old values, but
                      ;; with the X and Y coords updated
                      (assoc-in [:integer_values :new]
                                (get-in janine [:integer_values :old]))
                      (assoc-in [:integer_values :new y]
                                (get-in swap-trainer [:integer_values :old y]))
                      (assoc-in [:integer_values :new x]
                                (get-in swap-trainer [:integer_values :old x])))
          swap-trainer' (-> swap-trainer
                            (assoc-in [:integer_values :new]
                                      (get-in swap-trainer [:integer_values :old]))
                            (assoc-in [:integer_values :new y]
                                      (get-in janine [:integer_values :old y]))
                            (assoc-in [:integer_values :new x]
                                      (get-in janine [:integer_values :old x])))]
      [janine' swap-trainer'])))

;; FIXME: get this spec to work
;; (s/fdef generate
;;   :args (s/cat :logic-options :crystal-key-item-randomizer.specs/logic-options
;;                :options (s/? (s/cat :rng (partial instance? java.util.Random))))
;;   :ret (s/coll-of ::patch :kind? vector))
