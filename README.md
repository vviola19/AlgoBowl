AlgoBOWL: Tents and Trees Solver
Overview

This project was developed as part of a three-person team for Colorado School of Mines' AlgoBOWL competition. The goal was to create a solver for the Tents and Trees puzzle, where tents must be placed on a grid while satisfying a set of spatial and numerical constraints.

The solver evaluates puzzle states, places tents according to game rules, and minimizes violations when a perfect solution cannot be found. The project emphasizes algorithm design, object-oriented programming, and optimization techniques.

Problem Description

Tents and Trees is a logic puzzle played on a two-dimensional grid containing trees and empty spaces. A valid solution must satisfy several constraints:

Every tent must be paired with a tree directly adjacent horizontally or vertically.
Every tree must have exactly one corresponding tent.
Tents cannot be adjacent to one another, including diagonally.
Each row and column must contain a specified number of tents.

The objective is to produce a solution with the fewest possible violations.

Technologies Used
Java
Object-Oriented Programming
Algorithms
Data Structures
Git/GitHub
My Contributions

Contributions included:

Designing and implementing core puzzle-solving logic
Developing object-oriented representations of tents, trees, and board states
Assisting with violation tracking and solution evaluation
Testing and debugging puzzle-solving behavior
Collaborating on algorithm design and project integration
Project Structure
Component	Purpose
Tent	Represents an individual tent and its properties
Tree	Represents a tree on the puzzle board
TentPlacer	Contains logic for tent placement and puzzle solving
FileProcessor	Reads and processes puzzle input files
Testing Classes	Validation and debugging of solver behavior
Challenges

Some of the primary challenges included:

Managing multiple puzzle constraints simultaneously
Preventing illegal tent placements
Efficiently evaluating solution quality
Coordinating code contributions within a team environment
Balancing correctness with runtime performance
What I Learned

Through this project I gained experience with:

Algorithmic problem solving
Constraint-based optimization
Object-oriented software design
Team software development workflows
Debugging and testing complex logic systems
Team

This project was completed as part of a three-person team for the Colorado School of Mines AlgoBOWL competition.
