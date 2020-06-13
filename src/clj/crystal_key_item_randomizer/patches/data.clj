(ns crystal-key-item-randomizer.patches.data
  "Contains data for binary patches that can be applied to
  crystal-speedchoice.gbc."
  (:require [clojure.data.json :as json]
            [clojure.java.io :as io]))

(def essential-patches
  "Essential binaries patches that are ready to be applied to any ROM."
  (-> "randomizer-patches-diff-speedchoice.json"
      io/resource
      slurp
      (json/read-str :key-fn keyword)))

(def other-patches
  "Patches which are either not always applied, or which must be updated
  by the randomizer before being applied."
  (-> "speedchoice-patches.json"
      io/resource
      slurp
      (json/read-str :key-fn keyword)))

(def all-patches
  (concat essential-patches other-patches))

(defn get-patch [name]
  (-> (->> all-patches
           (filter #(= name (% :label)))
           (first))
      (update :integer_values dissoc :new)))
