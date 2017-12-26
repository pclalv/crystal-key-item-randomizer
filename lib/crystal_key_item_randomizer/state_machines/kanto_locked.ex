defmodule CrystalKeyItemRandomizer.StateMachines.KantoLocked do
  use Diet.Transformations

  def kanto_items, do: CrystalKeyItemRandomizer.kanto_items
  def kanto_reaching_items, do: [:PASS, :S_S_TICKET]
  def maybe_required_pairs, do: [
    # if (any of) the stuff on the left is in kanto, and the thing on
    # the right is required, then the swap is kanto-locked
    {:BASEMENT_KEY, :CARD_KEY},
    {:CARD_KEY, :CLEAR_BELL},
    # this is a bit of a special case. CARD_KEY and CLEAR_BERLL have
    # other prereqs, but those prereqs are themselves required
    # (e.g. HM_CUT or SQUIRTBOTTLE.)
    {{:LOST_ITEM, :MACHINE_PART}, :PASS}
  ]
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

    { :begin, swaps } ->
      {
        kanto_items,
        maybe_required_pairs,
        kanto_items |> Enum.any?( &(Enum.member?(kanto_reaching_items, swaps[&1])) ),
        false, # assume that there are no required items in kanto
        swaps
      }

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
        Enum.member?(required_items, swaps[head]),
        false,
        swaps
      }
  end
end
