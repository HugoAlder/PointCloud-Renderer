package convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.NoSuchAlgorithmException;

public class Converter {

	public boolean asciiToBinary(String filePath, String encoding) throws IOException, NoSuchAlgorithmException {

		int bufferSize;
		short shortValue = new Short((short) 0);
		float floatValue = new Float(0f);
		double doubleValue = new Double(0);

		File file = new File(filePath);

		BufferedReader br = new BufferedReader(new FileReader(file));
		String oldName = file.getName();
		File newFile = new File(oldName.substring(0, oldName.length() - 4) + "_copy.pcd");
		newFile.createNewFile();
		String tmpS = "";
		String s = "";

		tmpS = br.readLine();

		while (tmpS.length() > 4 && !(tmpS.substring(0, 4).equals("DATA"))) {
			if (tmpS.substring(0, 4).equals("SIZE")) {
				tmpS = "SIZE ";
				String[] words = tmpS.trim().split(" ");
				int dimensions = words.length - 1;
				String encodingSymbol = "";
				switch (encoding) {
				case "short":
					encodingSymbol = "2 ";
					break;
				case "float":
					encodingSymbol = "4 ";
					break;
				case "double":
					encodingSymbol = "8 ";
					break;
				}
				for(int i = 0; i<dimensions; i++) {
					tmpS += encodingSymbol;
				}
			}
			s += tmpS + "\n";
			tmpS = br.readLine();
		}

		s += "DATA binary \n";

		System.out.println(s);
		BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
		bw.write(s);
		bw.close();

		FileOutputStream outputStream = new FileOutputStream(newFile, true);

		String line = "";
		int cptLine = 11;
		while ((line = br.readLine()) != null) {
			System.out.println(cptLine + " " + line);
			String[] words = line.trim().split(" ");
			byte[] buffer = new byte[0];
			for (int i = 0; i < words.length; i++) {
				switch (encoding) {
				case "short":
					shortValue = Short.parseShort(words[i]);
					bufferSize = 2;
					buffer = new byte[bufferSize];
					ByteBuffer.wrap(buffer, 0, bufferSize).order(ByteOrder.LITTLE_ENDIAN).putShort(shortValue);
					break;
				case "float":
					floatValue = Float.parseFloat(words[i]);
					bufferSize = 4;
					buffer = new byte[bufferSize];
					ByteBuffer.wrap(buffer, 0, bufferSize).order(ByteOrder.LITTLE_ENDIAN).putFloat(floatValue);
					break;
				case "double":
					doubleValue = Double.parseDouble(words[i]);
					bufferSize = 8;
					buffer = new byte[bufferSize];
					ByteBuffer.wrap(buffer, 0, bufferSize).order(ByteOrder.LITTLE_ENDIAN).putDouble(doubleValue);
					break;
				default:
					System.out.println("Format not handled");
					return false;
				}
				outputStream.write(buffer);
			}
			cptLine++;
		}

		outputStream.close();
		br.close();

		return true;
	}

}