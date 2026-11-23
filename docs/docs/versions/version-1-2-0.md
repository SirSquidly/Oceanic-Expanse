---
title: "Version 1.2.0"
sidebar_position: 2
---

<img align="right" img src='https://i.imgur.com/El2VCut.png' width='400'>

Version 1.2.0, referred to as **Magic (and also Dolphins) Update**, is a major update for Oceanic Expanse released on April 2, 2025. 

It adds new uses for Pearls in Stagnants, Magic Conches, Underwater TNT, along with polishing many existing features.

## Additions

### Blocks

#### Stagnant

* Activated by placing any Prismarine Blocks and/or Sea Lanterns within a 5x5 area around it.
  * When active, targets any entities within line of sight, attacking with a laser.
* Having at lest 64 blocks makes it 'Elder', adding 4 to the Attack Damage, and giving Mining Fatigue within a short range.
* Emits a light level of 15.
* Crafted using 1 Conduit, 4 Bleak Mulch, 2 Pearls, and 2 Prismarine Crystals.


#### Underwater TNT

* TNT, that functions underwater!
* Removes some water when exploding.
* Crafted using 1 TNT and 1 Bleak Mulch.

### Items


#### Magic Conch
* Plays the stored Conch sound, and creates a ranged effect:
  * Effect based on the 'Resonance', which is attached to the Magic Conch when crafted.
* Has 32 durability.
* Crafted using 1 Pearl and 1 [Conch](../items/conch.md).
* Cannot be given Mending or Unbreaking.


#### Potion of Seeping
* Brewed with an Awkward Potion and a [Tropical Slime Block](../blocks/tropical-slime-block.md).
* Gives the Seeping effect.

### Effects

#### Dolphin's Grace
* Increases Horizontal Swim Speed.
* Granted by Dolphins.

#### Seeping
* Affected entities spawn two [Tropical Slimes](../mobs/tropical-slime.md) upon death.
  * Amount scales based on amplifier.

### Enchantments

#### Nor's Rebound
* Exclusive to the [Heavy Boots](../items/heavy-boots.md).
* Bounces the user upon landing.
  * Scales based on the fall height.
* Each level increases number of bounces.
* Has 2 levels.

### Mobs


#### Dolphin
* Neutral mob. Will call for help of nearby Dolphins when hurt.
* Follows sprint swimming Players, giving them Dolphin's Grace.
* Can be bred using Cooked Lobster.
* 10% chance to spawn as a Baby.
* Drops Fish on death.
* Requires Air occasionally, and will swim to the surface to get it. 

#### Drowned Shipmate
* Summoned via the Captain's Call resonance.
* Owned by their Summoner
  * Follows their Owner
  * Attacks their Owner's Targets
  * Defends their Owner from attackers
* Can be equip with items and headwear.
  * Headwear items, Skulls (+ Pickled Head), Pumpkins, and Glass (+ Stained Glass) are worn on the Head.
  * Shield items, Arrows (+Tipped +Spectral), Conduit Charm, Conch, Nautilus Shells, and Magic Conchs are held Offhand.
     * Held Conduit Charms are passively used.
     * Held Conchs and Magic Conchs are used when in combat.
  * All else goes into the Mainhand.
  * Can only hold 1 of any item.
  * Items can be removed by interacting.
    * Items are removed Mainhand first, then Offhand, last Headwear.
* Have 4 skins, randomly chosen when spawned.

### Resonances

#### Brine's Breath
* Inflicts Slowness and Descent for 10 seconds to nearby entities.
* Non-Player entities are given Slowness 2.
* Costs 1 durability.

#### Captain's Call
* Summons a Drowned Shipmate, who is owned by the user.
  * Summoned Drowned inherits the name of the Conch, if the Conch has a custom name.
* Costs 4 durability.
  * Cost only applies if summon is successful.

#### Sea's Serenade
* Grants Regeneration and Dolphin's Grace for 10 seconds to nearby entities.
* Inflicts Instant Health to nearby Undead mobs, dealing 6 damage.

#### Wave's Wail
* Throws nearby entities away from the user.



***

## Changes

### Blocks

<img align="left" img src='https://i.imgur.com/8Rbsbl3.gif' width='40'>

#### [Dulse](../blocks/dulse.md)
* Slowed growth speed by 75%.
  * Growth speed further lowers by 1.25% per neighboring Dulse.
* Lowered drops to only 4 at max age.

<img align="left" img src='https://i.imgur.com/FvsYTlb.png' width='40'>

#### [Sea Oats](../blocks/sea-oats.md)
* Always placed at the short state.


<img align="left" img src='https://i.imgur.com/s5Kn3I6.gif' width='40'>

#### [Turtle Egg](../blocks/turtle-egg.md)
* Added all vanilla sound effects:
  * Stomping
  * Breaking
  * Cracking
  * Hatching
* Sneaking atop no longer can cause breaking.
* Turtles no longer can cause breaking.
* Spawns particles when placed atop Sand.


### Items

<img align="left" img src='https://minecraft.wiki/images/Water_Bucket_JE1_BE1.png?17e70' width='40'>

#### [Spawn Bucket](../items/spawn-bucket.md)
* Added vanilla sound effects when releasing the held entity.
* Reads NBT value `block` as the block to place when used, defaults to Water.

### Mobs

<img align="left" img src='https://i.imgur.com/dsTWgqV.png' width='40'>

#### [Clam](../mobs/clam.md)
* Will convert held items:
  * Gravel converts to Sand after 10 minutes.
  * Sand and/or Red Sand converts to Pearls after 40 minutes.
  * Shelly Sand converts into a Pearl after 5 minutes.
  * This list is fully configurable.
* Moved launching functionality to a separate AI task.
* Removed the automatic closing when non-sneaking Players are near.

<img align="left" img src='https://i.imgur.com/thO3Xcs.gif' width='40'>

#### [Cod](../mobs/cod.md)
* Passively swim away from nearby Players.
* Children inherit persistence from the primary parent.
* Now faces their movement direction.

<img align="left" img src='https://i.imgur.com/SoudBBn.png' width='40'>

#### [Drowned](../mobs/drowned.md)
* Slowed water drag when hurt.
* Can now utilize Riptide if on a held Trident.
* Improvements made to targeting AI.

<img align="left" img src='https://i.imgur.com/Ukbkwc8.png' width='40'>

#### [Drowned Captain](../mobs/drowned.md#drowned-captains)
* Spawned Conch now has a random sound set.
* Uses Captain's Call for summoning Drowned.


<img align="left" img src='https://i.imgur.com/OAL1lKo.png' width='40'>

#### [Lobster](../mobs/lobster.md)
* Equips Saddle to chest slot.
* Drops exact equipped Saddle when removed and/or on death.
* Molting drop now pulls from the `oe:gameplay/lobster_molt` loot table.
  * Can be overridden by the `MoltLootTable` NBT tag.
  * Loot table seed can be overridden by the `MoltLootTableSeed` NBT tag.


#### [Pufferfish](../mobs/pufferfish.md)
* Children inherit persistence from the primary parent.
* Now faces their movement direction.

<img align="left" img src='https://i.imgur.com/EX9hQU8.gif' width='40'>

#### [Salmon](../mobs/salmon.md)
* Passively swim away from nearby Players.
* Children inherit persistence from the primary parent.
* Now faces their movement direction.


<img align="left" img src='https://i.imgur.com/JmwXb0A.gif' width='40'>

#### [Tropical Fish](../mobs/tropical-fish.md)
* Passively swim away from nearby Players.
* Children inherit persistence from the primary parent.
* Now faces their movement direction.

<img align="left" img src='https://i.imgur.com/kT6zVM6.png' width='40'>

#### [Tropical Slime](../mobs/tropical-slime.md)
* Immune to evaporating when under the effect of Fire Resistance.

<img align="left" img src='https://i.imgur.com/MqnrncA.png' width='40'>

#### [Turtle](../mobs/turtle.md)
* Renders slightly larger when carrying an egg.

### World generation

<img align="left" img src='https://i.imgur.com/jA9ecz3.png' width='40'>

#### [Palm Trees](../generation/palm-tree.md)
* Only have a 75% chance to place each [Flowering Palm Leaves](../blocks/flowering-coconut-leaves.md).

### [Mod Compatibilities](../compatibility/mod-compatibilities.md)

#### Enchantment Descriptions
* Added an Enchantment Description for Nor's Rebound.

#### Fluidlogged API
* Updated all logic to better function with Fluidlogged API.

#### JEI
* Added information for the Stagnant.

***

## Bugfixes

* [#119](https://github.com/SirSquidly/Oceanic-Expanse/issues/119) - Liquids from other mods cannot be bucketed.
* [#123](https://github.com/SirSquidly/Oceanic-Expanse/issues/123) - Waterlogged generation broken in modern Fluidlogged API versions.
* [#128](https://github.com/SirSquidly/Oceanic-Expanse/issues/128) - Mobs from the Drown Conversion List and/or No Drowning list fail within Fluidlogged API waterlogged blocks.
* [#129](https://github.com/SirSquidly/Oceanic-Expanse/issues/129) - Coral Blocks dry from Fluidlogged API waterlogged blocks.
+ Fixed Entity list configs not working with modded entities.
+ Fixed Icebergs improperly generating in worlds with a non-default Sea Level.
+ Fixed Turtles breaking their own eggs.
