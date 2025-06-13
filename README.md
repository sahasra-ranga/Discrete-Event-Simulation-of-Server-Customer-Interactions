# Discrete-Event-Simulation-of-Server-Customer-Interactions
A Java-based discrete event simulation that models customer arrivals, service processes, and waiting queues across multiple servers. The project tracks key metrics like wait times, queue lengths, and server idle periods using an immutable, event-driven architecture with functional programming patterns.

# Description
This project features a shop with a customisable number of servers. Each server has a queue, and a queue capacity is specified before hand. Customers arrive to the shop, and if a server is available, the first available server serves the customer. (Servers are labelled by numbers) However, if there are no available servers, the customer waits in queue at a server, and the customer waits at the first server available for queueing, despite if any other servers have shorter queues. However, if all servers have reached maximum queue capacity, the customer leaves. Each of these occurences are labelled as events, Arrive Event, Serve Event, Done Event, Wait Event and Leave Event.

These are the following pathways
Arrive --> Serve --> Done
Arrive --> Wait --> Serve --> Done
Arrive --> Leave

At the end of the simulation, statistics are collected on how many customers are served, average waiting time and how many customers left without being served.

# Features
• Simulates multiple servers 
• Handles customer arrivals, serving, waiting, and leaving events
• Configurable queue lengths and service times
• Functional, immutable design with minimal side effects
• Accurate event prioritisation using event queues

Java Concepts Demonstrated
1. Discrete Event Simulation
2. Event-driven architecture
3. Functional programming in Java
4. Immutability and state transitions
5. OOP design patterns


Sample result: 
• 0.500 customer 1 arrives
• 0.500 customer 1 serves by server 1
• 0.600 customer 2 arrives
• 0.600 customer 2 waits at server 1
• 0.700 customer 3 arrives
• 0.700 customer 3 leaves
• 1.500 customer 1 done
• 1.500 customer 2 serves by server 1
• 2.500 customer 2 done
