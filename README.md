# CrowCulling

A client-side Fabric mod for multiplayer servers that improves performance during player combat by hiding unnecessary players and lowering your render distance.

## What does it do?

On multiplayer servers, when you take damage from a player or deal damage to a player, the mod hides all players outside a distance you choose for 15 seconds and changes your render distance to the value you set. This aims to improve performance during crowded fights.

## How does it work?

Using the Mod Menu settings, you set a block distance and, optionally, a render distance. When you take damage from or deal damage to another player:

- For 15 seconds, players farther than your chosen block distance (0–25 blocks) are not rendered.
- If render distance changing is enabled, your render distance is set to the chosen value for 15 seconds (it can be lowered or raised).
- When the 15 seconds are over, or when you die, both player rendering and your render distance return to normal.

## Features

- Render distance changes to your chosen value for 15 seconds after dealing or taking damage from a player.
- Players beyond your chosen block distance (0–25 blocks) are not rendered during combat.
- If no damage is dealt or taken for 15 seconds, everything returns to normal.
- Everything also returns to normal when you die.

## Settings

> These settings can only be changed through Mod Menu.

- **Maximum block distance:** the distance within which other players are still rendered after damage is dealt or taken.
- **Render distance during combat:** the render distance applied after damage is dealt or taken.
- **Toggle render distance changing:** enable or disable the render distance change.

## Requirements

- Minecraft 1.21.11 (other versions may come in the future)
- Java 21 or newer
- Fabric Loader 0.18.0 or newer
- Fabric API (built and tested with 0.141.3+1.21.11)
- Mod Menu (recommended, needed to change the settings)

## Installation

1. Install Fabric Loader and Fabric API for Minecraft 1.21.11.
2. (Recommended) Install Mod Menu.
3. Put the CrowCulling `.jar` file into your `mods` folder.

## AI Disclosure

I  AI assistance for the parts I struggled with. However, most of the code, the idea, the design, the settings, the testing and the icon were made by me, a human.

## License

MIT
