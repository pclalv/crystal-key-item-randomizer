(ns crystal-key-item-randomizer.rom
  (:require #?(:clj [clojure.spec.alpha :as s]
               :cljs [cljs.spec.alpha :as s])))

(s/def ::byte (s/int-in 0 256))
