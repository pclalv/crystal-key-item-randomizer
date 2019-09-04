(ns crystal-key-item-randomizer.reachability
  (:require [reduce-fsm :as fsm]))

(defn any? [pred col]
  (not (not-any? pred col)))

(defn initialize [swaps]
  {:maybe-required-pairs crystal-key-item-randomizer.randomizer/maybe-required-pairs
   :ss-ticket-replaced-with-required-item? (->> crystal-key-item-randomizer.randomizer/required-item-names
                                                (any? (fn [required-item] (= (swaps :S_S_TICKET)
                                                                             required-item))))
   :swaps swaps})

(defn ss-ticket-replaced-with-required-item! [acc]
  {:ss-locked})

(fsm/defsm ss-locked
  [[:start
    [_]
    -> {:action initialize} :start']
   [:start'
    [false {:maybe-required-pairs _}
     :ss-ticket-replaced-with-required-item? true
     :swaps _]
    -> :ss-ticket-replaced-with-required-item!]
   [:ss-ticket-replaced-with-required-item!
    [_]
    -> {:action ss-ticket-replaced-with-required-item!}]
      
    
   [:found-a
    [_ \a] -> :found-a
    [_ \b] -> {:action inc-b-count} :found-b
    [_ _]  -> :start]   
   [:found-b
    [(n :when count-satisfied?) \c] -> {:action matched-event} done-state
    [_ \b] -> {:action inc-b-count} :found-b
    [_ _]  -> {:action reset-b-count} :start]
   [done-state]]
  :default-acc false
  :dispatch :event-and-acc)

