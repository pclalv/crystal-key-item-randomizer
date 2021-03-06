(ns crystal-key-item-randomizer.key-items)

(def non-required-items
  "These items aren't required to make any progress, nor are they
  required for any bingo goals, so we might as well use them for
  something if we can."
  [:BLUE_CARD
   :GOOD_ROD
   :ITEMFINDER
   :LOST_ITEM
   :OLD_ROD])

(def ordering
  "Looks like this: {:MYSTERY_EGG {:order 0} ...}"
  (->> [:MYSTERY_EGG :HM_FLASH :OLD_ROD :HM_CUT
        :COIN_CASE :BLUE_CARD :BICYCLE :SQUIRTBOTTLE
        :GOOD_ROD :ITEMFINDER :HM_SURF :HM_STRENGTH
        :SECRETPOTION :HM_FLY
        :RED_SCALE :HM_WHIRLPOOL
        :BASEMENT_KEY :CARD_KEY :CLEAR_BELL
        :HM_WATERFALL
        :S_S_TICKET
        :SUPER_ROD
        :MACHINE_PART :LOST_ITEM :PASS
        :SILVER_WING]
       (map-indexed (fn [idx key-item]
                      [key-item {:order idx}]))
       (into {})))

(defn speedchoice [& {:keys [rockets]}]
  (let [rockets-items (if (= :rocketless rockets)
                        ;; lance gives the player all of these at Lake of Rage
                        {:BASEMENT_KEY {:address 458935 :value 133}
                         :CARD_KEY {:address 458941 :value 127}
                         :CLEAR_BELL {:address 458992 :value 70}
                         :HM_WHIRLPOOL {:address 458876 :value 248}}
                        ;; the player obtains these as they would in vanilla
                        {:BASEMENT_KEY {:address 393277 :value 133}
                         :CARD_KEY {:address 514508 :value 127}
                         :CLEAR_BELL {:address 393435 :value 70}
                         :HM_WHIRLPOOL {:address 446886 :value 248}})]
    (conj rockets-items {:BICYCLE {:address 345957
                                   :value 7}
                         :SQUIRTBOTTLE {:address 349063
                                        :value 175}
                         :BLUE_CARD {:address 382984
                                     :value 116}
                         :OLD_ROD {:address 433012
                                   :value 58}
                         :HM_CUT {:address 454090
                                  :value 243}
                         :RED_SCALE {:address 459112
                                     :value 66}
                         :S_S_TICKET {:address 495156
                                      :value 68}
                         :COIN_CASE {:address 508678
                                     :value 54}
                         :HM_WATERFALL {:address 517234
                                        :value 249}
                         :SUPER_ROD {:address 521368
                                     :value 61}
                         :HM_SURF {:address 627978
                                   :value 245}
                         :ITEMFINDER {:address 632335
                                      :value 55}
                         :GOOD_ROD {:address 640815
                                    :value 59}
                         :HM_STRENGTH {:address 641230
                                       :value 246}
                         :SECRETPOTION {:address 647092
                                        :value 67}
                         :HM_FLASH {:address 1591747
                                    :value 247}
                         :MACHINE_PART {:address 1606860
                                        :value 128}
                         :PASS {:address 1617811
                                :value 134}
                         :SILVER_WING {:address 1622044
                                       :value 71}
                         :LOST_ITEM {:address 1644627
                                     :value 130}
                         :MYSTERY_EGG {:address 1667235
                                       :value 69}
                         :HM_FLY {:address 1704097
                                  :value 244}})))

