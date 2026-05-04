# Rough Beginnings — Changelog

## V1.0.0
First public release. NeoForge 1.21.1.

### Block-breaking restriction (Never Punching Trees logic, refined)
- Hand-breaking blocked unless the block is in `rough_beginnings:breakable` or the held item is in `rough_beginnings:canbreak`.
- Default breakable list: dirt family (including mud, packed mud, muddy mangrove roots, moss block — fixed inconsistency where packed mud was breakable but mud wasn't), sand family, leaves, torches, flowers, saplings, mushrooms, short grass, tall grass, fern, large fern, plus common-tag fallbacks for `#c:sands` and `#c:gravels`.
- `[blockBreaking]` config section: `requirePreferredTool`, `alwaysAllowInstaBreak`, `forceInCreative`.

### EarlyStage-derived content
- **Rock** generates across every overworld biome (`#minecraft:is_overworld`) on dirt or stone surfaces. Four variants × four facings, weighted small > medium > large > extra-large. Random variant + facing on every player placement too — no two placed rocks look the same.
- **Throwable rocks**: snowball-style throw, 2 damage on hit. 85% drop / 15% shatter on impact. Snowball-throw sound on release; stone-hit thud on drop, stone-break burst on shatter.
- **Flint tools** — sword, shovel, pickaxe, axe, hoe. Stats: 100 uses, 3.0 mining speed, +1.0 attack damage bonus, enchantability 5. Wooden mining tier (can't mine iron ore — keeps the stone-pick gate intact). Recipes require plant string.
- **Crafting Rock** — 3×3 knap-to-craft block. Accepts any vanilla crafting recipe (no input/output filter). Tooltip styled to match Create's `Hold [Shift] for Summary`. No durability — you keep your crafting rock.
- **Sieve** — composter-style processing block. ~40% advancement chance per click, ~6 successful advances to drop loot. Dust-plume + block particles, composter sounds. Sieve drops defined per block in `data/rough_beginnings/sieve_drops/`.

### New
- **Plant Fiber** drops from short grass (10%), tall grass (15%), fern (10%), large fern (15%) via `BlockDropsEvent`.
- **Plant String** crafted shapeless from 1 plant fiber → 2 strings.

### Compatibility patches
- **Immersive Weathering**: sieve drops for `sandy_dirt`, `grassy_sandy_dirt`, `silt`, `grassy_silt` with themed loot tables.
- **Salt** (NeoForge port): `salt:salt_block` added to `rough_beginnings:breakable`.
- **Still Life**: covered automatically — Still Life adds its 108 biomes to `#minecraft:is_overworld`.

### Removed from upstream
- All bark items, all bucket items (wooden, water, brick, lava brick, clay), stone shears, wooden shield, redstone sieve.
- All steel content (block, ingot, nugget, tools, armor, horse armor).
- Beginner death-count system — death is fully vanilla.
- ModMenu, Cloth Config, EMI plugin, AutoConfig, Jankson — replaced with NeoForge `ModConfigSpec`.
- Deep blast-furnace mixins (extra-slot feature).
- Compat hooks for Fabric-only mods (Dehydration, Oblivion, Natures Spirit, Regions Unexplored, Terrestria, Meadow, Beachparty, BigGlobe, This Rocks, TreeChop, FallingTree, Missing Wilds, ModMenu, environmentz, dehydration).

### Credits
- [EarlyStage](https://github.com/Globox1997/EarlyStage) by Globox_Z (MIT)
- [Never Punching Trees](https://github.com/96Leaf/NeverPunchingTrees) by 96_Leaf (MIT)

See [ATTRIBUTIONS](ATTRIBUTIONS) for what was kept, modified, or removed from each.
