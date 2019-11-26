(ns crystal-key-item-randomizer.text-encoding)

(def hex->token-jp
  (let [encoding-table-lines (-> "resources/gameboy_jap.tbl"
                                 slurp
                                 (clojure.string/split #"\n"))]
    (->> encoding-table-lines
         (map #(clojure.string/split % #"="))
         (map (fn [[k v]]
                [(Integer/parseInt k 16) v]))
         (into {}))))

(def hex->token-eng
  (let [encoding-table-lines (-> "resources/gsc_english.tbl"
                                 slurp
                                 (clojure.string/split #"\n"))]
    (->> encoding-table-lines
         (map #(clojure.string/split % #"="))
         (map (fn [[k v]]
                [(Integer/parseInt k 16) v]))
         (into {}))))

(def hex->token (merge hex->token-jp hex->token-eng))
(def token->hex (clojure.set/map-invert hex->token))

(def longest-token-length
  (->> token->hex
       keys
       (map count)
       (apply max)))

(def string-printed-text-end 87)
(def string-terminator 80)

(defn gsc-decode [hexs]
  (map hex->token hexs))

(defn gsc-encode
  ([text]
   (gsc-encode text
               ;; encoded string have an initial zero byte.
               [0]))
  ([text encoded]
   (gsc-encode text
               encoded
               ;; start by assuming that the token (one or more
               ;; characters) at the beginning of the string is the
               ;; maximum length.
               longest-token-length))
  ([text encoded assumed-token-length]
   (if (empty? text)
     encoded
     (let [length-to-encode (count text)
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


