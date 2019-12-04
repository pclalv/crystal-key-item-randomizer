(ns crystal-key-item-randomizer.patches
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [crystal-key-item-randomizer.key-items :as key-items])
  (:use [crystal-key-item-randomizer.patches.badges :only [replace-checkflag-for-badge]]
        [crystal-key-item-randomizer.patches.text.giveitem :only [fix-giveitems]]
        [crystal-key-item-randomizer.patches.text.received-badge :only [fix-received-badge-texts]]))

(def UNDERGROUND-ITEM-BALL :GoldenrodUndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL)
(def UNDERGROUND-ITEM-BALL-SPEEDCHOICE :UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL)

(def underground-warehouse-ultra-ball
  {:label "UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL"
   :description "Change the contents of the item ball from ULTRA_BALL to whatever replaces the CARD_KEY (a backup key item so that the player doesn't get softlocked). integer_values.new doesn't exist so that things will fail hard if the patches aren't update properly"
   :integer_values {:old ["*", 1]}
   :address_range {:begin 514536
                   :end 514538}})

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

(defn replace-underground-warehouse-ultra-ball-with-key-item [patches {card-key-replacement :CARD_KEY} {:keys [speedchoice?]}]
  (let [patch (assoc-in underground-warehouse-ultra-ball
                        [:integer_values :new]
                        (item-ball card-key-replacement {:speedchoice? speedchoice?}))]
    (conj patches patch)))

(defn generate [{:keys [item-swaps badge-swaps]} {:keys [speedchoice?]}]
  (let [patches (if speedchoice?
                  speedchoice-patches
                  vanilla-patches)]
    (-> patches
        (replace-underground-warehouse-ultra-ball-with-key-item item-swaps {:speedchoice? speedchoice?})
        (replace-checkflag-for-badge :PLAINBADGE badge-swaps)
        (replace-checkflag-for-badge :RISINGBADGE badge-swaps)
        (fix-giveitems item-swaps)
        (fix-received-badge-texts badge-swaps))))
