defmodule CrystalKeyItemRandomizer.Reachability do
  @locks [:ss_locked?, :kanto_locked?, :sudowoodo_locked?, :surf_locked?, :tree_locked?]
  @enforce_keys @locks
  defstruct @locks

  def analyze(swaps) do
    %CrystalKeyItemRandomizer.Reachability{
      ss_locked?: ss_locked?(swaps),
      kanto_locked?: kanto_locked?(swaps),
      sudowoodo_locked?: sudowoodo_locked?(swaps),
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

  defp sudowoodo_locked?(swaps) do
    cond do
      CrystalKeyItemRandomizer.pre_sudowoodo_items |> Enum.any?(fn(pre_sudowoodo_item) -> swaps[pre_sudowoodo_item] == :SQUIRTBOTTLE end) ->
        false
      CrystalKeyItemRandomizer.pre_sudowoodo_items |> Enum.any?(fn(pre_sudowoodo_item) -> swaps[pre_sudowoodo_item] == :PASS end) ->
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
    |> Enum.any?(fn(surf_blocked_item) -> surf_blocked_item == :HM_SURF end)
  end

  defp tree_locked?(swaps) do
    !Enum.any?(
      CrystalKeyItemRandomizer.pre_tree_items,
      fn
        (pre_tree_item) ->
          swaps[pre_tree_item] == :HM_CUT || swaps[pre_tree_item] == :SQUIRTBOTTLE
      end
    )
  end

  defp in_kanto?(swaps, item) do
    CrystalKeyItemRandomizer.kanto_items
    |> Enum.any?(fn(kanto_item) -> swaps[kanto_item] == item end)
  end

  defp in_johto?(swaps, item) do
    !in_kanto?(swaps, item)
  end

  defp required?(item) do
    Enum.member?(
      CrystalKeyItemRandomizer.required_items,
      item
    )
  end
end
