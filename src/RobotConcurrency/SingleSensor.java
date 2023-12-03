package src.RobotConcurrency;

import java.io.BufferedWriter;

import src.Problem;

/*
 * Robot controller with a single sensor
 */
public class SingleSensor implements Problem {

    private final int runningTime = 10;         // Running time for main program
    private Thread sensorThread;                // Thread holding the sensor
    private Thread analyzerThread;              // Thread holding the analyzer
    private Thread actuatorThread;              // Thread holding the actuator

    // Actors
    private Sensor sensor;          
    private Analyzer analyzer;  
    private Actuator actuator;

    // Variables
    private String scheduling;
    private int lambda;
    private float pos;

    private StatsLogger statsLogger;            // Object to record the program's statistics

    /*
     * Monitor instances for all three actors
     */
    private Monitor<Task> taskMonitor;
    private Monitor<Result> resultMonitor;

    private BufferedWriter bufferedWriter;      // Writer for data report

    /*
     * Constructor for no data report
     */
    public SingleSensor (int lambda, float pos, String scheduling) {
        this.lambda = lambda;
        this.pos = pos;
        this.scheduling = scheduling;
        this.bufferedWriter = null;
    }

    /*
     * Constructor overloading, for data report
     */
    public SingleSensor (int lambda, float pos, String scheduling, BufferedWriter bufferedWriter) {
        this.lambda = lambda;
        this.pos = pos;
        this.scheduling = scheduling;
        this.bufferedWriter = bufferedWriter;
    }

    public String name() {
        return "Concurrent Single Sensor";
    }

    public void init() {

        statsLogger = new StatsLogger(runningTime, bufferedWriter);

        // Initializing monitors with buffer cap set to 20
        taskMonitor = new Monitor<>(20, scheduling, statsLogger);
        resultMonitor = new Monitor<>(20, "FCFS", statsLogger);

        // Initializing actors and passing monitor instances
        sensor = new Sensor("Sensor 1", lambda, taskMonitor);
        analyzer = new Analyzer("Analyzer", taskMonitor, resultMonitor);
        actuator = new Actuator("Actuator", resultMonitor, pos);

        sensorThread = new Thread(sensor);
        analyzerThread = new Thread(analyzer);
        actuatorThread = new Thread(actuator);
    }

    public void go() {
        // Start all threads
        sensorThread.start();
        analyzerThread.start();
        actuatorThread.start();

        // Run for 10 seconds
        try {Thread.sleep(runningTime*1000); } catch (InterruptedException e) { }

        // Interrupt all threads
        sensorThread.interrupt();
        analyzerThread.interrupt();
        actuatorThread.interrupt();

        // Extra 3 seconds to let threads finish
        try {Thread.sleep(3000); } catch (InterruptedException e) { }

        statsLogger.getStatsLog();
    }
}
