(ns crystal-key-item-randomizer.patches-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches :refer :all]))

(def speedchoice-item-ball
  (->> speedchoice-patches
       (filter #(= (% "name")
                   (name UNDERGROUND-ITEM-BALL-SPEEDCHOICE)))
       first))

(deftest replace-underground-warehouse-ultra-ball-with-key-item-test
  (testing "when speedchoice"
    (is (= {:integer_values {:old ["*" 1]
                             :new [128 1]}
            :address_range {:begin 514536
                            :end 514538}
            :label "UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL"
            :description "Change the contents of the item ball from ULTRA_BALL to whatever replaces the CARD_KEY (a backup key item so that the player doesn't get softlocked). integer_values.new doesn't exist so that things will fail hard if the patches aren't update properly"}
           (->> (replace-underground-warehouse-ultra-ball-with-key-item speedchoice-patches
                                                                        {:CARD_KEY :MACHINE_PART}
                                                                        {:speedchoice? true})
                (filter #(= (% :label)
                            (name UNDERGROUND-ITEM-BALL-SPEEDCHOICE)))
                (first))))))

(deftest replace-checkflag-for-badge-test
  (testing "RISINGBADGE"
    (is (= {:label "BlackthornGymClairScript.ckir_BEFORE_checkflag_ENGINE_RISINGBADGE",
            :description "Check whatever badge is actually given by Clair, even in badge rando",
            :address_range {:begin 1658406
                            :end 1658409}
            :integer_values {:old [52 34 0]
                             :new [52 27 0]}}
           (first (replace-checkflag-for-badge [] :RISINGBADGE {:RISINGBADGE :ZEPHYRBADGE})))))

  (testing "PLAINBADGE"
    (is (= {:label "WhitneyScript_0x5400c.ckir_BEFORE_checkflag_ENGINE_PLAINBADGE",
            :description "Check whatever badge is actually given by Whitney, even in badge rando",
            :address_range {:begin 344138
                            :end 344141}
            :integer_values {:old [52 29 0]
                             :new [52 27 0]}}
           (first (replace-checkflag-for-badge [] :PLAINBADGE {:PLAINBADGE :ZEPHYRBADGE}))))))
