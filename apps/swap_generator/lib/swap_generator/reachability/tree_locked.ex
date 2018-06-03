defmodule SwapGenerator.Reachability.TreeLocked do
  use Diet.Transformations

  @doc ~S"""

  Determines if they player cannot bypass either the cuttable tree in
  Ilex Forest, or the Sudowoodo between Violet, Goldenrod and
  Ecruteak.

  ## Examples

  """

  reductions do
    {true = tree_locked, swaps} ->
      {:tree_locked, swaps}

    {false = tree_locked, swaps} ->
      {:ok, swaps}

    {:begin, swaps} ->
      {
        !Enum.any?(
          SwapGenerator.pre_tree_items(),
          &(swaps[&1] == :HM_CUT || swaps[&1] == :SQUIRTBOTTLE)
        ),
        swaps
      }
  end
end
