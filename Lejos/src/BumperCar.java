import lejos.nxt.*;

public class BumperCar {

	public void run() {
		Motor.A.forward();
		Motor.B.forward();
		while (Button.ESCAPE.isUp()) {
			//if ()
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BumperCar bot = new BumperCar();
		bot.run();
	}

}
