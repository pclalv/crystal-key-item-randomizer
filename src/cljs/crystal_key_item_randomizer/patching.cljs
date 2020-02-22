(ns crystal-key-item-randomizer.patching
  (:require [crystal-key-item-randomizer.badges :as badges]
            [crystal-key-item-randomizer.key-items :as key-items]
            [cljs.spec.alpha :as s]
            [crystal-key-item-randomizer.specs]))

(def wildcard "*")

(defn throw-js [str]
  (throw (clj->js {:error str})))

(defn update-address [label]
  (fn [rom-bytes {:keys [address old-value new-value]}]
    (if (or (= old-value wildcard)
            (= old-value (aget rom-bytes address)))
      (do (aset rom-bytes address new-value)
          rom-bytes)
      (throw-js (str "Expected \"" old-value
                     "\" at address \"" address
                     "\" but found \"" (aget rom-bytes address)
                     "\" for label \"" label "\""
                     "\". Make sure to select a Speedchoice v6 ROM (with or without other randomizations).")))))

(defn apply-patch [rom-bytes {{old-values :old new-values :new} :integer_values
                              {begin-addr :begin end-addr :end} :address_range
                              label :label
                              :as patch}]
  (let [address-range (range begin-addr end-addr)]
    (if (not= (count address-range) (count old-values) (count new-values))
      (throw (str "Mismatch between address range, old values and new values for \"" label "\"."))
      (let [address-values (map (fn [address old-value new-value]
                                  {:address address :old-value old-value :new-value new-value})
                                address-range
                                old-values
                                new-values)]
        (reduce (update-address label)
                rom-bytes
                address-values)))))

(defn apply-patches [rom-bytes patches]
  (reduce apply-patch rom-bytes patches))

(defn apply-badge-swap [rom-bytes [original replacement]]
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

(defn apply-item-swap [rockets]
  (fn [rom-bytes [original replacement]]
    (let [key-items (key-items/speedchoice :rockets rockets)
          original-address (-> (keyword original)
                               key-items
                               :address)
          replacement-value (-> (keyword replacement)
                                key-items
                                :value)]
      (aset rom-bytes original-address replacement-value)
      rom-bytes)))

(defn apply-item-swaps [rom-bytes swaps {:keys [rockets] :as seed-options}]
  (reduce (apply-item-swap rockets) rom-bytes (js->clj swaps)))

(defn patch-rom [rom-bytes {:keys [item-swaps badge-swaps patches options]}]
  (-> rom-bytes
      (apply-item-swaps item-swaps options)
      (apply-badge-swaps badge-swaps)
      (apply-patches patches)))


;; TODO: add with-gen for this JS object
;; https://stackoverflow.com/questions/41176696/clojure-spec-custom-generator-for-java-objects
;; it'd be nice if we could actually assert on this thing as a collection;
;; otherwise, just use simpler objects...
(s/def ::uint8array (partial instance? js/Uint8Array))

(s/fdef patch-rom
  :args (s/cat :rom-bytes ::uint8array
               :seed :crystal-key-item-randomizer.specs/seed))
