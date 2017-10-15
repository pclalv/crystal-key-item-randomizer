REQUIRED_ITEMS = [
  :HM_SURF,
  :HM_STRENGTH,
  :HM_WHIRLPOOL,
  :HM_WATERFALL,
  :SECRETPOTION,
]

# only one of HM_CUT/SQUIRTBOTTLE is required
MAYBE_REQUIRED_ITEMS = [
  :BASEMENT_KEY,
  :CARD_KEY,
  :HM_CUT,
  :LOST_ITEM,
  :PASS,
  :S_S_TICKET,
  :SQUIRTBOTTLE,
]

NON_REQUIRED_ITEMS = [
  # HMs
  :HM_FLASH,
  :HM_FLY,

  # non-HMs
  :BICYCLE,
  :BLUE_CARD,
  :CLEAR_BELL,   # not sure - possibly required for progress?

  # useless
  :COIN_CASE,    # useless
  :GOOD_ROD,     # useless
  :ITEMFINDER,   # useless
  :MACHINE_PART, # useless; train will be available from the get go
  :MYSTERY_EGG,  # useless; blocking battle will be disabled
  :OLD_ROD,      # useless
  # :RAINBOW_WING, # useless; randomizing this is a bad idea...
  :RED_SCALE,    # useless
  :SILVER_WING,  # useless
  :SUPER_ROD,    # useless
]

KANTO_ITEMS = [
  :LOST_ITEM,
  :MACHINE_PART,
  :PASS,
  :SILVER_WING,
  :SUPER_ROD,
]

PRE_SUDOWOODO_ITEMS = [
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

PRE_TREE_ITEMS = [
  :HM_CUT,
  :HM_FLASH,
  :MYSTERY_EGG,
  :OLD_ROD,
]

KEY_ITEMS = REQUIRED_ITEMS + MAYBE_REQUIRED_ITEMS + NON_REQUIRED_ITEMS
