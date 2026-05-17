# AGENTS.md

## Project Overview
Journey, is the name of this game repository.
You play as a journeying knight/sorcerer, traveling through the land. 
Meeting people of the land, gathering people for you course, through interactions and questing. 
Fighting battles to reach the goal.

The game is heavily inspired by the series "heroes of might and magic" 2 and 3. 
The overall feel of the game is going to be the same, but without the castle building

Core gameplay goals.
To be determined
Core technical features.
- java17
- gradle
- lwjgl
-

---

## Architecture

The code base contains it's own engine.

The engine lives within `<base package>.engine`.

The gameplay code lives within `<base package >.game`.

A utils code package is available within `<base package>.utils`.

Engine
- provides renderer
- game state handling
- input handling
- provides asset loading
- subsystems api
- oblivious to the gameplay code (enforced via archUnit tests)

Gameplay
- main game loop
- world building
- inventory
- scenes


Utils
- stringUtils
- streamUtils
- predicateUtils
- Utils should remain generic and reusable.
- Business or gameplay logic does not belong in utils.
- used by both engine and gameplay.

---

## Hard Rules

Non-negotiable constraints.
- deterministic simulation. Elements of random will need to happen, but needs to be testable.
- no gameplay logic in rendering
- utils package isolation
- frame-rate independent movement

These should ideally be enforceable through tests.

---

## Development Philosophy

- composition over inheritance
- avoid overengineering
- prefer readability over cleverness

Keep this section short.

---

## AI Agent Expectations

No modifications to files are permitted.
Any and all code examples are to be presented to the user within the chat interface.

- prefer architectural guidance over implementation
- explain what classes/systems are needed
- explain responsibilities and interactions between classes
- hints such as implementing/extending existing classes are acceptable
- Only provide actually code implementation if the user specifically requests it.

---

## References

- checkstyle.xml
- ArchUnit tests