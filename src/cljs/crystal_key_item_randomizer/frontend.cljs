(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]))

(def input-hidden (r/atom false))

(defn test-component []
  [:a {:href "#test"} "I am a test link created with reagent"])

(defn embed-download-link [filename rom]
  (let [parent (-> js/document
                   (.getElementById "download"))]
    (r/render [test-component] parent)))

(defn apply-swaps [rom-bytes swaps]
  rom-bytes)

(defn apply-diffs [rom-bytes]
  rom-bytes)

(defn patch-rom [rom-bytes swaps]
  (let [seed "FIXME"]
    (-> rom-bytes
        (apply-swaps swaps)
        (apply-diffs)
        (embed-download-link (str "pokecrystal-key-item-randomized-seed-" seed ".gbc")))))

(defn randomize-rom [event]
  (let [rom-bytes (js/Uint8Array. (-> event
                                      .-target
                                      .-result))]
    (-> (js/Request. "/swaps")
        (js/fetch)
        (.then (fn [resp] (.json resp))
               (fn [resp] (js/console.error "GET /swaps" resp)))
        (.then (fn [resp-json] (.-swaps resp-json))
               (fn [resp] (js/console.error "Could not extract JSON response" resp)))
        (.then (partial patch-rom rom-bytes)
               (fn [resp] (js/console.error "Could not get swaps" resp))))))

(defn handle-rom [event]
  (when (not= "" (-> event .-target .-value))
    (reset! input-hidden true)
    (let [^js/File rom-file (-> event .-target .-files (aget 0))
          reader (js/FileReader.)]
      (set! (.-onload reader) randomize-rom)
      (.readAsArrayBuffer reader rom-file))))

(defn rom-input []
  [:label {:style (when @input-hidden {:display "none"})} "Select ROM FILE"
   [:input {:id "rom-file" :type "file" :accept ".gbc" :on-change handle-rom}]])

(defn init! []
  (r/render [rom-input] (-> js/document
                            (.getElementById "upload"))))
