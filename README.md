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
1. **Run the uberjar**: 
   ```
   java \
     -cp target/uberjar/crystal-key-item-randomizer.jar \
     clojure.main \
     -m crystal-key-item-randomizer.server
   ```

You should be able to see the randomizer home page in your browser at
`http://localhost:8080`.

### local development

1. **Compile the frontend code with shadow-cljs**: The simplest way is
   to run `shadow-cljs compile dev` at the command line; you might
   also run continuous builds with `shadow-cljs watch dev`, or
   otherwise run shadow-cljs from within your editor.
1. **Run the HTTP server**: `lein ring server`

At that point, you should be able to see the randomizer home page in
your browser at `http://localhost:3000`. Note that `lein ring server`
automatically reloads server-side code.

### tests

Currently, only the Clojure code is tested. To run the tests, run
`lein test`.

## to do

in priority order:

- patch vanilla crystal roms into the speedchoice rom 
  - i believe we can determine this by reading some header
- make all pokemon level 100 in order to mitigate how broken the
  level-up system is.
  - what about moves? maybe this should only be a feature for roms
    with randomized moves
- convert JSON patch files into a more readable, commentable format
  (yaml probably)
- investigate seed 177
  - seems okay, but it forces the player to start the Radio Tower to
    get Surf from the Ice Path
- progressive fishing rods
- see if there's an easy and general way to improve trainer AI
- implement new trainer card page to view kanto badges
- consider adding start points other than newbark?
  - ecruteak?
  - goldenrod + bill's eevee, restore cut tree in Ilex to block the
    player in?
- add new optional locations?
  - whirl islands
  - mt mortar
  - dark cave

### tracker

- optionally show names of items, so that the tracker will be more
  immediately useful to those unfamiliar with it

### logic

- **new option: no-grind** so that players don't have to go from
  Goldenrod to Saffron and immediately defeat Sabrina
  - easy way to accomplish this might be ensuring that Sabrina doesn't
    have an early game badge - anything after FOGBADGE should be fine
  - alternatively, is there some way of telling if the player would
    have access to a high level pokemon before facing down high-level
    gym leaders like Sabrina? maybe Super Rod, or Ecruteak access (for
    roamers) would have to be a requirement for those leaders.
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

- fix glitchy clair text
- randomize which janine is the real janine in fuchsia gym
- QOL
  - currently the player has to surf from Cinnabar to Fuchsia to clear
    the barricades in Fuchsia - can we improve this for players?
    https://github.com/pret/pokecrystal/blob/745339014c39a4d47d8a4bdee05bbe8e573933ec/maps/Route20.asm#L10
- **new option: early radio tower** reduce the required badge counts
  from 6 and 7 to 3 and 4 respectively
- enable player to view Kanto pokedex area from the beginning of the
  game
- prevent players from visiting the Underground Warehouse before
  defeating Team Rocket in Mahogany Town; even if the player gets the
  `BASEMENT_KEY` early, nobody will be in the Underground Warehouse
  until after defeating Team Rocket in Mahogany Town. (won't fix?)
- **Randomizer pokegear cards among items**. `giveitem` and
  `verbosegiveitem` are 3-byte instructions; `setflag` and `itemball`
  are (pokegear cards are flags) are 2-byte instructions. how to
  reconcile this? i think this would require custom ASM that acts like
  an itemball on the overworld but instead sets a game flag.
  - problem: fit instructions that behave like `{,verbose}giveitem`
    into 2 bytes
  - problem: fit instructions that behave like `setflag` and
    `itemball` into 3 bytes
  - itemballs can also be `person_event`s - see electrodes in
    `TeamRocketBaseB2F.asm`
- try to create a person_event itemball that grants a pokegear card

[pclalv/randomizer-labels]: https://github.com/pclalv/pokecrystal/tree/randomizer-labels
[pclalv/speedchoice]: https://github.com/pclalv/pokecrystal/tree/speedchoice
