(ns crystal-key-item-randomizer.patches
  (:require [crystal-key-item-randomizer.key-items :as key-items]
            [crystal-key-item-randomizer.patches.text.gym-leader-post-defeat :as post-defeat]
            [clojure.spec.alpha :as s]
            [crystal-key-item-randomizer.rom]
            [crystal-key-item-randomizer.patches.rockets]
            [crystal-key-item-randomizer.patches.copycat :as copycat]
            [crystal-key-item-randomizer.patches.randomize-janine :as randomize-janine]
            [crystal-key-item-randomizer.specs])
  (:use [crystal-key-item-randomizer.patches.badges :only [replace-checkflag-for-badge]]
        [crystal-key-item-randomizer.patches.text.giveitem :only [fix-giveitems]]
        [crystal-key-item-randomizer.patches.text.received-badge :only [fix-received-badge-texts]]
        [crystal-key-item-randomizer.patches.data :only [get-patch essential-patches]]))

(def underground-warehouse-ultra-ball
  ;; there's a wildcard ("*") here because item randomization might've
  ;; turned this ultra ball into some other item; CKIR should be
  ;; compatible with that, so we can't guarantee the value of this
  ;; byte.
  (-> (get-patch "UndergroundWarehouseUltraBall.ckir_BEFORE_ITEMBALL_ULTRABALL")
      (assoc-in [:integer_values :old 0] "*")))

(s/def ::rom-address int?) ;; less than 2MiB, tbh
(s/def ::begin ::rom-address)
(s/def ::end ::rom-address)
(s/def ::address_range (s/keys :req-un [::begin ::end]))

(s/def ::wildcard-byte #{"*"})
(s/def ::byte-or-wildcard (s/or :byte :crystal-key-item-randomizer.rom/byte
                                :wildcard ::wildcard-byte))
(s/def ::new (s/coll-of ::byte :kind vector?))
(s/def ::old (s/coll-of ::byte-or-wildcard
                        :kind vector?))
;; wtf, old and new are horrible names for specs.
(s/def ::integer_values (s/keys :req-un [::old ::new]))

(s/def ::description string?)
(s/def ::label string?)
(s/def ::patch
  (s/keys :req-un [::integer_values ::address_range]
          :opt-un [::label ::description]))

(s/def ::patches (s/coll-of ::patch
                            :kind? vector))

(defn item-ball [key-item {:keys [rockets]}]
  (let [key-items' (key-items/speedchoice :rockets rockets)
        key-item-value (get-in key-items' [key-item :value])]
    [key-item-value 1]))

(defn replace-underground-warehouse-ultra-ball-with-key-item
  "Change the contents of the item ball from ULTRA_BALL to whatever
  replaces the CARD_KEY (a backup key item so that the player doesn't
  get softlocked). The key integer_values.new doesn't exist in the raw
  patch; things will fail hard if the patches aren't update properly"
  [{card-key-replacement :CARD_KEY} {:keys [rockets]}]
  (assoc-in underground-warehouse-ultra-ball
            [:integer_values :new]
            (item-ball card-key-replacement {:rockets rockets})))

(defn generate [{:keys [item-swaps badge-swaps copycat-item]}
                {:keys [rockets] :or {rockets :normal}
                 :as logic-options}]
  (-> essential-patches
      (conj (replace-underground-warehouse-ultra-ball-with-key-item item-swaps logic-options)
            (replace-checkflag-for-badge :PLAINBADGE badge-swaps)
            (replace-checkflag-for-badge :RISINGBADGE badge-swaps))
      (concat (fix-giveitems item-swaps)
              (fix-received-badge-texts badge-swaps)
              post-defeat/pre-badge-blurb-patches
              post-defeat/post-badge-speech-patches
              (if (or (= :LOST_ITEM copycat-item) (nil? copycat-item))
                [] ;; don't bother
                (copycat/generate copycat-item rockets))
              (if (= :early rockets)
                crystal-key-item-randomizer.patches.rockets/trigger-early
                [])
              (randomize-janine/generate logic-options))))

(s/fdef generate
  :args (s/cat :swaps :crystal-key-item-randomizer.specs/swaps
               :options :crystal-key-item-randomizer.specs/logic-options)
  :ret ::patches)
