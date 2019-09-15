(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]
            [crystal-key-item-randomizer.data :refer [key-items]]))

(def input-hidden (r/atom false))
(def error (r/atom nil))

(defn mismatched-lengths-error [patch]
  (str "Mismatch between address range, old values and new values for FIXME"))


(defn download-link [href filename]
  [:a {:href href
       :download filename}
   "download randomized ROM"])

(defn embed-download-link [rom filename]
  (let [parent (-> js/document
                   (.getElementById "download"))
        blob (js/Blob. [rom])
        href (js/window.webkitURL.createObjectURL blob)]
    (r/render [download-link href filename] parent)))

(defn apply-swap [rom-bytes [original replacement]]
  (let [original-address (-> (keyword original)
                             key-items
                             :address)
        replacement-value (-> (keyword replacement)
                              key-items
                              :value)]
    (aset rom-bytes original-address replacement-value)
    rom-bytes))

(defn apply-swaps [rom-bytes swaps]
  (reduce apply-swap rom-bytes (js->clj swaps)))

(defn update-address [rom-bytes {:keys [address old-value new-value]}]
  (if (not= old-value (aget rom-bytes address))
    (js/console.log "Error: expected " old-value " at address " address " but found " (aget rom-bytes address))
    (do (aset rom-bytes address new-value)
        rom-bytes)))

(defn apply-patch [rom-bytes {{old-values :old new-values :new} :integer_values
                              {begin-addr :begin end-addr :end} :address_range
                              name :name
                              :as patch}]
  (let [address-range (range begin-addr end-addr)]
    (if (not= (count address-range) (count old-values) (count new-values))
      (js/console.error (mismatched-lengths-error patch))
      (let [address-values (map (fn [address old-value new-value]
                                  {:address address :old-value old-value :new-value new-value})
                                address-range
                                old-values
                                new-values)]
        (reduce update-address
                rom-bytes
                address-values)))))

(defn apply-patches [rom-bytes patches]
  (reduce apply-patch rom-bytes patches))

(defn patch-rom [rom-bytes swaps patches]
  ;; TODO: come up with a better name for this fn
  (let [seed "FIXME"]
    (-> rom-bytes
        (apply-swaps swaps)
        (apply-patches patches)
        (embed-download-link (str "pokecrystal-key-item-randomized-seed-" seed ".gbc")))))

(defn randomize-rom [event]
  (let [rom-bytes (js/Uint8Array. (-> event
                                      .-target
                                      .-result))
        seed-id (-> js/document
                    (.getElementById "seed")
                    (.-value))]
    (-> (js/Request. (if (empty? seed-id)
                       "/seed"
                       (str "/seed/" seed-id)))
        (js/fetch)
        (.then (fn [resp] (.json resp))
               (fn [resp] (reset! error (-> resp .json .-error))))
        (.then (fn [resp]
                 (patch-rom rom-bytes
                            (-> resp .-swaps (js->clj :keywordize-keys true))
                            (-> resp .-patches (js->clj :keywordize-keys true))))))))

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
  
