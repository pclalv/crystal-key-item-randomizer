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

  test "if it's possible to reach kanto because the PASS is not in kanto" do
    non_kanto_items = CrystalKeyItemRandomizer.key_item_names -- CrystalKeyItemRandomizer.kanto_items
    swaps = %{} |> Map.put(Enum.random(non_kanto_items), :PASS)

    result = Diet.Stepper.new(KantoLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "if it's possible to reach kanto because the S_S_TICKET is not in kanto" do
    non_kanto_items = CrystalKeyItemRandomizer.key_item_names -- CrystalKeyItemRandomizer.kanto_items
    swaps = %{} |> Map.put(Enum.random(non_kanto_items), :S_S_TICKET)

    result = Diet.Stepper.new(KantoLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "if it's not possible to reach kanto and a required item is in kanto" do
    [random_item1, random_item2, random_item3] = CrystalKeyItemRandomizer.kanto_items
    |> Enum.take_random(3)
    swaps = %{}
    |> Map.put(random_item1, :S_S_TICKET)
    |> Map.put(random_item2, :PASS)
    |> Map.put(random_item3, Enum.random(CrystalKeyItemRandomizer.required_item_names))

    result = Diet.Stepper.new(KantoLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:kanto_locked, ^swaps}, _} = result
  end

  test "if it's not possible to reach kanto and a maybe-required item is required and its prereq is in kanto" do
    [random_item1, random_item2, random_item3] = CrystalKeyItemRandomizer.kanto_items
    |> Enum.take_random(3)
    non_kanto_maybe_requireds = (for {prereq, maybe_required} <- CrystalKeyItemRandomizer.maybe_required_pairs, Enum.member?([:BASEMENT_KEY, :CARD_KEY], maybe_required), do: {prereq, maybe_required}, into: %{})
    {prereq, maybe_required} = Enum.random(non_kanto_maybe_requireds)
    swaps = %{}
    |> Map.put(random_item1, :S_S_TICKET)
    |> Map.put(random_item2, :PASS)
    |> Map.put(random_item3, prereq)
    |> Map.put(maybe_required, Enum.random(CrystalKeyItemRandomizer.required_item_names))

    result = Diet.Stepper.new(KantoLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:kanto_locked, ^swaps}, _} = result
  end
end
