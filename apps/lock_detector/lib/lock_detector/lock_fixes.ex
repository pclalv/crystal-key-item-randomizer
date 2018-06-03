defmodule LockDetector.LockFixes do
  @doc """

  Fix ss-locked swaps by taking whatever required key item replaced
  the `S_S_TICKET` and switching it with some non-required item.

  """

  def fix_ss_lock(swaps, %{ss_locked?: locked}) when not locked, do: swaps

  def fix_ss_lock(swaps, _reachability) do
    old_original_item = :S_S_TICKET
    old_replacement_item = swaps[old_original_item]

    {new_replacement_item, _} = Enum.random(LockDetector.non_required_items())

    {new_original_item, _} =
      swaps
      |> Enum.find(fn {_key, val} -> val == new_replacement_item end)

    swaps
    |> Map.put(old_original_item, new_replacement_item)
    |> Map.put(new_original_item, old_replacement_item)
  end

  @doc """

  Fix kanto-locked swaps by putting either the `PASS` or the
  `S_S_TICKET` in Johto.

  """

  def fix_kanto_lock(swaps, %{kanto_locked?: locked}) when not locked, do: swaps

  def fix_kanto_lock(swaps, _reachability) do
    old_replacement_item = Enum.random([:PASS, :S_S_TICKET])

    {old_original_item, _} =
      swaps
      |> Enum.find(fn {_key, val} -> val == old_replacement_item end)

    # choose a random non-required item to stick in kanto
    new_replacement_item = Enum.random(LockDetector.non_required_items())

    {new_original_item, _} =
      swaps
      |> Enum.find(fn {_key, val} -> val == new_replacement_item end)

    swaps
    |> Map.put(old_original_item, new_replacement_item)
    |> Map.put(new_original_item, old_replacement_item)
  end

  @doc """

  Fix goldenrod-locked swaps by making the `SQUIRTBOTTLE` available.

  """

  # in the future, it would be nice to implement more complicated
  # fixes; say, if the `PASS` is available, then put the
  # `SQUIRTBOTTLE` in kanto
  def fix_goldenrod_lock(swaps, %{goldenrod_locked?: locked}) when not locked, do: swaps

  def fix_goldenrod_lock(swaps, _reachability) do
    old_replacement_item = :SQUIRTBOTTLE

    old_original_item =
      LockDetector.goldenrod_blocked_items()
      |> Enum.find(&(swaps[&1] == old_replacement_item))

    new_original_item =
      (LockDetector.key_item_names() -- LockDetector.goldenrod_blocked_items() -- [:S_S_TICKET])
      |> Enum.random()

    new_replacement_item = swaps[new_original_item]

    swaps
    |> Map.put(old_original_item, new_replacement_item)
    |> Map.put(new_original_item, old_replacement_item)
  end

  def fix_surf_lock(swaps, %{surf_locked?: locked}) when not locked, do: swaps

  def fix_surf_lock(swaps, _reachability) do
    old_replacement_item = :HM_SURF

    old_original_item =
      LockDetector.surf_blocked_items()
      |> Enum.find(&(swaps[&1] == old_replacement_item))

    new_original_item =
      (LockDetector.key_item_names() -- LockDetector.surf_blocked_items() -- [:S_S_TICKET])
      |> Enum.random()

    new_replacement_item = swaps[new_original_item]

    swaps
    |> Map.put(old_original_item, new_replacement_item)
    |> Map.put(new_original_item, old_replacement_item)
  end

  @doc """

  Fix tree-locked swaps by making either `HM_CUT` or `SQUIRTBOTTLE`
  available as a pre-tree item.

  """

  # in the future, it would be nice for this function to recognize
  # `CARD_KEY` and `CLEAR_BELL` as maybe pre-tree items.
  def fix_tree_lock(swaps, %{tree_locked?: locked}) when not locked, do: swaps

  def fix_tree_lock(swaps, _reachability) do
    old_original_item = Enum.random(LockDetector.pre_tree_items())
    old_replacement_item = swaps[old_original_item]
    new_replacement_item = Enum.random([:HM_CUT, :SQUIRTBOTTLE])

    {new_original_item, _} =
      swaps
      |> Enum.find(fn {_key, val} -> val == new_replacement_item end)

    swaps
    |> Map.put(old_original_item, new_replacement_item)
    |> Map.put(new_original_item, old_replacement_item)
  end
end
