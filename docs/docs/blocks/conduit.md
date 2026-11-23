---
title: "Conduit"
sidebar_image: "https://i.imgur.com/yc0GPHX.gif"
---

A conduit is a block that gives [Conduit Power](../effects/conduit-power.md) when active, and can attack nearby underwater hostile mobs.

## Obtaining
### Breaking
Conduits can be mined by hand.

### Crafting
Conduits can be crafted using a [heart of the sea](../items/heart-of-the-sea.md) and eight [nautilus shells](../items/nautilus-shell.md).

## Usage
### Activating
To be activated, a Conduit required a frame to be constructed around it. The frame consists of two parts:
* The inner 3x3x3 area to only be water or water plants
* The outer 5x5x5 area built out of, at minimum, 12 frame blocks. _Accepted frame blocks are Prismarine and Sea Lanterns_⁽¹⁾.

When active, the Conduit grants [Conduit Power](../effects/conduit-power.md) to any Player within range. The range of the conduit is equal to 16 blocks per each 7 blocks in the frame, and extends out in a circle centered on the Conduit. 

Active Conduits will gently bob in place, with their nautilus case open and spinning, pulling currents of water around itself and _conduit particles_⁽¹⁾. The center of the conduit is the Heart of the Sea, which _gently pulses_⁽¹⁾,

### Hunting
When the conduit Frame has 42 accepted blocks, the center Heart will open in an Eye, and the Conduit can attack nearby mobs.

If a hostile mob is within 8 blocks of the Conduit and is wet, the Conduit will target that mob, as shown by Particles. Every 2 seconds, the conduit will attack the targeted mob for 2 hearts (4 damage). If the target leaves the Conduit's hunting range, it will forget and choose a new target.

### Light Source
Conduits emit a light level of _15_⁽¹⁾, no matter the state.


## Mod Compatibility
_Main page: [Mod Compatibilities](../compatibility/mod-compatibilities.md)_  
### Just Enough Items (JEI)
The following information is attached to the Conduit when JEI is installed:  
`A Conduit gives the Conduit Power effect when active, and can attack nearby underwater hostile mobs.`  
`  `  
`To activate, build a frame of Prismarine Blocks around the outside of the conduit, in a 5x5x5 area. The inner 3x3x3 area must be open water, with the conduit in the center. The conduit will animate when active. At least 12 frame blocks are required to activate.`  
`  `  
`The Conduit Power effect is given within an area equal to [frame blocks amount]/7 * 16.`  
`  `  
`If there are at lest 42 block in the frame, the conduit will begin 'hunting', dealing damage to any wet hostile mobs within 8 blocks.`  


## History
| Version | Changes |
| ------- | ------- |
| [1.0.7](../versions/version-1-0-7.md) | Added JEI information. |


## Trivia
* The [Eye of the Sea](../blocks/conduit-eye.md) in the Conduit is rendered using a separate entity, which exists only to be rendered by the conduit tile entity.
* The _Conduit Pulse_⁽¹⁾ feature came from me (mod dev) gaslighting myself because of a bad gif on the Minecraft Wiki ([link](https://minecraft.fandom.com/wiki/Conduit?file=Activated_conduit.gif#Gallery)).

***
1. ↑ Features are configurable.
