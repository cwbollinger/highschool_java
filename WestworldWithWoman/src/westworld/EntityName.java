package westworld;

public enum EntityName {
	Ent_Miner_Bob, Ent_Elsa;

	public static String getNameOfEntity(EntityName n) {
		return n.name();
	}
		
	public static String getNameOfEntity(int n) {
		if (n == Ent_Miner_Bob.ordinal()) {
			return "Miner Bob";
		}
		else if (n == Ent_Elsa.ordinal()) {
			return "Elsa";
		}
		else {
			return "UNKNOWN!";
		}
	}

}