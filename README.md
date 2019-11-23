# [crystal-key-item-randomizer](https://crystal-key-item-randomizer.herokuapp.com/)

A Clojure + Clojurescript application for randomizing the key items
(including HMs) in the speedchoice fork of Pokemon Crystal.

## changes, known issues

See the [homepage](https://crystal-key-item-randomizer.herokuapp.com/).

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

- convert JSON patch files into a more readable, commentable format
  (yaml probably)
- investigate seed 177
  - seems okay, but it forces the player to start the Radio Tower to
    get Surf from the Ice Path
- progressive fishing rods
  
### logic

- **consider pokegear cards in prereqs logic**. this is primarily
  motivated by wanting to randomize the kanto expansion card, which
  allows the player to wake the snorlax and progress to pewter from
  vermilion.
- randomize which non-required item the copycat is looking for.
- randomize badges
- find out what happens if the player:
  1. trigger the Radio Tower takeover
  2. then trigger the Lake of Rage/Mahogany events
  3. then defeat Team Rocket at Mahogany Town
  4. finally defeat Team Rocket at the Radio Tower
  - players should probably not switch the order of 3. and 4., but
    what happens in that case anyway?
- option to not have to do Rock Tunnel without Flash

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
- **Randomizer pokegear cards among items**. this is pretty
  straightforward, because `giveitem`, `verbosegiveitem` and `setflag`
  (pokegear cards are flags) are all three-byte instructions. the
  exception, of course, is `itemball`. i think this would require
  custom ASM that acts like an itemball on the overworld but instead
  sets a game flag.
  - itemballs can also be `person_event`s - see electrodes in
    `TeamRocketBaseB2F.asm`

[pclalv/randomizer-labels]: https://github.com/pclalv/pokecrystal/tree/randomizer-labels
[pclalv/speedchoice]: https://github.com/pclalv/pokecrystal/tree/speedchoice
