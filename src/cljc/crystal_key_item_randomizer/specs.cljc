(ns crystal-key-item-randomizer.specs
  (:require #?(:clj [clojure.spec.alpha :as s]
               :cljs [cljs.spec.alpha :as s])))

(s/def ::seed
  (s/keys :req-un [::patches ::id]))

(s/def ::item-swaps map?)
(s/def ::badge-swaps map?)
(s/def ::swaps (s/keys :req-un [::item-swaps ::badge-swaps]))

(s/def ::endgame-condition #{:defeat-red :defeat-elite-4})
(s/def ::expanded-logic? boolean?)
(s/def ::no-blind-rock-tunnel? boolean?)
(s/def ::no-early-sabrina? boolean?)
(s/def ::randomize-janine? boolean?)
(s/def ::rockets #{:normal :early :rocketless})
(s/def ::speedchoice? boolean?)
(s/def ::logic-options
  (s/keys :req-un [::endgame-condition ::rockets]
          :opt-un [::expanded-logic? ::no-blind-rock-tunnel? ::no-early-sabrina? ::randomize-janine? ::speedchoice?]))

(s/def ::early-bicycle? boolean?)
(s/def ::no-early-super-rod? boolean?)
(s/def ::randomize-badges? boolean?)
(s/def ::randomize-copycat-item? boolean?)
(s/def ::rng #?(:clj (partial instance? java.util.Random)
                :cljs #(throw "rng not currently supported in cljs")))               
(s/def ::swaps-options
  (s/keys :opt-un [::early-bicycle?
                   ::no-early-super-rod?
                   ::randomize-badges?
                   ::randomize-copycat-item?
                   ::rng]))
