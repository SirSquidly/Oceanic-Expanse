---
title: "Drowned"
sidebar_image: "https://i.imgur.com/SoudBBn.png"
---

Drowned are an underwater zombie variant. 

## Spawning
### Natural Generation
Drowned spawn in Oceans and Rivers as long as there is proper darkness.

### Shipwrecks
Drowned spawn in packs in/around [shipwrecks](../generation/shipwreck.md), with 1 pack spawning per chest⁽¹⁾.

## Variants
### Pickled
_Main page: [Pickled](../mobs/pickled.md)_

Pickled are functionally identical, except they are change visually when removed from water, and can drop [sea pickles](../blocks/sea-pickle.md).

### Drowned Captains⁽¹⁾
<img align="right" img src='https://i.imgur.com/Ukbkwc8.png' width='200'>

Some drowned spawn as special 'captains', which have a _unique purplish, decayed texture_⁽¹⁾ and functionality. Drowned Captains feature _double_⁽¹⁾ the health of a regular drowned, will always pickup items, will always spawn with a Trident and Conch, and can summon more drowned.

### Armored Drowned⁽¹⁾
Some Drowned may spawn wearing Armor, which can be randomly enchanted. This functions in the same way [Geared Zombies](https://minecraft.fandom.com/wiki/Zombie#Geared_zombies) do.

Can be disabled via Config.

## Appearance
Bright blue parts of the Drowned's body, including the eyes and mouth, glow in the dark⁽¹⁾.

When in water, Drowned slightly tilt and bob their bodies⁽¹⁾.

## Drops
When killed, drowned drop:
* 0-2 Rotten Flesh (+1 per looting level)
* 5 Exp + 1-3 per equipment (+6 if a Captain or Baby)
* 1 Gold Ingot (5% chance, +1% per looting level)

### Natural Equipment
* Random Armor (If spawned wearing).

Main Hand (Mutually Exclusive):
* 6.25% spawn with a [Trident](../items/trident.md)⁽¹⁾. (100% spawn if a Captain).
* 3.75% spawn with a Fishing Rod⁽¹⁾.

Offhand:
* 3% spawn with a [Nautilus Shell](../items/nautilus-shell.md).
* 100% spawn with a Conch if a Captain.

Any Naturally spawned equipment has a 8.5% chance (+1% per Looting Level) to drop.

Nautilus Shells always drop.

## Behavior
Drowned are functionally very similar to zombies, with the exception that they prefer traveling through water, and are much faster in it. They also do not require oxygen.

Drowned can step up a full block while moving in in water, but _not on land_⁽¹⁾.

### Swimming
When in water, Drowned will swim to match their target, and move in to attack. If it is day, they will keep their lower body submerged in order not to burn.

### Trident Usage
Drowned that spawn holding a [trident](../items/trident.md) will use it as a ranged weapon. They move within 20 blocks of their target, and throw their trident every 1.5 seconds. This will not remove their trident, and tridents thrown by Drowned cannot be picked up.

If their target is within _3 blocks_⁽¹⁾, the Drowned will instead move to melee attack them with the Trident.

### Conch Summoning
Drowned Captains have the ability to spawn additional drowned, on a _10_⁽¹⁾ second cooldown. This summoning is done by blowing their conch, which then spawns one drowned within 10 blocks of itself within water. 


The specifics of the spawning are first these requirements: 
* There are _5_⁽¹⁾ or less drowned within 15 blocks of the Captain
* The Captain is targeting an entity within 10 blocks of itself.  

If these requirements are met, 50 attempts are made to spawn in a drowned. Each attempt will be within a 10 block area, but will keep 3 blocks away from the Captain, and 2 Blocks away from any players. Summoned drowned will also never spawn in the same space as another mob, and can only spawn in water.

## Entity Data
* isCaptain: Gives the drowned the captain texture, along with allowing it to summon. Note that summoning does not require the drowned to hold a conch.

## History
| Version | Changes |
| ------- | ------- |
| 1.0.4 | Removed `EntityAIMoveThroughVillage` and fixed the headlayer of baby drowned not rendering. |
| [1.0.7](../versions/version-1-0-7.md) | Drowned now will not spawn in dimensions outside of the Overworld. |

## Trivia
* Drowned Captains are based on vanilla minecraft's 'leader' zombies, which is an obscure, forgotten feature. I (mod dev) think the feature is neat, and can be reworked into something cool, like the Drowned Captains are.
* The texture for Drowned Captains is based on vanilla minecraft's [early drowned texture](https://minecraft.fandom.com/wiki/Drowned?file=Drowned_old_img.png#Gallery) along with minecraft dungeons [1st variant drowned](https://minecraft.fandom.com/wiki/Minecraft_Dungeons:Drowned?file=Drowned_Variant_1.png).
* In earlier versions of the mod, Drowned used gurgling sounds recorded by me (mod dev). They, uh, kinda sucked. Be glad I just use the vanilla ones.

***
1. ↑ Features are configurable.
