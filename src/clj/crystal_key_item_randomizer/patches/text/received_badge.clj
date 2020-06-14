(ns crystal-key-item-randomizer.patches.text.received-badge
  (:use [crystal-key-item-randomizer.text-encoding :only [gsc-encode-to-original-length]]
        [crystal-key-item-randomizer.patches.data :only [get-patch]]))

(def received-badge-replacement-text-template
  "This replacement text is guaranteed to be shorter than any original
  game text."
  "<START>It's\\n{{badge}}.\\e")

(def received-risingbadge-replacement-text-template
  "Unlike the general case, Clair's text begins with 'para'."
  (clojure.string/replace received-badge-replacement-text-template "<START>" "\\p"))

(def received-badge-text-locations
  (->> [{:badge :ZEPHYRBADGE,
         :label "UnknownText_0x685af.ckir_BEFORE_text_ZEPHYRBADGE_received"}
        {:badge :HIVEBADGE,
         :label
         "Text_ReceivedHiveBadge.ckir_BEFORE_text_HIVEBADGE_received"}
        {:badge :PLAINBADGE,
         :label "UnknownText_0x54273.ckir_BEFORE_text_PLAINBADGE_received"}
        {:badge :FOGBADGE,
         :label "UnknownText_0x9a043.ckir_BEFORE_text_FOGBADGE_received"}
        {:badge :STORMBADGE,
         :label "UnknownText_0x9d835.ckir_BEFORE_text_STORMBADGE_received"}
        {:badge :MINERALBADGE,
         :label
         "UnknownText_0x9c33a.ckir_BEFORE_text_MINERALBADGE_received"}
        {:badge :GLACIERBADGE,
         :label
         "UnknownText_0x199d3b.ckir_BEFORE_text_GLACIERBADGE_received"}
        {:badge :RISINGBADGE,
         :label "ClairText_Lazy.ckir_BEFORE_text_RISINGBADGE_received"}
        {:badge :BOULDERBADGE,
         :label
         "UnknownText_0x1a2a3d.ckir_BEFORE_text_BOULDERBADGE_received"}
        {:badge :CASCADEBADGE,
         :label
         "UnknownText_0x188768.ckir_BEFORE_text_CASCADEBADGE_received"}
        {:badge :THUNDERBADGE,
         :label
         "UnknownText_0x192277.ckir_BEFORE_text_THUNDERBADGE_received"}
        {:badge :RAINBOWBADGE,
         :label
         "UnknownText_0x72c96.ckir_BEFORE_text_RAINBOWBADGE_received"}
        {:badge :SOULBADGE,
         :label "UnknownText_0x195feb.ckir_BEFORE_text_SOULBADGE_received"}
        {:badge :MARSHBADGE,
         :label "UnknownText_0x189e95.ckir_BEFORE_text_MARSHBADGE_received"}
        {:badge :VOLCANOBADGE,
         :label
         "UnknownText_0x1ab683.ckir_BEFORE_text_VOLCANOBADGE_received"}
        {:badge :EARTHBADGE,
         :label
         "Text_ReceivedEarthBadge.ckir_BEFORE_text_EARTHBADGE_received"}]
       (map (fn [txt-data]
              (let [patch (get-patch (:label txt-data))]
                (-> (merge txt-data patch)
                    (assoc :integer_values (-> patch :integer_values :old))))))))

(defn received-badge-patch [swaps {badge :badge
                                   old-integer-values :integer_values
                                   :as received-badge-text-location}]
  (let [orig-num-bytes (count old-integer-values)
        new-badge (-> badge swaps name)
        template (if (= badge :RISINGBADGE)                   
                   received-risingbadge-replacement-text-template
                   received-badge-replacement-text-template)
        new-text (clojure.string/replace template "{{badge}}" new-badge)
        ;; all new texts are shorter than their corresponding original
        ;; text, so we'll unconditionally terminate the new text, and
        ;; then pad the rest with 0s.
        new-integer-values (gsc-encode-to-original-length new-text orig-num-bytes)]
    (assoc received-badge-text-location :integer_values {:old old-integer-values
                                                         :new new-integer-values})))
        

(defn fix-received-badge-texts [swaps]
  (map #(received-badge-patch swaps %) received-badge-text-locations))
