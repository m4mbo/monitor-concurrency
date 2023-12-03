/*
Rig.java

Author : Dr James Stovold
Date   : Aug 17, 2022
Version: 0.1
*/

package src;

import java.lang.System;

import src.RobotConcurrency.DataReporter;
import src.RobotConcurrency.MultipleSensors;
import src.RobotConcurrency.SingleSensor;

import java.io.*;

public class Rig { 

  Problem problem;       // abstract class interface (to be overridden by 
                         // student implementations) 
  private static int lambda;
  private static float pos;
  private static String scheduling;
  private static int senseNum;
  
  /*
   * Method to get the variables necessary for the single sensor controller:
   * lambda
   * initial position
   * scheduling algorithm
   */
  public static void getVariablesSingleSensor() {
      System.out.println("\r\n=================");
      System.out.print("Variables");
      System.out.println("\r\n=================\n");

      inputLambda();

      inputPos();

      inputScheduling();
  }

  /*
   * Method to get the variables necessary for the multiple sensors controller:
   * lambda
   * initial position
   * scheduling algorithm
   * number of sensors
   */
  public static void getVariablesMultipleSensors() {
      System.out.println("\r\n=================");
      System.out.print("Variables");
      System.out.println("\r\n=================\n");

      inputLambda();

      inputPos();

      inputSensorNumber();

      inputScheduling();
  }

  // Method to ask for lambda input
  public static void inputLambda() {
    while (true) {
        System.out.print("Lambda: " );
        BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
        String line = "";
        try {
            line = buffer.readLine();
            System.out.println();
            lambda = Integer.parseInt(line);
            if (lambda <= 0) {
                  System.out.println("Please choose a value greater than 0." );
                  continue;
              }
              break;
        } catch (NumberFormatException e) {
            System.out.println("I don't know what '" + line + "' is, please input a number." );
            continue;
        } catch (IOException e) {
            System.out.println("IOException, quitting...");
            break;
        }
    }
  }

  // Method to ask for inital robot position, accepting only floating points between 0 and 1
  public static void inputPos() {
      while (true) {
          System.out.print("Initial Robot Position: ");

          BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
          String line = "";
          try {
              line = buffer.readLine();
              System.out.println();
              pos = Float.parseFloat(line);

              if (pos > 1 || pos < 0) {
                  System.out.println("Please choose a value between 0 and 1." );
                  continue;
              }
              break;
          } catch (NumberFormatException e) {
              System.out.println("I don't know what '" + line + "' is, please input a floating point number." );
              
              continue;
          } catch (IOException e) {
              System.out.println("IOException, quitting...");
              break;
          }
      }
  }

  // Method to ask for scheduling algorithm
  public static void inputScheduling() {
      while (true) {
          System.out.println("\r=================");
          System.out.print("Scheduling Algorithm");
          System.out.println("\r\n=================\n");
          System.out.println("1. FCFS\n");
          System.out.println("2. SJF\n");
          System.out.print("Pick one: ");

          BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
          String line = "";
          try {
              line = buffer.readLine();
              System.out.println();
              int option = Integer.parseInt(line);

              switch (option) {
                  case 1:
                      scheduling = "FCFS";
                      break;
                  case 2:
                      scheduling = "SJF";
                      break;
                  default:
                      System.out.println("I don't know what '" + line + "' is, please input a valid number." );
                      continue;
              }
              break;
          } catch (NumberFormatException e) {
              System.out.println("I don't know what '" + line + "' is, please input a valid number." );
              continue;
          } catch (IOException e) {
              System.out.println("IOException, quitting...");
              break;
          }
      }

  }

  // Method to ask for number of sensors
  public static void inputSensorNumber() {
    while (true) {
          System.out.print("Number of sensors: ");

          BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
          String line = "";
          try {
              line = buffer.readLine();
              System.out.println();
              senseNum = Integer.parseInt(line);

              if (pos > 1 || pos < 0) {
                  System.out.println("Please choose a value greater than 0." );
                  continue;
              }
              break;
          } catch (NumberFormatException e) {
              System.out.println("I don't know what '" + line + "' is, please input an integer number." );
              
              continue;
          } catch (IOException e) {
              System.out.println("IOException, quitting...");
              break;
          }
    }
  }
  
  public static void main(String[] args) {

    while (true) {

      System.out.println("\r\n=================");
      System.out.println("Coursework 1 Menu");
      System.out.println("=================\r\n");
      System.out.println("1. Robot Controller: Single sensor");
      System.out.println("2. Robot Controller: Multiple sensors");
      System.out.println("3. Data report ('target/Stats.txt')");
      System.out.println("0. Exit");

      System.out.print("Pick a problem: ");
      Integer selectedOption = 0;
      BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
      String line = "";
      try {
        line = buffer.readLine();
        System.out.println();
        selectedOption = Integer.parseInt(line);
      } catch (NumberFormatException e) {
        System.out.println("I don't know what '" + line + "' is, please input a number." );
        continue;
      } catch (IOException e) {
        System.out.println("IOException, quitting...");
      }

      if (selectedOption == 0) { break; }
      Rig rig = new Rig();
      System.out.println("Setting up problem...");
 
      switch (selectedOption) {
        case 1: // single sensor
          getVariablesSingleSensor();
          rig.problem = new SingleSensor(lambda, pos, scheduling); 
          break;
        case 2: // multiple sensors
          getVariablesMultipleSensors();
          rig.problem = new MultipleSensors(lambda, pos, scheduling, senseNum);
          break;
        case 3: // multiple sensors
          rig.problem = new DataReporter();
          break;
        default:
          System.out.println("I don't know what '" + line + "' is, please input a valid option." );
          continue;
      }

      System.out.println("Initialising problem: " + rig.problem.name());
      rig.problem.init();
      System.out.println(rig.problem.name() + " established.");
      System.out.println("Running...\r\n");
      rig.problem.go();     
    }
    System.exit(0);
  }  
}


  







