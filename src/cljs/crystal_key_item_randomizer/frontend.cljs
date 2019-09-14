(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]))

(def input-hidden (r/atom false))

(defn test-component []
  [:a {:href "#test"} "I am a test link created with reagent"])

(defn embed-download-link []
  (let [parent (-> js/document
                   (.getElementById "download"))]
    (r/render [test-component] parent)))

(defn patch-rom [rom-bytes swaps-response]
  (let [swaps-json (-> swaps-response .json)]
    (js/console.log "swaps-response" swaps-response)
    (js/console.log "swaps-json" swaps-json)))

(defn server-error [& args]
  (js/console.error "server error")
  (js/console.error "args" args))

(defn randomize-rom [event]
  (let [rom-bytes (js/Uint8Array. (-> event
                                      .-target
                                      .-result))]
    (-> (js/Request. "/swaps")
        (js/fetch)
        (.then (partial patch-rom rom-bytes) server-error))))
    ;; (go (let [response (<! (http/get "swaps"))]
    ;;       (prn (:status response))
    ;;       (prn (:body response))))))
;;  (embed-download-link))

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
