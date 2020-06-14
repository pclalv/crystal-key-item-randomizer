(ns crystal-key-item-randomizer.patches.text.gym-leader-post-defeat
  "The patches here smooth out inconsistencies in in-game text, so that,
  in badge rando, a gym leader doesn't talk about a badge that they
  didn't give. In other words, these patches are only useful for
  making in-game text nice."
  (:require [clojure.java.io :as io])
  (:use [crystal-key-item-randomizer.patches.data :only [get-patch]]
        [crystal-key-item-randomizer.text-encoding :only [gsc-encode-to-original-length]]))

(def pre-badge-blurb-patches
  (->> [{:label "ClairText_Lazy.ckir_BEFORE_text_RISINGBADGE",
         :new_text "\\pThis is your\\nbadge.\\lJust take it.",
         :badge "RISINGBADGE"}
        {:label "LeaderBlueWinText.ckir_BEFORE_text_EARTHBADGE",
         :new_text
         "\\pTch, all right…\\nHere, take this--\\lit's your badge.\\e",
         :badge "EARTHBADGE"}
        {:label "UnknownText_0x18870c.ckir_BEFORE_text_CASCADEBADGE",
         :new_text "\\pHere you go. It's\\nyour badge.\\e",
         :badge "CASCADEBADGE"}
        {:label "UnknownText_0x189df4.ckir_BEFORE_text_MARSHBADGE",
         :new_text "\\pOK, you win. You\\nearned yourself\\lthis badge.\\e",
         :badge "MARSHBADGE"}
        {:label "UnknownText_0x192238.ckir_BEFORE_text_THUNDERBADGE",
         :new_text "\\pOK, kid. You get\\nthis badge!\\e",
         :badge "THUNDERBADGE"}
        {:label "UnknownText_0x195fa1.ckir_BEFORE_text_SOULBADGE",
         :new_text "\\pHere's the badge.\\nTake it.\\e",
         :badge "SOULBADGE"}
        {:label "UnknownText_0x1ab646.ckir_BEFORE_text_VOLCANOBADGE",
         :new_text "\\pYou've earned\\nthis badge!\\e",
         :badge "VOLCANOBADGE"}
        {:label "UnknownText_0x54222.ckir_BEFORE_text_PLAINBADGE",
         :new_text "\\pOh, right.\\nI forgot. Here's\\lyour badge.\\e",
         :badge "PLAINBADGE"}
        {:label "UnknownText_0x6854a.ckir_BEFORE_text_ZEPHYRBADGE",
         :new_text "\\pIt's an official\\n<POKé>MON LEAGUE\\lbadge.\\e",
         :badge "ZEPHYRBADGE"}
        {:label "UnknownText_0x72c3e.ckir_BEFORE_text_RAINBOWBADGE",
         :new_text "\\pI shall give you\\nthis badge…\\e",
         :badge "RAINBOWBADGE"}
        {:label "UnknownText_0x9d7f6.ckir_BEFORE_text_STORMBADGE",
         :new_text "\\pHow about that!\\nYou're worthy of\\lthis BADGE!\\e",
         :badge "STORMBADGE"}]
       (map (fn [txt-data]
              (let [patch (get-patch (:label txt-data))]
                (-> (merge txt-data patch)
                    (assoc-in [:integer_values :new]
                              (gsc-encode-to-original-length (txt-data :new_text)
                                                             (-> patch :integer_values :old count)))))))))

(def post-badge-speech-patches
  (->> [{:label
         "UnknownText_0x685c8.ckir_BEFORE_text_ZEPHYRBADGE_unimportant",
         :new_text "<START>Here--take this\\ntoo.\\e"}
        {:label
         "BugsyText_HiveBadgeSpeech.ckir_BEFORE_text_HIVEBADGE_unimportant",
         :new_text "<START>Here--take this\\ntoo.\\e"}
        {:label
         "UnknownText_0x5428b.ckir_BEFORE_text_PLAINBADGE_unimportant",
         :new_text "<START>Here--take this\\ntoo.\\e"}
        {:label "UnknownText_0x9a059.ckir_BEFORE_text_FOGBADGE_unimportant",
         :new_text "<START>Here--take this\\ntoo.\\e"}
        {:label
         "UnknownText_0x9d84d.ckir_BEFORE_text_STORMBADGE_unimportant",
         :new_text "<START>Here--take this\\ntoo.\\e"}
        {:label
         "UnknownText_0x9c354.ckir_BEFORE_text_MINERALBADGE_unimportant",
         :new_text "<START>Here--take this\\ntoo.\\e"}
        {:label
         "UnknownText_0x199d55.ckir_BEFORE_text_GLACIERBADGE_unimportant",
         :new_text "<START>Here--take this\\ntoo.\\e"}
        {:label
         "BlackthornGymClairText_DescribeBadge.ckir_BEFORE_text_RISINGBADGE_unimportant",
         :new_text "<START>Here--take this\\ntoo.\\e"}
        {:label
         "UnknownText_0x1a2a57.ckir_BEFORE_text_BOULDERBADGE_unimportant",
         :new_text
         "<START>BROCK: <PLAY_G>,\\nthanks. I enjoyed\\pbattling you, even\\nthough I am a bit\\lupset.\\pThat new badge\\nwill make your\\p<POKé>MON even more\\npowerful.\\e"}
        {:label
         "UnknownText_0x192291.ckir_BEFORE_text_THUNDERBADGE_unimportant",
         :new_text
         "<START>SURGE: That\\nbadge increases\\l<POKé>MON's speed. \\pConsider it proof\\nthat you defeated\\pme. You wear it\\nproudly, hear?\\e"}
        {:label
         "UnknownText_0x189ead.ckir_BEFORE_text_MARSHBADGE_unimportant",
         :new_text
         "<START>SABRINA: That\\nbadge draws out\\pyour subliminal\\npowers…\\pAlthough I failed\\nto accurately pre-\\ldict your power,\\lthis much I know\\lto be true.\\pYou will become a\\ncelebrated and\\lbeloved CHAMPION!\\e"}]
       (map (fn [txt-data]
              (let [patch (get-patch (:label txt-data))]
                (-> (merge txt-data patch)
                    (assoc-in [:integer_values :new]
                              (gsc-encode-to-original-length (txt-data :new_text)
                                                             (-> patch :integer_values :old count)))))))))
