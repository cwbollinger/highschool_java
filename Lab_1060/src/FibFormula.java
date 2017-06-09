
public class FibFormula implements FindFib {

	@Override
	public int findFibTerm(int termNum) {
		int term = (int) ((Math.pow((1+Math.sqrt(5))/2, termNum) - (Math.pow((1-Math.sqrt(5))/2, termNum))) / Math.sqrt(5));
		
		return term;
		
	}

}
