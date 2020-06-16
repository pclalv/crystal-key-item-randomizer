(def speedchoice-key-item-data
  "Generates crystal-key-item-randomizer.key-items/speedchoice.

  Because that code is cljc (compiled to both clj and cljs), we can't
  inline this stuff in that file... or can we?

  Update: no, we can't, because the below code depends on clj code."
  (->> ["CeruleanGymHiddenMachinePart.ckir_BEFORE_dwb_EVENT_FOUND_MACHINE_PART_IN_CERULEAN_GYM_MACHINE_PART"
        "UnknownScript_0x1a009c.ckir_BEFORE_verbosegiveitem_HM_FLY"
        "CianwoodPharmacist.ckir_BEFORE_giveitem_SECRETPOTION"
        "Copycat.ckir_BEFORE_verbosegiveitem_PASS"
        "UnknownScript_0x99505.ckir_BEFORE_verbosegiveitem_HM_SURF"
        "CooltrainerMScript_0x9a5fb.ckir_BEFORE_verbosegiveitem_ITEMFINDER"
        "ElmGiveTicketScript.ckir_BEFORE_verbosegiveitem_S_S_TICKET"
        "ClerkScript_0x54750.ckir_BEFORE_giveitem_BICYCLE"
        "FlowerShopTeacherScript.ckir_BEFORE_verbosegiveitem_SQUIRTBOTTLE"
        "IcePath1FHMWaterfall.ckir_BEFORE_itemball_HM_WATERFALL"
        "IlexForestCharcoalMasterScript.ckir_BEFORE_verbosegiveitem_HM_CUT"
        "UnknownScript_0x7007a.ckir_BEFORE_giveitem_RED_SCALE"
        "MrPokemonsHouse_MapScriptHeader.ckir_BEFORE_giveitem_MYSTERY_EGG"
        "SailorScript_0x9c8c1.ckir_BEFORE_verbosegiveitem_HM_STRENGTH"
        "GoodRodGuru.ckir_BEFORE_verbosegiveitem_GOOD_ROD"
        "GrampsScript_0x18c00f.ckir_BEFORE_verbosegiveitem_SILVER_WING"
        "UnknownScript_0x191844.ckir_BEFORE_giveitem_LOST_ITEM"
        "UnknownScript_0x5d800.ckir_BEFORE_verbosegiveitem_BLUE_CARD"
        "FakeDirectorScript.ckir_BEFORE_verbosegiveitem_BASEMENT_KEY"
        "ckir_BEFORE_verbosegiveitem_CLEAR_BELL"
        "FishingGuruScript_0x7f484.ckir_BEFORE_verbosegiveitem_SUPER_ROD"
        "FishingGuruScript_0x69b55.ckir_BEFORE_verbosegiveitem_OLD_ROD"
        "SageLiScript.ckir_BEFORE_verbosegiveitem_HM_FLASH"
        "UnknownScript_0x6d184.ckir_BEFORE_verbosegiveitem_HM_WHIRLPOOL"
        "GentlemanScript_0x7d9bf.ckir_BEFORE_verbosegiveitem_CARD_KEY"
        "WarehouseEntranceCoinCase.ckir_BEFORE_itemball_COIN_CASE"

        "RocketlessLoRLanceScript2.ckir_BEFORE_verbosegiveitem_BASEMENT_KEY"
        "RocketlessLoRLanceScript2.ckir_BEFORE_verbosegiveitem_CARD_KEY"
        "RocketlessLoRLanceScript2.ckir_BEFORE_verbosegiveitem_CLEAR_BELL"
        "RocketlessLoRLanceScript2.ckir_BEFORE_verbosegiveitem_HM_WHIRLPOOL"]
       (map (fn [label]
              (let [patch (crystal-key-item-randomizer.patches.data/get-patch label)
                    asm-call (cond (clojure.string/includes? label "verbosegiveitem") :verbosegiveitem
                                   (clojure.string/includes? label "giveitem") :giveitem
                                   (clojure.string/includes? label "itemball") :itemball
                                   (clojure.string/includes? label "MACHINE_PART") :hiddenitem
                                   :else (throw (str "no match for label " label)))
                    key-item (if (= :hiddenitem asm-call)
                               "MACHINE_PART"
                               (-> (re-pattern (str ".*" (name asm-call) "_(\\w+)"))
                                   (re-matches label)
                                   last))
                    asm-call-item-index (asm-call {:verbosegiveitem 1
                                                   :giveitem 1
                                                   :itemball 0
                                                   :hiddenitem 2})]
                [(keyword key-item) {:value (-> patch
                                                :integer_values :old
                                                (nth asm-call-item-index))
                                     :address (-> patch
                                                  :address_range :begin
                                                  (+ asm-call-item-index))}])))
       (into {})))
