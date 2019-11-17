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
    (is (= {:swaps          
            {:HM_FLASH :HM_WATERFALL,
             :SILVER_WING :SECRETPOTION,
             :LOST_ITEM :S_S_TICKET,
             :ITEMFINDER :BASEMENT_KEY,
             :GOOD_ROD :COIN_CASE,
             :CARD_KEY :SILVER_WING,
             :COIN_CASE :BICYCLE,
             :BLUE_CARD :MYSTERY_EGG,
             :CLEAR_BELL :HM_WHIRLPOOL,
             :SQUIRTBOTTLE :BLUE_CARD,
             :HM_WHIRLPOOL :GOOD_ROD,
             :RED_SCALE :HM_FLY,
             :HM_WATERFALL :CLEAR_BELL,
             :SECRETPOTION :SUPER_ROD,
             :BASEMENT_KEY :HM_FLASH,
             :MACHINE_PART :OLD_ROD,
             :MYSTERY_EGG :RED_SCALE,
             :S_S_TICKET :LOST_ITEM,
             :PASS :CARD_KEY,
             :HM_CUT :PASS,
             :HM_FLY :HM_CUT,
             :HM_STRENGTH :HM_STRENGTH,
             :OLD_ROD :HM_SURF,
             :BICYCLE :SQUIRTBOTTLE,
             :HM_SURF :MACHINE_PART,
             :SUPER_ROD :ITEMFINDER},
            :id "471763713"} (-> (generate-random {:early-bicycle? true
                                                   :rng (new java.util.Random 1)})
                                 :seed
                                 (select-keys [:swaps :id])))))

  (testing "when no-early-super-rod? is false"
    (is (= {:swaps          
            {:HM_FLASH :MYSTERY_EGG,
             :SILVER_WING :MACHINE_PART,
             :LOST_ITEM :PASS,
             :ITEMFINDER :OLD_ROD,
             :GOOD_ROD :CARD_KEY,
             :CARD_KEY :HM_CUT,
             :COIN_CASE :HM_FLASH,
             :BLUE_CARD :HM_SURF,
             :CLEAR_BELL :SILVER_WING,
             :SQUIRTBOTTLE :SQUIRTBOTTLE,
             :HM_WHIRLPOOL :BICYCLE,
             :RED_SCALE :HM_STRENGTH,
             :HM_WATERFALL :COIN_CASE,
             :SECRETPOTION :ITEMFINDER,
             :BASEMENT_KEY :BLUE_CARD,
             :MACHINE_PART :CLEAR_BELL,
             :MYSTERY_EGG :HM_WHIRLPOOL,
             :S_S_TICKET :RED_SCALE,
             :PASS :GOOD_ROD,
             :HM_CUT :SECRETPOTION,
             :HM_FLY :SUPER_ROD,
             :HM_STRENGTH :HM_FLY,
             :OLD_ROD :BASEMENT_KEY,
             :BICYCLE :LOST_ITEM,
             :HM_SURF :S_S_TICKET,
             :SUPER_ROD :HM_WATERFALL},
            :id "1559630936"} (-> (generate-random {:no-early-super-rod? true
                                                    :rng (new java.util.Random 1)})
                                  :seed
                                  (select-keys [:swaps :id]))))))
