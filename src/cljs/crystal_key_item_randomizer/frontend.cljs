(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]))


(defn test-component []
  [:a {:href "#test"} "I am a test link created with reagent"])

(defn embed-download-link []
  (let [parent (-> js/document
                   (.getElementById "download"))]
    (r/render [test-component] parent)))

(defn handle-rom [event]
  (when (not= "" (-> event .-target .-value))
    (let [^js/File file (-> event .-target .-files (aget 0))]
      ;; seems like the input automatically disappeared? why???
      (embed-download-link))))

(defn rom-input []
  [:label "Select ROM FILE"
   [:input {:id "rom-file" :type "file" :accept ".gbc" :on-change handle-rom}]])

(defn init! []
  (r/render [rom-input] (-> js/document
                            (.getElementById "download"))))
