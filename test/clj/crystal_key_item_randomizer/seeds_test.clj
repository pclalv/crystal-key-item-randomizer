(ns crystal-key-item-randomizer.seeds-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.seeds :refer :all]))

;; TODO: get this to work
;; (deftest generate-test
;;   (testing "spec"
;;     (is (= {} (-> (stest/check `generate))))))

(deftest generate-random-test
  (testing "parity with generate"
    (is (= (generate 1359243304)
           (-> (generate-random {:swaps-options {:early-bicycle? true
                                                 :rng (new java.util.Random 1)}
                                 :logic-options {:endgame-condition :defeat-red
                                                 :rockets :normal
                                                 :speedchoice? true}})
               ;; :iterations are not relevant when generating a specific seed
               (dissoc :iterations)))))

  (testing "early-bicycle?"
    (testing "when false"
      (is (= {:item-swaps          
              {:HM_FLASH :HM_STRENGTH,
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
             (-> (generate-random {:swaps-options {:early-bicycle? false
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :rockets :normal
                                                   :speedchoice? true}})
                 :seed
                 (select-keys [:item-swaps :id])))))
    (testing "when true"
      (is (= {:item-swaps {:HM_FLASH :HM_CUT,
                           :SILVER_WING :HM_FLASH,
                           :LOST_ITEM :HM_STRENGTH,
                           :ITEMFINDER :BASEMENT_KEY,
                           :GOOD_ROD :LOST_ITEM,
                           :CARD_KEY :S_S_TICKET,
                           :COIN_CASE :HM_SURF,
                           :BLUE_CARD :RED_SCALE,
                           :CLEAR_BELL :CLEAR_BELL,
                           :SQUIRTBOTTLE :SQUIRTBOTTLE,
                           :HM_WHIRLPOOL :MACHINE_PART,
                           :RED_SCALE :BLUE_CARD,
                           :HM_WATERFALL :SILVER_WING,
                           :SECRETPOTION :COIN_CASE,
                           :BASEMENT_KEY :ITEMFINDER,
                           :MACHINE_PART :HM_FLY,
                           :MYSTERY_EGG :BICYCLE,
                           :S_S_TICKET :GOOD_ROD,
                           :PASS :SECRETPOTION,
                           :HM_CUT :MYSTERY_EGG,
                           :HM_FLY :HM_WHIRLPOOL,
                           :HM_STRENGTH :CARD_KEY,
                           :OLD_ROD :PASS,
                           :BICYCLE :HM_WATERFALL,
                           :HM_SURF :OLD_ROD,
                           :SUPER_ROD :SUPER_ROD},
              :id "1359243304"}
             (-> (generate-random {:swaps-options {:early-bicycle? true
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :rockets :normal
                                                   :speedchoice? true}})
                 :seed
                 (select-keys [:item-swaps :id]))))))

  (testing "no-early-super-rod?"
    (testing "when true"
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
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :rockets :normal
                                                   :speedchoice? true}})
                 :seed
                 (select-keys [:item-swaps :id]))))))

  (testing "no-early-sabrina?"
    (testing "when true"
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
              :badge-swaps {:PLAINBADGE :GLACIERBADGE,
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
                            :GLACIERBADGE :THUNDERBADGE},
              :id "155629808"}
             (-> (generate-random {:swaps-options {:randomize-badges? true
                                                   :rng (new java.util.Random 1)}
                                   
                                   :logic-options {:endgame-condition :defeat-red
                                                   :rockets :normal
                                                   :speedchoice? true
                                                   :no-early-sabrina? true}})
                 :seed
                 (select-keys [:item-swaps :badge-swaps :id]))))))

  (testing "randomize-badges?"
    (testing "when true"
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
                            :GLACIERBADGE :THUNDERBADGE}
              :id "155629808"}
             (-> (generate-random {:swaps-options {:randomize-badges? true
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :rockets :normal
                                                   :speedchoice? true}})
                 :seed
                 (select-keys [:badge-swaps :id])))))
    (testing "when false"
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
               :GLACIERBADGE :GLACIERBADGE}
              :id "155629808"}
             (-> (generate-random {:swaps-options {:randomize-badges? false
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :rockets :normal
                                                   :speedchoice? true}})
                 :seed
                 (select-keys [:badge-swaps :id]))))))

  (testing "randomize-copycat-item?"
    (testing "when true"
      (is (= {:copycat-item :ITEMFINDER, :id "155629808"} (-> (generate-random {:swaps-options {:randomize-copycat-item? true
                                                                                                :rng (new java.util.Random 1)}
                                                                                :logic-options {:endgame-condition :defeat-red
                                                                                                :rockets :normal
                                                                                                :speedchoice? true}})
                                                              :seed
                                                              (select-keys [:copycat-item :id])))))
    (testing "when false"
      (is (= {:copycat-item :LOST_ITEM :id "155629808"} (-> (generate-random {:swaps-options {:randomize-copycat-item? false
                                                                                              :rng (new java.util.Random 1)}
                                                                              :logic-options {:endgame-condition :defeat-red
                                                                                              :rockets :normal
                                                                                              :speedchoice? true}})
                                                            :seed
                                                            (select-keys [:copycat-item :id]))))))

  (testing "rockets"
    (testing "when normal"
      (is (= {:item-swaps
              {:CARD_KEY :BICYCLE,
               :CLEAR_BELL :HM_FLY,
               :HM_WHIRLPOOL :BLUE_CARD,
               :BASEMENT_KEY :SUPER_ROD},
              :id "155629808"}
             (-> (generate-random {:swaps-options {:rng (new java.util.Random 1)}
                                   :logic-options {:rockets :normal
                                                   :endgame-condition :defeat-red
                                                   :speedchoice? true}})
                 :seed
                 (select-keys [:item-swaps :id])
                 (update :item-swaps select-keys lance-items)))))
    (testing "when early"
      (is (= {:item-swaps
              {:CARD_KEY :BICYCLE,
               :CLEAR_BELL :HM_FLY,
               :HM_WHIRLPOOL :BLUE_CARD,
               :BASEMENT_KEY :SUPER_ROD},
              :id "155629808"}
             (-> (generate-random {:swaps-options {:rng (new java.util.Random 1)}
                                   :logic-options {:rockets :early
                                                   :endgame-condition :defeat-red
                                                   :speedchoice? true}})
                 :seed
                 (select-keys [:item-swaps :id])
                 (update :item-swaps select-keys lance-items)))))
    (testing "when rocketless"
      (is (= {:item-swaps          
              {:CARD_KEY :MYSTERY_EGG,
               :CLEAR_BELL :CARD_KEY,
               :HM_WHIRLPOOL :ITEMFINDER,
               :BASEMENT_KEY :BLUE_CARD},
              :id "587682406"}
             (-> (generate-random {:swaps-options {:rng (new java.util.Random 1)}
                                   :logic-options {:rockets :rocketless
                                                   :endgame-condition :defeat-red
                                                   :speedchoice? true}})
                 :seed
                 (select-keys [:item-swaps :id])
                 (update :item-swaps select-keys lance-items)))))))
