(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]
            [crystal-key-item-randomizer.key-items :as key-items]))

(def show-spoilers? (r/atom false))
(def input-hidden? (r/atom false))
(def error (r/atom nil))
(def swaps-table (r/atom {}))
(def no-early-super-rod? (r/atom true))
(def early-bicycle? (r/atom true))

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
  (reset! input-hidden? false))

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
                             key-items/speedchoice
                             :address)
        replacement-value (-> (keyword replacement)
                              key-items/speedchoice
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
                    (.-value))
        body {:options {:early-bicycle? @early-bicycle?
                        :no-early-super-rod? @no-early-super-rod?}}]
    (-> (js/fetch (str "/seed/" seed-id)
                  (clj->js {:method "POST"
                            :headers {"Content-Type" "application/json"}
                            :body (-> body clj->js js/JSON.stringify)}))
        (.then (fn [resp] (-> resp .json)))
        (.then (fn [json]
                 (if (.-error json)
                   (do (render-as-error (.-error json))
                       (reset-form))
                   (let [{:keys [swaps patches id]} (-> json (aget "seed") (js->clj :keywordize-keys true))]
                     (reset! swaps-table swaps)
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
    (reset! input-hidden? true)
    (let [^js/File rom-file (-> event .-target .-files (aget 0))
          reader (js/FileReader.)]
      (set! (.-onload reader) randomize-rom)
      (.readAsArrayBuffer reader rom-file))))

(defn set-boolean-atom [atom]
  (fn [event]
    (reset! atom (-> event .-target .-checked))))

(defn error-display []
  [:div {:class ["error"] :style (when (nil? @error) {:display "none"})}
   [:p (str "Error: " @error)]
   ;; link to and create maintainer/contact anchor on the page,
   ;; probably with links to github and discord
   [:p "Please reload this page and try again. If this is a bug, "
    [:a {:href "https://github.com/pclalv/crystal-key-item-randomizer/issues"}
     "please report it."]]])

(defn rom-input []
  [:label {:style (when @input-hidden? {:display "none"})} "Select ROM file"
   [:input {:id "rom-file" :type "file" :accept ".gbc" :on-change handle-rom-input}]])

(defn swap-row []
  (fn [swap]
    (let [orig-item (-> swap .-key)
          new-item (-> swap .-val)]
      [:tr [:td orig-item] [:td new-item]])))
          
(defn spoilers-display []
  [:div
   [:label {:for "show-spoilers"} "Show spoilers"]
   [:input {:id "show-spoilers ":type "checkbox"
            :on-change (set-boolean-atom show-spoilers?) :checked @show-spoilers?}]
   [:table {:id "swaps" :style (when (not @show-spoilers?) {:display "none"})}
    [:thead [:tr
             [:th "Vanilla item"] [:th "New item"]]]
    [:tbody (for [swap @swaps-table]
              [swap-row swap])]]])

(defn options []
  ;; TODO: make these unmodifiable if seed ID is not nil. it does not
  ;; make sense to specify options AND a seed.
  [:div
   [:strong "Options (note that options don't apply if you manually input a seed)"]
   [:div
    [:label {:for "no-early-super-rod"} "No early " [:tt "SUPER_ROD"]]
    [:input {:id "no-early-super-rod" :type "checkbox"
             :on-change (set-boolean-atom no-early-super-rod?) :checked @no-early-super-rod?}]]
   [:div
    [:label {:for "early-bicycle"} "Early " [:tt "BICYCLE"]]
    [:input {:id "early-bicycle" :type "checkbox"
             :on-change (set-boolean-atom early-bicycle?) :checked @early-bicycle?}]]])

(r/render [options] (-> js/document
                        (.getElementById "options")))

(r/render [spoilers-display] (-> js/document
                                 (.getElementById "spoilers")))

(r/render [error-display] (-> js/document
                              (.getElementById "error")))

(r/render [rom-input] (-> js/document
                          (.getElementById "input")))
  
