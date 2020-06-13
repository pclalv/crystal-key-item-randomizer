(ns crystal-key-item-randomizer.patches-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.patches :refer :all])
  (:use [crystal-key-item-randomizer.seeds :only [all-items badges]]))

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
  (testing "return value"
    (is (= {:integer_values {:old ["*" 1]
                             :new [128 1]}
            :address_range {:begin 514536
                            :end 514538}
            :label "UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL"}
           (replace-underground-warehouse-ultra-ball-with-key-item {:CARD_KEY :MACHINE_PART}
                                                                   {})))))

(deftest generate-test
  (testing "includes undreground warehouse itemball patch"
    (is (subseq? [(replace-underground-warehouse-ultra-ball-with-key-item vanilla-item-swaps {})]
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {:endgame-condition :defeat-red
                            :rockets :normal}))))

  (testing "includes patches for badge checkflag occurrences"
    (is (subseq? [(crystal-key-item-randomizer.patches.badges/replace-checkflag-for-badge :PLAINBADGE {:PLAINBADGE :PLAINBADGE})
                  (crystal-key-item-randomizer.patches.badges/replace-checkflag-for-badge :RISINGBADGE {:RISINGBADGE :RISINGBADGE})]
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {:endgame-condition :defeat-red
                            :rockets :normal}))))

  (testing "includes giveitem patches"
    (is (subseq? (crystal-key-item-randomizer.patches.text.giveitem/fix-giveitems vanilla-item-swaps)
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {:endgame-condition :defeat-red
                            :rockets :normal}))))

  (testing "includes received badge text patches"
    (is (subseq? (crystal-key-item-randomizer.patches.text.received-badge/fix-received-badge-texts vanilla-badge-swaps)
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {:endgame-condition :defeat-red
                            :rockets :normal}))))

  (testing "includes pre-badge blurb patches"
    (is (subseq? crystal-key-item-randomizer.patches.text.gym-leader-post-defeat/pre-badge-blurb-patches
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {:endgame-condition :defeat-red
                            :rockets :normal}))))

  (testing "includes post-defeat speech patches"
    (is (subseq? crystal-key-item-randomizer.patches.text.gym-leader-post-defeat/post-badge-speech-patches
                 (generate {:item-swaps vanilla-item-swaps
                            :badge-swaps vanilla-badge-swaps}
                           {:endgame-condition :defeat-red
                            :rockets :normal}))))

  (testing "copycat-item"
    (is (= true (subseq? (crystal-key-item-randomizer.patches.copycat/generate :MYSTERY_EGG :normal)
                         (generate {:item-swaps vanilla-item-swaps
                                    :badge-swaps vanilla-badge-swaps
                                    :copycat-item :MYSTERY_EGG}
                                   {:endgame-condition :defeat-red
                                    :rockets :normal}))))
    (is (= #{} (clojure.set/intersection (set (crystal-key-item-randomizer.patches.copycat/generate :LOST_ITEM :normal))
                                         (set (generate {:item-swaps vanilla-item-swaps
                                                         :badge-swaps vanilla-badge-swaps
                                                         :copycat-item :LOST_ITEM}
                                                        {:endgame-condition :defeat-red
                                                         :rockets :normal}))))))

  (testing "early rockets"
    (is (= true (subseq? crystal-key-item-randomizer.patches.rockets/trigger-early
                         (generate {:item-swaps vanilla-item-swaps
                                    :badge-swaps vanilla-badge-swaps}
                                   {:endgame-condition :defeat-red
                                    :rockets :early}))))
    (is (= #{} (clojure.set/intersection (set crystal-key-item-randomizer.patches.rockets/trigger-early)
                                         (set (generate {:item-swaps vanilla-item-swaps
                                                         :badge-swaps vanilla-badge-swaps}
                                                        {:endgame-condition :defeat-red
                                                         :rockets :normal})))))))
