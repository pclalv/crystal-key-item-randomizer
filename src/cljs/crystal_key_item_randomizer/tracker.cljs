(ns crystal-key-item-randomizer.tracker
  (:require [reagent.core :as r]
            [crystal-key-item-randomizer.badges :as badges]
            [crystal-key-item-randomizer.key-items :as key-items]))

(def badge-regions {:ZEPHYRBADGE :johto,
                    :HIVEBADGE :johto,
                    :PLAINBADGE :johto,
                    :FOGBADGE :johto,
                    :STORMBADGE :johto,
                    :MINERALBADGE :johto,
                    :GLACIERBADGE :johto,
                    :RISINGBADGE :johto

                    :BOULDERBADGE :kanto,
                    :CASCADEBADGE :kanto,
                    :THUNDERBADGE :kanto,
                    :SOULBADGE :kanto,
                    :RAINBOWBADGE :kanto,
                    :MARSHBADGE :kanto,
                    :VOLCANOBADGE :kanto,
                    :EARTHBADGE :kanto})

(def hms
  [:HM_CUT ; green
   :HM_FLY ; multi/dragon
   :HM_SURF
   :HM_STRENGTH ; brown
   :HM_FLASH ; yellow
   :HM_WHIRLPOOL
   :HM_WATERFALL])

(def rods
  [:OLD_ROD :GOOD_ROD :SUPER_ROD])
(def transportation
  [:BICYCLE :S_S_TICKET :PASS])
(def progression
  [:SQUIRTBOTTLE :SECRETPOTION :BASEMENT_KEY :CARD_KEY :MACHINE_PART :LOST_ITEM])
(def useless
  [:MYSTERY_EGG :COIN_CASE :BLUE_CARD :ITEMFINDER :RED_SCALE :CLEAR_BELL :SILVER_WING])

(defn badge-grid-element [badge]
  (let [clicked (r/atom false)]
    (fn []
      (let [filename (str (-> badge
                              name
                              (clojure.string/replace "BADGE" "-badge")
                              clojure.string/lower-case)
                          ".png")]
        ^{:key badge} [:img {:class (if @clicked
                                      [:clicked]
                                      [])
                             :src (str "/assets/images/" filename)
                             :on-click #(reset! clicked (not @clicked))}]))))

(defn key-item-grid-element [key-item]
  (let [clicked (r/atom false)]
    (fn []
      (let [filename (str (-> key-item
                              name
                              (clojure.string/replace "_" "-")
                              clojure.string/lower-case)
                          ".png")]
        ^{:key key-item} [:img {:class (if @clicked
                                         [:clicked]
                                         [])
                                :src (str "/assets/images/" filename)
                                :on-click #(reset! clicked (not @clicked))}]))))

(defn badge-tracker []
  (let [sorted-badges (->> badges/speedchoice
                           (merge-with into badges/ordering)
                           (map (fn [[badge data]]
                                  (assoc data :badge badge)))
                           (sort-by :order))
        {:keys [johto kanto]} (group-by (comp badge-regions
                                              :badge)
                                        sorted-badges)]
    [:<>
     [:div {:class "row"} (for [{badge :badge} johto]
                            [badge-grid-element badge])]
     [:div {:class "row"} (for [{badge :badge} kanto]
                            [badge-grid-element badge])]]))

(defn key-item-tracker []
  [:<>
   [:div {:class "row"} (map (fn [ki] [key-item-grid-element ki]) rods)]
   [:div {:class "row"} (map (fn [ki] [key-item-grid-element ki]) transportation)]
   [:div {:class "row"} (map (fn [ki] [key-item-grid-element ki]) progression)]
   [:div {:class "row"} (map (fn [ki] [key-item-grid-element ki]) useless)]
   [:div {:class "row"} (map (fn [ki] [key-item-grid-element ki]) hms)]])

(r/render [badge-tracker] (-> js/document
                              (.getElementById "badges")))

(r/render [key-item-tracker] (-> js/document
                                 (.getElementById "key-items")))
