defmodule CrystalKeyItemRandomizer do
  @moduledoc """
  Randomizes the key items of Pokemon Crystal at the assembly level.
  """

  alias CrystalKeyItemRandomizer.Item

  @required_items %{
    HM_SURF: %Item{
      name: :HM_SURF,
      location: :DanceTheatre,
    },
    HM_STRENGTH: %Item{
      name: :HM_STRENGTH,
      location: :OlivineCafe,
    },
    HM_WHIRLPOOL: %Item{
      name: :HM_WHIRLPOOL,
      location: :TeamRocketBaseB2F,
    },
    HM_WATERFALL: %Item{
      name: :HM_WATERFALL,
      location: :IcePath1F,
      macro: :itemball,
    },
    SECRETPOTION: %Item{
      name: :SECRETPOTION,
      location: :CianwoodPharmacy,
    },
  }

  # only one of HM_CUT/SQUIRTBOTTLE is required
  @maybe_required_items %{
    BASEMENT_KEY: %Item{
      name: :BASEMENT_KEY,
      location: :RadioTower5F,
    },
    CARD_KEY: %Item{
      name: :CARD_KEY,
      location: :UndergroundWarehouse,
    },
    HM_CUT: %Item{
      name: :HM_CUT,
      location: :IlexForest,
    },
    LOST_ITEM: %Item{
      name: :LOST_ITEM,
      location: :PokemonFanClub,
    },
    PASS: %Item{
      name: :PASS,
      location: :CopycatsHouse2F,
    },
    S_S_TICKET: %Item{
      name: :S_S_TICKET,
      location: :ElmsLab,
    },
    SQUIRTBOTTLE: %Item{
      name: :SQUIRTBOTTLE,
      location: :GoldenrodFlowerShop,
    },
  }

  @maybe_required_pairs [
    # if (any of) the stuff on the left is in kanto, and the thing on
    # the right is required, then the swap is kanto-locked
    {:BASEMENT_KEY, :CARD_KEY},
    {:CARD_KEY, :CLEAR_BELL},
    # this is a bit of a special case. CARD_KEY and CLEAR_BERLL have
    # other prereqs, but those prereqs are themselves required
    # (e.g. HM_CUT or SQUIRTBOTTLE.)
    {[:LOST_ITEM, :MACHINE_PART], :PASS}
  ]

  @non_required_items %{
    # HMs
    HM_FLASH: %Item{
      name: :HM_FLASH,
      location: :SproutTower3F,
    },
    HM_FLY: %Item{
      name: :HM_FLY,
      location: :CianwoodCity,
    },

    # non-HMs
    BICYCLE: %Item{
      name: :BICYCLE,
      location: :GoldenrodBikeShop,
    },
    CLEAR_BELL: %Item{
      name: :CLEAR_BELL,
      location: :RadioTower5F,
    },   # not sure - possibly required for progress?

    # useless
    BLUE_CARD: %Item{
      name: :BLUE_CARD,
      location: :RadioTower2F,
    },    # useless
    COIN_CASE: %Item{
      name: :COIN_CASE,
      location: :WarehouseEntrance,
      macro: :itemball,
    },    # useless
    GOOD_ROD: %Item{
      name: :GOOD_ROD,
      location: :OlivineGoodRodHouse,
    },     # useless
    ITEMFINDER: %Item{
      name: :ITEMFINDER,
      location: :EcruteakItemfinderHouse,
    },   # useless
    MACHINE_PART: %Item{
      name: :MACHINE_PART,
      location: :CeruleanGym,
      macro: :"dwb EVENT_FOUND_MACHINE_PART_IN_CERULEAN_GYM,",
    }, # useless; train will be available from the get go
    MYSTERY_EGG: %Item{
      name: :MYSTERY_EGG,
      location: :MrPokemonsHouse,
    },  # useless; blocking battle will be disabled
    OLD_ROD: %Item{
      name: :OLD_ROD,
      location: :Route32PokeCenter1F,
    },      # useless
    # RAINBOW_WING: %Item{
    #   name: :RAINBOW_WING,
    #   location: :TinTower1F,
    # }, # useless; randomizing this is a bad idea because of how much
    #    # of a pain it is to catch all three beasts
    RED_SCALE: %Item{
      name: :RED_SCALE,
      location: :LakeofRage,
    },    # useless
    SILVER_WING: %Item{
      name: :SILVER_WING,
      location: :PewterCity,
    },  # useless
    SUPER_ROD: %Item{
      name: :SUPER_ROD,
      location: :Route12SuperRodHouse,
    },    # useless
  }

  @kanto_items [
    :LOST_ITEM,
    :MACHINE_PART,
    :PASS,
    :SILVER_WING,
    :SUPER_ROD,
  ]

  @pre_tree_items [
    :HM_CUT,
    :HM_FLASH,
    :MYSTERY_EGG,
    :OLD_ROD,
  ]

  # `CARD_KEY` is not included in pre_goldenrod_items given the
  # current state of the underlying assembly! this is because even if
  # you have the `BASEMENT_KEY`, the basement will be devoid of any
  # rockets

  # `CLEAR_BELL` is not included because i believe that the same goes
  # for the `CARD_KEY`/radio tower upper floors; even if it's not
  # true, i bet it'd make things more complicated, so i'm just
  # considering the `CLEAR_BELL` inaccessible for now.
  @pre_goldenrod_items [
    :BICYCLE,
    :BLUE_CARD,
    :COIN_CASE,
    :SQUIRTBOTTLE
    | @pre_tree_items
  ]

  @surf_blocked_items [
    :CARD_KEY,     # surf-blocked because the player can't
    :BASEMENT_KEY, # trigger the rocket radio tower takeover
    :CLEAR_BELL,   # without defeating the red gyarados

    :RED_SCALE,
    :HM_FLY,
    :SECRETPOTION,
    :HM_WATERFALL # surf blocked unless we opt to remove the guy
                  # blocking mahogany town/route 44 junction
    | (for item <- @kanto_items, item != :SUPER_ROD, do: item)
  ]

  def required_items, do: @required_items
  def required_item_names, do: @required_items |> Map.keys
  def maybe_required_items, do: @maybe_required_items
  def non_required_items, do: @non_required_items
  def key_items, do: required_items |> Map.merge(maybe_required_items) |> Map.merge(non_required_items)
  def key_item_names, do: key_items |> Map.keys

  def kanto_items, do: @kanto_items
  def pre_goldenrod_items, do: @pre_goldenrod_items
  def pre_tree_items, do: @pre_tree_items
  def surf_blocked_items, do: @surf_blocked_items
  def goldenrod_blocked_items, do: key_item_names -- pre_goldenrod_items
  def maybe_required_pairs do
    @maybe_required_pairs |> Enum.reduce([], fn
      {prereqs, maybe_required}, acc when is_list(prereqs) ->
        (prereqs |> Enum.map(fn (prereq) -> {prereq, maybe_required} end)) ++ acc
      {_, _} = pair, acc ->
        [pair | acc]
      end
    )
  end

  @maps_dir "./pokecrystal/maps"

  def maps_dir, do: @maps_dir

  def vanilla_swaps do
    CrystalKeyItemRandomizer.key_item_names \
    |> Enum.zip(CrystalKeyItemRandomizer.key_item_names) \
    |> Enum.into(%{})
  end

  # STUFF FOR TESTING
  @goldenrod_locked_swaps %{
    BASEMENT_KEY: :HM_CUT,
    BICYCLE: :CLEAR_BELL,
    BLUE_CARD: :MACHINE_PART,
    CARD_KEY: :HM_STRENGTH,
    CLEAR_BELL: :HM_FLY,
    COIN_CASE: :GOOD_ROD,
    GOOD_ROD: :ITEMFINDER,
    HM_CUT: :BASEMENT_KEY,
    HM_FLASH: :MYSTERY_EGG,
    HM_FLY: :HM_FLASH,
    HM_STRENGTH: :HM_WHIRLPOOL,
    HM_SURF: :LOST_ITEM,
    HM_WATERFALL: :SECRETPOTION,
    HM_WHIRLPOOL: :SUPER_ROD,
    ITEMFINDER: :BLUE_CARD,
    LOST_ITEM: :COIN_CASE,
    MACHINE_PART: :HM_SURF,
    MYSTERY_EGG: :S_S_TICKET,
    OLD_ROD: :CARD_KEY,
    PASS: :HM_WATERFALL,
    RED_SCALE: :OLD_ROD,
    SECRETPOTION: :SQUIRTBOTTLE,
    SILVER_WING: :SILVER_WING,
    SQUIRTBOTTLE: :PASS,
    SUPER_ROD: :RED_SCALE,
    S_S_TICKET: :BICYCLE
  }
  def goldenrod_locked_swaps, do: @goldenrod_locked_swaps
  # STUFF FOR TESTING

  @doc """
  Run the randomization.
  """
  def run do
    System.cmd("git", ["reset", "--hard", "HEAD"], cd: "./pokecrystal/")

    CrystalKeyItemRandomizer.key_item_names
    |> Enum.shuffle
    |> Enum.zip(CrystalKeyItemRandomizer.key_item_names)
    |> Enum.into(%{})
    |> IO.inspect
    |> ensure_reachable
    |> IO.inspect
    |> Enum.each(&apply_swap(&1)) # TODO: make this more idiomatic?

    System.cmd("make", [], cd: "./pokecrystal/")
  end

  def apply_swap({original, replacement}) do
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
    |> CrystalKeyItemRandomizer.LockFixes.fix_goldenrod_lock(reachability)
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
end
