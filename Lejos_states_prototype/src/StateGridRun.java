//------------------------------------------------------------------------
//  robot Grid Run state - search Room 3
//------------------------------------------------------------------------
import lejos.nxt.*;

public class StateGridRun extends State {

	static private StateGridRun instance = new StateGridRun();

	boolean canFound = false;

	int left;
	int right;
	int up;
	int down;

	private StateGridRun() {
	}

	// this is a singleton
	public static StateGridRun getInstance() {
		return instance;
	}

	private boolean goForward(Robot robot) {
		if (robot.ultrasonic.getDistance() > 35) {
			robot.map.grid[robot.x][robot.y] = -1;
			robot.forward(30);
			return true;
		} else {
			Sound.buzz();
			robot.stop();
			return false;
		}
	}

	private void faceLeft(Robot robot) {
		int heading = robot.dir;
		debug("faceLeft, " + heading);
		int diff = 0;
		if (heading == 180) {
			return;
		} else {
			diff = heading - 180;
			if (diff < 0) {
				robot.left(Math.abs(diff));
			} else {
				robot.right(Math.abs(diff));
			}
		}
		robot.dir = 180;
	}

	private void faceRight(Robot robot) {
		int heading = robot.dir;
		debug("faceRight, " + heading);
		int diff = 0;
		if (heading == 0) {
			return;
		} else {
			diff = heading;
			if (diff > 180) {
				robot.left(Math.abs(diff));
			} else {
				robot.right(Math.abs(diff));
			}
		}
		robot.dir = 0;
	}

	private void faceUp(Robot robot) {

		int heading = robot.dir;
		debug("faceUp, " + heading);
		int diff = 0;
		if (heading == 90) {
			return;
		} else {
			diff = heading - 90;
			if (diff < 0 || diff > 180) {
				robot.left(Math.abs(diff));
			} else {
				robot.right(Math.abs(diff));
			}
		}
		robot.dir = 90;
	}

	private void faceDown(Robot robot) {
		int heading = robot.dir;
		debug("faceDown, " + heading);
		int diff = 0;
		if (heading == 270) {
			return;
		} else {
			diff = heading - 270;
			if (diff > 0 || diff < -180) {
				robot.left(Math.abs(diff));
			} else {
				robot.right(Math.abs(diff));
			}
		}
		robot.dir = 270;
	}
	
	public void printMap(Robot robot) {	
		for(int j = robot.map.ROWS-1; j >= 0; j--) {
			for(int i = 0; i < robot.map.COLS; i++) {
				debug(robot.map.grid[i][j]+" ");
			}
			debug("\n");
		}
	}

	public void enter(Robot robot) {
		debug("StGridRun enter\n");
	}

	public void execute(Robot robot) {
		debug("StGridRun execute\n");
		robot.x = 1;
		robot.y = 1;
		robot.dir = 90;
		//*
		while (!canFound) {
			left = robot.map.grid[robot.x - 1][robot.y];
			right = robot.map.grid[robot.x + 1][robot.y];
			up = robot.map.grid[robot.x][robot.y + 1];
			down = robot.map.grid[robot.x][robot.y - 1];

			if (up == 0) {
				faceUp(robot);

				goForward(robot);
				robot.y++;

				robot.sleep(1000);

			} else if (right == 0) {
				faceRight(robot);

				goForward(robot);
				robot.x++;

				robot.sleep(1000);
			} else if (down == 0) {
				faceDown(robot);

				goForward(robot);
				robot.y--;

				robot.sleep(1000);
			}else if (left == 0) {

				faceLeft(robot);

				goForward(robot);
				robot.x--;

				robot.sleep(1000);
			} else {
				Sound.playTone(440, 100);
				Sound.playTone(880, 100);
				Sound.playTone(440, 100);
				Sound.playTone(220, 100);
				break;
			}

			debug(robot.x + "	" + robot.y+"\n");
		}
		/*
		faceDown(robot);
		robot.sleep(100);
		faceLeft(robot);
		robot.sleep(100);
		faceRight(robot);
		robot.sleep(100);
		faceUp(robot);
		robot.sleep(100);
		*/
		
		robot.changeState(StateExit.getInstance());
	}

	public void exit(Robot robot) {
		debug("StGridRun exit\n");
	}
}
