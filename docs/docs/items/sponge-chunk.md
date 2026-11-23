---
title: "Sponge Chunk"
sidebar_image: "https://i.imgur.com/y0S91Rn.png"
---

A Sponge Chunk is a tool used to collect and remove water in an area. This turns it into a Damp, then a Wet Sponge Chunk, which can be smelted or used in the Nether to dry.

## Obtaining
### Mob Loot
[Drowned Captains](../mobs/drowned.md#drowned-captains) will always drop 1 sponge chunk on death.

### Crafting
Sponge Chunks can be crafted using 9 [Tube Sponge](../blocks/tube-sponge.md).

## Usage
When used on a block, any water in a _5x5x5_⁽¹⁾ area from that block is turned to air.

<img align="right" img src='https://i.imgur.com/rkzOEVe.gif' width='100'>

### Saturation
Each source block of water removed by the sponge chunk is added to its `Saturation`. Being over 0 `Saturation` gives the sponge chunk a greener texture, and renames it to Damp Sponge Chunk.

A sponge chunk can hold a maximum of _30_⁽¹⁾ `Saturation` before it adopts a dripping texture, renames to Wet Sponge Chunk, and can no longer be used.

### Drying
Cooking a damp or wet sponge chunk in a furnace removes all of its `Saturation`, allowing it to be reused. Wet sponge chunks can also be used in the Nether to instantly dry them.

### Crafting
9 sponge chunks can be crafted into a [Sponge](https://minecraft.wiki/w/Sponge) block.

If the sponge chunks have any saturation over 0, they can only be used to craft a Wet Sponge block.

## Item Data
* WaterCount: Integer amount of collected Water Blocks.

## History
| Version | Changes |
| ------- | ------- |
| [1.1.0](../versions/version-1-0-7.md) | <img src='https://i.imgur.com/y0S91Rn.png' width='30'><img src='https://i.imgur.com/uok3aWk.png' width='30'><img src='https://i.imgur.com/bxZDNtv.png' width='30'> Added Sponge Chunks. |


***
1. ↑ Features are configurable.
