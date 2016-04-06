package pcdLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Reader {
	
	public static PCDFile readFile(String filepath){
		PCDFile file = new PCDFile();

		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				String[] words = sCurrentLine.split(" ");
				String firstWord = words[0].toUpperCase();
				switch (firstWord) {
				case "#":
					break;
				case "VERSION":
					file.version = Double.parseDouble(words[1]);
					break;
				case "FIELDS":
					file.fields = Arrays.copyOfRange(words, 1, words.length);
					break;
				case "SIZE":
					file.size = StringArraytoIntArray(Arrays.copyOfRange(words, 1, words.length));
					break;
				case "TYPE":
					file.type = StringArraytoCharArray(Arrays.copyOfRange(words, 1, words.length));
					break;
				case "COUNT":
					file.count = StringArraytoIntArray(Arrays.copyOfRange(words, 1, words.length));
					break;
				case "WIDTH":
					file.width = Integer.parseInt(words[1]);
					break;
				case "HEIGHT":
					file.height = Integer.parseInt(words[1]);
					break;
				case "VIEWPOINT":
					file.viewpoint = StringArraytoIntArray(Arrays.copyOfRange(words, 1, words.length));
					break;
				case "POINTS":
					file.points = Integer.parseInt(words[1]);
					break;

				default:
					// USE THE DATA
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return file;
	}

	private static int[] StringArraytoIntArray(String[] array) {
		int[] res = new int[array.length];
		for (int i = 0; i < array.length; i++)
			res[i] = Integer.parseInt(array[i]);
		return res;
	}
	
	private static char[] StringArraytoCharArray(String[] array) {
		char[] res = new char[array.length];
		for (int i = 0; i < array.length; i++)
			res[i] = array[i].charAt(0);
		return res;
	}

	public static void main(String[] args) {
		PCDFile file = Reader.readFile("res/test.pcd");
	}

}
