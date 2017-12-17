defmodule CrystalKeyItemRandomizer.StateMachine do
  use Diet.Transformations

  # state:
  # {
  #   :in_progress,
  #   items_reached     %{},
  #   locations_reached %{},
  #   gyms_reached      %{},
  #   swaps             %{}
  # }
  #
  #
  # conditions ?
  # %{
  #   CanUseCut: true
  #   CanUseSurf: false,
  #   CanUseStrength: false
  #   CanUseWaterfall: false,
  #     ... etc
  # }
  alias CrystalKeyItemRandomizer.Item

  @accessible_locations [
    :ElmsLab, :IlexForest, :MrPokemonsHouse,
    :Route32PokeCenter1F, :SproutTower3F
  ]

  @all_gyms [
    :VioletGym, :AzaleaGym, :GoldenrodGym, :EcruteakGym, :OlivineGym, :CianwoodGym, :MahoganyGym, :BlackthornGym,
    :PewterGym, :CeruleanGym, :VermilionGym, :CeladonGym, :FuchsiaGym, :SaffronGym, :CinnabarGym, :ViridianGym
  ]

  @accessible_gyms [
    :VioletGym,
    :AzaleaGym
  ]

  def all_locations, do: for {_, %Item{location: location}} <- CrystalKeyItemRandomizer.key_items, do: location
  def accessible_locations, do: @accessible_locations
  #def initial_locations_reached, do: for location <- all_locations, do: {location, Enum.member?(accessible_locations, location)}, into: %{}
  def initial_locations_reached, do: %{
    NewBarkTown: true,
    CherrygroveCity: true,
    VioletCity: true,
    AzaleaTown: true,
    GoldenrodCity: false,
    EcruteakCity: false,
    OlivineCity: false,
    CianwoodCity: false,
    MahoganyTown: false,
    BlackthornCity: false,
    RadioTower5F: false,
    UndergroundWarehouse: false,
    LakeOfRage: false,
    PowerPlant: false,
  }
  def all_gyms, do: @all_gyms
  def accessible_gyms, do: @accessible_gyms
  def initial_gyms_reached, do: for gym <- all_gyms, do: {gym, Enum.member?(accessible_gyms, gym)}, into: %{}
  def all_items, do: CrystalKeyItemRandomizer.key_items |> Map.keys
  def initial_badge_count, do: 2

  reductions do
    # {
    #   items_obtained,
    #   locations_reached,
    #   gyms_reached,
    #   badge_count,
    #   swaps
    # } ->
    #   {
    #     items_obtained,
    #     locations_reached,
    #     gyms_reached,
    #     badge_count,
    #     swaps
    #   }

    # FINAL STATE
    {
      %{HM_WATERFALL: true} = items_obtained,
      locations_reached,
      %{
        GoldenrodGym: true,
        EcruteakGym: true,
        BlackthornGym: true,
      } = gyms_reached,
      8 = badge_count,
      swaps
    } ->
      {:done, swaps}

    # KANTO

    # how to continue this? perhaps with the `conditions` refactoring?
    #
    # {
    #   %{HM_SURF: true, MACHINE_PART: true} = items_obtained,
    #   locations_reached,
    #   %{EcruteakGym: true} = gyms_reached,
    #   badge_count,
    #   swaps
    # } ->
    #   {
    #     items_obtained,
    #     locations_reached,
    #     gyms_reached,
    #     badge_count,
    #     swaps
    #   }

    # start MACHINE_PART sidequest
    {
      %{HM_SURF: true} = items_obtained,
      %{PowerPlant: false} = locations_reached,
      %{EcruteakGym: true} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        %{items_obtained | swaps[:MACHINE_PART] => true},
        %{locations_reached | PowerPlant: true},
        gyms_reached,
        badge_count,
        swaps
      }

    # S_S_TICKET and reaching Vermilion also allows you to progress to
    # Ecruteak, Olivine and Mahogany
    {
      %{S_S_TICKET: true} = items_obtained,
      %{
        VermilionCity: true,
        EcruteakCity: false,
        OlivineCity: false,
        MahoganyTown: false
      } = locations_reached,
      gyms_reached,
      badge_count,
      swaps
    } ->
      {
        items_obtained,
        %{
          locations_reached |
          EcruteakCity: true,
          OlivineCity: true,
          MahoganyTown: true
        },
        gyms_reached,
        badge_count,
        swaps,
      }

    # Reach Saffron; get the badge and reach all of the neighboring cities
    {
      items_obtained,
      %{SaffronCity: true} = locations_reached,
      %{SaffronGym: false} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        items_obtained,
        %{
          locations_reached |
          CeruleanCity: true,
          VermilionCity: true,
          LavenderTown: true,
          CeladonCity: true,
        },
        %{gyms_reached | SaffronGym: true},
        badge_count + 1,
        swaps
      }

    # JOHTO

    {
      %{HM_SURF: true} = items_obtained,
      %{BlackthornCity: true} = locations_reached,
      %{EcruteakGym: true, BlackthornGym: false} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        items_obtained,
        locations_reached,
        %{gyms_reached | BlackthornGym: true},
        badge_count + 1,
        swaps
      }

    {
      %{HM_STRENGTH: true} = items_obtained,
      %{RadioTower5F: true, UndergroundWarehouse: true, BlackthornCity: false} = locations_reached,
      # if we roll `locations_reached` and `gyms_reached` together
      # into something like `conditions` then we can make this more
      # readable, i.e.
      # %{GoldenrodRocketsCleared: true, CanUseStrength: true} = conditions
      %{GoldenrodGym: true} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        %{items_obtained | swaps[:HM_WATERFALL] => true},
        %{locations_reached | BlackthornCity: true},
        gyms_reached,
        badge_count,
        swaps
      }

    # TODO: figure out if you MUST clear the UndergroundWarehouse
    # before using the CARD_KEY to conquer the RadioTower and obtain
    # the CLEAR_BELL.
    #
    # idea is that even after clearing the RadioTower maybe you can
    # still explore the UndergroundWarehouse and collect the
    # CARD_KEY. proabably not, though, as the director gives you the
    # CARD_KEY and will likely not be present in the
    # UndergroundWarehouse after beating the CARD_KEY-blocked areas of
    # the RadioTower. one workaround would be to turn the CARD_KEY
    # into an itemball.
    {
      %{CARD_KEY: true} = items_obtained,
      %{RadioTower5F: true, UndergroundWarehouse: true} = locations_reached,
      gyms_reached,
      7 = badge_count,
      swaps
    } ->
      {
        %{items_obtained | swaps[:CLEAR_BELL] => true},
        locations_reached,
        gyms_reached,
        badge_count,
        swaps
      }

    {
      %{BASEMENT_KEY: true} = items_obtained,
      %{UndergroundWarehouse: false} = locations_reached,
      gyms_reached,
      7 = badge_count,
      swaps
    } ->
      {
        %{items_obtained | swaps[:CARD_KEY] => true},
        %{locations_reached | UndergroundWarehouse: true},
        gyms_reached,
        badge_count,
        swaps
      }

    {
      items_obtained,
      %{RadioTower5F: false, UndergroundWarehouse: false} = locations_reached,
      gyms_reached,
      7 = badge_count,
      swaps
    } ->
      {
        %{items_obtained | swaps[:BASEMENT_KEY] => true},
        %{locations_reached | RadioTower5F: true},
        gyms_reached,
        badge_count,
        swaps
      }

    {
      %{HM_SURF: true} = items_obtained,
      %{LakeOfRage: false, MahoganyTown: true} = locations_reached,
      %{EcruteakGym: true, MahoganyGym: false} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        %{items_obtained | swaps[:RED_SCALE] => true, swaps[:HM_WHIRLPOOL] => true},
        %{locations_reached | LakeOfRage: true},
        %{gyms_reached | MahoganyGym: true},
        badge_count + 1,
        swaps
      }

    # HM_SURF and reaching Ecruteak Gym allows you to progress to
    # Cianwood and its Gym.
    {
      %{HM_SURF: true} = items_obtained,
      %{CianwoodCity: false} = locations_reached,
      %{EcruteakGym: true, CianwoodGym: false} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        %{items_obtained | swaps[:HM_FLY] => true, swaps[:SECRETPOTION] => true},
        %{locations_reached | CianwoodCity: true},
        %{gyms_reached | CianwoodGym: true},
        badge_count + 1,
        swaps
      }

    # SECRETPOTION and reaching Olivine allows you to beat Olivine Gym
    {
      %{SECRETPOTION: true} = items_obtained,
      %{OlivineCity: true} = locations_reached,
      %{OlivineGym: false} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        items_obtained,
        locations_reached,
        %{gyms_reached | OlivineGym: true},
        badge_count + 1,
        swaps
      }

    # after you reach Ecruteak, you beat the gym and get some items
    #
    # FIXME:
    # kinda weird that i consider the olivine items to be acquired in
    # ecruteak, but it simplifies the whole thing of keeping track of
    # locations_reached, which otherwise would need to include
    # ItemfinderHouse and OlivineCafe
    {
      items_obtained,
      %{EcruteakCity: true} = locations_reached,
      %{EcruteakGym: false} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        %{
          items_obtained |
          swaps[:HM_SURF] => true,
          swaps[:ITEMFINDER] => true,
          swaps[:HM_STRENGTH] => true,
          swaps[:GOOD_ROD] => true
        },
        locations_reached,
        %{gyms_reached | EcruteakGym: true},
        badge_count + 1,
        swaps
      }

    # SQUIRTBOTTLE allows you to progress to Ecruteak, Olivine and Mahogany
    {
      %{SQUIRTBOTTLE: true} = items_obtained,
      %{EcruteakCity: false, OlivineCity: false, MahoganyTown: false} = locations_reached,
      gyms_reached,
      badge_count,
      swaps
    } ->
      {
        items_obtained,
        %{
          locations_reached |
          EcruteakCity: true,
          OlivineCity: true,
          MahoganyTown: true
        },
        gyms_reached,
        badge_count,
        swaps
      }

    # PASS + Goldenrod -> Saffron
    {
      %{PASS: true} = items_obtained,
      %{GoldenrodCity: true, SaffronCity: false} = locations_reached,
      gyms_reached,
      badge_count,
      swaps
    } ->
      {
        items_obtained,
        %{locations_reached | SaffronCity: true},
        gyms_reached,
        badge_count,
        swaps
      }

    # after you reach Goldenrod, you beat the gym and get some items
    {
      items_obtained,
      %{GoldenrodCity: true} = locations_reached,
      %{GoldenrodGym: false} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        %{
          items_obtained |
          swaps[:SQUIRTBOTTLE] => true,
          swaps[:BICYCLE] => true,
          swaps[:COIN_CASE] => true,
          swaps[:BLUE_CARD] => true
        },
        locations_reached,
        %{gyms_reached | GoldenrodGym: true},
        badge_count + 1,
        swaps
      }

    # SQUIRTBOTTLE is another way of getting to Goldenrod
    {
      %{SQUIRTBOTTLE: true} = items_obtained,
      %{GoldenrodCity: false} = locations_reached,
      gyms_reached,
      badge_count,
      swaps
    } ->
      {
        items_obtained,
        %{locations_reached | GoldenrodCity: true},
        gyms_reached,
        badge_count,
        swaps
      }

    # HM_CUT + AzaleaGym allows you to progress to Goldenrod
    {
      %{HM_CUT: true} = items_obtained,
      %{GoldenrodCity: false} = locations_reached,
      %{AzaleaGym: true} = gyms_reached,
      badge_count,
      swaps
    } ->
      {
        items_obtained,
        %{locations_reached | GoldenrodCity: true},
        gyms_reached,
        badge_count,
        swaps
      }

    { :begin, swaps } ->
      {
        (for item <- all_items, do: {swaps[item], Enum.member?(CrystalKeyItemRandomizer.pre_tree_items, item)}, into: %{}),
        initial_locations_reached,
        initial_gyms_reached,
        initial_badge_count,
        swaps
      }

  end
end
