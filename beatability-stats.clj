(defn beatability-stats [n]
  (let [{:keys [beatable not-beatable]} (->> (take n (repeatedly #(-> java.util.Random
                                                                      new
                                                                      .nextLong
                                                                      java.lang.Math/abs)))
                                             (map crystal-key-item-randomizer.seeds/generate)
                                             (group-by #(if (-> % :seed :beatable?)
                                                          :beatable
                                                          :not-beatable)))]
    {:beatable (count beatable)
     :not-beatable (count not-beatable)
     :ratio (float (/ (count beatable)
                      n))}))

(let [n 10000
      {:keys [beatable not-beatable]} (->> (take n (repeatedly #(-> java.util.Random
                                                                    new
                                                                    .nextLong
                                                                    java.lang.Math/abs)))
                                           (map #(crystal-key-item-randomizer.seeds/generate % {:randomize-badges? true}))
                                           (group-by #(if (-> % :seed :beatable?)
                                                        :beatable
                                                        :not-beatable)))]
  {:beatable (count beatable)
   :not-beatable (count not-beatable)
   :ratio (float (/ (count beatable)
                    n))})
