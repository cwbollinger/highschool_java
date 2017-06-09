
public class FibRecursion implements FindFib {

	@Override
	public int findFibTerm(int termNum) {
		if((termNum == 1) || (termNum == 2)) {
			return 1;
		} else {
			return findFibTerm(termNum-2)+findFibTerm(termNum-1);
		}
	}

}
