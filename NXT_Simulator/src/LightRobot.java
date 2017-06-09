
import ch.aplu.jgamegrid.*;
import ch.aplu.nxtsim.*;
import java.awt.Color;
import java.awt.Point;

public class LightRobot {
    private NxtRobot robot;
    private Gear gear;
    private LightSensor ls;
   
    public LightRobot() {
        robot = new NxtRobot();
        gear = new Gear();
        ls = new LightSensor(SensorPort.S3);
        robot.addPart(gear);
        robot.addPart(ls);
  }
   
    public void run() {
        gear.setSpeed(100);

       
        while (true) {
            if (ls.getValue() < 1000) {
            	gear.leftArc(.1);
            } else {
            	gear.rightArc(.1);
            }
        }
    }

    public static void main(String[] args) {
        LightRobot robot = new LightRobot();
        	robot.run();
        }

    // ------------------ Environment --------------------------
    static {
    	/*
        NxtContext.showNavigationBar();
        NxtContext.useObstacle("sprites/bar0.gif", 250, 200);
        NxtContext.useObstacle("sprites/bar1.gif", 400, 250);
        NxtContext.useObstacle("sprites/bar2.gif", 250, 400);
        NxtContext.useObstacle("sprites/bar3.gif", 100, 250);
        */
    }
    public static void _init(GameGrid gg)
    {
    	GGBackground bg = gg.getBg();
    	bg.setPaintColor(Color.black);
    	bg.fillArc(new Point(250, 230), 50, 0, 360);
    	bg.fillArc(new Point(250, 340), 100, 0, 360);
    	bg.setPaintColor(Color.white);
    	bg.fillArc(new Point(250, 230), 45, 0, 360);
    	bg.fillArc(new Point(250, 340), 95, 0, 360);
    }
}

