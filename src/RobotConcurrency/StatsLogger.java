package src.RobotConcurrency;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;

/*
 * Class to keep track of the program's statistics
 */
public class StatsLogger {
    
    private int throughput;                 // Total throughput of the system, how many tasks we processed

    private int runningTime;                // Total running time

    private LinkedList<Task> processedTasksRecord;    // A record of all tasks processed, in the order they were processed
    private LinkedList<Task> addedTasksRecord;    // A record of all tasks processed, in the order they were processed

    private BufferedWriter bufferedWriter; 

    public StatsLogger(int runningTime, BufferedWriter bufferedWriter) {
        this.throughput = 1;
        this.runningTime = runningTime;
        this.processedTasksRecord = new LinkedList<>();
        this.addedTasksRecord = new LinkedList<>();
        this.bufferedWriter = bufferedWriter;
    }

    public void incrementThroughput() {
        this.throughput++;
    }

    /*
     * Method to calculate average waiting time
     * Sums up waiting time for all tasks (they are stored in the order they were processed)
     * Divides by number of tasks
     */
    public float getAverageWaitingTime() {
        float waitingTime = 0;
        float currentSec = 0;
        for (int i = 0; i < processedTasksRecord.size() - 1; i++) {
            currentSec += processedTasksRecord.get(i).getComplexity();
            waitingTime += currentSec;
        }
        return waitingTime/processedTasksRecord.size();
    }

    public float getAverageTasksPerSecond() {
        return ((float) addedTasksRecord.size()) / runningTime;
    }

    public void updateProcessedTaskRecord(Task task) {
        processedTasksRecord.addFirst(task);
    }

    public void updateAddedTaskRecord(Task task) {
        addedTasksRecord.addFirst(task);
    }

    public String getLastTaskProcessed() {
        return (processedTasksRecord.peek() == null ? "None" : Integer.toString(processedTasksRecord.peek().getId()));
    }

    public String getLastTaskAdded() {
        return (addedTasksRecord.peek() == null ? "None" : Integer.toString(addedTasksRecord.peek().getId()));
    }

    public void getStatsLog() {

        String averageTasksPerSecond = "" + getAverageTasksPerSecond();

        String averageWaitingTime = "" + getAverageWaitingTime();

        System.out.print("\r\n=================\n");
        System.out.print("Stats Log");
        System.out.print("\r\n=================\n\n");
        System.out.print("Total Throughput {" + throughput + "}\n\n");
        System.out.print("Average Tasks Per Second {" + averageTasksPerSecond + "}\n\n");
        System.out.print("Average Waiting Time {" + averageWaitingTime + "}\n");

        if (bufferedWriter != null) {
            try {
                bufferedWriter.write(throughput + ", " + averageTasksPerSecond + ", " + averageWaitingTime);
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
           
        }
    }
}
