package pcdLoader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
			br.mark(1000);

			String sCurrentLine;
			while (file.datatype == null) {
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
					if (file.datatype.equals("ascii")) {
						file.data = new Mat(file.points, file.fields.length, CvType.CV_32FC1);
					} else if (file.datatype.equals("binary")) {

						// 256 being the size of the header. We can't skip it so
						// we have to make room for it in the matrix. We'll
						// later subdivide the matrix.

						file.data = new Mat(file.points, file.fields.length, CvType.CV_32FC1);
					}

					break;

				}
			}

			// Reading the data in ASCII.

			if (file.datatype.equals("ascii")) {
				while ((sCurrentLine = br.readLine()) != null) {
					String[] words = sCurrentLine.trim().split(" ");
					file.data.put(currentLine, 0, StringArraytoFloatArray(words));
					currentLine++;
				}

				br.close();

				System.out.println("Number of points : " + file.data.rows());

			}

			// Reading the data in binary. Need to start after the header.

			else if (file.datatype.equals("binary")) {

				FileInputStream in = new FileInputStream(filepath);			
				br.reset();				
				String s = "";
				String tmpS = "";	
				tmpS = br.readLine();
				
				while(tmpS.length() > 4 && !(tmpS.substring(0, 4).equals("DATA"))) {
					s += tmpS + "\n";
					tmpS = br.readLine();
				}
				
				s += tmpS + "\n";			
				byte[] header = s.getBytes("UTF-8");
				in.read(header);

				int tmp = 0;
				currentLine = 0;
				int size = file.size[0];
				byte[] b = new byte[size];
				int dimensionsAndRgb = file.fields.length;
				float[] line = new float[dimensionsAndRgb];

				while (in.read(b) != -1) {
					float value = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getFloat();
					line[tmp] = value;
					if (tmp == dimensionsAndRgb - 1) {
						file.data.put(currentLine, 0, line);
						tmp = -1;
						currentLine++;
					}
					tmp++;
					size = file.size[tmp];
					b = new byte[size];
				}

				in.close();

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

	private static float[] StringArraytoFloatArray(String[] array) {
		float[] res = new float[array.length];
		for (int i = 0; i < array.length; i++)
			res[i] = Float.parseFloat(array[i]);
		return res;
	}

}
