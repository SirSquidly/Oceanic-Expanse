---
title: "Conch"
sidebar_image: "https://i.imgur.com/1kMYEPU.png"
---

A conch is a shell instrument dug from [Shelly Sand](../blocks/shelly-sand.md). It has four types, each playing a unique sound when used.

## Obtaining
### Shelly Sand
A conch has a rare chance of being gathered from Shelly sand by either using a shovel on, or directly digging, the block.

The Conch's Sound is then randomly chosen.

## Usage
When used, they play a loud horn sound, heard from _256 blocks away_⁽¹⁾.

The Pitch of the sound changes depending on the Player's head position, raising in pitch when looking up, and lowering when looking down. This only applies right when the sound begins, so sound pitch cannot be altered mid-playing.

Sneaking while using the Conch will always play it at the base pitch.

Conches have a cooldown of _~four seconds_⁽¹⁾ before they can be used again. This applies to all conches in the inventory.

## Item Data
* sound: The registry name of the conch sound.
* cooldown: Integer cooldown between uses.

The "sound" tag is which sound the Conch plays when used. This accepts any registered sound.

The "cooldown" tag controls the cooldown between uses of the Conch.

.

Using Commands, the player can spawn Conches with distinct sounds.
For example, the command:

> /give @p oe:conch 1 0 {Sound:ambient.cave,Cooldown:0}

Will give the nearest player a Conch that plays the Minecraft Cave sound, with zero cooldown.

1. ↑ Features are configurable.
