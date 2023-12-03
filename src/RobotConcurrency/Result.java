package src.RobotConcurrency;

public class Result extends Task{

    private float Y;

    /*
    *   Constructor accepting the task ID, 
    *   complexity of said task,
    *   result of analysis (Y),
    *   and sensor ID
    */ 
    public Result(int taskId, double complexity, float Y, String sensorId) {
        super(taskId, complexity, sensorId);
        this.Y = Y;
    }

    public float getY() {
        return Y;
    }
}
