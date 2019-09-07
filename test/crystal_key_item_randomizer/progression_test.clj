(ns crystal-key-item-randomizer.progression-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.progression :refer :all]))

(deftest beatable?-test
  (testing "vanilla swaps are beatable."
    (is (= true (:beatable? (beatable? (zipmap crystal-key-item-randomizer.randomizer/all-items
                                               crystal-key-item-randomizer.randomizer/all-items)))))))
    
