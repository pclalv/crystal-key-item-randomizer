defmodule LockDetector.Reachability.TreeLockedTest do
  use ExUnit.Case, async: true
  alias LockDetector.Reachability.TreeLocked
  doctest TreeLocked

  test "the vanilla swaps are ok" do
    swaps = LockDetector.vanilla_swaps()

    result =
      Diet.Stepper.new(TreeLocked, nil)
      |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "a swap where neither HM_CUT nor SQUIRTBOTTLE is available early" do
    swaps = for item <- LockDetector.pre_tree_items(), do: {item, :FOO}, into: %{}

    result =
      Diet.Stepper.new(TreeLocked, nil)
      |> Diet.Stepper.run({:begin, swaps})

    assert {{:tree_locked, ^swaps}, _} = result
  end
end
