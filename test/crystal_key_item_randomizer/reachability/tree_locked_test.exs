defmodule CrystalKeyItemRandomizer.Reachability.TreeLockedTest do
  use ExUnit.Case, async: true
  alias CrystalKeyItemRandomizer.Reachability.TreeLocked
  doctest TreeLocked

  test "works with the vanilla swaps" do
    swaps = CrystalKeyItemRandomizer.vanilla_swaps
    result = Diet.Stepper.new(TreeLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end
end
