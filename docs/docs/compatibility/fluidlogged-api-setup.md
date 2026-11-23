---
title: "Setting Up Fluidlogged API Support"
---

**THIS TUTORIAL IS WRITTEN FOR OCEANICEXPANSE-1.0.0 AND FLUIDLOGGED-API-v2.1.2. ENSURE YOU ARE USING THESE VERSIONS.**

Oceanic Expanse uses many tricks to add its water-based blocks, such as kelp or seagrass, though players will still notice the limitations of these tricks. Utilizing [Fluidlogged API](https://www.curseforge.com/minecraft/mc-mods/fluidlogged-api) and a few config options though will remove these tricks and instead use the proper system created by Fluidlogged API. Oceanic Expanse's world generators are fully compatible with this setup, thus allowing water algae and plants to generate touching the water surface when fully setup!

## Basic Requirements
* You have a decent understanding of altering the config files for mods
* You have both this mod and Fluidlogged API installed, at the latest versions
* You have read through and understand most of the [Fluidlogged API page on the Config](https://github.com/jbredwards/Fluidlogged-API/wiki/Config)
* You have access to the `config` folder for your installation of Minecraft
* You do not blame me for any issues which may come about from doing this wrong. There shouldn't be any, but I am being safe.

## Process

1. Open your config folder

2. Open `Oceanic Expanse.cfg`

3. Find the option `disableBlockWaterLogic` and set it to true. This disables how Oceanic Expanse handles its water blocks by default, allowing Fluidlogged API to override it.

4. Save and close the file.

5. Open 'fluidlogged_api.cfg'

6. Find the option `whitelist` and within the brackets, add a list of every water block from Oceanic Expanse, along with any additional blocks you many want. 

Here is an example containing every non-solid block from Oceanic Expanse, feel free to copy and insert it.
`
whitelist:[
{blockId:"oe:seagrass"},
{blockId:"oe:tall_seagrass"},
{blockId:"oe:sea_pickle"},
{blockId:"oe:kelp"},
{blockId:"oe:palm_sapling"},
{blockId:"oe:coconut"},
{blockId:"oe:dulse"},
{blockId:"oe:seastar"},
{blockId:"oe:tube_sponge"},
{blockId:"oe:blue_coral"},
{blockId:"oe:pink_coral"},
{blockId:"oe:purple_coral"},
{blockId:"oe:red_coral"},
{blockId:"oe:yellow_coral"},
{blockId:"oe:blue_coral_fan"},
{blockId:"oe:pink_coral_fan"},
{blockId:"oe:purple_coral_fan"},
{blockId:"oe:red_coral_fan"},
{blockId:"oe:yellow_coral_fan"},
{blockId:"oe:blue_coral_dead"},
{blockId:"oe:pink_coral_dead"},
{blockId:"oe:purple_coral_dead"},
{blockId:"oe:red_coral_dead"},
{blockId:"oe:yellow_coral_dead"},
{blockId:"oe:blue_coral_fan_dead"},
{blockId:"oe:pink_coral_fan_dead"},
{blockId:"oe:purple_coral_fan_dead"},
{blockId:"oe:red_coral_fan_dead"},
{blockId:"oe:yellow_coral_fan_dead"}
],
`

7. Save and close the file.

8. Load your game and in a new world. Place any (not 2 block tall) water block from Oceanic Expanse at the water surface. 
If it places and is fluildlogged properly, you are done!
