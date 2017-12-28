defmodule CrystalKeyItemRandomizer.Reachability.GoldenrodLocked do
  use Diet.Transformations

  @doc ~S"""

  Determines if HM03 would be unreachable.

  ## Examples

    iex> result = Diet.Stepper.new(CrystalKeyItemRandomizer.Reachability.GoldenrodLocked, nil) \
    ...> |> Diet.Stepper.run({:begin, CrystalKeyItemRandomizer.vanilla_swaps})
    ...> with {{:ok, _}, _} <- result, do: :passed
    :passed

  """

  reductions do
    {
      [] = progress_items_obtainable,
      swaps
    } ->
      { :goldenrod_locked, swaps }

    {
      [:SQUIRTBOTTLE, :PASS] = progress_items_obtainable,
      swaps
    } ->
      { :ok, swaps }

    {
      [:SQUIRTBOTTLE] = progress_items_obtainable,
      swaps
    } ->
      { :ok, swaps }

    {
      [:PASS] = progress_items_obtainable,
      %{SUPER_ROD: :S_S_TICKET, HM_STRENGTH: :HM_SURF} = swaps
    } ->
      { :ok, swaps }

    {
      [:PASS] = progress_items_obtainable,
      %{SUPER_ROD: :S_S_TICKET, HM_SURF: :HM_SURF} = swaps
    } ->
      { :ok, swaps }

    {
      [:PASS] = progress_items_obtainable,
      %{SUPER_ROD: :S_S_TICKET, GOOD_ROD: :HM_SURF} = swaps
    } ->
      { :ok, swaps }

    {
      [:PASS] = progress_items_obtainable,
      %{SUPER_ROD: :SQUIRTBOTTLE} = swaps
    } ->
      { :ok, swaps }

    {
      [:PASS] = progress_items_obtainable,
      swaps
    } ->
      { :goldenrod_locked, swaps }

    { :begin, swaps } ->
      {
        [:SQUIRTBOTTLE, :PASS] \
        |> Enum.filter(fn item ->
          Enum.member?(
            CrystalKeyItemRandomizer.pre_goldenrod_items |> Enum.map( &(swaps[&1]) ),
            item
          )
        end),
        swaps
      }
  end
end
