package src.RobotConcurrency;

public class Actuator implements Runnable {
    private String name;

    // Monitor reference to interact with the result buffer
    private Monitor<Result> resultMonitor;

    private float position;                 // Curent robot position

    private final float leftWall = 0;       // Wall positions
    private final float rightWall = 1;

    private boolean goingRight;             // True if going right

    /*
     * Constructor accepting a name
     * Monitor reference a
     * And initial position
     */
    public Actuator(String name, Monitor<Result> resultMonitor, float initPos) {
        this.name = name;
        this.goingRight = (initPos == 1 ? false : true);    // If the initial position was set to 1, the robot is going left, else, set to right
        this.position = initPos;
        this.resultMonitor = resultMonitor;
    }

    public void start() { 
        this.start();
    }

    public void run() {

        while (true) {

            float oldPos = position;

            // Getting a result from the buffer
            Result nextResult = resultMonitor.removeItem(name);

            float toMove = nextResult.getY();

            float bounceCorrection = bounceSequence(toMove);

            moveRobot(bounceCorrection);
            
            System.out.println("\nRobot moving. Task id {" + nextResult.getId() + "}, sensor id {" + (nextResult.getSensorId()) + "}, task complexity {" + nextResult.getComplexity() + "}, result {" + toMove + "}, old position: {" + oldPos + "}, new position: {" + position + "}.");
            
            try {
                Thread.sleep(0); 
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    // Method to update the direction in which the robot is heading to after the bouncing sequence
    public float bounceSequence(float toMove) {

        // Substracting 1 from the toMove value and changing direction
        while (toMove > 1) {
            toMove--;
            goingRight = !goingRight;
        }
        return toMove;
    }

    // Method to handle robot movement after the bouncing sequence
    public void moveRobot(float toMove) {

        if (goingRight) {

            position += toMove;

            if (position == rightWall) {
                goingRight = false;
            } else if (position > rightWall) {
                goingRight = false;
                position = rightWall - (position - rightWall);
            }

        } else {
            position -= toMove;

            if (position == leftWall) {
                goingRight = true;
            } else if (position < leftWall) {
                goingRight = true;
                position = leftWall + (position * -1);
            }

        }
    }
}
