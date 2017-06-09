//------------------------------------------------------------------------
//
//------------------------------------------------------------------------
public class StateDrunk extends State {
	static private StateDrunk m_instance = new StateDrunk();

	private StateDrunk() {
	}

	// this is a singleton
	public static StateDrunk getInstance() {
		return m_instance;
	}

	public void enter(Miner miner) {
		if (miner.getLocation() != Location.Saloon) {    
		    System.out.println("\nERROR!\nERROR!\nERROR!");
		  }
	}

	public void execute(Miner miner) {
		if (miner.isDrunk()) {
		     System.out.println(EntityName.getNameOfEntity(miner.getID())+": "
		    		 +"Aww, mah head be hurtin' mightily");

		     miner.changeState(StateGoHomeAndSleepTilRested.getInstance());
		  }

		  else 
		  {
		    System.out.println("\nERROR!\nERROR!\nERROR!");
		  } 
	}

	public void exit(Miner miner) {
		System.out.println(EntityName.getNameOfEntity(miner.getID())+": "
				+"Ah'm goin home til the world stops spinnin");
	}
}
