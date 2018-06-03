defmodule SwapGenerator.Reachability.SSLockedTest do
  use ExUnit.Case, async: true
  alias SwapGenerator.Reachability.SSLocked
  doctest SSLocked

  test "works with the vanilla swaps" do
    swaps = SwapGenerator.vanilla_swaps()

    result =
      Diet.Stepper.new(SSLocked, nil)
      |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "a swaps when the S_S_TICKET is replaced with a required item" do
    swaps = %{S_S_TICKET: Enum.random(SwapGenerator.required_item_names())}

    result =
      Diet.Stepper.new(SSLocked, nil)
      |> Diet.Stepper.run({:begin, swaps})

    assert {{:ss_locked, ^swaps}, _} = result
  end

  test "a swap when the S_S_TICKET is replaced with a maybe-required item" do
    {prereq, maybe_required} = Enum.random(SwapGenerator.maybe_required_pairs())

    swaps = %{
      :S_S_TICKET => prereq,
      maybe_required => Enum.random(SwapGenerator.required_item_names())
    }

    result =
      Diet.Stepper.new(SSLocked, nil)
      |> Diet.Stepper.run({:begin, swaps})

    assert {{:ss_locked, ^swaps}, _} = result
  end
end
