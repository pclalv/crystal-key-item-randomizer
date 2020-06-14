(ns crystal-key-item-randomizer.patches.text.giveitem
  (:use [crystal-key-item-randomizer.text-encoding :only [gsc-encode-to-original-length]]
        [crystal-key-item-randomizer.patches.data :only [get-patch]]))

(def replacement-text-template
  "For the giveitem key items, this replacement text is guaranteed to be
  shorter than any original game text."
  "<START>It's\\n{{key-item}}!\\e")

(def key-item-in-game-names {:HM_CUT "HM01"
                             :HM_FLY "HM02"
                             :HM_STRENGTH "HM04"
                             :HM_SURF "HM03"
                             :HM_FLASH "HM05"
                             :HM_WHIRLPOOL "HM06"
                             :HM_WATERFALL "HM07"
                             :S_S_TICKET "S.S.TICKET"})

(defn key-item->in-game-name [key-item]
  (let [in-game-name (key-item-in-game-names key-item)]
    (if in-game-name
      in-game-name
      (-> key-item
          name
          (clojure.string/replace "_" " ")))))

(def giveitem-key-item-text-locations
  (->> {:MYSTERY_EGG "ckir_BEFORE_giveitem_text_MrPokemonsHouse_GotEggText"
        :SECRETPOTION "ckir_BEFORE_giveitem_text_ReceivedSecretpotionText"
        :LOST_ITEM "ckir_BEFORE_giveitem_text_UnknownText_0x191d0a"
        :BICYCLE "ckir_BEFORE_giveitem_text_UnknownText_0x54848"
        :RED_SCALE "ckir_BEFORE_giveitem_text_UnknownText_0x703df"}
       (map (fn [[key-item label]]
              (-> (get-patch label)
                  (assoc :key-item key-item))))
       (map #(assoc %
                    :integer_values
                    (get-in % [:integer_values :old])))
       (into #{})))

(defn giveitem-patch [swaps {key-item :key-item
                             old-integer-values :integer_values
                             :as giveitem-key-item-text-location}]
  (let [orig-num-bytes (count old-integer-values)
        new-key-item (let [new-item (swaps key-item)
                           in-game-name (key-item->in-game-name new-item)]
                       (if in-game-name
                         in-game-name
                         (name key-item)))
        new-text (clojure.string/replace replacement-text-template "{{key-item}}" new-key-item)
        ;; all new texts are shorter than their corresponding original
        ;; text, so we'll unconditionally terminate the new text, and
        ;; then pad the rest with 0s.
        new-integer-values (gsc-encode-to-original-length new-text orig-num-bytes)]
    (-> giveitem-key-item-text-location
        (assoc :integer_values {:old old-integer-values
                                :new new-integer-values}))))

(defn fix-giveitems [swaps]
  (mapv #(giveitem-patch swaps %)
        giveitem-key-item-text-locations))
