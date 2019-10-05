(ns crystal-key-item-randomizer.patches
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io])
  (:use [crystal-key-item-randomizer.data :only [key-items key-items-speedchoice]]))

(def UNDERGROUND-ITEM-BALL :GoldenrodUndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL)

(def vanilla-patches
  "Contains data that the frontend can use to modify the ROM file with changes beyond the usual key item swaps."
  (->> "randomizer-patches-diff.json"
       io/resource
       slurp
       json/read-str))

(def speedchoice-patches
  "Contains data that the frontend can use to modify the ROM file with changes beyond the usual key item swaps."
  ;; TODO: define this file
  (->> "randomizer-patches-diff-speedchoice.json"
       io/resource
       slurp
       json/read-str))

(defn item-ball [key-item {:keys [speedchoice?]}]
  (let [key-items' (if speedchoice? key-items-speedchoice key-items)
        key-item-value (get-in key-items' [key-item :value])]
    [key-item-value 1]))

(defn replace-underground-warehouse-ultra-ball-with-key-item [patches {card-key-replacement :CARD_KEY} {:keys [speedchoice?]}]
  (let [{rest :rest patch UNDERGROUND-ITEM-BALL} (->> patches
                                                      (group-by (fn [patch]
                                                                  (if (= (name UNDERGROUND-ITEM-BALL)
                                                                         (patch "name"))
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
    (-> default
        (replace-underground-warehouse-ultra-ball-with-key-item swaps {:speedchoice? speedchoice?}))))
