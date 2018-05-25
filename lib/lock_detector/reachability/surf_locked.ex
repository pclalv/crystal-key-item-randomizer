defmodule LockDetector.Reachability.SurfLocked do
  use Diet.Transformations

  @doc ~S"""

  Determines if HM03 would be unreachable.

  ## Examples

  """

  reductions do
    { true = surf_locked, swaps } ->
      { :surf_locked, swaps }

    { false = surf_locked, swaps } ->
      { :ok, swaps }

    { :begin, swaps } ->
      {
        Enum.any?(
          LockDetector.surf_blocked_items,
          &( swaps[&1] == :HM_SURF )
        ),
        swaps
      }
  end
end
