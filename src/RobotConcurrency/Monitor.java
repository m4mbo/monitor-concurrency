package src.RobotConcurrency;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Monitor around a shared resource of type E
 * E extends Task, accepting:
 * Task buffer
 * Result buffer
 */
public class Monitor<E extends Task> {
    
    private int cap;                        // Max amount of items in buffer at any given time
    private String scheduling;              // Scheduling algorithm for item removal
    private int count;                      // Keeping track of items in buffer
    private StatsLogger statsLogger;        // Object to log the program's statistics

    private final Lock lock = new ReentrantLock();                  // Reentrant lock to, in the abscence of a better word, lock the monitor instance for mutual exclusion
    // Condition variables to synchronize threads within this monitor instance
    private final Condition spaceAvailable = lock.newCondition();  
    private final Condition itemAvailable = lock.newCondition();

    // Shared resource
    private Queue<E> buffer = new LinkedList<>();

    public Monitor(int cap, String scheduling, StatsLogger statsLogger) {
        this.cap = cap;
        this.count = cap;                   // Initializing count to cap
        this.scheduling = scheduling;
        this.statsLogger = statsLogger;
    }

    public void addItem(E item, String threadName) {
        lock.lock();        // Initializing critical section
        count--;
        try {
            // If the count is negative, no more space in buffer
            while (count < 0) { 
                System.out.println((threadName == "Analyzer" ? "\nAnalyze error: too many results! " : "\nSense error: too many tasks! ") + "Last task added {" + statsLogger.getLastTaskAdded() + "}");
                spaceAvailable.await();      // Call await in condition variable
            }
            buffer.add(item);
            statsLogger.updateAddedTaskRecord(item);
            itemAvailable.signal();          // Signal for item available
        } catch (InterruptedException e) {

        } finally {
            lock.unlock();  // unlocking instance
        }
    }

    public E removeItem(String threadName) {
        lock.lock();
        count++;
        try {
            // If the count is equal to cap, there are no items in buffer
            while (count >= cap) { 
                System.out.println((threadName == "Analyzer" ? "\nAnalyze error: no tasks to analyze. " : "\nActuate error: no results to process. ")  + "Last task processed {" + statsLogger.getLastTaskProcessed() + "}");
                itemAvailable.await();      // Call await in condition variable
            }
            if (threadName == "Actuator") {
                statsLogger.incrementThroughput();
                E item = buffer.poll();
                statsLogger.updateProcessedTaskRecord(item);
                spaceAvailable.signal();    // Signal for space available
                return item;
            } else {
                E item = (scheduling == "FCFS" ? buffer.poll() : SJF());
                spaceAvailable.signal();    // Signal for space available     
                return item;
            }
        } catch (InterruptedException e) {
            
        } finally {
            lock.unlock();  // unlocking instance
        }
        return buffer.poll();
    }

    // Iterating over available tasks and removing the shortest
    public E SJF() {
        int index = 0;
        double complexity = -1;

        LinkedList<E> linkedBuffer = (LinkedList<E>) buffer;

        for (int i = 0; i < buffer.size(); i++) {
            if (complexity == -1) {
                complexity = linkedBuffer.get(i).getComplexity();
                index = i;
            }    
            else if (complexity > linkedBuffer.get(i).getComplexity()) {
                complexity = linkedBuffer.get(i).getComplexity();
                index = i;
            }
        }
        return linkedBuffer.remove(index);
    }
}
