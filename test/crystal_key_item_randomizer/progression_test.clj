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
  (testing "can when the player has obtained HM_STRENGTH and has the PLAINBADGE"
    (is (= #{:can-strength}
           (-> {:swaps vanilla-swaps
                :conditions-met #{}
                :items-obtained #{:HM_STRENGTH}
                :badges #{:PLAINBADGE}}
               can-strength?
               :conditions-met))))

  (testing "can't when the player doesn't have PLAINBADGE"
    (is (= #{}
           (-> {:swaps vanilla-swaps
                :conditions-met #{}
                :items-obtained #{:HM_STRENGTH}
                :badges #{}}
               can-strength?
               :conditions-met))))
  (testing "can't when the player doesn't have HM_STRENGTH"
    (is (= #{}
           (-> {:swaps vanilla-swaps
                :conditions-met #{}
                :items-obtained #{}
                :badges #{:PLAINBADGE}}
               can-strength?
               :conditions-met)))))

(deftest can-surf?-test
  (testing "can when the player has FOGBADE and has obtained HM_SURF"
    (is (= {:items-obtained #{:HM_WHIRLPOOL :RED_SCALE :SECRETPOTION :HM_SURF}
            :conditions-met #{:can-surf}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:HM_SURF}
                :conditions-met #{}
                :badges #{:FOGBADGE}}
               can-surf?
               (select-keys [:items-obtained :conditions-met])))))
  (testing "can't when the player has FOGBADGE and has not obtained HM_SURF"
    (is (= {:items-obtained #{}
            :conditions-met #{}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{}
                :conditions-met #{}
                :badges #{:FOGBADGE}}
               can-surf?
               (select-keys [:items-obtained :conditions-met])))))
  (testing "can when the player does not have FOGBADGE and has obtained HM_SURF"
    (is (= {:items-obtained #{:HM_SURF}
            :conditions-met #{}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:HM_SURF}
                :conditions-met #{}
                :badges #{}}
               can-surf?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-whirlpool?-test
  (testing "can when the player has obtained the GLACIERBADGE and HM_WHIRLPOOL"
    (is (= #{:can-whirlpool} (-> {:swaps vanilla-swaps
                                  :items-obtained #{:HM_WHIRLPOOL}
                                  :badges #{:GLACIERBADGE}
                                  :conditions-met #{}}
                                 can-whirlpool?
                                 :conditions-met))))
  (testing "can't when the player has obtained the GLACIERBADGE and not HM_WHIRLPOOL"
    (is (= #{} (-> {:swaps vanilla-swaps
                    :items-obtained #{}
                    :badges #{:GLACIERBADGE}
                    :conditions-met #{}}
                   can-whirlpool?
                   :conditions-met))))
  (testing "can't when the player has not obtained the GLACIERBADGE and has obtained HM_WHIRLPOOL"
    (is (= #{} (-> {:swaps vanilla-swaps
                    :items-obtained #{:HM_WHIRLPOOL}
                    :badges #{}
                    :conditions-met #{}}
                   can-whirlpool?
                   :conditions-met)))))

(deftest can-waterfall?-test
  (testing "can when the player has obtained the RISINGBADGE and HM_WATERFALL"
    (is (= #{:can-waterfall} (-> {:swaps vanilla-swaps
                                  :items-obtained #{:HM_WATERFALL}
                                  :badges #{:RISINGBADGE}
                                  :conditions-met #{}}
                                 can-waterfall?
                                 :conditions-met))))
  (testing "can't when the player has obtained the RISINGBADGE and not HM_WATERFALL"
    (is (= #{} (-> {:swaps vanilla-swaps
                    :items-obtained #{}
                    :badges #{:RISINGBADGE}
                    :conditions-met #{}}
                   can-waterfall?
                   :conditions-met))))
  (testing "can't when the player has not obtained the RISINGBADGE and has obtained HM_WATERFALL"
    (is (= #{} (-> {:swaps vanilla-swaps
                    :items-obtained #{:HM_WATERFALL}
                    :badges #{}
                    :conditions-met #{}}
                   can-waterfall?
                   :conditions-met)))))

(deftest can-reach-blackthorn?-test
  (testing "can when the player can use strength, has defeated team rocket and has reached ecruteak"
    (is (= #{:can-strength :blackthorn :ecruteak
             :trigger-radio-tower-takeover} (-> {:swaps vanilla-swaps
                                                 :items-obtained #{}
                                                 :conditions-met #{:can-strength :trigger-radio-tower-takeover :ecruteak}}
                                                can-reach-blackthorn?
                                                :conditions-met))))
  (testing "can't otherwise"
    (is (= #{} (-> {:swaps vanilla-swaps
                    :items-obtained #{}
                    :conditions-met #{}}
                   can-reach-blackthorn?
                   :conditions-met)))))

(deftest can-trigger-radio-tower-takeover?-test
  (testing "can when the player has obtained johto 7 badges"
    (is (= {:items-obtained #{:BASEMENT_KEY} :conditions-met #{:trigger-radio-tower-takeover}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{}
                :conditions-met #{}
                :badges #{:ZEPHYRBADGE
                          :HIVEBADGE
                          :PLAINBADGE
                          :FOGBADGE
                          :STORMBADGE
                          :MINERALBADGE
                          :GLACIERBADGE}}
               can-trigger-radio-tower-takeover?
               (select-keys [:items-obtained :conditions-met])))))

  (testing "can when the player has obtained 7 kanto badges"
    (is (= {:items-obtained #{:BASEMENT_KEY} :conditions-met #{:trigger-radio-tower-takeover}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{}
                :conditions-met #{}
                :badges #{:BOULDERBADGE
                          :CASCADEBADGE
                          :THUNDERBADGE
                          :RAINBOWBADGE
                          :SOULBADGE
                          :MARSHBADGE
                          :VOLCANOBADGE}}
               can-trigger-radio-tower-takeover?
               (select-keys [:items-obtained :conditions-met])))))

  (testing "can't when the player has not obtained 7 badges"
    (is (= {:items-obtained #{} :conditions-met #{}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{}
                :conditions-met #{}
                :badges #{:BOULDERBADGE
                          :CASCADEBADGE
                          :THUNDERBADGE
                          :RAINBOWBADGE
                          :SOULBADGE
                          :MARSHBADGE}}
               can-trigger-radio-tower-takeover?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-reach-underground-warehouse?-test
  (testing "can when the has obtained the BASEMENT_KEY, and has triggered the takeover"
    (is (= {:items-obtained #{:CARD_KEY :BASEMENT_KEY}
            :conditions-met #{:underground-warehouse :trigger-radio-tower-takeover}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:BASEMENT_KEY}
                :conditions-met #{:trigger-radio-tower-takeover}}
               can-reach-underground-warehouse?
               (select-keys [:items-obtained :conditions-met])))))

  (testing "can't when the has triggered the takeover but has not obtained the BASEMENT KEY"
    (is (= {:items-obtained #{}
            :conditions-met #{:trigger-radio-tower-takeover}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{}
                :conditions-met #{:trigger-radio-tower-takeover}}
               can-reach-underground-warehouse?
               (select-keys [:items-obtained :conditions-met])))))

  (testing "can't when the player has obtained the BASEMENT_KEY, and has not triggered the takekover"
    (is (= {:items-obtained #{:BASEMENT_KEY}, :conditions-met #{}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:BASEMENT_KEY}
                :conditions-met #{}}
               can-reach-underground-warehouse?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-defeat-team-rocket?-test
  (testing "can when the player has obtained the CARD_KEY, and has triggered the takeover"
    (is (= {:items-obtained #{:CARD_KEY :CLEAR_BELL}
            :conditions-met #{:defeat-team-rocket :trigger-radio-tower-takeover}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:CARD_KEY}
                :conditions-met #{:trigger-radio-tower-takeover}}
               can-defeat-team-rocket?
               (select-keys [:items-obtained :conditions-met])))))

  (testing "can't when the player has triggered the takeover and has not obtained the CARD_KEY and"
    (is (= {:items-obtained #{}
            :conditions-met #{:trigger-radio-tower-takeover}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{}
                :conditions-met #{:trigger-radio-tower-takeover}}
               can-defeat-team-rocket?
               (select-keys [:items-obtained :conditions-met])))))

  (testing "can't when has obtained the CARD_KEY and has not triggered the takeover"
    (is (= {:items-obtained #{:CARD_KEY}
            :conditions-met #{}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:CARD_KEY}
                :conditions-met #{}}
               can-defeat-team-rocket?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-reach-kanto?-test
  (testing "reachable when the player has reached goldenrod and has obtained the PASS"
    (is (= {:items-obtained #{:PASS :SUPER_ROD}
            :conditions-met #{:goldenrod :kanto}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:PASS}
                :conditions-met #{:goldenrod}}
               can-reach-kanto?
               (select-keys [:items-obtained :conditions-met])))))
  (testing "reachable when the player has reached ecruteak and has obtained the S_S_TICKER"
    (is (= {:items-obtained #{:S_S_TICKET :SUPER_ROD}
            :conditions-met #{:ecruteak :kanto}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:S_S_TICKET}
                :conditions-met #{:ecruteak}}
               can-reach-kanto?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-fix-power-plant?-test
  (testing "can when the player has the MACHINE PART and has talked to the power plant manager"
    (is (= {:items-obtained #{:LOST_ITEM :MACHINE_PART}
            :conditions-met #{:fix-power-plant :talk-to-power-plant-manager}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:MACHINE_PART}
                :conditions-met #{:talk-to-power-plant-manager}}
               can-fix-power-plant?
               (select-keys [:items-obtained :conditions-met])))))
  (testing "can't when the player does not have the MACHINE PART and has talked to the power plant manager"
    (is (= {:items-obtained #{}
            :conditions-met #{:talk-to-power-plant-manager}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{}
                :conditions-met #{:talk-to-power-plant-manager}}
               can-fix-power-plant?
               (select-keys [:items-obtained :conditions-met])))))
  (testing "can't when the player has the MACHINE PART and has not talked to the power plant manager"
    (is (= {:items-obtained #{:MACHINE_PART}
            :conditions-met #{}}
           (-> {:swaps vanilla-swaps
                :items-obtained #{:MACHINE_PART}
                :conditions-met #{}}
               can-fix-power-plant?
               (select-keys [:items-obtained :conditions-met]))))))

(deftest can-get-copycat-item?-test
  (testing "can when the player has reached kanto and has obtained the LOST_ITEM"
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
                     (beatable? {:speedchoice? false})
                     :beatable?))))
  (testing "when speedchoice, vanilla swaps are beatable"
    (is (= true (-> vanilla-swaps
                    (beatable? {:speedchoice? true})
                    :beatable?))))

  (testing "When speedchoice, the player gets every item"
    (is (= (set crystal-key-item-randomizer.randomizer/all-items) (-> vanilla-swaps
                                                                      (beatable? {:speedchoice? true})
                                                                      :swaps
                                                                      vals
                                                                      set)))))
