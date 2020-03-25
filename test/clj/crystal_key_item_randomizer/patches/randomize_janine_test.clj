(ns crystal-key-item-randomizer.patches.randomize-janine-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches.randomize-janine :refer :all]))

(deftest generate-test
  (testing "by default you don't want it"
    (is (= []
           (generate {}
                     :rng (new java.util.Random 1)))))

  (testing "when you want it"
    (is (= []
           (generate {:randomize-jasmine? true}
                     :rng (new java.util.Random 1)))))

  (testing "when you don't want it"
    (is (= []
           (generate {:randomize-jasmine? false}
                     :rng (new java.util.Random 1))))))
