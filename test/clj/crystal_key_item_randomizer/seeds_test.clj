(ns crystal-key-item-randomizer.seeds-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.seeds :refer :all]))

(deftest deterministic-shuffle-test
  (testing "returns a fixed shuffle for a particular seed value"
    (is (= [:CLEAR_BELL :MACHINE_PART :GOOD_ROD :SUPER_ROD :RED_SCALE
            :SQUIRTBOTTLE :OLD_ROD :BICYCLE :ITEMFINDER :LOST_ITEM
            :SILVER_WING :COIN_CASE :PASS :CARD_KEY :MYSTERY_EGG :HM_FLASH
            :HM_STRENGTH :HM_WHIRLPOOL :HM_CUT :BASEMENT_KEY :HM_SURF
            :HM_FLY :HM_WATERFALL :BLUE_CARD :SECRETPOTION :S_S_TICKET]
           (deterministic-shuffle all-items 1)))))

(deftest generate-random-test
  (testing "parity with generate"
    (is (= (generate 2092024379)
           (as-> (generate-random {:swaps-options {:early-bicycle? true
                                                   :rng (new java.util.Random 1)}}) result
             ;; :iterations and [:seed :options] are not crucial
             (dissoc result :iterations)
             (update-in result [:seed] dissoc :options)))))

  (testing "when early-bicycle? is true"
    (is (= {:item-swaps {:HM_FLASH :BICYCLE,
                         :SILVER_WING :COIN_CASE,
                         :LOST_ITEM :MACHINE_PART,
                         :ITEMFINDER :S_S_TICKET,
                         :GOOD_ROD :SECRETPOTION,
                         :CARD_KEY :HM_WATERFALL,
                         :COIN_CASE :SUPER_ROD,
                         :BLUE_CARD :SQUIRTBOTTLE,
                         :CLEAR_BELL :HM_CUT,
                         :SQUIRTBOTTLE :HM_SURF,
                         :HM_WHIRLPOOL :OLD_ROD,
                         :RED_SCALE :HM_STRENGTH,
                         :HM_WATERFALL :BLUE_CARD,
                         :SECRETPOTION :GOOD_ROD,
                         :BASEMENT_KEY :HM_WHIRLPOOL,
                         :MACHINE_PART :BASEMENT_KEY,
                         :MYSTERY_EGG :LOST_ITEM,
                         :S_S_TICKET :HM_FLY,
                         :PASS :PASS,
                         :HM_CUT :RED_SCALE,
                         :HM_FLY :SILVER_WING,
                         :HM_STRENGTH :MYSTERY_EGG,
                         :OLD_ROD :HM_FLASH,
                         :BICYCLE :ITEMFINDER,
                         :HM_SURF :CARD_KEY,
                         :SUPER_ROD :CLEAR_BELL},
            :id "2092024379"}
           (-> (generate-random {:swaps-options {:early-bicycle? true
                                                 :rng (new java.util.Random 1)}})
               :seed
               (select-keys [:item-swaps :id])))))

  (testing "when no-early-super-rod? is false"
    (is (= {:item-swaps {:HM_FLASH :HM_STRENGTH,
                         :SILVER_WING :S_S_TICKET,
                         :LOST_ITEM :HM_WATERFALL,
                         :ITEMFINDER :SECRETPOTION,
                         :GOOD_ROD :CLEAR_BELL,
                         :CARD_KEY :BICYCLE,
                         :COIN_CASE :COIN_CASE,
                         :BLUE_CARD :HM_SURF,
                         :CLEAR_BELL :HM_FLY,
                         :SQUIRTBOTTLE :GOOD_ROD,
                         :HM_WHIRLPOOL :BLUE_CARD,
                         :RED_SCALE :HM_FLASH,
                         :HM_WATERFALL :MACHINE_PART,
                         :SECRETPOTION :HM_CUT,
                         :BASEMENT_KEY :SUPER_ROD,
                         :MACHINE_PART :BASEMENT_KEY,
                         :MYSTERY_EGG :OLD_ROD,
                         :S_S_TICKET :LOST_ITEM,
                         :PASS :SILVER_WING,
                         :HM_CUT :ITEMFINDER,
                         :HM_FLY :MYSTERY_EGG,
                         :HM_STRENGTH :PASS,
                         :OLD_ROD :SQUIRTBOTTLE,
                         :BICYCLE :HM_WHIRLPOOL,
                         :HM_SURF :RED_SCALE,
                         :SUPER_ROD :CARD_KEY},
            :id "155629808"}
           (-> (generate-random {:swaps-options {:no-early-super-rod? true
                                                 :rng (new java.util.Random 1)}})
               :seed
               (select-keys [:item-swaps :id])))))

  (testing "when randomize-badges? is true"
    (is (= {:badge-swaps {:PLAINBADGE :GLACIERBADGE,
                          :MARSHBADGE :HIVEBADGE,
                          :RISINGBADGE :SOULBADGE,
                          :FOGBADGE :EARTHBADGE,
                          :ZEPHYRBADGE :RAINBOWBADGE,
                          :RAINBOWBADGE :BOULDERBADGE,
                          :STORMBADGE :CASCADEBADGE,
                          :VOLCANOBADGE :RISINGBADGE,
                          :SOULBADGE :STORMBADGE,
                          :EARTHBADGE :MINERALBADGE,
                          :THUNDERBADGE :VOLCANOBADGE,
                          :HIVEBADGE :ZEPHYRBADGE,
                          :MINERALBADGE :FOGBADGE,
                          :BOULDERBADGE :PLAINBADGE,
                          :CASCADEBADGE :MARSHBADGE,
                          :GLACIERBADGE :THUNDERBADGE}}
           (-> (generate-random {:swaps-options {:randomize-badges? true
                                                 :rng (new java.util.Random 1)}})
               :seed
               (select-keys [:badge-swaps])))))

  (testing "when randomize-badges? is false"
    (is (= {:badge-swaps
            {:PLAINBADGE :PLAINBADGE,
             :MARSHBADGE :MARSHBADGE,
             :RISINGBADGE :RISINGBADGE,
             :FOGBADGE :FOGBADGE,
             :ZEPHYRBADGE :ZEPHYRBADGE,
             :RAINBOWBADGE :RAINBOWBADGE,
             :STORMBADGE :STORMBADGE,
             :VOLCANOBADGE :VOLCANOBADGE,
             :SOULBADGE :SOULBADGE,
             :EARTHBADGE :EARTHBADGE,
             :THUNDERBADGE :THUNDERBADGE,
             :HIVEBADGE :HIVEBADGE,
             :MINERALBADGE :MINERALBADGE,
             :BOULDERBADGE :BOULDERBADGE,
             :CASCADEBADGE :CASCADEBADGE,
             :GLACIERBADGE :GLACIERBADGE}}
           (-> (generate-random {:swaps-options {:randomize-badges? false}})
               :seed
               (select-keys [:badge-swaps]))))))
