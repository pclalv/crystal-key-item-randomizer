defmodule SwapGenerator.Reachability.KantoLocked do
  use Diet.Transformations

  @doc ~S"""

  Determines if it is possible to reach Kanto, and if it is not
  possible to reach Kanto, then determines whether or not there are
  any required items in Kanto.

  The swaps are kanto-locked if Kanto is inaccessible but any required
  key item is in Kanto.

  ## Examples

  """

  def kanto_reaching_items, do: [:PASS, :S_S_TICKET]
  def required_items, do: Map.keys(SwapGenerator.required_items())

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
      {:ok, swaps}

    # if we check all of the items and there are no required items in kanto, we're good
    {
      [] = kanto_items,
      [] = maybe_required_pairs,
      false = can_reach_kanto?,
      false = required_items_in_kanto?,
      swaps
    } ->
      {:ok, swaps}

    # otherwise, it's kanto-locked
    {
      kanto_items,
      maybe_required_pairs,
      false = can_reach_kanto?,
      true = required_items_in_kanto?,
      swaps
    } ->
      {:kanto_locked, swaps}

    # then iteratively check maybe required item pairs. if
    # maybe_required _is_ required and the prereq to that
    # maybe_Required is in kanto, you're boned

    {
      [] = kanto_items,
      [{prereq, maybe_required} | tail] = maybe_required_pairs,
      false = can_reach_kanto?,
      false = required_items_in_kanto?,
      swaps
    } ->
      {
        [],
        tail,
        false,
        Enum.member?(required_items, swaps[maybe_required]) &&
          SwapGenerator.kanto_items() |> Enum.any?(&(swaps[&1] == prereq)),
        swaps
      }

    # first iteratively check if any required item is in kanto item

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

    {:begin, swaps} ->
      {
        SwapGenerator.kanto_items(),
        SwapGenerator.maybe_required_pairs(),
        kanto_reaching_items
        |> Enum.any?(fn item ->
          !Enum.member?(
            SwapGenerator.kanto_items() |> Enum.map(&swaps[&1]),
            item
          )
        end),
        # assume that there are no required items in kanto
        false,
        swaps
      }
  end
end
