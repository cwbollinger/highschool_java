package soccer;

import lejos.nxt.Button;

public class Navigator {

	public float ENEMY_GOAL;
	public float ALLY_GOAL;

	protected Robot bot;
	private float facingDegree;

	// private int lastTachoA;
	// private int lastTachoB;
	// private int lastTachoC;

	private int xPos;
	private int yPos;

	public static enum ZONE {
		LEFT, MID_LEFT, MIDDLE, MID_RIGHT, RIGHT
	}

	public static enum DIRECTION {
		LEFT, RIGHT, STOPPED
	}

	ZONE currentZone;
	ZONE lastZone;

	DIRECTION currentDirection;
	DIRECTION lastDirection;
	
	int centeredHeading = 5;

	public Navigator(Robot bot) {
		this.bot = bot;

		currentZone = Navigator.ZONE.MIDDLE;
		lastZone = Navigator.ZONE.MIDDLE;
		currentDirection = Navigator.DIRECTION.STOPPED;
		lastDirection = Navigator.DIRECTION.STOPPED;

	}

	public void moveDir(double dir) {
		Vector2D v = new Vector2D(Vector2D.toRadian(dir));

		double w0 = v.dot(Robot.F0) / bot.getR();
		double w1 = v.dot(Robot.F1) / bot.getR();
		double w2 = v.dot(Robot.F2) / bot.getR();

		double max = Math.max(Math.abs(w0),
				Math.max(Math.abs(w1), Math.abs(w2)));

		double scale = 100.0 / max;

		// bbot.io.debugln("" + Double.toString(scale));

		bot.motA.setPower((int) Math.round(w0 * scale));
		bot.motB.setPower((int) Math.round(w1 * scale));
		bot.motC.setPower((int) Math.round(w2 * scale));
		bot.motA.forward();
		bot.motB.forward();
		bot.motC.forward();
	}

	// NOTE: speed is not handled well for values close to and over 100.
	// Keep below 75 for predictable results. -- Chris
	public void moveDir(double dir, int speed) {
		Vector2D v = new Vector2D(Vector2D.toRadian(dir)).times(speed);

		double w0 = v.dot(Robot.F0) / bot.getR();
		double w1 = v.dot(Robot.F1) / bot.getR();
		double w2 = v.dot(Robot.F2) / bot.getR();

		bot.motA.setPower((int) Math.round(w0));
		bot.motB.setPower((int) Math.round(w1));
		bot.motC.setPower((int) Math.round(w2));
		bot.motA.forward();
		bot.motB.forward();
		bot.motC.forward();
	}

	public void moveDir(double dir, double turn) {
		Vector2D v = new Vector2D(Vector2D.toRadian(dir));

		double w0 = v.dot(Robot.F0) / bot.getR();
		double w1 = v.dot(Robot.F1) / bot.getR();
		double w2 = v.dot(Robot.F2) / bot.getR();
		
		turn = turn*60;
		
		bot.setPower((int)turn);

		double max = Math.max(Math.abs(w0)+turn,
				Math.max(Math.abs(w1)+turn, Math.abs(w2)+turn));

		double scale = 100.0 / max;

		bot.motA.setPower((int) (Math.round(w0 * scale)+Math.round(turn)));
		bot.motB.setPower((int) (Math.round(w1 * scale)+Math.round(turn)));
		bot.motC.setPower((int) (Math.round(w2 * scale)+Math.round(turn)));
		bot.motA.forward();
		bot.motB.forward();
		bot.motC.forward();
	}

	public void pointToHeading(float heading) {
		facingDegree = bot.compass.getDegrees();
		facingDegree = (float) normalizeAngle(facingDegree);
		heading = (float) normalizeAngle(heading);
		// bot.io.debugln("Heading is: " + heading);
		// bot.io.debugln("Robot is facing: " + facingDegree);
		float pointToDegree = heading - facingDegree;
		pointToDegree = (float) normalizeAngle(pointToDegree);

		// bot.io.debugln("Point to: " + pointToDegree);
		rotateTo(pointToDegree);
	}

	public void pointToHeadingArc(float heading) {
		facingDegree = bot.compass.getDegrees();
		facingDegree = (float) normalizeAngle(facingDegree);
		heading = (float) normalizeAngle(heading);
		// bot.io.debugln("Heading is: " + heading);
		// bot.io.debugln("Robot is facing: " + facingDegree);
		float pointToDegree = heading - facingDegree;
		pointToDegree = (float) normalizeAngle(pointToDegree);

		// bot.io.debugln("Point to: " + pointToDegree);

		arcTo(pointToDegree);
	}

	public void rotateTo(float turnDegree) {

		facingDegree = bot.compass.getDegrees();
		float targetDegree = facingDegree + turnDegree;
		facingDegree = (float) normalizeAngle(facingDegree);
		// bot.io.debugln("First Heading: " + facingDegree);
		targetDegree = (float) normalizeAngle(targetDegree);
		// bot.io.debugln("Target: " + targetDegree);
		if (bot.name.equals("LineBacker")) {
			bot.setPower(80);
		}

		if (turnDegree > 0) {
			bot.turnRightPrecise((double) turnDegree);

		} else {
			bot.turnLeftPrecise((double) Math.abs(turnDegree));
		}
		/*
		 * while (Button.ENTER.isUp()) {
		 * 
		 * facingDegree = bot.compass.getDegrees(); facingDegree = (float)
		 * normalizeAngle(facingDegree); // bot.io.debugln("Heading: " +
		 * facingDegree + " Remaining Angle: " // + normalize(targetDegree -
		 * facingDegree));
		 * 
		 * if ((targetDegree < facingDegree + 3) && (targetDegree > facingDegree
		 * - 3)) { break; } } bot.stopAll();
		 */
	}

	public void arcTo(float turnDegree) {

		facingDegree = bot.compass.getDegrees();
		float targetDegree = facingDegree + turnDegree;
		facingDegree = (float) normalizeAngle(facingDegree);
		// bot.io.debugln("First Heading: " + facingDegree);
		targetDegree = (float) normalizeAngle(targetDegree);
		// bot.io.debugln("Target: " + targetDegree);

		if (turnDegree > 0) {
			bot.moveArcRight();
		} else {
			bot.moveArcLeft();

		}

		while (true) {

			facingDegree = bot.compass.getDegrees();
			facingDegree = (float) normalizeAngle(facingDegree);
			// bot.io.debugln("Heading: " + facingDegree + " Remaining Angle: "
			// + normalize(targetDegree - facingDegree));

			if (targetDegree < facingDegree + 5
					&& targetDegree > facingDegree - 5) {
				break;
			}
		}
		bot.stopAll();
	}

	public void calibrate() {
		bot.compass.startCalibration();

		while (true) {
			if (Button.ENTER.isDown()) {
				bot.compass.stopCalibration();
				return;

			}
		}
	}

	public void strafe() {
		double heading = bot.compass.getDegrees();
		int i = 0;
		boolean flip = false;

		while (Button.ENTER.isUp()) {

			if (bot.USY.getDistance() > 20
					&& bot.compass.getDegrees() + 5 > heading
					&& bot.compass.getDegrees() - 5 < heading) {
				while (bot.USY.getDistance() > 20) {
					bot.nav.moveDir(270);
				}
				bot.floatAll();
			} else if (bot.USY.getDistance() < 15
					&& bot.compass.getDegrees() + 5 > heading
					&& bot.compass.getDegrees() - 5 < heading) {
				while (bot.USY.getDistance() < 15) {
					bot.nav.moveDir(90);
				}
				bot.floatAll();
			}

			if (i > 5) {
				bot.nav.pointToHeading((float) heading);
				i = 0;
			} else {
				i++;
			}

			if ((normalizeMeasurement(bot.USX.getDistance()) < 112) && flip) {
				// moves left, unless at edge of goal
				bot.io.debugln("Left");
				bot.nav.moveDir(180);
			} else if ((normalizeMeasurement(bot.USX.getDistance()) > 66)
					&& !flip) {
				// moves right, unless at edge of goal
				bot.io.debugln("Right");
				bot.nav.moveDir(0);
			} else {
				flip = !flip;
			}

		}
		bot.stopAll();
		bot.changeState(StateCommand.getInstance());
	}

	public double pointToGoal() {
		// bot.io.debugln("went into pointToGoal");
		getLocation();
		bot.sleep(1000);
		double angle = Math.atan2(91 - xPos, yPos);
		angle = (angle * 180) / Math.PI;
		bot.io.debugln("" + angle);
		bot.io.debugln("Angle calculated");
		// rotateTo((float) angle);
		bot.setPower((int)(bot.MAX_MOTOR_POWER / 1.5));
		if (angle > 0) {
			bot.turnRightPrecise(angle);
		} else {
			bot.turnLeftPrecise(Math.abs(angle));
		}
		bot.io.debugln("turned");
		bot.sleep(1000);

		return Math.sqrt(Math.pow(yPos, 2) + Math.pow(91 - xPos, 2));
	}

	public void getLocation() {

		int wall1dist;
		int wall2dist;

		bot.arduino.update();
		wall1dist = bot.arduino.getDisXRight();
		wall2dist = bot.arduino.getDisYBack();
		xPos = 182 - wall1dist;
		yPos = 243 - wall2dist;
	}

	public int getXLocation() {
		return xPos;
	}

	public int getYLocation() {
		return yPos;
	}

	/*
	 * public void updateLocation() { int tachoA = bot.motA.getTachoCount(); int
	 * tachoB = bot.motB.getTachoCount(); int tachoC = bot.motC.getTachoCount();
	 * 
	 * int tachoDiffA = lastTachoA - tachoA; int tachoDiffB = lastTachoB -
	 * tachoB; int tachoDiffC = lastTachoC - tachoC;
	 * 
	 * double moveDistA = tachoDiffA * bot.getR(); double moveDistB = tachoDiffB
	 * * bot.getR(); double moveDistC = tachoDiffC * bot.getR();
	 * 
	 * }
	 * 
	 * public void resetLocation() { lastTachoA = 0; lastTachoB = 0; lastTachoC
	 * = 0; bot.motA.resetTachoCount(); bot.motB.resetTachoCount();
	 * bot.motC.resetTachoCount(); }
	 */

	protected int normalizeMeasurement(int measure) {
		if (measure < 59) {
			measure = measure + 60;
		}
		return measure;
	}

	protected double normalizeAngle(double angle) {
		while (angle >= 360) {
			angle -= 360;
		}

		while (angle <= -360) {
			angle += 360;
		}

		if (angle >= 180) {
			angle = angle - 360;

		}
		if (angle <= -180) {
			angle = angle + 360;

		}

		return angle;
	}

	public void run() {

		moveDir(240);
	}

}