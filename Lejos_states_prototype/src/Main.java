import lejos.nxt.*;
import lejos.nxt.comm.RConsole;

public class Main {

	public static void main(String[] args) {

		// create a robot
		Robot robot = Robot.getRobot();

		// See StateStart() for Bluetooth handling
		//RConsole.openBluetooth(120000); RConsole.println("Connected!"); Sound.playTone(440, 200);

		while (Button.ESCAPE.isUp() && !robot.exit) {
			robot.update();
		}

	}
}
