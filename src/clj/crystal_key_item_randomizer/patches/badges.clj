(ns crystal-key-item-randomizer.patches.badges
  (:require [crystal-key-item-randomizer.badges :as badges]))

(def standalone-checkflag-badges
  {:PLAINBADGE {:label "WhitneyScript_0x5400c.ckir_BEFORE_checkflag_ENGINE_PLAINBADGE"
                :description "Check whatever badge is actually given by Whitney, even in badge rando"
                :address_range {:begin 344138
                                :end 344141}
                :integer_values {:old [52 29 0]}}
   :RISINGBADGE {:label "BlackthornGymClairScript.ckir_BEFORE_checkflag_ENGINE_RISINGBADGE"
                 :description "Check whatever badge is actually given by Clair, even in badge rando"
                 :address_range {:begin 1658406
                                 :end 1658409}
                 :integer_values {:old [52 34 0]}}})

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

  There are two additional problemation `checkflag` calls around
  interactions with Whitney and Clair in their respective gyms, owing
  to the fact that the vanilla game does not immediately grant either
  badge. Instead, we need to acutally check if the player has received
  the replacement badge."
  [original-badge badge-swaps]
  (let [replacement-badge (badge-swaps original-badge)]
    (-> standalone-checkflag-badges
        original-badge
        (assoc-in [:integer_values :new] (checkflag replacement-badge)))))
