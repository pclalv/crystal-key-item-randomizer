defmodule CrystalKeyItemRandomizer do
  @moduledoc """
  Randomizes the key items of Pokemon Crystal at the assembly level.
  """

  @required_items [
    :HM_SURF,
    :HM_STRENGTH,
    :HM_WHIRLPOOL,
    :HM_WATERFALL,
    :SECRETPOTION,
  ]

  # only one of HM_CUT/SQUIRTBOTTLE is required
  @maybe_required_items [
    :BASEMENT_KEY,
    :CARD_KEY,
    :HM_CUT,
    :LOST_ITEM,
    :PASS,
    :S_S_TICKET,
    :SQUIRTBOTTLE,
  ]

  @non_required_items [
    # HMs
    :HM_FLASH,
    :HM_FLY,

    # non-HMs
    :BICYCLE,
    :BLUE_CARD,
    :CLEAR_BELL,   # not sure - possibly required for progress?

    # useless
    :COIN_CASE,    # useless
    :GOOD_ROD,     # useless
    :ITEMFINDER,   # useless
    :MACHINE_PART, # useless; train will be available from the get go
    :MYSTERY_EGG,  # useless; blocking battle will be disabled
    :OLD_ROD,      # useless
    # :RAINBOW_WING, # useless; randomizing this is a bad idea...
    :RED_SCALE,    # useless
    :SILVER_WING,  # useless
    :SUPER_ROD,    # useless
  ]

  @kanto_items [
    :LOST_ITEM,
    :MACHINE_PART,
    :PASS,
    :SILVER_WING,
    :SUPER_ROD,
  ]

  @pre_sudowoodo_items [
    :HM_CUT,
    :HM_FLASH,
    # :BASEMENT_KEY, # pretty sure we can't get to this
    :BICYCLE,
    :BLUE_CARD,
    # :CARD_KEY,     # pretty sure we can't get to this
    # :CLEAR_BELL,   # pretty sure we can't get to this
    :COIN_CASE,
    :MYSTERY_EGG,
    :OLD_ROD,
    :SQUIRTBOTTLE,
  ]

  @pre_tree_items [
    :HM_CUT,
    :HM_FLASH,
    :MYSTERY_EGG,
    :OLD_ROD,
  ]

  @surf_blocked_items [
    :RED_SCALE,
    :HM_FLY,
    :SECRETPOTION,
    :HM_WATERFALL, # surf blocked unless we opt to remove the guy
                   # blocking mahogany town/route 44 junction
  ]

  def required_items, do: @required_items
  def maybe_required_items, do: @maybe_required_items
  def non_required_items, do: @non_required_items
  def kanto_items, do: @kanto_items
  def pre_sudowoodo_items, do: @pre_sudowoodo_items
  def pre_tree_items, do: @pre_tree_items
  def surf_blocked_items, do: @surf_blocked_items

  def key_items, do: @required_items ++ @maybe_required_items ++ @non_required_items

  @doc """
  Run.

  ## Examples

      iex> CrystalKeyItemRandomizer.run
  """
  def run do
    copy_item_constants_file()
    generate_key_item_swaps() |> ensure_reachable()
  end

  def ensure_reachable(swaps) do
    reachability = CrystalKeyItemRandomizer.Reachability.analyze(swaps)

    swaps
    |> CrystalKeyItemRandomizer.LockFixes.fix_ss_lock(reachability)
    |> CrystalKeyItemRandomizer.LockFixes.fix_kanto_lock(reachability)
    |> CrystalKeyItemRandomizer.LockFixes.fix_sudowoodo_lock(reachability)
    |> CrystalKeyItemRandomizer.LockFixes.fix_surf_lock(reachability)
    |> CrystalKeyItemRandomizer.LockFixes.fix_tree_lock(reachability)
    |> ensure_reachable(reachability)
  end

  defp ensure_reachable(swaps, reachability) do
    if Map.values(reachability) |> ! Enum.any? do
      swaps
    else
      ensure_reachable(swaps)
    end
  end

  defp generate_key_item_swaps do
    Enum.shuffle(CrystalKeyItemRandomizer.key_items)
    |> Enum.zip(CrystalKeyItemRandomizer.key_items)
    |> Enum.into(%{})
  end

  defp copy_item_constants_file do
    File.copy!(
      './pokecrystal/constants/item_constants.asm',
      './pokecrystal/constants/item_constants.asm.tmp'
    )
  end
end
