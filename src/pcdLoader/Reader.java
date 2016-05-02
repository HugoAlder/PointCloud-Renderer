package pcdLoader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
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
						// we have to male room for it in the matrix. We'll
						// later subdivide the matrix.

						file.data = new Mat(file.points + 256, file.fields.length, CvType.CV_32FC1);
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

			// Reading the data in binary. Need to start after the header. For
			// now works with 4 bytes (size) data only.

			else if (file.datatype.equals("binary")) {

				int size = file.size[0];
				byte[] buffer = new byte[16];
				float[] line = new float[size];
				FileInputStream in = new FileInputStream(file);

				/*
				 * while ((in.read(buffer)) != -1) { int j = 0; while (j < 1024)
				 * { line = new float[size]; for (int i = 0; i < size; i++) {
				 * int asInt = (buffer[j] & 0xff) | ((buffer[j + 1] & 0xff) <<
				 * 8) | ((buffer[j + 2] & 0xff) << 16) | ((buffer[j + 3] & 0xff)
				 * << 24); j += size; float asFloat =
				 * Float.intBitsToFloat(asInt); line[i] = asFloat; }
				 * file.data.put(currentLine, 0, line); currentLine++; } }
				 */
				
				while ((in.read(buffer)) != -1) {
					int k = 0;
					while(k < 16) {
						byte[] subBuffer = new byte[4];
						for(int l = 0; l < 4; l++) {
							subBuffer[l] = buffer[k + l];
						}
						ByteBuffer wrapped = ByteBuffer.wrap(subBuffer);
						float asFloat = wrapped.getFloat();
						line[k/4] = asFloat;
						k += 4;
					}
					file.data.put(currentLine, 0, line); 
					currentLine++;
				}

				in.close();

				// Removing the header by subdividing the matrix.

				file.data = file.data.submat(256, file.data.rows(), 0, 3);
				System.out.println("CurrentLine value : " + currentLine);
				System.out.println("Number of points : " + file.data.rows());
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
