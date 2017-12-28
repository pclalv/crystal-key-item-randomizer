defmodule CrystalKeyItemRandomizer.Reachability.SurfLocked do
  use Diet.Transformations

  @doc ~S"""

  Determines if HM03 would be unreachable.

  ## Examples

    iex> result = Diet.Stepper.new(CrystalKeyItemRandomizer.Reachability.SurfLocked, nil) \
    ...> |> Diet.Stepper.run({:begin, CrystalKeyItemRandomizer.vanilla_swaps})
    ...> with {{:ok, _}, _} <- result, do: :passed
    :passed

  """

  reductions do
    { true = surf_locked, swaps } ->
      { :surf_locked, swaps }

    { false = surf_locked, swaps } ->
      { :ok, swaps }

    { :begin, swaps } ->
      {
        Enum.any?(
          CrystalKeyItemRandomizer.surf_blocked_items,
          &( swaps[&1] == :HM_SURF )
        ),
        swaps
      }
  end
end
