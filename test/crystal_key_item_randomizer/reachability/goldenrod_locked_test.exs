defmodule CrystalKeyItemRandomizer.Reachability.GoldenrodLockedTest do
  use ExUnit.Case, async: true
  alias CrystalKeyItemRandomizer.Reachability.GoldenrodLocked
  doctest GoldenrodLocked

  test "works with the vanilla swaps" do
    swaps = CrystalKeyItemRandomizer.vanilla_swaps
    result = Diet.Stepper.new(GoldenrodLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end
end
