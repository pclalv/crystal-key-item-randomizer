defmodule CrystalKeyItemRandomizer.LockFixes do
  def fix_ss_lock(swaps, reachability) do
    if ! reachability[:ss_locked?] do
      swaps
    else
      cond do
        required?(swaps[:S_S_TICKET]) ->
          fix_ss_lock_required(swaps)
        true ->
          fix_ss_lock_maybe_required(swaps)
      end
    end
  end

  defp fix_ss_lock_required(swaps) do
    required_item = swaps[:S_S_TICKET]
    new_s_s_ticket_item = Enum.random(CrystalKeyItemRandomizer.non_required_items)
    new_item_that_gives_required_item = swaps
    |> Enum.find({_key, val} -> val == new_s_s_ticket_item end)
    |> elem(0)

    swaps
    |> Map.puts(:S_S_TICKET, new_s_s_ticket_item)
    |> Map.puts(new_item_that_gives_required_item, required_item)
  end

  defp fix_ss_lock_maybe_required(swaps) do
    
  end

  def fix_kanto_lock(swaps, reachability) do
    if ! reachability[:kanto_locked?] do
      swaps
    else
      
    end
  end

  def fix_sudowoodo_lock(swaps, reachability) do
    if ! reachability[:sudowoodo_locked?] do
      swaps
    else
      
    end
  end

  def fix_surf_lock(swaps, reachability) do
    if ! reachability[:surf_locked?] do
      swaps
    else
      surf_item_to_change = CrystalKeyItemRandomizer.surf_blocked_items
      |> Enum.find(fn(surf_blocked_item) -> swaps[surf_blocked_item] == :HM_SURF end)

      item_that_will_give_surf = CrystalKeyItemRandomizer.key_items -- CrystalKeyItemRandomizer.surf_blocked_items -- [:S_S_TICKET]
      |> Enum.random

      swaps
      |> Map.puts(surf_item_to_change, swaps[item_that_will_give_surf])
      |> Map.puts(item_that_will_give_surf, :HM_SURF)
    end
  end

  def fix_tree_lock(swaps, reachability) do
    if ! reachability[:tree_locked?] do
      swaps
    else
      # pick a pre-tree item
      # swap its replacement it with either HM_CUT or SQUIRTBOTTLE

      pre_tree_item_to_change = Enum.random(CrystalKeyItemRandomizer.pre_tree_items)
      old_replacement_item = swaps[pre_tree_item_to_change]
      new_replacement_item = Enum.random([:HM_CUT, :SQUIRTBOTTLE])
      new_replacement_item_swap = swaps
      |> Enum.reject(fn {key, _val} -> key == pre_tree_item_to_change end)
      |> Enum.find(fn {_key, val} -> val == new_replacement_item end)
      |> elem(0)

      swaps
      |> Map.puts(pre_tree_item_to_change, new_replacement_item)
      |> Map.puts(new_replacement_item_swap, old_replacement_item)
    end
  end
end
