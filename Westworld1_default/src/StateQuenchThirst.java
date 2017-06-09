//------------------------------------------------------------------------
//
//------------------------------------------------------------------------
public class StateQuenchThirst extends State {
	static private StateQuenchThirst m_instance = new StateQuenchThirst();

	private StateQuenchThirst() {
	}

	// this is a singleton
	public static StateQuenchThirst getInstance() {
		return m_instance;
	}

	public void enter(Miner miner) {
		if (miner.getLocation() != Location.Saloon) {    
		    miner.changeLocation(Location.Saloon);
		    System.out.println(EntityName.getNameOfEntity(miner.getID())+": "
		    		+"Boy, ah sure is thusty! Walking to the saloon");
		  }
	}

	public void execute(Miner miner) {
		if (miner.thirsty()) {
		     miner.buyAndDrinkAWhiskey();
		     System.out.println(EntityName.getNameOfEntity(miner.getID())+": "
		    		 +"That's mighty fine sippin liquer");
		     
		     if(miner.isDrunk()) {
		    	 miner.changeState(StateDrunk.getInstance());
		     } else {
		    	 miner.changeState(StateEnterMineAndDigForNugget.getInstance());
		     }
		  }

		  else 
		  {
		    System.out.println("\nERROR!\nERROR!\nERROR!");
		  } 
	}

	public void exit(Miner miner) {
		System.out.println(EntityName.getNameOfEntity(miner.getID())+": "
				+"Leaving the saloon, feelin' good");
	}
}
