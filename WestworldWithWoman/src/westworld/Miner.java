package westworld;

import westworld.states.GoHomeAndSleepTilRested;
import westworld.states.State;

//------------------------------------------------------------------------
//  Name:   Miner.java
//
//  Desc:   A class defining a goldminer.
//
//  Author: Mat Buckland 2002 (fup@ai-junkie.com)
//------------------------------------------------------------------------
public class Miner extends BaseGameEntity {
	// the amount of gold a miner must have before he feels comfortable
	public final int ComfortLevel = 5;
	// the amount of nuggets a miner can carry
	public final int MaxNuggets = 3;
	// above this value a miner is thirsty
	public final int ThirstLevel = 5;
	// above this value a miner is sleepy
	public final int TirednessThreshold = 5;

	State current_state;

	Location m_Location;

	// how many nuggets the miner has in his pockets
	int m_iGoldCarried;

	int m_iMoneyInBank;

	// the higher the value, the thirstier the miner
	int m_iThirst;

	// the higher the value, the more tired the miner
	int m_iFatigue;

	Miner(EntityName name) {
		super(name.ordinal());
		m_Location = Location.Shack;
		m_iGoldCarried = 0;
		m_iMoneyInBank = 0;
		m_iThirst = 0;
		m_iFatigue = 0;
		current_state = GoHomeAndSleepTilRested.getInstance();
	}

	// this must be implemented
	public void Update() {
		m_iThirst += 1;

		if (current_state != null) {
			current_state.execute(this);
		}
	}

	// this method changes the current state to the new state. It first
	// calls the Exit() method of the current state, then assigns the
	// new state to m_pCurrentState and finally calls the Entry()
	// method of the new state.
	public void ChangeState(State new_state) {
		// make sure both states are both valid before attempting to
		// call their methods
		if (current_state == null || new_state == null) {
			System.err.println("STATE DOES NOT EXIST");
		}

		// call the exit method of the existing state
		current_state.exit(this);

		// change state to the new state
		current_state = new_state;

		// call the entry method of the new state
		current_state.enter(this);
	}

	public Location getLocation() {
		return m_Location;
	}

	public void ChangeLocation(final Location loc) {
		m_Location = loc;
	}

	public int GoldCarried() {
		return m_iGoldCarried;
	}

	public void SetGoldCarried(int val) {
		m_iGoldCarried = val;
	}

	public void AddToGoldCarried(int val) {
		m_iGoldCarried += val;

		if (m_iGoldCarried < 0) {
			m_iGoldCarried = 0;
		}
	}

	public boolean PocketsFull() {
		return m_iGoldCarried >= MaxNuggets;
	}

	public boolean Fatigued() {
		if (m_iFatigue > TirednessThreshold) {
			return true;
		}
		return false;
	}

	public void DecreaseFatigue() {
		m_iFatigue -= 1;
	}

	public void IncreaseFatigue() {
		m_iFatigue += 1;
	}

	public int Wealth() {
		return m_iMoneyInBank;
	}

	public void SetWealth(int val) {
		m_iMoneyInBank = val;
	}

	public void AddToWealth(int val) {
		m_iMoneyInBank += val;

		if (m_iMoneyInBank < 0) {
			m_iMoneyInBank = 0;
		}
	}

	public boolean Thirsty() {
		if (m_iThirst >= ThirstLevel) {
			return true;
		}
		return false;
	}

	public void BuyAndDrinkAWhiskey() {
		m_iThirst = 0;
		m_iMoneyInBank -= 2;
	}

}