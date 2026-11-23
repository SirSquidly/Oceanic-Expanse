---
title: "Clam"
sidebar_image: "https://i.imgur.com/dsTWgqV.png"
---

A clam is a large, passive mob that launches entities atop it.

## Spawning
Clam spawn underwater atop sand blocks, in any ocean biome. They are rare to find outside of [warm oceans](../generation/warm-ocean.md), due to the abundance of sand in that feature.

## Drops
When killed, clams drop:
* 1–3 Exp when killed by a player or tamed wolf.

Clams always drop any item they are given, and drop items they spawn with naturally at a 100% chance Naturally spawned items are:

| Item | Chance|
| ------- | ------- |
| Pearl | 1/20 |
| Sand | 1/2 |
| Gravel | 1/10 |

### Interacting
When interacted with using an empty hand, if the clam is open, it will drop its held item.

## Behavior
<img align="right" img src='https://i.imgur.com/Gjqhm1i.gif' width='200'>
Clams occasionally open and close on their own. If a non-sneaking player is within a 8x8x8 area of an open clam, it will quickly close itself.

When it is closed, a clam has 20 armor points. This natural armor resets when it opens again, and it will take normal damage.

### Holding Items
Clams are very likely to naturally spawn holding an item. This item is only rendered when the clam is open, displayed at the center of its mouth.

While a clam is open, interacting with it using an empty hand will cause it to drop its held item. An empty clam can have an item placed into it, by using an item on the empty open clam.

Clams never pickup items on their own, and are more likely to launch them. Clams naturally have a 100% chance to drop any held item on death, natural or otherwise.

### Launching
<img align="right" img src='https://i.imgur.com/1rjmrNf.gif' width='200'>

A clam will react to an entity atop it, first by shaking. After 2 seconds of shaking, they will flip open, launching the entity atop it directly up into the air, before closing again. The height of the launch depends on if the clam is submerged, with 15 block underwater, or 7 blocks when out of water.

A clam cannot flip open if there is a solid block directly above it.

Note that the clam's launch height is heavily affected by how much water the launched entity passes through, as water slows the launched entity. So a clam in one block of water can actually throw an entity 30 blocks into the air, as the thrown entity isn't slowed by water.

## History
| Version | Changes |
| ------- | ------- |
| 1.0.0 | Added Clams. |
| [1.1.0](../versions/version-1-1-0.md) | Clams no longer react to any mobs what are not players. Clam's detection range is increased to 8x8x8. Clams now can hold items, and spawn with items. Held items can be taken using an empty hand while the clam is open. Items can be added to an empty clam. |
