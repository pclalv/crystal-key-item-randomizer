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
