(ns crystal-key-item-randomizer.frontend-test
  (:require [cljs.test :refer (deftest testing is)]
            [crystal-key-item-randomizer.frontend :as frontend])
  (:require-macros [crystal-key-item-randomizer.test-utils :as test-utils]))

(def rom (test-utils/slurp-bytes-at-compile-time "test/fixtures/crystal-speedchoice-v6.gbc"))

(deftest patch-rom-test
  (testing "idempotency"
    (is (= rom (frontend/patch-rom rom {:item-swaps {}
                                        :badge-swaps {}
                                        :patches []})))))
