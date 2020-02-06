(ns crystal-key-item-randomizer.text-encoding
  (:require [clojure.set :as cset]
            [clojure.java.io :as io]))

(def hex->token
  (-> "text-encoding.edn" io/resource slurp clojure.edn/read-string))
(def token->hex (cset/map-invert hex->token))

(def longest-token-length
  (->> token->hex
       keys
       (map count)
       (apply max)))

(def string-printed-text-end 87)
(def string-terminator
  "Represented by '@' in the disassembly; see charmap.asm."
  80)

(defn gsc-decode [hexs]
  (map hex->token hexs))

(defn gsc-encode
  ([text]
   (gsc-encode text
               []
               ;; start by assuming that the token (one or more
               ;; characters) at the beginning of the string is the
               ;; maximum length.
               longest-token-length))
  ([text encoded assumed-token-length]
   (cond (empty? text) encoded
         (< assumed-token-length 1) (throw (Exception. (str "cannot encode text: invalid token: " text)))
         :else (let [length-to-encode (count text)
                     minimum-safe-bounds (min length-to-encode
                                              assumed-token-length)
                     substring-length (max minimum-safe-bounds
                                           1)
                     token (subs text 0 substring-length)
                     hex (get token->hex token)]
                 (if hex
                   (recur (subs text substring-length length-to-encode)
                          (conj encoded hex)
                          longest-token-length)
                   ;; the token of assumed-token-length was not a real
                   ;; token; next, we have to check if we can get a valid
                   ;; token by chopping off the last character.
                   (recur text
                          encoded
                          (dec assumed-token-length)))))))

(defn pad [coll n val]
  (vec (take n (concat coll (repeat val)))))

(defn gsc-encode-to-original-length [text original-text-length]
  (let [encoded (gsc-encode text)]
    (cond
      (= original-text-length (count encoded)) encoded
      (> original-text-length (count encoded)) (-> encoded
                                                   (conj string-terminator)
                                                   (pad original-text-length 0))
      (< original-text-length (count encoded)) (throw (Exception. (str "gsc-encode-to-original-length: Encoded text was longer than original text:"
                                                                       "text: " text
                                                                       "original-text-length: " original-text-length))))))
