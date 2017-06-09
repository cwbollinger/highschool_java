
public class itereation {

	public static void main(String[]args) {
		count(100);
	}
	
	public static void count(int n) {
		
		while (true) {
			System.out.println(n);
			n--;
			if (n<0){
				break;
			}
		}
	}
	
}
