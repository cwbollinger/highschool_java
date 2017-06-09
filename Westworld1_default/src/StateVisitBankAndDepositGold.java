//------------------------------------------------------------------------
//  Entity will go to a bank and deposit any nuggets he is carrying. If the 
//  miner is subsequently wealthy enough he'll walk home, otherwise he'll
//  keep going to get more gold
//------------------------------------------------------------------------
class StateVisitBankAndDepositGold extends State {
	
	static private StateVisitBankAndDepositGold m_instance = new StateVisitBankAndDepositGold();

	private StateVisitBankAndDepositGold() {
	}

	// this is a singleton
	public static StateVisitBankAndDepositGold getInstance() {
		return m_instance;
	}

	public void enter(Miner miner) {
		//on entry the miner makes sure he is located at the bank
		  if (miner.getLocation() != Location.Bank) {
		    System.out.println(EntityName.getNameOfEntity(miner.getID())+": "
		    		+"Goin' to the bank. Yes siree");

		    miner.changeLocation(Location.Bank);
		  }
	}

	public void execute(Miner miner) {
		//deposit the gold
		  miner.addToWealth(miner.getGoldCarried());
		    
		  miner.setGoldCarried(0);

		  System.out.println(EntityName.getNameOfEntity(miner.getID())+": " 
		       +"Depositing gold. Total savings now: "+ miner.getWealth());

		  //wealthy enough to have a well earned rest?
		  if (miner.getWealth() >= miner.ComfortLevel) {
		    System.out.println(EntityName.getNameOfEntity(miner.getID())+": " 
		         +"WooHoo! Rich enough for now. Back home to mah li'lle lady");
		      
		    miner.changeState(StateGoHomeAndSleepTilRested.getInstance());      
		  }

		  //otherwise get more gold
		  else 
		  {
		    miner.changeState(StateEnterMineAndDigForNugget.getInstance());
		  }
	}

	public void exit(Miner miner) {
		  System.out.println(EntityName.getNameOfEntity(miner.getID())+": "
				  +"Leavin' the bank");
	}
}
