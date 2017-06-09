import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.*;
import lejos.nxt.comm.BTConnection;
//import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;

public class Robot {

	// this is a singleton
	private static Robot robot;

	public boolean exit = false;

	final double wheelDiameter = 5.6; // both in cm
	final double robotDiameter = 13.6;
	// final double angleError =
	// 1.0+((360.0-280.0)/360.0)+((360.0-354.0)/360.0)+((360.0-365.0)/360.0);
	// at full voltage, gives accurate turns at 40 power.
	final double angleError = (360.0/305.0);

	NXTMotor motRight;
	NXTMotor motLeft;

	TouchSensor touch;

	LightSensor lightLeft;
	LightSensor lightRight;
	UltrasonicSensor ultrasonic;

	public int baseMotorPower;

	State current_state;

	Map2D map;
	int x;
	int y;
	int dir;

	BTConnection btc;
	DataInputStream inStream;
	DataOutputStream outStream;

	public static Robot getRobot() {
		if (robot == null) {
			robot = new Robot();
		}
		return robot;
	}

	private Robot() {
		motRight = new NXTMotor(MotorPort.B);
		motLeft = new NXTMotor(MotorPort.C);

		baseMotorPower = 40;

		motRight.setPower(baseMotorPower);
		motLeft.setPower(baseMotorPower);
		motRight.stop();
		motLeft.stop();

		touch = new TouchSensor(SensorPort.S1);
		lightLeft = new LightSensor(SensorPort.S2);
		lightRight = new LightSensor(SensorPort.S3);
		ultrasonic = new UltrasonicSensor(SensorPort.S4);
		ultrasonic.continuous();

		current_state = StateStart.getInstance();

		map = new Map2D();
		x = 0;
		y = 0;
		dir = 0;

		// actual BT connection is done in StateCommand
		btc = null;
		inStream = null;
		outStream = null;
	}

	
	/**
	 * Print message over Bluetooth connection if it's running
	 * @param msg
	 */
	public void debug(String msg) {
		if ((btc != null) && (outStream != null)) {
			try {
				// TODO
				outStream.writeUTF(msg);
				outStream.flush();
			} catch (IOException e) {
				LCD.drawString("Rbt IO Err", 0, 1);
			}
		}
		else {
			//System.out.println(msg);
			RConsole.print(msg);
		}
	}
	
	// TODO
	public void debugln(String msg) {
		debug(msg + "\n");
	}
	
	public void update() {
		if (current_state != null) {
			current_state.execute(this);
		}
	}

	/**
	 * This method changes the current state to the new state. It first calls
	 * the Exit() method of the current state, then assigns the new state to
	 * m_pCurrentState and finally calls the Entry() method of the new state.
	 */
	public void changeState(State new_state) {
		// make sure both states are both valid before attempting to
		// call their methods
		if (current_state == null || new_state == null) {
			System.err.println("STATE DOES NOT EXIST");
		}

		// call the exit method of the existing state
		current_state.exit(this);

		// change state to the new state
		current_state = new_state;

		// call the entry method of the new state
		current_state.enter(this);
	}

	public void sleep(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void right(double degrees) {
		motRight.setPower(baseMotorPower);
		motLeft.setPower(baseMotorPower);
		motRight.stop();
		motLeft.stop();

		double t_init, t_final;
		t_init = motLeft.getTachoCount();
		t_final = (int) (degrees * angleError)
				* (robotDiameter / wheelDiameter) + t_init;

		while (motLeft.getTachoCount() < t_final) {
			motRight.backward();
			motLeft.forward();
		}
		motRight.stop();
		motLeft.stop();
	}

	public void left(double degrees) {
		motRight.setPower(baseMotorPower);
		motLeft.setPower(baseMotorPower);
		motRight.stop();
		motLeft.stop();

		double t_init, t_final;
		t_init = motRight.getTachoCount();
		t_final = (int) (degrees * angleError)
				* (robotDiameter / wheelDiameter) + t_init;

		while (motRight.getTachoCount() < t_final) {
			motLeft.backward();
			motRight.forward();
			
			
		}
		motLeft.stop();
		motRight.stop();
	}

	public void forward(double distance) {
		// Makes the robot go forward for the given distance
		resetAngle();
		while ((motLeft.getTachoCount() * Math.PI / 180) * (wheelDiameter / 2) < distance) {
			double kP = 1;
			int leftAngle;
			int rightAngle;
			int error;

			forward();

			leftAngle = motLeft.getTachoCount();
			rightAngle = motRight.getTachoCount();
			error = leftAngle - rightAngle;
			motRight.setPower((int) (baseMotorPower + (error * kP)));
			motLeft.setPower((int) (baseMotorPower - (error * kP)));
		}
		stop();
		resetAngle();
	}

	public void backward(double distance) {
		// Makes the robot go backward for the given distance
		while ((motLeft.getTachoCount() * Math.PI / 180 * (wheelDiameter / 2)) > -distance) {
			double kP = 1;
			int leftAngle;
			int rightAngle;
			int error;

			backward();

			leftAngle = motLeft.getTachoCount();
			rightAngle = motRight.getTachoCount();
			error = leftAngle - rightAngle;
			motRight.setPower((int) (baseMotorPower + (error * kP)));
			motLeft.setPower((int) (baseMotorPower - (error * kP)));
		}
		stop();
		resetAngle();
	}

	public void forward() {
		// Makes the robot go forward in a straight line
		motLeft.forward();
		motRight.forward();

	}

	public void backward() {
		// Makes the robot go forward
		motLeft.backward();
		motRight.backward();
	}

	public void stop() {
		// Stops both wheel motors
		motLeft.stop();
		motRight.stop();
	}

	public void resetAngle() {
		motLeft.resetTachoCount();
		motRight.resetTachoCount();
	}

	/**
	 * left turns return negative angles, and right turns positive angles
	 * 
	 * @return
	 */
	public double getAngle() {

		double t_current = motRight.getTachoCount();
		return (t_current * (wheelDiameter / robotDiameter) * (1 / angleError));
	}
}
