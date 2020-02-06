(ns crystal-key-item-randomizer.patches.copycat-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches.copycat :refer :all]))

(deftest replace-checkflag-for-badge-test
  (testing "BLUE_CARD"
    (is (= [{:label "Copycat.ckir_BEFORE_checkitem_LOST_ITEM",
             :integer_values {:old [33 130],
                              :new [33 116]},
             :address_range {:begin 1617643,
                             :end 1617645}}
            {:label "Copycat.ckir_BEFORE_takeitem_LOST_ITEM",
             :integer_values {:old [32 130 1],
                              :new [32 116 1]},
             :address_range {:begin 1617793,
                             :end 1617796}}
            {:label "UnknownText_0x18b064.ckir_BEFORE_para_lost_your_favorite_doll",
             :address_range {:begin 1618094,
                             :end 1618121},
             :integer_values {:old [81 171 174 178 179 127 184 174 180 177 127 165 160 181
                                    174 177 168 179 164 79 84 127 131 142 139 139 232],
                              :new [81 171 174 178 179 127 172 184 79 129 139 148 132 127
                                    130 128 145 131 232 80 0 0 0 0 0 0 0]}}]
           (generate :BLUE_CARD))))

  (testing "LOST_ITEM"
    (is (= [{:label "Copycat.ckir_BEFORE_checkitem_LOST_ITEM",
             :integer_values {:old [33 130],
                              :new [33 130]},
             :address_range {:begin 1617643,
                             :end 1617645}}
            {:label "Copycat.ckir_BEFORE_takeitem_LOST_ITEM",
             :integer_values {:old [32 130 1],
                              :new [32 130 1]},
             :address_range {:begin 1617793,
                             :end 1617796}}
            {:label "UnknownText_0x18b064.ckir_BEFORE_para_lost_your_favorite_doll",
             :address_range {:begin 1618094,
                             :end 1618121},
             :integer_values {:old [81 171 174 178 179 127 184 174 180 177 127 165 160 181
                                    174 177 168 179 164 79 84 127 131 142 139 139 232],
                              :new [81 171 174 178 179 127 172 184 79 139 142 146 147 127
                                    136 147 132 140 232 80 0 0 0 0 0 0 0]}}]
           (generate :LOST_ITEM)))))
