defmodule LockDetector.Reachability.SSLocked do
  use Diet.Transformations

  @doc ~S"""

  Determines if the S_S_TICKET has been replaced by any required item.

  """

  reductions do
    {
      [] = maybe_required_pairs,
      false = replaced_with_required_item?,
      swaps
    } ->
      {:ok, swaps}

    {
      maybe_required_pairs,
      true = replaced_with_required_item?,
      swaps
    } ->
      {:ss_locked, swaps}

    {
      [{prereq, maybe_required} | tail] = maybe_required_pairs,
      false = replaced_with_required_item?,
      swaps
    } ->
      {
        tail,
        swaps[:S_S_TICKET] == prereq &&
          Enum.member?(LockDetector.required_item_names(), swaps[maybe_required]),
        swaps
      }

    {:begin, swaps} ->
      {
        LockDetector.maybe_required_pairs(),
        LockDetector.required_item_names() |> Enum.any?(&(swaps[:S_S_TICKET] == &1)),
        swaps
      }
  end
end
