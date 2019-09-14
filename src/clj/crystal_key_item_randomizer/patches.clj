(ns crystal-key-item-randomizer.patches
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]))

(def default
  "Contains data that the frontend can use to modify the ROM file with changes beyond the usual key item swaps."
  (->> "randomizer-patches-diff.json"
       io/resource
       slurp
       json/read-str))
