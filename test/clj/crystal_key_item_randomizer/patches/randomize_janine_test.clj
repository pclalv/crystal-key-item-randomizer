(ns crystal-key-item-randomizer.patches.randomize-janine-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches.randomize-janine :refer :all]))

(deftest generate-test
  (testing "by default you don't want it"
    (is (= []
           (generate {}
                     :rng (new java.util.Random 1)))))

  (testing "when you want it"
    (is (= [{:label          
             "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_JANINE_at_10_1",
             :address_range {:begin 1664408, :end 1664410},
             :integer_values {:new [6 8], :old [14 5]}}
            {:label
             "FuchsiaGym_MapEventHeader.ckir_BEFORE_person_event_SPRITE_FUCHSIA_GYM_4_at_2_4",
             :address_range {:begin 1664460, :end 1664462},
             :integer_values {:new [14 5], :old [6 8]}}]
           (generate {:randomize-janine? true}
                     :rng (new java.util.Random 1)))))

  (testing "when you don't want it"
    (is (= []
           (generate {:randomize-janine? false}
                     :rng (new java.util.Random 1))))))
