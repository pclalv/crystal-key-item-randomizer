defmodule LockDetector.Reachability.SurfLockedTest do
  use ExUnit.Case, async: true
  alias LockDetector.Reachability.SurfLocked
  doctest SurfLocked

  test "works with the vanilla swaps" do
    swaps = LockDetector.vanilla_swaps()

    result =
      Diet.Stepper.new(SurfLocked, nil)
      |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "a swap where HM_SURF is swapped with an item that can only be obtained by surfing" do
    surf_blocked_item = Enum.random(LockDetector.surf_blocked_items())
    swaps = for item <- LockDetector.surf_blocked_items(), do: {item, :FOO}, into: %{}
    swaps = Map.put(swaps, surf_blocked_item, :HM_SURF)

    result =
      Diet.Stepper.new(SurfLocked, nil)
      |> Diet.Stepper.run({:begin, swaps})

    assert {{:surf_locked, ^swaps}, _} = result
  end
end
