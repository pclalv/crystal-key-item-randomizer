;; goal is to understand story item progression and how we might
;; traverse the list of swaps in order to _discover_ what's
;; accessible.

;; this sounds more promising than enumerating all of the
;; progress-blocking conditions

;; all items are immediately obtainable unless marked otherwise.

[; beginning items, guaranteed
 :MYSTERY_EGG  ;; Mr Pokemon
 :HM_FLASH     ;; Sprout Tower
 :OLD_ROD      ;; Union Cave PokeCenter
 :HM_CUT       ;; Ilex Forest

 ;; goldenrod1
 :BICYCLE
 :BLUE_CARD
 :COIN_CASE
 :SQUIRTBOTTLE

 ;; ecruteak
 :HM_SURF
 :ITEMFINDER

 ;; olivine
 :GOOD_ROD
 :HM_STRENGTH

 ;; HM_SURF required
 :RED_SCALE    ;; lake of rage
 :HM_FLY       ;; cianwood
 :SECRETPOTION ;; cianwood
 :HM_WATERFALL ;; ice path
 :HM_WHIRLPOOL ;; mahogany              ; surf is required provided that the player
                                        ; is blocked from entering the rocket hideout until
                                        ; defeating the red Gyarados.

 ;; goldenrod2
 :BASEMENT_KEY ;; radio tower           ; requires rockets trigger
 :CARD_KEY     ;; underground           ; likewise requires rockets trigger
 :CLEAR_BELL   ;; radio tower           ; event trigger for this
                                        ; seems to be defeating the
                                        ; fake director

 ;; kanto
 :SUPER_ROD
 :MACHINE_PART ;; immediately obtainable?
 :LOST_ITEM ;; CopycatsHouse2F.asm      ; Copycat.Default_Merge_1 indicates
                                        ; that you need to have returned the MACHINE_PART.
 :PASS ;; the same ASM suggests that you can get this at any time, as long as you have the LOST_ITEM

 ;; practically unobtainable
 :S_S_TICKET
 :SILVER_WING]

;; badges are also required, although i don't think they need to be johto badges.
;; TODO: figure out of ANY 8 badges are good enough.

[;; johto
 :ZEPHYRBADGE  ;; guaranteed
 :HIVEBADGE    ;; guaranteed
 :PLAINBADGE   ;; goldenrod1
 :FOGBADGE     ;; ecruteak
 :STORMBADGE   ;; cianwood
 :MINERALBADGE ;; secretpotion

 ;; TODO: what are the sufficient conditions to get this
 ;; badge? have HM_SURF so that you can defeat the red
 ;; Gyarados and rid Mahogany of rockets?
 :GLACIERBADGE ;; goldenrod2 ? reaching the goldenrod2 condition
 ;; implies that you've beaten the mahogany rockets.

 :RISINGBADGE  ;; goldenrod2 as well, i guess

 ;; kanto

 ;; not sure yet what to say about these. i feel likey
 
 :BOULDERBADGE ;; viridian (ie complete power plant sidequest to get expn card)
 :CASCADEBADGE ;; vermilion             ; not sure how cerulean gym works. do you
                                        ; have to visit the power plant??
 :THUNDERBADGE ;; vermilion + HM_CUT
 :RAINBOWBADGE ;; vermilion + HM_CUT
 :SOULBADGE ;; vermilion
 :MARSHBADGE ;; vermilion
 :VOLCANOBADGE ;; viridian (ie complete power plant sidequest to get expn card)
 :EARTHBADGE  ;; viridian (ie complete power plant sidequest to get expn card)

 ]
 

;; now, how can we define these conditions?

[{:name :goldenrod1
  :reachable-if [{:have_item :HM_CUT}]}
 {:name :ecruteak
  :reachable-if [:and [{:condition :goldenrod1}
                       {:or [{:have_item :SQUIRTBOTTLE}
                             {:and [{:have_item :PASS}
                                    {:have_item :S_S_TICKET}]}]}]]}
 {:name :cianwood
  :reachable-if [:and [{:condition :ecruteak}
                       {:have-item :HM_SURF}]]}
 {:name :secretpotion
  :reachable-if {:have-item :SECRETPOTION}}
 {:goldenrod2 ;; equivalent to :cianwood
  :reachable-if [:and [{:condition :ecruteak}
                       {:have-item :HM_SURF}]]}
 ]
