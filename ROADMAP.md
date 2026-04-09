# Isolated Island — Development Roadmap

## 🎯 Goal
Create a small, **playable** 2.5D pixel-art crafting and exploration game.
Focus on finishing a vertical slice first. If it's fun → expand. If not → adjust early.

---

## 🧪 MVP (Vertical Slice)
A minimal but complete playable loop:
- Player can move around the island
- Player can gather 1 resource (wood)
- Player has a simple inventory
- Player can craft 1 item (axe from wood)
- Player can complete 1 quest from 1 NPC

**This is the definition of done for v1. Everything else is bonus.**

---

## ✅ COMPLETED — Phase 0: Engine Foundation

### Core Engine
- [x] Game loop with fixed timestep (60 FPS)
- [x] Window management (GLFW + OpenGL)
- [x] Input handling (keyboard + mouse)
- [x] State management system (menu ↔ gameplay)
- [x] Logger with throttle and change-detection helpers

### Graphics & Rendering
- [x] Texture loading system (file-based PNG/JPG)
- [x] Procedural texture generation (fallback for prototyping)
- [x] Tile-based grid rendering with camera transform
- [x] Camera system (pan with WASD, zoom with scroll)
- [x] View culling (only renders visible tiles)
- [x] ViewportManager for responsive coordinate helpers
- [x] TextRenderer loading ttf resource (FiraSans)
- 
### UI System
- [x] Interface-based UI architecture (UIElement)
- [x] Anchor system for responsive positioning (UIAnchor)
- [x] UIBounds with mouse collision detection
- [x] UIButton with hover states and onClick callback
- [x] UIPanel (container with background)
- [x] UILabel with alignment and optional background
- [x] UIManager (root element list, reverse-order update)

### World System
- [x] Grid data structure (64×64 tile array)
- [x] TileType enum (GRASS, WATER, DIRT, STONE, SAND)
- [x] Starting island carved from water (SAND border → DIRT → GRASS center)
- [x] Camera starts centered on island

**🎮 What You Can Do Now:**
- Open the game to a main menu with Play/Settings/Exit
- Enter gameplay and see a small island surrounded by water
- Pan the camera with WASD/arrow keys
- Zoom in/out with scroll wheel

---

## 🔨 Phase 1: Player & Movement
**Goal: "I exist in the world and can move around"**
**Estimated time: 1 week**

### Tasks
- [x] Create `Player` class (position, size, speed)
- [x] Render player as a colored rectangle (no art needed yet)
- [x] Move player with WASD input
- [x] Camera follows player (smooth or snapped)
- [x] Basic tile collision — player cannot walk on WATER tiles

### Files to Create
- `game/entity/Player.java`

### Files to Modify
- `PlayState.java` — instantiate and update/render player

### Done When
Player appears on screen, moves with WASD, stops at water edges, and camera follows.

---

## 🌲 Phase 2: Resource Gathering
**Goal: "I can interact with the world"**
**Estimated time: 1 week**

### Tasks
- [ ] Add TREE tile type (or place tree objects on GRASS tiles)
- [ ] Detect when player is adjacent to a tree and presses interact key (E)
- [ ] Remove tree / change tile on gather
- [ ] Give player 1 wood item (hardcoded)
- [ ] Simple visual feedback (tile changes color or disappears)

### Notes
- Keep it hardcoded: `if (interactedTile == TREE) { inventory.add(WOOD); }`
- Do NOT build a generic resource system yet

### Done When
Player walks up to a tree, presses E, tree disappears, player has wood.

---

## 🎒 Phase 3: Inventory
**Goal: "I can hold items"**
**Estimated time: 2–3 days**

### Tasks
- [ ] Create `Inventory` class — simple list or int array of item counts
- [ ] Add/remove items by type
- [ ] Render a minimal inventory bar on screen (e.g., "Wood: 3")
- [ ] Wire up gathering from Phase 2 to actually store items

### Files to Create
- `game/inventory/Inventory.java`
- `game/inventory/ItemType.java` (enum: WOOD, STONE, AXE, etc.)

### Notes
- Display can just be a UILabel showing "Wood: X" — no fancy grid UI yet
- Do NOT build a full inventory UI with slots and drag-drop

### Done When
Player gathers wood, inventory count updates, number shows on screen.

---

## 🔨 Phase 4: Crafting
**Goal: "I can create something new"**
**Estimated time: 1 week**

### Tasks
- [ ] Hardcode 1 recipe: 3 Wood → 1 Axe
- [ ] Add craft action (press C or interact with a crafting tile)
- [ ] Check if player has required items
- [ ] Consume items, add result to inventory
- [ ] Show feedback ("Crafted: Axe!")

### Files to Create
- `game/crafting/CraftingSystem.java` (or just a method on PlayState initially)

### Notes
- Hardcode the recipe directly: `if (inventory.has(WOOD, 3)) { inventory.remove(WOOD, 3); inventory.add(AXE, 1); }`
- Do NOT build a recipe registry or data-driven crafting system yet

### Done When
Player has enough wood, presses craft, axe appears in inventory.

---

## 🧑‍🌾 Phase 5: NPC & Quest
**Goal: "I have a purpose"**
**Estimated time: 1–2 weeks**

### Tasks
- [ ] Create `NPC` class — position, name, hardcoded dialogue lines
- [ ] Render NPC as a distinct colored rectangle
- [ ] Player can walk up and press E to interact
- [ ] Show dialogue: "Bring me 10 wood and I'll open the northern path"
- [ ] Hardcode quest state: NOT_STARTED → IN_PROGRESS → COMPLETE
- [ ] On delivery: mark quest complete, unlock something (e.g., remove a STONE barrier tile)

### Files to Create
- `game/entity/NPC.java`
- `game/quest/Quest.java` (simple state enum + condition check)

### Notes
- Dialogue can be a single UILabel or a simple text panel — no dialogue tree
- One NPC, one quest, fully hardcoded
- "Unlock something" can be as simple as changing a tile from STONE to GRASS

### Done When
Player talks to NPC, gets quest, collects 10 wood, returns, quest completes, area unlocks.

---

## 🌍 Phase 6: Progression (World Expansion)
**Goal: "The world evolves as I play"**
**Estimated time: 1 week**

### Tasks
- [ ] Add a locked area (blocked by tiles or a visual barrier)
- [ ] Define an unlock condition (quest complete, item crafted, etc.)
- [ ] Add a new resource or tile type in the unlocked area
- [ ] Make the world feel slightly larger after progression

### Notes
- This can be as simple as a row of STONE tiles that get replaced with GRASS when the quest is done
- Do NOT build a generic area/zone system

### Done When
Completing the quest opens a new part of the island with something new to find.

---

## 🎨 Phase 7: Assets (Light Config — Only If Needed)
**Goal: "I can add content without changing code"**
**Only tackle this if Phase 1–6 are complete and repetition is painful.**

### Tasks
- [ ] Simple JSON or properties file mapping tile/item IDs to texture file paths
- [ ] Load texture assignments at startup instead of hardcoding strings

### Notes
- Do NOT build a full asset pipeline
- Do NOT add hot-reloading, asset bundles, or a resource manager abstraction

---

## 🧹 Phase 8: Refactor (Only If Needed)
**Only refactor when the pain of NOT refactoring is bigger than the pain of refactoring.**

Candidates (only if repeated):
- [ ] Extract quest logic into a reusable structure if you add a second quest
- [ ] Extract NPC dialogue into a shared pattern if you add a second NPC
- [ ] Clean up PlayState if it grows too large

---

## ⚠️ Anti-Scope Rules
Avoid adding these unless the MVP is fully complete and they genuinely increase fun:
- Large crafting trees or recipe systems
- Procedural world generation
- Complex dialogue or branching conversations
- Advanced pathfinding or AI
- Economy or trading systems
- Save/load (nice to have, but not MVP)
- Audio (nice to have, but not MVP)
- Isometric rendering

---

## ✅ Definition of Progress
Progress **is**:
- A playable feature that works in the game
- Something the player can see or interact with
- A step closer to the MVP loop

Progress **is NOT**:
- More abstraction layers
- A new manager or system with no gameplay effect
- Refactoring working code for cleanliness

---

## 📋 Immediate Priority Order

1. **Player movement** — exists in the world, moves with WASD
2. **Camera follow** — camera tracks the player
3. **Tile collision** — can't walk on water
4. **Resource gathering** — chop a tree with E key
5. **Inventory** — store and display wood count
6. **Crafting** — turn wood into an axe
7. **NPC + Quest** — talk to someone, get a goal, complete it
8. **World unlock** — quest completion opens something new

---

## 🧠 Guiding Rule
**If a feature does not make the game more fun immediately → delay it.**
Build the game, not the engine.