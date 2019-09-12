(ns crystal-key-item-randomizer.progression-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.progression :refer :all]))

(deftest get-swaps-test
  (testing "returns the vanilla swaps"
    (is (= (set crystal-key-item-randomizer.randomizer/all-items)
           (get-swaps vanilla-swaps
                      crystal-key-item-randomizer.randomizer/all-items)))))
;; TODO: assert that shuffled swaps are returned

(deftest beatable?-test
  (testing "vanilla swaps are beatable"
    (is (= true (->> vanilla-swaps
                     beatable?
                     :beatable?)))))

(deftest can-reach-goldenrod?-test
  (testing "always adds :goldenrod to conditions-met"
    (is (= #{:goldenrod} (->> {}
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

(deftest can-surf?-test
  (testing "meetable when the player has reached ecruteak and has obtained HM_SURF"
    (is (= {:items-obtained #{:HM_WHIRLPOOL :RED_SCALE :HM_WATERFALL :SECRETPOTION :BASEMENT_KEY :HM_FLY :HM_SURF}
            :conditions-met #{:ecruteak :can-surf}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:HM_SURF}
                :conditions-met #{:ecruteak}}
               can-surf?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-reach-underground-warehouse?-test
  (testing "reachable when the player can surf and has obtained the BASEMENT_KEY"
    (is (= {:items-obtained #{:CARD_KEY :BASEMENT_KEY}
            :conditions-met #{:underground-warehouse :can-surf}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:BASEMENT_KEY}
                :conditions-met #{:can-surf}}
               can-reach-underground-warehouse?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-defeat-team-rocket?-test
  (testing "meetable when the player has reached the underground warehouse and has obtained the CARD_KEY"
    (is (= {:items-obtained #{:CARD_KEY :CLEAR_BELL}
            :conditions-met #{:defeat-team-rocket :underground-warehouse}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:CARD_KEY}
                :conditions-met #{:underground-warehouse}}
               can-defeat-team-rocket?
               (select-keys [:items-obtained :conditions-met]))))))

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
