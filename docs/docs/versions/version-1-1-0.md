---
title: "Version 1.1.0"
sidebar_position: 7
---

<img align="right" img src='https://i.imgur.com/OaWvxJL.png' width='400'>

Version 1.1.0, referred to as **Shine & Brine**, is a major update for Oceanic Expanse released on April 24, 2024. 

It focused on adding additional deep sea/sea floor features such as [Lobsters](../mobs/lobster.md), Tropical Slimes, additional valuables such as the Pearl and Prismarine Pots, along with adding additional depth and polish to existing features such as the [Crab](../mobs/crab.md), [Clam](../mobs/clam.md), [Drowned](../mobs/drowned.md), [Trident](../items/trident.md), and Heavy Boots. 

## Additions

### Blocks

<img align="left" img src='https://i.imgur.com/VTG577h.png' width='40'>

#### [Tropical Slime Block](../blocks/tropical-slime-block.md)
* Pushes and pulls adjacent blocks, just like normal Slime Blocks.
* Cannot be pulled by a Sticky Piston.
* Has Momentum-Based collision, allowing fast entities to pass straight through.
* Tropical Slimes always move straight through.
* Crafted using 9 Tropical Slimeballs, can be reversed.

<img align="left" img src='https://i.imgur.com/o9obHNA.gif' width='60'>

#### [Prismarine Pot](../blocks/prismarine-pot.md)
* A new container, with 4 slots.
* Crafted using 1 Pearl, and 7 Prismarine Shards.
* Drops 3 (+ Fortune Level) to 7 Prismarine Shards when mined without Silk Touch.
* Can be plugged using a Wood Button, preventing access.
* Dramatically increases in hardness when plugged.
* In Creative Mode, can be un-plugged using an Axe.

<img align="left" img src='https://i.imgur.com/5qG6tqK.gif' width='30'>

#### [Pickled Head](../blocks/pickled-head.md)
* Outputs a comparator signal depending on weather. 3 in Rain, 7 in Thunder.
* Changes appearance when wet.
* Plays Drowned sounds when attached to a Noteblock.
* Dropped by killing a Pickled with a Charged Creeper explosion.
* Rarely dropped by bonemealing a Pickled, at a 6.9% chance.

#### Nacre Block
* Crafted using 1 Pearl.
* Slowly changes in hue.

#### Smooth Nacre Block
* Currently unobtainable.
* Slowly changes in hue.

### Enchantments

#### Mob Stomp
* Exclusive to the [Heavy Boots](../items/heavy-boots.md).
* Deals damage to a mob when landing on them, proportional to the distance fallen, multiplied by the enchantment level.
* Resets fall damage on a successful stomp.
* Has 2 levels.
* Takes 1 Durability Damage on a successful stomp.
* Bounces the user back up after a successful bounce, proportional to the damage dealt.

### Items

<img align="left" img src='https://i.imgur.com/h1mdBKS.png' width='40'>

#### [Bleak Mulch](../items/bleak-mulch.md)
* Removes Seagrass in a 15x15x15 area.
* Crafted using 1 Pearl and 1 Prismarine Shard.

<img align="left" img src='https://i.imgur.com/Yn4m6cW.png' width='40'>

#### [Cooked Crab](../items/cooked-crab.md)
* Restores 6 hunger and 7.2 saturation.

<img align="left" img src='https://i.imgur.com/leNboSb.png' width='40'>

#### [Cooked Lobster](../items/cooked-lobster.md)
* Restores 8 hunger and 12.8 saturation.

<img align="left" img src='https://i.imgur.com/kcsEqd1.png' width='40'>

#### [Crab Bisque](../items/crab-bisque.md)
* Restores 10 hunger and 16 saturation.
* Crafted using 1 Bowl, 1 Bucket of Milk, 1 Cooked Crab, and 1 Crustacean Shell.

<img align="left" img src='https://i.imgur.com/M2N1SCn.png' width='40'>

#### [Crustacean Shell](../items/crustacean-shell.md)
* Used to craft Coquina, Bonemeal, Crab Bisque, and Lobster Bisque.

<img align="left" img src='https://i.imgur.com/9zp7L6R.png' width='40'>

#### [Conduit Charm](../items/conduit-charm.md)
* Automatically activates when underwater, and without Conduit Power.
* Operates in Pulses, granting Conduit Power for 5 seconds.
* Also grants Conduit Power to any players in a 16x16x16 area.
* Make a beat and particle pull each Pulse.
* Takes Durability Damage on each Pulse
* Lasts for 2.5 minutes of constant usage.
* Crafted using 1 Pearl, 2 Nautilus Shells, and 5 Iron Nuggets.
* Cannot be given Mending or Unbreaking.

<img align="left" img src='https://i.imgur.com/rMJpoye.png' width='40'>

#### [Glow Item Frame](../items/glow-item-frame.md)
* A variant of the item frame.
* Has a slight glow, but doesn't produce light.
* Renders held items fullbright.
* Can be placed on ceilings and floors.
* Crafted using 1 Glow Ink Sac and 1 Item Frame.

<img align="left" img src='https://i.imgur.com/9VHY8Qx.png' width='40'>

#### [Lobster Bisque](../items/lobster-bisque.md)
* Restores 14 hunger and 22.4 saturation.
* Crafted using 1 Bowl, 1 Bucket of Milk, 1 Cooked Lobster, and 1 Crustacean Shell.

<img align="left" img src='https://i.imgur.com/Rgsm6Ad.png' width='40'>

#### [Raw Crab](../items/raw-crab.md)
* Drops from crabs.
* Restores 2 hunger and 1.6 saturation.
* Can be cooked into Cooked Crab.

<img align="left" img src='https://i.imgur.com/aiHYHfY.png' width='40'>

#### [Raw Lobster](../items/raw-lobster.md)
* Drops from lobsters.
* Restores 3 hunger and 0.9 saturation.
* Can be cooked into Cooked Lobster.

<img align="left" img src='https://i.imgur.com/rkzOEVe.gif' width='40'>

#### [Sponge Chunk](../items/sponge-chunk.md)
* Collects water in a 5x5x5 area.
* Holds up to 30 Water.
* Cook in a furnace to dry.
* Used to craft Sponge blocks if dry, or Wet Sponge when wet.

<img align="left" img src='https://i.imgur.com/uDSktu6.png' width='40'>

#### [Tropical Slimeball](../items/tropical-slimeball.md)
* Drops from Tropical Slimes.
* Removes 3 hunger and restores 4 air bubbles. Does not affect Saturation.
* Used to craft Tropical Slime Blocks.
* Eaten at double the speed of normal food items.

<img align="left" img src='https://i.imgur.com/otUvqsU.png' width='40'>

#### [Palm Boat](../items/palm-boat.md)
* A new boat type, crafted from Palm Planks.

### Mobs

<img align="left" img src='https://i.imgur.com/OAL1lKo.png' width='60'>

#### [Lobster](../mobs/lobster.md)
* Come in 9 variants, or split-colored.
* Come in varying sizes, which affect their health, speed, damage, and drops.
* Consume food items given to them, or collected from the floor.
* Attempt to molt after 2.5 minutes, requiring enough fullness to succeed.
* Molting increases their size by 1, drops a Crustacean Shell, and gives them Weakness for 15 seconds.
* Can be saddled and ridden at sizes over 12.
* Using shears on a saddled lobster removes the saddle.
* Can be bred using Clownfish. One will then search for a Water block, then spawn the baby.

<img align="left" img src='https://i.imgur.com/kT6zVM6.png' width='40'>

#### [Tropical Slime](../mobs/tropical-slime.md)
* A variant of Slime, spawning on the ocean floor of Slime Chunks.
* Spawns at a Medium Size, with rare Large sizes only spawning based on Regional Difficulty.
* Spawns a Tropical Fish on death.
* Hostile only to wet players.
* Bucketing kills them instantly, instead giving a Bucket of Tropical Fish.
* Burning evaporates them!
* Also spawn in Jungle Biomes, depending on the Moon Phase.
* Has custom sound effects!

### World generation

#### Monument Pots
* Generates within Ocean Monuments.
* Uses it's own loot table, `oe:chests/monument_mystic`.

### Gameplay

#### Drown Conversion
* Zombies underwater for ~16 seconds convert into Drowned.

#### Drown Disabling
* Most undead mobs no longer loose air underwater.

#### Swim Removal
* Most undead mobs no longer Swim, instead sinking into water.


***

## Changes

### Blocks

<img align="left" img src='https://i.imgur.com/0TyF1p8.png' width='40'>

#### [Blue Ice](../blocks/blue-ice.md)
* Crafted using 9 Packed Ice.

<img align="left" img src='https://i.imgur.com/yR0e5F8.png' width='40'>

#### [Coquina](../blocks/coquina.md)
* Recipe changed to Bonemeal and either [Barnacle Shells](../items/barnacle-shells.md), [Turtle Scute](../items/scute.md), or Crustacean Shell.
* Recipe changed to only give 1.

<img align="left" img src='https://i.imgur.com/LN93e0o.png' width='40'>

#### [Palm Planks](../blocks/palm-planks.md)
* Can be crafted using [Stripped Palm Log](../blocks/palm-log.md).
* Can be crafted using [Stripped Palm Wood](../blocks/palm-wood.md).

<img align="left" img src='https://i.imgur.com/9PgVb2s.gif' width='40'>

#### [Tube Sponge](../blocks/tube-sponge.md)
* Changed shear harvest to drop 2, if harvested at full age, making tube sponges farmable.
* Now used to craft Sponge Chunks.

### Items

<img align="left" img src='https://i.imgur.com/DxNc5ay.png' width='40'>

#### [Glow Ink Sac](../items/glow-ink-sac.md)
* Using sprays ink.
* Mobs within the ink cloud are given Blindness for 5 seconds.

<img align="left" img src='https://i.imgur.com/OlQhoSl.png' width='40'>

#### [Heart of the Sea](../items/heart-of-the-sea.md)
* Replaced the Texture.

<img align="left" img src='https://minecraft.wiki/images/Ink_Sac_JE1_BE1.png?2f56f' width='40'>

#### Ink Sac
* Using sprays glow ink.
* Mobs within the ink cloud are given Blindness, and Glowing for 5 seconds.

<img align="left" img src='https://i.imgur.com/056MShB.png' width='40'>

#### Pearl
* Implemented.
* Replaced the Texture.

<img align="left" img src='https://minecraft.wiki/images/Water_Bucket_JE1_BE1.png?17e70' width='40'>

#### [Spawn Bucket](../items/spawn-bucket.md)
* Displays the variant/color name of Lobsters and Crabs.
* Copies less tags.

#### Spawn Egg
* Tweaked the Crab spawn egg color.

<img align="left" img src='https://i.imgur.com/VB8rRI2.png' width='40'>

#### [Trident](../items/trident.md)
* Added new 1st and 3ed person animations.
* Works with all vanilla combat enchantments, even though they cannot be applied by default.
* Can be shot from Dispensers.
* Passes through ridden mobs.
* Has extra bounce after hitting an entity.
* Cannot hurt other entities when bouncing.
* Makes a smoke puff when extinguished.
* Slightly adjusted the turning.

### Mobs

<img align="left" img src='https://i.imgur.com/dsTWgqV.png' width='40'>

#### [Clam](../mobs/clam.md)
* Spawn occasionally carrying Sand, Gravel, or Pearls.
* Have a 100% chance to drop held items on death.
* Held items can be removed by interacting with an empty hand while it is open.
* Empty clams can have items placed into them, by interacting while it is open.
* Now only avoid ambiently opening if a player is near.
* Expanded their detection to 8x8x8.

<img align="left" img src='https://i.imgur.com/GKa0nHE.png' width='40'>

#### [Crab](../mobs/crab.md)
* Digging has been completely overhauled, and replaced:
* Can dig on more blocks such as Red Sand, Gravel, Shelly Sand, and Soul Sand.
* Has unique Dig Loot Tables per block.
* Drop Raw Crab and Crustacean Shell when killed.
* Spawns in all vanilla Beach Biomes.
* Comes in 6 new Variants, 2 per beach biome.
* Can be bred using Clownfish. One will then search for a Water block, then spawn the baby crab.
* Will never re-bury a held item if a nearby player is carrying a trading item.
* Raise claws when angry.
* Release ambient bubbles when underwater.
* Added 'CanBarter' nbt tag, setting if the crab can barter with players or floor items.
* Added 'CanDig' nbt tag, setting if the crab can dig to obtain or remove held items.
* Added 'HasEgg' nbt tag, if the crab has to go drop off a baby.
* Replaced the Texture.

<img align="left" img src='https://i.imgur.com/thO3Xcs.gif' width='40'>

#### [Cod](../mobs/cod.md)
* Smoothed all animations.
* Added side-to-side flopping animations.

<img align="left" img src='https://i.imgur.com/SoudBBn.png' width='40'>

#### [Drowned](../mobs/drowned.md)
* Smoothed trident raising animations, and made more natural.
* Added player-style swimming animations when going directly up or down to a target.
* No longer in-fight with fellow Drowned.
* Added a unique loot table for Drowned Captains.
* Can spawn with their equipment enchanted.

#### [Pufferfish](../mobs/pufferfish.md)
* Smoothed all animations.
* Added side-to-side flopping animations.

<img align="left" img src='https://i.imgur.com/EX9hQU8.gif' width='40'>

#### [Salmon](../mobs/salmon.md)
* Smoothed all animations.
* Added side-to-side flopping animations.

#### Squid
* Has a sound when spraying ink.

<img align="left" img src='https://i.imgur.com/JmwXb0A.gif' width='40'>

#### [Tropical Fish](../mobs/tropical-fish.md)
* Smoothed all animations.
* Added side-to-side flopping animations.

<img align="left" img src='https://i.imgur.com/MqnrncA.png' width='40'>

#### [Turtle](../mobs/turtle.md)
* Renders slightly larger when carrying an egg.

### [Mod Compatibilities](../compatibility/mod-compatibilities.md)

#### Enchantment Descriptions
* Added an Enchantment Description for Mob Stomp.

#### Fluidlogged API
* Added Prismarine Pots to the Fluidlogged API.

#### JEI
* Removed Double Slabs from appearing in the menu.

***

## Removals

### Blocks

<img align="left" img src='https://i.imgur.com/PI83QMp.gif' width='40'>

#### Kelp Stem
* Removed to consolidate into Kelp.

#### Tall Seagrass
* Removed to consolidate into Seagrass.


### Items

<img align="left" img src='https://i.imgur.com/C6zBvjB.png' width='40'>

#### [Bleach](../items/chlorine.md)
* Removed and replaced by Bleak Mulch.

## Bugfixes

* [#31](https://github.com/SirSquidly/Oceanic-Expanse/issues/31) - BucketMobEvent causing a crash.
* [#36](https://github.com/SirSquidly/Oceanic-Expanse/issues/36) - Certain blocks don't filter light, when using Fluidlogged API.
* [#42](https://github.com/SirSquidly/Oceanic-Expanse/issues/42) - Disabling Channeling also disables Loyalty.
* [#45](https://github.com/SirSquidly/Oceanic-Expanse/issues/45) - InjectAIEvent causing a crash.
* [#48](https://github.com/SirSquidly/Oceanic-Expanse/issues/48) - Shear on short Sea Oats returns 2.
* [#49](https://github.com/SirSquidly/Oceanic-Expanse/issues/49) - Fish do not despawn.
* [#50](https://github.com/SirSquidly/Oceanic-Expanse/issues/50) - Multiple blocks don't support modded shears.
* [#54](https://github.com/SirSquidly/Oceanic-Expanse/issues/54) - Multiple world gen issues if blocks are disabled.
* [#55](https://github.com/SirSquidly/Oceanic-Expanse/issues/55) - Slabs cause items, particles, ect to render pitch black atop them.
* [#56](https://github.com/SirSquidly/Oceanic-Expanse/issues/56) - Descent pulls down Creative/Spectator Mode players.
* [#57](https://github.com/SirSquidly/Oceanic-Expanse/issues/57) - Bucketed Mobs keep their Leash.
* [#59](https://github.com/SirSquidly/Oceanic-Expanse/issues/59) - InjectAIEvent causing a crash.
* Fixed bouncing Tridents repeatedly dealing attack damage.
* Fixed bouncing Tridents repeatedly taking durability damage.
* Fixed Clam Death not using the correct Subtitles.
* Fixed Conduits making active sounds, when not active.
* Fixed Conduit Power nullifying Descent.
* Fixed Spawn Buckets holding Null causing a crash.
* Fixed Tridents not taking damage when used for a melee attack.
* Fixed Tridents not taking damage when used to mine blocks.
* Fixed Turtles not moving on land.
