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

  # the seed is ss-locked if the `S_S_TICKET` is replaced by any
  # required key item.
  defp ss_locked?(swaps) do
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

  # the seed is kanto-locked if kanto is inaccessible but any required
  # key item is in kanto.
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

  # the seed is goldenrod-locked if the player reaches goldenrod and
  # can make progress neither by clearing sudowoodo nor by traveling to
  # kanto.
  defp goldenrod_locked?(swaps) do
    cond do
      CrystalKeyItemRandomizer.pre_goldenrod_items |> Enum.any?( &(swaps[&1] == :SQUIRTBOTTLE) ) ->
        false
      CrystalKeyItemRandomizer.pre_goldenrod_items |> Enum.any?( &(swaps[&1] == :PASS) ) ->
        cond do
          swaps[:SUPER_ROD] == :SQUIRTBOTTLE ->
            false
          swaps[:SUPER_ROD] == :S_S_TICKET ->
            cond do
              # `HM_SURF` will allow the player to do lots of stuff,
              # including kanto key item sidequests.
              [:HM_STRENGTH, :HM_SURF, :GOOD_ROD] |> Enum.any?( & (swaps[&1] == :HM_SURF) ) ->
                false
              # `BASEMENT_KEY` allows the player to get the `CARD_KEY`
              # and then maybe the `CLEAR_BELL`.
              #
              # TODO: not sure about the implications of receiving
              # `BASEMENT_KEY` at this point in the game. i'm
              # concerned that the player would not be able to get the
              # original `BASEMENT_KEY`. so, if that item is required,
              # then we're still blocked here, and checking right now
              # might be the only way of knowing...
              # [:HM_STRENGTH, :HM_SURF, :GOOD_ROD] |> Enum.any?( & (swaps[&1] == :BASEMENT_KEY) ) ->
              # required?(swaps[:BASEMENT_KEY])
              # TODO: not sure about the implications of receiving
              # CARD_KEY at this point in the game. does getting
              # CARD_KEY early preclude the player from triggering
              # some rocket/radio tower events?  are any of those
              # events important? possibly there'd be the same problem
              # of not being able to get the `BASEMENT_KEY`.
              # [:HM_STRENGTH, :HM_SURF, :GOOD_ROD] |> Enum.any?( & (swaps[&1] == :CARD_KEY) ) ->
              #   required?(swaps[:BASEMENT_KEY])
              true ->
                true
            end
          true ->
            true
        end
      true ->
        true
    end
  end

  # the seed is surf-locked if any surf-blocked item is replaced by `HM_SURF`.
  defp surf_locked?(swaps) do
    CrystalKeyItemRandomizer.surf_blocked_items\
    |> Enum.any?( &(swaps[&1] == :HM_SURF) )
  end

  # the seed is tree-locked if none of the pre-tree items are HM_CUT or the SQUIRTBOTTLE.
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
      CrystalKeyItemRandomizer.required_item_names,
      item
    )
  end
end
