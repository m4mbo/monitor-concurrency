package src.RobotConcurrency;

import java.io.BufferedWriter;
import java.util.LinkedList;

import src.Problem;

/*
 * Robot Controller with N sensors
 */
public class MultipleSensors implements Problem {

    private final int runningTime = 10;             // Running time for main program
    private Thread analyzerThread;                  // Thread holding the analyzer
    private Thread actuatorThread;                  // Thread holding the actuator

    private Analyzer analyzer;                      
    private Actuator actuator;

    /*
     * General variables
     */
    private String scheduling;
    private int lambda;
    private float pos;
    private int sensorNumber;

    private StatsLogger statsLogger;                 // Object to record the program's statistics

    /*
     * Monitor instances for all three actors
     */
    private Monitor<Task> taskMonitor;
    private Monitor<Result> resultMonitor;

    private LinkedList<Thread> sensorThreads;         // Linked List of N sensor holding threads

    private BufferedWriter bufferedWriter;            // Writer for data report

    /*
     * Constructor for no data report
     */
    public MultipleSensors(int lambda, float pos, String scheduling, int sensorNumber) {
        this.lambda = lambda;
        this.pos = pos;
        this.scheduling = scheduling;
        this.sensorNumber = sensorNumber;
        this.bufferedWriter = null;
    }

    /*
     * Constructor overloading, for data report
     */
    public MultipleSensors(int lambda, float pos, String scheduling, int sensorNumber, BufferedWriter bufferedWriter) {
        this.lambda = lambda;
        this.pos = pos;
        this.scheduling = scheduling;
        this.sensorNumber = sensorNumber;
        this.bufferedWriter = bufferedWriter;
    }

    public String name() {
        return "Concurrent Multiple Sensors";
    }

    public void init() {

        statsLogger = new StatsLogger(runningTime, bufferedWriter);

        // Initializing monitors with buffer cap set to 20
        taskMonitor = new Monitor<>(20, scheduling, statsLogger);
        resultMonitor = new Monitor<>(20, "FCFS", statsLogger);

        sensorThreads = new LinkedList<>();

        // For number of sensors, create a new thread an store it in linked list
        for (int i = 0; i < sensorNumber; i++) {
            sensorThreads.add(new Thread(new Sensor("Sensor " + i, lambda, taskMonitor)));
        }

        // Passing the monitor instances to actors
        analyzer = new Analyzer("Analyzer", taskMonitor, resultMonitor);
        actuator = new Actuator("Actuator", resultMonitor, pos);

        analyzerThread = new Thread(analyzer);
        actuatorThread = new Thread(actuator);
    }

    public void go() {

        /*
         * Start all threads
         */
        for (Thread thread : sensorThreads) {
            thread.start();
        }
        analyzerThread.start();
        actuatorThread.start();

        // Run for 10 seconds
        try {Thread.sleep(runningTime*1000); } catch (InterruptedException e) { }

        // Interrupt all threads
        for (Thread thread : sensorThreads) {
            thread.interrupt();
        }
        analyzerThread.interrupt();
        actuatorThread.interrupt();

        // Giving 3 seconds for threads to finish completely
        try {Thread.sleep(3000); } catch (InterruptedException e) { }

        statsLogger.getStatsLog();
    }
}
