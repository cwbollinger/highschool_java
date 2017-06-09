
public class Centalyzer {
	
	private final double OLDCOINWEIGHT = 3.1;
	private final double NEWCOINWEIGHT = 2.5;
	
	private int oldCoins;
	private int newCoins;

	public Centalyzer(double weight) {
		calculateCoins(weight);
	}
	
	private void calculateCoins(double weight) { // weight is given in grams
		weight = Math.round(weight); // compensates for worn coins.
		for(int i = 0; i < 1000; i++) {
			if(weight == (OLDCOINWEIGHT*i+NEWCOINWEIGHT*(1000-i)+7.0)) { // bag weighs 7.0 grams
				setOldCoins(i);
				setNewCoins(1000-i);
			}
		}
	}
	
	public int getOldCoins() {
		return oldCoins;
	}

	private void setOldCoins(int oldCoins) {
		this.oldCoins = oldCoins;
	}

	public int getNewCoins() {
		return newCoins;
	}

	private void setNewCoins(int newCoins) {
		this.newCoins = newCoins;
	}
}
