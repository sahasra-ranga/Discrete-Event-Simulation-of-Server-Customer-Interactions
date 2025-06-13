# Discrete-Event-Simulation-of-Server-Customer-Interactions
A Java-based discrete event simulation that models customer arrivals, service processes, and waiting queues across multiple servers. The project tracks key metrics like wait times, queue lengths, and server idle periods using an immutable, event-driven architecture with functional programming patterns.

# Features
Simulates multiple servers (human/self-checkout)
Handles customer arrivals, serving, waiting, and leaving events
Configurable queue lengths and service times
Functional, immutable design with minimal side effects
Accurate event prioritisation using event queues

Java Concepts Demonstrated
1. Discrete Event Simulation
2. Event-driven architecture
3. Functional programming in Java
4. Immutability and state transitions
5. OOP design patterns


Sample result
0.500 customer 1 arrives
0.500 customer 1 serves by server 1
0.600 customer 2 arrives
0.600 customer 2 waits at server 1
0.700 customer 3 arrives
0.700 customer 3 leaves
1.500 customer 1 done
1.500 customer 2 serves by server 1
2.500 customer 2 done
