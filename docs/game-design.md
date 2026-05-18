# GAME_DESIGN.md

## High Concept
Journey, is the name of this game repository.
You play as a journeying knight/sorcerer, traveling through the land.
Meeting people of the land, gathering people for you course, through interactions and questing.
Fighting battles to reach the goal.

### Core fantasy

### What makes the game unique

### Emotional/player experience goals

---

# Vision

## Design Pillars
- 3–5 core principles guiding all decisions

Examples:
- meaningful exploration
- tactical combat
- readable systems
- player-driven progression

---

## Inspirations
### Games

The main inspiration of this game is "Heroes of might and magic" (HoMM) series. Specifically 2 and 3.

Many of the aspects of this game has a direct link to said series. 
But offers some different takes on the world and the combat.

Copying:
- Hexagon combat
- Hero main character
- Army linked to hero
- Movement style of tile-based movement in "open world"
- Items
- Spells
- Dome movement penalty
- interacting with building in the world (with a twist)
- Turn based "open world"
- Turn based combat simulation


Not copying:
- Castle buildings
- Hero being able to cast any and all spells
- Sea based movement


### Art style references

Style is not fully decided on. But will be closer to pixelart compared to it's main inspiration.

---

# Player Experience

## Intended Feel
### pacing
### mood
### tension
### progression feel
### power curve


---

## Target Audience
- who the game is for: People who has played HoMM before. Me, my friends. Game is not expected to be sold. Just learning experience 
- expected player familiarity: People who has played HoMM before.
- niche vs broad appeal: TBD

---

# Core Gameplay Loop

## Primary Loop
- Explore
- Gather resources / artifacts
- Quests
- Fight (turn based)
- Upgrade hero via EXP
- Progress
- Repeat

---

## Secondary Loops
### side progression
TBD
### collectibles
Artifacts - Provides buff to the hero, that can affect the army units.

### quests
Hero will travel the world, meeting people (buildings). 

These people will be essential for the progress.

People will provide access to more units. 

Some will be provided upon meet. (Lower tier units generally)

Others will be provided upon completion of a quest and returning to the person requesting it.

Quests:
- Provide resource
- Clear pathing
- TBD

### faction reputation
Doubt, but TBD

### crafting
None!

### economy
Resources:
- Lumber
- Iron
- Gems
- TBD

---

# Gameplay Modes

## Exploration Mode
### movement
Tile based movement.
Each move takes a certain amount of movement points.
If not enough movement points are available for a move. The resource is depleted until next turn
### interaction
### world traversal
### discovery
### events

---

## Combat Mode
### turn structure
### initiative
### victory conditions
### unit control
### combat flow

---

## Transition Between Modes
### when combat starts
### when combat ends
### persistence between modes

---

# World Design

## Setting
### world overview
### themes
### tone
### factions
1 initially.
### regions
Different biomes should be available.
- Grass
- Dirt
- Sand (movement penalty)
- Water (non-traversable)

---

## Lore
### important history
### major conflicts
### mythology

---

## World Structure
### overworld
### procedural vs handcrafted
### biome structure
### progression gating

---

# Character Design

## Player Character
### role
### abilities
### progression
### customization

---

## Companion System
### recruitment
### progression
### relationship systems
### party management

---

## Enemy Design
### enemy categories
### AI philosophy
### scaling philosophy

---

# Combat Design

## Combat Philosophy
### tactical vs fast-paced
tactical - As from HoMM series.
### deterministic vs chaotic
### punishment/reward balance

---

## Turn Structure
### initiative
### action economy
### movement rules

---

## Abilities & Skills
### categories
### cooldowns/resources
### synergies

---

## Damage & Stats
### health
### armor
### resistances
### scaling rules

---

## Units
### Mage
Unlike the hero, the Mage will have access to cast different types of spells on their turn.

Can attack like a ranged unit

The number of spells are based on the base mage, or upgraded mage. Plus the stack size

Base mage:
- Fireball (can burn?, weak to water units)
- Magic arrow (consistent)
- enemy slow

stacked mage:
- Fireblast (can burn?, weak to water units, can hit multiple enemies)
- Magic arrows (casts 2 arrows)

Upgraded mage:
- Same as base mage
- Lightning strike (weak to ground/stone units)

Stacked upgraded mage:
- Same as stacked mage
- Chain lightning (weak to ground/stone units)

### Knight
Can inspire ally units, to increase damage output and defence. But takes up a turn

### Peasant
Weak, tier 1 unit

### Bowman
Ranged unit

---

## Status Effects
### buffs
### debuffs
### duration rules

---

# Progression Systems

## Character Progression
### levels
### stats
### skills
### classes/specs

---

## Equipment
### rarity philosophy
### item categories
### upgrade systems

---

## Economy
### currencies
### vendors
### rewards
### balancing philosophy

---

# Exploration Systems

## Map Design
### navigation
### fog of war
Doubt. TBD
### secrets

### landmarks

---

## Quests & Events
### event structure
### branching choices
### random encounters

---

## Discovery Rewards
### loot
### lore
### companions
### unlocks

---

# AI Design

## Enemy AI
### aggression rules
### tactical behavior
### difficulty philosophy

---

## NPC Behavior
### schedules
### interactions
### reactions

---

# UI/UX

## Interface Philosophy
### readability
### minimalism vs density
### accessibility

---

## HUD
### combat HUD
### exploration HUD
### contextual information

---

## Menus
### inventory
### map
### character sheets
### settings

---

# Audio Design

## Music
### tone
### adaptive systems
### exploration/combat themes

---

## Sound Effects
### combat feedback
### UI feedback
### ambient audio
Yes. Good luck mate!

---

# Visual Design

## Art Direction
### style references
### color philosophy
### visual readability

---

## Animation Philosophy
### responsiveness
### readability
### spectacle level

---

## Effects
### particles
### spell effects
Yes. TBD
### environmental effects

---

# Technical Design Constraints

## Performance Goals
### target FPS
Most likely 60. Given the art style any more will not provide any useful experience.

### memory considerations
### loading philosophy

---

## Save System
### save structure
TBD
### persistence rules
TBD
### compatibility goals

---

## Modding Support (Optional)
### exposed systems
### scripting support
### data-driven design goals
---

# Content Pipeline

## Asset Workflow
### naming conventions
### folder organization
### import pipeline

---

## Data Design
### JSON/XML/etc
### balancing workflow
### localization strategy

---

# Balancing Philosophy

## Difficulty Philosophy
### intended challenge
### failure punishment
### recovery mechanics

---

## RNG Philosophy
### deterministic systems
### randomness boundaries
### fairness rules

---

# Scope Management

## Must-Have Features
### required for MVP

---

## Nice-To-Have Features
### optional/stretch goals

---

## Explicit Non-Goals
Important:
What the game intentionally will NOT include.

Examples:
- multiplayer
- base building
- crafting
- open-world simulation

---

# Development Notes

## Current Priorities
### active development focus

---

## Known Risks
### technical risks
### design risks
### scope risks

---

# Future Ideas

## Potential Expansions
### systems
### regions
### mechanics
### game modes

---

# References

## Related Documents
- AGENTS.md

---

# Appendix

## Terminology
### internal naming
### gameplay terms
### abbreviations

---

## Open Questions
### unresolved design decisions
### experiments to test