(ns crystal-key-item-randomizer.patches.badges-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches.badges :refer :all]))

(deftest replace-checkflag-for-badge-test
  (testing "RISINGBADGE"
    (is (= {:label "BlackthornGymClairScript.ckir_BEFORE_checkflag_ENGINE_RISINGBADGE",
            :address_range {:begin 1658406
                            :end 1658409}
            :integer_values {:old [52 34 0]
                             :new [52 27 0]}}
           (replace-checkflag-for-badge :RISINGBADGE {:RISINGBADGE :ZEPHYRBADGE}))))

  (testing "PLAINBADGE"
    (is (= {:label "WhitneyScript_0x5400c.ckir_BEFORE_checkflag_ENGINE_PLAINBADGE",
            :address_range {:begin 344138
                            :end 344141}
            :integer_values {:old [52 29 0]
                             :new [52 27 0]}}
           (replace-checkflag-for-badge :PLAINBADGE {:PLAINBADGE :ZEPHYRBADGE})))))
