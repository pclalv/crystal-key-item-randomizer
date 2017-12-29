defmodule CrystalKeyItemRandomizer.Reachability.KantoLockedTest do
  use ExUnit.Case, async: true
  alias CrystalKeyItemRandomizer.Reachability.KantoLocked
  doctest KantoLocked

  test "works with the vanilla swaps" do
    swaps = CrystalKeyItemRandomizer.vanilla_swaps
    result = Diet.Stepper.new(KantoLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end
end
