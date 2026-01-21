# Project Context: 2.5D Transport Tycoon-Style Game in Java/LWJGL

## Project Overview
I'm building a 2.5D transport tycoon-style game from scratch using Java and LWJGL (Lightweight Java Game Library). The goal is learning game development fundamentals, not financial gain, which is why I chose to build my own engine rather than use Unity or Godot.

## Technical Stack
- **Language:** Java
- **Graphics Library:** LWJGL 3.3.6 (OpenGL, GLFW, STB)
- **Build System:** Gradle
- **Target:** Desktop (Windows native currently configured)
- **Rendering:** OpenGL with immediate mode (glBegin/glEnd)
- **Coordinate System:** Top-down 2D tiles with future isometric consideration

## Architecture Decisions Made

### Design Patterns
- **Singletons:** GameLoop, WindowManager, InputManager, MouseManager, GameStateManager, UIManager
- **State Pattern:** GameState abstract class with MainMenuState and PlayState implementations
- **Interface-based UI:** IUIElement interface with final implementations (UIButton, UIPanel, UILabel)
- **Composition over Inheritance:** Avoided abstract classes where possible, prefer interfaces and final classes
- **Component-based:** UIBounds as a reusable component for position/collision

### Core Systems Implemented

#### 1. Game Loop (GameLoop.java)
- Fixed timestep at 60 FPS using accumulator pattern
- Separates update (physics) from render (graphics)
- Manages WindowManager, InputManager, MouseManager, GameStateManager
- Clean init → loop → cleanup lifecycle

#### 2. Graphics System
- **WindowManager:** GLFW window creation, OpenGL context management, 800×600 viewport
- **Texture:** Wrapper around OpenGL texture IDs with bind/unbind
- **TextureLoader:** Uses STB to load PNG/JPG files into OpenGL textures
- **TileTextureManager:** Manages tile texture variations (e.g., grass_01.png, grass_02.png, grass_03.png)
- **ProceduralTextureGenerator:** Generates textures at runtime for prototyping (no art files needed)

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
- **IUIElement:** Interface defining update/render/visible/enabled
- **UIBounds:** Position and collision detection component
- **UIButton:** Clickable button with hover states, color changes, onClick callback
- **UIPanel:** Container for child elements with background rendering
- **UILabel:** Text placeholder (not yet rendering actual text)
- **UIManager:** Manages root UI elements, updates in reverse order for click priority

#### 6. World/Grid System
- **Grid:** 2D array of Tiles with width/height
- **Tile:** Grid position (x,y), TileType enum, texture variation index
- **TileType:** Enum with GRASS, WATER, DIRT, STONE, SAND + fallback RGB colors
- **Camera:** World position, zoom (0.5-3.0x), screen-to-world coordinate conversion
- **GridRenderer:** Renders visible tiles with camera transformation and view culling

### Key Features Implemented
✅ 60 FPS game loop with fixed timestep  
✅ Window management and OpenGL context  
✅ Keyboard and mouse input handling  
✅ State transitions (menu ↔ gameplay)  
✅ UI system with buttons and panels  
✅ Tile-based grid (currently 100×100 tiles)  
✅ Camera pan (WASD/arrows), zoom (scroll wheel), drag (middle mouse)  
✅ Texture loading from files (PNG/JPG)  
✅ Procedural texture generation for prototyping  
✅ Tile variations (multiple textures per tile type)  
✅ View culling (only renders visible tiles)  
✅ Click interaction (click tiles to change type)

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
    │   │   │   ├── TileTextureManager.java
    │   │   │   └── ProceduralTextureGenerator.java
    │   │   ├── input/
    │   │   │   ├── InputManager.java
    │   │   │   └── MouseManager.java
    │   │   ├── state/
    │   │   │   ├── GameState.java (abstract)
    │   │   │   ├── GameStateManager.java
    │   │   │   ├── MainMenuState.java
    │   │   │   └── PlayState.java
    │   │   └── ui/
    │   │       ├── IUIElement.java (interface)
    │   │       ├── UIBounds.java
    │   │       ├── UIButton.java
    │   │       ├── UIPanel.java
    │   │       ├── UILabel.java
    │   │       └── UIManager.java
    │   ├── game/world/
    │   │   ├── Camera.java
    │   │   ├── Grid.java
    │   │   ├── GridRenderer.java
    │   │   ├── Tile.java
    │   │   └── TileType.java (enum)
    │   └── utils/
    │       └── Constants.java
    └── resources/textures/tiles/
        ├── grass_01.png (optional)
        ├── grass_02.png (optional)
        └── ... (or use procedural generation)
```

## Current Capabilities
The game currently runs with:
- A main menu with Play/Settings/Exit buttons (mouse or keyboard navigation)
- Gameplay state with 100×100 tile grid
- Procedurally generated textures (grass, water, dirt, stone, sand with variations)
- Full camera controls (pan with WASD, zoom with scroll, drag with middle mouse)
- Click tiles to cycle through tile types
- Escape to return to menu

## Design Philosophy Choices

### Why Interface-Based UI?
- **Flexibility:** Can implement IUIElement however needed
- **Final Classes:** All UI components are final to prevent inheritance complexity
- **Composition:** UIBounds is a component any element can use
- **No Abstract Headaches:** Avoided abstract class constraints

### Why Procedural Textures?
- **Rapid Prototyping:** Test systems without creating art assets
- **Zero Dependencies:** No need for external files during development
- **Educational:** Learn texture generation and OpenGL texture handling
- **Fallback:** Can switch to file-based textures anytime

### Coordinate Systems Used
1. **Grid Coordinates:** Logical 2D array indices (0-99, 0-99)
2. **World Coordinates:** Pixel positions (grid × tileSize = 32px)
3. **Screen Coordinates:** Window pixels (0-800, 0-600)
4. **Texture UV Coordinates:** Normalized 0.0-1.0 for texture mapping

### Rendering Pipeline
```
GameLoop.render()
  └─> GameStateManager.render()
      └─> PlayState.render()
          └─> GridRenderer.render(camera)
              ├─> Apply camera transform (translate, scale, translate)
              ├─> Calculate visible tile range (culling)
              ├─> For each visible tile:
              │   ├─> Get texture from TileTextureManager
              │   ├─> Bind texture
              │   ├─> Draw quad with UV coordinates
              │   └─> Unbind texture
              └─> Draw grid lines
```

## Known Limitations / TODOs
❌ No text rendering (UI shows colored rectangles, no labels)  
❌ No entity system (can't place buildings/vehicles yet)  
❌ No pathfinding  
❌ No economy/money system  
❌ No save/load functionality  
❌ No audio  
❌ No time/tick system  
❌ UILabel doesn't render actual text yet

## Next Development Phase
According to the roadmap, the next priorities are:
1. **Text Rendering** - Bitmap fonts to display labels, numbers, debug info
2. **Entity System** - Buildings and objects that exist on tiles
3. **Economy System** - Money tracking, costs, income

## Important Context for Future Conversations

### When Discussing New Features:
- Prefer composition over inheritance
- Keep classes final when possible
- Use interfaces for polymorphism
- Maintain singleton pattern for managers
- Consider performance (view culling, texture batching)
- Avoid large framework such as Spring

### When Discussing Rendering:
- We're using immediate mode OpenGL (glBegin/glEnd) for simplicity
- All textures use GL_NEAREST filtering (pixel art friendly)
- Camera uses matrix transformations (push/pop matrix pattern)
- Rendering order matters for isometric (back-to-front)

### When Discussing Architecture:
- Don't break existing singleton patterns
- New systems should integrate with GameLoop's update/render cycle
- State-specific logic goes in GameState subclasses
- Reusable logic goes in managers/utilities

### Isometric Considerations:
- Currently top-down orthographic
- Isometric is planned but not yet implemented
- Coordinate transformation vs. isometric tile art still being considered
- Rendering order becomes critical for isometric (Y-sorting)

## Game Vision
A 2.5D transport tycoon-style game where players:
- Build transportation networks (roads, rails, routes)
- Manage vehicles (trucks, trains, ships)
- Connect industries to cities
- Earn money through deliveries
- Expand their empire

More focused on gameplay and learning than on being a direct Transport Tycoon clone.
The game is not created with the intent of being sold. Purely learning

## Terminology Used
- **Tile:** A single grid cell (32×32 pixels)
- **Entity:** Objects on tiles (buildings, vehicles) - not yet implemented
- **Artifact:** Code examples/files shown in this conversation
- **Variation:** Different texture for same tile type (grass_01, grass_02, etc.)
- **UV Coordinates:** Texture coordinates (0.0-1.0 range)
- **View Culling:** Only rendering visible tiles for performance

## Questions to Ask Me
When starting a new conversation, feel free to ask:
- "What feature are you working on from the roadmap?"
- "Are you using procedural textures or have you created art files?"
- "What's working and what's broken?"
- "Which phase of the roadmap are you in?"