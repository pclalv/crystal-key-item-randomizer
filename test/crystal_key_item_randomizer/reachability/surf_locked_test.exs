defmodule CrystalKeyItemRandomizer.Reachability.SurfLockedTest do
  use ExUnit.Case, async: true
  alias CrystalKeyItemRandomizer.Reachability.SurfLocked
  doctest SurfLocked

  test "works with the vanilla swaps" do
    swaps = CrystalKeyItemRandomizer.vanilla_swaps
    result = Diet.Stepper.new(SurfLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end
end
