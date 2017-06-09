
public class TestSlotMachine {
	
	public static void main(String[] args) {
		ArrayListSlotMachine test = new ArrayListSlotMachine();
		double payIn;
		double payOut = 0;
		
		for(payIn = 0; payIn < 100000; payIn++) {
			payOut += test.spin();
			System.out.println("Total Payout: " + payOut);
		}
		
		double percentReturn = payOut/payIn;
		
		System.out.println("Percent Return: " + percentReturn);
		
	}
}
