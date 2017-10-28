# reachability analysis

*randomize first, then fix unreachables*

- define invalid seeds
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
        - when `RED_SCALE` or `HM_FLY` or `SECRETPOTION` or
          `HM_WATERFALL` is replaced with `HM_SURF`
        - what about radio tower? that's triggered by beating pryce,
          and pryce can't be beaten until after the player beats the
          red gyarados.
            - can the goldenrod underground be accessed with the
              `CARD_KEY` at any time? if so, then `HM_SURF` could be
              down there in place of the `CLEAR_BELL`.
            - seems like underground can be done at any time, but
              radio tower depends on mahogany town/lake of rage
              events.
    - tree-locked
        - when neither `HM_CUT` nor `SQUIRTBOTTLE` are acquired by the
          time the player reaches ilex forest
            - see `PRE_TREE_ITEMS`
