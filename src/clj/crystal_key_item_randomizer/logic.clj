(ns crystal-key-item-randomizer.logic
  "The logic that in-game progression follows. Mostly, these are prereqs
  that must be satisfied in order to meet conditions, collect badges
  and collect items.")

(defn badge-prereqs
  "A list detailing the various conditions that must be met to acquire a
  particular badge."
  [{:keys [speedchoice?]
    :or {speedchoice? true}}]
  [{:badge :ZEPHYRBADGE}
   {:badge :HIVEBADGE}
   {:badge :PLAINBADGE
    :conditions-met #{:goldenrod}}

   {:badge :FOGBADGE
    :conditions-met #{:ecruteak}}

   {:badge :STORMBADGE
    :conditions-met #{:can-surf :can-strength}}

   {:badge :MINERALBADGE
    :conditions-met #{:ecruteak}
    :items-obtained #{:SECRETPOTION}}

   {:badge :GLACIERBADGE
    :conditions-met #{:can-surf}}

   {:badge :RISINGBADGE
    :conditions-met (if speedchoice?
                      #{:defeat-team-rocket
                        :blackthorn}
                      #{:can-whirlpool
                        :defeat-team-rocket
                        :blackthorn})}

   {:badge :BOULDERBADGE
    :conditions-met #{:pewter}}

   {:badge :CASCADEBADGE
    :conditions-met #{:kanto :talk-to-power-plant-manager}}

   {:badge :THUNDERBADGE
    :conditions-met #{:kanto :can-cut}}

   {:badge :RAINBOWBADGE
    :conditions-met #{:kanto :can-cut}}

   {:badge :SOULBADGE
    :conditions-met #{:kanto}}

   {:badge :MARSHBADGE
    :conditions-met #{:kanto}}

   {:badge :VOLCANOBADGE
    :conditions-met #{:pewter}}

   {:badge :EARTHBADGE
    :conditions-met #{:pewter}}])

(def hm-use-prereqs
  [{:condition :can-flash
    :prereqs {:badges #{:ZEPHYRBADGE}
              :items-obtained #{:HM_FLASH}}}

   {:condition :can-cut
    :prereqs {:badges #{:HIVEBADGE}
              :items-obtained #{:HM_CUT}}}

   {:condition :can-strength
    :prereqs {:badges #{:PLAINBADGE}
              :items-obtained #{:HM_STRENGTH}}}

   {:condition :can-surf
    :prereqs {:badges #{:FOGBADGE}
              :items-obtained #{:HM_SURF}}}

   ;; speedchoice-specific
   {:condition :can-whirlpool
    :prereqs {:badges #{:HM_WHIRLPOOL}
              :items-obtained #{:GLACIERBADGE}}}

   {:condition :can-waterfall
    :prereqs {:badges #{:RISINGBADGE}
              :items-obtained #{:HM_WATERFALL}}}])

(defn condition-prereqs
  ":prereqs is a vector that specifies one or more set of prereqs, each
  one of which is sufficient for the player to meet the :condition."
  [{:keys [no-blind-rock-tunnel? early-rockets?]
    :or {nno-blind-rock-tunnel? true
         early-rockets? false}}]
  [{:condition :goldenrod
    :prereqs {:conditions-met #{}
              :items-obtained #{}}}

   ;; there are TWO ways of getting to ecruteak
   {:condition :ecruteak
    :prereqs {:conditions-met #{}
              :items-obtained #{:SQUIRTBOTTLE}}}
   {:condition :ecruteak
    :prereqs {:conditions-met #{}
              :items-obtained #{:PASS :S_S_TICKET}}}

   {:condition :defeat-red-gyarados
    :prereqs {:conditions-met #{:can-surf :ecruteak}
              :items-obtained #{}}}

   {:condition :trigger-radio-tower-takeover
    ;; TODO: is the PHONE_CARD a prereq here?
    :prereqs {:conditions-met (if early-rockets?
                                #{:four-badges}
                                #{:seven-badges})
              :items-obtained #{}}}

   {:condition :underground-warehouse
    :prereqs {:conditions-met #{:trigger-radio-tower-takeover}
              :items-obtained #{:BASEMENT_KEY}}}

   {:condition :defeat-team-rocket
    :prereqs {:conditions-met #{:trigger-radio-tower-takeover}
              :items-obtained #{:CARD_KEY}}}

   {:condition :blackthorn
    :prereqs {:conditions-met #{:ecruteak :can-strength :trigger-radio-tower-takeover}
              :items-obtained #{}}}

   ;; there are TWO ways of getting to kanto
   {:condition :kanto
    :prereqs {:conditions-met #{:ecruteak}
              :items-obtained #{:S_S_TICKET}}}
   {:condition :kanto
    :prereqs {:conditions-met #{:goldenrod}
              :items-obtained #{:PASS}}}

   ;; if the player doesn't want to do blind rock tunnel, the they
   ;; need to use either Cut or Flash.
   ;; otherwise, there's only one way; it doesn't matter that it's
   ;; listed twice.
   {:condition :talk-to-power-plant-manager
    :prereqs {:conditions-met (if no-blind-rock-tunnel?
                                #{:can-flash :can-surf :kanto}
                                #{:can-surf :kanto})
              :items-obtained #{}}}
   {:condition :talk-to-power-plant-manager
    :prereqs {:conditions-met (if no-blind-rock-tunnel?
                                #{:can-cut :can-surf :kanto}
                                #{:can-surf :kanto})
              :items-obtained #{}}}

   {:condition :fix-power-plant
    :prereqs {:conditions-met #{:talk-to-power-plant-manager}
              :items-obtained #{:MACHINE_PART}}}

   {:condition :pewter
    :prereqs {:conditions-met #{:can-cut}
              :pokegear-cards #{:EXPN_CARD :RADIO_CARD}
              :items-obtained #{}}}

   ;; there are TWO ways of getting to the E4
   {:condition :defeat-elite-4
    :prereqs {:conditions-met #{:pewter}
              :items-obtained #{}}}
   {:condition :defeat-elite-4
    :prereqs {:conditions-met #{:eight-badges :can-surf :can-waterfall}
              :items-obtained #{}}}

   {:condition :defeat-red
    :prereqs {:conditions-met #{:sixteen-badges}
              :items-obtained #{}}}])

(defn item-prereqs
  [{:keys [copycat-item] :or {copycat-item :LOST_ITEM}}]
  [{;; the cuttable tree in Ilex Forest is removed by the randomizer,
    ;; so goldenrod is always accessible
    :conditions-met #{}
    :items-obtained #{}
    :grants #{:MYSTERY_EGG
              :HM_FLASH 
              :OLD_ROD
              :HM_CUT}}

   {:conditions-met #{:goldenrod}
    :items-obtained #{}
    :grants #{:BICYCLE
              :BLUE_CARD 
              :COIN_CASE}}

   {:conditions-met #{:goldenrod}
    :items-obtained #{}
    :badges #{:PLAINBADGE}
    :grants #{:SQUIRTBOTTLE}}

   {:conditions-met #{:ecruteak}
    :items-obtained #{}
    :grants #{:ITEMFINDER
              :HM_SURF
              :GOOD_ROD
              :HM_STRENGTH}}

   {:conditions-met #{:ecruteak :can-surf}
    :items-obtained #{}
    :grants #{:RED_SCALE
              :SECRETPOTION}}

   {:conditions-met #{:ecruteak :can-surf}
    :items-obtained #{}
    :grants #{:HM_WHIRLPOOL}}

   {:conditions-met #{:ecruteak :can-surf :can-strength}
    :items-obtained #{}
    :grants #{:HM_FLY}}

   {:conditions-met #{:seven-badges}
    :items-obtained #{}
    :grants #{:BASEMENT_KEY}}

   {:conditions-met #{:ecruteak :trigger-radio-tower-takeover}
    :items-obtained #{}
    :grants #{:HM_WATERFALL}}

   {:conditions-met #{:defeat-elite-4}
    :items-obtained #{}
    :grants #{:S_S_TICKET}}

   {:conditions-met #{:underground-warehouse}
    :items-obtained #{}
    :grants #{:CARD_KEY}}

   {:conditions-met #{:defeat-team-rocket}
    :items-obtained #{}
    :grants #{:CLEAR_BELL}}

   {:conditions-met #{:kanto}
    :items-obtained #{}
    :grants #{:SUPER_ROD}}

   {:conditions-met #{:talk-to-power-plant-manager}
    :items-obtained #{}
    :grants #{:MACHINE_PART}}

   {:conditions-met #{:kanto}
    :items-obtained #{copycat-item}
    :grants #{:PASS}}

   {:conditions-met #{:fix-power-plant}
    :items-obtained #{}
    :grants #{:LOST_ITEM}}

   {:conditions-met #{:pewter}
    :items-obtained #{}
    :grants #{:SILVER_WING}}])

;; wip - eventually, pokegear cards should count as items

;; TODO: patch out the map card walking around thing
(def pokegear-card-prereqs
  ;; what happens if you get 7 badges but don't have the phone card?
  ;; does the game call you anyway? or do you get softlocked because
  ;; you can't receive the call?
  [{:prereqs {:conditions-met #{}}
    :pokegear-card :PHONE_CARD}

   {:prereqs {:conditions-met #{}}
    :pokegear-card :MAP_CARD}

   {:prereqs {:conditions-met #{:goldenrod}}
    :pokegear-card :RADIO_CARD}

   {:prereqs {:conditions-met #{:fix-power-plant}}
    :pokegear-card :EXPN_CARD}])
