package google;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This Class tries to use the Google Dictionary API to get several synonyms for
 * a given term. It reads from an input file, searchs for synonyms and writes
 * them into an output File
 * 
 * @author Marc Gruenewald
 * @version 1.0
 * 
 * 
 */
public class FindSynonyms {
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
	public FindSynonyms(String input, String output) {
		this.input = input;
		this.output = output;
	}

	/**
	 * This Method uses the Input-File to search for synonyms for the given
	 * words. To do so, it uses the Unofficial Google Dictionary API
	 * 
	 * @param term
	 *            An Array of the String values that were read from the
	 *            Input-File.
	 * @return synonyms returns an Array containing the synonyms that have been
	 *         found
	 */
	public String findSynonyms(String term) {

		StringBuilder sb = new StringBuilder();
		try {
			URL serverAddress = new URL(
					"http://www.google.com/dictionary/json?callback=dict_api.callbacks.id100&q="
							+ term + "&sl=en&tl=en&restrict=pr%2Cde&client=te");
			HttpURLConnection connection = (HttpURLConnection) serverAddress
					.openConnection();
			connection.connect();
			int rc = connection.getResponseCode();
			if (rc == 200) {
				String line = null;
				BufferedReader br = new BufferedReader(
						new java.io.InputStreamReader(connection
								.getInputStream()));
				while ((line = br.readLine()) != null)
					sb.append(line + '\n');
				line = sb.toString();
				line = line.replaceAll("dict_api\\.callbacks\\.id100\\(", "");
				line = line.replaceAll("\\,200\\,null\\)", "");
				String[] lineshort;
				lineshort = line.split("\"type\"\\:\"meaning\"\\,\"terms\"\\:");
				line = lineshort[0];
				lineshort = line.split("\"type\"\\:\"text\"\\,\"text\"\\:");
				for (String string : lineshort) {
					string = string.replaceAll("(\")(\\w*)(\")([\\.\\s]*)",
							"$2");
				}
				lineshort = line.split("\\,");
				sb = new StringBuilder();
				for (String string : lineshort) {
					string = string.replaceAll("(\"text\"\\:)(\"\\w*\")", "$2");
					string = string.replaceAll("\"", "");
					if (string.matches("\\w*"))
						sb.append(string + ", ");
				}

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
		return sb.toString();
	}
}
