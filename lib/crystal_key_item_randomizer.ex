defmodule CrystalKeyItemRandomizer do
  @moduledoc """
  Randomizes the key items of Pokemon Crystal at the assembly level.
  """

  @required_items [
    %Item{
      name: :HM_SURF,
      location: :DanceTheatre,
      macro: :verbosegiveitem,
    },
    %Item{
      name: :HM_STRENGTH,
      location: :OlivineCafe,
      macro: :verbosegiveitem
    },
    %Item{
      name: :HM_WHIRLPOOL,
      location: :TeamRockBaseB2F,
      macro: :verbosegiveitem,
    },
    %Item{
      name: :HM_WATERFALL,
      location: :IcePath1F,
      macro: :itemball,
    },
    %Item{
      name: :SECRETPOTION,
      location: :CianwoodPharmacy,
      macro: :giveitem,
    },
  ]

  # only one of HM_CUT/SQUIRTBOTTLE is required
  @maybe_required_items [
    %Item{
      name: :BASEMENT_KEY,
      location: :RadioTower5F,
      macro: :verbosegiveitem,
    },
    %Item{
      name: :CARD_KEY,
      location: :UndergroundWarehouse,
      macro: :verbosegiveitem,
    },
    %Item{
      name: :HM_CUT,
      location: :IlexForest,
      macro: :verbosegiveitem,
    },
    %Item{
      name: :LOST_ITEM,
      location: :PokemonFanClub,
      macro: :giveitem,
    },
    %Item{
      name: :PASS,
      location: :CopycatsHouse2F,
      macro: :verbosegiveitem,
    },
    %Item{
      name: :S_S_TICKET,
      location: :ElmsLab,
      macro: :verbosegiveitem,
    },
    %Item{
      name: :SQUIRTBOTTLE,
      location: :GoldenrodFlowerShop,
      macro: :verbosegiveitem,
    },
  ]

  @non_required_item_locations [
    # HMs
    %Item{
      name: :HM_FLASH,
      location: :SproutTower3F,
      macro: :verbosegiveitem,
    },
    %Item{
      name: :HM_FLY,
      location: :CianwoodCity,
      macro: :verbosegiveitem,
    },

    # non-HMs
    %Item{
      name: :BICYCLE,
      location: :GoldenrodBikeShop,
      macro: :giveitem,
    },
    %Item{
      name: :CLEAR_BELL,
      location: :RadioTower5F,
      macro: :verbosegiveitem,
    },   # not sure - possibly required for progress?

    # useless
    %Item{
      name: :BLUE_CARD,
      location: :RadioTower2F,
      macro: :verbosegiveitem,
    },    # useless
    %Item{
      name: :COIN_CASE,
      location: :WarehouseEntrance,
      macro: :itemball,
    },    # useless
    %Item{
      name: :GOOD_ROD,
      location: :OlivineGoodRodHouse,
      macro: :verbosegiveitem,
    },     # useless
    %Item{
      name: :ITEMFINDER,
      location: :EcruteakItemfinderHouse,
      macro: :verbosegiveitem,
    },   # useless
    %Item{
      name: :MACHINE_PART,
      location: :foo,
      macro: :"dwb EVENT_FOUND_MACHINE_PART_IN_CERULEAN_GYM,", 
    }, # useless; train will be available from the get go
    %Item{
      name: :MYSTERY_EGG,
      location: :MrPokemonsHouse,
      macro: :giveitem,
    },  # useless; blocking battle will be disabled
    %Item{
      name: :OLD_ROD,
      location: :Route32PokeCenter1F,
      macro: :verbosegiveitem,
    },      # useless
    # %Item{
    #   name: :RAINBOW_WING,
    #   location: :TinTower1F,
    #   macro: :verbosegiveitem,
    # }, # useless; randomizing this is a bad idea because of how much
    #    # of a pain it is to catch all three beasts
    %Item{
      name: :RED_SCALE,
      location: :LakeofRage,
      macro: :giveitem,
    },    # useless
    %Item{
      name: :SILVER_WING,
      location: :PewterCity,
      macro: :verbosegiveitem,
    },  # useless
    %Item{
      name: :SUPER_ROD,
      location: :Route12SuperRodHouse,
      macro: :verbosegiveitem,
    },    # useless
  ]

  @kanto_item_locations [
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
  def key_items, do: required_items() ++ maybe_required_items() ++ non_required_items()
  def key_item_names, do: key_items |> Enum.into([], &(&1[:name]))

  def kanto_items, do: @kanto_items
  def pre_sudowoodo_items, do: @pre_sudowoodo_items
  def pre_tree_items, do: @pre_tree_items
  def surf_blocked_items, do: @surf_blocked_items
  def sudowoodo_blocked_items, do: key_item_names -- pre_sudowoodo_items
  def item_attributes, do: @item_attributes
  def ugly_pretty_names, do: @ugly_pretty_names

  @item_attributes_path "./pokecrystal/items/item_attributes.asm"

  def item_attributes_path, do: @item_attributes_path

  @doc """
  Run the randomization.

  ## Examples

      iex> CrystalKeyItemRandomizer.run
  """
  def run do
    CrystalKeyItemRandomizer.key_items
    |> Enum.shuffle
    |> Enum.zip(CrystalKeyItemRandomizer.key_items)
    |> Enum.into(%{})
    |> ensure_reachable
    |> Enum.into(%{}, fn {k, v} -> {k, to_string(v)} end)
    |> IO.inspect
    |> update_maps
  end

  def update_maps(swaps) do
    swaps
    |> write_out_item_constants
  end

  def write_out_item_constants(swaps) do
    new_item_constants = File.read!(item_constants_path)
    # hack because i don't know how negative regex matching would work
    |> String.replace("BASEMENT_KEY", "BASEMENT_KEY;original", global: false)
    |> String.replace("BICYCLE", "BICYCLE;original", global: false)
    |> String.replace("BLUE_CARD", "BLUE_CARD;original", global: false)
    |> String.replace("CARD_KEY", "CARD_KEY;original", global: false)
    |> String.replace("CLEAR_BELL", "CLEAR_BELL;original", global: false)
    |> String.replace("COIN_CASE", "COIN_CASE;original", global: false)
    |> String.replace("GOOD_ROD", "GOOD_ROD;original", global: false)
    |> String.replace("add_hm CUT", "add_hm CUT;original", global: false)
    |> String.replace("add_hm FLASH", "add_hm FLASH;original", global: false)
    |> String.replace("add_hm FLY", "add_hm FLY;original", global: false)
    |> String.replace("add_hm STRENGTH", "add_hm STRENGTH;original", global: false)
    |> String.replace("add_hm SURF", "add_hm SURF;original", global: false)
    |> String.replace("add_hm WATERFALL", "add_hm WATERFALL;original", global: false)
    |> String.replace("add_hm WHIRLPOOL", "add_hm WHIRLPOOL;original", global: false)
    |> String.replace("ITEMFINDER", "ITEMFINDER;original", global: false)
    |> String.replace("LOST_ITEM", "LOST_ITEM;original", global: false)
    |> String.replace("MACHINE_PART", "MACHINE_PART;original", global: false)
    |> String.replace("MYSTERY_EGG", "MYSTERY_EGG;original", global: false)
    |> String.replace("OLD_ROD", "OLD_ROD;original", global: false)
    |> String.replace("PASS", "PASS;original", global: false)
    |> String.replace("RED_SCALE", "RED_SCALE;original", global: false)
    |> String.replace("SECRETPOTION", "SECRETPOTION;original", global: false)
    |> String.replace("SILVER_WING", "SILVER_WING;original", global: false)
    |> String.replace("SQUIRTBOTTLE", "SQUIRTBOTTLE;original", global: false)
    |> String.replace("SUPER_ROD", "SUPER_ROD;original", global: false)
    |> String.replace("S_S_TICKET", "S_S_TICKET;original", global: false)
    # now we can replace the original with the swapped
    |> String.replace(~r/\w+\sBASEMENT_KEY;original/, item_constant(swaps[:BASEMENT_KEY]), global: false)
    |> String.replace(~r/\w+\sBICYCLE;original/, item_constant(swaps[:BICYCLE]), global: false)
    |> String.replace(~r/\w+\sBLUE_CARD;original/, item_constant(swaps[:BLUE_CARD]), global: false)
    |> String.replace(~r/\w+\sCARD_KEY;original/, item_constant(swaps[:CARD_KEY]), global: false)
    |> String.replace(~r/\w+\sCLEAR_BELL;original/, item_constant(swaps[:CLEAR_BELL]), global: false)
    |> String.replace(~r/\w+\sCOIN_CASE;original/, item_constant(swaps[:COIN_CASE]), global: false)
    |> String.replace(~r/\w+\sGOOD_ROD;original/, item_constant(swaps[:GOOD_ROD]), global: false)
    |> String.replace(~r/\w+\sCUT;original/, item_constant(swaps[:HM_CUT]), global: false)
    |> String.replace(~r/\w+\sFLASH;original/, item_constant(swaps[:HM_FLASH]), global: false)
    |> String.replace(~r/\w+\sFLY;original/, item_constant(swaps[:HM_FLY]), global: false)
    |> String.replace(~r/\w+\sSTRENGTH;original/, item_constant(swaps[:HM_STRENGTH]), global: false)
    |> String.replace(~r/\w+\sSURF;original/, item_constant(swaps[:HM_SURF]), global: false)
    |> String.replace(~r/\w+\sWATERFALL;original/, item_constant(swaps[:HM_WATERFALL]), global: false)
    |> String.replace(~r/\w+\sWHIRLPOOL;original/, item_constant(swaps[:HM_WHIRLPOOL]), global: false)
    |> String.replace(~r/\w+\sITEMFINDER;original/, item_constant(swaps[:ITEMFINDER]), global: false)
    |> String.replace(~r/\w+\sLOST_ITEM;original/, item_constant(swaps[:LOST_ITEM]), global: false)
    |> String.replace(~r/\w+\sMACHINE_PART;original/, item_constant(swaps[:MACHINE_PART]), global: false)
    |> String.replace(~r/\w+\sMYSTERY_EGG;original/, item_constant(swaps[:MYSTERY_EGG]), global: false)
    |> String.replace(~r/\w+\sOLD_ROD;original/, item_constant(swaps[:OLD_ROD]), global: false)
    |> String.replace(~r/\w+\sPASS;original/, item_constant(swaps[:PASS]), global: false)
    |> String.replace(~r/\w+\sRED_SCALE;original/, item_constant(swaps[:RED_SCALE]), global: false)
    |> String.replace(~r/\w+\sSECRETPOTION;original/, item_constant(swaps[:SECRETPOTION]), global: false)
    |> String.replace(~r/\w+\sSILVER_WING;original/, item_constant(swaps[:SILVER_WING]), global: false)
    |> String.replace(~r/\w+\sSQUIRTBOTTLE;original/, item_constant(swaps[:SQUIRTBOTTLE]), global: false)
    |> String.replace(~r/\w+\sSUPER_ROD;original/, item_constant(swaps[:SUPER_ROD]), global: false)
    |> String.replace(~r/\w+\sS_S_TICKET;original/, item_constant(swaps[:S_S_TICKET]), global: false)

    File.write!(item_constants_path, new_item_constants)

    swaps
  end

  def ensure_reachable(swaps) do
    reachability = CrystalKeyItemRandomizer.Reachability.analyze(swaps)
    IO.puts("in ensure_reachable")
    IO.inspect(reachability)

    swaps
    |> CrystalKeyItemRandomizer.LockFixes.fix_ss_lock(reachability)
    |> CrystalKeyItemRandomizer.LockFixes.fix_kanto_lock(reachability)
    |> CrystalKeyItemRandomizer.LockFixes.fix_surf_lock(reachability)
    |> CrystalKeyItemRandomizer.LockFixes.fix_sudowoodo_lock(reachability)
    |> CrystalKeyItemRandomizer.LockFixes.fix_tree_lock(reachability)
    |> ensure_reachable(reachability)
  end

  defp ensure_reachable(swaps, reachability) do
    if reachability |> Map.from_struct |> Map.values |> Enum.any? do
      ensure_reachable(swaps)
    else
      swaps
    end
  end

  defp item_constant(item) do
    if item |> String.starts_with?("HM_") do
      "add_hm #{item |> String.replace_prefix("HM_", "")}"
    else
      "const #{item}"
    end
  end
end
