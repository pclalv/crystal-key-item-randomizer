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
               :location :PokemonFanClub}
   :MACHINE_PART {:name :MACHINE_PART
                  :location :CeruleanGym
                  :macro "dwb EVENT_FOUND_MACHINE_PART_IN_CERULEAN_GYM"}

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
                  :location :GoldenrodFlowerShop}

   ;; i forget why RED_SCALE is maybe required...
   :RED_SCALE {:name :RED_SCALE
               :location :LakeofRage}})

(def maybe-required-pairs
  [{:BASEMENT_KEY :CARD_KEY}
   {:CARD_KEY :CLEAR_BELL}
   {[:LOST_ITEM :MACHINE_PART] :PASS}
   ;; TODO: wtf is this about, we don't care about SILVER_WING
   {:MACHINE_PART :SILVER_WING}])

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
  ;; `CARD_KEY` is not included in pre_goldenrod_items! this is
  ;; because even if you have the `BASEMENT_KEY`, the basement will be
  ;; devoid of Team Rockets.

  ;; `CLEAR_BELL` is likewise not included because i believe that the
  ;; same goes for the `CARD_KEY`/radio tower upper floors. i'm just
  ;; considering the `CLEAR_BELL` inaccessible for now.

  ;; TODO: figure out if having the CARD_KEY is enough to get the
  ;; CLEAR_BELL, regardless of in-game story progress.
  (concat pre-tree-items [:BICYCLE
                          :BLUE_CARD
                          :COIN_CASE
                          :SQUIRTBOTTLE]))

(def surf-blocked-items
  (concat kanto-items [:CARD_KEY     ;; surf-blocked because the player can't
                       :BASEMENT_KEY ;; trigger the rocket radio tower takeover
                       :CLEAR_BELL   ;; without defeating the red gyarados

                       :RED_SCALE    ;; you have to surf to battle the Gyarados

                       :HM_FLY       ;; on Cianwood
                       :SECRETPOTION ;; on Cianwood

                       ;; surf blocked unless we opt to remove the guy
                       ;; blocking mahogany town/route 44
                       ;; junction. HM_SURF is required to progress
                       ;; the story, and hence to go to Ice Path.
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
        replaced-with-maybe-required-item? (reduce (fn [ss-locked? {prereq maybe-required :as maybe-required-pair}
                                                        (and (= (swaps :S_S_TICKET)
                                                                prereq
                                                                (contains? required-item-names
                                                                           ((swap maybe-required)))))])
                                                   false
                                                   maybe-required-pairs)]
    (or ss-ticket-replaced-with-required-item?
        replaced-with-maybe-required-item?)))

(defn surf-not-reachable? [swaps]
  "Return true if HM_SURF is unreachable. HM_SURF is required because
  without it there would be no way to obtain the badge from Cianwood,
  which is required for the E4."
  (any? (fn [surf-blocked-item]
          (= :HM_SURF
             (swaps surf-blocked-item)))
        surf-blocked-items))

(defn stuck-in-lex-forest? [swaps]
  "Returns true if the player cannot bypass the cuttable tree in Ilex
  Forest."
  (not-any? (fn [pre-tree-item]
              (or (= :HM_CUT
                     (swaps pre-tree-item))))
            pre-tree-items))

(defn stuck-with-pass? [swaps]
  ;; kanto items reachable with the pass

  ;; * super rod

  ;; * machine part (theoretically - not sure if there's an event
  ;;                 trigger for this)

  ;; * lost item (ditto)

  ;; * pass (unknown. you probably need surf in order to complete the
  ;;         power plant sidequest)

  ;; TODO: update this function once uncertainties are resolved.

  (if (= :SQUIRTBOTTLE (swaps :SUPER_ROD))
    false ;; player goes back to Goldenrod and beats Sudowoodo
    ;; otherwise, player might be able to go back around via Olivine
    ;; and make some progres.
    (if (= :S_S_TICKET (swaps :SUPER_ROD))
      ;; assume that HM_SURF is the only progress item.
      (cond (= :HM_SURF (swaps :GOOD_ROD)) false
            (= :HM_SURF (swaps :HM_STRENGTH)) false
            (= :HM_SURF (swaps :HM_SURF)) false))))
      

(defn stuck-in-goldenrod? [swaps]
  "Returns true if the player would get stuck in Goldenrod. 

   * If The player can obtain the Squirtbottle, then they're not stuck.

   * If the player can obtain neither the Squirtbottle nor the Pass,
     then they're stuck.

   * If the player can obtain the Pass, then we have to dig deeper."
  (let [obtainable-by-goldenrod (filter #(contains? pre-goldenrod-items (swaps %1))
                                        [:SQUIRTBOTTLE :PASS])]
        
    (cond (empty? obtainable-by-goldenrod) true
          (include? obtainable-by-goldenrod :SQUIRTBOTTLE) false
          (= obtainable-by-goldenrod [:PASS]) (stuck-with-pass? swaps))))

(defn beatable? [swaps]
  (or (surf-not-reachable? swaps)
      (stuck-in-lex-forest? swaps)
      (stuck-in-goldenrod? swaps)))
