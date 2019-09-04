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

(defn any? [pred col]
  (not (not-any? pred col)))

;; is this even worth checking?
(defn ss-locked? [swaps]
  (let [ss-ticket-replaced-with-required-item? (any? (fn [required-item] (= (swaps :S_S_TICKET)
                                                                            required-item)))
        ss-ticket-replaced-with-maybe-required-item? (reduce (fn [ss-locked? {prereq maybe-required :as maybe-required-pair}]
                                                               (and (= (swaps :S_S_TICKET)
                                                                       prereq)
                                                                    (contains? crystal-key-item-randomizer.randomizer/required-item-names
                                                                               ((swap maybe-required)))))
                                                             false
                                                             crystal-key-item-randomizer.randomizer/maybe-required-pairs)]
    (or ss-ticket-replaced-with-required-item?
        ss-ticket-replaced-with-maybe-required-item?)))

(defn surf-not-reachable? [swaps]
  "Return true if HM_SURF is unreachable. HM_SURF is required because
  without it there would be no way to obtain the badge from Cianwood,
  which is required for the E4."
  (any? (fn [surf-blocked-item]
          (= :HM_SURF
             (swaps surf-blocked-item)))
        surf-blocked-items))

(defn stuck-in-lex-forest? [swaps]
  "Returns tree if the player cannot bypass either the cuttable tree
  in Ilex Forest."
  (not-any? (fn [pre-tree-item]
              (or (= :HM_CUT
                     (swaps pre-tree-item))))
            pre-tree-items))

(defn obtainable-by-goldenrod? [swaps item]
  (contains? pre-goldenrod-items (swaps item)))

(defn squirtbottle-obtainable? [swaps]
  (obtainable-by-goldenrod? swaps :SQUIRTBOTTLE))

(defn pass-obtainable? [swaps]
  (foo))

(defn stuck-in-goldenrod? [swaps]
  (or (squirtbottle-obtainable? swaps)
      (pass-obtainable? swaps)))

(defn beatable? [swaps]
  (or (surf-not-reachable? swaps)
      (stuck-in-lex-forest? swaps)
      (stuck-in-goldenrod? swaps)))
