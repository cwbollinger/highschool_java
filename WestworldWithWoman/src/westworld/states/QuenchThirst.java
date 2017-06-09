package westworld.states;

import westworld.EntityName;
import westworld.Miner;
import westworld.Location;

//------------------------------------------------------------------------
//
//------------------------------------------------------------------------
public class QuenchThirst extends State {
	static private QuenchThirst m_instance = new QuenchThirst();

	private QuenchThirst() {
	}

	// this is a singleton
	public static QuenchThirst getInstance() {
		return m_instance;
	}

	public void enter(Miner miner) {
		if (miner.getLocation() != Location.Saloon) {    
		    miner.ChangeLocation(Location.Saloon);
		    System.out.println(EntityName.getNameOfEntity(miner.ID())+": "
		    		+"Boy, ah sure is thusty! Walking to the saloon");
		  }
	}

	public void execute(Miner miner) {
		if (miner.Thirsty()) {
		     miner.BuyAndDrinkAWhiskey();
		     System.out.println(EntityName.getNameOfEntity(miner.ID())+": "
		    		 +"That's mighty fine sippin liquer");

		     miner.ChangeState(EnterMineAndDigForNugget.getInstance());
		  }

		  else 
		  {
		    System.out.println("\nERROR!\nERROR!\nERROR!");
		  } 
	}

	public void exit(Miner miner) {
		System.out.println(EntityName.getNameOfEntity(miner.ID())+": "
				+"Leaving the saloon, feelin' good");
	}
}
