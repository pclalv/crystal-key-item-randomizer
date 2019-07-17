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
value corresponding to `MACHINE_PART`.

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
