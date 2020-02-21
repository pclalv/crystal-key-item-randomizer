(ns crystal-key-item-randomizer.patches.copycat
  (:use [crystal-key-item-randomizer.text-encoding :only [gsc-encode-to-original-length]]))

(def lost-your-favorite-doll-replacement-text-template
  "\\plost my\\n{{item}}.")

(def lost-your-favorite-doll-text-patch
  {:label "UnknownText_0x18b064.ckir_BEFORE_para_lost_your_favorite_doll",
   :address_range {:begin 1618094,
                   :end 1618121},
   :integer_values {:old [81 171 174 178 179 127 184 174 180 177 127 165 160 181 174 177 168 179 164 79 84 127 131 142 139 139 232]}})

(def take-item-lost-item-patch
  {:label "Copycat.ckir_BEFORE_takeitem_LOST_ITEM"
   :integer_values {:old [32 130 1]}
   :address_range {:begin 1617793
                   :end 1617796}})

(def check-item-lost-item-patch
  {:label "Copycat.ckir_BEFORE_checkitem_LOST_ITEM"
   :integer_values {:old [33 130]}
   :address_range {:begin 1617643
                   :end 1617645}})

(defn fix-text [copycat-item]
  (let [item-name (-> copycat-item
                      name
                      (clojure.string/replace "_" " "))
        original-bytes-length (-> lost-your-favorite-doll-text-patch :integer_values :old count)
        new-integer-values (-> lost-your-favorite-doll-replacement-text-template
                               (clojure.string/replace "{{item}}" item-name)
                               (gsc-encode-to-original-length original-bytes-length))]
    (assoc-in lost-your-favorite-doll-text-patch [:integer_values :new] new-integer-values)))

(defn generate
  "Change which item the Copycat is looking for."
  [copycat-item rockets]
  (let [item-id (-> (crystal-key-item-randomizer.key-items/speedchoice :rockets rockets)
                    copycat-item
                    :value)]
    [(assoc-in check-item-lost-item-patch [:integer_values :new] [33 item-id])
     (assoc-in take-item-lost-item-patch [:integer_values :new] [32 item-id 1])
     (fix-text copycat-item)]))
