defmodule CrystalKeyItemRandomizer.Reachability do
  @locks [:ss_locked?, :kanto_locked?, :goldenrod_locked?, :surf_locked?, :tree_locked?]
  @enforce_keys @locks
  defstruct @locks

  def analyze(swaps) do
    %CrystalKeyItemRandomizer.Reachability{
      ss_locked?: ss_locked?(swaps),
      kanto_locked?: kanto_locked?(swaps),
      goldenrod_locked?: goldenrod_locked?(swaps),
      surf_locked?: surf_locked?(swaps),
      tree_locked?: tree_locked?(swaps),
    }
  end

  defp ss_locked?(swaps) do
    # the goal of the randomizer is to beat the elite 4; elm only gives
    # you the SS Ticket after you beat the elite 4.
    replacement = swaps[:S_S_TICKET]

    cond do
      required?(replacement) ->
        true
      replacement == :BASEMENT_KEY && required?(swaps[:CARD_KEY]) ->
        true
      replacement == :CARD_KEY && required?(swaps[:CLEAR_BELL]) ->
        true
      replacement == :LOST_ITEM && required?(swaps[:PASS]) ->
        true
      true ->
        false
    end
  end

  defp kanto_locked?(swaps) do
    if in_kanto?(swaps, :PASS) && in_kanto?(swaps, :S_S_TICKET) do
      cond do
        any_required_item_in_kanto?(swaps) ->
          true
        # the following three cases check maybe-required items.
        in_kanto?(swaps, :BASEMENT_KEY) && required?(swaps[:CARD_KEY]) ->
          true
        in_kanto?(swaps, :CARD_KEY) && required?(swaps[:CLEAR_BELL]) ->
          true
        in_kanto?(swaps, :LOST_ITEM) && required?(swaps[:PASS]) ->
          true
        true ->
          false
      end
    else
      false
    end
  end

  defp any_required_item_in_kanto?(swaps) do
    CrystalKeyItemRandomizer.required_items
    |> Enum.any?(fn(required_item) -> in_kanto?(swaps, required_item) end)
  end

  defp goldenrod_locked?(swaps) do
    cond do
      CrystalKeyItemRandomizer.pre_goldenrod_items |> Enum.any?( &(swaps[&1] == :SQUIRTBOTTLE)) ->
        false
      CrystalKeyItemRandomizer.pre_goldenrod_items |> Enum.any?( &(swaps[&1] == :PASS)) ->
        if in_kanto?(swaps, :SQUIRTBOTTLE) || in_kanto?(swaps, :S_S_TICKET) do
          false
        else
          true
        end
      true ->
        false
    end
  end

  defp surf_locked?(swaps) do
    CrystalKeyItemRandomizer.surf_blocked_items\
    |> Enum.any?( &(swaps[&1] == :HM_SURF) )
  end

  defp tree_locked?(swaps) do
    !Enum.any?(
      CrystalKeyItemRandomizer.pre_tree_items,
      &( swaps[&1] == :HM_CUT || swaps[&1] == :SQUIRTBOTTLE )
    )
  end

  defp in_kanto?(swaps, item) do
    CrystalKeyItemRandomizer.kanto_items
    |> Enum.any?( &(swaps[&1] == item) )
  end

  defp required?(item) do
    Enum.member?(
      CrystalKeyItemRandomizer.required_items,
      item
    )
  end
end
