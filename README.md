### Description of Project
The objective of this project was to expand upon a 2D platforming game using Java and JavaFX. It was undertaken as a component of a second-year undergraduate software development course, where we were tasked with applying a range of Design Patterns and SOLID and GRASP principles acquired during the course to implement the necessary game features. The original codebase provided was a barebones version of the platforming game, with the rudimentary mechanics (excluding jumping), game engine and GUI rendering already built. 

### Coding Style
<a href="https://oracle.com/technetwork/java/codeconventions-150003.pdf">Oracle</a>

### Location of the Configuration file
src/main/resources/level_1.json

### Description of the Configuration file
The config file has the hero as its own JSON object.
The immovable, movable and enemy entities are all stored in
three separate JSON arrays. Each entity that belongs to an
array requires attributes specific to that array. This facilitates
the creation process and allows you to add a new entity anywhere in
its corresponding JSON array.

### Tech Stack
- Java: Used as the programming language to code the game.
- JavaFX: Used to render the GUI.
- Gradle: Used to automate the compilation and testing of the software package.

### Design Patterns Used:
- Creational Patterns: Factory Method and Builder 
- Behavioral Patterns: Strategy, Observer and State

## Acknowledgements
<a href="https://opengameart.org/content/top-down-2d-metal-box">Block png</a><br>
