defmodule CrystalKeyItemRandomizer do
  @moduledoc """
  Documentation for CrystalKeyItemRandomizer.
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

  def required_items, do: @required_items
  def maybe_required_items, do: @maybe_required_items
  def non_required_items, do: @non_required_items
  def kanto_items, do: @kanto_items
  def pre_sudowoodo_items, do: @pre_sudowoodo_items
  def pre_tree_items, do: @pre_tree_items
  def key_items, do: @required_items ++ @maybe_required_items ++ @non_required_items

  @doc """
  Run.

  ## Examples

      iex> CrystalKeyItemRandomizer.run
  """
  def run do
    
  end
end
