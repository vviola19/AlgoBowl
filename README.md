Alice in Mazeland Solver
Overview

Alice in Mazeland was a graph modeling project completed for CSCI 406: Algorithms at Colorado School of Mines.

The objective was to help Alice navigate a maze while minimizing the total energy required to reach the goal. Unlike a traditional maze, Alice's movement distance changes throughout the maze based on special cells that increase or decrease her step size. This required modeling not only Alice's location, but also her current movement state.

The project focused on transforming a complex puzzle into a graph problem and applying shortest-path algorithms to find an optimal solution.

Problem Description

The maze consists of cells that contain:

Allowed movement directions
Step size modifiers
A starting location
A goal location

Alice begins with a step size of 1. As she moves through the maze, certain cells increase or decrease her step size, changing which cells are reachable in future moves.

The challenge is to determine the minimum-energy path from the start to the goal while accounting for:

Variable movement distances
Directional movement restrictions
Dynamic state changes throughout the maze
Approach

The maze was modeled as an explicit weighted graph.

Each vertex represented a unique state consisting of:

Alice's current position
Her current step size

Edges represented valid movements between states.

Edge weights were based on the movement cost defined by the problem, allowing shortest-path algorithms to determine the optimal route through the maze.

After constructing the graph, a shortest-path algorithm was used to identify the minimum-cost path from the start state to the goal state.

Technologies Used
Java
Graph Theory
Algorithms
Data Structures
Object-Oriented Programming
Key Concepts
Graph Modeling
State-Space Representation
Weighted Graphs
Shortest Path Algorithms
Complexity Analysis
Algorithm Correctness
Challenges

Some of the biggest challenges included:

Representing dynamic maze states as graph vertices
Modeling changing movement distances without modifying the shortest-path algorithm itself
Managing graph size efficiently for larger mazes
Ensuring correctness of graph construction and edge generation
Analyzing time and space complexity of the resulting graph
What I Learned

This project strengthened my understanding of:

Translating real-world problems into graph models
Designing state-space representations
Applying graph algorithms to non-traditional problems
Analyzing algorithm performance and scalability
Defending technical design decisions during code reviews and interviews
Course Information

Course: CSCI 406 – Algorithms
Institution: Colorado School of Mines
