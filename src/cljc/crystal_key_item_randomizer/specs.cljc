(ns crystal-key-item-randomizer.specs
  (:require #?(:clj [clojure.spec.alpha :as s]
               :cljs [cljs.spec.alpha :as s])))

(s/def ::seed
  (s/keys :req-un [::patches ::id]))

(s/def ::item-swaps map?)
(s/def ::badge-swaps map?)
(s/def ::swaps (s/keys :req-un [::item-swaps ::badge-swaps]))

(s/def ::speedchoice? boolean?)
(s/def ::rockets  #{:normal :early :rocketless})
(s/def ::endgame-condition #{:defeat-red :defeat-elite-4})
(s/def ::no-blind-rock-tunnel? boolean?)
(s/def ::seed-options
  (s/keys :req-un []
          :opt-un [::rockets ::speedchoice? ::endgame-condition ::no-blind-rock-tunnel?]))
