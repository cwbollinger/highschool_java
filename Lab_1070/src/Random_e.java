
public class Random_e {

	public double calculateE() {
		double sum;
		double count;
		double eApprox=0;
		
		int i;
		for(i = 0; i < 1000000; i++) {
			sum = 0;
			count = 0;
			while(sum < 1) {
				sum += Math.random();
				count++;
			}
			eApprox += count;
		}
		eApprox /= i;
		return eApprox;
	}
}
