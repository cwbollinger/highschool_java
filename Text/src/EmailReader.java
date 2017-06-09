import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EmailReader {

	public static void main(String[] args) {
		// Read the contents of guitar-email.html
		// Put the results, as a String, into the content variable
		String content = "";
		try {
			File file = new File("guitar-email.html");
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null)
				sb.append(line);
			content = sb.toString();
		} catch (IOException e) {
			System.err.println("There's been an error: " + e.getMessage());
			System.exit(-1);
		}
		int start = content.indexOf("\">");
		int end = content.indexOf("</a>");
		String topGuitar = content.substring((start+2), end);

		start = content.indexOf("<strong>");
		end = content.indexOf("</strong>");
		String topGuitarPrice = content.substring(start,end);
		System.out.println("The best priced guitar this week sells for: " + topGuitarPrice);
		System.out.println("It's a " + topGuitar);
	}
}