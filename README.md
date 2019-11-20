# crystal-key-item-randomizer

A Clojure + Clojurescript application for randomizing the key items
(including HMs) in the speedchoice fork of Pokemon Crystal.

## changes

In addition to swapping around key items, these changes open up the game:

* Travel to Violet City without having retrieved the Mystery Egg for Elm
* Travel to Goldenrod via Ilex Forest without `CUT` (the cuttable tree
  is removed by the randomizer)
* Travel via train without having fixed the Power Plant
* Travel via SS Aqua without having beaten the Elite 4, on any day of
  the week
* Defeating any 7 gyms will trigger the Radio Tower takeover

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
* The Cianwood Pharmacy item (`SECRETPOTION` in vanilla) is always
  obtainable, even if they player has used the `SECRETPOTION` on Amphy
  at the Lighthouse.
* The Underground Warehouse item is obtainable as soon as Team Rocket
  takes over the Radio Tower. The player may either visit the
  Underground Warehouse during the takeover and obtain the item from
  the director, or visit the Underground Warehouse after the takeover
  and obtain the item from an item ball.

## known issues

* If the player uses the `BASEMENT KEY` to visit the Underground
  Warehouse before defeating Team Rocket in Mahogany Town, the
  Underground Warehouse will be totally empty.
* Some dialogs don't reveal which item was obtained; this affects the
  following key items:
   - `BICYCLE`
   - `RED_SCALE`
   - `SECRETPOTION`
   - `LOST_ITEM`
   - `MYSTERY_EGG`
* During the Goldenrod Radio Tower takeover, if the player gets the
  `CARD KEY` early, the player cannot skip the battle with the fake Radio
  Tower director by using the key card to skip straight to the final
  Rocket executives.
* During the Goldenrod Radio Tower takeover, the player cannot skip
  the first Rocket Executive on the left side of the tower; before
  that, it is impossible to battle the final executive on the right
  side of the tower.

## development

### dependencies

* [clojure](https://clojure.org)
* [leiningen](https://leiningen.org)
* [shadow-cljs](http://shadow-cljs.org/)

To run this application locally, run `lein ring server` at the command
line. At that point, you should be able to see the randomizer home
page in your browser at `http://localhost:3000`. Note that `lein ring
server` automatically reloads code.

## to do

in priority order:

- investigate bug with Buena
  - steps to reproduce: do the Radio Card quiz before talking to
    Buena
  - don't forget to reenable the Buena patch
- modify frontend to enable users to generate multiple seeds without
  refreshing
- convert JSON patch files into a more readable, commentable format
  (yaml probably)
- investigate seed 177
  - seems okay, but it forces the player to start the Radio Tower to
    get Surf from the Ice Path
  
### logic

- **randomize pokegear cards**. this is primarily motivated by wanting
  to randomize the kanto expansion card, which allows the player to
  wake the snorlax and progress to pewter from vermilion.
- randomize which non-required item the copycat is looking for.
- randomize badges
- find out what happens if the player:
  1. trigger the Radio Tower takeover
  2. then trigger the Lake of Rage/Mahogany events
  3. then defeat Team Rocket at Mahogany Town
  4. finally defeat Team Rocket at the Radio Tower
  - players should probably not switch the order of 3. and 4., but
    what happens in that case anyway?

### binary patching

- prevent players from visiting the Underground Warehouse before
  defeating Team Rocket in Mahogany Town; even if the player gets the
  `BASEMENT_KEY` early, nobody will be in the Underground Warehouse
  until after defeating Team Rocket in Mahogany Town. (won't fix?)
- fix `giveitem` key items so that the player sees item is actually
  being given.
  - hard way: change `giveitem` to `verbosegiveitem` and fill the rest
    of the routine with `nop` and a final `end` to cut off any
    unintended additional text
  - items:
	- `SECRETPOTION`
    - `BICYCLE`
    - `RED_SCALE`
    - `MYSTERY_EGG`
    - `LOST_ITEM`
- patch kanto gyms so that they can activate Team Rocket's takeover of
  the Radio Tower (vanilla, not speedchoice; won't fix?)

[pclalv/randomizer-labels]: https://github.com/pclalv/pokecrystal/tree/randomizer-labels
[pclalv/speedchoice]: https://github.com/pclalv/pokecrystal/tree/speedchoice
