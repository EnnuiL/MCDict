<img src="icon.png" align="right" width="180px"/>

# MCDict

## Port Information

This is an unofficial port of MCDict to Minecraft 1.17.1, made originally for 1.15.2 by LemmaEOF under The Cotton Project. This fork is meant to be exclusively used by Crooked Crooks, but you may use it on your mods.

This mod has been superseded by [Quilt Standard Libraries](https://modrinth.com/mod/qsl)'s Registry Entry Attachments API, which allows for the same functionality but with it being properly maintained and with more features. With that, this repository has been archived.

## Original Description

[>> Downloads <<](https://github.com/CottonMC/MCDict/releases)

*Drive your data further!*

**This mod is open source and under a permissive license.** As such, it can be included in any modpack on any platform without prior permission. We appreciate hearing about people using our mods, but you do not need to ask to use them. See the [LICENSE file](LICENSE) for more details.

MCDict adds a data-driven system for loading key-value pairs in Minecraft data packs. They follow a similar format and setup to tags for ease of understanding, and out of the box support `blocks`, `items`, `fluids`, and `entity_types`. A dict is structured as such:
```json
{
  "replace": false,
  "override": false,
  "values": {
    "minecraft:bricks": 5,
    "minecraft:iron_block": 3
  }
}
```

The `"replace"` tag works identical to `"replace"` in tags; if true, then this dict will delete existing values loaded from other dicts. If the `"override"` tag is true, then entries from higher-priority dicts will overwrite the values for entries in lower-priority dicts.

Information on registering new dicts will be added when I have time.
