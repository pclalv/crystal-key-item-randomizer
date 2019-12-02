(ns crystal-key-item-randomizer.patches
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]
            [crystal-key-item-randomizer.key-items :as key-items]))

(def UNDERGROUND-ITEM-BALL :GoldenrodUndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL)
(def UNDERGROUND-ITEM-BALL-SPEEDCHOICE :UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL)

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
  (let [{rest :rest patch UNDERGROUND-ITEM-BALL} (->> patches
                                                      (group-by (fn [patch]
                                                                  (if (= (name (if speedchoice?
                                                                                 UNDERGROUND-ITEM-BALL-SPEEDCHOICE
                                                                                 UNDERGROUND-ITEM-BALL))
                                                                         (patch "label"))
                                                                    UNDERGROUND-ITEM-BALL
                                                                    :rest))))
        patch (first patch)
        updated-patch (assoc-in patch
                                ["integer_values" "new"]
                                (item-ball card-key-replacement {:speedchoice? speedchoice?}))]
    (conj rest updated-patch)))

(defn generate [swaps {:keys [speedchoice?]}]
  (let [patches (if speedchoice?
                  speedchoice-patches
                  vanilla-patches)]
    (-> patches
        (replace-underground-warehouse-ultra-ball-with-key-item swaps {:speedchoice? speedchoice?}))))
