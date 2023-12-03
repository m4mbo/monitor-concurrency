package src.RobotConcurrency;

public class Analyzer implements Runnable{
    
    private String name;

    // Monitor references to interact with the shared resources
    private Monitor<Task> taskMonitor;
    private Monitor<Result> resultMonitor;

    /*
     * Constructor accepting the name and monitors
     */
    public Analyzer(String name, Monitor<Task> taskMonitor, Monitor<Result> resultMonitor) {
        this.name = name;
        this.taskMonitor = taskMonitor;
        this.resultMonitor = resultMonitor;
    }

    public void start() { 
        this.start();
    }

    public void run() {

        while (true) {

            Task nextTask = taskMonitor.removeItem(name); // getting a new task from buffer
            long analysisTime = (long) (nextTask.getComplexity() * 1000); // calculating the analysis time in seconds
            
            // Analysing
            try {
                Thread.sleep(analysisTime); 
                // After analysis, add the result in the buffer
                resultMonitor.addItem(new Result(nextTask.getId(), nextTask.getComplexity(), getResultAnalysis(nextTask.getComplexity()), nextTask.getSensorId()), name);

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    /*
     * Procedure to get the result value
     */
    public float getResultAnalysis(double complexity) {
        return (float) Math.sqrt(1/complexity);
    }
}
