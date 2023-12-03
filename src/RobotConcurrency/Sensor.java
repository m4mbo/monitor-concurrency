package src.RobotConcurrency;

public class Sensor implements Runnable {

    // Defining range of task complexity
    private final double minComplexityVal = 0.1;
    private final double maxComplexityVal = 0.5;

    // Monitor reference to interact with the task buffer
    private Monitor<Task> monitor;

    // Rate in which k tasks will be produced, 1 second
    private int averageTaskRate = 1000;

    private int lambda;

    String name;

    /*
     * Constructor accepting a name, a lambda and a monitor
     */
    public Sensor(String name, int lambda, Monitor<Task> monitor) {
        this.name = name;
        this.lambda = lambda;
        this.monitor = monitor;
    }   

    public void start() { 
        this.start();
    }

    public void run() {

        while (true) {

            // Producing k number of tasks every second

            int k = poissonDistribution();

            for (int i = 0; i < k; i++) {
                // Creating a random number between 0.1 and 0.5 for the task's complexity
                double random = Math.random();
                random = minComplexityVal + (random * (maxComplexityVal - minComplexityVal));
                monitor.addItem(new Task(random, name), name);
            }

            try {
                // Sending thread to sleep for a second
                Thread.sleep(averageTaskRate); 
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /*
     * Implementation of Knuth's algorithm for generating poisson distributed numbers
     */
    public int poissonDistribution() {

        double prob = 1;                            // Arbitrary value prob, initialized as 1
        int k = 0;                                  // Initialize k as 0
        double commonFactor = Math.exp(-lambda);    // precompute common factor first for efficiency

        do {
            prob *= Math.random();                  // Multiply by common factor
            k++;                                    // Add 1 to k
        } while (prob > commonFactor);

        return k - 1;
    }
}
