(ns crystal-key-item-randomizer.randomizer)

(def required-items
  {:HM_SURF {:name :HM_SURF
             :location :DanceTheatre}             
   :HM_STRENGTH {:name :HM_STRENGTH
                 :location :OlivineCafe}
   :HM_WHIRLPOOL {:name :HM_WHIRLPOOL
                  :location :TeamRocketBaseB2F}
   :HM_WATERFALL {:name :HM_WATERFALL
                  :location :IcePath1F
                  :macro :itemball}
   :SECRETPOTION {:name :SECRETPOTION
                  :location :CianwoodPharmacy}})

(def maybe-required-items 
  {:BASEMENT_KEY {:name :BASEMENT_KEY
                  :location :RadioTower5F}
   :CARD_KEY {:name :CARD_KEY
              :location :UndergroundWarehouse}
   :HM_CUT {:name :HM_CUT
            :location :IlexForest}
   :LOST_ITEM {:name :LOST_ITEM
               :location :PokemonFanClub}
   :PASS {:name :PASS
          :location :CopycatsHouse2F}
   :S_S_TICKET {:name :S_S_TICKET
                :location :ElmsLab}
   :SQUIRTBOTTLE {:name :SQUIRTBOTTLE
                  :location :GoldenrodFlowerShop}
   :RED_SCALE {:name :RED_SCALE
               :location :LakeofRage}

   :MACHINE_PART {:name :MACHINE_PART
                  :location :CeruleanGym
                  :macro "dwb EVENT_FOUND_MACHINE_PART_IN_CERULEAN_GYM"}})

(def maybe-required-pairs
  [;; if the thing on the left is in kanto and the thing on
   ;; the right is required, then the swap is kanto-locked
   {:BASEMENT_KEY :CARD_KEY}
   {:CARD_KEY :CLEAR_BELL}
   {:MACHINE_PART :SILVER_WING}
   ;; this is a bit of a special case. CARD_KEY and CLEAR_BERLL have
   ;; other prereqs, but those prereqs are themselves required
   ;; (e.g. HM_CUT or SQUIRTBOTTLE.)
   {[:LOST_ITEM :MACHINE_PART] :PASS}])

(def non-required-items
  {;; HMs
   :HM_FLASH {:name :HM_FLASH
              :location :SproutTower3F}
   :HM_FLY {:name :HM_FLY
            :location :CianwoodCity}

   ;; non-HMs
   :BICYCLE {:name :BICYCLE
             :location :GoldenrodBikeShop}

   ;; useless
   :BLUE_CARD {:name :BLUE_CARD
               :location :RadioTower2F}

   :CLEAR_BELL {:name :CLEAR_BELL
                :location :RadioTower5F}

   :COIN_CASE {:name :COIN_CASE
               :location :GoldenrodUnderground
               :macro :itemball}

   :GOOD_ROD {:name :GOOD_ROD
              :location :OlivineGoodRodHouse}

   :ITEMFINDER {:name :ITEMFINDER
                :location :EcruteakItemfinderHouse}

   ;; useless; blocking battle will be disabled
   :MYSTERY_EGG {:name :MYSTERY_EGG
                 :location :MrPokemonsHouse}

   :OLD_ROD {:name :OLD_ROD
             :location :Route32PokeCenter1F}

   :SILVER_WING {:name :SILVER_WING
                 :location :PewterCity}

   :SUPER_ROD {:name :SUPER_ROD
               :location :Route12SuperRodHouse}})

(def kanto-items [:LOST_ITEM
                  :MACHINE_PART
                  :PASS
                  :SILVER_WING
                  :SUPER_ROD])

(def pre-tree-items [:HM_CUT
                     :HM_FLASH
                     :MYSTERY_EGG
                     :OLD_ROD])

(def pre-goldenrod-items 
  ;; `CARD_KEY` is not included in pre_goldenrod_items given the
  ;; current state of the underlying assembly! this is because even if
  ;; you have the `BASEMENT_KEY`, the basement will be devoid of any
  ;; rockets

  ;; `CLEAR_BELL` is not included because i believe that the same goes
  ;; for the `CARD_KEY`/radio tower upper floors; even if it's not
  ;; true, i bet it'd make things more complicated, so i'm just
  ;; considering the `CLEAR_BELL` inaccessible for now.
  
  (concat pre-tree-items [:BICYCLE
                          :BLUE_CARD
                          :COIN_CASE
                          :SQUIRTBOTTLE]))

(def surf-blocked-items
  (concat kanto-items [;; surf-blocked because the player can't
                       :CARD_KEY
                       ;; trigger the rocket radio tower takeover
                       :BASEMENT_KEY
                       ;; without defeating the red gyarados
                       :CLEAR_BELL
                       :RED_SCALE
                       :HM_FLY
                       :SECRETPOTION
                       ;; surf blocked unless we opt to remove the guy
                       ;; blocking mahogany town/route 44 junction
                       :HM_WATERFALL]))

(def all-items (sort (concat (keys required-items)
                             (keys maybe-required-items)
                             (keys non-required-items))))

(defn ss-locked? [swaps]
  ())

(defn run []
  (zipmap all-items (shuffle all-items)))

(defn beatable? [swaps]
  (ss-locked? swaps))
