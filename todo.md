# Todo

- [ ] randomize pokegear cards. this is primarily motivated by wanting
      to randomize the kanto expansion card, which allows the player
      to wake the snorlax and progress to pewter from vermillion.

## reachability

- [ ] try to come up with an example that proves that badge/HM stuff
      is important. for example, is it possible to receive surf, need
      surf to obtain some key item (maybe from cianwood) and
      simultaneously be blocked from getting the ecruteak badge (which
      allows the player to use surf)?

## binary patching
- [ ] fix `giveitem` key items so that the player sees item is actually being given.
    - hard way: change `giveitem` to `verbosegiveitem` and fill the
      rest of the routine with `nop` and a final `end` to cut off any
      inintended additional text (from, say, a local labelled routine).
- [X] patch out MYSTERY_EGG stuff
    - should be addressed by patching out important battle
- [X] patch out important battle
- [X] allow early travel to kanto
- [X] "Allow player to visit power plant for the first time with the
  MACHINE_PART and still trigger the Cerulean rocket events"?
  https://github.com/pret/pokecrystal/commit/d17cca0693734bc0d53f9cf515f2dbd072bcea03

## frontend

- [X] support seeds

- use this: `bc <<< "obase=16;ibase=16;($internal_address - 4000) + ($bank * 4000)"`
  to compute the hex address from a symlink
- to disappear a person at $address, set their daytime to 0, ie set
  $address + 6 to 0.
- hex patches:
    - BC430: 14 -> 15
        -`setevent EVENT_ROUTE_30_YOUNGSTER_JOEY` -> `setevent EVENT_ROUTE_30_BATTLE`
        - disables 'important battle'

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
