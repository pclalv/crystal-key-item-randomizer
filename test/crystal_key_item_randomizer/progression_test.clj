(ns crystal-key-item-randomizer.progression-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.progression :refer :all]))

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

(deftest can-reach-goldenrod?-test
  (testing "always adds :goldenrod to conditions-met"
    (is (= #{:goldenrod} (->> {:conditions-met #{}}
                              can-reach-goldenrod?
                              :conditions-met))))
  (testing "adds items that the player can obtain"
    (is (= #{:BICYCLE :BLUE_CARD :COIN_CASE :SQUIRTBOTTLE} (->> {:swaps vanilla-swaps}
                                                                can-reach-goldenrod?
                                                                :items-obtained)))))

(deftest can-reach-ecruteak?-test
  (testing "reachable when the player has the SQUIRTBOTTLE"
    (is (= {:conditions-met #{:ecruteak}
            :items-obtained #{:ITEMFINDER :GOOD_ROD :SQUIRTBOTTLE :HM_STRENGTH :HM_SURF}}
           (-> {:swaps vanilla-swaps :items-obtained #{:SQUIRTBOTTLE} :conditions-met #{}}
               can-reach-ecruteak?
               (select-keys [:items-obtained :conditions-met])))))

  (testing "reachable when the player has also reached goldenrod and has obtained the PASS and S_S_TICKET"
    (is (= {:items-obtained #{:ITEMFINDER :GOOD_ROD :S_S_TICKET :PASS :HM_STRENGTH :HM_SURF :SUPER_ROD}
            :conditions-met #{:ecruteak :goldenrod :kanto}}
           (-> {:swaps vanilla-swaps :items-obtained #{:PASS :S_S_TICKET} :conditions-met #{:goldenrod}}
               can-reach-ecruteak?
               (select-keys [:items-obtained :conditions-met])))))

  (testing (str "reachable when the player has reached goldenrod, "
                "has obtained the PASS, "
                "and can obtain the S_S_TICKET in kanto")
    (is (= {:items-obtained #{:ITEMFINDER :GOOD_ROD :S_S_TICKET :PASS :HM_STRENGTH :HM_SURF}
            :conditions-met #{:ecruteak :goldenrod :kanto}}
           (-> {:swaps (-> vanilla-swaps
                           (assoc :SUPER_ROD :S_S_TICKET))
                :items-obtained #{:PASS}
                :conditions-met #{:goldenrod}}
               can-reach-ecruteak?
               (select-keys [:items-obtained :conditions-met])))))

  (testing (str "reachable when the player has reached goldenrod, "
                "has obtained the PASS, "
                "and can obtain the SQUIRTBOTTLE in Kanto")
    (is (= {:items-obtained #{:ITEMFINDER :GOOD_ROD :SQUIRTBOTTLE :PASS :HM_STRENGTH :HM_SURF}
            :conditions-met #{:ecruteak :goldenrod :kanto}}
           (-> {:swaps (-> vanilla-swaps
                           (assoc :SUPER_ROD :SQUIRTBOTTLE))
                :items-obtained #{:PASS}
                :conditions-met #{:goldenrod}}
               can-reach-ecruteak?
               (select-keys [:items-obtained :conditions-met])))))

  (testing (str "reachable when the player has reached goldenrod, "
                "has obtained the PASS, "
                "can obtain the LOST_ITEM in Kanto "
                "and the Copycat's item is the S_S_TICKET")
    (is (= {:items-obtained #{:LOST_ITEM :ITEMFINDER :GOOD_ROD :S_S_TICKET :PASS :HM_STRENGTH :HM_SURF}
            :conditions-met #{:ecruteak :copycat-item :goldenrod :kanto}}
           (-> {:swaps (-> vanilla-swaps
                           (assoc :SUPER_ROD :LOST_ITEM)
                           (assoc :PASS :S_S_TICKET))
                :items-obtained #{:PASS}
                :conditions-met #{:goldenrod}}
               can-reach-ecruteak?
               (select-keys [:items-obtained :conditions-met])))))

  (testing (str "reachable when the player has reached goldenrod, "
                "has obtained the PASS, "
                "can obtain the LOST_ITEM in Kanto "
                "and the Copycat's item is the SQUIRTBOTTLE")
    (is (= {:items-obtained #{:LOST_ITEM :ITEMFINDER :GOOD_ROD :SQUIRTBOTTLE :PASS :HM_STRENGTH :HM_SURF}
            :conditions-met #{:ecruteak :copycat-item :goldenrod :kanto}}
           (-> {:swaps (-> vanilla-swaps
                           (assoc :SUPER_ROD :LOST_ITEM)
                           (assoc :PASS :SQUIRTBOTTLE))
                :items-obtained #{:PASS}
                :conditions-met #{:goldenrod}}
               can-reach-ecruteak?
               (select-keys [:items-obtained :conditions-met])))))

  (testing (str "reachable when the player has reached goldenrod, "
                "has obtained the PASS and LOST_ITEM, "
                "and the Copycat's item is the S_S_TICKET")
    (is (= {:items-obtained #{:LOST_ITEM :ITEMFINDER :GOOD_ROD :S_S_TICKET :PASS :HM_STRENGTH :HM_SURF :SUPER_ROD}
            :conditions-met #{:ecruteak :copycat-item :goldenrod :kanto}}
           (-> {:swaps (-> vanilla-swaps
                           (assoc :PASS :S_S_TICKET))
                :items-obtained #{:PASS :LOST_ITEM}
                :conditions-met #{:goldenrod}}
               can-reach-ecruteak?
               (select-keys [:items-obtained :conditions-met])))))

  (testing (str "reachable when the player has reached goldenrod, "
                "has obtained the PASS and LOST_ITEM, "
                "and the Copycat's item is the SQUIRTBOTTLE")
    (is (= {:items-obtained #{:LOST_ITEM :ITEMFINDER :GOOD_ROD :SQUIRTBOTTLE :PASS :HM_STRENGTH :HM_SURF :SUPER_ROD}
            :conditions-met #{:ecruteak :copycat-item :goldenrod :kanto}}
           (-> {:swaps (-> vanilla-swaps
                           (assoc :PASS :SQUIRTBOTTLE))
                :items-obtained #{:PASS :LOST_ITEM}
                :conditions-met #{:goldenrod}}
               can-reach-ecruteak?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-strength?-test
  (testing "pls fix"
    (is (= true false))))

(deftest can-surf?-test
  (testing "meetable when the player has reached ecruteak and has obtained HM_SURF"
    (is (= {:items-obtained #{:HM_WHIRLPOOL :RED_SCALE :HM_WATERFALL :SECRETPOTION :BASEMENT_KEY :HM_FLY :HM_SURF}
            :conditions-met #{:ecruteak :can-surf}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:HM_SURF}
                :conditions-met #{:ecruteak}}
               can-surf?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-whirlpool?-test
  (testing "meetable when the player has obtained the GLACIERBADGE and HM_WHIRLPOOL"
    (is (= #{:can-whirlpool} (-> {:swaps vanilla-swaps
                                  :items-obtained #{:HM_WHIRLPOOL}
                                  :badges #{:GLACIERBADGE}
                                  :conditions-met #{}}
                                 can-whirlpool?
                                 :conditions-met)))))

(deftest can-waterfall?-test
  (testing "meetable when the player has obtained the RISINGBADGE and HM_WATERFALL"
    (is (= #{:can-waterfall} (-> {:swaps vanilla-swaps
                                  :items-obtained #{:HM_WATERFALL}
                                  :badges #{:RISINGBADGE}
                                  :conditions-met #{}}
                                 can-waterfall?
                                 :conditions-met)))))

(deftest can-reach-underground-warehouse?-test
  (testing "reachable when the has obtained the BASEMENT_KEY, and has obtained 7 johto badges"
    (is (= {:items-obtained #{:CARD_KEY :BASEMENT_KEY}
            :conditions-met #{:underground-warehouse}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:BASEMENT_KEY}
                :conditions-met #{}
                :badges #{:ZEPHYRBADGE
                          :HIVEBADGE
                          :PLAINBADGE
                          :FOGBADGE
                          :STORMBADGE
                          :MINERALBADGE
                          :GLACIERBADGE}}
               can-reach-underground-warehouse?
               (select-keys [:items-obtained :conditions-met])))))
  (testing "not reachable when the player has obtained the BASEMENT_KEY, and doesn't have 7 johto badges"
    (is (= #{}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:BASEMENT_KEY}
                :conditions-met #{}
                :badges #{:ZEPHYRBADGE
                          :HIVEBADGE
                          :PLAINBADGE
                          :FOGBADGE
                          :STORMBADGE
                          :MINERALBADGE}}
               can-reach-underground-warehouse?
               :conditions-met)))))

(deftest can-defeat-team-rocket?-test
  (testing "meetable when the player has reached the underground warehouse, has obtained the CARD_KEY, and has 7 badges"
    (is (= {:items-obtained #{:CARD_KEY :CLEAR_BELL}
            :conditions-met #{:defeat-team-rocket :underground-warehouse}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:CARD_KEY}
                :conditions-met #{:underground-warehouse}
                :badges #{:ZEPHYRBADGE :HIVEBADGE :PLAINBADGE :FOGBADGE
                          :STORMBADGE :MINERALBADGE :GLACIERBADGE}}
               can-defeat-team-rocket?
               (select-keys [:items-obtained :conditions-met])))))
  (testing "not meetable when the player has reached the underground warehouse, has obtained the CARD_KEY, and doesn't have 7 johto badges"
    (is (= true false))))

(deftest can-reach-kanto?-test
  (testing "reachable when the player has reached goldenrod and has obtained the PASS"
    (is (= {:items-obtained #{:MACHINE_PART :PASS :SUPER_ROD}
            :conditions-met #{:goldenrod :kanto}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:PASS}
                :conditions-met #{:goldenrod}}
               can-reach-kanto?
               (select-keys [:items-obtained :conditions-met])))))
  (testing "reachable when the player has reached ecruteak and has obtained the PASS"
    (is (= {:items-obtained #{:MACHINE_PART :S_S_TICKET :SUPER_ROD}
            :conditions-met #{:ecruteak :kanto}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:S_S_TICKET}
                :conditions-met #{:ecruteak}}
               can-reach-kanto?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-fix-power-plant?-test
  (testing "meetable when the player has reached kanto and can surf"
    (is (= {:items-obtained #{:SILVER_WING :LOST_ITEM :MACHINE_PART}
            :conditions-met #{:fix-power-plant :kanto :can-surf}}
           (-> {:swaps vanilla-swaps
                :conditions-met #{:can-surf :kanto}}
               can-fix-power-plant?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-get-copycat-item?-test
  (testing "obtainable when the player has reached kanto and has obtained the LOST_ITEM"
    (is (= {:items-obtained #{:LOST_ITEM :PASS}
            :conditions-met #{:copycat-item :kanto}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:LOST_ITEM}
                :conditions-met #{:kanto}}
               can-get-copycat-item?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-collect-badges?-test
  (defn get-badge-prereqs [badge]
    (->> crystal-key-item-randomizer.conditions/badge-prereqs
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
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:defeat-team-rocket
                                                                  :can-strength
                                                                  :can-whirlpool}
                                                :badges #{}}
                                               (get-badge-prereqs :RISINGBADGE))))))
  (testing "can collect BOULDERBADGE"
    (is (= #{:BOULDERBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:fix-power-plant}
                                                :badges #{}}
                                               (get-badge-prereqs :BOULDERBADGE))))))
  (testing "can collect CASCADEBADGE"
    (is (= #{:CASCADEBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:kanto}
                                                :badges #{}}
                                               (get-badge-prereqs :CASCADEBADGE))))))
  (testing "can collect THUNDERBADGE"
    (is (= #{:THUNDERBADGE}
           (:badges (can-satisfy-badge-prereq? {:items-obtained #{:HM_CUT}
                                                :conditions-met #{:kanto}
                                                :badges #{}}
                                               (get-badge-prereqs :THUNDERBADGE))))))
  (testing "can collect RAINBOWBADGE"
    (is (= #{:RAINBOWBADGE}
           (:badges (can-satisfy-badge-prereq? {:items-obtained #{:HM_CUT}
                                                :conditions-met #{:kanto}
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
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:fix-power-plant}
                                                :badges #{}}
                                               (get-badge-prereqs :VOLCANOBADGE))))))
  (testing "can collect EARTHBADGE"
    (is (= #{:EARTHBADGE}
           (:badges (can-satisfy-badge-prereq? {:conditions-met #{:fix-power-plant}
                                                :badges #{}}
                                               (get-badge-prereqs :EARTHBADGE)))))))

(deftest beatable?-test
  (testing "vanilla swaps are beatable"
    (is (= true (->> vanilla-swaps
                     beatable?
                     :beatable?)))))
