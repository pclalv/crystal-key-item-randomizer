(ns crystal-key-item-randomizer.logic-test
  (:require [clojure.test :refer :all]
            [crystal-key-item-randomizer.logic :refer :all]))

(deftest badge-prereqs-test
  (testing "default value"
    (is (= [{:badge :ZEPHYRBADGE}          
            {:badge :HIVEBADGE}
            {:badge :PLAINBADGE, :conditions-met #{:goldenrod}}
            {:badge :FOGBADGE, :conditions-met #{:ecruteak}}
            {:badge :STORMBADGE,
             :conditions-met #{:ecruteak :can-strength :can-surf}}
            {:badge :MINERALBADGE,
             :conditions-met #{:ecruteak},
             :items-obtained #{:SECRETPOTION}}
            {:badge :GLACIERBADGE, :conditions-met #{:ecruteak :can-surf}}
            {:badge :RISINGBADGE,
             :conditions-met #{:defeat-team-rocket :blackthorn}}
            {:badge :BOULDERBADGE, :conditions-met #{:pewter}}
            {:badge :CASCADEBADGE,
             :conditions-met #{:talk-to-power-plant-manager :kanto}}
            {:badge :THUNDERBADGE, :conditions-met #{:can-cut :kanto}}
            {:badge :THUNDERBADGE, :conditions-met #{:kanto :can-surf}}
            {:badge :RAINBOWBADGE, :conditions-met #{:can-cut :kanto}}
            {:badge :SOULBADGE, :conditions-met #{:kanto}}
            {:badge :MARSHBADGE, :conditions-met #{:kanto}}
            {:badge :VOLCANOBADGE, :conditions-met #{:pewter}}
            {:badge :EARTHBADGE, :conditions-met #{:pewter}}]
           (badge-prereqs {})))))

(deftest condition-prereqs-test
  (testing "default value"
    (is (= [{:condition :goldenrod, :prereqs {:conditions-met #{},
                                              :items-obtained #{}}}
            {:condition :ecruteak, :prereqs {:conditions-met #{},
                                             :items-obtained #{:SQUIRTBOTTLE}}}
            {:condition :ecruteak, :prereqs {:conditions-met #{},
                                             :items-obtained #{:S_S_TICKET :PASS}}}
            {:condition :defeat-red-gyarados, :prereqs {:conditions-met #{:ecruteak :can-surf},
                                                        :items-obtained #{}}}
            {:condition :trigger-radio-tower-takeover, :prereqs {:conditions-met #{:seven-badges},
                                                                 :items-obtained #{}}}
            {:condition :underground-warehouse, :prereqs {:conditions-met #{:trigger-radio-tower-takeover},
                                                          :items-obtained #{:BASEMENT_KEY}}}
            {:condition :defeat-team-rocket, :prereqs {:conditions-met #{:trigger-radio-tower-takeover},
                                                       :items-obtained #{:CARD_KEY}}}
            {:condition :blackthorn, :prereqs {:conditions-met #{:ecruteak :can-strength :trigger-radio-tower-takeover},
                                               :items-obtained #{}}}
            {:condition :kanto, :prereqs {:conditions-met #{:ecruteak},
                                          :items-obtained #{:S_S_TICKET}}}
            {:condition :kanto, :prereqs {:conditions-met #{:goldenrod},
                                          :items-obtained #{:PASS}}}
            {:condition :talk-to-power-plant-manager, :prereqs {:conditions-met #{:can-flash :kanto :can-surf},
                                                                :items-obtained #{}}}
            {:condition :talk-to-power-plant-manager, :prereqs {:conditions-met #{:can-cut :kanto :can-surf},
                                                                :items-obtained #{}}}
            {:condition :fix-power-plant, :prereqs {:conditions-met #{:talk-to-power-plant-manager},
                                                    :items-obtained #{:MACHINE_PART}}} nil
            {:condition :pewter, :prereqs {:conditions-met #{:can-cut}, :pokegear-cards #{:RADIO_CARD :EXPN_CARD},
                                           :items-obtained #{}}}
            {:condition :defeat-elite-4, :prereqs {:conditions-met #{:pewter},
                                                   :items-obtained #{}}}
            {:condition :defeat-elite-4, :prereqs {:conditions-met #{:eight-badges :can-waterfall},
                                                   :items-obtained #{}}}
            {:condition :defeat-red, :prereqs {:conditions-met #{:sixteen-badges},
                                               :items-obtained #{}}}]
           (condition-prereqs {})))))

(deftest item-prereqs-test
  (testing "default value"
    (is (= [{:conditions-met #{:seven-badges},          
             :items-obtained #{},
             :grants #{:BASEMENT_KEY}}
            {:conditions-met #{:underground-warehouse},
             :items-obtained #{},
             :grants #{:CARD_KEY}}
            {:conditions-met #{:defeat-team-rocket},
             :items-obtained #{},
             :grants #{:CLEAR_BELL}}
            {:conditions-met #{:ecruteak :can-surf},
             :items-obtained #{},
             :grants #{:HM_WHIRLPOOL}}
            {:conditions-met #{:ecruteak :trigger-radio-tower-takeover},
             :items-obtained #{},
             :grants #{:HM_WATERFALL}}
            {:conditions-met #{},
             :items-obtained #{},
             :grants #{:HM_FLASH :MYSTERY_EGG :HM_CUT :OLD_ROD}}
            {:conditions-met #{:goldenrod},
             :items-obtained #{},
             :grants #{:COIN_CASE :BLUE_CARD :BICYCLE}}
            {:conditions-met #{:goldenrod},
             :items-obtained #{},
             :badges #{:PLAINBADGE},
             :grants #{:SQUIRTBOTTLE}}
            {:conditions-met #{:ecruteak},
             :items-obtained #{},
             :grants #{:ITEMFINDER :GOOD_ROD :HM_STRENGTH :HM_SURF}}
            {:conditions-met #{:ecruteak :can-surf},
             :items-obtained #{},
             :grants #{:RED_SCALE :SECRETPOTION}}
            {:conditions-met #{:ecruteak :can-strength :can-surf},
             :items-obtained #{},
             :grants #{:HM_FLY}}
            {:conditions-met #{:defeat-elite-4},
             :items-obtained #{},
             :grants #{:S_S_TICKET}}
            {:conditions-met #{:kanto},
             :items-obtained #{},
             :grants #{:SUPER_ROD}}
            {:conditions-met #{:talk-to-power-plant-manager},
             :items-obtained #{},
             :grants #{:MACHINE_PART}}
            {:conditions-met #{:kanto},
             :items-obtained #{:LOST_ITEM},
             :grants #{:PASS}}
            {:conditions-met #{:fix-power-plant},
             :items-obtained #{},
             :grants #{:LOST_ITEM}}
            {:conditions-met #{:pewter},
             :items-obtained #{},
             :grants #{:SILVER_WING}}]
           (item-prereqs {})))))
