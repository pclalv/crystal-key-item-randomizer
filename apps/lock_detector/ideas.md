# goals

* game changes
    - consider turning `CARD_KEY` guy into an `ITEM_BALL`. that would
      allow the player to obtain the `BASEMENT_KEY` early and go get
      whatever's down there without having to first trigger the
      goldenrod rockets events.

* qualitative
    - recreate the experience of a LTTP randomizer with pokemon
      crystal

* quantitative
    - how to quantify
        - open-endedness?
            - what can the player do, and when?
                - new bark: 0
                    - any
                        - go to cherrygrove
                - cherrygrove: 0
                    - any
                        - go to violet
                - violet
                    - SQUIRTBOTTLE
                        - go to goldenrod
                        - go to ecruteak
                    - any
                        - go to azalea
                - azalea
                    - any
                        - go to goldenrod
                - goldenrod
                    - SQUIRTBOTTLE
                        - go to ecruteak
                    - HM_CUT
                        - go to azalea
                    - PASS
                        - go to saffron
                    - BASEMENT_KEY
                        - go to underground/basement
                    - CARD_KEY
                        - go to radio tower
                        - but really, you can't do `CARD_KEY` before
                          getting the `BASEMENT_KEY` because otherwise
                          you won't be able to get the `BASEMENT_KEY`
                - ecruteak
                    - go to olivine
                    - go to mahogany
                - olivine
                    - go to cianwood
                    - go to vermilion
                - mahogany
                    - go to lake of rage
                    - go to rocket hideout
                    - (?) go to blackthorn
                - blackthorn
                    - go to new bark
                - saffron
                    - go to cerulean
                    - go to lavender
                    - go to vermilion
                    - go to goldenrod
                - vermilion
                    - go to saffron
                    - (?) go to pewter
        - choice?

# reachability conditions
    - ss-locked
        - when `S_S_TICKET` is replaced with a required item or a
          maybe required key item that blocks a key item (see subcases
          of kanto-locked)
    - kanto-locked
        - when `PASS` and `S_S_TICKET` both in kanto (can't get to
          kanto)
            - and when any required item is in kanto
            - and when `BASEMENT_KEY` is in kanto and `CARD_KEY` is
              replaced with a required item
            - and when `CARD_KEY` is in kanto and `CLEAR_BELL` is
              replaced with a required item
            - and when `LOST_ITEM` is in kanto and `PASS` is replaced
              wih a required item
    - goldenrod-locked
        - when neither `SQUIRTBOTTLE` nor `PASS` is acquired by the
          time the player reaches sudowoodo from goldenrod side
            - see `PRE_GOLDENROD_ITEMS`
            - and neither the `SQUIRTBOTTLE` nor the `S_S_TICKET` are
              in kanto
                - `S_S_TICKET` assuming that you can ride the boat
                  from vermillion to olivine at any time
            - if `PASS` is available, we're still goldenrod locked
              unless `SUPER_ROD` is `SQUIRTBOTTLE` or `S_S_TICKET`
                - if it's `S_S_TICKET`, then we're still goldenrod
                  locked unless any of `HM_STRENGTH`, `HM_SURF` or
                  `GOOD_ROD` are replaced with either `HM_SURF`,
                  `BASEMENT_KEY`, or `CARD_KEY`
    - surf-locked
        - when any of the following is replaced with `HM_SURF`:
            - `BASEMENT_KEY`
            - `CARD_KEY`
            - `CLEAR_BELL`
            - `RED_SCALE`
            - `HM_FLY`
            - `SECRETPOTION`
            - `HM_WATERFALL`
            - any kanto key item that is not the `SUPER_ROD`
        - see `@surf_blocked_items`
    - tree-locked
        - when neither `HM_CUT` nor `SQUIRTBOTTLE` are acquired by the
          time the player reaches ilex forest
            - see `@pre_tree_items`

# crazy stuff

- trigger rockets radio tower takeover if you get the `BASEMENT_KEY` early
    - those sprites only appear if
      `EVENT_RADIO_TOWER_ROCKET_TAKEOVER`. would trigger by inserting
      line immediately after the line that gives the `BASEMENT_KEY`
        - this could be complicated for, say, `HM_WATERFALL`.
        - it'd be `jumpstd radiotowerrockets`. `goldenrodrockets` is
          just what we call the state where two additional rockets
          are standing around goldenrod after you've beaten 6 gyms
          but before you've beaten 7 gyms.
            - what would happen if you trigger radio tower rockets
              after getting the `BASEMENT_KEY`, but then
              `CARD_KEY` happens to be, say, where `HM_WATERFALL`
              is?
                - could be a lock
            - when should this be triggered?
    - otherwise, `BASEMENT_KEY`, `CARD_KEY`, and `CLEAR_BELL` are surf-locked
        - maybe the doors opened by `BASEMENT_KEY` and `CARD_KEY`
          should check `VAR_BADGES` too?
            - this goes against the spirit of open-ended exploration
              that i'm trying to achieve.
        - if any of those become `HM_SURF` then you'll be blocked,
          because `EVENT_RADIO_TOWER_ROCKET_TAKEOVER` is only set
          after defeating 7 gym leaders, which requires defeating all
          6 before pryce, and then claire OR pryce, (but most likely
          pryce, short of allowing the player to visit blackthorn at
          any time), which would require having beaten the red
          gyarados, which requires surf.
        - sort of? my romhack allows you to trigger rockets after
          collecting kanto gym badges. there are certain cases where
          you'll be able to (theoretically) trigger the rockets with
          kanto gym leaders.
            - surge
                - requires `HM_CUT` or `HM_SURF`
                    - in order to cut the bush or bypass it with surf
            - sabrina
                - nothing required?
            - erika
                - requires `HM_CUT`
            - misty
                - nothing required?
            - brock
                - requires `HM_CUT` and `HM_SURF`
                    - in order to get to pewter via diglett's tunnel
            - blue
                - ???
            - blaine
                - requires `HM_CUT` and `HM_SURF`
                    - in order to get to seafoam via diglett's tunnel
            - janine
                - nothing required?
