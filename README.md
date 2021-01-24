# [crystal-key-item-randomizer](https://crystal-key-item-randomizer.herokuapp.com/)

A Clojure + ClojureScript application for randomizing the key items
(including HMs) in the speedchoice fork of Pokemon Crystal.

## warning

I no longer work on this project. If you're looking for a great
Pokemon Crystal randomizer experience, please check out [ERC's
randomizer](https://github.com/erudnick-cohen/Pokemon-Crystal-Item-Randomizer/releases/).

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
   java -jar target/uberjar/crystal-key-item-randomizer.jar
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

- _don't_ randomize the blue card? because it's useless, and
  free. maybe same with similarly free and useless items, like
  itemfinder.
- decide how to support v7
  - wholesale move to v7?
  - support both v6 and v7?
    - could detect which version we've got:

      [~/code/pokecrystal] (speedchoice-changes-v7 0c1e521d4)
      # rg -F VersionNumberText ./crystal-speedchoice.sym
      43191:7a:4000 VersionNumberText

      [~/code/pokecrystal] (speedchoice-labels 170cbae60)
      # rg -F VersionNumberText ./crystal-speedchoice.sym
      6252:05:750a VersionNumberText

- randomize which Mahogany base rockets know the password
  - probably can work similar to randomizing janine
- make it easy to generate all of the patch/label data that CKIR needs
  with a script in crystal-speedchoice
  - mainly for if/when crystal-speedchoice gets updated and patches
    break because code got moved around
  - refactor all code that references patches to pull the patch out of
    a data file, instead of inlining patches in files
- key item zones; see `key-item-zones.md`
- when copycat item is randomized, copycat should tell the player what
  item she's looking for immediately, i.e. without the player having
  fixed the power plant
- make all pokemon level 100 in order to mitigate how broken the
  level-up system is.
  - what about moves? maybe this should only be a feature for roms
    with randomized moves
- convert JSON patch files into a more readable, commentable format
  (yaml probably)
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
- patch vanilla crystal roms into the speedchoice rom
  - i believe we can determine this by reading some header
  - low priority because people will wanna use other randomizations
    first; it's unlikely people will be submitting vanilla ROMs

### tracker

- add numbers to HMs
- optionally show names of items, so that the tracker will be more
  immediately useful to those unfamiliar with it

### logic

- options to improve the Radio Tower rocket section:
  - reach out to ShockSlayer (on discord?) about adding level scaling
    for the rocket sections?
  - figure out what level people are at after the 7th gym and buff
    radio tower rocket levels to somewhere between their current
    levels and the average player level.
    - Snowbear: "I’d say no more than low 30s typical grunts. At most
      executive at Sabrina level lol. Probably aim for level 45
      highest for executives."

### binary patching

- allow the player to escape from the Goldenrod Underground via the
  Department Store
  - there are map tiles that get repositioned if and only if the
    player talks to the director in the basement. those tiles should
    be reposition if the player beats the radio tower section.
- QOL
  - currently the player has to surf from Cinnabar to Fuchsia to clear
    the barricades in Fuchsia - can we improve this for players?
    https://github.com/pret/pokecrystal/blob/745339014c39a4d47d8a4bdee05bbe8e573933ec/maps/Route20.asm#L10
- enable player to view Kanto pokedex area from the beginning of the
  game
- prevent players from visiting the Underground Warehouse before
  defeating Team Rocket in Mahogany Town; even if the player gets the
  `BASEMENT_KEY` early, nobody will be in the Underground Warehouse
  until after defeating Team Rocket in Mahogany Town. (won't fix?)
- **Randomizer pokegear cards among items**

[pclalv/randomizer-labels]: https://github.com/pclalv/pokecrystal/tree/randomizer-labels
[pclalv/speedchoice]: https://github.com/pclalv/pokecrystal/tree/speedchoice
