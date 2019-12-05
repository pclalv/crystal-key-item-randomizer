(ns crystal-key-item-randomizer.patches.text.gym-leader-post-defeat)

(def pre-badge-blurb-patches
  (-> "resources/pre-badge-blurb-patches.edn" slurp clojure.edn/read-string))

(def post-badge-speech-patches
  (-> "resources/post-badge-speech-patches.edn" slurp clojure.edn/read-string))
