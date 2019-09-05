;; goal is to understand story item progression and how we might
;; traverse the list of swaps in order to _discover_ what's
;; accessible.

;; this sounds more promising than enumerating all of the
;; progress-blocking conditions

[; beginning items, guaranteed
 :MYSTERY_EGG  ;; Mr Pokemon
 :HM_FLASH     ;; Sprout Tower
 :OLD_ROD      ;; Union Cave PokeCenter
 :HM_CUT       ;; Ilex Forest

 ;; also guaranteed
 :BICYCLE      ;; goldenrod1
 :BLUE_CARD    ;; goldenrod1
 :COIN_CASE    ;; goldenrod1
 :SQUIRTBOTTLE ;; goldenrod1

 :HM_SURF      ;; ecruteak
 :ITEMFINDER   ;; ecruteak

 :GOOD_ROD     ;; olivine
 :HM_STRENGTH  ;; olivine

 ;; HM_SURF required
 :RED_SCALE    ;; lake of rage
 :HM_FLY       ;; cianwood
 :SECRETPOTION ;; cianwood
 :HM_WATERFALL ;; ice path
 :HM_WHIRLPOOL ;; mahogany              ; surf is required provided that the player
                                        ; is blocked from entering the rocket hideout until
                                        ; defeating the red Gyarados.

 ;; TODO: what is the correct order?
 :CARD_KEY     ;; goldenrod2
 :BASEMENT_KEY ;; goldenrod2
 :CLEAR_BELL   ;; goldenrod2

 ;; kanto
 ;; TODO: what is the correct order?
 :LOST_ITEM
 :MACHINE_PART
 :PASS
 :SUPER_ROD

 ;; practically unobtainable
 :S_S_TICKET
 :SILVER_WING]
