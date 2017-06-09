//------------------------------------------------------------------------
//  miner will go home and sleep until his fatigue is decreased
//  sufficiently
//------------------------------------------------------------------------
public class StateGoHomeAndSleepTilRested extends State {
	static private StateGoHomeAndSleepTilRested instance = new StateGoHomeAndSleepTilRested();

	private StateGoHomeAndSleepTilRested() {
	}

	// this is a singleton
	public static StateGoHomeAndSleepTilRested getInstance() {
		return instance;
	}

	public void enter(Miner miner) {
		if (miner.getLocation() != Location.Shack) {
			System.out.println(EntityName.getNameOfEntity(miner.getID())
					+ ": " + "Walkin' home");
			miner.changeLocation(Location.Shack);
		}
	}

	public void execute(Miner miner) {
		//if miner is not fatigued start to dig for nuggets again.
		  if (!miner.fatigued()) {
		    System.out.println(EntityName.getNameOfEntity(miner.getID())+": " 
		          +"What a fantastic nap! Time to find more gold");

		     miner.changeState(StateEnterMineAndDigForNugget.getInstance());
		  } else {
			  miner.decreaseFatigue();
			  System.out.println(EntityName.getNameOfEntity(miner.getID())+": " 
			          +"ZZZZZ.......");
		  }
	}

	public void exit(Miner miner) {
		  System.out.println(EntityName.getNameOfEntity(miner.getID())+": "
				  +"Leaving the house");
	}
}
