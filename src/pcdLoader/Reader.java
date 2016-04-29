package pcdLoader;

import java.io.BufferedReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Reader {

	public static PCDFile readFile(String filepath) {

		System.loadLibrary("opencv_java310");
		PCDFile file = new PCDFile(filepath);
		file.data = null;
		int currentLine = 0;

		try {

			BufferedReader br = new BufferedReader(new FileReader(filepath));

			String sCurrentLine;
			for (int i = 0; i < 11; i++) {
				sCurrentLine = br.readLine();
				String[] words = sCurrentLine.trim().split(" ");
				String firstWord = words[0].toUpperCase();
				switch (firstWord) {
				case "#":
					break;
				case "VERSION":
					file.version = Double.parseDouble(words[1]);
					break;
				case "FIELDS":
					file.fields = Arrays.copyOfRange(words, 1, words.length);
					file.colored = file.fields[file.fields.length - 1].equals("rgb");
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
				case "DATA":
					file.datatype = words[1];
					file.data = new Mat(file.points, file.fields.length, CvType.CV_32FC1);
					break;

				}
			}

			// Reading the data in ASCII

			if (file.datatype.equals("ascii")) {
				while ((sCurrentLine = br.readLine()) != null) {
					String[] words = sCurrentLine.trim().split(" ");
					file.data.put(currentLine, 0, StringArraytoFloatArray(words));
					currentLine++;
				}
				
			// Reading the data in binary. Need to start after the header	
				
			} else if (file.datatype.equals("binary")) {
				int readBytes;
				
				//String line = br.readLine();
				
				
				float[] line = new float[4];
				byte[] bytes = new byte[1024];
				FileInputStream in = new FileInputStream(file);
				
				while ((readBytes = in.read(bytes)) != -1) {
					int j = 0;
					while(j < 1024) {
						line = new float[4];
						for (int i = 0; i < 4; i++) {
							int asInt = (bytes[j] & 0xFF) | ((bytes[j + 1] & 0xFF) << 8) | ((bytes[j + 2] & 0xFF) << 16)
									| ((bytes[j + 3] & 0xFF) << 24);
							j += 4;
							float asFloat = Float.intBitsToFloat(asInt);
							line[i] = asFloat;

						}
						file.data.put(currentLine, 0, line);
						currentLine++;
					}	
				}
				in.close();
				file.data.submat(256, file.data.rows(), 0, 3);
				System.out.println("Number of points : " + file.data.rows());
			}

			br.close();
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

	private static float[] StringArraytoFloatArray(String[] array) {
		float[] res = new float[array.length];
		for (int i = 0; i < array.length; i++)
			res[i] = Float.parseFloat(array[i]);
		return res;
	}

}
