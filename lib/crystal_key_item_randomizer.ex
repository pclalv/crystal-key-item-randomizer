defmodule CrystalKeyItemRandomizer do
  @moduledoc """
  Randomizes the key items of Pokemon Crystal at the assembly level.
  """

  @required_items [
    :HM_SURF,
    :HM_STRENGTH,
    :HM_WHIRLPOOL,
    :HM_WATERFALL,
    :SECRETPOTION,
  ]

  # only one of HM_CUT/SQUIRTBOTTLE is required
  @maybe_required_items [
    :BASEMENT_KEY,
    :CARD_KEY,
    :HM_CUT,
    :LOST_ITEM,
    :PASS,
    :S_S_TICKET,
    :SQUIRTBOTTLE,
  ]

  @non_required_items [
    # HMs
    :HM_FLASH,
    :HM_FLY,

    # non-HMs
    :BICYCLE,
    :CLEAR_BELL,   # not sure - possibly required for progress?

    # useless
    :BLUE_CARD,    # useless
    :COIN_CASE,    # useless
    :GOOD_ROD,     # useless
    :ITEMFINDER,   # useless
    :MACHINE_PART, # useless; train will be available from the get go
    :MYSTERY_EGG,  # useless; blocking battle will be disabled
    :OLD_ROD,      # useless
    # :RAINBOW_WING, # useless; randomizing this is a bad idea because
    #                # of how much of a pain it is to catch all three
    #                # beasts
    :RED_SCALE,    # useless
    :SILVER_WING,  # useless
    :SUPER_ROD,    # useless
  ]

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

  @item_attributes %{
    BASEMENT_KEY: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_CLOSE,   ITEMMENU_NOUSE",
    BICYCLE: "	item_attribute     0, 0,                 0,               CANT_TOSS, KEY_ITEM, ITEMMENU_CLOSE,   ITEMMENU_NOUSE",
    BLUE_CARD: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_CURRENT, ITEMMENU_NOUSE",
    CARD_KEY: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_CLOSE,   ITEMMENU_NOUSE",
    CLEAR_BELL: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    COIN_CASE: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_CURRENT, ITEMMENU_NOUSE",
    GOOD_ROD: "	item_attribute     0, 0,                 0,               CANT_TOSS, KEY_ITEM, ITEMMENU_CLOSE,   ITEMMENU_NOUSE",
    HM_CUT: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, TM_HM,    ITEMMENU_PARTY,   ITEMMENU_NOUSE",
    HM_FLY: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, TM_HM,    ITEMMENU_PARTY,   ITEMMENU_NOUSE",
    HM_SURF: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, TM_HM,    ITEMMENU_PARTY,   ITEMMENU_NOUSE",
    HM_STRENGTH: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, TM_HM,    ITEMMENU_PARTY,   ITEMMENU_NOUSE",
    HM_FLASH: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, TM_HM,    ITEMMENU_PARTY,   ITEMMENU_NOUSE",
    HM_WHIRLPOOL: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, TM_HM,    ITEMMENU_PARTY,   ITEMMENU_NOUSE",
    HM_WATERFALL: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, TM_HM,    ITEMMENU_PARTY,   ITEMMENU_NOUSE",
    ITEMFINDER: "	item_attribute     0, 0,                 0,               CANT_TOSS, KEY_ITEM, ITEMMENU_CLOSE,   ITEMMENU_NOUSE",
    LOST_ITEM: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    MACHINE_PART: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    MYSTERY_EGG: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    OLD_ROD: "	item_attribute     0, 0,                 0,               CANT_TOSS, KEY_ITEM, ITEMMENU_CLOSE,   ITEMMENU_NOUSE",
    PASS: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    RED_SCALE: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    S_S_TICKET: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    SECRETPOTION: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    SILVER_WING: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_NOUSE,   ITEMMENU_NOUSE",
    SUPER_ROD: "	item_attribute     0, 0,                 0,               CANT_TOSS, KEY_ITEM, ITEMMENU_CLOSE,   ITEMMENU_NOUSE",
    SQUIRTBOTTLE: "	item_attribute     0, 0,                 0, CANT_SELECT | CANT_TOSS, KEY_ITEM, ITEMMENU_CLOSE,   ITEMMENU_NOUSE",
  }

  def required_items, do: @required_items
  def maybe_required_items, do: @maybe_required_items
  def non_required_items, do: @non_required_items
  def kanto_items, do: @kanto_items
  def pre_sudowoodo_items, do: @pre_sudowoodo_items
  def pre_tree_items, do: @pre_tree_items
  def surf_blocked_items, do: @surf_blocked_items
  def sudowoodo_blocked_items, do: key_items() -- pre_sudowoodo_items()
  def item_attributes, do: @item_attributes

  def key_items, do: required_items() ++ maybe_required_items() ++ non_required_items()

  @doc """
  Run.

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
    |> write_out_all
  end

  def write_out_all(swaps) do
    write_out_item_constants(swaps)
    write_out_item_attributes(swaps)
    #write_out_item_names(swaps)
    #write_out_item_effects(swaps)
  end

  def write_out_item_constants(swaps) do
    new_item_constants = File.read!("./pokecrystal/constants/item_constants.asm")
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

    File.write!("./pokecrystal/constants/item_constants.asm", new_item_constants)
  end

  def write_out_item_attributes(swaps) do
    # something like this
    # item_attrs_asm =': item_attrs_asm = File.read!("./pokecrystal/items/item_attributes.asm")
    # Regex.replace(~r/(MASTER\sBALL\n)(\t.*)/, item_attrs_asm, "\\1#{CrystalKeyItemRandomizer.item_attributes[:HM_CUT]}")
    # barf
    new_item_attributes = File.read!("./pokecrystal/items/item_attributes.asm")
    |> String.replace(~r/(BASEMENT_KEY)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(BASEMENT_KEY)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(BICYCLE)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(BLUE_CARD)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(CARD_KEY)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(CLEAR_BELL)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(COIN_CASE)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(GOOD_ROD)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(HM_CUT)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(HM_FLASH)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(HM_FLY)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(HM_STRENGTH)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(HM_SURF)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(HM_WATERFALL)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(HM_WHIRLPOOL)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(ITEMFINDER)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(LOST_ITEM)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(MACHINE_PART)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(MYSTERY_EGG)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(OLD_ROD)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(PASS)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(RED_SCALE)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(SECRETPOTION)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(SILVER_WING)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(SQUIRTBOTTLE)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(SUPER_ROD)(\n\t.*)/, "\\1;original\\2", global: false)
    |> String.replace(~r/(S_S_TICKET)(\n\t.*)/, "\\1;original\\2", global: false)
    # similar hack
    |> String.replace(~r/(BASEMENT_KEY);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:BASEMENT_KEY])]}", global: false)
    |> String.replace(~r/(BASEMENT_KEY);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:BASEMENT_KEY])]}", global: false)
    |> String.replace(~r/(BICYCLE);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:BICYCLE])]}", global: false)
    |> String.replace(~r/(BLUE_CARD);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:BLUE_CARD])]}", global: false)
    |> String.replace(~r/(CARD_KEY);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:CARD_KEY])]}", global: false)
    |> String.replace(~r/(CLEAR_BELL);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:CLEAR_BELL])]}", global: false)
    |> String.replace(~r/(COIN_CASE);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:COIN_CASE])]}", global: false)
    |> String.replace(~r/(GOOD_ROD);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:GOOD_ROD])]}", global: false)
    |> String.replace(~r/(HM_CUT);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:HM_CUT])]}", global: false)
    |> String.replace(~r/(HM_FLASH);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:HM_FLASH])]}", global: false)
    |> String.replace(~r/(HM_FLY);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:HM_FLY])]}", global: false)
    |> String.replace(~r/(HM_STRENGTH);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:HM_STRENGTH])]}", global: false)
    |> String.replace(~r/(HM_SURF);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:HM_SURF])]}", global: false)
    |> String.replace(~r/(HM_WATERFALL);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:HM_WATERFALL])]}", global: false)
    |> String.replace(~r/(HM_WHIRLPOOL);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:HM_WHIRLPOOL])]}", global: false)
    |> String.replace(~r/(ITEMFINDER);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:ITEMFINDER])]}", global: false)
    |> String.replace(~r/(LOST_ITEM);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:LOST_ITEM])]}", global: false)
    |> String.replace(~r/(MACHINE_PART);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:MACHINE_PART])]}", global: false)
    |> String.replace(~r/(MYSTERY_EGG);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:MYSTERY_EGG])]}", global: false)
    |> String.replace(~r/(OLD_ROD);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:OLD_ROD])]}", global: false)
    |> String.replace(~r/(PASS);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:PASS])]}", global: false)
    |> String.replace(~r/(RED_SCALE);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:RED_SCALE])]}", global: false)
    |> String.replace(~r/(SECRETPOTION);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:SECRETPOTION])]}", global: false)
    |> String.replace(~r/(SILVER_WING);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:SILVER_WING])]}", global: false)
    |> String.replace(~r/(SQUIRTBOTTLE);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:SQUIRTBOTTLE])]}", global: false)
    |> String.replace(~r/(SUPER_ROD);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:SUPER_ROD])]}", global: false)
    |> String.replace(~r/(S_S_TICKET);original\n(\t.*)/, "\\1\n#{CrystalKeyItemRandomizer.item_attributes[String.to_existing_atom(swaps[:S_S_TICKET])]}", global: false)

    File.write!("./pokecrystal/items/item_attributes.asm", new_item_attributes)
  end

  def write_out_item_names(swaps) do
    
  end

  def write_out_item_effects(swaps) do
    
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
