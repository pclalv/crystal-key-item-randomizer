# misc

* in the case of swapping two item balls, we have to move the entire
  item script, otherwise it might not work (not sure why). how to
  programmatically move a block like that?
    * we don't move it - maybe we just template it out and have
      the script interpolate script blocks as necessary
    * we regex match until the next line not beginning with "\t"

* think about `GS_BALL` and `MYSTERY_EGG`, kanto items

* do i want to try stumpdotio's approach of 'patching each item
  identity'?

# reachability analysis

- non-required items - see scratch.rb

## randomize first

- define invalid seeds
    - [ss-locked] when `S_S_TICKET` is replaced with a required item or a maybe
      required key item that blocks a key item (see subcases of kanto-locked)
    - [kanto-locked] when `PASS` and `S_S_TICKET` both in kanto (can't get to kanto)
        - and when any required item is in kanto
            - enumerate kanto items and write function to determine if an item is in kanto
        - and when `BASEMENT_KEY` is in kanto and `CARD_KEY` is replaced
          with a required item
        - and when `CARD_KEY` is in kanto and `CLEAR_BELL` is replaced
          with a required item
        - and when `LOST_ITEM` is in kanto and `PASS` is replaced wih a
          required item
    - [sudowoodo-locked] when neither `SQUIRTBOTTLE` nor `PASS` is acquired by the time
      the player reaches sudowoodo from goldenrod side
        - and neither the `SQUIRTBOTTLE` nor the `S_S_TICKET` are in kanto
        - see `PRE_SUDOWOODO_ITEMS`
    - [surf-locked] when `RED_SCALE` or `HM_FLY` or `SECRETPOTION` is replaced with
      `HM_SURF`
    - [tree-locked] when neither `HM_CUT` nor `SQUIRTBOTTLE` are acquired by the
      time the player reaches ilex forest
        - see `PRE_TREE_ITEMS`
