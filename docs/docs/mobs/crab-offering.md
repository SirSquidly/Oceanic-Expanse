---
title: "Crab Offering"
---

Offering is a feature that allows players to feed [crabs](../mobs/crab.md) to possibly obtain their held item.

## Mechanics

### Digging
Adult crabs standing atop any valid blocks⁽¹⁾ have a 1⁄1000 chance every tick to dig for an item. After 3 seconds of digging, a random item from the relevant digging loot table is given to them in their main hand. If they already have a held item when digging, they will instead remove the item. If a crab without any held item wanders atop an unsupported block mid-animation, this will cancel the digging. Crabs holding items will not dig if a nearby player is holding a fish.

### Eating
Adult Crabs holding an item then can be offered to by picking up dropped fish, or by the player using a fish on them. The crab will hold the fish for 3 seconds, before consuming it, and has a 50% chance to drop the held item. Consuming the fish restores as much of the crab's health as it would restore hunger points when eaten by the player.

If a player uses a fish a crab that has no held item, it will still take and eat the fish.


## Digging Loot tables⁽¹⁾
_For the loot table prior to 1.1.0, see [Crab/Digging](https://github.com/SirSquidly/Oceanic-Expanse/wiki/Crab/a19d8363caa501bf8f0c92edd5c0363754af7e39#digging)_

The block an adult crab finishes the digging animation atop determines which loot table they will pull from. 

### Gravel
| Item             | Weight | Chance |
| ---------------- | ------ | ------ |
| Tripwire Hook | ¹³⁄₅₀     | 26%     |
| Iron Nugget | ¹³⁄₅₀     | 26%     |
| Flint | ⁹⁄₅₀     | 18%    |
| Prismarine Crystals | ⁹⁄₅₀     | 18%     |
| Emerald | ⁵⁄₅₀     | 10%     |
| Enchanted Leather Boots | ¹⁄₅₀     | 2%     |



### Sand
| Item             | Weight | Chance |
| ---------------- | ------ | ------ |
| Paper | ¹³⁄₅₀     | 26%     |
| [Palm Sapling](../blocks/palm-sapling.md) | ¹³⁄₅₀     | 26%     |
| Stick | ⁹⁄₅₀     | 18%    |
| Iron Nugget | ⁹⁄₅₀     | 18%     |
| Gold Nugget | ⁵⁄₅₀     | 10%     |
| Enchanted Fishing Rod | ¹⁄₅₀     | 2%     |


### Red Sand
| Item             | Weight | Chance |
| ---------------- | ------ | ------ |
| Rotten Flesh | ¹³⁄₅₀     | 26%     |
| Clay Ball | ¹³⁄₅₀     | 26%     |
| Gold Nugget | ⁹⁄₅₀     | 18%    |
| Gunpowder | ⁹⁄₅₀     | 18%     |
| Dead Bush | ⁵⁄₅₀     | 10%     |
| Enchanted Golden Shovel | ¹⁄₅₀     | 2%     |

### Shelly Sand
| Item             | Weight | Chance |
| ---------------- | ------ | ------ |
| [Barnacle Shells](../items/barnacle-shells.md) | ¹³⁄₅₀     | 26%     |
| [Sea Star](../blocks/sea-star.md) | ¹³⁄₅₀     | 26%     |
| Glass Bottle | ⁹⁄₅₀     | 18%    |
| [Turtle Scute](../items/scute.md) | ⁹⁄₅₀     | 18%     |
| [Conch](../items/conch.md) | ⁵⁄₅₀     | 10%     |
| Enchanted Bow | ¹⁄₅₀     | 2%     |

### Soul Sand
| Item             | Weight | Chance |
| ---------------- | ------ | ------ |
| Bone | ¹³⁄₅₀     | 26%     |
| String | ¹³⁄₅₀     | 26%     |
| Nether Brick | ⁹⁄₅₀     | 18%    |
| Fire Charge | ⁹⁄₅₀     | 18%     |
| Gold Nugget | ⁵⁄₅₀     | 10%     |
| Enchanted Stone Sword | ¹⁄₅₀     | 2%     |

## History
| Version | Changes |
| ------- | ------- |
| 1.0.0 | Added Crab Offering. |
| [1.1.0](../versions/version-1-1-0.md) | Revamped the crab offering system with separate loot tables depending on the dug block, each with different items, and can be easily configured. |

***
1. ↑ All valid dig blocks and their loot tables are defined in the config.
