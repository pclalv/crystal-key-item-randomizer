(ns crystal-key-item-randomizer.patches.badge-text-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches.badge-text :refer :all]))

(deftest fix-received-badge-texts-test
  (testing "it"
    (is (= [{:badge :ZEPHYRBADGE,
             :label "UnknownText_0x685af.ckir_BEFORE_text_ZEPHYRBADGE_received",
             :address_range {:begin 427450, :end 427475},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    153 132 143 135 152 145 129 128 131 134 132
                                    232 87],
                              :new [0 136 179 212 79 153 132 143 135 152 145 129
                                    128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :HIVEBADGE,
             :label "Text_ReceivedHiveBadge.ckir_BEFORE_text_HIVEBADGE_received",
             :address_range {:begin 1633667, :end 1633690},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    135 136 149 132 129 128 131 134 132 232 87],
                              :new [0 136 179 212 79 135 136 149 132 129 128 131
                                    134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :PLAINBADGE,
             :label "UnknownText_0x54273.ckir_BEFORE_text_PLAINBADGE_received",
             :address_range {:begin 344691, :end 344715},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    143 139 128 136 141 129 128 131 134 132 232
                                    87],
                              :new [0 136 179 212 79 143 139 128 136 141 129 128
                                    131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :FOGBADGE,
             :label "UnknownText_0x9a043.ckir_BEFORE_text_FOGBADGE_received",
             :address_range {:begin 630851, :end 630873},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    133 142 134 129 128 131 134 132 232 87],
                              :new [0 136 179 212 79 133 142 134 129 128 131 134
                                    132 232 87 80 0 0 0 0 0 0]}}
            {:badge :STORMBADGE,
             :label "UnknownText_0x9d835.ckir_BEFORE_text_STORMBADGE_received",
             :address_range {:begin 645176, :end 645200},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    146 147 142 145 140 129 128 131 134 132 232
                                    87],
                              :new [0 136 179 212 79 146 147 142 145 140 129 128
                                    131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :MINERALBADGE,
             :label "UnknownText_0x9c33a.ckir_BEFORE_text_MINERALBADGE_received",
             :address_range {:begin 639802, :end 639828},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    140 136 141 132 145 128 139 129 128 131 134
                                    132 232 87],
                              :new [0 136 179 212 79 140 136 141 132 145 128 139
                                    129 128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :GLACIERBADGE,
             :label "UnknownText_0x199d3b.ckir_BEFORE_text_GLACIERBADGE_received",
             :address_range {:begin 1678907, :end 1678933},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    134 139 128 130 136 132 145 129 128 131 134
                                    132 232 87],
                              :new [0 136 179 212 79 134 139 128 130 136 132 145
                                    129 128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :RISINGBADGE,
             :label "ClairText_Lazy.ckir_BEFORE_text_RISINGBADGE_received",
             :address_range {:begin 1659544, :end 1659569},
             :integer_values {:old [81 82 127 177 164 162 164 168 181 164 163 79
                                    145 136 146 136 141 134 129 128 131 134 132
                                    232 87],
                              :new [0 136 179 212 79 145 136 146 136 141 134 129
                                    128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :BOULDERBADGE,
             :label "UnknownText_0x1a2a3d.ckir_BEFORE_text_BOULDERBADGE_received",
             :address_range {:begin 1714769, :end 1714795},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    129 142 148 139 131 132 145 129 128 131 134
                                    132 232 87],
                              :new [0 136 179 212 79 129 142 148 139 131 132 145
                                    129 128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :CASCADEBADGE,
             :label "UnknownText_0x188768.ckir_BEFORE_text_CASCADEBADGE_received",
             :address_range {:begin 1607548, :end 1607574},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    130 128 146 130 128 131 132 129 128 131 134
                                    132 232 87],
                              :new [0 136 179 212 79 130 128 146 130 128 131 132
                                    129 128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :THUNDERBADGE,
             :label "UnknownText_0x192277.ckir_BEFORE_text_THUNDERBADGE_received",
             :address_range {:begin 1647251, :end 1647277},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    147 135 148 141 131 132 145 129 128 131 134
                                    132 232 87],
                              :new [0 136 179 212 79 147 135 148 141 131 132 145
                                    129 128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :RAINBOWBADGE,
             :label "UnknownText_0x72c96.ckir_BEFORE_text_RAINBOWBADGE_received",
             :address_range {:begin 470761, :end 470787},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    145 128 136 141 129 142 150 129 128 131 134
                                    132 232 87],
                              :new [0 136 179 212 79 145 128 136 141 129 142 150
                                    129 128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :SOULBADGE,
             :label "UnknownText_0x195feb.ckir_BEFORE_text_SOULBADGE_received",
             :address_range {:begin 1663509, :end 1663532},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    146 142 148 139 129 128 131 134 132 232 87],
                              :new [0 136 179 212 79 146 142 148 139 129 128 131
                                    134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :MARSHBADGE,
             :label "UnknownText_0x189e95.ckir_BEFORE_text_MARSHBADGE_received",
             :address_range {:begin 1613501, :end 1613525},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    140 128 145 146 135 129 128 131 134 132 232
                                    87],
                              :new [0 136 179 212 79 140 128 145 146 135 129 128
                                    131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :VOLCANOBADGE,
             :label "UnknownText_0x1ab683.ckir_BEFORE_text_VOLCANOBADGE_received",
             :address_range {:begin 1750679, :end 1750705},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    149 142 139 130 128 141 142 129 128 131 134
                                    132 232 87],
                              :new [0 136 179 212 79 149 142 139 130 128 141 142
                                    129 128 131 134 132 232 87 80 0 0 0 0 0 0]}}
            {:badge :EARTHBADGE,
             :label "Text_ReceivedEarthBadge.ckir_BEFORE_text_EARTHBADGE_received",
             :address_range {:begin 633891, :end 633915},
             :integer_values {:old [0 82 127 177 164 162 164 168 181 164 163 79
                                    132 128 145 147 135 129 128 131 134 132 232
                                    87],
                              :new [0 136 179 212 79 132 128 145 147 135 129 128
                                    131 134 132 232 87 80 0 0 0 0 0 0]}}]
           (fix-received-badge-texts [] (let [giveitems (map :badge received-badge-text-locations)]
                                          (zipmap giveitems giveitems)))))))
