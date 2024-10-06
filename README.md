# Monitors and Concurrency

This repository showcases a simple implementation of the monitor concurrent technique for multi-thread safe behaviour.

# Overview

The given tasks involved creating robot controllers able to concurrently handle a workflow of sensing, analyzing and actuating. The program can be compiled and ran by executing the bash files build.sh and run.sh in said order. Three threads where created, each one representing an actor within the system: sensor, analyzer, and actuator. The task was approached as an extension to the producer-consumer problem. In this case, the Sensor acted purely as a producer, the Actuator as a consumer, and the Analyzer as hybrid between the two. That being said, 2 buffers connected each of the producers with their respective consumers (sensor-analyzer and analyzer-consumer), and the concurrent technique selected to ensure multi-thread safe behavior was the monitor. 

The concurrent simulation cosisted of 3 steps:

# Sense
Sensors create k number of tasks every second following a Poisson distribution. These tasks would have a complexity c, evenly distributed between 0.1 adn 0.5.
# Analyze
The analyzer would analyze said tasks and send the results to the actuator. The analysis would take c seconds.
# Actuate
The actuator would read from those results and move the robot accordingly. 
