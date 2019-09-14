(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]
            [crystal-key-item-randomizer.data :refer [key-items]]))

(def input-hidden (r/atom false))

(defn test-component []
  [:a {:href "#test"} "I am a test link created with reagent"])

(defn embed-download-link [filename rom]
  (let [parent (-> js/document
                   (.getElementById "download"))]
    (r/render [test-component] parent)))

(defn apply-swap [rom-bytes [original replacement]]
  (let [original-address (-> (keyword original)
                             key-items
                             :address)
        replacement-value (-> (keyword replacement)
                              key-items
                              :value)]
    (js/console.log "Replacing " original " with " replacement)
    (aset rom-bytes original-address replacement-value)))

(defn apply-swaps [rom-bytes swaps]
  (reduce apply-swap rom-bytes (js->clj swaps)))

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
