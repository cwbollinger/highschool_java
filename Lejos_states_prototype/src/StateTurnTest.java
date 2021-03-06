
public class StateTurnTest extends State{
	static private StateTurnTest instance = new StateTurnTest();

	private StateTurnTest() {
	}

	// this is a singleton
	public static StateTurnTest getInstance() {
		return instance;
	}

	public void enter(Robot robot) {
		debug("StTurnTest enter\n");
	}

	public void execute(Robot robot) {
		debug("StTurnTest execute\n");
		robot.left(90);
		robot.right(90);
		robot.changeState(StateExit.getInstance());
	}

	public void exit(Robot robot) {
		debug("StTurnTest exit\n");
	}
}
