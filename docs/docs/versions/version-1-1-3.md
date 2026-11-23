---
title: "Version 1.1.3"
sidebar_position: 6
---

1.1.3 is a minor bug fix update for Oceanic Expanse, released on July 20, 2024, adding multiple new languages and multiple server bug fixes.

## Additions

### Languages
* Added Simplified Chinese, contributed by SaltOvOFish

* Added LOLcat

* Added Pirate Speak

* Added Shakespearean English


## Changes


### Items

#### [Conduit Charm](../items/conduit-charm.md)
* Changed description text to Blue
* Description text now uses a Translation Key

#### [Spawn Bucket](../items/spawn-bucket.md)
* Changes Translation Key to use a format specifier, allowing proper translations


### Mobs

#### [Tropical Fish](../mobs/tropical-fish.md)
* Config defined unique names now use Translation Keys
  * Raw Strings are still accepted

## Bugfixes
5 issues fixed

* [#85](https://github.com/SirSquidly/Oceanic-Expanse/issues/85) - Crabs cause Server Crash... due to dancing
* [#87](https://github.com/SirSquidly/Oceanic-Expanse/issues/87) - Disabling Picked Heads causes Server Crash
* Fixed Orange-Black Lobsters not being properly called 'Halloween'
* Fixed PlayerJukeBoxEvent filling log with errors
* Fixed intense cascading worldgen when Shipwrecks attempt to generate in Lava Ocean worlds
