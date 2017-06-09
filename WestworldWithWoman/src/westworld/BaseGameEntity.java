package westworld;

//------------------------------------------------------------------------
//
//  Name:   BaseGameEntity.h
//
//  Desc:   Base class for a game object
//
//  Author: Mat Buckland 2002 (fup@ai-junkie.com)
//
//------------------------------------------------------------------------
public abstract class BaseGameEntity {

	// every entity must have a unique identifying number
	int m_ID;

	// this is the next valid ID. Each time a BaseGameEntity is instantiated
	// this value is updated
	static int m_iNextValidID = 0;

	/**
	 * this must be called within the constructor to make sure the ID is set
	 * correctly. It verifies that the value passed to the method is greater or
	 * equal to the next valid ID, before setting the ID and incrementing the
	 * next valid ID
	 */
	void SetID(int val) {
		// make sure the val is equal to or greater than the next available ID
		if ((val < m_iNextValidID)) {
			System.out.println("<BaseGameEntity.SetID>: invalid ID");
		}
		m_ID = val;
		m_iNextValidID = m_ID + 1;
	}

	BaseGameEntity(int id) {
		SetID(id);
	}

	// all entities must implement an update function
	public abstract void Update();

	public int ID() {
		return m_ID;
	}
};