(ns ^:dev/always crystal-key-item-randomizer.frontend
  (:require [reagent.core :as r]
            [crystal-key-item-randomizer.key-items :as key-items]
            [cljs.spec.alpha :as s]
            [cljs.spec.test.alpha :as st])
  (:use [crystal-key-item-randomizer.patching :only [patch-rom]]))

;; TODO: use cursors https://cljdoc.org/d/reagent/reagent/0.9.1/doc/tutorials/-wip-managing-state-atoms-cursors-reactions-and-tracking#cursors
(def state (r/atom {:ui {:error nil
                         :handling-rom? false
                         :randomized-rom nil
                         :spoilers {:item-swaps {}
                                    :badge-swaps {}}}
                    :seed-id ""
                    :seed-options {:endgame-condition "defeat-red"
                                   :randomize-janine? false
                                   :rockets "normal"}
                    :swaps-options {:early-bicycle? true
                                    :no-early-super-rod? true
                                    :no-early-sabrina? true
                                    :randomize-badges? false
                                    :randomize-copycat-item? false}}))

;; UI
(def error (r/cursor state [:ui :error]))
(def handling-rom? (r/cursor state [:ui :handling-rom?]))
(def randomized-rom (r/cursor state [:ui :randomized-rom]))

(def item-swaps-table (r/cursor state [:ui :spoilers :item-swaps]))
(def badge-swaps-table (r/cursor state [:ui :spoilers :badge-swaps]))

;; these atoms are inputs to the randomizer.
(def seed-id (r/cursor state [:seed-id]))

;; swaps-options
(def early-bicycle? (r/cursor state [:swaps-options :early-bicycle?]))
(def randomize-badges? (r/cursor state [:swaps-options :randomize-badges?]))
(def no-early-sabrina? (r/cursor state [:swaps-options :no-early-sabrina?]))
(def no-early-super-rod? (r/cursor state [:swaps-options :no-early-super-rod?]))
(def randomize-copycat-item? (r/cursor state [:swaps-options :randomize-copycat-item?]))

;; seed-options
(def endgame-condition (r/cursor state [:seed-options :endgame-condition]))
(def randomize-janine? (r/cursor state [:seed-options :randomize-janine?]))
(def rockets (r/cursor state [:seed-options :rockets]))

(defn render-error [text]
  (reset! error text))

(defn reset-form []
  (when-let [rom-file-el (-> js/document
                             (.getElementById "rom-file"))]
    (set! (-> rom-file-el .-value) ""))
  (reset! handling-rom? false)
  (reset! error nil)
  (reset! item-swaps-table {})
  (reset! badge-swaps-table {})
  (reset! randomized-rom nil)
  (reset! seed-id ""))

(defn randomize-rom [event]
  (let [;; TODO: use https://clojuredocs.org/clojure.core/byte-array
        rom-bytes (js/Uint8Array. (-> event
                                      .-target
                                      .-result))
        body {:options {:seed-options {:endgame-condition @endgame-condition
                                       :randomize-janine? @randomize-janine?
                                       :rockets @rockets}
                        :swaps-options (let [always-options {:randomize-badges? @randomize-badges?
                                                             :randomize-copycat-item? @randomize-copycat-item?}]
                                         ;; this use of seq is a clojure idiom; see the docs for clojure.code/empty?
                                         (if (seq @seed-id)
                                           always-options
                                           (assoc always-options
                                                  :early-bicycle? @early-bicycle?
                                                  ;; TODO: the frontend totally broke and required a hard-reload when i had
                                                  ;; no-early-sabrina? instead of @no-early-sabrina?, probably some kind of
                                                  ;; serialization error. investiagte what it would take to at least reset
                                                  ;; the form and render a reasonable error for the user.
                                                  :no-early-sabrina? @no-early-sabrina?
                                                  :no-early-super-rod? @no-early-super-rod?)))}}]
    (-> (js/fetch (str "/seed/" @seed-id)
                  (clj->js {:method "POST"
                            :headers {"Content-Type" "application/json"}
                            :body (-> body clj->js js/JSON.stringify)}))
        (.then (fn [resp] (-> resp .json)))
        (.then (fn [json]
                 (if (.-error json)
                   (do (reset-form)
                       (render-error (.-error json)))
                   (let [{:keys [item-swaps badge-swaps patches id options] :as seed} (-> json
                                                                                          (aget "seed")
                                                                                          (js->clj :keywordize-keys true))
                         ;; TODO: add other options to the filename
                         filename (str "pokecrystal-key-item-randomized-seed-"
                                       (if @randomize-badges?
                                         (str id "-badges")
                                         id)
                                       ".gbc")]
                     (reset! item-swaps-table item-swaps)
                     (reset! badge-swaps-table badge-swaps)
                     (reset! randomized-rom {:rom (patch-rom rom-bytes seed)
                                             :filename filename})))))
        ;; the arg name 'resp' is not accurate in all cases but i'm
        ;; not sure what else to call it.
        (.catch (fn [resp]
                  (let [error (if (.-error resp)
                                (.-error resp)
                                resp)
                        _ (js/console.error "in catch; error" (clj->js error))]
                    (do (reset-form)
                        (render-error error))))))))

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

(defn set-value-on-atom [atom]
  (fn [event]
    (reset! atom (-> event .-target .-value))))

(defn error-display []
  (when-let [error @error]
    [:div {:class ["error"]}
     [:p (str "Error: " error)]
     [:table
      [:thead [:tr [:th "Option"] [:th "Value"]]]
      [:tbody [:tr [:td "Randomize badges?"] [:td (str @randomize-badges?)]]]]
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
  [:fieldset
   [:legend "Options"]
   [:input {:id "no-early-super-rod" :type "checkbox"
            :on-change (set-checkbox-value-on-atom no-early-super-rod?)
            :checked (and (empty? @seed-id)
                          @no-early-super-rod?)
            :disabled (or @handling-rom?
                          (not (empty? @seed-id)))}]
   [:label {:for "no-early-super-rod"} "No early " [:tt "SUPER_ROD"] " - Ensure that " [:tt "SUPER_ROD"] " is not obtainable until after the player has left Goldenrod."]
   [:br]

   [:input {:id "early-bicycle" :type "checkbox"
            :on-change (set-checkbox-value-on-atom early-bicycle?)
            :checked (and (empty? @seed-id)
                          @early-bicycle?)
            :disabled (or @handling-rom?
                          (not (empty? @seed-id)))}]
   [:label {:for "early-bicycle"} "Early " [:tt "BICYCLE"] " - Ensure that " [:tt "BICYCLE"] " is obtainable before the player has left Goldenrod."]
   [:br]

   [:input {:id "no-early-sabrina" :type "checkbox"
            :on-change (set-checkbox-value-on-atom no-early-sabrina?)
            :checked (and (empty? @seed-id)
                          @no-early-sabrina?)
            :disabled (or @handling-rom?
                          (not @randomize-badges?)
                          (not (empty? @seed-id)))}]
   [:label {:for "no-early-sabrina"} "No early Sabrina - (Badge randomization only) Ensure that Sabrina does not have any of the first four Johto badges (Zephy, Hive, Plain, Fog)"]
   [:br]

   [:input {:id "randomize-badges" :type "checkbox"
            :on-change (set-checkbox-value-on-atom randomize-badges?)
            :checked @randomize-badges?
            :disabled @handling-rom?}]
   [:label {:for "randomize-badges"} "Randomize badges"]
   [:br]

   [:input {:id "randomize-copycat-item" :type "checkbox"
            :on-change (set-checkbox-value-on-atom randomize-copycat-item?)
            :checked @randomize-copycat-item?
            :disabled @handling-rom?}]
   [:label {:for "randomize-copycat-item"}
    "Randomize Copycat item (Experimental) - Randomize which item the Copycat is looking for; "
    "the replacement will be a non-progression key item. The item will be one of the following: "
    (->> key-items/non-required-items
         (map name)
         (map #([:tt %])))]
   [:br]

   [:input {:id "randomize-janine" :type "checkbox"
            :on-change (set-checkbox-value-on-atom randomize-janine?)
            :checked @randomize-janine?
            :disabled @handling-rom?}]
   [:label {:for "randomize-janine"} "Randomize Janine (Experimental) - Randomize which Janine is the real Janine in Fuchsia Gym."]
   [:br]

   [:select {:id "endgame-condition"
             :on-change (set-value-on-atom endgame-condition)
             :value @endgame-condition
             :disabled @handling-rom?}
    [:option {:value "defeat-red"} "Defeat Red"]
    [:option {:value "defeat-elite-4"} "Defeat Elite 4"]]
   [:label {:for "endgame-condition"} "Endgame condition"]
   [:br]

   [:select {:id "rockets"
             :on-change (set-value-on-atom rockets)
             :value @rockets
             :disabled @handling-rom?}
    [:option {:value "normal"} "Normal"]
    [:option {:value "early"} "Early"]
    [:option {:value "rocketless"} "Rocketless"]]
   [:label {:for "rockets"} "Rockets"]
   [:table
    [:thead [:tr [:th "Rockets Option"] [:th "Description"]]]
    [:tbody [:tr [:td "Normal"] [:td "Rocket sequence plays out normally."]]]
    [:tbody [:tr [:td "Early"] [:td "Trigger Team Rocket Radio Tower takeover after obtaining 4 badges instead of 7 badges."]]]
    [:tbody [:tr [:td "Rocketless"] [:td "(Experimental) Select this if you want to use the 'Rocketless' Speedchoice option. "
                                     "Lance will " [:strong "not"] " give you anything useful. "
                                     "Might not play well with 'Randomize Copycat item'."]]]]])

(defn rom-input []
  (when (not @handling-rom?)
    [:p
     [:input {:id "rom-file" :type "file" :accept ".gbc" :on-change handle-rom-input}]
     [:label {:for "rom-file"} "Select Pokemon Crystal Speedchoice ROM file"]]))

(defn spoilers-table [swaps {swap-type :swap-type}]
  [:table {:id "swaps"}
   [:thead [:tr
            [:th (str "Vanilla " swap-type)] [:th (str "New " swap-type)]]]
   [:tbody (for [swap swaps]
             (let [orig (-> swap .-key)
                   new (-> swap .-val)]
               ^{:key orig} [:tr [:td orig] [:td new]]))]])

;; TODO: add copycat item swap to spoiler
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

(defn ^:dev/after-load instrument []
  (st/instrument)
  (js/console.log "instrument called"))
