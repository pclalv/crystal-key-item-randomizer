(ns crystal-key-item-randomizer.specs
  (:require #?(:clj [clojure.spec.alpha :as s]
               :cljs [cljs.spec.alpha :as s])))

(s/def ::seed
  (s/keys :req-un [::patches ::id]))
