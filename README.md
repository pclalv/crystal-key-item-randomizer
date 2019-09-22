# crystal-key-item-randomizer

A Clojure + Clojurescript application for randomizing the key items
(including HMs) in Pokemon Crystal.

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
* The Underground Warehouse item is obtainable as soon as Team Rocket
  takes over the Radio Tower. The player may either visit the
  Underground Warehouse during the takeover and obtain the item from
  the director, or visit the Underground Warehouse after the takeover
  and obtain the item from an item ball.

## known issues

* If the player uses the `BASEMENT KEY` to visit the Underground
  Warehouse before defeating Team Rocket in Mahogany Town, the
  Underground Warehouse will be totally empty.
* If the player's 7th badge is not one of the first 7 Johto badges,
  then they will be unable to activate the Goldenrod Radio Tower
  Takeover, potentially hardlocking the player from beating that file.
* Some dialogs don't reveal which item was obtained; this affects the following key items: 
   - BICYCLE
   - RED_SCALE
   - SECRETPOTION
   - LOST_ITEM
   - MYSTERY_EGG

## development

### dependencies

* [clojure](https://clojure.org)
* [leiningen](https://leiningen.org) (recommended)
* [shadow-cljs](http://shadow-cljs.org/) (recommended)

To run this application locally, run `lein ring server` at the command
line. At that point, you should be able to see the randomizer home
page in your browser at `http://localhost:3000`. Note that `lein ring
server` automatically reloads code.

## to do

### randomizer logic

### binary patching

- [ ] verify that the Underground Warehouse itemball patch works for
      arbitrary key items.
- [ ] address all aforementioned known issues
- [ ] fix `giveitem` key items so that the player sees item is
      actually being given.
    - hard way: change `giveitem` to `verbosegiveitem` and fill the
      rest of the routine with `nop` and a final `end` to cut off any
      unintended additional text
    - items:
        - `SECRETPOTION`
        - `BICYCLE`
        - `RED_SCALE`
        - `MYSTERY_EGG`
        - `LOST_ITEM`
- [ ] **randomize pokegear cards**. this is primarily motivated by wanting
      to randomize the kanto expansion card, which allows the player
      to wake the snorlax and progress to pewter from vermilion.
- [ ] randomize which non-required item the copycat is looking for.
- [ ] prevent players from visiting the Underground Warehouse before
      defeating Team Rocket in Mahogany Town; even if the player gets
      the `BASEMENT_KEY` early, nobody will be in the Underground
      Warehouse until after defeating Team Rocket in Mahogany Town.
- [ ] **speedchoice compatibility**. this would be straightforward, if
      tedious. to my mind, the gist of this work is as follows:
	  1. figure out the speedchoice ROM's item addresses and use that
         to create a speechoice-specific version of
         `crystal-key-item-randomizer.data/key-items`
	  2. starting with [`pclalv/speedchoice`][pclalv/speedchoice],
	     create a `speedchoice-randomizer-labels` branch which contains
         the extra labels present in [`pclalv/randomizer-labels`][pclalv/randomizer-labels].
	  3. creatae a `speedchoice-randomizer-changes` branch based off
         of `speedchoice-randomizer-labels` (akin to the relationship
         between `randomizer-labels` and `randomizer-changes`) and use
         the `generate-randomizer-changes-diff` script to diff the the
         two branches to discover the addresses that the ranodmizer
         would need modify. use that information to create a
         speedchoice-specific version of
         `resources/randomizer-patches-diff.json`
	  4. add a 'speedchoice' option to this app which would allow the
         app to use speechoice-appropriate values.

### backend/frontend

[pclalv/randomizer-labels]: https://github.com/pclalv/pokecrystal/tree/randomizer-labels
[pclalv/speedchoice]: https://github.com/pclalv/pokecrystal/tree/speedchoice
