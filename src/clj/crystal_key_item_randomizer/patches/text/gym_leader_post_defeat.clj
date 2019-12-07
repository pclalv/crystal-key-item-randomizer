(ns crystal-key-item-randomizer.patches.text.gym-leader-post-defeat
  (:require [clojure.java.io :as io] ))

(def pre-badge-blurb-patches
  (-> "pre-badge-blurb-patches.edn" io/resource slurp clojure.edn/read-string))

(def post-badge-speech-patches
  (-> "post-badge-speech-patches.edn" io/resource slurp clojure.edn/read-string))
