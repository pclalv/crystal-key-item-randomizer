(ns crystal-key-item-randomizer.patches.text.giveitem
  (:use [crystal-key-item-randomizer.text-encoding :only [gsc-encode-with-terminator]]))

(def replacement-text-template
  "For the giveitem key items, this replacement text is guaranteed to be
  shorter than any original game text."
  "It's\\n{{key-item}}!\\e")

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
  [{:key-item :MYSTERY_EGG
    :label "ckir_BEFORE_giveitem_text_MrPokemonsHouse_GotEggText",
    :address_range {:begin 1667591, :end 1667616},
    :integer_values [0 82 127 177 164 162 164 168
                     181 164 163 79 140 152 146
                     147 132 145 152 127 132 134
                     134 232 87]}
   {:key-item :SECRETPOTION
    :label "ckir_BEFORE_giveitem_text_ReceivedSecretpotionText",
    :address_range {:begin 647257, :end 647283},
    :integer_values [0 82 127 177 164 162 164 168
                     181 164 163 79 146 132 130
                     145 132 147 143 142 147 136
                     142 141 232 87]}
   {:key-item :LOST_ITEM
    :label "ckir_BEFORE_giveitem_text_UnknownText_0x191d0a",
    :address_range {:begin 1645842, :end 1645862},
    :integer_values [0 82 127 177 164 162 164 168
                     181 164 163 79 84 127 131 142
                     139 139 232 87]}
   {:key-item :BICYCLE
    :label "ckir_BEFORE_giveitem_text_UnknownText_0x54848",
    :address_range {:begin 346184, :end 346207},
    :integer_values [0 82 127 161 174 177 177 174
                     182 164 163 127 160 79 129
                     136 130 152 130 139 132 232
                     87]}
   {:key-item :RED_SCALE
    :label "ckir_BEFORE_giveitem_text_UnknownText_0x703df",
    :address_range {:begin 460315, :end 460340},
    :integer_values [0 82 127 174 161 179 160 168
                     173 164 163 127 160 79 145
                     132 131 127 146 130 128 139
                     132 232 87]}])

(defn pad [n val coll]
  (take n (concat coll (repeat val))))

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
        new-integer-values (->> new-text
                                gsc-encode-with-terminator
                                (pad orig-num-bytes 0)
                                vec)]
    (-> giveitem-key-item-text-location
        (dissoc :name :integer_values)
        (assoc :integer_values {:old old-integer-values
                                :new new-integer-values}))))

(defn fix-giveitems [patches swaps]
  (reduce (fn [patches giveitem-key-item-text-location]
            (conj patches
                  (giveitem-patch swaps
                                  giveitem-key-item-text-location)))
          patches
          giveitem-key-item-text-locations))
