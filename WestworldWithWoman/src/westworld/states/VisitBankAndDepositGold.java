package westworld.states;

import westworld.EntityName;
import westworld.Miner;
import westworld.Location;

//------------------------------------------------------------------------
//  Entity will go to a bank and deposit any nuggets he is carrying. If the 
//  miner is subsequently wealthy enough he'll walk home, otherwise he'll
//  keep going to get more gold
//------------------------------------------------------------------------
class VisitBankAndDepositGold extends State {
	static private VisitBankAndDepositGold m_instance = new VisitBankAndDepositGold();

	private VisitBankAndDepositGold() {
	}

	// this is a singleton
	public static VisitBankAndDepositGold getInstance() {
		return m_instance;
	}

	public void enter(Miner miner) {
		//on entry the miner makes sure he is located at the bank
		  if (miner.getLocation() != Location.Bank) {
		    System.out.println(EntityName.getNameOfEntity(miner.ID())+": "
		    		+"Goin' to the bank. Yes siree");

		    miner.ChangeLocation(Location.Bank);
		  }
	}

	public void execute(Miner miner) {
		//deposit the gold
		  miner.AddToWealth(miner.GoldCarried());
		    
		  miner.SetGoldCarried(0);

		  System.out.println(EntityName.getNameOfEntity(miner.ID())+": " 
		       +"Depositing gold. Total savings now: "+ miner.Wealth());

		  //wealthy enough to have a well earned rest?
		  if (miner.Wealth() >= miner.ComfortLevel) {
		    System.out.println(EntityName.getNameOfEntity(miner.ID())+": " 
		         +"WooHoo! Rich enough for now. Back home to mah li'lle lady");
		      
		    miner.ChangeState(GoHomeAndSleepTilRested.getInstance());      
		  }

		  //otherwise get more gold
		  else 
		  {
		    miner.ChangeState(EnterMineAndDigForNugget.getInstance());
		  }
	}

	public void exit(Miner miner) {
		  System.out.println(EntityName.getNameOfEntity(miner.ID())+": "
				  +"Leavin' the bank");
	}
}
