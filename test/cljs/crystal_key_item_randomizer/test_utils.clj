(ns crystal-key-item-randomizer.test-utils)

(defmacro slurp-bytes-at-compile-time
  "Slurp the bytes from a slurpable thing"
  [f]
  (clojure.core/with-open [out (java.io.ByteArrayOutputStream.)]
    (clojure.java.io/copy (clojure.java.io/input-stream f) out)
    (->> out .toByteArray
         (mapv #(java.lang.Byte/toUnsignedInt %)))))

(defmacro slurp-edn
  [f]
  (-> f slurp clojure.edn/read-string))
