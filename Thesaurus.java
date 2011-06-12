package google;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * This Class tries to use Thesaurus Dictionary from the WEB to get several
 * synonyms for a given term. It reads from an input file, searchs for synonyms
 * and writes them into an output File
 * 
 * @author Marc Gruenewald
 * @version 1.0
 * 
 * 
 */

public class Thesaurus {
	String input;
	String output;

	/**
	 * The Constructor sets both variables using the given parameters.
	 * 
	 * @param input
	 *            Path to the InputFile.
	 * @param output
	 *            Path to the OutputFile.
	 */
	public Thesaurus(String input, String output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * This Method uses the Input-File to search for synonyms for the given
	 * words. To do so, it uses a Thesaurus Dictionary
	 * 
	 * @param word
	 *            A String values that was read from the Input-File.
	 * @return synonyms returns an Array containing the synonyms that have been
	 *         found
	 */
	public String sendRequest(String word) {
		String line = "";
		try {
			//get the json-String
			URL serverAddress = new URL(
					"http://thesaurus.altervista.org/thesaurus/v1?word="
							+ URLEncoder.encode(word, "UTF-8")
							+ "&language=en_US&key=WgsfwXWUsepnNTAE34Xd&output=json");
			HttpURLConnection connection = (HttpURLConnection) serverAddress
					.openConnection();
			connection.connect();
			int rc = connection.getResponseCode();
			if (rc == 200) {
				line = null;
				BufferedReader br = new BufferedReader(
						new java.io.InputStreamReader(connection
								.getInputStream()));
				StringBuilder sb = new StringBuilder();
				while ((line = br.readLine()) != null)
					sb.append(line + '\n');
				
				//filter everything that's not needed for the output. 
				line = sb.toString();
				line = line.replaceAll("\\{\"response\"\\:\\[", "");
				sb = new StringBuilder();
				String[] line2 = line.split("\\}\\,");
				for (int i = 0; i < line2.length; i++) {
					line2[i] = line2[i]
							.replaceAll(
									"\\{\"list\"\\:\\{\"category\"\\:\"\\(verb\\)\"\\,\"synonyms\"\\:",
									"");
					sb.append(line2[i]);
				}

				line = sb.toString();
				line = line.replaceAll("\"", "");
				line = line.replaceAll("\\|", ", ");
				line = line.replaceAll("\\}", " ");
				line = line.replaceAll("\\]", "");
				line = line.replaceAll(
						"(\\{list\\:\\{category\\:\\(noun\\)\\,synonyms\\:)",
						"");
				line = line.replaceAll("(\\(antonym\\))", "");

			} else
				System.out.println("HTTP error:" + rc);
			connection.disconnect();
		} catch (java.net.MalformedURLException e) {
			e.printStackTrace();
		} catch (java.net.ProtocolException e) {
			e.printStackTrace();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return line;
	}

}