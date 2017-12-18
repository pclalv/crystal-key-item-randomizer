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
    # johto
    # we never check these four
    # NewBarkTown: true,
    # CherrygroveCity: true,
    # VioletCity: true,
    # AzaleaTown: true,
    GoldenrodCity: false,
    EcruteakCity: false,
    OlivineCity: false,
    CianwoodCity: false,
    MahoganyTown: false,
    BlackthornCity: false,
    RadioTower5F: false,
    UndergroundWarehouse: false,
    LakeOfRage: false,
    # kanto
    PewterCity: false,
    ViridianCity: false,
    CeruleanCity: false,
    VermilionCity: false,
    PokemonFanClub: false,
    CeladonCity: false,
    SaffronCity: false,
    CopycatsHouse2F: false,
    LavenderTown: false,
    FuchsiaCity: false,
    CinnabarIsland: false,
    PowerPlant: false,
  }
  def all_gyms, do: @all_gyms
  def accessible_gyms, do: @accessible_gyms
  def initial_gyms_reached, do: for gym <- all_gyms, do: {gym, Enum.member?(accessible_gyms, gym)}, into: %{}
  def all_items, do: CrystalKeyItemRandomizer.key_items |> Map.keys
  def initial_badge_count, do: 2

  reductions do
    # in-progress state:
    # {
    #   items_obtained,
    #   locations_reached,
    #   gyms_reached,
    #   badge_count,
    #   misc,
    #   swaps
    # }

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
      misc,
      swaps
    } ->
      {:done, swaps}

    # JOHTO

    { :begin, swaps } ->
      {
        (for item <- all_items, do: {swaps[item], Enum.member?(CrystalKeyItemRandomizer.pre_tree_items, item)}, into: %{}),
        initial_locations_reached,
        initial_gyms_reached,
        initial_badge_count,
        %{PowerPlantFixed: false, RocketsDefeated: false, ArrivedInKanto: false},
        swaps
      }

    # HM_CUT + AzaleaGym allows you to progress to Goldenrod
    {
      %{HM_CUT: true} = items_obtained,
      %{GoldenrodCity: false} = locations_reached,
      %{AzaleaGym: true} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        %{locations_reached | GoldenrodCity: true},
        gyms_reached,
        badge_count,
        misc,
        swaps
      }

    # SQUIRTBOTTLE is another way of getting to Goldenrod
    {
      %{SQUIRTBOTTLE: true} = items_obtained,
      %{GoldenrodCity: false} = locations_reached,
      gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        %{locations_reached | GoldenrodCity: true},
        gyms_reached,
        badge_count,
        misc,
        swaps
      }

    # after you reach Goldenrod, you beat the gym and get some items
    {
      items_obtained,
      %{GoldenrodCity: true} = locations_reached,
      %{GoldenrodGym: false} = gyms_reached,
      badge_count,
      misc,
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
        misc,
        swaps
      }

    # PASS + Goldenrod -> Saffron
    {
      %{PASS: true} = items_obtained,
      %{GoldenrodCity: true, SaffronCity: false} = locations_reached,
      gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        %{locations_reached | SaffronCity: true},
        gyms_reached,
        badge_count,
        misc,
        swaps
      }

    # SQUIRTBOTTLE allows you to progress to Ecruteak, Olivine and Mahogany
    {
      %{SQUIRTBOTTLE: true} = items_obtained,
      %{EcruteakCity: false, OlivineCity: false, MahoganyTown: false} = locations_reached,
      gyms_reached,
      badge_count,
      misc,
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
        misc,
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
      misc,
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
        misc,
        swaps
      }

    # S_S_TICKET can take you from Olivine to Vermilion
    {
      %{S_S_TICKET: true} = items_obtained,
      %{OlivineCity: true, VermilionCity: false} = locations_reached,
      gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        %{locations_reached | VermilionCity: true},
        gyms_reached,
        badge_count,
        misc,
        swaps
      }

    # HM_SURF and reaching Ecruteak Gym allows you to progress to
    # Cianwood and its Gym.
    {
      %{HM_SURF: true} = items_obtained,
      %{CianwoodCity: false} = locations_reached,
      %{EcruteakGym: true, CianwoodGym: false} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        %{items_obtained | swaps[:HM_FLY] => true, swaps[:SECRETPOTION] => true},
        %{locations_reached | CianwoodCity: true},
        %{gyms_reached | CianwoodGym: true},
        badge_count + 1,
        misc,
        swaps
      }

    # SECRETPOTION and reaching Olivine allows you to beat Olivine Gym
    {
      %{SECRETPOTION: true} = items_obtained,
      %{OlivineCity: true} = locations_reached,
      %{OlivineGym: false} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        locations_reached,
        %{gyms_reached | OlivineGym: true},
        badge_count + 1,
        misc,
        swaps
      }

    # reach LakeOfRage, clear it and the Rocket Hideout, beat Pryce
    {
      %{HM_SURF: true} = items_obtained,
      %{LakeOfRage: false, MahoganyTown: true} = locations_reached,
      %{EcruteakGym: true, MahoganyGym: false} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        %{items_obtained | swaps[:RED_SCALE] => true, swaps[:HM_WHIRLPOOL] => true},
        %{locations_reached | LakeOfRage: true},
        %{gyms_reached | MahoganyGym: true},
        badge_count + 1,
        misc,
        swaps
      }

    {
      items_obtained,
      %{RadioTower5F: false, UndergroundWarehouse: false} = locations_reached,
      gyms_reached,
      7 = badge_count,
      misc,
      swaps
    } ->
      {
        %{items_obtained | swaps[:BASEMENT_KEY] => true},
        %{locations_reached | RadioTower5F: true},
        gyms_reached,
        badge_count,
        misc,
        swaps
      }

    {
      %{BASEMENT_KEY: true} = items_obtained,
      %{UndergroundWarehouse: false} = locations_reached,
      gyms_reached,
      7 = badge_count,
      misc,
      swaps
    } ->
      {
        %{items_obtained | swaps[:CARD_KEY] => true},
        %{locations_reached | UndergroundWarehouse: true},
        gyms_reached,
        badge_count,
        misc,
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
      %{RocketsDefeated: false} = misc,
      swaps
    } ->
      {
        %{items_obtained | swaps[:CLEAR_BELL] => true},
        locations_reached,
        gyms_reached,
        badge_count,
        %{misc | RocketsDefeated: true},
        swaps
      }

    # TODO: is defeating the Rockets a prereq to getting HM_WATERFALL?
    {
      %{HM_STRENGTH: true} = items_obtained,
      %{BlackthornCity: false} = locations_reached,
      # can make this more readable, i.e.
      # %{GoldenrodRocketsCleared: true, CanUseStrength: true} = conditions
      %{GoldenrodGym: true} = gyms_reached,
      badge_count,
      %{RocketsDefeated: true} = misc,
      swaps
    } ->
      {
        %{items_obtained | swaps[:HM_WATERFALL] => true},
        %{locations_reached | BlackthornCity: true},
        gyms_reached,
        badge_count,
        misc,
        swaps
      }

    {
      %{HM_SURF: true} = items_obtained,
      %{BlackthornCity: true} = locations_reached,
      %{EcruteakGym: true, BlackthornGym: false} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        locations_reached,
        %{gyms_reached | BlackthornGym: true},
        badge_count + 1,
        misc,
        swaps
      }

    # # KANTO

    # Reach Vermilion, and reach all of the neighboring cities
    {
      items_obtained,
      %{VermilionCity: true} = locations_reached,
      gyms_reached,
      badge_count,
      %{ArrivedInKanto: false} = misc,
      swaps
    } ->
      {
        items_obtained,
        %{
          locations_reached |
          CeruleanCity: true,
          LavenderTown: true,
          CeladonCity: true,
          SaffronCity: true,
          FuchsiaCity: true,
        },
        gyms_reached,
        badge_count,
        %{misc | ArrivedInKanto: true},
        swaps
      }

    # Reach Vermilion, reach gym with HM_CUT, get the badge
    {
      %{HM_CUT: true} = items_obtained,
      %{VermilionCity: true} = locations_reached,
      %{VermilionGym: false, AzaleaGym: true} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        locations_reached,
        %{gyms_reached | VermilionGym: true},
        badge_count + 1,
        misc,
        swaps
      }

    # Reach Vermilion, reach gym with HM_SURF, get the badge
    {
      %{HM_SURF: true} = items_obtained,
      %{VermilionCity: true} = locations_reached,
      %{VermilionGym: false, EcruteakGym: true} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        locations_reached,
        %{gyms_reached | VermilionGym: true},
        badge_count + 1,
        misc,
        swaps
      }

    # Reach Saffron, reach all of the neighboring cities
    {
      items_obtained,
      %{SaffronCity: true} = locations_reached,
      gyms_reached,
      badge_count,
      %{ArrivedInKanto: false} = misc,
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
          FuchsiaCity: true,
        },
        gyms_reached,
        badge_count,
        %{misc | ArrivedInKanto: true},
        swaps
      }

    # Reach Saffron, get the badge
    {
      items_obtained,
      %{SaffronCity: true} = locations_reached,
      %{SaffronGym: false} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        items_obtained,
        locations_reached,
        %{gyms_reached | SaffronGym: true},
        badge_count + 1,
        misc,
        swaps
      }

    # Reach Fuchsia, get the badge and acquire the SUPER_ROD
    {
      items_obtained,
      %{FuchsiaCity: true} = locations_reached,
      %{FuchsiaGym: false} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        %{items_obtained | swaps[:SUPER_ROD] => true},
        locations_reached,
        %{gyms_reached | FuchsiaGym: true},
        badge_count,
        misc,
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
      misc,
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
        misc,
        swaps,
      }

    # start MACHINE_PART sidequest
    {
      %{HM_SURF: true} = items_obtained,
      %{PowerPlant: false} = locations_reached,
      %{EcruteakGym: true} = gyms_reached,
      badge_count,
      misc,
      swaps
    } ->
      {
        %{items_obtained | swaps[:MACHINE_PART] => true},
        %{locations_reached | PowerPlant: true},
        gyms_reached,
        badge_count,
        misc,
        swaps
      }

    {
      %{HM_SURF: true, MACHINE_PART: true} = items_obtained,
      %{PowerPlant: true} = locations_reached,
      %{EcruteakGym: true} = gyms_reached,
      badge_count,
      %{PowerPlantFixed: false} = misc,
      swaps
    } ->
      {
        items_obtained,
        locations_reached,
        gyms_reached,
        badge_count,
        %{misc | PowerPlantFixed: true},
        swaps
      }

    {
      items_obtained,
      %{PokemonFanClub: false} = locations_reached,
      gyms_reached,
      badge_count,
      %{PowerPlantFixed: true} = misc,
      swaps
    } ->
     {
       %{items_obtained | swaps[:LOST_ITEM] => true},
       %{locations_reached | PokemonFanClub: true},
       gyms_reached,
       badge_count,
       misc,
       swaps
     }

    {
      %{LOST_ITEM: true} = items_obtained,
      %{PokemonFanClub: true, CopycatsHouse2F: false} = locations_reached,
      gyms_reached,
      badge_count,
      %{PowerPlantFixed: true} = misc,
      swaps,
    } ->
      {
        %{items_obtained | swaps[:PASS] => true},
        %{locations_reached | CopycatsHouse2F: true},
        gyms_reached,
        badge_count,
        misc,
        swaps
      }

    {
      items_obtained,
      %{PewterCity: false} = locations_reached,
      gyms_reached,
      badge_count,
      %{PowerPlantFixed: true} = misc,
      swaps,
    } ->
      {
        %{items_obtained | swaps[:SILVER_WING] => true},
        %{locations_reached | PewterCity: true},
        %{gyms_reached | PewterGym: true},
        badge_count + 1,
        misc,
        swaps,
      }

    {
      items_obtained,
      %{CinnabarIsland: false, PewterCity: true} = locations_reached,
      gyms_reached,
      badge_count,
      %{PowerPlantFixed: true} = misc,
      swaps,
    } ->
      {
        items_obtained,
        %{locations_reached | CinnabarIsland: true},
        %{gyms_reached | CinnabarGym: true},
        badge_count + 1,
        misc,
        swaps,
      }

    # there's no way that ViridianGym is useful in this randomizer.
    # not even sure what the condition for this event is honestly
    {
      items_obtained,
      %{ViridianCity: false, CinnabarIsland: true} = locations_reached,
      gyms_reached,
      badge_count,
      %{PowerPlantFixed: true} = misc,
      swaps,
    } ->
      {
        items_obtained,
        %{locations_reached | ViridianCity: true},
        %{gyms_reached | ViridianGym: true},
        badge_count + 1,
        misc,
        swaps,
      }
  end
end
