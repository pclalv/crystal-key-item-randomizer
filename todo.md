# Todo

- [ ] randomize pokegear cards. this is primarily motivated by wanting
      to randomize the kanto expansion card, which allows the player
      to wake the snorlax and progress to pewter from vermillion.
- [ ] randomize which non-required item the copycat is looking for.
- [ ] also randomizer SILVER_WING if it wouldn't inconvenience the
      player too much
- [ ] if you fight sudowoodo before you visit the flower shop, you
	  can't get the flower shop item. ensure the flower shop item is
	  always obtainable.

## binary patching

- [ ] fix `giveitem` key items so that the player sees item is actually being given.
    - hard way: change `giveitem` to `verbosegiveitem` and fill the
      rest of the routine with `nop` and a final `end` to cut off any
      unintended additional text (from, say, a local labelled routine).
    - items:
        - SECRETPOTION
        - BICYCLE
        - RED_SCALE
        - MYSTERY_EGG
        - LOST_ITEM
- [X] WHAT DOES THIS MEAN? "Allow player to visit power plant for the first time with the
  MACHINE_PART and still trigger the Cerulean rocket events"?
  https://github.com/pret/pokecrystal/commit/d17cca0693734bc0d53f9cf515f2dbd072bcea03

## frontend

- [ ] support seeds for real

# tips

- use this: `bc <<< "obase=16;ibase=16;($internal_address - 4000) + ($bank * 4000)"`
  to compute the hex address from a sym label
- add exported labels to the disassembly to discover byte values in
  the rom
- to disappear a person at $address, set their daytime to 0, ie set
  $address + 6 to 0.
- hex patches:
    - BC430: 14 -> 15
        -`setevent EVENT_ROUTE_30_YOUNGSTER_JOEY` -> `setevent EVENT_ROUTE_30_BATTLE`
        - disables 'important battle'
