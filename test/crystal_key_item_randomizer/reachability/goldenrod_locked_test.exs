defmodule CrystalKeyItemRandomizer.Reachability.GoldenrodLockedTest do
  use ExUnit.Case, async: true
  alias CrystalKeyItemRandomizer.Reachability.GoldenrodLocked
  doctest GoldenrodLocked

  # when SQUIRTBOTTLE is obtainable, but not PASS
  test "works with the vanilla swaps" do
    swaps = CrystalKeyItemRandomizer.vanilla_swaps
    result = Diet.Stepper.new(GoldenrodLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "when PASS is obtainable, and SUPER_ROD is the SQUIRTBOTTLE" do
    swaps = (for item <- CrystalKeyItemRandomizer.pre_goldenrod_items, do: {item, :FOO}, into: %{})
    |> Map.put(Enum.random(CrystalKeyItemRandomizer.pre_goldenrod_items), :PASS)
    |> Map.put(:SUPER_ROD, :SQUIRTBOTTLE)
    result = Diet.Stepper.new(GoldenrodLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "when PASS is obtainable, and SUPER_ROD is the S_S_TICKET and GOOD_ROD is HM_SURF" do
    swaps = (for item <- CrystalKeyItemRandomizer.pre_goldenrod_items, do: {item, :FOO}, into: %{})
    |> Map.put(Enum.random(CrystalKeyItemRandomizer.pre_goldenrod_items), :PASS)
    |> Map.put(:SUPER_ROD, :S_S_TICKET)
    |> Map.put(:GOOD_ROD, :HM_SURF)
    result = Diet.Stepper.new(GoldenrodLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "when PASS is obtainable, and SUPER_ROD is the S_S_TICKET and HM_SURF is HM_SURF" do
    swaps = (for item <- CrystalKeyItemRandomizer.pre_goldenrod_items, do: {item, :FOO}, into: %{})
    |> Map.put(Enum.random(CrystalKeyItemRandomizer.pre_goldenrod_items), :PASS)
    |> Map.put(:SUPER_ROD, :S_S_TICKET)
    |> Map.put(:HM_SURF, :HM_SURF)
    result = Diet.Stepper.new(GoldenrodLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "when PASS is obtainable, and SUPER_ROD is the S_S_TICKET and HM_STRENGTH is HM_SURF" do
    swaps = (for item <- CrystalKeyItemRandomizer.pre_goldenrod_items, do: {item, :FOO}, into: %{})
    |> Map.put(Enum.random(CrystalKeyItemRandomizer.pre_goldenrod_items), :PASS)
    |> Map.put(:SUPER_ROD, :S_S_TICKET)
    |> Map.put(:HM_STRENGTH, :HM_SURF)
    result = Diet.Stepper.new(GoldenrodLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "when SQUIRTBOTTLE is obtainable" do
    swaps = (for item <- CrystalKeyItemRandomizer.pre_goldenrod_items, do: {item, :FOO}, into: %{})
    |> Map.put(Enum.random(CrystalKeyItemRandomizer.pre_goldenrod_items), :SQUIRTBOTTLE)
    result = Diet.Stepper.new(GoldenrodLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end

  test "when SQUIRTBOTTLE and PASS are obtainable" do
    [random_item1, random_item2] = CrystalKeyItemRandomizer.pre_goldenrod_items
    |> Enum.take_random(2)
    swaps = (for item <- CrystalKeyItemRandomizer.pre_goldenrod_items, do: {item, :FOO}, into: %{})
    |> Map.put(random_item1, :SQUIRTBOTTLE)
    |> Map.put(random_item2, :PASS)
    result = Diet.Stepper.new(GoldenrodLocked, nil)
    |> Diet.Stepper.run({:begin, swaps})

    assert {{:ok, ^swaps}, _} = result
  end
end
