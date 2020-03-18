(ns crystal-key-item-randomizer.prng-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.prng :refer :all])
  (:use [crystal-key-item-randomizer.seeds :only [all-items]]))

(deftest deterministic-shuffle-test
  (testing "returns a fixed shuffle for a particular seed value"
    (is (= [:CLEAR_BELL :MACHINE_PART :GOOD_ROD :SUPER_ROD :RED_SCALE
            :SQUIRTBOTTLE :OLD_ROD :BICYCLE :ITEMFINDER :LOST_ITEM
            :SILVER_WING :COIN_CASE :PASS :CARD_KEY :MYSTERY_EGG :HM_FLASH
            :HM_STRENGTH :HM_WHIRLPOOL :HM_CUT :BASEMENT_KEY :HM_SURF
            :HM_FLY :HM_WATERFALL :BLUE_CARD :SECRETPOTION :S_S_TICKET]
           (deterministic-shuffle all-items 1)))))
