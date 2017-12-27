defmodule CrystalKeyItemRandomizer.Reachability.KantoLocked do
  use Diet.Transformations

  @doc ~S"""

  Determines if it is possible to reach Kanto, and if it
  not possible to reach Kanto, then determines whether or not there
  are any required items in Kanto.

  ## Examples

      iex> Diet.Stepper.new(CrystalKeyItemRandomizer.Reachability.KantoLocked, nil) \
      ...> |> Diet.Stepper.run({:begin, CrystalKeyItemRandomizer.vanilla_swaps})
      {{:ok, _}, _}

  """

  def kanto_reaching_items, do: [:PASS, :S_S_TICKET]
  def required_items, do: Map.keys(CrystalKeyItemRandomizer.required_items)

  reductions do
    # state
    # {
    #   kanto_items,
    #   maybe_required_pairs,
    #   can_reach_kanto?,
    #   required_items_in_kanto?,
    #   swaps
    # }

    # final states

    # if you can reach kanto, just bail
    {
      kanto_items,
      maybe_required_pairs,
      true = can_reach_kanto?,
      required_items_in_kanto?,
      swaps
    } ->
      { :ok, swaps }

    # if we check all of the items and there are no required items in kanto, we're good
    {
      [] = kanto_items,
      [] = maybe_required_pairs,
      false = can_reach_kanto?,
      false = required_items_in_kanto?,
      swaps
    } ->
      { :ok, swaps }

    # otherwise, it's kanto-locked
    {
      kanto_items,
      maybe_required_pairs,
      false = can_reach_kanto?,
      true = required_items_in_kanto?,
      swaps
    } ->
      { :kanto_locked, swaps }

    # checks

    {
      [] = kanto_items,
      [{{prereq1, prereq2}, maybe_required} = head | tail] = maybe_required_pairs,
      false = can_reach_kanto?,
      false = required_items_in_kanto?,
      swaps
    } ->
      {
        [],
        tail,
        false,
        Enum.member?(required_items, swaps[maybe_required])
          && CrystalKeyItemRandomizer.kanto_items |> Enum.any?( &(swaps[&1] == prereq1 || swaps[&1] == prereq2) ),
        swaps
      }

    {
      [] = kanto_items,
      [{prereq, maybe_required} = head | tail] = maybe_required_pairs,
      false = can_reach_kanto?,
      false = required_items_in_kanto?,
      swaps
    } ->
      {
        [],
        tail,
        false,
        Enum.member?(required_items, swaps[maybe_required])
          && CrystalKeyItemRandomizer.kanto_items |> Enum.any?( &(swaps[&1] == prereq) ),
        swaps
      }

    {
      [head | tail] = kanto_items,
      maybe_required_pairs,
      false = can_reach_kanto?,
      false = required_items_in_kanto?,
      swaps
    } ->
      {
        tail,
        maybe_required_pairs,
        false,
        Enum.member?(required_items, swaps[head]),
        swaps
      }

    { :begin, swaps } ->
      {
        CrystalKeyItemRandomizer.kanto_items,
        CrystalKeyItemRandomizer.maybe_required_pairs,
        kanto_reaching_items |> Enum.any?( &(! Enum.member?(kanto_items, swaps[&1])) ),
        false, # assume that there are no required items in kanto
        swaps
      }
  end
end
