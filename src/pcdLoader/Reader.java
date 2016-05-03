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
						// we have to make room for it in the matrix. We'll
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

			// Reading the data in binary. Need to start after the header.

			else if (file.datatype.equals("binary")) {

				int lineSize = file.size.length;
				float[] line = new float[lineSize];
				System.out.println("Size of a line : " + lineSize);
				int lineSizeInBytes = 0;
				for (int i = 0; i < lineSize; i++) {
					lineSizeInBytes += file.size[i];
				}
				System.out.println("Size of a line in bytes : " + lineSizeInBytes);
				byte[] buffer = new byte[lineSizeInBytes];
				FileInputStream in = new FileInputStream(file);
				int bytesRead = 0;
				int tmp = 0;

				while ((bytesRead = in.read(buffer)) != -1) {
					int k = 0;
					while (k < bytesRead) {
						byte[] subBuffer = new byte[file.size[k/lineSize]];
						for (int l = 0; l < subBuffer.length; l++) {
							subBuffer[l] = buffer[k + l];
						}
						ByteBuffer wrapped = ByteBuffer.wrap(subBuffer);
						float asFloat = wrapped.getFloat();
						line[k / lineSize] = asFloat;
						k += subBuffer.length;
					}
					file.data.put(currentLine, 0, line);
					tmp = bytesRead;
					currentLine++;
				}
				System.out.println("Last bytesRead : " + tmp);
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
