(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]
            [crystal-key-item-randomizer.data :refer [key-items-speedchoice]]))

(def input-hidden (r/atom false))
(def error (r/atom nil))
(def wildcard "*")

(defn throw-js [str]
  (throw (clj->js {:error str})))

(defn render-as-error [text]
  (reset! error text))

(defn reset-form []
  (set! (-> js/document
            (.getElementById "rom-file")
            .-value)
        "")
  (reset! input-hidden false))

(defn download-link [href filename]
  [:a {:href href
       :download filename}
   "Download!"])

(defn embed-download-link [rom filename]
  (let [parent (-> js/document
                   (.getElementById "download"))
        blob (js/Blob. [rom])
        href (js/window.webkitURL.createObjectURL blob)]
    (r/render [download-link href filename] parent)))

(defn apply-swap [rom-bytes [original replacement]]
  (let [original-address (-> (keyword original)
                             key-items-speedchoice
                             :address)
        replacement-value (-> (keyword replacement)
                              key-items-speedchoice
                              :value)]
    (aset rom-bytes original-address replacement-value)
    rom-bytes))

(defn apply-swaps [rom-bytes swaps]
  (reduce apply-swap rom-bytes (js->clj swaps)))

(defn update-address [rom-bytes {:keys [address old-value new-value]}]
  (if (or (= old-value wildcard)
          (= old-value (aget rom-bytes address)))
    (do (aset rom-bytes address new-value)
        rom-bytes)
    (throw-js (str "Expected \"" old-value
                   "\" at address \"" address
                   "\" but found \"" (aget rom-bytes address) "\"."))))

(defn apply-patch [rom-bytes {{old-values :old new-values :new} :integer_values
                              {begin-addr :begin end-addr :end} :address_range
                              name :name
                              :as patch}]
  (let [address-range (range begin-addr end-addr)]
    (if (not= (count address-range) (count old-values) (count new-values))
      (throw (str "Mismatch between address range, old values and new values for \"" (patch :name) "\"."))
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

(defn patch-rom [rom-bytes {:keys [swaps patches]}]
  (-> rom-bytes
      (apply-swaps swaps)
      (apply-patches patches)))

(defn randomize-rom [event]
  (let [rom-bytes (js/Uint8Array. (-> event
                                      .-target
                                      .-result))
        seed-id (-> js/document
                    (.getElementById "seed")
                    (.-value))]
    (-> (js/fetch (str "/seed/" seed-id))
        (.then (fn [resp] (-> resp .json)))
        (.then (fn [json]
                 (if (.-error json)
                   (do (render-as-error (.-error json))
                       (reset-form))
                   (let [{:keys [swaps patches id]} (-> json (aget "seed") (js->clj :keywordize-keys true))]
                     (-> rom-bytes
                         (patch-rom {:swaps swaps :patches patches})
                         (embed-download-link (str "pokecrystal-key-item-randomized-seed-" id ".gbc")))))))
        (.catch (fn [resp]
                  (if (.-error resp)
                    (do (render-as-error (.-error resp))
                        (reset-form))
                    (js/console.error "some kind of horrible error, sorry" resp)))))))

(defn handle-rom-input [event]
  (when (not= "" (-> event .-target .-value))
    (reset! error nil)
    (reset! input-hidden true)
    (let [^js/File rom-file (-> event .-target .-files (aget 0))
          reader (js/FileReader.)]
      (set! (.-onload reader) randomize-rom)
      (.readAsArrayBuffer reader rom-file))))

(defn error-display []
  ;; TODO: style error class - maybe put a box around it and indent it for visibility.
  [:div {:class ["error"] :style (when (nil? @error) {:display "none"})}
   [:p (str "Error: " @error)]
   ;; link to and create maintainer/contact anchor on the page,
   ;; probably with links to github and discord
   [:p "Please reload this page and try again. If this is a bug, "
    [:a {:href "https://github.com/pclalv/crystal-key-item-randomizer/issues"}
     "please report it."]]])

(defn rom-input []
  [:label {:style (when @input-hidden {:display "none"})} "Select ROM file"
   [:input {:id "rom-file" :type "file" :accept ".gbc" :on-change handle-rom-input}]])

(r/render [error-display] (-> js/document
                                (.getElementById "error")))
(r/render [rom-input] (-> js/document
                          (.getElementById "input")))
  
