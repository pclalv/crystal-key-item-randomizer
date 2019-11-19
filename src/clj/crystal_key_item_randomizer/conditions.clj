(ns crystal-key-item-randomizer.conditions
  "Data pertaining to the conditions that must be satisfied in order to
  make progress, namely collecting badges and collecting items.")

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

(def condition-prereqs
  ":prereqs is a vector that specifies one or more set of prereqs, each
  one of which is sufficient for the player to meet the :condition."
  [{:condition :goldenrod
    :prereqs [{:conditions-met #{}
               :badges #{}
               :badge-count 0
               :items-obtained #{}}]}

   {:condition :ecruteak
    :prereqs [{:conditions-met #{}
               :badges #{}
               :badge-count 0
               :items-obtained #{:SQUIRTBOTTLE}}

              {:conditions-met #{}
               :badges #{}
               :badge-count 0
               :items-obtained #{:PASS :S_S_TICKET}}]}

   {:condition :can-cut
    :prereqs [{:conditions-met #{}
               :badges #{:HIVEBADGE}
               :badge-count 0
               :items-obtained #{:HM_CUT}}]}

   {:condition :can-strength
    :prereqs [{:conditions-met #{}
               :badges #{:PLAINBADGE}
               :badge-count 0
               :items-obtained #{:HM_STRENGTH}}]}

   {:condition :can-surf
    :prereqs [{:conditions-met #{}
               :badges #{:FOGBADGE}
               :badge-count 0
               :items-obtained #{:HM_SURF}}]}

   ;; speedchoice-specific
   {:condition :can-whirlpool
    :prereqs [{:conditions-met #{}
               :badges #{}
               :badge-count 0
               :items-obtained #{}}]}

   {:condition :can-waterfall
    :prereqs [{:conditions-met #{}
               :badges #{:RISINGBADGE}
               :badge-count 0
               :items-obtained #{:HM_WATERFALL}}]}

   {:condition :seven-badges
    :prereqs [{:conditions-met #{}
               :badges #{}
               :badge-count 7
               :items-obtained #{}}]}

   {:condition :defeat-red-gyarados
    :prereqs [{:conditions-met #{:can-surf :ecruteak}
               :badges #{}
               :badge-count 0
               :items-obtained #{}}]}

   {:condition :trigger-radio-tower-takeover
    :prereqs [{:conditions-met #{:seven-badges}
               :badges #{}
               :badge-count 0
               :items-obtained #{}}]}

   {:condition :underground-warehouse
    :prereqs [{:conditions-met #{:trigger-radio-tower-takeover}
               :badges #{}
               :badge-count 0
               :items-obtained #{:BASEMENT_KEY}}]}

   {:condition :defeat-team-rocket
    :prereqs [{:conditions-met #{:trigger-radio-tower-takeover}
               :badges #{}
               :badge-count 0
               :items-obtained #{:CARD_KEY}}]}

   {:condition :blackthorn
    :prereqs [{:conditions-met #{:ecruteak :can-strength :trigger-radio-tower-takeover}
               :badges #{}
               :badge-count 0
               :items-obtained #{}}]}

   {:condition :kanto
    :prereqs [{:conditions-met #{:ecruteak}
               :badges #{}
               :badge-count 0
               :items-obtained #{:S_S_TICKET}}

              {:conditions-met #{:goldenrod}
               :badges #{}
               :badge-count 0
               :items-obtained #{:PASS}}]}

   {:condition :talk-to-power-plant-manager
    :prereqs [{:conditions-met #{:can-surf :kanto}
               :badges #{}
               :badge-count 0
               :items-obtained #{}}]}

   {:condition :fix-power-plant
    :prereqs [{:conditions-met #{:talk-to-power-plant-manager}
               :badges #{}
               :badge-count 0
               :items-obtained #{:MACHINE_PART}}]}

   {:condition :pewter
    :prereqs [{:conditions-met #{:fix-power-plant :can-cut}
               :badges #{}
               :badge-count 0
               :items-obtained #{}}]}

   {:condition :defeat-elite-4
    :prereqs [{:conditions-met #{:pewter}
               :badges #{}
               :badge-count 8
               :items-obtained #{}}

              {:conditions-met #{:can-cut :can-waterfall}
               :badges #{}
               :badge-count 8
               :items-obtained #{}}]}

   {:condition :defeat-red
    :prereqs [{:conditions-met #{}
               :badges #{}
               :badge-count 16
               :items-obtained #{}}]}])

(def item-prereqs
  [{;; the cuttable tree in Ilex Forest is removed by the randomizer,
    ;; so goldenrod is always accessible
    :conditions-met #{}
    :badge-count 0
    :items-obtained #{}
    :grants #{:MYSTERY_EGG
              :HM_FLASH 
              :OLD_ROD
              :HM_CUT}}

   {:conditions-met #{:goldenrod}
    :badge-count 0
    :items-obtained #{}
    :grants #{:BICYCLE
              :BLUE_CARD 
              :COIN_CASE
              :SQUIRTBOTTLE}}

   {:conditions-met #{:ecruteak}
    :badge-count 0
    :items-obtained #{}
    :grants #{:ITEMFINDER
              :HM_SURF
              :GOOD_ROD
              :HM_STRENGTH}}

   {:conditions-met #{:ecruteak :can-surf}
    :badge-count 0
    :items-obtained #{}
    :grants #{:RED_SCALE
              :HM_WHIRLPOOL
              :SECRETPOTION}}

   {:conditions-met #{:ecruteak :can-surf :can-strength}
    :badge-count 0
    :items-obtained #{}
    :grants #{:HM_FLY}}

   {:conditions-met #{:ecruteak :can-surf :can-strength}
    :badge-count 7
    :items-obtained #{}
    :grants #{:BASEMENT_KEY :HM_WATERFALL}}

   {:conditions-met #{:defeat-elite-4}
    :badge-count 0
    :items-obtained #{}
    :grants #{:S_S_TICKET}}

   {:conditions-met #{:underground-warehouse}
    :badge-count 0
    :items-obtained #{}
    :grants #{:CARD_KEY}}

   {:conditions-met #{:defeat-team-rocket}
    :badge-count 0
    :items-obtained #{}
    :grants #{:CLEAR_BELL}}

   {:conditions-met #{:kanto}
    :badge-count 0
    :items-obtained #{}
    :grants #{:SUPER_ROD}}

   {:conditions-met #{:can-talk-to-power-plant-manager}
    :badge-count 0
    :items-obtained #{}
    :grants #{:MACHINE_PART}}

   {:conditions-met #{:kanto}
    :badge-count 0
    :items-obtained #{:LOST_ITEM}
    :grants #{:PASS}}

   {:conditions-met #{:fix-power-plant}
    :badge-count 0
    :items-obtained #{}
    :grants #{:LOST_ITEM}}

   {:conditions-met #{:pewter}
    :badge-count 0
    :items-obtained #{}
    :grants #{:SILVER_WING}}])
