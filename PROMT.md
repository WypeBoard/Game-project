# Project Context: Isolated Island (2.5D Java/LWJGL Game)

## Project Overview
I'm building a small-scale 2.5D pixel-art exploration and crafting game from scratch using Java and LWJGL (Lightweight Java Game Library). The goal is learning game development fundamentals while actually finishing a playable experience. This is NOT a transport tycoon, factory builder, or large-scale simulation — it's a focused, small-scope RPG-lite game.

## Game Vision
A minimal, focused exploration and crafting experience:
- Player explores a small island
- Gathers resources (wood, stone, etc.)
- Crafts simple tools
- Interacts with NPCs
- Unlocks new areas of the island

The game is about **feel, exploration, and progression**, not complexity or systems depth.

## Technical Stack
- **Language:** Java
- **Graphics Library:** LWJGL 3.3.6 (OpenGL, GLFW, STB)
- **Build System:** Gradle
- **Target:** Desktop (Windows native currently configured)
- **Rendering:** OpenGL with immediate mode (glBegin/glEnd)
- **Coordinate System:** Top-down 2D tiles

## Architecture Decisions Made

### Design Philosophy
- **Gameplay First:** Every feature must create a player-facing experience. If it doesn't affect the player directly, delay it.
- **Hardcode First → Generalize Later:** Start with simple, hardcoded implementations. Only abstract when repetition actually appears.
- **Small Scope:** Prefer a small, polished experience over large unfinished systems.
- **Avoid Overengineering:** Do NOT build flexible systems before they are needed. A system that exists but isn't used is waste.

### Design Patterns
- **Singletons:** GameLoop, WindowManager, InputManager, MouseManager, GameStateManager, UIManager
- **State Pattern:** GameState abstract class with MainMenuState and PlayState implementations
- **Interface-based UI:** UIElement interface with final implementations (UIButton, UIPanel, UILabel)
- **Composition over Inheritance:** Avoided abstract classes where possible; prefer interfaces and final classes
- **Component-based:** UIBounds as a reusable component for position/collision

### Core Systems Implemented

#### 1. Game Loop (GameLoop.java)
- Fixed timestep at 60 FPS using accumulator pattern
- Separates update (logic) from render (graphics)
- Manages WindowManager, InputManager, MouseManager, GameStateManager
- Clean init → loop → cleanup lifecycle

#### 2. Graphics System
- **WindowManager:** GLFW window creation, OpenGL context management, resizable viewport
- **Texture:** Wrapper around OpenGL texture IDs with bind/unbind
- **TextureLoader:** Uses STB to load PNG/JPG files into OpenGL textures
- **TileTextureManager:** Manages tile texture variations (e.g., grass_01.png, grass_02.png)
- **ProceduralTextureGenerator:** Generates textures at runtime for prototyping (no art files needed)
- **ViewportManager:** Tracks window dimensions; provides coordinate helpers for responsive UI

#### 3. Input System
- **InputManager:** Keyboard state tracking (pressed, justPressed, justReleased)
- **MouseManager:** Mouse position, button states, scroll wheel, delta movement
- Both update once per frame to clear single-frame events

#### 4. State Management
- **GameState:** Abstract base with init/update/render/cleanup lifecycle
- **GameStateManager:** Handles transitions between states with onEnter/onExit
- **MainMenuState:** Menu with clickable buttons
- **PlayState:** Main gameplay with grid world

#### 5. UI System (Interface-Based)
- **UIElement:** Interface defining update/render/visible/enabled
- **UIAnchor:** Anchor system for responsive element positioning (center, topLeft, bottomRight, etc.)
- **UIBounds:** Position and collision detection component
- **UIButton:** Clickable button with hover states, color changes, onClick callback, anchor support
- **UIPanel:** Container for child elements with background rendering
- **UILabel:** Text display with alignment options, optional background, anchor support
- **UIManager:** Manages root UI elements, updates in reverse order for click priority

#### 6. World/Grid System
- **Grid:** 2D array of Tiles with width/height
- **Tile:** Grid position (x,y), TileType enum, texture variation index
- **TileType:** Enum with GRASS, WATER, DIRT, STONE, SAND + fallback RGB colors
- **Camera:** World position, zoom (0.5–3.0x), screen-to-world coordinate conversion
- **GridRenderer:** Renders visible tiles with camera transformation and view culling

#### 7. Utilities
- **Logger:** Debug logging with throttling and change-detection helpers to avoid log spam
- **Constants:** Shared constants (window size, title)

### Key Features Implemented
✅ 60 FPS game loop with fixed timestep
✅ Window management and OpenGL context
✅ Keyboard and mouse input handling
✅ State transitions (menu ↔ gameplay)
✅ Anchor-based responsive UI system
✅ Tile-based grid world (currently 64×64 tiles)
✅ Starting island carved from water (SAND/DIRT/GRASS/WATER layout)
✅ Camera pan (WASD/arrows), zoom (scroll wheel)
✅ Texture loading from files (PNG/JPG) with procedural fallback
✅ Tile variations (multiple textures per tile type)
✅ View culling (only renders visible tiles)
✅ Text rendering (FiraSans bitmap fonts)

### File Structure
```
project/
├── build.gradle
└── src/main/
    ├── java/org/code/
    │   ├── Main.java
    │   ├── engine/
    │   │   ├── GameLoop.java
    │   │   ├── graphics/
    │   │   │   ├── WindowManager.java
    │   │   │   ├── Texture.java
    │   │   │   ├── TextureLoader.java
    │   │   │   ├── TextRenderer.java
    │   │   │   ├── TileTextureManager.java
    │   │   │   ├── ViewportManager.java
    │   │   │   └── ProceduralTextureGenerator.java
    │   │   │   └── world/
    │   │   │       ├── Camera.java
    │   │   │       ├── Grid.java
    │   │   │       ├── GridRenderer.java
    │   │   │       ├── Tile.java
    │   │   │       └── TileType.java
    │   │   ├── input/
    │   │   │   ├── InputManager.java
    │   │   │   └── MouseManager.java
    │   │   ├── state/
    │   │   │   ├── GameState.java (abstract)
    │   │   │   ├── GameStateManager.java
    │   │   │   ├── MainMenuState.java
    │   │   │   └── PlayState.java
    │   │   └── ui/
    │   │       ├── UIElement.java (interface)
    │   │       ├── UIAnchor.java
    │   │       ├── UIBounds.java
    │   │       ├── UIButton.java
    │   │       ├── UIPanel.java
    │   │       ├── UILabel.java
    │   │       └── UIManager.java
    │   └── utils/
    │       ├── Constants.java
    │       └── Logger.java
    └── resources/
        └── assets/textures/tiles/
            ├── grass_01.png (optional)
            └── ... (procedural fallback if missing)
```

## Current Capabilities
The game currently runs with:
- A main menu with Play/Settings/Exit buttons
- A 64×64 tile world with a small starting island (water surrounds a SAND/DIRT/GRASS interior)
- Camera that starts centered on the island
- Procedurally generated tile textures (grass, water, dirt, stone, sand)
- Camera pan with WASD/arrow keys, zoom with scroll wheel
- Escape to return to menu (planned, not yet wired)

## What Does NOT Exist Yet
❌ Player entity (no character on screen)
❌ Player movement
❌ Collision detection
❌ Resource gathering
❌ Inventory system
❌ Crafting
❌ NPCs or dialogue
❌ Quests
❌ Save/load
❌ Audio
❌ Real bitmap font rendering (text is placeholder blocks)
❌ Entity system

## Important Constraints for Future Conversations

### When Suggesting Features:
- Every suggestion must directly make the game more fun or playable
- Hardcode first — no frameworks, no data-driven config until repetition demands it
- Keep classes small and focused
- Avoid suggesting systems the game doesn't need yet
- Fit into the existing singleton/state/interface architecture

### When Discussing Rendering:
- Using immediate mode OpenGL (glBegin/glEnd) — this is intentional for simplicity
- All textures use GL_NEAREST filtering (pixel art friendly)
- Camera uses matrix transformations (push/pop matrix pattern)
- Projection is orthographic, Y-down (top-left is origin)

### When Discussing Architecture:
- Don't break existing singleton patterns
- New gameplay logic belongs in PlayState or a dedicated manager called from it
- Reusable systems go in engine/; game-specific code goes in game/
- Avoid abstract base classes for gameplay objects unless clearly needed

## Coordinate Systems
1. **Grid Coordinates:** Logical 2D array indices (0–63, 0–63)
2. **World Coordinates:** Pixel positions (grid × tileSize, where tileSize = 32px)
3. **Screen Coordinates:** Window pixels (origin top-left)
4. **Texture UV Coordinates:** Normalized 0.0–1.0

## Terminology Used
- **Tile:** A single grid cell (32×32 pixels)
- **Entity:** Objects that exist in the world but aren't tiles (player, NPCs, resources) — not yet implemented
- **Variation:** Different texture for same tile type (grass_01, grass_02, etc.)
- **UV Coordinates:** Texture coordinates (0.0–1.0 range)
- **View Culling:** Only rendering visible tiles for performance
- **Anchor:** A UIAnchor that positions UI elements relative to screen edges or center

## Questions to Ask at Start of Each Session
- "What feature are you working on from the roadmap?"
- "What's working and what's broken?"
- "What felt fun or frustrating last session?"
- "Are you using procedural textures or real art files?"