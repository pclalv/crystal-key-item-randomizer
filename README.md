# crystal-key-item-randomizer

## development

### dependencies

* clojure
* leiningen

To run this application locally, run `lein run` at the command
line. At that point, you should be able to see the randomizer home
page in your browser at `http://localhost:8080`.

## changes

In addition to swapping around key items, these changes open up the game:

* Travel to Violet City without having retrieved the Mystery Egg for Elm
* Progress through Ilex Forest without `CUT`
* Travel via train without having fixed the Power Plant
* Travel via SS Aqua without having beaten the Elite 4, on any day of
  the week
* Get Copycat's `LOST ITEM` from the guy in the Pokemon Fan Club at
  any time after fixing the Power Plant, even without talking to
  Copycat

## todo

### randomizer logic

- [ ] randomize SILVER_WING

### binary patching

- [ ] ensure the flower shop item is always obtainable, even if the
	  player fights sudowoodo before they first visit the flower shop.
- [ ] randomize pokegear cards. this is primarily motivated by wanting
      to randomize the kanto expansion card, which allows the player
      to wake the snorlax and progress to pewter from vermilion.
- [ ] randomize which non-required item the copycat is looking for.
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
- [ ] Allow player to visit power plant for the first time with the
      MACHINE_PART and still trigger the Cerulean rocket events

### backend/frontend

- [ ] support seeds for real
