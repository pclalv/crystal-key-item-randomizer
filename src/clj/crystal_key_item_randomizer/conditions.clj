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
                      ;; speedchoice.
                      :can-whirlpool
                      ;; the supernerd blocking the gym
                      ;; doesn't move until the player
                      ;; defeats Team Rocket
                      :defeat-team-rocket
                      :blackthorn}}

   {:badge :BOULDERBADGE
    :conditions-met #{:fix-power-plant}}

   {:badge :CASCADEBADGE
    :conditions-met #{:fix-power-plant}}

   {:badge :THUNDERBADGE
    :conditions-met #{:kanto}
    :items-obtained #{:HM_CUT}}

   {:badge :RAINBOWBADGE
    :conditions-met #{:kanto}
    :items-obtained #{:HM_CUT}}

   {:badge :SOULBADGE
    :conditions-met #{:kanto}}

   {:badge :MARSHBADGE
    :conditions-met #{:kanto}}

   {:badge :VOLCANOBADGE
    :conditions-met #{:fix-power-plant}}

   {:badge :EARTHBADGE
    :conditions-met #{:fix-power-plant}}])

(def item-conditions
  "A map from items to the conditions that must be satisfied in order
  for the player to obtain that item in the vanilla game.

  An empty array means that the item is obtainable without meeting any
  prerequisite.

  Any condition in the conditions array may be satisfied in order to
  obtain the item."
  ;; TODO: flip this so as to not repeat conditions so much.

  ;; "Describes the items that are obtained upon meeting some
  ;; conditions/obtaining an item/acquiring a badge in the vanilla
  ;; game."
  {:MYSTERY_EGG []
   :HM_FLASH []
   :OLD_ROD []
   :HM_CUT []

   ;; goldenrod
   :BICYCLE [{:conditions-met #{:goldenrod}}]
   :BLUE_CARD [{:conditions-met #{:goldenrod}}]
   :COIN_CASE [{:conditions-met #{:goldenrod}}]
   :SQUIRTBOTTLE [{:conditions-met #{:goldenrod}}]

   ;; ecruteak
   :ITEMFINDER [{:conditions-met #{:ecruteak}}]
   :HM_SURF [{:conditions-met #{:ecruteak}}]

   ;; olivine
   :GOOD_ROD [{:conditions-met #{:ecruteak}}]
   :HM_STRENGTH [{:conditions-met #{:ecruteak}}]

   ;; cianwood
   :HM_FLY [{:conditions-met #{:can-surf}}]
   :SECRETPOTION [{:conditions-met #{:can-surf}}]

   ;; mahogany
   :RED_SCALE [{:conditions-met #{:can-surf}}]
   :BASEMENT_KEY [{:conditions-met #{:can-surf}}]
   :HM_WHIRLPOOL [{:conditions-met #{:can-surf}}]

   ;; ice path
   :HM_WATERFALL [{:conditions-met #{:can-surf}}]

   :CARD_KEY [{:items-obtained #{:BASEMENT_KEY}}]
   :CLEAR_BELL [{:items-obtained #{:CARD_KEY}}]

   :SUPER_ROD [{:conditions-met #{:kanto}}]
   :MACHINE_PART [{:conditions-met #{:can-surf}}]
   :PASS [{:items-obtained #{:LOST_ITEM}}]
   :LOST_ITEM [{:conditions-met #{:fix-power-plant}}]
   :SILVER_WING [{:conditions-met #{:fix-power-plant}}]})

;; this item is not obtainable.
;; :S_S_TICKET [{:conditions-met #{:impossible}}]
