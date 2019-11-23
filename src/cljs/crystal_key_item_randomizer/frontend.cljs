(ns crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]
            [crystal-key-item-randomizer.badges :as badges]
            [crystal-key-item-randomizer.key-items :as key-items]))

(def handling-rom? (r/atom false))
(def error (r/atom nil))
(def item-swaps-table (r/atom {}))
(def badge-swaps-table (r/atom {}))
(def randomized-rom (r/atom nil))

;; these atoms are inputs to the randomizer.
(def no-early-super-rod? (r/atom true))
(def early-bicycle? (r/atom true))
(def randomize-badges? (r/atom false))
(def seed-id (r/atom ""))

(def wildcard "*")

(defn throw-js [str]
  (throw (clj->js {:error str})))

(defn render-error [text]
  (reset! error text))

(defn reset-form []
  (when-let [rom-file-el (-> js/document
                             (.getElementById "rom-file"))]
    (set! (-> rom-file-el .-value) ""))
  (reset! handling-rom? false)
  (reset! error nil)
  (reset! item-swaps-table {})
  (reset! randomized-rom nil)
  (reset! seed-id ""))

(defn apply-badge-swap [rom-bytes [original replacement]]
  (println "[original replacement]" [original replacement])
  (let [original-address (-> (keyword original)
                             badges/speedchoice
                             :address)
        replacement-value (-> (keyword replacement)
                              badges/speedchoice
                              :value)]
    (aset rom-bytes original-address replacement-value)
    rom-bytes))

(defn apply-badge-swaps [rom-bytes swaps]
  (reduce apply-badge-swap rom-bytes (js->clj swaps)))

(defn apply-item-swap [rom-bytes [original replacement]]
  (let [original-address (-> (keyword original)
                             key-items/speedchoice
                             :address)
        replacement-value (-> (keyword replacement)
                              key-items/speedchoice
                              :value)]
    (aset rom-bytes original-address replacement-value)
    rom-bytes))

(defn apply-item-swaps [rom-bytes swaps]
  (reduce apply-item-swap rom-bytes (js->clj swaps)))

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

(defn patch-rom [rom-bytes {:keys [item-swaps badge-swaps patches]}]
  (-> rom-bytes
      (apply-item-swaps item-swaps)
      (apply-badge-swaps badge-swaps)
      (apply-patches patches)))

(defn randomize-rom [event]
  (let [rom-bytes (js/Uint8Array. (-> event
                                      .-target
                                      .-result))
        body {:options {:early-bicycle? @early-bicycle?
                        :no-early-super-rod? @no-early-super-rod?
                        :randomize-badges? @randomize-badges?}}]
    (-> (js/fetch (str "/seed/" @seed-id)
                  (clj->js {:method "POST"
                            :headers {"Content-Type" "application/json"}
                            :body (-> body clj->js js/JSON.stringify)}))
        (.then (fn [resp] (-> resp .json)))
        (.then (fn [json]
                 (if (.-error json)
                   (do (reset-form)
                       (render-error (.-error json)))
                   (let [{:keys [item-swaps badge-swaps patches id]} (-> json
                                                                         (aget "seed")
                                                                         (js->clj :keywordize-keys true))]
                     (reset! item-swaps-table item-swaps)
                     (reset! badge-swaps-table badge-swaps)
                     (reset! randomized-rom {:rom (patch-rom rom-bytes {:item-swaps item-swaps
                                                                        :badge-swaps badge-swaps
                                                                        :patches patches})
                                             :filename (str "pokecrystal-key-item-randomized-seed-" id ".gbc")})))))
        (.catch (fn [resp]
                  (if (.-error resp)
                    (do (reset-form)
                        (render-error (.-error resp)))
                    (js/console.error "some kind of horrible error, sorry" resp)))))))

(defn handle-rom-input [event]
  (when (not= "" (-> event .-target .-value))
    (reset! error nil)
    (reset! handling-rom? true)
    (let [^js/File rom-file (-> event .-target .-files (aget 0))
          reader (js/FileReader.)]
      (set! (.-onload reader) randomize-rom)
      (.readAsArrayBuffer reader rom-file))))

(defn set-checkbox-value-on-atom [atom]
  (fn [event]
    (reset! atom (-> event .-target .-checked))))

(defn error-display []
  (when-let [error @error]
    [:div {:class ["error"]}
     [:p (str "Error: " error)]
     [:table
      [:thead [:tr [:th "Option"] [:th "Value"]]]
      [:tbody [:tr [:td "Randomize badges?"] [:td (str @randomize-badges?)]]]]
     ;; TODO: link to and create maintainer/contact anchor on the page,
     ;; probably with links to github and discord
     [:p "Please reload this page and try again. If this is a bug, "
      [:a {:href "https://github.com/pclalv/crystal-key-item-randomizer/issues"}
       "please report it."]]]))

(defn seed []
  [:<>
   [:p
    [:label {:for "seed-id"} "Seed:"]
    [:input {:id "seed-id" :type "number" :min "0" :max "9223372036854775807"
             :value @seed-id
             :on-change #(reset! seed-id (.-target.value %))
             :disabled @handling-rom?}]]
   [:p
    "You may input a seed value greater than or equal to 0 and less "
    "than or equal to 9223372036854775807, or leave it blank to get a "
    "random seed."]])

(defn options []
  [:<>
   [:p
    [:strong "Options"]]
   [:p
    [:label {:for "no-early-super-rod"} "No early " [:tt "SUPER_ROD"]]
    [:input {:id "no-early-super-rod" :type "checkbox"
             :on-change (set-checkbox-value-on-atom no-early-super-rod?)
             :checked (and (empty? @seed-id)
                           @no-early-super-rod?)
             :disabled (or @handling-rom?
                           (not (empty? @seed-id)))}]]
   [:p
    [:label {:for "early-bicycle"} "Early " [:tt "BICYCLE"]]
    [:input {:id "early-bicycle" :type "checkbox"
             :on-change (set-checkbox-value-on-atom early-bicycle?)
             :checked (and (empty? @seed-id)
                           @early-bicycle?)
             :disabled (or @handling-rom?
                           (not (empty? @seed-id)))}]]
   [:p
    [:label {:for "randomize-badges"} "Randomize badges (experimental)"]
    [:input {:id "randomize-badges" :type "checkbox"
             :on-change (set-checkbox-value-on-atom randomize-badges?)
             :checked @randomize-badges?}]]])

(defn rom-input []
  (when (not @handling-rom?)
    [:p
     [:label {:for "rom-file"} "Select ROM file"]
     [:input {:id "rom-file" :type "file" :accept ".gbc" :on-change handle-rom-input}]]))

(defn spoilers-table [swaps {swap-type :swap-type}]
  [:table {:id "swaps"}
   [:thead [:tr
            [:th (str "Vanilla " swap-type)] [:th (str "New " swap-type)]]]
   [:tbody (for [swap swaps]
             (let [orig (-> swap .-key)
                   new (-> swap .-val)]
               ^{:key orig} [:tr [:td orig] [:td new]]))]])

(defn spoilers-display []
  (let [show-spoilers? (r/atom false)]
    (fn spoilers-display* []
      [:<>
       [:p
        [:label {:for "show-spoilers"} "Show spoilers"]
        [:input {:id "show-spoilers ":type "checkbox"
                 :on-change (set-checkbox-value-on-atom show-spoilers?)
                 :checked @show-spoilers?}]]
       (when (and @show-spoilers? @randomize-badges?)
         [spoilers-table @badge-swaps-table {:swap-type "badge"}])
       (when @show-spoilers?
         [spoilers-table @item-swaps-table {:swap-type "item"}])])))

(defn reset []
  (when @randomized-rom
    [:p
     [:button {:on-click reset-form}
      "Generate a new ROM"]]))

(defn download-link []
  (when @randomized-rom
    (let [{:keys [rom filename]} @randomized-rom
          blob (js/Blob. [rom])
          href (js/window.webkitURL.createObjectURL blob)]
      [:a {:href href
           :download filename}
       "Download!"])))

(defn randomizer []
  [:<>
   [error-display]
   [seed]
   [options]
   [rom-input]
   [download-link]
   [reset]
   [spoilers-display]])

(r/render [randomizer] (-> js/document
                           (.getElementById "randomizer")))
