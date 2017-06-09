package westworld.states;

import westworld.EntityName;
import westworld.Miner;
import westworld.Location;

//------------------------------------------------------------------------
//  miner will go home and sleep until his fatigue is decreased
//  sufficiently
//------------------------------------------------------------------------
public class GoHomeAndSleepTilRested extends State {
	static private GoHomeAndSleepTilRested instance = new GoHomeAndSleepTilRested();

	private GoHomeAndSleepTilRested() {
	}

	// this is a singleton
	public static GoHomeAndSleepTilRested getInstance() {
		return instance;
	}

	public void enter(Miner miner) {
		if (miner.getLocation() != Location.Shack) {
			System.out.println(EntityName.getNameOfEntity(miner.ID())
					+ ": " + "Walkin' home");
			miner.ChangeLocation(Location.Shack);
		}
	}

	public void execute(Miner miner) {
		//if miner is not fatigued start to dig for nuggets again.
		  if (!miner.Fatigued()) {
		    System.out.println(EntityName.getNameOfEntity(miner.ID())+": " 
		          +"What a fantastic nap! Time to find more gold");

		     miner.ChangeState(EnterMineAndDigForNugget.getInstance());
		  }
	}

	public void exit(Miner miner) {
		  System.out.println(EntityName.getNameOfEntity(miner.ID())+": "
				  +"Leaving the house");
	}
}
