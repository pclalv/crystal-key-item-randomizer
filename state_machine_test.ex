alias Diet.Stepper
state_machine = Stepper.new(CrystalKeyItemRandomizer.StateMachine, nil)
swaps = CrystalKeyItemRandomizer.key_item_names \
|> Enum.zip(CrystalKeyItemRandomizer.key_item_names) \
|> Enum.into(%{})
{{:done, _}, _} = Stepper.run(state_machine, {:begin, swaps})
