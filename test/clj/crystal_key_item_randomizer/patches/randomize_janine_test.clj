(ns crystal-key-item-randomizer.patches.randomize-janine-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches.randomize-janine :refer :all]))

(deftest generate-test
  (testing "by default you don't want it"
    (is (= []
           (generate {}
                     :rng (new java.util.Random 1)))))

  (testing "when you want it"
    (is (= [{:integer_values          
             {:old [10 14 5 3 0 255 255 144 0 207 95 255 255],
              :new [10 11 9 3 0 255 255 144 0 207 95 255 255]},
             :address_range {:end 1664420, :begin 1664407},
             :label
             "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_JANINE_at_10_1"}
            {:integer_values
             {:old [247 11 9 10 0 255 255 144 0 69 96 255 255],
              :new [247 14 5 10 0 255 255 144 0 69 96 255 255]},
             :address_range {:end 1664433, :begin 1664420},
             :label
             "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_FUCHSIA_GYM_1_at_7_5"}]
           (generate {:randomize-janine? true}
                     :rng (new java.util.Random 1)))))

  (testing "when you don't want it"
    (is (= []
           (generate {:randomize-janine? false}
                     :rng (new java.util.Random 1))))))
