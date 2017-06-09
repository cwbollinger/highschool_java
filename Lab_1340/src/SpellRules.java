import java.io.*;



public class SpellRules {

	private String text;
	
	public SpellRules(String fileName) {
		try {
			readFile(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readFile(String fileName) throws IOException {
		FileInputStream f = new FileInputStream("test.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(f));
		text = "";
		String temp = br.readLine();
		
		while(temp != null) {
			text = text + temp;
			temp = br.readLine();
		}
		
	}
	
	public void checkSpelling() {
		
	}
	
	public void checkQU() {
		
	}
	
	public void checkIEC() {
		
	}
}
