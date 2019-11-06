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
    (is (= {"integer_values" {"old" ["*" 1]
                              "new" [128 1]}
            "address_range" {"begin" 514536
                             "end" 514538}
            "name" "UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL"
            "description" "Change the contents of the item ball from ULTRA_BALL to whatever replaces the CARD_KEY (a backup key item so that the player doesn't get softlocked). integer_values.new doesn't exist so that things will fail hard if the patches aren't update properly"}
           (->> (replace-underground-warehouse-ultra-ball-with-key-item speedchoice-patches
                                                                        {:CARD_KEY :MACHINE_PART}
                                                                        {:speedchoice? true})
                (filter #(= (% "name")
                            (name UNDERGROUND-ITEM-BALL-SPEEDCHOICE)))
                (first))))))
