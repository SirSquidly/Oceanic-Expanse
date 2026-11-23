---
title: "Version 1.1.4"
sidebar_position: 5
---

1.1.4 is a minor bug fix update for Oceanic Expanse, released on August 12, 2024, which fixes various bugs including one critical server bug.

## Changes


### Items

#### [Trident](../items/trident.md)
* Keeps the original thrower recorded and read from the `TrueOwner` NBT tag.


### Mobs

#### [Lobster](../mobs/lobster.md)
* Lowered pickup delay to about 4 seconds

## Bugfixes
9 issues fixed

* [#93](https://github.com/SirSquidly/Oceanic-Expanse/issues/93) - Simplified Chinese Translations use the proper file extension
* [#94](https://github.com/SirSquidly/Oceanic-Expanse/issues/94) - Tridents properly save `pickupStatus`
* [#95](https://github.com/SirSquidly/Oceanic-Expanse/issues/95) - Sea Pickles correctly update their state when grown
* [#96](https://github.com/SirSquidly/Oceanic-Expanse/issues/96) - Added `TrueOwner` NBT to Tridents so Loyalty remembers their owner
* [#97](https://github.com/SirSquidly/Oceanic-Expanse/issues/97) - Lobsters should no longer cause a Server Crash
* Fixed Sponge Chunks trying to register when disabled via config, contributed by Boffinbox
* Fixed Wet Sponges being able to absorb water when full, contributed by Boffinbox
* Fixed Drown Conversion Table not being able to be disabled
* Fixed Lobsters not properly loading `MoltCooldown`
