defmodule CrystalKeyItemRandomizer do
  @moduledoc """
  Randomizes the key items of Pokemon Crystal at the assembly level.
  """

  @required_items %{
    HM_SURF: %CrystalKeyItemRandomizer.Item{
      name: :HM_SURF,
      location: :DanceTheatre,
      macro: :verbosegiveitem,
    },
    HM_STRENGTH: %CrystalKeyItemRandomizer.Item{
      name: :HM_STRENGTH,
      location: :OlivineCafe,
      macro: :verbosegiveitem
    },
    HM_WHIRLPOOL: %CrystalKeyItemRandomizer.Item{
      name: :HM_WHIRLPOOL,
      location: :TeamRocketBaseB2F,
      macro: :verbosegiveitem,
    },
    HM_WATERFALL: %CrystalKeyItemRandomizer.Item{
      name: :HM_WATERFALL,
      location: :IcePath1F,
      macro: :itemball,
    },
    SECRETPOTION: %CrystalKeyItemRandomizer.Item{
      name: :SECRETPOTION,
      location: :CianwoodPharmacy,
      macro: :giveitem,
    },
  }

  # only one of HM_CUT/SQUIRTBOTTLE is required
  @maybe_required_items %{
    BASEMENT_KEY: %CrystalKeyItemRandomizer.Item{
      name: :BASEMENT_KEY,
      location: :RadioTower5F,
      macro: :verbosegiveitem,
    },
    CARD_KEY: %CrystalKeyItemRandomizer.Item{
      name: :CARD_KEY,
      location: :UndergroundWarehouse,
      macro: :verbosegiveitem,
    },
    HM_CUT: %CrystalKeyItemRandomizer.Item{
      name: :HM_CUT,
      location: :IlexForest,
      macro: :verbosegiveitem,
    },
    LOST_ITEM: %CrystalKeyItemRandomizer.Item{
      name: :LOST_ITEM,
      location: :PokemonFanClub,
      macro: :giveitem,
    },
    PASS: %CrystalKeyItemRandomizer.Item{
      name: :PASS,
      location: :CopycatsHouse2F,
      macro: :verbosegiveitem,
    },
    S_S_TICKET: %CrystalKeyItemRandomizer.Item{
      name: :S_S_TICKET,
      location: :ElmsLab,
      macro: :verbosegiveitem,
    },
    SQUIRTBOTTLE: %CrystalKeyItemRandomizer.Item{
      name: :SQUIRTBOTTLE,
      location: :GoldenrodFlowerShop,
      macro: :verbosegiveitem,
    },
  }

  @non_required_items %{
    # HMs
    HM_FLASH: %CrystalKeyItemRandomizer.Item{
      name: :HM_FLASH,
      location: :SproutTower3F,
      macro: :verbosegiveitem,
    },
    HM_FLY: %CrystalKeyItemRandomizer.Item{
      name: :HM_FLY,
      location: :CianwoodCity,
      macro: :verbosegiveitem,
    },

    # non-HMs
    BICYCLE: %CrystalKeyItemRandomizer.Item{
      name: :BICYCLE,
      location: :GoldenrodBikeShop,
      macro: :giveitem,
    },
    CLEAR_BELL: %CrystalKeyItemRandomizer.Item{
      name: :CLEAR_BELL,
      location: :RadioTower5F,
      macro: :verbosegiveitem,
    },   # not sure - possibly required for progress?

    # useless
    BLUE_CARD: %CrystalKeyItemRandomizer.Item{
      name: :BLUE_CARD,
      location: :RadioTower2F,
      macro: :verbosegiveitem,
    },    # useless
    COIN_CASE: %CrystalKeyItemRandomizer.Item{
      name: :COIN_CASE,
      location: :WarehouseEntrance,
      macro: :itemball,
    },    # useless
    GOOD_ROD: %CrystalKeyItemRandomizer.Item{
      name: :GOOD_ROD,
      location: :OlivineGoodRodHouse,
      macro: :verbosegiveitem,
    },     # useless
    ITEMFINDER: %CrystalKeyItemRandomizer.Item{
      name: :ITEMFINDER,
      location: :EcruteakItemfinderHouse,
      macro: :verbosegiveitem,
    },   # useless
    MACHINE_PART: %CrystalKeyItemRandomizer.Item{
      name: :MACHINE_PART,
      location: :CeruleanGym,
      macro: :"dwb EVENT_FOUND_MACHINE_PART_IN_CERULEAN_GYM,",
    }, # useless; train will be available from the get go
    MYSTERY_EGG: %CrystalKeyItemRandomizer.Item{
      name: :MYSTERY_EGG,
      location: :MrPokemonsHouse,
      macro: :giveitem,
    },  # useless; blocking battle will be disabled
    OLD_ROD: %CrystalKeyItemRandomizer.Item{
      name: :OLD_ROD,
      location: :Route32PokeCenter1F,
      macro: :verbosegiveitem,
    },      # useless
    # RAINBOW_WING: %CrystalKeyItemRandomizer.Item{
    #   name: :RAINBOW_WING,
    #   location: :TinTower1F,
    #   macro: :verbosegiveitem,
    # }, # useless; randomizing this is a bad idea because of how much
    #    # of a pain it is to catch all three beasts
    RED_SCALE: %CrystalKeyItemRandomizer.Item{
      name: :RED_SCALE,
      location: :LakeofRage,
      macro: :giveitem,
    },    # useless
    SILVER_WING: %CrystalKeyItemRandomizer.Item{
      name: :SILVER_WING,
      location: :PewterCity,
      macro: :verbosegiveitem,
    },  # useless
    SUPER_ROD: %CrystalKeyItemRandomizer.Item{
      name: :SUPER_ROD,
      location: :Route12SuperRodHouse,
      macro: :verbosegiveitem,
    },    # useless
  }

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
  def key_items, do: required_items |> Map.merge(maybe_required_items) |> Map.merge(non_required_items)
  def key_item_names, do: key_items |> Map.keys

  def kanto_items, do: @kanto_items
  def pre_sudowoodo_items, do: @pre_sudowoodo_items
  def pre_tree_items, do: @pre_tree_items
  def surf_blocked_items, do: @surf_blocked_items
  def sudowoodo_blocked_items, do: key_item_names -- pre_sudowoodo_items
  def item_attributes, do: @item_attributes
  def ugly_pretty_names, do: @ugly_pretty_names

  @maps_dir "./pokecrystal/maps"

  def maps_dir, do: @maps_dir


  @doc """
  Run the randomization.

  ## Examples

      iex> CrystalKeyItemRandomizer.run
  """
  def run do
    CrystalKeyItemRandomizer.key_item_names
    |> Enum.shuffle
    |> Enum.zip(CrystalKeyItemRandomizer.key_item_names)
    |> Enum.into(%{})
    |> IO.inspect
    |> ensure_reachable
    |> IO.inspect
    |> update_maps
  end

  def update_maps(swaps) do
    swaps
    |> Enum.each(&swap(&1))
  end

  def swap({original, replacement}) do
    original_item = CrystalKeyItemRandomizer.key_items[original]
    map_path = "#{CrystalKeyItemRandomizer.maps_dir}/#{original_item.location}.asm"

    updated_map = File.read!(map_path)
    |> String.replace("#{original_item.macro} #{original}", "#{original_item.macro} #{replacement}")

    File.write!(map_path, updated_map)
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
