(ns crystal-key-item-randomizer.patches.fix-radio-tower-boss
  (:require [clojure.spec.alpha :as s])
  (:use [crystal-key-item-randomizer.patches.data :only [get-patch]]))

(def v6-patch-names
  #{"FakeDirectorScript.ckir_BEFORE_setscene_SCENE_RADIOTOWER5F_ROCKET_BOSS"
    "RadioTower5FDirectorThankYouText.ckir_BEFORE_RadioTower5FDirectorThankYouText"
    "RadioTower5F_MapEventHeader.ckir_BEFORE_object_event_EVENT_RADIO_TOWER_5F_ULTRA_BALL"
    "RadioTower5F_MapEventHeader.ckir_BEFORE_object_event_ObjectEvent_RocketBoss"
    "ckir_BEFORE_RadioTower5FRocketBossScene_NPC_0"
    "ckir_BEFORE_RadioTower5FRocketBossScene_NPC_1"})

(def v7-patch-names
  #{"RadioTower5FDirectorThankYouText.ckir_BEFORE_RadioTower5FDirectorThankYouText"
    "RadioTower5FRocketBossScene.ckir_BEFORE_applymovement_PLAYER_RadioTower5FPlayerTwoStepsLeftMovement"
    "RadioTower5F_MapEvents.ckir_BEFORE_object_event_EVENT_RADIO_TOWER_5F_ULTRA_BALL"
    "RadioTower5F_MapEvents.ckir_BEFORE_object_event_ObjectEvent_RocketBoss"
    "ckir_BEFORE_RadioTower5FRocketBossScene_NPC_0"
    "ckir_BEFORE_RadioTower5FRocketBossScene_NPC_1"})

(defn apply
  "If the player does not wish to fix the Radio Tower boss,
  then these diffs must be _removed_ from the diff list."
  [patches {:keys [fix-radio-tower-boss?]}]
  (let [patch-names v6-patch-names]
    (if fix-radio-tower-boss?
      patches
      (remove #(patch-names (:label %)) patches))))
