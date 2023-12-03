package src.RobotConcurrency;

import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private int id;                 // Task unique identifier
    private String sensorId;        // Sensor identifier mapped to each task
    private double complexity;      // Analysis time or complexity of task

    // Atomic integer to assign each task a unique id
    private static AtomicInteger tidAllocator = new AtomicInteger();

    // Constructor accepting the complexity or analysis time, and sensorID
    public Task(double complexity, String sensorId) {
        this.id = tidAllocator.getAndIncrement();
        this.complexity = complexity;
        this.sensorId = sensorId;
    }

    // Constructor accepting the complexity, taskID and sensorID
    public Task(int taskId, double complexity, String sensorId) {
        this.id = taskId;
        this.complexity = complexity;
        this.sensorId = sensorId;
    }

    /*
     * Getters
     */
    public int getId() {
        return id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public double getComplexity() {
        return complexity;
    }
}
