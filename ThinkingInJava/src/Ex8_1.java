
public class Ex8_1 {

	public static String revString(String str) {
		String temp = "";
		for(int i = str.length()-1; i >= 0; i--) {
			temp += str.charAt(i);
		}
		return temp;
	}
	
	public static void main(String args[]) {
		System.out.println(Ex8_1.revString("!ereht olleh"));
	}
}
