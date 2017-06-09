import java.util.*;


public class ArrayListSlotMachine implements SlotMachine {

	final static ArrayList<String> reel1 = new ArrayList<String>(Arrays.asList("Blank", "Bars", "Blank", "Blank", "Bars", "Blank", 
			"Cherries", "Seven", "Bars", "Blank", "Blank", "Bars", "Seven", "Cherries", "Seven", "Blank", "Seven", "Bars", "Seven",
			"Blank"));
	
	final static ArrayList<String> reel2 = new ArrayList<String>(Arrays.asList("Seven", "Cherries", "Bars", "Bars", "Blank", "Bars",
			"Blank", "Blank", "Seven", "Blank", "Blank", "Cherries", "Blank", "Blank", "Blank", "Bars", "Blank", "Blank", "Blank",
			"Cherries"));
	
	final static ArrayList<String> reel3 = new ArrayList<String>(Arrays.asList("Bars", "Blank", "Blank", "Blank", "Cherries", "Blank",
			"Seven", "Bars", "Blank", "Bars", "Blank", "Blank", "Bars", "Blank", "Cherries", "Seven", "Blank", "Seven", "Blank",
			"Bars"));
	
	final static ArrayList<ArrayList<String>> reels = new ArrayList<ArrayList<String>>(Arrays.asList(reel1, reel2, reel3));
	
	@Override
	public int spin() {
		
		String result[] = new String[3];
		
		for(int i = 0; i < 3; i++) {
			int spin = (int) (Math.random()*32.0);
			if( spin < 20) {
				result[i] = reels.get(i).get(spin);
			} else {
				result[i] = "Blank";
			}
		}
		
		System.out.println(result[0] + " | " + result[1] + " | " + result[2]);
		
		if(result[0].equals("Seven") && result[1].equals("Seven") && result[2].equals("Seven")) {
			return 250;
		} else if(result[0].equals("Bars") && result[1].equals("Bars") && result[2].equals("Bars")) {
			return 75;
		} else if(result[0].equals("Cherries") && result[1].equals("Cherries") && result[2].equals("Cherries")) {
			return 12;
		} else if((result[0].equals("Cherries") && result[1].equals("Cherries")) ||
				(result[1].equals("Cherries") && result[2].equals("Cherries")) ||
				(result[0].equals("Cherries") && result[2].equals("Cherries"))) {
			return 8;
		} else if((result[0].equals("Cherries")) || (result[1].equals("Cherries")) || (result[2].equals("Cherries"))) {
			return 2;
		} else {
			return 0;
		}
	}
}
