(ns crystal-key-item-randomizer.randomizer)

(def required-items
  {:HM_SURF {:name :HM_SURF
             :location :DanceTheatre
             :comment "required for Cianwood badge"}
   :HM_STRENGTH {:name :HM_STRENGTH
                 :location :OlivineCafe
                 :comment "required for Victory Road"}
   :SECRETPOTION {:name :SECRETPOTION
                  :location :CianwoodPharmacy
                  :comment "required for Olivine badge"}

   ;; HM_WATERFALL is required to cross Tohjo Falls - or wait, can the
   ;; player perhaps get through from Viridian?

   ;; i believe it should be possible to reach the League for the
   ;; first time from Viridian. a player having HM_SURF would be able
   ;; to complete the Power Plant sidequest, obtain the Radio Expansion Card,
   ;; wake the Snorlax and get to Viridian.

   ;; all that's really required is the Radio Expansion Card, but
   ;; we're not to the point of randomizing that yet.
   :HM_WATERFALL {:name :HM_WATERFALL
                  :location :IcePath1F
                  :macro :itemball}})

(def maybe-required-items
  {:BASEMENT_KEY {:name :BASEMENT_KEY
                  :location :RadioTower5F}
   :CARD_KEY {:name :CARD_KEY
              :location :UndergroundWarehouse}
   :HM_CUT {:name :HM_CUT
            :location :IlexForest}
   :LOST_ITEM {:name :LOST_ITEM
               :location :PokemonFanClub
               :comment "Obtainable at any time after fixing the Power Plant, without talking to Copycat"}
   :MACHINE_PART {:name :MACHINE_PART
                  :location :CeruleanGym}

   ;; probably not required?? TODO: settle this!
   :HM_WHIRLPOOL {:name :HM_WHIRLPOOL
                  :location :TeamRocketBaseB2F}

   ;; the player might need to go to kanto.
   :PASS {:name :PASS
          :location :CopycatsHouse2F}

   ;; the player might need to goto kanto.
   :S_S_TICKET {:name :S_S_TICKET
                :location :ElmsLab}

   ;; the player might be able to work around Sudowoodo with PASS and
   ;; S_S_TICKET
   :SQUIRTBOTTLE {:name :SQUIRTBOTTLE
                  :location :GoldenrodFlowerShop}})

(def non-required-items
  {:BICYCLE {:name :BICYCLE
             :location :GoldenrodBikeShop}
   :BLUE_CARD {:name :BLUE_CARD
               :location :RadioTower2F}
   :CLEAR_BELL {:name :CLEAR_BELL
                :location :RadioTower5F}
   :COIN_CASE {:name :COIN_CASE
               :location :GoldenrodUnderground
               :macro :itemball}
   :GOOD_ROD {:name :GOOD_ROD
              :location :OlivineGoodRodHouse}
   :HM_FLASH {:name :HM_FLASH
              :location :SproutTower3F}
   :HM_FLY {:name :HM_FLY
            :location :CianwoodCity}
   :ITEMFINDER {:name :ITEMFINDER
                :location :EcruteakItemfinderHouse}
   :MYSTERY_EGG {:name :MYSTERY_EGG
                 :location :MrPokemonsHouse
                 :comment "no longer required; the 'Important Battle' is disabled"}
   :OLD_ROD {:name :OLD_ROD
             :location :Route32PokeCenter1F}
   :RED_SCALE {:name :RED_SCALE
               :location :LakeofRage}
   :SILVER_WING {:name :SILVER_WING
                 :location :PewterCity}
   :SUPER_ROD {:name :SUPER_ROD
               :location :Route12SuperRodHouse}})

(def all-items (sort (concat (keys required-items)
                             (keys maybe-required-items)
                             (keys non-required-items))))
