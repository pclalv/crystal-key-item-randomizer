# Todo

## binary patching
- [?] patch out MYSTERY_EGG stuff
    - should be addressed by patching out important battle
- [X] patch out important battle
- [X] allow early travel to kanto
- [X] "Allow player to visit power plant for the first time with the
  MACHINE_PART and still trigger the Cerulean rocket events"?
  https://github.com/pret/pokecrystal/commit/d17cca0693734bc0d53f9cf515f2dbd072bcea03

## frontend
- support seeds

- use this: `bc <<< "obase=16;ibase=16;($internal_address - 4000) + ($bank * 4000)"`
  to compute the hex address from a symlink
- to disappear a person at $address, set their daytime to 0, ie set
  $address + 6 to 0.
- hex patches:
    - BC430: 14 -> 15
        -`setevent EVENT_ROUTE_30_YOUNGSTER_JOEY` -> `setevent EVENT_ROUTE_30_BATTLE`
        - disables 'important battle'

# notes on inspecting the ROM

*rebase on pret/master, it's actually improving a lot!*
*don't worry about item events*

```
in maps/LakeOfRage.asm, put a label right before the script line you want to modify
like this:
GiveItemRedScaleLine::
    giveitem RED_SCALE
then when you build the ROM, look for GiveItemRedScaleLine in pokecrystal.sym
giveitem_command == $1F and RED_SCALE == $42, so at that address you'll see 1F 42 in a hex editor
```

i'll see something like this:

```yaml
pclalvRecvRedScale:
  hex_address_range:
    begin: 458876
    end: 458879
    excl: true
  :values: "$1f $42 $1"
```

refer to `macro/scripts/events.asm` for what these values correspond
to; last value is QUANTITY

`itemball` seems straightforward. `hiddenitem` as used by the machine
part event, has `:values: "$fb $0 $80"`. i would bet that `$80` is the
value corresponding to MACHINE_PART.

the `dw` declaration, `declare word` null-terminates whatever value
you pass to it.

## Serious

## Reachability

- cut-locked
    - if one of the non-super rod kanto key items is required, but CUT
      is itself one of those items...
        - then you have to go thru rock tunnel.
            - that's fine.

## Fun

- consider randomizing which non-required item the copycat is looking
  for.

## pokecyrstal

- if you fight sudowoodo before you visit the flower shop, you can't
  get the flower shop item.
    - what do? i guess the simplest thing is not check if sudowoodo
      has been fought.
    - maybe the flower shop "teacher" should just not even care if
      you've talked to floria.
    - _probably_ fixed

## Playtesting

- see what happens if you get the `BASEMENT_KEY` early
    - you fight rival, but rockets and the radio tower manager are missing...
- see what happens if you get the `CARD_KEY` early
    - as bad as early `BASEMENT_KEY`, i bet

# Elixir

- "I’d probably do something like write to ETS and then stream in a
  separate process. That’d be through a separate GenServer, most
  likely"
    - check out ETS and GenServer, per brichey's advice
- use `recompile` in iex! https://hexdocs.pm/iex/IEx.Helpers.html#recompile/0

# Considerations

## Misc

- what about copycat? copycat lost item side quest is only enabled by
  actually returning the machine part.
    - if the copycat has a required item, then the machine part is
      required.
- also, can i walk to pewter via cerulean?
    - looks like not...
    - you need
        - the expansion card to wake the snorlax
        - cut
        - so tentatively, to get a required item in pewter city, you need
            - `S_S_TICKET`/`PASS`
            - `HM_CUT`
            - `HM_SURF`
                - to restore power to the lavender radio tower and get
                  the expansion card
        - OR to be able to surf thru seafoam islands?
            - can't get to fuchsia via cycling road unless you have
              bike
            - what about via lavender town?
                - can get to fuchsia
                - cannot get to cinnabar
                    - if i were to unblock the seafoam island route,
                      you'd still need `S_S_TICKET`/`PASS` and `HM_SURF`,
                      but not `HM_CUT`. this route should be much slower
                      than the power plant because of how long route
                      12/13/14 is. this could make for interesting routing.
                        - in this case, i'd probably want to edit the
                          vermilion city map so that you don't exit
                          and land right on top of the snorlax
                            - you'd probably get stuck.
        - OR, even if the player could get to the indigo plateau, they'd
          still be blocked from entering kanto.
        - bottom line is, you need to fix the power plant
- how do i trigger the machine part sidequest?
    - need to talk to the guy at the power plant
        - need surf for that...
            - now, say i'm not goldenrod-locked because i have the
              `PASS` and can get to saffron. what can i actually do in
              kanto?
                - i can't surf
                    - can't get to the power plant to trigger the
                      `MACHINE_PART` sidequest
                        - can't get the `LOST_ITEM`
                    - can't get to pewter for the `SILVER_WING`
                - i can only get to lavender town and down to the
                  fishing guru for the `SUPER_ROD`q
                - fuuuuuck
