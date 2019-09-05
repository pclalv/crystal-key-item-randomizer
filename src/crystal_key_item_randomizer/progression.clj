(ns crystal-key-item-randomizer.progression)

(defn can-reach-goldenrod1? [{:keys [swaps]}]
  (let [initial-items (->> swaps
                           (select-keys '(:MYSTERY_EGG
                                          :HM_FLASH   
                                          :OLD_ROD    
                                          :HM_CUT))
                           (vals))
        can-progress-to-goldenrod? (contains? initial-items :HM_CUT)])
  (if can-progress-to-goldenrod?
    {:swaps swaps}
    {:swaps swaps
     :progress-items [:HM_CUT]
     :cannot-progress? true
     :reason "goldenrod1: cannot progress without HM_CUT"}))

(defn can-reach-ecruteak? [{:keys [swaps progress-items cannot-progress?] :as args}]
  (if cannot-progress?
    args
    (let [goldenrod1-items (->> swaps
                                (select-kets '(:BICYCLE     
                                               :BLUE_CARD   
                                               :COIN_CASE   
                                               :SQUIRTBOTTLE))
                                (vals))]
      (if ))))



(def beatable? [swaps]
  (->> {:swaps swaps}
       (can-reach-goldenrod1?)
       (can-reach-ecruteak?)))
