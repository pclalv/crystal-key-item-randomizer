(ns crystal-key-item-randomizer.progression-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.progression :refer :all])
  (:use [crystal-key-item-randomizer.seeds :only [all-items badges]]))

(def vanilla-badge-swaps
  "A map of key items where the key and value are always equal."
  (zipmap badges badges))

(def vanilla-item-swaps
  "A map of key items where the key and value are always equal."
  (zipmap all-items all-items))

(deftest get-swaps-test
  (testing "returns the vanilla swaps"
    (is (= (set all-items)
           (get-swaps vanilla-item-swaps
                      all-items))))

  (testing "returns chocolate swaps"
    (let [shuffled-items (shuffle all-items)
          swaps (zipmap all-items
                        shuffled-items)]
      (is (= (set shuffled-items)
             (get-swaps vanilla-item-swaps
                        all-items))))))

(deftest can-collect-badges?-test
  (defn get-badge-prereqs [badge]
    (->> (crystal-key-item-randomizer.logic/badge-prereqs {})
         (filter #(= badge
                     (%1 :badge)))
         (first)))

  (testing "can collect ZEPHYRBADGE"
    (is (= #{:ZEPHYRBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:badges #{}}
                                               (get-badge-prereqs :ZEPHYRBADGE))))))
  (testing "can collect HIVEBADGE"
    (is (= #{:HIVEBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:badges #{}}
                                               (get-badge-prereqs :HIVEBADGE))))))
  (testing "can collect PLAINBADGE"
    (is (= #{:PLAINBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:goldenrod}
                                                :badges #{}}
                                               (get-badge-prereqs :PLAINBADGE))))))
  (testing "can collect FOGBADGE"
    (is (= #{:FOGBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:ecruteak}
                                                :badges #{}}
                                               (get-badge-prereqs :FOGBADGE))))))
  (testing "can collect STORMBADGE"
    (is (= #{:STORMBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:can-surf
                                                                  :can-strength}
                                                :badges #{}}
                                               (get-badge-prereqs :STORMBADGE))))))
  (testing "can collect MINERALBADGE"
    (is (= #{:MINERALBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:ecruteak}
                                                :items-obtained #{:SECRETPOTION}
                                                :badges #{}}
                                               (get-badge-prereqs :MINERALBADGE))))))
  (testing "can collect GLACIERBADGE"
    (is (= #{:GLACIERBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:ecruteak :can-surf}
                                                :badges #{}}
                                               (get-badge-prereqs :GLACIERBADGE))))))
  (testing "can collect RISINGBADGE"
    (is (= #{:RISINGBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:blackthorn
                                                                  :can-whirlpool
                                                                  :defeat-team-rocket}
                                                :badges #{}}
                                               (get-badge-prereqs :RISINGBADGE))))))
  (testing "can collect BOULDERBADGE"
    (is (= #{:BOULDERBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:pewter}
                                                :badges #{}}
                                               (get-badge-prereqs :BOULDERBADGE))))))
  (testing "can collect CASCADEBADGE"
    (is (= #{:CASCADEBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:kanto :talk-to-power-plant-manager}
                                                :badges #{}}
                                               (get-badge-prereqs :CASCADEBADGE))))))
  (testing "can collect THUNDERBADGE"
    (is (= #{:THUNDERBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:kanto :can-cut}
                                                :badges #{}}
                                               (get-badge-prereqs :THUNDERBADGE))))))
  (testing "can collect RAINBOWBADGE"
    (is (= #{:RAINBOWBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:kanto :can-cut}
                                                :badges #{}}
                                               (get-badge-prereqs :RAINBOWBADGE))))))
  (testing "can collect SOULBADGE"
    (is (= #{:SOULBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:kanto}
                                                :badges #{}}
                                               (get-badge-prereqs :SOULBADGE))))))
  (testing "can collect MARSHBADGE"
    (is (= #{:MARSHBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:kanto}
                                                :badges #{}}
                                               (get-badge-prereqs :MARSHBADGE))))))
  (testing "can collect VOLCANOBADGE"
    (is (= #{:VOLCANOBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:pewter}
                                                :badges #{}}
                                               (get-badge-prereqs :VOLCANOBADGE))))))
  (testing "can collect EARTHBADGE"
    (is (= #{:EARTHBADGE}
           (:badges (can-satisfy-badge-prereq? vanilla-badge-swaps
                                               {:conditions-met #{:pewter}
                                                :badges #{}}
                                               (get-badge-prereqs :EARTHBADGE)))))))

(deftest beatable?-test
  (testing "when not speedchoice, vanilla swaps aren't beatable"
    ;; TODO: eventually fix the code so that it supports vanilla and
    ;; not just speedchoice.
    (is (= false (-> {:item-swaps vanilla-item-swaps :badge-swaps {}}
                     (beatable? {:speedchoice? false})
                     :beatable?))))
  (testing "when speedchoice"
    (testing "vanilla swaps are beatable to red"
      (is (= true (-> (beatable? {:item-swaps vanilla-item-swaps :badge-swaps vanilla-badge-swaps}
                                 {:endgame-condition :defeat-red})
                      :beatable?))))

    (testing "vanilla swaps are beatable to e4"
      (is (= true (-> (beatable? {:item-swaps vanilla-item-swaps :badge-swaps vanilla-badge-swaps}
                                 {:endgame-condition :defeat-elite-4})
                      :beatable?))))

    (testing "the player gets every item"
      (is (= (set all-items) (-> {:item-swaps vanilla-item-swaps :badge-swaps vanilla-badge-swaps}
                                 beatable?
                                 :items-obtained))))

    (testing "the player gets every badge"
      (is (= (set badges) (-> {:item-swaps vanilla-item-swaps :badge-swaps vanilla-badge-swaps}
                              beatable?
                              :badges))))))
