(ns crystal-key-item-randomizer.patches.rockets)

(def trigger-early (-> "rocket-triggers.edn"
                       clojure.java.io/resource
                       slurp
                       clojure.edn/read-string))
