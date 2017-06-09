/**
 * abstract base class to define an interface for a state
 * 
 * @author Mat Buckland 2002 (fup@ai-junkie.com)
 *
 */
public abstract class State {

	/**
	 * this will execute when the state is entered
	 * 
	 * @param m
	 */
	public abstract void enter(Miner m);

	/**
	 * this is the state's normal update function
	 * 
	 * @param m
	 */
	public abstract void execute(Miner m);

	/**
	 * this will execute when the state is exited. (My word, isn't life full of
	 * surprises... ;o))
	 * 
	 * @param m
	 */
	public abstract void exit(Miner m);

}
