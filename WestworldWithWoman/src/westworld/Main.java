package westworld;

public class Main {

	public static void main(String[] args) {

		// create a miner
		Miner miner = new Miner(EntityName.Ent_Miner_Bob);

		// simply run the miner through a few Update calls
		for (int i = 0; i < 20; ++i) {
			miner.Update();

			try {
				Thread.sleep(800);
			} catch (Exception e) {
				System.err.println("Sleep interrupted");
			}
		}

		// wait for a keypress before exiting
		//try { System.in.read(); }
		//catch (Exception e) {	
		//}
	}
}
