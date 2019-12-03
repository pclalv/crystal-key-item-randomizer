(ns crystal-key-item-randomizer.tracker
  (:require [reagent.core :as r]
            [crystal-key-item-randomizer.badges :as badges]
            [crystal-key-item-randomizer.key-items :as key-items]))

(defn badge-grid-element [badge]
  (let [clicked (r/atom false)]
    (fn []
      (let [filename (str (-> badge
                              name
                              (clojure.string/replace "BADGE" "-badge")
                              clojure.string/lower-case)
                          ".png")]
        [:img {:class (if @clicked
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
        [:img {:class (if @clicked
                        [:clicked]
                        [])
               :src (str "/assets/images/" filename)
               :on-click #(reset! clicked (not @clicked))}]))))

(defn badge-tracker []
  [:<>
   (let [sorted-badges (->> badges/speedchoice
                            (merge-with into badges/ordering)
                            (map (fn [[badge data]]
                                   (assoc data :badge badge)))
                            (sort-by :order))]
     (for [{badge :badge} sorted-badges]
       ^{:key badge} [badge-grid-element badge]))])

(defn key-item-tracker []
  [:<>
   (let [sorted-key-items (->> key-items/speedchoice
                               (merge-with into key-items/ordering)
                               (map (fn [[key-item data]]
                                      (assoc data :key-item key-item)))
                               (sort-by :order))]
     (for [{key-item :key-item} sorted-key-items]
       ^{:key key-item} [key-item-grid-element key-item]))])

(r/render [badge-tracker] (-> js/document
                              (.getElementById "badges")))

(r/render [key-item-tracker] (-> js/document
                                 (.getElementById "key-items")))
