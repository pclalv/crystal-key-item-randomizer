#!/usr/bin/env ruby

require 'fileutils'

TREE_LOCKED_SEED = 33759719438042609792783088758057327520

def copy_item_constants_file
  FileUtils.cp('./constants/item_constants.asm', './constants/item_constants.asm.tmp')
end

def generate_seed
  seed = Random.new_seed
  puts "using seed #{seed}"
  seed
end

def in_kanto?(swaps, item)
  KANTO_ITEMS.any? { |kanto_item| swaps[kanto_item] == item }
end

def required?(item)
  REQUIRED_ITEMS.include?(item)
end

def shuffled_key_items
  #KEY_ITEMS.shuffle(random: Random.new(generate_seed))
  KEY_ITEMS.shuffle(random: Random.new(TREE_LOCKED_SEED))
end

def generate_key_item_swaps
  key_item_swaps = KEY_ITEMS.zip(shuffled_key_items).to_h
end

def fix_sudowoodo_locked(swaps)
  # pick a pre-sudowoodo item
  require 'pry'; binding.pry

end

def fix_tree_locked(swaps)
  # pick a pre-tree item
  # swap its replacement it with either HM_CUT or SQUIRTBOTTLE
  require 'pry'; binding.pry

  original_item_to_fix = PRE_TREE_ITEMS.sample
  original_replacement_item = swaps[original_item_to_fix]
  new_replacement_item = [:HM_CUT, :SQUIRTBOTTLE].sample

  swaps[original_item_to_fix] = new_replacement_item

  swaps.each do |original, replacement|
    next unless replacement == new_replacement_item && original != original_item_to_fix
    swaps[original] = original_replacement_item
  end
end

def ss_locked?(swaps)
  # the goal of the randomizer is to beat the elite 4; elm only gives
  # you the SS Ticket after you beat the elite 4.
  replacement = swaps[:S_S_TICKET]

  return true if required?(replacement)

  return true if replacement == :BASEMENT_KEY && required?(swaps[:CARD_KEY])

  return true if replacement == :CARD_KEY && required?(swaps[:CLEAR_BELL])

  return true if replacement == :LOST_ITEM && required?(swaps[:PASS])

  false
end

def kanto_locked?(swaps)
  return false unless in_kanto?(swaps, :PASS) && in_kanto?(swaps, :S_S_TICKET)

  return true if REQUIRED_ITEMS.any? { |item| in_kanto?(swaps, item) }

  return true if in_kanto?(swaps, :BASEMENT_KEY) && required?(swaps[:CARD_KEY])

  return true if in_kanto?(swaps, :CARD_KEY) && required?(swaps[:CLEAR_BELL])

  return true if in_kanto?(swaps, :LOST_ITEM) && required?(swaps[:PASS])

  false
end

def sudowoodo_locked?(swaps)
  return false if PRE_SUDOWOODO_ITEMS.any? { |original_item| swaps[original_item] == :SQUIRTBOTTLE }

  if PRE_SUDOWOODO_ITEMS.any? { |original_item| swaps[original_item] == :PASS }
    return true unless KANTO_ITEMS.any? do |original_item|
      swaps[original_item] == :SQUIRTBOTTLE || swaps[original_item] == :S_S_TICKET
    end
  end

  false
end

def surf_locked?(swaps)
  swaps[:RED_SCALE] == :HM_SURF \
    || swaps[:HM_FLY] == :HM_SURF \
    || swaps[:SECRETPOTION] == :HM_SURF
end

def tree_locked?(swaps)
  PRE_TREE_ITEMS.none? do |original_item|
    swaps[original_item] == :HM_CUT \
    || swaps[original_item] == :SQUIRTBOTTLE
  end
end

def any_unreachable?(swaps)
  ss_locked?(swaps) \
    || kanto_locked?(swaps) \
    || sudowoodo_locked?(swaps) \
    || surf_locked?(swaps) \
    || tree_locked?(swaps)
end

def main
  copy_item_constants_file
  key_item_swaps = generate_key_item_swaps

  while any_unreachable?(key_item_swaps)
    key_item_swaps = fix_ss_locked(key_item_swaps) if ss_locked?(key_item_swaps)

    key_item_swaps = fix_kanto_locked(key_item_swaps) if kanto_locked?(key_item_swaps)

    key_item_swaps = fix_sudowoodo_locked(key_item_swaps) if sudowoodo_locked?(key_item_swaps)

    key_item_swaps = fix_surf_locked(key_item_swaps) if surf_locked?(key_item_swaps)

    key_item_swaps = fix_tree_locked(key_item_swaps) if tree_locked?(key_item_swaps)
  end
end

main
