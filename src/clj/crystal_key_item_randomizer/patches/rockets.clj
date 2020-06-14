(ns crystal-key-item-randomizer.patches.rockets
  "Trigger Rocket events earlier"
  (:use [crystal-key-item-randomizer.patches.data :only [get-patch]]))

(def badge-count
  "Index for the desired badge count in the array of bytes defining the if_equal statement"
  1)

(defn update-badge-count [patch desired-badge-count]
  (-> patch
      (assoc-in [:integer_values :new]
                (get-in patch [:integer_values :old]))
      (assoc-in [:integer_values :new badge-count]
                desired-badge-count)))

(def early-goldenrod-rockets
  (->> ["AzaleaGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "BlackthornGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "CeladonGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "CeruleanGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "CianwoodGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "EcruteakGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "FuchsiaGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "GoldenrodGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "MahoganyGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "OlivineGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "PewterGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "SaffronGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "SeafoamGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "VermilionGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "VioletGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"
        "ViridianGymTriggerRockets.ckir_BEFORE_GoldenrodRockets"]
       (map get-patch)
       (map #(update-badge-count % 3))))

(def early-radio-tower-rockets
  (->> 
   ["AzaleaGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "BlackthornGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "CeladonGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "CeruleanGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "CianwoodGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "EcruteakGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "FuchsiaGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "GoldenrodGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "MahoganyGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "OlivineGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "PewterGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "SaffronGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "SeafoamGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "VermilionGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "VioletGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"
    "ViridianGymTriggerRockets.ckir_BEFORE_RadioTowerRockets"]
   (map get-patch)
   (map #(update-badge-count % 4))))

(def trigger-early (concat early-radio-tower-rockets early-goldenrod-rockets))
