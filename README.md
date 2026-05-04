# Rough Beginnings

An early-game survival overhaul for NeoForge 1.21.1. Combines a heavily modified version of EarlyStage with Never Punching Trees, plus a few additions of my own.

## What it does

- **Hands off.** Most blocks need a tool to break — soft stuff (dirt family, sand, gravel, leaves, flowers, saplings, mushrooms, plants, torches, packed mud, salt blocks) you can still grab by hand. Configurable via tags and a config section.
- **Rocks everywhere.** Rocks scatter across every overworld biome on dirt or stone surfaces. Pick them up, place them as decoration (random size and facing on each placement), or throw them — 85% chance to drop back, 15% chance to shatter.
- **Flint tools.** Sword, shovel, pickaxe, axe, hoe. Stronger than wood, slightly weaker than stone, wooden mining tier (you'll still need to find a stone pickaxe to mine iron). Recipes need plant string.
- **Crafting Rock.** A 3×3 surface you knap by smacking with a rock. Accepts any vanilla crafting recipe — works as a primitive crafting table for when you can't make a real one yet.
- **Sieve.** Right-click with a sieveable block (dirt, sand, gravel, etc.) to load it, then keep right-clicking to process — composter-style probabilistic advancement, ~40% per click. Dust-plume and block particles, composter sounds. Drops useful early-game items defined per block in the data pack.
- **Plant Fiber & Plant String.** Fiber drops from short grass / tall grass / fern / large fern (10–15% chance). Twist 1 fiber into 2 strings — used for tool bindings.

## Compatibility

- **Immersive Weathering** — sandy dirt, silt, and grassy variants are sieve-able with themed loot.
- **Salt** (NeoForge port) — `salt:salt_block` is hand-breakable.
- **Still Life** — rocks generate in all 108 biomes via `#minecraft:is_overworld`.

## Configuration

`config/rough_beginnings-common.toml`:
- `requirePreferredTool` — must use the matching tool category (default false)
- `alwaysAllowInstaBreak` — instant-break blocks bypass the rule (default true)
- `forceInCreative` — apply the rule in creative mode too (default false)

For finer control over which blocks are hand-breakable or which items count as tools, edit the `rough_beginnings:breakable` block tag and the `rough_beginnings:canbreak` item tag via a datapack.

## License

MIT — see [LICENSE](LICENSE). Built on [EarlyStage](https://github.com/Globox1997/EarlyStage) by Globox_Z and [Never Punching Trees](https://www.curseforge.com/minecraft/mc-mods/never-punching-trees) by 96_Leaf, both MIT. See [ATTRIBUTIONS](ATTRIBUTIONS) for the full breakdown of what came from where.
