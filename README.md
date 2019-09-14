# crystal-key-item-randomizer

## changes

In addition to swapping around key items, these changes open up the game:

* Travel to Violet City without having retrieved the Mystery Egg for Elm
* Travel to Goldenrod via Ilex Forest without `CUT` (the cuttable tree
  is removed by the randomizer)
* Travel via train without having fixed the Power Plant
* Travel via SS Aqua without having beaten the Elite 4, on any day of
  the week

### quality of life changes

Randomizing the items complicates some parts of the game. The
following changes have been made so that key items are still
collectable.

* The Pokemon Fan Club item (Copycat's `LOST ITEM` in vanilla) is
  always obtainable, even if the player has already returned the real
  `LOST ITEM` to Copycat. The Pokemon Fan Club item is obtainable any
  time after fixing the Power Plant, even without talking to Copycat.
* The Cerulean Gym item (`MACHINE PART` in vanilla) is always
  obtainable, even if the player obtains the `MACHINE PART` early and
  gives it back to the Power Plant manager. The player may activate
  the Rocket Grunt in Cerulean sidequest (in order to retrieve the
  hidden item in the Cerulean Gym) by talking to the the Power Plant
  manager again after returning the `MACHINE PART`.
* The Flower Shop item (`SQUIRTBOTTLE` in vanilla) is always
  obtainable, even if the player has used the `SQUIRTBOTTLE` to defeat
  the Sudowoodo.

## known issues

* If the player uses the `BASEMENT KEY` to visit the Underground
  Warehouse before defeating Team Rocket in Mahogany Town, the
  Underground Warehouse will be totally empty.
* If the player uses the `CARD KEY` to defeat the Rocket Executives in
  the Radio Tower before obtaining the Goldenrod Underground item
  (`BASEMENT KEY` in vanilla), then the player **will not be able to
  obtain the Goldenrod Underground item**, potentially hardlocking the
  player from beating that file.

## development

### dependencies

* [clojure](https://clojure.org)
* [leiningen](https://leiningen.org) (recommended)

To run this application locally, run `lein run` at the command
line. At that point, you should be able to see the randomizer home
page in your browser at `http://localhost:8080`.

## to do

### randomizer logic

- [X] randomize `SILVER_WING`
- [X] update code to reflect that the `MACHINE PART` is unavailable
      without `HM_SURF`, as `HM_SURF` is required to talk to the Power
      Plant manager to begin the sidequest
- [X] allow the player to progress from Violet to Ecruteak with
      `SQUIRTBOTTLE`

### binary patching

- [ ] ensure the flower shop item is always obtainable, even if the
	  player fights sudowoodo before they first visit the flower shop.
- [ ] Allow player to visit power plant for the first time with the
      `MACHINE_PART` and still trigger the Cerulean rocket events
- [ ] fix `giveitem` key items so that the player sees item is actually being given.
    - hard way: change `giveitem` to `verbosegiveitem` and fill the
      rest of the routine with `nop` and a final `end` to cut off any
      unintended additional text (from, say, a local labelled
      routine).
    - items:
        - `SECRETPOTION`
        - `BICYCLE`
        - `RED_SCALE`
        - `MYSTERY_EGG`
        - `LOST_ITEM`
- [ ] randomize pokegear cards. this is primarily motivated by wanting
      to randomize the kanto expansion card, which allows the player
      to wake the snorlax and progress to pewter from vermilion.
- [ ] randomize which non-required item the copycat is looking for.
- [ ] prevent players from visiting the Underground Warehouse before
      defeating Team Rocket in Mahogany Town; even if the player gets
      the `BASEMENT_KEY` early, nobody will be in the Underground
      Warehouse until after defeating Team Rocket in Mahogany Town.

### backend/frontend

- [ ] support seeds for real
