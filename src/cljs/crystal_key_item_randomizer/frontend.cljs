(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]))

(def key-items {:BICYCLE {:address 345957
                          :value 7}
                :SQUIRTBOTTLE {:address 349058
                               :value 175}
                :BLUE_CARD {:address 382984
                            :value 116}
                :BASEMENT_KEY {:address 393277
                               :value 133}
                :CLEAR_BELL {:address 393435
                             :value 70}
                :OLD_ROD {:address 433001
                          :value 58}
                :HM_WHIRLPOOL {:address 446871
                               :value 248}
                :HM_CUT {:address 454075
                         :value 243}
                :RED_SCALE {:address 458877
                            :value 66}
                :S_S_TICKET {:address 495156
                             :value 68}
                :COIN_CASE {:address 508678
                            :value 54}
                :CARD_KEY {:address 514508
                           :value 127}
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
                :SECRETPOTION {:address 647089
                               :value 67}
                :HM_FLASH {:address 1591747
                           :value 247}
                :MACHINE_PART {:address 1606840
                               :value 128}
                :PASS {:address 1617761
                       :value 134}
                :SILVER_WING {:address 1622044
                              :value 71}
                :LOST_ITEM {:address 1644619
                            :value 130}
                :MYSTERY_EGG {:address 1666670
                              :value 69}
                :HM_FLY {:address 1704097
                         :value 244}})

(defn test-component []
  [:a {:href "#test"} "I am a test link created with reagent"])

(defn embed-download-link []
  (let [parent (-> js/document
                   (.getElementById "download"))]
    (r/render [test-component] parent)))

(defn handle-rom [event]
  (println "handle-files event.target.value" (-> event
                                                 (aget "target")
                                                 (aget "value")))
  (println "handle-files event.target.files" (-> event
                                                 (aget "target")
                                                 (aget "files")
                                                 (aget 0)))
  (when (not= "" (-> event .-target .-value))
    (let [^js/File file (-> event .-target .-files (aget 0))]
      (embed-download-link))))

(defn init! []
  (-> js/document
      (.getElementById "rom-file")
      (.addEventListener "change" handle-rom false)))
