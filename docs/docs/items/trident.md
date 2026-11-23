---
title: "Trident"
sidebar_image:
  - "https://i.imgur.com/SrJZvSX.png"
  - "https://i.imgur.com/VB8rRI2.png"
---

A trident is a weapon for both melee and ranged combat, a rare drop from [drowned](../mobs/drowned.md) or crafted using Guardian Spikes.

## Obtaining
### Mob Loot
_6.25%_⁽¹⁾ of [drowned](../mobs/drowned.md) spawn with a trident as their weapon, and have a _8.5% chance_⁽¹⁾ (+1% per Looting Level) to drop it on death.

### Thrown Tridents
Tridents thrown by a player in survival mode can be picked up off the ground. If the trident has Loyalty, it will naturally return to its owner, and cannot be picked up. Tridents thrown by mobs cannot be picked up.

### Repairing
Tridents can only be repaired by combining them with another Trident. Combining can either be done in a crafting table (removing all enchantments) or in an Anvil, preserving the enchantments.

### Crafting
Tridents can be crafted using 3 [Guardian Spikes](../blocks/guardian-spike.md) and 2 Prismarine Shards.

## Usage

### Melee attack
Attacking a mob using the trident deals _9 melee damage_⁽¹⁾, and costs 1 durability to the trident.

### Ranged attack
Holding the use key while holding a Trident charges it. When released at or near full charge, the trident exits the player's inventory, thrown through the air as a projectile.

When a thrown trident impacts a mob, it deals _8 damage_⁽¹⁾ and costs 1 durability to the trident. After hitting a mob, it will bounce off. Thrown Tridents will pass through a mob if it is being ridden by the Trident's user.

When a thrown trident lands on a block, it will stick into that block. A thrown trident will activate detectors like wooden buttons or pressure plates. It can be retrieved after. 

Tridents enchanted with [Loyalty](../enchantments/loyalty.md) will, upon impacting a mob or block, fly back to the owner. The speed of its return scales with the level of Loyalty. Returning Tridents will not deal damage to mobs, and can pass through blocks._ A Loyalty Trident thrown into the void will return_⁽¹⁾ when it reaches _y -15_⁽¹⁾.

Tridents enchanted with [Channeling](../enchantments/channeling.md) will strike a lightning bolt when impacting a mob or _Iron Bars_⁽¹⁾, if there is currently a thunderstorm.

When powered, a dispenser can throw a trident in the direction it is facing. Dispensed Tridents still utilize their enchantments, such as Loyalty Tridents staying in the Dispenser.

### Magic/Riptide attack
If the player use [Riptide](../enchantments/riptide.md) near a mob or another player, the trident deals throwing damage. Using riptide costs 1 durability, with an addition 1 durability per mob damaged. This damage bypasses shields.

When a Riptide Trident is thrown from a dispenser, it's speed is multiplied by the level of Riptide, making it dramatically faster. _If enabled in config, a player can free throw a Riptide Trident by sneaking._

### Elytra
A trident with the riptide enchantment can be used to propel a player using an elytra, but only if they are wet (exposed to rain, or in water).

### Enchantments
There are 5 exclusive enchantments for the trident:
* [Channeling](../enchantments/channeling.md)
* [Impaling](../enchantments/impaling.md)
* [Loyalty](../enchantments/loyalty.md)
* [Riptide](../enchantments/riptide.md)
* [Waterjet](../enchantments/waterjet.md)

Tridents also accept the vanilla enchantments:
* Unbreaking
* Mending
* Curse of Vanishing

While other vanilla enchantments cannot be applied to the Trident in survival, the Trident can actually utilize them. This includes almost every Sword and Bow enchantment, such as Infinity Tridents creating clones, which break upon impact. These use the original enchantment formulas, and thus act the same as the vanilla enchantments.

## Entity Data
* Returning: true if the trident is in the middle of returning to the user. It has no collision (to blocks or entites), when in this state.
* Did_Lightning: If the trident has struck lightning. This is to make sure Channeling doesn't repeatedly strike.
* Item: The item used for rendering the trident. Yes, this accepts any item.

## History
| Version | Changes |
| ------- | ------- |
| [1.0.7](../versions/version-1-0-7.md) | Tridents no longer breaks blocks in Creative Mode |
| [1.1.0](../versions/version-1-1-0.md) | Tridents take damage when mining blocks or attacking mobs. Tridents no longer impact ridden mobs. Tridents can be shot from Dispensers. Tridents how have 1st and 3ed person animations. Tridents can utilize any vanilla enchantments. |

## Trivia
* Due to the trident in this mod using an item to render, it is very easy for resource pack makers to alter the trident without needing to use optifine!


***
1. ↑ Features are configurable.
