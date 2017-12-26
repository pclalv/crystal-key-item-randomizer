state_machine = Diet.Stepper.new(CrystalKeyItemRandomizer.StateMachines.KantoLocked, nil)

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

# TODO: resolve this case:
swaps = %{BASEMENT_KEY: :SECRETPOTION, BICYCLE: :OLD_ROD, BLUE_CARD: :SQUIRTBOTTLE,
  CARD_KEY: :BICYCLE, CLEAR_BELL: :SUPER_ROD, COIN_CASE: :GOOD_ROD,
  GOOD_ROD: :RED_SCALE, HM_CUT: :HM_STRENGTH, HM_FLASH: :MYSTERY_EGG,
  HM_FLY: :HM_CUT, HM_STRENGTH: :HM_FLY, HM_SURF: :HM_SURF,
  HM_WATERFALL: :CARD_KEY, HM_WHIRLPOOL: :HM_FLASH, ITEMFINDER: :HM_WHIRLPOOL,
  LOST_ITEM: :BLUE_CARD, MACHINE_PART: :SILVER_WING, MYSTERY_EGG: :S_S_TICKET,
  OLD_ROD: :PASS, PASS: :HM_WATERFALL, RED_SCALE: :BASEMENT_KEY,
  SECRETPOTION: :LOST_ITEM, SILVER_WING: :ITEMFINDER, SQUIRTBOTTLE: :COIN_CASE,
  SUPER_ROD: :CLEAR_BELL, S_S_TICKET: :MACHINE_PART}
