(dotimes [i 100000]
  (spit "seeds.log"
        (-> (crystal-key-item-randomizer.seeds/generate-random {:logic-options {:endgame-condition :defeat-red
                                                                                :rockets :normal}
                                                                :swaps-options {:randomize-badges? true
                                                                                :early-bicycle? true
                                                                                :no-early-sabrina true
                                                                                :no-early-super-rod? true}})
            (:seed)
            (select-keys [:badge-swaps :item-swaps])
            (str "\n"))
        :append true))
