package rescue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Properties;

import lejos.nxt.*;
import lejos.nxt.addon.*;
import lejos.nxt.comm.BTConnection;
//import lejos.nxt.comm.Bluetooth;

public class Robot {

	// this is a singleton
	private static Robot robot;

	public boolean exit = false;

	String name;

	// Set in constructor; depends on robot name
	double wheelDiameter; // both in cm
	double robotDiameter;
	double angleError;
	int threshUS = 4;
	int threshEOPD = 2;
	boolean leftBlack = false;
	boolean rightBlack = false;
	boolean avoidedLeft = true;
	boolean isOnRamp = false;
	int threshSilver = 65;
	int threshBlack = 50;
	float compOffset = 0.0f;
	double headingNorth = 176.0; // measured heading of room north
	double headingNorthEast = 133.0; // measured heading of room northeast
	double headingEast = 90.0; // measured heading of room east
	double headingSouthEast = 47.0; // measured heading of room southeast
	double headingSouth = 354.0; // measured heading of room south
	double headingSouthWest = 315.0; // measured heading of room southwest
	double headingWest = 268.0; // measured heading of room west
	double headingNorthWest = 219.0; // measured heading of room northwest
	int finalCanDist = 0;

	double kScale = 78.36;
	double kError = 4.7; // original value 5.7 returning -1 values
	public NXTMotor motRight;
	NXTMotor motLeft;
	NXTMotor arduPower;
	private int baseMotorPower;

	NXTRegulatedMotor motRegRight;
	NXTRegulatedMotor motRegLeft;
	private int baseMotorAcceleration;
	private int baseMotorSpeed;
	private boolean isRegulated;

	ArduRCJ servoDriver;

	TouchSensor touch;
	LightSensor lightLeft;
	LightSensor lightRight;
	ColorSensor colorsensor;
	UltrasonicSensor ultrasonic;
	boolean useUltrasonicObstacleDetect = true;
	AccelHTSensor accel;
	CompassHTSensor compass;
	EOPD eopdSensor;

	State current_state;
	boolean stepMode;
	boolean useCommands;
	boolean useDebug;
	boolean enableTurnBeeps;

	public Map2D map;
	WaveFront nav;
	private int x;
	private int y;
	boolean canFound;
	boolean platformFound;
	boolean canHeld;
	private boolean gridDone;

	BTConnection btc;
	DataInputStream inStream;
	DataOutputStream outStream;

	// ------------------------------------------------------------------------
	private Robot() {
		// check which robot this and set diameters, sensors
		Properties props = Settings.getProperties();
		name = props.getProperty("lejos.usb_name");
		if (name.equals("NXTChris")) {
			wheelDiameter = 5.6; // both in cm
			robotDiameter = 13.6;
			angleError = (360.0 / 305.0);
			servoDriver = new ArduRCJ(SensorPort.S1);
			arduPower = new NXTMotor(MotorPort.A); // power arduino
			accel = new AccelHTSensor(SensorPort.S2);
			compass = new CompassHTSensor(SensorPort.S3);
			ultrasonic = new UltrasonicSensor(SensorPort.S4);
			ultrasonic.continuous();
			eopdSensor = null;
		} else if (name.equals("ebay")) {
			wheelDiameter = 5.6; // both in cm
			robotDiameter = 13.6;
			angleError = 1; // (360.0/276.0)

			// lightLeft = new LightSensor(SensorPort.S1);
			// lightRight = new LightSensor(SensorPort.S2);
			compass = new CompassHTSensor(SensorPort.S3);
			servoDriver = new ArduRCJ(SensorPort.S4);
			arduPower = new NXTMotor(MotorPort.A); // power arduino
			// For direct connect: eopdSensor = new EOPD(SensorPort.S1, true /*
			// longRange */);
			eopdSensor = null;
			ultrasonic = new UltrasonicSensor(SensorPort.S2);
			ultrasonic.continuous();
		} /*
		 * else if (name.equals("LineBacker")) { wheelDiameter = 5.6;
		 * robotDiameter = 17.0; angleError = 1.0; lightLeft = new
		 * LightSensor(SensorPort.S1); ultrasonic = new
		 * UltrasonicSensor(SensorPort.S4); ultrasonic.continuous(); } else if
		 * (name.equals("Dr_Lakata")) { wheelDiameter = 5.6; robotDiameter =
		 * 15.9; angleError = 1.0; eopdSensor = new EOPD(SensorPort.S2, true);
		 * // longRange // lightLeft = new LightSensor(SensorPort.S1); //
		 * lightRight = new LightSensor(SensorPort.S2); // touch = new
		 * TouchSensor(SensorPort.S3); compass = new
		 * CompassHTSensor(SensorPort.S3); ultrasonic = new
		 * UltrasonicSensor(SensorPort.S4); ultrasonic.continuous(); }
		 */else if (name.equals("JPNXT")) {
			// defaults for Jeremy
			wheelDiameter = 4.96;
			robotDiameter = 13.5;
			angleError = 1.0;

			// colorsensor = new ColorSensor(SensorPort.S1);
			lightLeft = new LightSensor(SensorPort.S1);
			lightRight = new LightSensor(SensorPort.S2);
			compass = new CompassHTSensor(SensorPort.S3);
			ultrasonic = new UltrasonicSensor(SensorPort.S4);
			ultrasonic.continuous();

		} else {
			// Unknown robot
		}

		motRight = new NXTMotor(MotorPort.B);
		motLeft = new NXTMotor(MotorPort.C);
		setBaseMotorPower(60);

		motRegRight = new NXTRegulatedMotor(MotorPort.B);
		motRegLeft = new NXTRegulatedMotor(MotorPort.C);
		setBaseMotorAcceleration(1500);
		setBaseMotorSpeed(500);
		stop();

		current_state = StateStart.getInstance();
		stepMode = false;
		useCommands = false;
		useDebug = false;
		enableTurnBeeps = true;

		map = new Map2D();
		resetGrid();
		nav = new WaveFront(map);
		setGridDone(false);

		// actual BT connection is done in StateCommand
		btc = null;
		inStream = null;
		outStream = null;

		// Power up Roboduino
		setArduinoPoweredUp(true);
	}

	/**
	 * @return true if Arduino is On (powered up), false if it's Off
	 */
	public boolean getArduinoPoweredUp() {
		if (arduPower != null) {
			if (arduPower.getPower() == 100) {
				return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------------
	public void setArduinoPoweredUp(boolean powerOn) {
		if (arduPower != null) {
			if (powerOn == true) {
				// Turn on Arduino power; lower compass
				arduPower.setPower(100);
				arduPower.forward();
				sleep(6000);
				Sound.beep();
				dropCompass();
				dropClaw();
				openClaw();

			} else {
				// Turn off Arduino
				arduPower.setPower(0);
				arduPower.flt();
			}
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getWheelDiameter() {
		return wheelDiameter;
	}

	public void setWheelDiameter(double wheelDiameter) {
		this.wheelDiameter = wheelDiameter;
	}

	public double getRobotDiameter() {
		return robotDiameter;
	}

	public void setRobotDiameter(double robotDiameter) {
		this.robotDiameter = robotDiameter;
	}

	public int getBaseMotorSpeed() {
		return baseMotorSpeed;
	}

	public void setBaseMotorSpeed(int baseMotorSpeed) {
		this.baseMotorSpeed = baseMotorSpeed;
		motRegRight.setSpeed(baseMotorSpeed);
		motRegLeft.setSpeed(baseMotorSpeed);
	}

	public int getBaseMotorAcceleration() {
		return baseMotorAcceleration;
	}

	public void setBaseMotorAcceleration(int baseMotorAcceleration) {
		this.baseMotorAcceleration = baseMotorAcceleration;
		motRegRight.setAcceleration(baseMotorAcceleration);
		motRegLeft.setAcceleration(baseMotorAcceleration);
	}

	public int getBaseMotorPower() {
		return baseMotorPower;
	}

	public void setBaseMotorPower(int baseMotorPower) {
		this.baseMotorPower = baseMotorPower;
		motRight.setPower(baseMotorPower);
		motLeft.setPower(baseMotorPower);
	}

	public void setGridDone(boolean gridDone) {
		this.gridDone = gridDone;
	}

	public boolean isGridDone() {
		return gridDone;
	}

	public int getDir() {

		return (int) getHeading();
	}

	public float getCompOffset() {
		return compOffset;
	}

	public void setCompOffset(float val) {
		compOffset = val;
	}

	public void setDir(int direction) {
		compOffset = 0.0f;
		double compHeading;
		while (direction < 0) {
			direction += 360;
		}
		while (direction > 360) {
			direction -= 360;
		}
		compHeading = getHeading();
		compOffset = (int) (Util.round(compHeading - direction));
		debugln("compOffset:" + compOffset);
	}

	// ------------------------------------------------------------------------
	public float getHeading() {
		float cReading = 0.0f;
		float correctedCReading = 0.0f;

		if (compass != null) {
			if (!isCompassUp()) {
				liftCompass();
				sleep(1000);
			}
			cReading = compass.getDegrees();
		}
		correctedCReading = 360 - cReading + 90;
		// debugln("Old Heading: " + correctedCReading);
		if (correctedCReading >= 360) {
			correctedCReading = correctedCReading - 360;
		}
		correctedCReading = correctedCReading - compOffset;
		// debugln("New Heading: " + correctedCReading);
		if (correctedCReading < 0) {
			correctedCReading = correctedCReading + 360;
		}
		return correctedCReading;
	}

	public int getLightLeft() {
		int val = 1024 - servoDriver.readLightLeft();
		return (val / 10);
	}

	public int getLightRight() {
		int val = 1024 - servoDriver.readLightRight();
		return (val / 10);
	}

	public static Robot getRobot() {
		if (robot == null) {
			robot = new Robot();
		}
		return robot;
	}
	
	public void joystickControl(double x, double y, int button) {
		variableTurn((int)(75.0*x), (int)(-100.0*y));
		if(button == 128) {
			servoDriver.servoClawLift.setAngle(95);
		} else if(button == 64){
			servoDriver.servoClawLift.setAngle(75);
		} else {
			servoDriver.servoClawLift.setAngle(86);
		}
		if(button == 16) {
			closeClaw();
		} else if(button == 32) {
			openClaw();
		}
		if(button == 4096) {
			liftCompass();
		} else if(button == 16384) {
			dropCompass();
		}
		if(button == 1) {
			Sound.playTone(220, 100);
		} else if(button == 2) {
			Sound.playTone(330, 100);
		} else if(button == 4) {
			Sound.playTone(440, 100);
		} else if(button == 8) {
			Sound.playTone(550, 100);
		}
	}
	
	public void variableTurn(int turnCurve, int turnPower) {
		
		if(turnPower == 0 && turnCurve == 0) {
			motLeft.stop();
			motRight.stop();
		} else if(turnPower == 0) {
			setBaseMotorPower(-turnCurve);
			motLeft.backward();
			motRight.forward();	
		} else {
			motLeft.setPower(turnPower+turnCurve);
			motRight.setPower(turnPower-turnCurve);
			motLeft.forward();
			motRight.forward();
		}
	}

	public static void playTone(int freq, int duration) {
		if (robot.enableTurnBeeps == true) {
			Sound.playTone(freq, duration);
		}
	}

	public void canSequence() {
		setBaseMotorSpeed(50);
		while (robot.getEOPDProcessedValue() > 70) {
			robot.backward();
		}
		robot.stop();
		liftCan();
		setBaseMotorSpeed(500);
	}

	public void toggleBeeps() {
		enableTurnBeeps = !enableTurnBeeps;
	}

	public void victorySong() {
		playTone(440, 100);
		sleep(100);
		playTone(220, 100);
		sleep(100);
		playTone(880, 100);
		sleep(100);
		playTone(220, 100);
		sleep(100);
		playTone(440, 100);
		sleep(100);
		playTone(880, 100);
		sleep(100);
	}

	public void resetGrid() {
		setX(1);
		setY(1);
		canFound = false;
		platformFound = false;
		canHeld = false;
		StateGridRunNew.getInstance().enterGrid = true;
		setGridDone(false);

		// setDir(90);
		map.reset();
		map.grid[x][y] = Map2D.ROBOT;
	}

	/**
	 * Print message over Bluetooth connection if it's running
	 * 
	 * @param msg
	 */
	public void debug(String msg) {
		if (useDebug == false)
			return;
		if ((btc != null) && (outStream != null)) {
			try {
				outStream.writeUTF(msg);
				outStream.flush();
			} catch (IOException e) {
				LCD.drawString("Rbt IO Err", 0, 1);
			}
		}
		// else {
		// For competition, do nothing here
		// System.out.println(msg);
		// RConsole.print(msg);
		// }
	}

	public void debugln(String msg) {
		if (useDebug == false)
			return;
		debug(msg + "\n");
	}

	public void run() {
		while (Button.ESCAPE.isUp() && !this.exit) {
			update();
		}
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

	// ------------------------------------------------------------------------
	// public void compassCardinalCalibrate(){
	// while(Button.ENTER.isUp()){
	// }
	// cardinal0 = getHeading() + compOffset;
	// debugln("cardinal0 = "+cardinal0);
	// while(Button.ENTER.isDown()){
	// }
	// while(Button.ENTER.isUp()){
	// }
	// cardinal45 = getHeading() + compOffset;
	// debugln("cardinal45 = "+cardinal45);
	// while(Button.ENTER.isDown()){
	// }
	// while(Button.ENTER.isUp()){
	// }
	// cardinal90 = getHeading() + compOffset;
	// debugln("cardinal90 = "+cardinal90);
	// while(Button.ENTER.isDown()){
	// }
	// while(Button.ENTER.isUp()){
	// }
	// cardinal135 = getHeading() + compOffset;
	// debugln("cardinal135 = "+cardinal135);
	// while(Button.ENTER.isDown()){
	// }
	// while(Button.ENTER.isUp()){
	// }
	// cardinal180 = getHeading() + compOffset;
	// debugln("cardinal180 = "+cardinal180);
	// while(Button.ENTER.isDown()){
	// }
	// while(Button.ENTER.isUp()){
	// }
	// cardinal225 = getHeading() + compOffset;
	// debugln("cardinal225 = "+cardinal225);
	// while(Button.ENTER.isDown()){
	// }
	// while(Button.ENTER.isUp()){
	// }
	// cardinal270 = getHeading() + compOffset;
	// debugln("cardinal270 = "+cardinal270);
	// while(Button.ENTER.isDown()){
	// }
	// while(Button.ENTER.isUp()){
	// }
	// cardinal315 = getHeading() + compOffset;
	// debugln("cardinal315 = "+cardinal315);
	// while(Button.ENTER.isDown()){
	// }
	// }

	// ------------------------------------------------------------------------
	public void goToHeading(double angle) {
		if (angle < 0) {
			angle += 360;
		}
		if (angle > 360) {
			angle -= 360;
		}

		float diff = (float) (angle - getHeading());
		if (diff < -180) {
			diff = diff + 360;
		}
		if (diff > 180) {
			diff = diff - 360;
		}
		if (diff > 0) {
			correctLeft(Math.abs(diff));
		}
		if (diff < 0) {
			correctRight(Math.abs(diff));
		}
	}

	// ------------------------------------------------------------------------
	public void correctRight(float degrees) {
		float origin = getHeading();
		float expectedVal = origin - degrees;

		right(degrees);
		sleep(500);
		float val = 0;
		val = getHeading();
		if (expectedVal < 0) {
			expectedVal = expectedVal + 360;
		}

		while (val != expectedVal) {
			if (expectedVal == 360) {
				expectedVal = 0;
			}
			if (expectedVal > 360) {
				expectedVal = expectedVal - 360;
			}
			if (val - expectedVal > 180) {
				expectedVal = expectedVal + 360;
			}
			if (expectedVal - val > 180) {
				val = val + 360;
			}
			if (val > expectedVal) {
				setBaseMotorAcceleration(1800);
				if (val - expectedVal < 3) {
					right(3);
				}
				right(val - expectedVal);
				setBaseMotorAcceleration(1500);
			} else {
				setBaseMotorAcceleration(1800);
				if (val - expectedVal < 3) {
					left(3);
				}
				left(expectedVal - val);
				setBaseMotorAcceleration(1500);
			}

			sleep(700);
			val = getHeading();
			// debugln("" + expectedVal);
		}

		sleep(100);
		// debugln("Original Heading: " + firstDir);
		// debugln("Current Heading: " + getDir());
	}

	// ------------------------------------------------------------------------
	public void correctLeft(float degrees) {
		float origin = getHeading();
		float expectedVal = origin + degrees;

		left(degrees);
		sleep(500);
		float val = getHeading();

		while (val != expectedVal) {
			if (expectedVal == 360) {
				expectedVal = 0;
			}
			if (expectedVal > 360) {
				expectedVal = expectedVal - 360;
			}
			if (expectedVal < 0) {
				expectedVal = expectedVal + 360;
			}
			if (val - expectedVal > 180) {
				expectedVal = expectedVal + 360;
			}
			if (expectedVal - val > 180) {
				val = val + 360;
			}
			if (val > expectedVal) {
				setBaseMotorAcceleration(1800);
				if (val - expectedVal < 3) {
					right(3);
				}
				right(val - expectedVal);
				setBaseMotorAcceleration(1500);
			} else {
				setBaseMotorAcceleration(1800);
				if (val - expectedVal < 3) {
					left(3);
				}
				left(expectedVal - val);
				setBaseMotorAcceleration(1500);
			}

			sleep(700);
			val = getHeading();
		}

		sleep(100);
		// debugln("Original Heading: " + firstDir);
		// debugln("Current Heading: " + getDir());
	}

	// ---------- begin new Right/Left -----------
	public void right(double degrees) {
		if (!isRegulated) {
			isRegulated = true;
		}

		int angle = (int) ((degrees * angleError) * (getRobotDiameter() / getWheelDiameter()));

		motRegRight.resetTachoCount();
		motRegLeft.resetTachoCount();
		motRegRight.rotate(-angle, true);
		motRegLeft.rotate(angle, true);
		while (motRegRight.isMoving() || motRegLeft.isMoving()) {
			sleep(10);
		}
		stop();
		// debugln("comp " + getHeading());
	}

	public void left(double degrees) {
		if (!isRegulated) {
			isRegulated = true;
		}

		int angle = (int) ((degrees * angleError) * (getRobotDiameter() / getWheelDiameter()));

		motRegRight.resetTachoCount();
		motRegLeft.resetTachoCount();
		motRegRight.rotate(angle, true);
		motRegLeft.rotate(-angle, true);
		while (motRegRight.isMoving() || motRegLeft.isMoving()) {
			sleep(10);
		}
		stop();
		// debugln("comp " + getHeading());
	}

	// ---------- end of new Right/Left -----------

	public void forward() {
		// Makes the robot go forward in a straight line
		if (!isRegulated) {
			isRegulated = true;
		}
		motRegRight.forward();
		motRegLeft.forward();
	}

	public boolean forward(double distance) {
		// Makes the robot go forward for the given distance
		if (!isRegulated) {
			isRegulated = true;
		}
		int angle;
		angle = (int) Util.round(distance / (getWheelDiameter() * Math.PI) * 360);

		motRegRight.resetTachoCount();
		motRegLeft.resetTachoCount();
		motRegRight.rotate(angle, true);
		motRegLeft.rotate(angle, true);
		while (motRegRight.isMoving() || motRegLeft.isMoving()) {
			if (motRegRight.isStalled() && motRegLeft.isStalled()) {
				return false;
			}
			sleep(10);
		}
		return true;
	}

	public void backward() {
		// Makes the robot go forward
		if (!isRegulated) {
			isRegulated = true;
		}
		motRegRight.backward();
		motRegLeft.backward();
	}

	// ------------------------------------------------------------------------
	public boolean backward(double distance) {
		// Makes the robot go backward for the given distance
		if (!isRegulated) {
			isRegulated = true;
		}
		int angle;
		angle = (int) Util.round(distance / (getWheelDiameter() * Math.PI) * 360);

		motRegRight.resetTachoCount();
		motRegLeft.resetTachoCount();
		motRegRight.rotate(-angle, true);
		motRegLeft.rotate(-angle, true);
		while (motRegRight.isMoving() || motRegLeft.isMoving()) {
			if (motRegRight.isStalled() && motRegLeft.isStalled()) {
				return false;
			}
			sleep(10);
		}
		stop();
		return true;
	}

	// ------------------------------------------------------------------------
	public void stop() {
		// Stops both wheel motors
		if (isRegulated) {
			motRegLeft.stop(true);
			motRegRight.stop(true);
			Robot.playTone(440, 10);
			sleep(100);
			motRegLeft.suspendRegulation();
			motRegRight.suspendRegulation();
		} else {
			motLeft.stop();
			motRight.stop();
		}
	}

	// ------------------------------------------------------------------------
	public boolean obstacleSideCheck() {
		// Checks to see whether distance to left is longer than distance to
		// right. Returns true is left is longer.
		sleep(500);
		correctRight(90);
		sleep(1000);

		int aveRightDist;
		aveRightDist = sonicAverage();

		if (aveRightDist < 15) {
			backward(5);
			stop();
		}

		correctLeft(180);
		sleep(500);

		int aveLeftDist;
		aveLeftDist = sonicAverage();
		if (aveLeftDist < 15) {
			backward(5);
			stop();
		}
		debugln("right: " + aveRightDist + ". Left: " + aveLeftDist);
		if (aveLeftDist > aveRightDist) {
			return true;
		} else {
			right(180);
			return false;
		}
	}

	// ------------------------------------------------------------------------
	public boolean forwardLookForLine(double distance) {
		// Makes the robot go forward for the given distance, and stop if it
		// sees a line
		leftBlack = false;
		rightBlack = false;

		// Makes the robot go forward for the given distance
		if (!isRegulated) {
			isRegulated = true;
		}
		int angle;
		angle = (int) Util.round(distance / (getWheelDiameter() * Math.PI) * 360);

		motRegRight.resetTachoCount();
		motRegLeft.resetTachoCount();
		motRegRight.rotate(angle, true);
		motRegLeft.rotate(angle, true);
		while (motRegRight.isMoving() || motRegLeft.isMoving()) {

			if (leftBlack != true) {
				if (getLightLeft() < threshBlack) {
					leftBlack = true;
				}
			}

			if (rightBlack != true) {
				if (getLightRight() < threshBlack) {
					rightBlack = true;
				}
			}

			if (leftBlack == true && rightBlack == true) {
				debug("I Should Stop Here");
				stop();
				return true;
			}

		}

		// forward();
		//
		// if (rightBlack == true) {
		// motRight.setPower(-20);
		// motLeft.setPower(50);
		// } else if (leftBlack == true) {
		// motLeft.setPower(-20);
		// motRight.setPower(50);
		// }

		// stop();
		return false;
	}

	// ------------------------------------------------------------------------
	public boolean correctLeftLine(float degrees) {
		boolean line = false;
		float origin = getHeading();
		float expectedVal = origin + degrees;
		line = turnLeftLookForLine(degrees);
		sleep(100);
		float val = getHeading();
		// debugln("" + val);
		val = getHeading();
		// debugln("" + val);

		while ((val != expectedVal) && !line) {
			if (expectedVal < 0) {
				expectedVal = expectedVal + 360;
			}
			if (val - expectedVal > 180) {
				expectedVal = expectedVal + 360;
			}
			if (expectedVal - val > 180) {
				val = val + 360;
			}
			if (val > expectedVal) {
				line = turnRightLookForLine(val - expectedVal);
			} else {
				line = turnLeftLookForLine(expectedVal - val);
			}
			if (expectedVal == 360) {
				expectedVal = 0;
			}
			if (expectedVal > 360) {
				expectedVal = expectedVal - 360;
			}

			sleep(100);
			val = getHeading();
			// debugln("" + expectedVal);
		}
		return line;
	}

	// ------------------------------------------------------------------------
	public boolean correctRightLine(float degrees) {
		boolean line = false;
		float origin = getHeading();
		float expectedVal = origin - degrees;
		line = turnRightLookForLine(degrees);
		sleep(100);
		float val = getHeading();
		// debugln("" + val);
		val = getHeading();
		// debugln("" + val);
		if (expectedVal < 0) {
			expectedVal = expectedVal + 360;
		}

		while ((val != expectedVal) && !line) {
			if (val - expectedVal > 180) {
				expectedVal = expectedVal + 360;
			}
			if (expectedVal - val > 180) {
				val = val + 360;
			}
			if (val > expectedVal) {
				line = turnRightLookForLine(val - expectedVal);
			} else {
				line = turnLeftLookForLine(expectedVal - val);
			}
			if (expectedVal == 360) {
				expectedVal = 0;
			}
			if (expectedVal > 360) {
				expectedVal = expectedVal - 360;
			}
			sleep(100);
			val = getHeading();
			// debugln("" + expectedVal);
		}
		return line;
	}

	public boolean turnLeftLookForLine(double degrees) {
		// Makes the robot turn left the given degrees, and stop if it
		// sees a line
		leftBlack = false;
		rightBlack = false;
		int angle = (int) ((degrees /* angleError */) * (getRobotDiameter() / getWheelDiameter()));

		if (!isRegulated) {
			isRegulated = true;
		}

		motRegRight.resetTachoCount();
		motRegLeft.resetTachoCount();
		motRegRight.rotate(angle, true);
		motRegLeft.rotate(-angle, true);
		while (motRegRight.isMoving() || motRegLeft.isMoving()) {
			if (getLightLeft() < threshBlack) {
				leftBlack = true;
				stop();
				return true;
			}

			// if (rightBlack != true) {
			// if (lightRight.getLightValue() < 45) {
			// rightBlack = true;
			// }
			// }
			// if (leftBlack == true && rightBlack == true) {
			// debug("I Should Stop Here");
			// stop();
			// return true;
			// }

		}
		// if (rightBlack == true) {
		// motRight.setPower(0);
		// motLeft.setPower(50);
		// debugln("rightblack = true");
		// } else if (leftBlack == true) {
		// motLeft.setPower(0);
		// motRight.setPower(50);
		// debugln("leftblack = true");
		// }

		stop();
		return false;
	}

	// ------------------------------------------------------------------------
	public boolean turnRightLookForLine(double degrees) {
		// Makes the robot turn right for the given degrees, and stop if it
		// sees a line
		leftBlack = false;
		rightBlack = false;
		int angle = (int) ((degrees /* angleError */) * (getRobotDiameter() / getWheelDiameter()));

		if (!isRegulated) {
			isRegulated = true;
		}

		motRegRight.resetTachoCount();
		motRegLeft.resetTachoCount();
		motRegRight.rotate(-angle, true);
		motRegLeft.rotate(angle, true);
		while (motRegRight.isMoving() || motRegLeft.isMoving()) {
			// if (leftBlack != true) {
			// if (lightLeft.getLightValue() < 45) {
			// leftBlack = true;
			// }
			// }
			if (getLightRight() < threshBlack) {
				rightBlack = true;
				stop();
				return true;
			}
			// if (leftBlack == true && rightBlack == true) {
			// debug("I Should Stop Here");
			// stop();
			// return true;
			// }
		}

		stop();
		return false;

	}

	// ------------------------------------------------------------------------
	public void findLineRight() {
		// makes the robot turn right, looking to reposition itself so as to
		// resume
		// normal line following.
		debugln("enter findline");
		stop();
		// motRegLeft.forward();
		correctRightLine(180);
		// while (lightLeft.getLightValue() > 45) {
		// }
		stop();
		debugln("stop findline");
	}

	public void findLineLeft() {
		// makes the robot turn left, looking to reposition itself so as to
		// resume
		// normal line following.
		debugln("enter findline");
		stop();
		// motRegRight.forward();
		correctLeftLine(180);
		// while (lightRight.getLightValue() > 45) {
		// }
		stop();
		debugln("stop findline");
	}

	// ------------------------------------------------------------------------
	public int getTouchPressed() {
		int val = servoDriver.readTouch();
		return val;
	}

	// ------------------------------------------------------------------------
	public int getEOPDScaled() {
		int val = 1023 - getEOPDRawValue();
		return (val / 10);
	}

	public int getEOPDRawValue() {
		int val;
		if (eopdSensor != null) {
			// sensor is directly connected to NXT
			val = eopdSensor.readRawValue();
		} else {
			// arduino
			val = servoDriver.readEOPD();
		}
		return val;
	}

	/**
	 * This mimics the HiTechnic programming block.
	 * 
	 * @return A value between 0 and 100.
	 */
	public int getEOPDProcessedValue() {
		return 100 - (int) (Math.sqrt((1023 - getEOPDRawValue()) * 10));
	}

	public double eopdAverage() {
		int average = 0;
		int count = 5;

		while (count > 0) {
			average = average + (int) getEOPDProcessedValue();
			count = count - 1;
		}
		average = average / 5;

		return average;
	}

	public double getEopdDistance() {
		double distance = Util.round(kScale / Math.sqrt((double) getEOPDRawValue()) - kError);
		// debugln("" + distance + " (" + kScale/Math.sqrt((double)
		// getEOPDRaw()) + ")");
		return distance;
	}

	public int sonicAverage() {
		int average = 0;
		int count = 0;
		while (count < 5) {
			average += ultrasonic.getDistance();

			count++;
		}
		average = average / count;
		return average;
	}

	// ------------------------------------------------------------------------
	public void findCanCoarseEOPD() {
		setBaseMotorPower(35);
		setBaseMotorSpeed(500);
		correctLeft(50);

		int currentValue = (int) eopdAverage();
		int lastValue = currentValue;
		int difference = 0;

		while (true) {
			right(4);
			currentValue = (int) eopdAverage();
			if (lastValue - currentValue >= threshEOPD) {
				debugln("Final EOPD Dif: " + (lastValue - currentValue));
				break;
			}
			difference = lastValue - currentValue;
			debugln("Dif " + difference);
			sleep(10);
			lastValue = currentValue;
		}
		stop();
		sleep(1000);
		debug("Head L " + getHeading());
		double headL = getHeading();
		correctRight(90);
		currentValue = (int) eopdAverage();
		lastValue = currentValue;

		while (true) {
			left(4);
			currentValue = (int) eopdAverage();
			if (lastValue - currentValue >= threshEOPD) {
				debugln("Final EOPD Dif: " + (lastValue - currentValue));
				break;
			}
			difference = lastValue - currentValue;
			debugln("Dif " + difference);
			sleep(10);
			lastValue = currentValue;
		}
		stop();
		sleep(1000);
		debugln("headR " + getHeading());
		double headR = getHeading();
		double headC;

		if (Math.abs(headL - headR) > 180) {
			headC = Util.round((headR + headL) / 2 - 180);
		} else {
			headC = Util.round((headR + headL) / 2);
		}

		debugln("Head C " + headC);
		goToHeading(Util.round(headC)); // findCanFine();
		stop();
		finalCanDist = sonicAverage();
		sleep(100);
		stop();
	}

	// ------------------------------------------------------------------------
	public void findCanCoarseSonic() {
		setBaseMotorSpeed(500);
		correctLeft(75);
		int storage;
		int degreesL = 0;
		int degreesR = 0;
		int headS = (int) getHeading();

		int currentValue = 40;
		if ((sonicAverage() < 40)) {
			currentValue = sonicAverage();
		}
		int lastValue = currentValue;
		int difference = 0;

		while (degreesL < 400) {
			right(4);
			if (lastValue - currentValue > threshUS) {
				break;
			}
			lastValue = currentValue;
			storage = sonicAverage();
			if (storage < 40) {
				currentValue = storage;
			}
			difference = lastValue - currentValue;
			debug("Cur " + currentValue);
			debug(" | Last " + lastValue);
			debugln(" | Dif " + difference);
			degreesL = degreesL + 4;
		}
		if (degreesL >= 399) {
			goToHeading(headS);
		}

		stop();
		sleep(1000);
		debug("Head L " + getHeading());
		double headL = getHeading();
		// if (headL > 180)
		// {headL = headL - 360;}
		correctRight(90);
		currentValue = 40;
		if (sonicAverage() < 40) {
			currentValue = sonicAverage();
		}
		lastValue = currentValue;

		while (degreesR < 400) {
			left(4);
			if (lastValue - currentValue > threshUS) {
				break;
			}
			lastValue = currentValue;
			storage = sonicAverage();
			if (storage < 40) {
				currentValue = storage;
			}
			difference = lastValue - currentValue;
			debug("Cur " + currentValue);
			debug(" | Last " + lastValue);
			debugln(" | Dif " + difference);
			degreesR = degreesR + 4;
		}
		if (degreesR >= 399) {
			goToHeading(headL);
		}
		stop();
		sleep(1000);
		debugln("headR " + getHeading());
		double headR = getHeading();
		// if (headR > 180)
		// {headR = headR - 360;}
		double headC;
		if (degreesL < 399) {
			if (Math.abs(headL - headR) > 180) {
				headC = Util.round((headR + headL) / 2 - 180);
			} else {
				headC = Util.round((headR + headL) / 2);
			}
		} else
			headC = headR;

		debugln("Head C " + headC);
		goToHeading(Util.round(headC)); // findCanFine();
		stop();
		finalCanDist = sonicAverage();
		sleep(100);
		stop();
	}

	// ------------------------------------------------------------------------
	public boolean isCanInSquare() {
		boolean foundCan = false;
		int sweepResolution = 5;
		int degrees = 0;
		int storage;

		setBaseMotorPower(20);
		setBaseMotorSpeed(500);
		robot.correctLeft(20);

		int currentValue = 45;
		if ((sonicAverage() < 45)) {
			currentValue = sonicAverage();
		}
		int lastValue = currentValue;
		int counter = 0;
		while (degrees < 40) {
			right(sweepResolution);
			storage = sonicAverage();
			if (storage < 45) {
				currentValue = storage;
			}
			// if (Math.abs(lastValue - currentValue) > thresh) {
			// foundCan = true;
			// }
			if (currentValue < 31) {
				counter++;
			}
			if (counter > 3) {
				foundCan = true;
			}
			debugln("Cur " + currentValue + " | Dif " + (lastValue - currentValue));
			degrees += sweepResolution;
			lastValue = currentValue;
		}

		robot.stop();
		robot.left(20);
		debugln("Can: " + foundCan);
		return foundCan;
	}

	public boolean isCanInSquareCorners() {
		boolean foundCan = false;
		int sweepResolution = 5;
		int degrees = 0;
		int storage;
		int difference;
		int lowVal = 100;

		setBaseMotorPower(20);
		setBaseMotorSpeed(500);
		robot.correctLeft(45);

		int currentValue = 45;
		if ((sonicAverage() < 45)) {
			currentValue = sonicAverage();
		}
		int lastValue = currentValue;

		while (degrees < 90) {
			right(4);
			if (lastValue - currentValue > threshUS) {
				foundCan = true;
			}
			lastValue = currentValue;
			storage = sonicAverage();
			if (storage < 40) {
				currentValue = storage;
			}
			difference = lastValue - currentValue;
			if (currentValue < lowVal) {
				lowVal = difference;
			}
			debug("Cur " + currentValue);
			debug(" | Last " + lastValue);
			debugln(" | Dif " + difference);
			degrees = degrees + 4;
		}
		lowVal = Math.abs(lowVal);
		robot.stop();
		robot.left(45);
		robot.forward(lowVal - 3);
		debugln("Can: " + foundCan);
		return foundCan;
	}

	// ------------------------------------------------------------------------
	public void findCanFine() {
		int dist = ultrasonic.getDistance();
		motLeft.backward();
		motRight.forward();
		while (true) {
			dist = ultrasonic.getDistance();
			if (dist < 30) {
				if (dist - ultrasonic.getDistance() > 0) {
					motLeft.backward();
					motRight.forward();
				} else {
					stop();
					left(5);
					break;
				}
			}
		}
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
		return (t_current * (getWheelDiameter() / getRobotDiameter()) * (1 / angleError));
	}

	/**
	 * Functions used by StateGridRun, but which made more sense being placed in
	 * Robot
	 * 
	 * @return
	 */
	public void faceDir(int n) {

		goToHeading(n);

		// double i = 0;
		//
		// if (n == 0){
		// i = cardinal0;
		// } else if(n == 45){
		// i = cardinal45;
		// } else if(n == 90){
		// i = cardinal90;
		// } else if(n == 135){
		// i = cardinal135;
		// } else if(n == 180){
		// i = cardinal180;
		// } else if(n == 225){
		// i = cardinal225;
		// } else if(n == 270){
		// i = cardinal270;
		// } else if(n == 315){
		// i = cardinal315;
		// } else {
		// i = n;
		// }
		//
		// goToHeading(i);

		// int diff = n - getDir();
		// //debugln("faceDir: diff = " + diff);
		// if (diff == 0) {
		// return;
		// } else {
		// if (diff > 180) {
		// correctRight(Math.abs(360 - diff));
		// } else if (diff > 0) {
		// correctLeft(Math.abs(diff));
		// } else if (diff < -180) {
		// correctLeft(Math.abs(360 + diff));
		// } else {
		// correctRight(Math.abs(diff));
		// }
		// }
		// // setDir(n);
	}

	public void faceDir(char c) {

		if ((c == 'w')) {
			faceUp();

		} else if (c == 'e') {
			faceUpRight();

		} else if (c == 'd') {
			faceRight();

		} else if (c == 'c') {
			faceDownRight();

		} else if (c == 'x') {
			faceDown();

		} else if (c == 'z') {
			faceDownLeft();

		} else if (c == 'a') {
			faceLeft();

		} else if (c == 'q') {
			faceUpLeft();

		} else {
			Robot.playTone(440, 100);
			sleep(100);
		}

	}

	private boolean goForward(int dir) {
		double factor = 1.0;

		double diffX = Math.cos(dir * (Math.PI / 180.0));
		double diffY = Math.sin(dir * (Math.PI / 180.0));

		if (diffX != Math.rint(diffX)) {
			factor = Math.abs(1 / diffX);
		}
		int nextX = getX() + (int) Util.round(factor * diffX);
		int nextY = getY() + (int) Util.round(factor * diffY);

		debugln("(" + nextX + ", " + nextY + ") dir=" + getDir());
		debugln("" + factor);
		if (ultrasonic.getDistance() > (Map2D.SCALE * factor)) {
			debugln("path is clear");
			map.grid[getX()][getY()] = 9;
			forward((factor * Map2D.SCALE));
			setX(nextX);
			setY(nextY);
			map.grid[getX()][getY()] = Map2D.ROBOT;
			return true;
		} else {
			stop();
			Sound.buzz();
			map.grid[nextX][nextY] = Map2D.CAN;
			return false;
		}
	}

	public boolean checkForPlatform() {
		setBaseMotorSpeed(150);
		backward(35);
		stop();
		sleep(1000);
		int val = getEOPDProcessedValue();
		debugln("EOPD reading: " + val);
		if (val < 90) {
			debugln("Platform found");
			forward(29);
			stop();
			setBaseMotorSpeed(500);
			return true;
		}
		debugln("NO platform");
		forward(29);
		stop();
		setBaseMotorSpeed(500);
		return false;
	}

	public boolean checkForPlatformUS() {
		forward(25);
		stop();
		sleep(1000);
		int val = sonicAverage();
		debugln("US: " + val);
		if (val < 21) {
			debugln("Platform found");
			backward(25);
			stop();
			return true;
		}
		debugln("NO platform");
		backward(25);
		stop();
		return false;
	}

	public void forwardCheckForDist(int dist) {
		forward();
		while (ultrasonic.getDistance() > dist) {
		}
		robot.stop();
	}

	public void forwardTillHit() {
		forward();
		int current = 0;
		int last = 1;
		int ticks = 0;
		while (ticks < 3) {
			if (last == current) {
				ticks = ticks + 1;
			}
			if (sonicAverage() < 40) {
				last = current;
				current = sonicAverage();
			} else {
				last = 1;
				current = 0;
				ticks = 0;
			}
			sleep(10);
			debugln("L" + last);
			debugln("T" + ticks);
			debugln("C" + current);

		}
		robot.stop();
	}

	public void faceUp() {
		goToHeading(headingNorth);
	}

	public void faceUpRight() {
		goToHeading(headingNorthEast);
	}

	public void faceRight() {
		goToHeading(headingEast);
	}

	public void faceDownRight() {
		goToHeading(headingSouthEast);
	}

	public void faceDown() {
		goToHeading(headingSouth);
	}

	public void faceDownLeft() {
		goToHeading(headingSouthWest);
	}

	public void faceLeft() {
		goToHeading(headingWest);
	}

	public void faceUpLeft() {
		goToHeading(headingNorthWest);
	}

	public void faceAway(char c) {
		if ((c == 'w')) {
			faceDown();

		} else if (c == 'e') {
			faceDownLeft();

		} else if (c == 'd') {
			faceLeft();

		} else if (c == 'c') {
			faceUpLeft();

		} else if (c == 'x') {
			faceUp();

		} else if (c == 'z') {
			faceUpRight();

		} else if (c == 'a') {
			faceRight();

		} else if (c == 'q') {
			faceDownRight();

		} else {
			Robot.playTone(440, 100);
			sleep(100);
		}
	}

	public int goLeft() {
		faceLeft();
		// faceDir(180);
		if (goForward(180)) {
			return 0;
		} else {
			return 1;
		}
	}

	public int goRight() {
		faceRight();
		// faceDir(0);
		if (goForward(0)) {
			// faceDir(0);
			return 0;
		} else {
			return 1;
		}
	}

	public int goUp() {
		faceUp();
		// faceDir(90);
		if (goForward(90)) {
			return 0;
		} else {
			return 1;
		}
	}

	public int goDown() {
		faceDown();
		// faceDir(270);
		if (goForward(270)) {
			return 0;
		} else {
			return 1;
		}
	}

	public int goUpRight() {
		faceUpRight();
		// faceDir(45);
		if (goForward(45)) {
			// faceDir(45);
			return 0;
		} else {
			return 1;
		}
	}

	public int goUpLeft() {
		faceUpLeft();
		// faceDir(135);
		if (goForward(135)) {
			return 0;
		} else {
			return 1;
		}
	}

	public int goDownLeft() {
		faceDownLeft();
		// faceDir(225);
		if (goForward(225)) {
			return 0;
		} else {
			return 1;
		}
	}

	public int goDownRight() {
		faceDownRight();
		// faceDir(315);
		if (goForward(315)) {
			return 0;
		} else {
			return 1;
		}
	}

	public void printMap() {
		debugln("------------");
		for (int j = Map2D.ROWS - 1; j >= 0; j--) {
			for (int i = 0; i < Map2D.COLS; i++) {
				debug(map.grid[i][j] + " ");
			}
			debugln("");
		}
	}

	public void followPath(int goal) {
		nav.makeWave(goal);
		debugln("Wavefront completed");
		String route = nav.makePath();
		debugln(route);
		debugln("Route to can sent to Robot");
		char dir;
		for (int n = 0; n < route.length() - 1; n++) {
			dir = route.charAt(n);

			if ((dir == 'w')) {
				goUp();

			} else if (dir == 'e') {
				goUpRight();

			} else if (dir == 'd') {
				goRight();

			} else if (dir == 'c') {
				goDownRight();

			} else if (dir == 'x') {
				goDown();

			} else if (dir == 'z') {
				goDownLeft();

			} else if (dir == 'a') {
				goLeft();

			} else if (dir == 'q') {
				goUpLeft();

			} else {
				Robot.playTone(440, 100);
				sleep(100);
			}
		}
		debugln("adjacent to object");
		faceDir(route.charAt(route.length() - 1));
		debugln("facing object");

	}

	public void goTo(int x, int y) {
		if ((map.findCoordinates(Map2D.ROBOT)[0] == x) && (map.findCoordinates(Map2D.ROBOT)[1] == y)) {
			return;
		}
		String route = nav.pathTo(x, y);
		debugln("Pathing complete to: " + x + ", " + y);
		char dir;
		for (int n = 0; n < route.length(); n++) {
			dir = route.charAt(n);

			if ((dir == 'w')) {
				goUp();

			} else if (dir == 'e') {
				goUpRight();

			} else if (dir == 'd') {
				goRight();

			} else if (dir == 'c') {
				goDownRight();

			} else if (dir == 'x') {
				goDown();

			} else if (dir == 'z') {
				goDownLeft();

			} else if (dir == 'a') {
				goLeft();

			} else if (dir == 'q') {
				goUpLeft();

			} else {
				Robot.playTone(440, 100);
				sleep(100);
			}
		}

		debugln("Routing complete to: " + x + ", " + y);
		robot.setX(x);
		robot.setY(y);

	}

	public void liftCan() {
		servoDriver.servoClawGrip.setAngle(0); // open claw
		sleep(100);
		dropClaw();
		backward(2);
		servoDriver.servoClawGrip.setAngle(180); // close claw
		sleep(1000);
		liftClaw();
		servoDriver.servoClawLift.setAngle(86);
		sleep(100);
	}

	public void dropCan() {
		servoDriver.servoClawLift.setAngle(80); // lower claw
		sleep(500); // wait for can to rest on platform
		servoDriver.servoClawLift.setAngle(86);
		servoDriver.servoClawGrip.setAngle(0);
		sleep(100);
	}

	public void dropClaw() {
		servoDriver.servoClawLift.setAngle(80); // lower claw
		while (eopdAverage() > 3) {
			sleep(10);
			debugln("eopd reading " + eopdAverage());
		}
		sleep(200);
		servoDriver.servoClawLift.setAngle(86);
		sleep(100);
	}

	public void liftClaw() {
		servoDriver.servoClawLift.setAngle(95); // raise claw
		sleep(8000);
		servoDriver.servoClawLift.setAngle(86);
		sleep(100);
	}

	public void openClaw() {
		servoDriver.servoClawGrip.setAngle(0);
	}

	public void closeClaw() {
		servoDriver.servoClawGrip.setAngle(180);
	}

	public void liftCompass() {
		servoDriver.servoCompass.setAngle(97);
	}

	public boolean isCompassUp() {
		if (servoDriver.servoCompass.getAngle() == 97) {
			return true;
		}
		return false;
	}

	public void dropCompass() {
		servoDriver.servoCompass.setAngle(0);
	}

	// public void faceTarget(int target) {
	// char dir = nav.dirTo(target);
	//
	// if (map.isInCenter(target)) {
	// } else if (dir == 'e' || dir == 'c' || dir == 'z' || dir == 'q') {
	// } else {
	// if (map.findCoordinates(target)[0] <= 2) {
	// goTo(2, 2);
	// } else {
	// goTo(3, 2);
	// }
	// }
	// faceDir(nav.dirTo(target));
	// }

	public boolean getUseCommands() {
		return useCommands;
	}

	public void setUseCommands(boolean flag) {
		useCommands = flag;
	}

	public boolean getUseDebug() {
		return useDebug;
	}

	public void setUseDebug(boolean flag) {
		useDebug = flag;
	}

	public boolean getStepMode() {
		return stepMode;
	}

	public void setStepMode(int mode) {
		if (mode != 0) {
			stepMode = true;
		} else {
			stepMode = false;
		}
	}

	public boolean getUseRConsole() {
		// TODO Auto-generated method stub
		return false;
	}

	public void status() {
		debugln(" dir = " + robot.getDir());
		debugln(" X/Y = " + robot.getX() + ", " + robot.getY());
		debugln("dist = " + robot.ultrasonic.getDistance());
		debugln("comp = " + robot.getHeading() + " (North=" + robot.compOffset + ")");

	}
}