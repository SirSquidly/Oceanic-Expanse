---
title: "Lobster"
sidebar_image: "https://i.imgur.com/OAL1lKo.png"
---

Lobsters are a neutral mob found on Sea Floors. They are a source of Raw Lobster and Crustacean Shells, can be grown to larger sizes, and can be Saddled and ridden when large enough.

## Spawning
Lobsters spawn underwater in Oceans, in groups of 2-4.

Lobsters spawn in different sizes, between 1 - 4.

## Drops
When killed, adult lobsters drop:
* 1 [Raw Lobster](../items/raw-lobster.md)
* 1–3 Exp when killed by a player or tamed wolf.
* 0-1 [Crustacean Shell](../items/crustacean-shell.md)

The drops of the lobster are multiplied by `size / 3`, rounded down. Along with this, each level of Looting has a 50% chance to add 1 more set of drops. Exp drops are not affected.

For example, a size 10 lobster killed with Looting 3 will drop 3-6 Raw Lobster and 0-6 Crustacean Shell. 

## Behavior
Lobsters move at a slow speed, and are barely affected by the drag of water. Lobsters are neutral, retaliating when hurt, but only once.

As they are arthropods, Lobsters are affected by the Bane of Arthropods enchantment.

### Eating
Lobsters have 1 invisible item slot, being their Mainhand. Interacting with a lobster while holding any Fish, Kelp, Dried Kelp, Raw Crab, Cooked Crab, Raw Lobster, Cooked Lobster, or Crustacean Shell will give 1 to the lobster. Any of these items left on the floor for over 5 seconds near the lobster will also be collected. Lobsters will not seek out floor items.

Every second, lobsters will eat their held items, adding to their fullness. Any eaten item will only increase fullness by 1.

### Molting
Every _2.5 minutes_⁽¹⁾, Lobsters will be ready to molt. Molting requires the lobster to be an adult, and to be 'full enough'. 

The formula for full enough is `fullness * 0.75 / current size > 1`.

If these prerequisites are met, the lobster will grow in size by 1, drop a Crustacean Shell, and be given Weakness for 15 seconds. After which, the molting cool down resets.

### Riding
Lobsters that are over 12 in size can be interacted with using a saddle, to put it on them. Saddled Lobsters can be controlled by holding any item they normally eat.

Ridden Lobsters have a slow movement speed, and will constantly move similar to a ridden pig. Rather than dismounting their rider when underwater, Lobsters instead become significantly faster, nearly doubling their speed.

Shift-Interacting with a saddled lobster while holding shears will remove the saddle.

### Breeding
Lobsters can be bred using Clownfish, making one of the lobsters pregnant, similar to Turtles. The pregnant Lobster then searches for a nearby Water Block, where it will then spawn the baby lobster. The baby's color is random.

## Coloring
Lobsters come in 9 base colors, along with having the chance to be split-colored, giving them two different colors on each side.

| Color | Chance |
| -------- | ------------- |
| Albino       | 1/1000     |
| Cotton Candy | 1/1000     |
| Yellow       | 1/2        |
| Orange       | 1/500      |
| Calico       | 1/500      |
| Red          | 1/500      |
| Blue         | 1/100      |
| Black        | 1/2        |
| Brown        | 1/2        |
| -------- | ------------- |
| Split-Colored        | 1/500       |


### Names
When in a bucket, the bucket name and tooltip will display the exact variant of lobster with a unique name. This name is formatted as [first color]-[secondary color (if not same as first)] Lobster. This leads to combinations such as "Brown Lobster" and "Blue-Red Lobster".

Split-colored lobsters that are Orange and Black (or reverse) display the custom name "Halloween Lobster".

## Entity Data
* Angry: If the lobster is angry.
* Size: The size of the lobster. The sizes that spawn naturally are between 1 - 4. Values that are greater than 126 get clamped to 126.
* Variant: The variant of the lobster.
* Saddle: If a saddle is on the lobster.
* MoltCooldown: Number of ticks before a Lobster can molt. Molting occurs at 0 and this timer gets reset to _3000_⁽¹⁾.

***
1. ↑ Features are configurable.
