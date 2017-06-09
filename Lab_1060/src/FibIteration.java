
public class FibIteration implements FindFib {

	@Override
	public int findFibTerm(int termNum) {
		int term = 1;
		int prevTerm = 1;
		int temp;
		for(int i = 2; i < termNum; i++) {
			temp = term;
			term += prevTerm;
			prevTerm = temp;
		}
		return term;
	}

}
