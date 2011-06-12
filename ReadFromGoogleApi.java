package google;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This Class just get's the hole thing started.
 * 
 * @author Marc Gruenewald
 * @version 1.0
 * 
 * 
 */
public class ReadFromGoogleApi {
	static String input = "";
	static String output = "";
	static int chooser;

	/**
	 * Main-Method to start everything.
	 * 
	 * @param args
	 *            First param is Path to the Input- second param to the
	 *            Output-File. The third param decides whether you want to use
	 *            the Google-API, the Thesaurus-Dic, or both
	 * 
	 * 
	 */
	public static void main(String[] args) {

		FindSynonyms findThemGoogle;
		Thesaurus findThemThesaurus;
		input = args[0];
		output = args[1];
		chooser = new Integer(args[2]).intValue();

		FileReader fileReader;
		try {
			String word = "";
			fileReader = new FileReader(args[0]);
			BufferedReader reader = new BufferedReader(fileReader);
			StringBuilder outputSynonyms = new StringBuilder();
			while (word != null) {
				word = reader.readLine();
				if (word != null) {
					switch (chooser) {
					case 1:
						outputSynonyms.append(word + ", ");
						findThemGoogle = new FindSynonyms(args[0], args[1]);
						outputSynonyms
								.append(findThemGoogle.findSynonyms(word) + '\n');
						break;
					case 2:
						outputSynonyms.append(word + ", ");
						findThemThesaurus = new Thesaurus(args[0], args[1]);
						outputSynonyms.append(findThemThesaurus
								.sendRequest(word));
						break;
					case 3:
						outputSynonyms.append(word + ", ");
						findThemGoogle = new FindSynonyms(args[0], args[1]);
						outputSynonyms.append(findThemGoogle.findSynonyms(word)
								+ " ");
						findThemThesaurus = new Thesaurus(args[0], args[1]);
						outputSynonyms.append(findThemThesaurus
								.sendRequest(word) + '\n');
					default:
						break;
					}
					outputSynonyms
							.append("------------------------------------------\n");
				}
			}
			writeOutput(outputSynonyms);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This Method puts everything into an Output-File
	 * 
	 * @param outputSynonyms
	 */
	public static void writeOutput(StringBuilder outputSynonyms) {
		try {
			FileWriter writeIt = new FileWriter(output);
			writeIt.append(outputSynonyms.toString());
			writeIt.flush();
			writeIt.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} // end of Thesaurus
