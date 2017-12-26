state_machine = Diet.Stepper.new(CrystalKeyItemRandomizer.StateMachine, nil)

swaps = CrystalKeyItemRandomizer.key_item_names \
|> Enum.shuffle \
|> Enum.zip(CrystalKeyItemRandomizer.key_item_names) \
|> Enum.into(%{})
{{:ok, _}, _} = Diet.Stepper.run(state_machine, {:begin, swaps})

swaps = CrystalKeyItemRandomizer.key_item_names \
|> Enum.zip(CrystalKeyItemRandomizer.key_item_names) \
|> Enum.into(%{})
Diet.Stepper.run(state_machine, {:begin, swaps})
