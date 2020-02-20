(ns crystal-key-item-randomizer.patches-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches :refer :all])
  (:use [crystal-key-item-randomizer.seeds :only [all-items badges]]))

(def UNDERGROUND-ITEM-BALL-SPEEDCHOICE :UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL)

(def speedchoice-item-ball
  (->> speedchoice-patches
       (filter #(= (% "name")
                   (name UNDERGROUND-ITEM-BALL-SPEEDCHOICE)))
       first))

(def vanilla-item-swaps
  "A map of key items where the key and value are always equal."
  (zipmap all-items all-items))

(def vanilla-badge-swaps
  "A map of key items where the key and value are always equal."
  (zipmap badges badges))

(defn subseq? [maybe-subseq seq]
  (-> (some #{maybe-subseq} (partition (count maybe-subseq) 1 seq))
      nil?
      not))

(deftest replace-underground-warehouse-ultra-ball-with-key-item-test
  (testing "when speedchoice"
    (is (= {:integer_values {:old ["*" 1]
                             :new [128 1]}
            :address_range {:begin 514536
                            :end 514538}
            :label "UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL"
            :description "Change the contents of the item ball from ULTRA_BALL\n   to whatever replaces the CARD_KEY (a backup key item so that the\n   player doesn't get softlocked). integer_values.new doesn't exist so\n   that things will fail hard if the patches aren't update properly"}
           (replace-underground-warehouse-ultra-ball-with-key-item {:CARD_KEY :MACHINE_PART}
                                                                   {:speedchoice? true})))))

(deftest generate-test
  (testing "includes undreground warehouse itemball patch"
    (is (subseq? [(replace-underground-warehouse-ultra-ball-with-key-item vanilla-item-swaps {:speedchoice? true})]
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {}))))

  (testing "includes patches for badge checkflag occurrences"
    (is (subseq? [(crystal-key-item-randomizer.patches.badges/replace-checkflag-for-badge :PLAINBADGE {:PLAINBADGE :PLAINBADGE})
                  (crystal-key-item-randomizer.patches.badges/replace-checkflag-for-badge :RISINGBADGE {:RISINGBADGE :RISINGBADGE})]
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {}))))

  (testing "includes giveitem patches"
    (is (subseq? (crystal-key-item-randomizer.patches.text.giveitem/fix-giveitems vanilla-item-swaps)
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {}))))

  (testing "includes received badge text patches"
    (is (subseq? (crystal-key-item-randomizer.patches.text.received-badge/fix-received-badge-texts vanilla-badge-swaps)
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {}))))

  (testing "includes pre-badge blurb patches"
    (is (subseq? crystal-key-item-randomizer.patches.text.gym-leader-post-defeat/pre-badge-blurb-patches
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {}))))

  (testing "includes post-defeat speech patches"
    (is (subseq? crystal-key-item-randomizer.patches.text.gym-leader-post-defeat/post-badge-speech-patches
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {}))))

  (testing "copycat-item"
    (is (= true (subseq? (crystal-key-item-randomizer.patches.copycat/generate :MYSTERY_EGG)
                         (generate {:item-swaps vanilla-item-swaps
                                    :badge-swaps vanilla-badge-swaps
                                    :copycat-item :MYSTERY_EGG}
                                   {}))))
    (is (= #{} (clojure.set/intersection (set (crystal-key-item-randomizer.patches.copycat/generate :LOST_ITEM))
                                         (set (generate {:item-swaps vanilla-item-swaps
                                                         :badge-swaps vanilla-badge-swaps
                                                         :copycat-item :LOST_ITEM}
                                                        {}))))))

  (testing "early-rockets"
    (is (= true (subseq? crystal-key-item-randomizer.patches.rockets/trigger-early
                         (generate {:item-swaps vanilla-item-swaps
                                    :badge-swaps vanilla-badge-swaps}
                                   {:early-rockets? true}))))
    (is (= #{} (clojure.set/intersection (set crystal-key-item-randomizer.patches.rockets/trigger-early)
                                         (set (generate {:item-swaps vanilla-item-swaps
                                                         :badge-swaps vanilla-badge-swaps}
                                                        {:early-rockets? false})))))))
