// Author:  Your Name goes here

import ch.aplu.nxt.*;

public class Robot {
	
	private NxtRobot nxt;
	
	// Combine two motors on an axis to perform car-like movements.
	Motor motA = new Motor(MotorPort.A);
	Motor motB = new Motor(MotorPort.B);
	
	
	
	//light sensor
	private LightSensor ls = new LightSensor(SensorPort.S3);
	
	// constructor
	Robot() {
		nxt = new NxtRobot();
		nxt.addPart(motA);
		nxt.addPart(motB);
		nxt.addPart(ls);
	}

	public void exit() {
		nxt.exit();
	}
	
	
	public void run() {
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