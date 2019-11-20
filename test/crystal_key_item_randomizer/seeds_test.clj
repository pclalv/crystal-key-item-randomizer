(ns crystal-key-item-randomizer.seeds-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.seeds :refer :all]))

(deftest deterministic-shuffle-test
  (testing "returns a fixed shuffle for a particular seed value"
    (is (= [:HM_FLASH :LOST_ITEM :CLEAR_BELL :S_S_TICKET :HM_SURF :HM_FLY
            :SILVER_WING :SQUIRTBOTTLE :CARD_KEY :BLUE_CARD :BICYCLE :GOOD_ROD
            :OLD_ROD :COIN_CASE :MACHINE_PART :BASEMENT_KEY :SECRETPOTION
            :HM_STRENGTH :PASS :ITEMFINDER :SUPER_ROD :RED_SCALE :HM_WATERFALL
            :HM_CUT :HM_WHIRLPOOL :MYSTERY_EGG]
           (deterministic-shuffle crystal-key-item-randomizer.randomizer/all-items 1)))))

(deftest generate-random-test
  (testing "when early-bicycle? is true"
    (is (= {:id "155629808"
            :swaps {:HM_FLASH :RED_SCALE
                    :SILVER_WING :HM_FLY
                    :LOST_ITEM :ITEMFINDER
                    :ITEMFINDER :S_S_TICKET
                    :GOOD_ROD :GOOD_ROD
                    :CARD_KEY :HM_WHIRLPOOL
                    :COIN_CASE :SQUIRTBOTTLE
                    :BLUE_CARD :HM_WATERFALL
                    :CLEAR_BELL :HM_FLASH
                    :SQUIRTBOTTLE :HM_STRENGTH
                    :HM_WHIRLPOOL :PASS
                    :RED_SCALE :MACHINE_PART
                    :HM_WATERFALL :LOST_ITEM
                    :SECRETPOTION :OLD_ROD
                    :BASEMENT_KEY :SECRETPOTION
                    :MACHINE_PART :SILVER_WING
                    :MYSTERY_EGG :BLUE_CARD
                    :S_S_TICKET :COIN_CASE
                    :PASS :CARD_KEY
                    :HM_CUT :SUPER_ROD
                    :HM_FLY :CLEAR_BELL
                    :HM_STRENGTH :HM_CUT
                    :OLD_ROD :BICYCLE
                    :BICYCLE :MYSTERY_EGG
                    :HM_SURF :BASEMENT_KEY
                    :SUPER_ROD :HM_SURF}}
           (-> (generate-random {:early-bicycle? true
                                 :rng (new java.util.Random 1)})
               :seed
               (select-keys [:swaps :id])))))

  (testing "when no-early-super-rod? is false"
    (is (= {:id "1749940626"
            :swaps {:HM_FLASH :COIN_CASE
                    :SILVER_WING :CARD_KEY
                    :LOST_ITEM :HM_CUT
                    :ITEMFINDER :MYSTERY_EGG
                    :GOOD_ROD :SILVER_WING
                    :CARD_KEY :HM_FLY
                    :COIN_CASE :HM_FLASH
                    :BLUE_CARD :PASS
                    :CLEAR_BELL :SUPER_ROD
                    :SQUIRTBOTTLE :SQUIRTBOTTLE
                    :HM_WHIRLPOOL :OLD_ROD
                    :RED_SCALE :GOOD_ROD
                    :HM_WATERFALL :SECRETPOTION
                    :SECRETPOTION :LOST_ITEM
                    :BASEMENT_KEY :ITEMFINDER
                    :MACHINE_PART :MACHINE_PART
                    :MYSTERY_EGG :HM_WHIRLPOOL
                    :S_S_TICKET :BLUE_CARD
                    :PASS :S_S_TICKET
                    :HM_CUT :HM_STRENGTH
                    :HM_FLY :RED_SCALE
                    :HM_STRENGTH :HM_WATERFALL
                    :OLD_ROD :HM_SURF
                    :BICYCLE :BASEMENT_KEY
                    :HM_SURF :BICYCLE
                    :SUPER_ROD :CLEAR_BELL}}
           (-> (generate-random {:no-early-super-rod? true
                                 :rng (new java.util.Random 1)})
               :seed
               (select-keys [:swaps :id]))))))
