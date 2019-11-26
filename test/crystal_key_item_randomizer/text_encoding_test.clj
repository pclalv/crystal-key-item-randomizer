(ns crystal-key-item-randomizer.text-encoding-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.text-encoding :refer :all]))

(def mr-psychic-text-bytes
  [  0 147 140 248 255 127 168 178 127 143 146 152 130 135 136 130 232
    81 136 179 127 172 160 184 127 171 174 182 164 177 127 179 167 164
    79 179 160 177 166 164 179 212 127 146 143 130 139 232 131 132 133
   232  87])

(def mr-psychic-text-string
  "This choice of in-game text was arbitrary."
  "TM29 is PSYCHIC.\\pIt may lower the\\ntarget's SPCL.DEF.\\e")

(deftest gsc-decode-test
  (testing "it decodes the Mr. Psychic text"
    (is (= mr-psychic-text-string
           (->> mr-psychic-text-bytes
                gsc-decode
                (apply str))))))

(deftest gsc-encode-test
  (testing "it encodes the Mr. Psychic text"
    (is (= mr-psychic-text-bytes
           (gsc-encode mr-psychic-text-string)))))
           
