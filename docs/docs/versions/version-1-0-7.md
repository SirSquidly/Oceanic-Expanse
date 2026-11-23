---
title: "Version 1.0.7"
sidebar_position: 8
---

Version 1.0.7 is an update for Oceanic Expanse that adds minor mods compatibilities, implements missed content, and adds various bug fixes.

## Additions

### [Mod Compatibilities](../compatibility/mod-compatibilities.md)
* Added Enchantment Descriptions support for:  
   * [Channeling](../enchantments/channeling.md)  
   * [Impaling](../enchantments/impaling.md)  
   * [Loyalty](../enchantments/loyalty.md)  
   * [Riptide](../enchantments/riptide.md)  
   * [Waterjet](../enchantments/waterjet.md)  

* Added Potion Descriptions support for:  
   * [Conduit Power](../effects/conduit-power.md)  
   * [Descent](../effects/descent.md)  

* Added JEI information for:  
   * [Barnacle Shells](../items/barnacle-shells.md)  
   * [Coconut](../blocks/coconut.md)  
   * [Conduit](../blocks/conduit.md)  
   * [Guardian Spike](../blocks/guardian-spike.md)  
   * [Flowering Coconut Leaves](../blocks/flowering-coconut-leaves.md)  
   * [Shelly Sand](../blocks/shelly-sand.md)  
   * [Sea Oats](../blocks/sea-oats.md)  
   * [Sea Pickles](../blocks/sea-pickle.md)  

## Changes

### Blocks

#### [Blue Ice](../blocks/blue-ice.md)
* Changed Hardness and Blast Resistance to 2.8
* Mined faster using Pickaxes

#### Coral
* Changed Hardness to 1.5 and Blast Resistance to 6.5

#### Coral Block
* Changed Hardness to 1.5 and Blast Resistance to 6.5

#### Coral Fan
* Changed Hardness to 1.5 and Blast Resistance to 6.5

#### Dead Coral
* Can now be obtained without Silk Touch
* Changed Hardness to 1.5 and Blast Resistance to 6.5

#### Dead Coral Blocks
* Can now be obtained without Silk Touch
* Changed Hardness to 1.5 and Blast Resistance to 6.5

#### Dead Coral Fans
* Can now be obtained without Silk Touch
* Changed Hardness to 1.5 and Blast Resistance to 6.5

#### [Shelly Sand](../blocks/shelly-sand.md)
* Changed Hardness and Blast Resistance to 0.5

#### [Turtle Egg](../blocks/turtle-egg.md)
* Changed Hardness and Blast Resistance to 0.5

### Enchantments

#### [Channeling](../enchantments/channeling.md)
* Can now be disabled in the config  

#### [Impaling](../enchantments/impaling.md)
* Can now be disabled in the config  

#### [Riptide](../enchantments/riptide.md)
* Can now be disabled in the config  



### Items

#### [Barnacle Shells](../items/barnacle-shells.md)
* Used to brew Potions of Descent

#### [Cracked Coconut](../items/cracked-coconut.md)
* Changed the Saturation value to 0.6
* Added Ore Dictionary entries `cropCoconut` and `foodCoconutMilk`

#### [Dried Dulse](../items/dried-dulse.md)
* Changed the Saturation value to 3.6

#### [Potion of Descent](../items/potions.md#potion-of-descent)
* Can be brewed from an Awkward Potion and Barnacle Shells, making it obtainable in Survival Mode

#### [Trident](../items/trident.md)
* No longer breaks blocks in Creative Mode

### Mobs

#### [Drowned](../mobs/drowned.md)
* No longer spawn in dimensions outside of the Overworld

#### [Glow Squid](../mobs/glow-squid.md)
* Max spawn height changed to Y-40
* Max spawn height is now configurable


## Bugfixes
6 issues fixed

* [#22](https://github.com/SirSquidly/Oceanic-Expanse/issues/22) - Food Item Saturation is too high/broken
* [#23](https://github.com/SirSquidly/Oceanic-Expanse/issues/23) - Multiple Blocks break instantly
* [#24](https://github.com/SirSquidly/Oceanic-Expanse/issues/24) - Dead Coral Blocks require Silk Touch to drop
* [#30](https://github.com/SirSquidly/Oceanic-Expanse/issues/30) - Crash when Drowned are Disabled, but Shipwreck Drowned Groups are not
* [#32](https://github.com/SirSquidly/Oceanic-Expanse/issues/32) - Drowned spawning in the Twilight Forest
* [#39](https://github.com/SirSquidly/Oceanic-Expanse/issues/39) - Tridents get 'stuck' in snow layers
