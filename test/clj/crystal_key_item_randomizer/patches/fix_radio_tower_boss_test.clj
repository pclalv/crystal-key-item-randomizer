(ns crystal-key-item-randomizer.patches.fix-radio-tower-boss-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches.fix-radio-tower-boss :refer :all])
  (:use [crystal-key-item-randomizer.patches.data :only [essential-patches get-patch]]))

(defn subseq? [maybe-subseq seq]
  (-> (some #{maybe-subseq} (partition (count maybe-subseq) 1 seq))
      nil?
      not))

(def v6-patches
  (map get-patch v6-patch-names))

(deftest apply-test
  (testing "when true"
    (is (= '({:integer_values {:new [0 135 180 167 230 127 147 167 164 127 161 174 178 178
                                     127 172 180 178 179 79 167 160 181 164 127 163 177 174
                                     175 175 164 163 127 179 167 168 178 232 87 0 79 160 162
                                     179 168 174 173 178 127 167 160 181 164 127 178 160 181
                                     164 163 81 84 140 142 141 127 173 160 179 168 174 173
                                     227 79 182 168 163 164 232 81 136 127 170 173 174 182
                                     127 168 179 212 127 173 174 179 79 172 180 162 167 244
                                     127 161 180 179 127 175 171 164 160 178 164 85 179 160
                                     170 164 127 179 167 168 178 232 87],
                               :old [0 131 136 145 132 130 147 142 145 156 127 20 244 79 179
                                     167 160 173 170 127 184 174 180 231 81 152 174 180 177
                                     127 162 174 180 177 160 166 164 174 180 178 79 160 162
                                     179 168 174 173 178 127 167 160 181 164 127 178 160 181
                                     164 163 81 84 140 142 141 127 173 160 179 168 174 173
                                     227 79 182 168 163 164 232 81 136 127 170 173 174 182
                                     127 168 179 212 127 173 174 179 79 172 180 162 167 244
                                     127 161 180 179 127 175 171 164 160 178 164 85 179 160
                                     170 164 127 179 167 168 178 232 87]},
              :address_range {:end 394810, :begin 394688},
              :label "RadioTower5FDirectorThankYouText.ckir_BEFORE_RadioTower5FDirectorThankYouText"}
             {:integer_values {:new [84 9 12 1 0 255 255 1 0 12 65 228 6],
                               :old [84 9 12 1 0 255 255 1 0 12 65 205 7]},
              :address_range {:end 395662, :begin 395649},
              :label "RadioTower5F_MapEventHeader.ckir_BEFORE_object_event_EVENT_RADIO_TOWER_5F_ULTRA_BALL"}
             {:integer_values {:new [53 9 17 8 0 255 255 0 0 114 64 206 6],
                               :old [53 9 17 8 0 255 255 0 0 222 39 206 6]},
              :address_range {:end 395623, :begin 395610},
              :label "RadioTower5F_MapEventHeader.ckir_BEFORE_object_event_ObjectEvent_RocketBoss"}
             {:integer_values {:new [127 57 0 71 76 223 67 84 73 100 1 69 0 0 104 3 94 51 1
                                     95 96 71 76 28 69 84 73 15 48 0 15 51 0 110 3 110 4 139
                                     15 15 50 0 51 113 5 51 33 0 53 19 0 51 204 6 51 205 6
                                     51 206 6 50 54 7 53 23 0 50 207 6 50 208 6 51 227 6 50
                                     228 6 15 60 0 14 0 24 48 14 0 24 48 14 0 24 48 14 0 24
                                     48 111 6],
                               :old [127 57 0 118 3 3 71 76 223 67 84 73 100 1 69 0 0 104 3
                                     94 51 1 95 96 71 76 28 69 84 73 15 48 0 15 51 0 110 3
                                     110 4 139 15 15 50 0 51 113 5 51 33 0 53 19 0 51 204 6
                                     51 205 6 51 206 6 50 54 7 53 23 0 50 207 6 50 208 6 51
                                     227 6 50 228 6 15 60 0 110 2 114 2 12 0 111 2 105 2 29
                                     65 118 0 3]},
              :address_range {:end 393429, :begin 393330},
              :label "ckir_BEFORE_RadioTower5FRocketBossScene_NPC_0"}
             {:integer_values {:new [73 20 2 18 4 1 0 51 120 0 51 97 7 145 145 145 145],
                               :old [76 58 70 84 73 20 2 18 4 1 0 51 120 0 51 97 7]},
              :address_range {:end 393454, :begin 393437},
              :label "ckir_BEFORE_RadioTower5FRocketBossScene_NPC_1"})
           (->> (apply essential-patches {:fix-radio-tower-boss? true})
                (filter #(v6-patch-names (:label %)))))))

  (testing "when false"
    (is (= '()
           (->> (apply essential-patches {:fix-radio-tower-boss? false})
                (filter #(v6-patch-names (:label %))))))))
