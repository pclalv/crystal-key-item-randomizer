(ns crystal-key-item-randomizer.progression-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.progression :refer :all]))

(def vanilla-swaps
  "A map of key items where the key and value are always equal."
  (zipmap crystal-key-item-randomizer.randomizer/all-items
          crystal-key-item-randomizer.randomizer/all-items))

(deftest get-swaps-test
  (testing "returns the vanilla swaps"
    (is (= (set crystal-key-item-randomizer.randomizer/all-items)
           (get-swaps vanilla-swaps
                      crystal-key-item-randomizer.randomizer/all-items))))

  (testing "returns chocolate swaps"
    (let [shuffled-items (shuffle crystal-key-item-randomizer.randomizer/all-items)
          swaps (zipmap crystal-key-item-randomizer.randomizer/all-items
                        shuffled-items)]
      (is (= (set shuffled-items)
             (get-swaps vanilla-swaps
                        crystal-key-item-randomizer.randomizer/all-items))))))

(deftest can-collect-badges?-test
  (defn get-badge-prereqs [badge]
    (->> crystal-key-item-randomizer.prereqs/badge-prereqs
         (filter #(= badge
                     (%1 :badge)))
         (first)))

  (testing "can collect ZEPHYRBADGE"
    (is (= #{:ZEPHYRBADGE}
           (:badges (can-satisfy-badge-prereq? {:badges #{}}
                                               (get-badge-prereqs :ZEPHYRBADGE))))))
  (testing "can collect HIVEBADGE"
    (is (= #{:HIVEBADGE}
           (:badges (can-satisfy-badge-prereq? {:badges #{}}
                                               (get-badge-prereqs :HIVEBADGE))))))
  (testing "can collect PLAINBADGE"
    (is (= #{:PLAINBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:goldenrod}
                                                :badges #{}}
                                               (get-badge-prereqs :PLAINBADGE))))))
  (testing "can collect FOGBADGE"
    (is (= #{:FOGBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:ecruteak}
                                                :badges #{}}
                                               (get-badge-prereqs :FOGBADGE))))))
  (testing "can collect STORMBADGE"
    (is (= #{:STORMBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:can-surf
                                                                  :can-strength}
                                                :badges #{}}
                                               (get-badge-prereqs :STORMBADGE))))))
  (testing "can collect MINERALBADGE"
    (is (= #{:MINERALBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:ecruteak}
                                                :items-obtained #{:SECRETPOTION}
                                                :badges #{}}
                                               (get-badge-prereqs :MINERALBADGE))))))
  (testing "can collect GLACIERBADGE"
    (is (= #{:GLACIERBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:can-surf}
                                                :badges #{}}
                                               (get-badge-prereqs :GLACIERBADGE))))))
  (testing "can collect RISINGBADGE"
    (is (= #{:RISINGBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:blackthorn
                                                                  :can-whirlpool
                                                                  :defeat-team-rocket}
                                                :badges #{}}
                                               (get-badge-prereqs :RISINGBADGE))))))
  (testing "can collect BOULDERBADGE"
    (is (= #{:BOULDERBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:pewter}
                                                :badges #{}}
                                               (get-badge-prereqs :BOULDERBADGE))))))
  (testing "can collect CASCADEBADGE"
    (is (= #{:CASCADEBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:kanto :talk-to-power-plant-manager}
                                                :badges #{}}
                                               (get-badge-prereqs :CASCADEBADGE))))))
  (testing "can collect THUNDERBADGE"
    (is (= #{:THUNDERBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:kanto :can-cut}
                                                :badges #{}}
                                               (get-badge-prereqs :THUNDERBADGE))))))
  (testing "can collect RAINBOWBADGE"
    (is (= #{:RAINBOWBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:kanto :can-cut}
                                                :badges #{}}
                                               (get-badge-prereqs :RAINBOWBADGE))))))
  (testing "can collect SOULBADGE"
    (is (= #{:SOULBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:kanto}
                                                :badges #{}}
                                               (get-badge-prereqs :SOULBADGE))))))
  (testing "can collect MARSHBADGE"
    (is (= #{:MARSHBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:kanto}
                                                :badges #{}}
                                               (get-badge-prereqs :MARSHBADGE))))))
  (testing "can collect VOLCANOBADGE"
    (is (= #{:VOLCANOBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:pewter}
                                                :badges #{}}
                                               (get-badge-prereqs :VOLCANOBADGE))))))
  (testing "can collect EARTHBADGE"
    (is (= #{:EARTHBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:pewter}
                                                :badges #{}}
                                               (get-badge-prereqs :EARTHBADGE)))))))

(deftest beatable?-test
  (testing "when not speedchoice, vanilla swaps aren't beatable"
    ;; TODO: eventually fix the code so that it supports vanilla and
    ;; not just speedchoice.
    (is (= false (-> vanilla-swaps
                     (beatable? :speedchoice? false)
                     :beatable?))))
  (testing "when speedchoice, vanilla swaps are beatable"
    (is (= true (-> vanilla-swaps
                    beatable?
                    :beatable?))))

  (testing "When speedchoice, the player gets every item"
    (is (= (set crystal-key-item-randomizer.randomizer/all-items) (-> vanilla-swaps
                                                                      beatable?
                                                                      :swaps
                                                                      vals
                                                                      set)))))
