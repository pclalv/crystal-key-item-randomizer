(ns crystal-key-item-randomizer.seeds-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.seeds :refer :all]))

;; TODO: get this to work
;; (deftest generate-test
;;   (testing "spec"
;;     (is (= {} (-> (stest/check `generate))))))

(deftest generate-test
  (testing "some seeds are unbeatable"
    (is (= false
           (-> (generate 1) :seed :beatable?)))))

(deftest generate-random-test
  (testing "parity with generate"
    (is (= (generate 685382526)
           (-> (generate-random {:swaps-options {:early-bicycle? true
                                                 :rng (new java.util.Random 1)}
                                 :logic-options {:endgame-condition :defeat-red
                                                 :fly-by :none
                                                 :rockets :normal}})
               ;; :iterations are not relevant when generating a specific seed
               (dissoc :iterations)))))

  (testing "early-bicycle?"
    (testing "when false"
      (is (= {:item-swaps {:HM_FLASH :OLD_ROD,
                           :SILVER_WING :GOOD_ROD,
                           :LOST_ITEM :BASEMENT_KEY,
                           :ITEMFINDER :LOST_ITEM,
                           :GOOD_ROD :SECRETPOTION,
                           :CARD_KEY :SUPER_ROD,
                           :COIN_CASE :S_S_TICKET,
                           :BLUE_CARD :BLUE_CARD,
                           :CLEAR_BELL :HM_CUT,
                           :SQUIRTBOTTLE :HM_SURF,
                           :HM_WHIRLPOOL :HM_WHIRLPOOL,
                           :RED_SCALE :PASS,
                           :HM_WATERFALL :HM_FLY,
                           :SECRETPOTION :CARD_KEY,
                           :BASEMENT_KEY :MACHINE_PART,
                           :MACHINE_PART :ITEMFINDER,
                           :MYSTERY_EGG :MYSTERY_EGG,
                           :S_S_TICKET :RED_SCALE,
                           :PASS :HM_FLASH,
                           :HM_CUT :COIN_CASE,
                           :HM_FLY :SILVER_WING,
                           :HM_STRENGTH :HM_STRENGTH,
                           :OLD_ROD :SQUIRTBOTTLE,
                           :BICYCLE :HM_WATERFALL,
                           :HM_SURF :CLEAR_BELL,
                           :SUPER_ROD :BICYCLE},
              :id "431529176"}
             (-> (generate-random {:swaps-options {:early-bicycle? false
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal}})
                 :seed
                 (select-keys [:item-swaps :id])))))
    (testing "when true"
      (is (= {:item-swaps {:HM_FLASH :SQUIRTBOTTLE,
                           :SILVER_WING :LOST_ITEM,
                           :LOST_ITEM :HM_STRENGTH,
                           :ITEMFINDER :HM_SURF,
                           :GOOD_ROD :OLD_ROD,
                           :CARD_KEY :GOOD_ROD,
                           :COIN_CASE :CARD_KEY,
                           :BLUE_CARD :HM_WHIRLPOOL,
                           :CLEAR_BELL :S_S_TICKET,
                           :SQUIRTBOTTLE :COIN_CASE,
                           :HM_WHIRLPOOL :HM_WATERFALL,
                           :RED_SCALE :HM_FLY,
                           :HM_WATERFALL :CLEAR_BELL,
                           :SECRETPOTION :MYSTERY_EGG,
                           :BASEMENT_KEY :ITEMFINDER,
                           :MACHINE_PART :BASEMENT_KEY,
                           :MYSTERY_EGG :SILVER_WING,
                           :S_S_TICKET :RED_SCALE,
                           :PASS :BLUE_CARD,
                           :HM_CUT :MACHINE_PART,
                           :HM_FLY :SECRETPOTION,
                           :HM_STRENGTH :HM_CUT,
                           :OLD_ROD :BICYCLE,
                           :BICYCLE :PASS,
                           :HM_SURF :SUPER_ROD,
                           :SUPER_ROD :HM_FLASH},
              :id "685382526"}
             (-> (generate-random {:swaps-options {:early-bicycle? true
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal}})
                 :seed
                 (select-keys [:item-swaps :id]))))))

  (testing "no-early-super-rod?"
    (testing "when true"
      (is (= {:item-swaps {:HM_FLASH :OLD_ROD,
                           :SILVER_WING :GOOD_ROD,
                           :LOST_ITEM :BASEMENT_KEY,
                           :ITEMFINDER :LOST_ITEM,
                           :GOOD_ROD :SECRETPOTION,
                           :CARD_KEY :SUPER_ROD,
                           :COIN_CASE :S_S_TICKET,
                           :BLUE_CARD :BLUE_CARD,
                           :CLEAR_BELL :HM_CUT,
                           :SQUIRTBOTTLE :HM_SURF,
                           :HM_WHIRLPOOL :HM_WHIRLPOOL,
                           :RED_SCALE :PASS,
                           :HM_WATERFALL :HM_FLY,
                           :SECRETPOTION :CARD_KEY,
                           :BASEMENT_KEY :MACHINE_PART,
                           :MACHINE_PART :ITEMFINDER,
                           :MYSTERY_EGG :MYSTERY_EGG,
                           :S_S_TICKET :RED_SCALE,
                           :PASS :HM_FLASH,
                           :HM_CUT :COIN_CASE,
                           :HM_FLY :SILVER_WING,
                           :HM_STRENGTH :HM_STRENGTH,
                           :OLD_ROD :SQUIRTBOTTLE,
                           :BICYCLE :HM_WATERFALL,
                           :HM_SURF :CLEAR_BELL,
                           :SUPER_ROD :BICYCLE},
              :id "431529176"}
             (-> (generate-random {:swaps-options {:no-early-super-rod? true
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal}})
                 :seed
                 (select-keys [:item-swaps :id]))))))

  (testing "no-early-sabrina?"
    (testing "when true"
      (is (= {:item-swaps {:HM_FLASH :HM_WATERFALL,
                           :SILVER_WING :HM_STRENGTH,
                           :LOST_ITEM :MYSTERY_EGG,
                           :ITEMFINDER :HM_SURF,
                           :GOOD_ROD :HM_FLY,
                           :CARD_KEY :COIN_CASE,
                           :COIN_CASE :BASEMENT_KEY,
                           :BLUE_CARD :SQUIRTBOTTLE,
                           :CLEAR_BELL :BICYCLE,
                           :SQUIRTBOTTLE :HM_WHIRLPOOL,
                           :HM_WHIRLPOOL :ITEMFINDER,
                           :RED_SCALE :OLD_ROD,
                           :HM_WATERFALL :RED_SCALE,
                           :SECRETPOTION :PASS,
                           :BASEMENT_KEY :SECRETPOTION,
                           :MACHINE_PART :GOOD_ROD,
                           :MYSTERY_EGG :HM_FLASH,
                           :S_S_TICKET :SILVER_WING,
                           :PASS :CARD_KEY,
                           :HM_CUT :LOST_ITEM,
                           :HM_FLY :CLEAR_BELL,
                           :HM_STRENGTH :SUPER_ROD,
                           :OLD_ROD :BLUE_CARD,
                           :BICYCLE :HM_CUT,
                           :HM_SURF :S_S_TICKET,
                           :SUPER_ROD :MACHINE_PART},
              :badge-swaps {:PLAINBADGE :HIVEBADGE,
                            :MARSHBADGE :EARTHBADGE,
                            :RISINGBADGE :RISINGBADGE,
                            :FOGBADGE :BOULDERBADGE,
                            :ZEPHYRBADGE :FOGBADGE,
                            :RAINBOWBADGE :PLAINBADGE,
                            :STORMBADGE :CASCADEBADGE,
                            :VOLCANOBADGE :RAINBOWBADGE,
                            :SOULBADGE :MINERALBADGE,
                            :EARTHBADGE :ZEPHYRBADGE,
                            :THUNDERBADGE :SOULBADGE,
                            :HIVEBADGE :MARSHBADGE,
                            :MINERALBADGE :THUNDERBADGE,
                            :BOULDERBADGE :STORMBADGE,
                            :CASCADEBADGE :VOLCANOBADGE,
                            :GLACIERBADGE :GLACIERBADGE},
              :id "1546424057"}
             (-> (generate-random {:swaps-options {:randomize-badges? true
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal
                                                   :no-early-sabrina? true}})
                 :seed
                 (select-keys [:item-swaps :badge-swaps :id]))))))

  (testing "randomize-badges?"
    (testing "when true"
      (is (= {:badge-swaps {:PLAINBADGE :HIVEBADGE,
                            :MARSHBADGE :EARTHBADGE,
                            :RISINGBADGE :RISINGBADGE,
                            :FOGBADGE :BOULDERBADGE,
                            :ZEPHYRBADGE :FOGBADGE,
                            :RAINBOWBADGE :PLAINBADGE,
                            :STORMBADGE :CASCADEBADGE,
                            :VOLCANOBADGE :RAINBOWBADGE,
                            :SOULBADGE :MINERALBADGE,
                            :EARTHBADGE :ZEPHYRBADGE,
                            :THUNDERBADGE :SOULBADGE,
                            :HIVEBADGE :MARSHBADGE,
                            :MINERALBADGE :THUNDERBADGE,
                            :BOULDERBADGE :STORMBADGE,
                            :CASCADEBADGE :VOLCANOBADGE,
                            :GLACIERBADGE :GLACIERBADGE},
              :id "1546424057"}
             (-> (generate-random {:swaps-options {:randomize-badges? true
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal}})
                 :seed
                 (select-keys [:badge-swaps :id])))))
    (testing "when false"
      (is (= {:badge-swaps {:PLAINBADGE :PLAINBADGE,
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
                            :GLACIERBADGE :GLACIERBADGE},
              :id "431529176"}
             (-> (generate-random {:swaps-options {:randomize-badges? false
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal}})
                 :seed
                 (select-keys [:badge-swaps :id]))))))

  (testing "randomize-copycat-item?"
    (testing "when true"
      (is (= {:copycat-item :OLD_ROD, :id "431529176"}
             (-> (generate-random {:swaps-options {:randomize-copycat-item? true
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal}})
                 :seed
                 (select-keys [:copycat-item :id])))))
    (testing "when false"
      (is (= {:copycat-item :LOST_ITEM, :id "431529176"}
             (-> (generate-random {:swaps-options {:randomize-copycat-item? false
                                                   :rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal}})
                 :seed
                 (select-keys [:copycat-item :id]))))))

  (testing "rockets"
    (testing "when normal"
      (is (= {:item-swaps {:CARD_KEY :SUPER_ROD,
                           :CLEAR_BELL :HM_CUT,
                           :HM_WHIRLPOOL :HM_WHIRLPOOL,
                           :BASEMENT_KEY :MACHINE_PART},
              :id "431529176"}
             (-> (generate-random {:swaps-options {:rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :normal}})
                 :seed
                 (select-keys [:item-swaps :id])
                 (update :item-swaps select-keys lance-items)))))
    (testing "when early"
      (is (= {:item-swaps {:CARD_KEY :SUPER_ROD,
                           :CLEAR_BELL :HM_CUT,
                           :HM_WHIRLPOOL :HM_WHIRLPOOL,
                           :BASEMENT_KEY :MACHINE_PART},
              :id "431529176"}
             (-> (generate-random {:swaps-options {:rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :early}})
                 :seed
                 (select-keys [:item-swaps :id])
                 (update :item-swaps select-keys lance-items)))))
    (testing "when rocketless"
      (is (= {:item-swaps {:CARD_KEY :ITEMFINDER,
                           :CLEAR_BELL :RED_SCALE,
                           :HM_WHIRLPOOL :MYSTERY_EGG,
                           :BASEMENT_KEY :OLD_ROD},
              :id "907914391"}
             (-> (generate-random {:swaps-options {:rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :rocketless}})
                 :seed
                 (select-keys [:item-swaps :id])
                 (update :item-swaps select-keys lance-items)))))

    (testing "expanded-logic"
      (is (= {:item-swaps {:HM_FLASH :SQUIRTBOTTLE,
                           :SILVER_WING :BICYCLE,
                           :LOST_ITEM :SECRETPOTION,
                           :ITEMFINDER :HM_FLY,
                           :GOOD_ROD :HM_STRENGTH,
                           :CARD_KEY :ITEMFINDER,
                           :COIN_CASE :MACHINE_PART,
                           :BLUE_CARD :HM_SURF,
                           :CLEAR_BELL :RED_SCALE,
                           :SQUIRTBOTTLE :SILVER_WING,
                           :HM_WHIRLPOOL :MYSTERY_EGG,
                           :RED_SCALE :CLEAR_BELL,
                           :HM_WATERFALL :GOOD_ROD,
                           :SECRETPOTION :HM_FLASH,
                           :BASEMENT_KEY :OLD_ROD,
                           :MACHINE_PART :S_S_TICKET,
                           :MYSTERY_EGG :CARD_KEY,
                           :S_S_TICKET :BASEMENT_KEY,
                           :PASS :LOST_ITEM,
                           :HM_CUT :HM_WATERFALL,
                           :HM_FLY :HM_WHIRLPOOL,
                           :HM_STRENGTH :SUPER_ROD,
                           :OLD_ROD :COIN_CASE,
                           :BICYCLE :HM_CUT,
                           :HM_SURF :PASS,
                           :SUPER_ROD :BLUE_CARD},
              :id "907914391"}
             (-> (generate-random {:swaps-options {:rng (new java.util.Random 1)}
                                   :logic-options {:endgame-condition :defeat-red
                                                   :fly-by :none
                                                   :rockets :rocketless
                                                   :expanded-logic? true}})
                 :seed
                 (select-keys [:item-swaps :id])))))))
