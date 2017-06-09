package westworld.states;

import westworld.EntityName;
import westworld.Miner;
import westworld.Location;

//------------------------------------------------------------------------
//  In this state the miner will walk to a goldmine and pick up a nugget
//  of gold. If the miner already has a nugget of gold he'll change state
//  to VisitBankAndDepositGold. If he gets thirsty he'll change state
//  to QuenchThirst
//------------------------------------------------------------------------
class EnterMineAndDigForNugget extends State {
	private static EnterMineAndDigForNugget m_instance = new EnterMineAndDigForNugget();

	EnterMineAndDigForNugget() {
	}

	// this is a singleton
	static EnterMineAndDigForNugget getInstance() {
		return m_instance;
	}

	public void enter(Miner miner) {
		// if the miner is not already located at the goldmine, he must
		// change location to the gold mine
		if (miner.getLocation() != Location.Goldmine) {
			System.out.println(EntityName.getNameOfEntity(miner.ID())
					+ ": " + "Walkin' to the goldmine");

			miner.ChangeLocation(Location.Goldmine);
		}
	}

	public void execute(Miner miner) {
		  //the miner digs for gold until he is carrying in excess of MaxNuggets. 
		  //If he gets thirsty during his digging he packs up work for a while and 
		  //changes state to go to the saloon for a whiskey.
		  miner.AddToGoldCarried(1);

		  miner.IncreaseFatigue();

		  System.out.println(EntityName.getNameOfEntity(miner.ID())+": "
				  +"Pickin' up a nugget");

		  //if enough gold mined, go and put it in the bank
		  if (miner.PocketsFull())
		  {
		    miner.ChangeState(VisitBankAndDepositGold.getInstance());
		  }

		  if (miner.Thirsty())
		  {
		    miner.ChangeState(QuenchThirst.getInstance());
		  }
	}

	public void exit(Miner miner) {
		System.out.println(EntityName.getNameOfEntity(miner.ID())+ ": " 
				+"Ah'm leavin' the goldmine with mah pockets full o' sweet gold");
	}

}