---
title: "Turtle"
sidebar_image: "https://i.imgur.com/MqnrncA.png"
---

Turtles are passive mobs.

## Spawning
Turtles spawn atop sand blocks in beach biomes, in groups of 2 - 6. They require a light level of 8 or higher, and between Y-levels 60-67.

## Drops
When killed, adult turtles drop:
* 0-2 Seagrass (+1 per looting level)
* 1–3 Exp when killed by a player or tamed wolf.
* 1 Bowl when killed by lightning (if the turtle has a custom name, the bowl inherits it)

Like other baby animals, killing a baby turtle yields no experience

When a baby turtle grows into an adult, it drops:
* 1 [Scute](../items/scute.md)


## Behavior
Turtles move slowly on land, randomly about, but move surprisingly quick when in water. Like other passive mobs, when attacked, turtles panic and flee.

Turtles cannot be attached by leads, but can be tempted by seagrass.
Turtles die instantly when struck my lightning.

### Predators
_Certain mobs_⁽¹⁾ target baby turtles, actively seeking out and attacking them. These include:

| Predators |
| -------- |
| Zombie |
| Husk |
| Skeleton |
| Stray |
| Wither Skeleton |
| Wild Wolf |
| Wild Ocelot |
| [Drowned](../mobs/drowned.md) |
| [Pickled](../mobs/pickled.md) |

### Home Beach
Adult Turtles remember the spot they spawned at. Every tick there is a 1⁄1000 chance they decide to return home.

### Babies
When a baby turtle grows into an adult, it drops a [scute](../items/scute.md).

## Entity Data
* Going_Home: If the Turtle is currently headed home.
* Has_Egg: If the Turtle has an Egg.
* HomePosX: The X coordinate where the turtle spawned, and will go to lay an egg.
* HomePosY: The Y coordinate where the turtle spawned, and will go to lay an egg.
* HomePosZ: The Z coordinate where the turtle spawned, and will go to lay an egg.

## History
| Version | Changes |
| ------- | ------- |
| 1.0.0 | Added Turtles. |
| [1.1.0](../versions/version-1-1-0.md) | Turtles now render slightly larger when carrying an egg. |

***
1. ↑ Given list of predators is the default defined in the config. The config can be altered, adding or removing almost any mob.
