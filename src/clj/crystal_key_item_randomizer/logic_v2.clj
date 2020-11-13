(ns crystal-key-item-randomizer.logic-v2)

;; trying to think of how to structure this data about more specific
;; item locations

;; could use some code like this to access nested paths
(let [path [:foo :bar]
      path-getter (apply comp (reverse path))]
  (path-getter {:foo {:bar :qux}}))

(defn item-conditions [{:keys [copycat-item]}]
  {:route-30 {:MYSTERY_EGG {}}
   :sprout-tower {:HM_FLASH {}}
   :route-32 {:OLD_ROD {}}
   :ilex-forest {:HM_CUT {}}

   :goldenrod {:BICYCLE {}
               :SQUIRTBOTTLE {:badges #{:PLAINBADGE}}}
   :underground {:CARD_KEY {:conditions-met #{:underground-warehouse}}
                 :COIN_CASE {}}
   :radio-tower {:BASEMENT_KEY {:conditions-met #{:seven-badges}}
                 :BLUE_CARD {}
                 :CLEAR_BELL {:conditions-met #{:defeat-team-rocket}}}

   :ecruteak {:HM_SURF {}
              :ITEMFINDER {}}
   :olivine {:GOOD_ROD {}
             :HM_STRENGTH {}}

   :cianwood {:HM_FLY {}
              :SECRETPOTION {}}

   :lake-of-rage {:RED_SCALE {:conditions-met #{:can-surf}}}

   :rocket-hideout {:HM_WHIRLPOOL {}}

   :ice-path {:HM_WATERFALL {}}

   :newbark {:S_S_TICKET {}}

   :route-12 {:SUPER_ROD {}}

   :cerulean {:MACHINE_PART {:conditions-met #{:can-surf}}}

   :vermilion {:LOST_ITEM {:conditions-met #{:fix-power-plant}}}

   :saffron {:PASS {:items-obtained #{copycat-item}}}

   :pewter {:SILVER_WING {}}})
