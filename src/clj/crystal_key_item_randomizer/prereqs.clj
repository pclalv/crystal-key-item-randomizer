(ns crystal-key-item-randomizer.prereqs
  "Data pertaining to the prereqs that must be satisfied in order to
  meet conditions, collect badges and collect items.")

(def badge-prereqs
  "A list detailing the various conditions that must be met to acquire a
  particular badge."
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
    :conditions-met #{;; can-whirlpool isn't actually a prereq in
                      ;; speedchoice. speedchoice logic will just
                      ;; assume the player can always use whirlpool,
                      ;; because whirlpool doesn't matter for anything
                      ;; else.
                      :can-whirlpool
                      ;; the supernerd blocking the gym
                      ;; doesn't move until the player
                      ;; defeats Team Rocket
                      :defeat-team-rocket
                      :blackthorn}}

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
  [{:condition :can-cut
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
    :prereqs {:badges #{}
              :items-obtained #{}}}

   {:condition :can-waterfall
    :prereqs {:badges #{:RISINGBADGE}
              :items-obtained #{:HM_WATERFALL}}}])

(def condition-prereqs
  ":prereqs is a vector that specifies one or more set of prereqs, each
  one of which is sufficient for the player to meet the :condition."
  [{:condition :goldenrod
    :prereqs [{:conditions-met #{}
               :items-obtained #{}}]}

   {:condition :ecruteak
    :prereqs [{:conditions-met #{}
               :items-obtained #{:SQUIRTBOTTLE}}

              {:conditions-met #{}
               :items-obtained #{:PASS :S_S_TICKET}}]}

   {:condition :defeat-red-gyarados
    :prereqs [{:conditions-met #{:can-surf :ecruteak}
               :items-obtained #{}}]}

   {:condition :trigger-radio-tower-takeover
    :prereqs [{:conditions-met #{:seven-badges}
               :items-obtained #{}}]}

   {:condition :underground-warehouse
    :prereqs [{:conditions-met #{:trigger-radio-tower-takeover}
               :items-obtained #{:BASEMENT_KEY}}]}

   {:condition :defeat-team-rocket
    :prereqs [{:conditions-met #{:trigger-radio-tower-takeover}
               :items-obtained #{:CARD_KEY}}]}

   {:condition :blackthorn
    :prereqs [{:conditions-met #{:ecruteak :can-strength :trigger-radio-tower-takeover}
               :items-obtained #{}}]}

   {:condition :kanto
    :prereqs [{:conditions-met #{:ecruteak}
               :items-obtained #{:S_S_TICKET}}

              {:conditions-met #{:goldenrod}
               :items-obtained #{:PASS}}]}

   {:condition :talk-to-power-plant-manager
    :prereqs [{:conditions-met #{:can-surf :kanto}
               :items-obtained #{}}]}

   {:condition :fix-power-plant
    :prereqs [{:conditions-met #{:talk-to-power-plant-manager}
               :items-obtained #{:MACHINE_PART}}]}

   {:condition :pewter
    :prereqs [{:conditions-met #{:fix-power-plant :can-cut}
               :items-obtained #{}}]}

   {:condition :defeat-elite-4
    :prereqs [{:conditions-met #{:pewter}
               :items-obtained #{}}

              {:conditions-met #{:eight-badges :can-cut :can-waterfall}
               :items-obtained #{}}]}

   {:condition :defeat-red
    :prereqs [{:conditions-met #{:sixteen-badges}
               :items-obtained #{}}]}])

(def item-prereqs
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
              :COIN_CASE
              :SQUIRTBOTTLE}}

   {:conditions-met #{:ecruteak}
    :items-obtained #{}
    :grants #{:ITEMFINDER
              :HM_SURF
              :GOOD_ROD
              :HM_STRENGTH}}

   {:conditions-met #{:ecruteak :can-surf}
    :items-obtained #{}
    :grants #{:RED_SCALE
              :HM_WHIRLPOOL
              :SECRETPOTION}}

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
    :items-obtained #{:LOST_ITEM}
    :grants #{:PASS}}

   {:conditions-met #{:fix-power-plant}
    :items-obtained #{}
    :grants #{:LOST_ITEM}}

   {:conditions-met #{:pewter}
    :items-obtained #{}
    :grants #{:SILVER_WING}}])