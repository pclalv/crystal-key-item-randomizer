(ns crystal-key-item-randomizer.patches.copycat
  (:use [crystal-key-item-randomizer.text-encoding :only [gsc-encode-to-original-length]]
        [crystal-key-item-randomizer.patches.data :only [get-patch]]))

(def lost-your-favorite-doll-replacement-text-template
  "\\plost my\\n{{item}}.")

(def lost-your-favorite-doll-text-patch
  (get-patch "UnknownText_0x18b064.ckir_BEFORE_para_lost_your_favorite_doll"))

(def take-item-lost-item-patch
  (get-patch "Copycat.ckir_BEFORE_takeitem_LOST_ITEM"))

(def check-item-lost-item-patch
  (get-patch "Copycat.ckir_BEFORE_checkitem_LOST_ITEM"))

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
