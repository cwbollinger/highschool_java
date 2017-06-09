import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;

public class RConsoleDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RConsole.openUSB(20000);
		RConsole.println("Connected!");
		
		while (Button.readButtons() != Button.ID_ESCAPE)
			;
	}

}
