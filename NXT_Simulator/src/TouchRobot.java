import ch.aplu.nxtsim.*;

public class TouchRobot {
    private NxtRobot robot;
    private Gear gear;
    private TouchSensor ts;
   
    public TouchRobot() {
        robot = new NxtRobot();
        gear = new Gear();
        ts = new TouchSensor(SensorPort.S3);
        robot.addPart(gear);
        robot.addPart(ts);
  }
   
    public void run() {
        gear.setSpeed(50);

       
        while (true) {
            gear.forward();
            if (ts.isPressed()) {
            	gear.backward(1000);
            	gear.left(1000);
            }
        }
    }

    public static void main(String[] args) {
        TouchRobot robot = new TouchRobot();
        	robot.run();
        }

    // ------------------ Environment --------------------------
    static {
        NxtContext.showNavigationBar();
        NxtContext.useObstacle("sprites/red_bar0.gif", 250, 200);
        NxtContext.useObstacle("sprites/bar1.gif", 400, 250);
        NxtContext.useObstacle("sprites/bar2.gif", 250, 400);
        NxtContext.useObstacle("sprites/bar3.gif", 100, 250);
    }
}
