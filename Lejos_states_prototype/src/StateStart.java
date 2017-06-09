import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.Bluetooth;

//------------------------------------------------------------------------
//  robot starting state - decides what to do when robot is turned on
//------------------------------------------------------------------------

public class StateStart extends State {

	static private StateStart instance = new StateStart();

	private StateStart() {
	}

	// this is a singleton
	public static StateStart getInstance() {
		return instance;
	}

	public void enter(Robot robot) {
	}

	public void execute(Robot robot) {
		
		robot.changeState(StateCommand.getInstance());
		
		//robot.changeState(StateGridRun.getInstance());
	}

	public void exit(Robot robot) {
	}
}
