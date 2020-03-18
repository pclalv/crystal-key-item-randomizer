(ns crystal-key-item-randomizer.patches.text.giveitem-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches.text.giveitem :refer :all])
  (:use [crystal-key-item-randomizer.prng :only [deterministic-shuffle]]))

(deftest fix-giveitems-test
  (testing "it"
    (is (= [{:key-item :MYSTERY_EGG,
             :label "ckir_BEFORE_giveitem_text_MrPokemonsHouse_GotEggText",
             :address_range {:begin 1667591, :end 1667616},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79 140 152 146 147 132 145 152 127 132 134 134 232 87]
                              :new [0 136 179 212 79 140 152 146 147 132 145 152 127 132 134 134 231 87 80 0 0 0 0 0 0]}}
            {:key-item :SECRETPOTION,
             :label "ckir_BEFORE_giveitem_text_ReceivedSecretpotionText",
             :address_range {:begin 647257, :end 647283},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79 146 132 130 145 132 147 143 142 147 136 142 141 232 87]
                              :new [0 136 179 212 79 146 132 130 145 132 147 143 142 147 136 142 141 231 87 80 0 0 0 0 0 0]}}
            {:key-item :LOST_ITEM,
             :label "ckir_BEFORE_giveitem_text_UnknownText_0x191d0a",
             :address_range {:begin 1645842, :end 1645862},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79 84 127 131 142 139 139 232 87],
                              :new [0 136 179 212 79 139 142 146 147 127 136 147 132 140 231 87 80 0 0 0]}}
            {:key-item :BICYCLE,
             :label "ckir_BEFORE_giveitem_text_UnknownText_0x54848",
             :address_range {:begin 346184, :end 346207},
             :integer_values {:old [0 82 127 161 174 177 177 174 182 164 163 127 160 79 129 136 130 152 130 139 132 232 87],
                              :new [0 136 179 212 79 129 136 130 152 130 139 132 231 87 80 0 0 0 0 0 0 0 0]}}
            {:key-item :RED_SCALE,
             :label "ckir_BEFORE_giveitem_text_UnknownText_0x703df",
             :address_range {:begin 460315, :end 460340},
             :integer_values {:old [0 82 127 174 161 179 160 168 173 164 163 127 160 79 145 132 131 127 146 130 128 139 132 232 87]
                              :new [0 136 179 212 79 145 132 131 127 146 130 128 139 132 231 87 80 0 0 0 0 0 0 0 0]}}]
           (fix-giveitems (let [giveitems (map :key-item giveitem-key-item-text-locations)]
                            (zipmap giveitems giveitems)))))))
