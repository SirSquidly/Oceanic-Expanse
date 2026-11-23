---
title: "Version 1.1.5"
sidebar_position: 4
---

1.1.5 is a minor bug fix update for Oceanic Expanse, released on December 22, 2024, which fixes various bugs including one critical server bug.

## Changes

### Blocks

#### [Conduit](../blocks/conduit.md)
* Changed Hardness and Blast Resistance to 3
* Now drops when mined without a Pickaxe
  * Pickaxe is still the assigned tool


#### [Palm Log](../blocks/palm-log.md)
* Can now be smelted into Charcoal


#### [Palm Wood](../blocks/palm-wood.md)
* Can now be smelted into Charcoal

### Mobs

#### [Tropical Slime](../mobs/tropical-slime.md)
* Buffed spawn rates and adjusted spawning logic, making them far more common

## Bugfixes
12 issues fixed

* [#92](https://github.com/SirSquidly/Oceanic-Expanse/issues/92) - Crab and Lobster Meat are disabled if their relevant mob is also disabled
* [#98](https://github.com/SirSquidly/Oceanic-Expanse/issues/98) - Coquina no longer generates atop Trees
* [#100](https://github.com/SirSquidly/Oceanic-Expanse/issues/100) - Conduit Power works within Fluidlogged Blocks
* [#103](https://github.com/SirSquidly/Oceanic-Expanse/issues/103) - Smelting Recipes only register if relevant blocks/items do
* [#104](https://github.com/SirSquidly/Oceanic-Expanse/issues/104) - Palm Logs can be smelted for Charcoal
* [#105](https://github.com/SirSquidly/Oceanic-Expanse/issues/105) - Fish can no longer enter Boats
* [#106](https://github.com/SirSquidly/Oceanic-Expanse/issues/106) - Drowned use player swimming animations up and down
* [#107](https://github.com/SirSquidly/Oceanic-Expanse/issues/107) - Underwater Torch placement has been improved, no longer causes crashes
* [#108](https://github.com/SirSquidly/Oceanic-Expanse/issues/108) - Conduit Charm no longer kicks Clients
* [#110](https://github.com/SirSquidly/Oceanic-Expanse/issues/110) - Turtle Eggs properly spawn particles, no longer kicks Clients
* [#112](https://github.com/SirSquidly/Oceanic-Expanse/issues/112) - Conduits have a Harness, Resistance, and Tool properly set and always drop
* Fixed Tropical Slimes causing a log warning due to not having a frame 0 to their animation
