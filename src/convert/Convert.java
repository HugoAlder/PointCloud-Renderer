package convert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Convert {
	
	static File fileOut = new File("res/bunnyBinary.pcd");
	
	public static void main(String[] args) throws IOException {
		fileOut.delete();
		String filepath = "res/bunny.pcd";
		File file = new File(filepath);	
		getHeader(file);
		
	}

	public static String AsciiToBinary(/*String asciiString*/) throws IOException{  
	
		String filepath = "res/bunny.pcd";
		String asciiString = "";
		File file = new File(filepath);
		File binaryFile = new File("res/bunneBinary.pcd");
		FileWriter fw = new FileWriter(binaryFile.getAbsolutePath(),true);
		BufferedWriter output = new BufferedWriter(fw);
		String header = getHeader(file);
		
		output.write(header);
		
		
		
		
		

		BufferedReader br = new BufferedReader(new FileReader(filepath));
	
		String s = "";
		String tmpS = "";
		tmpS = br.readLine();
		
		while(tmpS.length() > 4 && !(tmpS.substring(0, 4).equals("DATA"))) {

			tmpS = br.readLine();
		}
		while((tmpS=br.readLine()) != null){
			String[] words = tmpS.trim().split(" ");
			
		}
		
        byte[] bytes = asciiString.getBytes();  
        StringBuilder binary = new StringBuilder();  
        for (byte b : bytes)  
        {  
           int val = b;  
           for (int i = 0; i < 8; i++)  
           {  
              binary.append((val & 128) == 0 ? 0 : 1);  
              val <<= 1;  
           }  
          // binary.append(' ');  
        }  
        return binary.toString();  
  }  
	
	public static String getHeader(File file) throws IOException{
					
		BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()));	
		FileWriter fw = new FileWriter(fileOut.getAbsolutePath(),true);
		BufferedWriter output = new BufferedWriter(fw);
		String s = "";
		String tmpS = "";
		tmpS = br.readLine();
		
		while(tmpS.length() > 4 && !(tmpS.substring(0, 4).equals("DATA"))) {
			s += tmpS + "\n";
			output.write(tmpS);
			output.flush();
			tmpS = br.readLine();
		}

		s+=tmpS + "\n";

		output.close();
		return s;
		
	}
	
}
