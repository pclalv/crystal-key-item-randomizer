state_machine = Diet.Stepper.new(CrystalKeyItemRandomizer.Reachability.KantoLocked, nil)

swaps = CrystalKeyItemRandomizer.key_item_names \
|> Enum.shuffle \
|> Enum.zip(CrystalKeyItemRandomizer.key_item_names) \
|> Enum.into(%{})

{{:ok, _}, _} = Diet.Stepper.run(state_machine, {:begin, swaps})

swaps = CrystalKeyItemRandomizer.key_item_names \
|> Enum.zip(CrystalKeyItemRandomizer.key_item_names) \
|> Enum.into(%{})
Diet.Stepper.run(state_machine, {:begin, swaps})

CrystalKeyItemRandomizer.Reachability.analyze(swaps)

swaps = %{
  BASEMENT_KEY: :BASEMENT_KEY,
  BICYCLE: :BICYCLE,
  BLUE_CARD: :BLUE_CARD,
  CARD_KEY: :CARD_KEY,
  CLEAR_BELL: :CLEAR_BELL,
  COIN_CASE: :COIN_CASE,
  GOOD_ROD: :GOOD_ROD,
  HM_CUT: :HM_CUT,
  HM_FLASH: :HM_FLASH,
  HM_FLY: :HM_FLY,
  HM_STRENGTH: :HM_STRENGTH,
  HM_SURF: :SUPER_ROD,
  HM_WATERFALL: :HM_WATERFALL,
  HM_WHIRLPOOL: :HM_WHIRLPOOL,
  ITEMFINDER: :ITEMFINDER,
  LOST_ITEM: :LOST_ITEM,
  MACHINE_PART: :MACHINE_PART,
  MYSTERY_EGG: :MYSTERY_EGG,
  OLD_ROD: :OLD_ROD,
  PASS: :PASS,
  RED_SCALE: :RED_SCALE,
  SECRETPOTION: :SECRETPOTION,
  SILVER_WING: :SILVER_WING,
  SQUIRTBOTTLE: :SQUIRTBOTTLE,
  SUPER_ROD: :HM_SURF,
  S_S_TICKET: :S_S_TICKET
}
state_machine = Diet.Stepper.new(CrystalKeyItemRandomizer.Reachability.KantoLocked, nil)
Diet.Stepper.run(state_machine, {:begin, swaps})
