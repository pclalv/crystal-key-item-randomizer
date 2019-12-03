(ns crystal-key-item-randomizer.patches
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [crystal-key-item-randomizer.key-items :as key-items]
            [crystal-key-item-randomizer.badges :as badges])
  (:use [crystal-key-item-randomizer.patches.text :only [fix-giveitems]]))        

(def UNDERGROUND-ITEM-BALL :GoldenrodUndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL)
(def UNDERGROUND-ITEM-BALL-SPEEDCHOICE :UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL)

(def underground-warehouse-ultra-ball
  {:label "UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL"
   :description "Change the contents of the item ball from ULTRA_BALL to whatever replaces the CARD_KEY (a backup key item so that the player doesn't get softlocked). integer_values.new doesn't exist so that things will fail hard if the patches aren't update properly"
   :integer_values {:old ["*", 1]}
   :address_range {:begin 514536
                   :end 514538}})

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

(def vanilla-patches
  "Contains data that the frontend can use to modify the ROM file with
  changes beyond the usual key item swaps."
  (->> "randomizer-patches-diff.json"
       io/resource
       slurp
       json/read-str))

(def speedchoice-patches
  "Contains data specific to crystal-speedchoice.gbc that the frontend
  can use to modify the ROM file with changes beyond the usual key
  item swaps."
  (->> "randomizer-patches-diff-speedchoice.json"
       io/resource
       slurp
       json/read-str))

(defn item-ball [key-item {:keys [speedchoice?]}]
  (let [key-items' (if speedchoice? key-items/speedchoice key-items/vanilla)
        key-item-value (get-in key-items' [key-item :value])]
    [key-item-value 1]))

;;;;;;;;;;;;;;;;;
;; badge stuff ;;
;;;;;;;;;;;;;;;;;

(defn replace-underground-warehouse-ultra-ball-with-key-item [patches {card-key-replacement :CARD_KEY} {:keys [speedchoice?]}]
  (let [patch (assoc-in underground-warehouse-ultra-ball
                        [:integer_values :new]
                        (item-ball card-key-replacement {:speedchoice? speedchoice?}))]
    (conj patches patch)))

(defn checkflag [badge]
  (let [badge-flag-value (-> badges/speedchoice badge :value)]
    [52 badge-flag-value 0]))

(defn replace-checkflag-for-badge
  "For badge randomization, we patch out most `checkflag*BADGE` calls,
  for example, those in the Kanto gyms that essentially serve to check
  whether the player has beaten the Gym Leader yet or not. If we were
  to leave those checks alone, then the reuslt of badge randomization
  would be that obtaining the THUNDERBADGE from Bugsy would mean that
  the player could not even battle Lt Surge, let alone get his
  badge. Those checks are easily replaced with `checkevent` calls to
  see if the player has actually beaten the gym leader or not.

  There are two additional problemation `checkflag` calls around
  interactions with Whitney and Clair in their respective gyms, owing
  to the fact that the vanilla game does not immediately grant either
  badge. Instead,we need to acutally check if the player has received
  the replacement badge."
  [patches original-badge badge-swaps]
  (let [replacement-badge (badge-swaps original-badge)
        check-replacement-badge-patch (-> standalone-checkflag-badges
                                          original-badge
                                          (assoc-in [:integer_values :new] (checkflag replacement-badge)))]
    (conj patches check-replacement-badge-patch)))

(defn generate [{:keys [item-swaps badge-swaps]} {:keys [speedchoice?]}]
  (let [patches (if speedchoice?
                  speedchoice-patches
                  vanilla-patches)]
    (-> patches
        (replace-underground-warehouse-ultra-ball-with-key-item item-swaps {:speedchoice? speedchoice?})
        (replace-checkflag-for-badge :PLAINBADGE badge-swaps)
        (replace-checkflag-for-badge :RISINGBADGE badge-swaps)
        (fix-giveitems swaps))))
