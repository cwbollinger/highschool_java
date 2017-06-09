// Author:  Your Name goes here

import ch.aplu.nxtsim.*;

public class Robot {
	
	private NxtRobot nxt;
	
	// Combine two motors on an axis to perform car-like movements.
	private Gear gear;
	
	//light sensor
	private LightSensor ls = new LightSensor(SensorPort.S3);
	
	// constructor
	Robot() {
		nxt = new NxtRobot();
		gear = new Gear();
		nxt.addPart(gear);
		nxt.addPart(ls);
	}

	public void exit() {
		nxt.exit();
	}
	
	public void setSpeed(int speed) {
		gear.setSpeed(speed);
	}
	
	public void forward() {
		gear.forward();
	}

	public void turnLeft() {
		gear.left();
	}
	
	public void turnRight() {
		gear.right();
	}
	
	public void square() {
		for(int count = 4; count > 0; count--) {
			turnRight();
			Tools.delay(750);
			forward();
			Tools.delay(2000);
			this.gear.stop();
		}
	}
	
	public void run() {
		this.setSpeed(30);
		this.square();
		this.blink();
	}
	
	public void blink() {
		for(int count = 4; count > 0; count--) {
			this.ls.activate(true);
			Tools.delay(2000);
			this.ls.activate(false);
			Tools.delay(2000);
		}
	}

	public static void main(String[] args) {
		Robot robot = new Robot();
		robot.run();
	    robot.exit();
	}
}