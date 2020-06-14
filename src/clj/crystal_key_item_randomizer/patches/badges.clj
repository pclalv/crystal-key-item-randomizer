(ns crystal-key-item-randomizer.patches.badges
  (:require [crystal-key-item-randomizer.badges :as badges]
            [clojure.spec.alpha :as s])
  (:use [crystal-key-item-randomizer.patches.data :only [get-patch]]))

(def standalone-checkflag-badges
  "For badge randomization; check whatever badge is actually given by the gym leader instead of their vanilla badge"
  {:PLAINBADGE (get-patch "WhitneyScript_0x5400c.ckir_BEFORE_checkflag_ENGINE_PLAINBADGE")
   :RISINGBADGE (get-patch "BlackthornGymClairScript.ckir_BEFORE_checkflag_ENGINE_RISINGBADGE")})

(defn checkflag [badge]
  (let [badge-flag-value (-> badges/speedchoice badge :value)]
    [52 badge-flag-value 0]))

(defn replace-checkflag-for-badge
  "For badge randomization, we patch out most `checkflag*BADGE` calls,
  for example, those in the Kanto gyms that essentially serve to check
  whether the player has beaten the Gym Leader yet or not. If we were
  to leave those checks alone, then the result of badge randomization
  would be that obtaining the THUNDERBADGE from Bugsy would mean that
  the player could not battle Lt Surge, let alone get his badge. Those
  checks are easily replaced with `checkevent` calls to see if the
  player has actually beaten the gym leader or not.

  There are two additional problematic `checkflag` calls around
  interactions with Whitney and Clair in their respective gyms, owing
  to the fact that the vanilla game does not immediately grant either
  badge. Instead, we need to acutally check if the player has received
  the replacement badge."
  [original-badge badge-swaps]
  (let [replacement-badge (badge-swaps original-badge)]
    (-> standalone-checkflag-badges
        original-badge
        (assoc-in [:integer_values :new] (checkflag replacement-badge)))))

(s/def ::patchable-badge #{:RISINGBADGE :PLAINBADGE})

(s/fdef replace-checkflag-for-badge
  :args (s/cat :original-badge ::patchable-badge
               :badge-swaps map?))
