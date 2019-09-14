(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]))

(def input-hidden (r/atom false))

(defn test-component []
  [:a {:href "#test"} "I am a test link created with reagent"])

(defn embed-download-link []
  (let [parent (-> js/document
                   (.getElementById "download"))]
    (r/render [test-component] parent)))

(defn handle-rom [event]
  (when (not= "" (-> event .-target .-value))
    (reset! input-hidden true)
    (let [^js/File file (-> event .-target .-files (aget 0))]
      (embed-download-link))))

(defn rom-input []
  [:label {:style (when @input-hidden {:display "none"})} "Select ROM FILE"
   [:input {:id "rom-file" :type "file" :accept ".gbc" :on-change handle-rom}]])

(defn init! []
  (r/render [rom-input] (-> js/document
                            (.getElementById "upload"))))
