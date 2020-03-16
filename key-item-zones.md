# key item areas

in other randomizers like the ALTTP randomizer, it's known ahead of
time that there will be particular amounts of items found in
dungeons. i forget the specifics, but it's something like some
dungeons are guaranteed to contain one item, and some are guaranteed
to contain two, etc.

i like this idea, and wonder how it might be applied to crystal.

the goal here would be to arrive at some middleground between my
randomizer and ERC's - mine puts items exactly where you expect key
items to be (too strict), and his puts them just about anywhere (too
loose). in the spirit of the Zelda randomizers, players would know
ahead what general areas would hold key items, but not exactly where
the key item(s) would be within an area.

instead of randomizing key items with one another, key items could be
randomized within a key item area. each key item belongs to a key item
area, which can be further rolled up into key item zones. it's not
clear to me yet whether areas are good enough, or if this
randomization should instead be done at the level of zones.

| key item       | area                  | zone      |
|----------------|-----------------------|-----------|
| `MYSTERY_EGG`  | Route 30              |           |
| `HM_FLASH`     | Sprout Tower          | Violet    |
| `OLD_ROD`      | Route 32              |           |
| `HM_CUT`       | Ilex Forest           | Azalea    |
| `BICYCLE`      |                       | Goldenrod |
| `BLUE_CARD`    |                       | Goldenrod |
| `COIN_CASE`    |                       | Goldenrod |
| `SQUIRTBOTTLE` |                       | Goldenrod |
| `HM_SURF`      |                       | Ecruteak  |
| `ITEMFINDER`   |                       | Ecruteak  |
| `GOOD_ROD`     |                       | Olivine   |
| `HM_STRENGTH`  |                       | Olivine   |
| `HM_FLY`       |                       | Cianwood  |
| `RED_SCALE`    | Lake of Rage          | Mahogany  |
| `HM_WHIRLPOOL` | Rocket Hideout        | Mahogany  |
| `BASEMENT_KEY` | Radio Tower           | Goldenrod |
| `CARD_KEY`     | Goldenrod Underground | Goldenrod |
| `CLEAR_BELL`   | Radio Tower           | Goldenrod |
| `HM_WATERFALL` | Ice Path              | Mahogany  |
| `S_S_TICKET`   |                       | Newbark   |
| `SUPER_ROD`    | Route 12              |           |
| `MACHINE_PART` |                       | Cerulean  |
| `LOST_ITEM`    |                       | Vermilion |
| `PASS`         |                       | Saffron   |
| `SILVER_WING`  |                       | Pewter    |

i think that implementating this in CKIR would probably require a
low-level abstraction like 'rooms'.
