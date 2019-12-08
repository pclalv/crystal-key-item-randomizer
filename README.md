# [crystal-key-item-randomizer](https://crystal-key-item-randomizer.herokuapp.com/)

A Clojure + ClojureScript application for randomizing the key items
(including HMs) in the speedchoice fork of Pokemon Crystal.

## changes, known issues

See the [homepage](https://crystal-key-item-randomizer.herokuapp.com/).

## environments

### dependencies

* [clojure](https://clojure.org)
* [leiningen](https://leiningen.org)
* [shadow-cljs](http://shadow-cljs.org/) (development only)

### production

1. **Build the uberjar**: `lein with-profile production uberjar`
1. **Run the uberjar**: `java -cp target/uberjar/crystal-key-item-randomizer.jar clojure.main -m crystal-key-item-randomizer.server`

You should be able to see the randomizer home page in your browser at
`http://localhost:8080`.

### local development

1. **Compile the ClojureScript with shadow-cljs**
1. **Run the HTTP server**: `lein ring server`

At that point, you should be able to see the randomizer home page in
your browser at `http://localhost:3000`. Note that `lein ring server`
automatically reloads server-side code.

## to do

in priority order:

- convert JSON patch files into a more readable, commentable format
  (yaml probably)
- investigate seed 177
  - seems okay, but it forces the player to start the Radio Tower to
    get Surf from the Ice Path
- progressive fishing rods
- see if there's an easy and general way to improve trainer AI
- implement new trainer card page to view kanto badges

### tracker

- add a way to shrink the icons or something so it can be fit on a
  stream nicely
- hard to tell a square is on/off, especially for the badges
- reorganize; 
  - group the HMs together
  - group the fishing rods together
  - etc.
- fix broken images
  - for the different HMs, disks with a number beside it would be okay
  - try to change the marked/unmarked colors so that they are visible.

### logic

- **consider pokegear cards in prereqs logic**. this is primarily
  motivated by wanting to randomize the kanto expansion card, which
  allows the player to wake the snorlax and progress to pewter from
  vermilion.
- randomize which non-required item the copycat is looking for.
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
- **Randomizer pokegear cards among items**. this is pretty
  straightforward, because `giveitem`, `verbosegiveitem` and `setflag`
  (pokegear cards are flags) are all three-byte instructions. the
  exception, of course, is `itemball`. i think this would require
  custom ASM that acts like an itemball on the overworld but instead
  sets a game flag.
  - itemballs can also be `person_event`s - see electrodes in
    `TeamRocketBaseB2F.asm`
- try to create a person_event itemball that grants a pokegear card

[pclalv/randomizer-labels]: https://github.com/pclalv/pokecrystal/tree/randomizer-labels
[pclalv/speedchoice]: https://github.com/pclalv/pokecrystal/tree/speedchoice
