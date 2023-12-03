package src.RobotConcurrency;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import src.Problem;

/*
 * Class for data reporting, output to target/Stats.txt
 */
public class DataReporter implements Problem{

    private SingleSensor singleSensor;

    private MultipleSensors multipleSensors;

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public String name() {
        return "Data reporter";
    }

    public void init() {
        try {
            // Initialize file writer object
            fileWriter = new FileWriter("Stats.txt");
            bufferedWriter = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void go() {

        try {

            /*
             * Iterate over values of lambda and record 5 trials
             * FCFS - Single Sensor
             */
            for (int j = 0; j < 5; j++) {
                bufferedWriter.write("Single Sensor - FCFS - Trial " + j);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                for (int i = 2; i <= 20; i ++) {
                    singleSensor = new SingleSensor(i, 0, "FCFS", bufferedWriter);
                    singleSensor.init();
                    singleSensor.go();
                }
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }
            
            /*
             * Iterate over values of lambda and record 5 trials
             * SJF - Single Sensor
             */
            for (int j = 0; j < 5; j++) {
                bufferedWriter.write("Single Sensor - SJF - Trial " + j);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                for (int i = 2; i <= 20; i ++) {
                    singleSensor = new SingleSensor(i, 0, "SJF", bufferedWriter);
                    singleSensor.init();
                    singleSensor.go();
                }
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            /*
             * Iterate over values of lambda and record 5 trials
             * FCFS - Multiple Sensors (3)
             */
            for (int j = 0; j < 5; j++) {
                bufferedWriter.write("Multiple Sensors - FCFS - Trial " + j);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                for (int i = 2; i <= 20; i ++) {
                    multipleSensors = new MultipleSensors(i, 0, "FCFS", 3, bufferedWriter);
                    multipleSensors.init();
                    multipleSensors.go();
                }
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }   

            /*
             * Iterate over values of lambda and record 5 trials
             * SJF - Multiple Sensors (3)
             */
            for (int j = 0; j < 5; j++) {
                bufferedWriter.write("Multiple Sensors - SJF - Trial " + j);
                bufferedWriter.newLine();
                bufferedWriter.newLine();
                for (int i = 2; i <= 20; i ++) {
                    multipleSensors = new MultipleSensors(i, 0, "SJF", 3, bufferedWriter);
                    multipleSensors.init();
                    multipleSensors.go();
                }
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
