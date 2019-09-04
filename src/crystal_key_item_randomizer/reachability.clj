(ns crystal-key-item-randomizer.reachability
  (:require [reduce-fsm :as fsm]))

(fsm/defsm ss-locked?
  [[:start
    [{:ss-locked false
      :swaps _
      :maybe-required-pairs [{_}]}]
    -> :ss-ticket-replaced-with-required-item!]
   [:
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
  :dispatch :event-and-acc)



