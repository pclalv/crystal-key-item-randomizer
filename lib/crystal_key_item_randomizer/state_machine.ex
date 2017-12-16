defmodule CrystalKeyItemRandomizer.Validation do
  use Diet.Transformations

  # state:
  # {
  #   :in_progress,
  #   items_reached     %{},
  #   locations_reached %{},
  #   gyms_reached      %{},
  #   swaps             %{}
  # }
  alias CrystalKeyItemRandomizer.Item

  @all_locations CrystalKeyItemRandomizer.key_items

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

  def all_locations, do: @all_locations
  def accessible_locations, do: @accessible_locations
  def initial_locations_reached, do: for location <- all_locations, do: {location, Enum.member?(accessible_locations, location)}, into: %{}
  def all_gyms, do: @all_gyms
  def accessible_gyms, do: @accessible_gyms
  def initial_gyms_reached, do: for gym <- all_gyms, do: {gym, Enum.member?(accessible_gyms, gym)}, into: %{}
  def all_items, do: CrystalKeyItemRandomizer.key_items |> Map.keys

  reductions do
    { :in_progress, {[], result} } ->
            {:done, result |> Enum.reverse}

    { :in_progress, {[ {a, n}, a | tail], result }} ->
            {:in_progress, {[{a, n+1} | tail], result}}

    { :in_progress, {[ a, a | tail ], result } } ->
            {:in_progress, {[{a, 2} | tail], result }}

    { :in_progress, {[a | tail ], result } } ->
      {:in_progress, {tail, [a | result]  }}







    {
      :in_progress,
      [:HM_WATERFALL | _] = items_obtained,
      locations_reached,
      [{:BlackthornGym, true}, {_, true}, {_, true}, {_, true}, {_, true}, {_, true}, {_, true}, {_, true} | _] = gyms_reached,
      swaps
    } ->
      {:done, swaps}











    {
      :in_progress,
      [{:HM_STRENGTH, true} | _] = items_obtained,
      [{:RadioTower5F, true}, {:UndergroundWarehouse, true}, {:BlackthornCity, false} | _] = locations_reached
      [{:BlackthornGym, false}, {:GoldenrodGym, true} | _] = gyms_reached,
      swaps
    } ->
      {
        :in_progress,
        items_obtained,
          
      }

    {
      :in_progress,
      items_obtained,
      [{:RadioTower5F, false}, {:UndergroundWarehouse, false} | _] = locations_reached,
      [{_, true}, {_, true}, {_, true}, {_, true}, {_, true}, {_, true}, {_, true} | _] = gyms_reached,
      swaps
    } ->
      {
        :in_progress,
        [
          {swaps[:CLEAR_BELL], true},
          {swaps[:CARD_KEY], true},
          {swaps[:BASEMENT_KEY], true}
          | items_obtained
        ],
        [{:RadioTower5F, true}, {:UndergroundWarehouse, true} | locations_reached],
        gyms_reached,
        swaps
      }

    {
      :in_progress,
      [{:HM_SURF, true} | _] = items_obtained,
      [{:LakeOfRage, false} | _] = locations_reached,
      [{:EcruteakGym, true}, {:MahoganyGym, false} | _] = gyms_reached,
      swaps
    } ->
      {
        :in_progress,
        [{swaps[:RED_SCALE], true}, {swaps[:HM_WHIRLPOOL], true} | items_obtained],
        [{:LakeOfRage, true}, | locations_reached],
        [{:MahoganyGym, true} | gyms_reached],
        swaps
      }

    # HM_SURF and reaching Ecruteak Gym allows you to progress to Cianwood and its Gym
    {
      :in_progress,
      [{:HM_SURF, true} | _] = items_obtained,
      [{:CianwoodCity, false} | _] = locations_reached,
      [{:EcruteakGym, true}, {:CianwoodGym, false} | _] = gyms_reached,
      swaps
    } ->
      {
        :in_progress,
        [{swaps[:HM_FLY], true}, {swaps[:SECRETPOTION], true} | items_obtained],
        [{:CianwoodCity, true} | locations_reached],
        [{:CianwoodGym, true} | gyms_reached],
        swaps
      }

    # SECRETPOTION and reaching Olivine allows you to beat Olivine Gym
    {
      :in_progress,
      [{:SECRETPOTION, true} | _] = items_obtained,
      [{:OlivineCity, true} | _] = locations_reached,
      [{:OlivineGym, false} | _] = gyms_reached,
      swaps
    } ->
      {
        :in_progress,
        items_obtained,
        locations_reached,
        [{:OlivineGym, true} | gyms_reached],
        swaps
      }

    # SQUIRTBOTTLE allows you to progress to Ecruteak, Olivine and Mahogany
    {
      :in_progress,
      [{:SQUIRTBOTTLE, true} | _] = items_obtained,
      [{:EcruteakCity, false}, {:OlivineCity, false}, {:MahoganyTown, false} | _] = locations_reached,
      [{:EcruteakGym, false}, | _] = gyms_reached,
      swaps
    } ->
      {
        :in_progress,
        [
          {swaps[:HM_SURF], true},
          {swaps[:ITEMFINDER], true},
          {swaps[:HM_STRENGTH], true},
          {swaps[:GOOD_ROD], true},
          | items_obtained
        ],
        [{:EcruteakCity, true}, {:OlivineCity, true}, {:MahoganyTown, true} | locations_reached],
        [{:EcruteakGym, true} | gyms_reached],
        swaps
      }

    # HM_CUT allows you to progress to Goldenrod and obtain some key items
    {
      :in_progress,
      [{:HM_CUT, true} | _] = items_obtained,
      [{:GoldenrodCity, false} | _] = locations_reached,
      [{:AzaleaGym, true}, {:GoldenrodGym, false} | _] = gyms_reached,
      swaps
    } ->
      {
        :in_progress,
        [
          {swaps[:SQUIRTBOTTLE], true},
          {swaps[:BICYCLE], true},
          {swaps[:COIN_CASE], true},
          {swaps[:BLUE_CARD], true},
          | items_obtained
        ],
        [{:GoldenrodCity, true} | locations_reached],
        [{:GoldenrodGym, true} | gyms_reached],
        swaps
      }

    { :begin, swaps } ->
      {
        :in_progress,
        for item <- all_items, do: {swaps[item], Enum.member?(CrystalKeyItemRandomizer.pre_tree_items, item)}, into: %{},
        initial_locations_reached,
        initial_gyms_reached,
        swaps
      }

  end
end
