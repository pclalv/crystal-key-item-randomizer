(ns crystal-key-item-randomizer.frontend-test
  (:require [cljs.test :refer (deftest testing is)]
            [crystal-key-item-randomizer.frontend :as frontend])
  (:require-macros [crystal-key-item-randomizer.test-utils :as test-utils]))

(def rom (test-utils/slurp-bytes-at-compile-time "test/fixtures/crystal-speedchoice-v6.gbc"))
(def seed-1425133096-with-badges (test-utils/slurp-bytes-at-compile-time "test/fixtures/pokecrystal-key-item-randomized-seed-1425133096-badges.gbc"))
(def seed-1425133096-data (test-utils/slurp-edn "test/fixtures/seed-1425133096.edn"))

(deftest patch-rom-test
  (testing "idempotency"
    (is (= rom (frontend/patch-rom rom {:item-swaps {}
                                        :badge-swaps {}
                                        :patches []}))))

  (testing "actually patching"
    (is (= seed-1425133096-with-badges
         (-> (frontend/patch-rom (js/Uint8Array. rom) seed-1425133096-data)
             array-seq
             vec)))))
