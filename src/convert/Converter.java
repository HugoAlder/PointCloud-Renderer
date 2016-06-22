package convert;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import gl.Viewer;
import pcdLoader.PCDFile;

public class Converter {

	private static int size = 4;

	/**
	 * 
	 * @param file
	 * @return true if a new file is created, false if not.
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean asciiToBinary(PCDFile file) throws IOException, NoSuchAlgorithmException {

		int bufferSize;
		float floatValue = new Float(0f);
		double doubleValue = new Double(0);

		/* First we have to read the header on copy it. */
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String oldName = file.getName();
		File newFile = new File("res/" + oldName.substring(0, oldName.length() - 4) + "_copy.pcd");
		newFile.createNewFile();
		String tmpS = "";
		String s = "";

		tmpS = br.readLine();

		while (tmpS.length() > 4 && !(tmpS.substring(0, 4).equals("DATA"))) {
			if (tmpS.substring(0, 4).equals("SIZE")) {
				String[] words = tmpS.trim().split(" ");
				tmpS = "SIZE";
				int dimensions = words.length - 1;
				String encodingSymbol = "";
				switch (size) {
				case 4:
					encodingSymbol = " 4";
					break;
				case 8:
					encodingSymbol = " 8";
					break;
				}
				for (int i = 0; i < dimensions; i++) {
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

		/* Then we just have to copy the bytes. */
		
		FileOutputStream outputStream = new FileOutputStream(newFile, true);

		String line = "";
		int cptLine = 11;
		while ((line = br.readLine()) != null) {
			System.out.println(cptLine + " " + line);
			String[] words = line.trim().split(" ");
			byte[] buffer = new byte[0];
			for (int i = 0; i < words.length; i++) {
				switch (size) {
				case 4:
					floatValue = Float.parseFloat(words[i]);
					bufferSize = 4;
					buffer = new byte[bufferSize];
					ByteBuffer.wrap(buffer, 0, bufferSize).order(ByteOrder.LITTLE_ENDIAN).putFloat(floatValue);
					break;
				case 8:
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

	public static void openSizeSelector(PCDFile file) {
		JDialog frame = new JDialog(Viewer.frame);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel(null);
		frame.setContentPane(panel);
		panel.setPreferredSize(new Dimension(300, 200));
		frame.setResizable(false);
		panel.setBackground(new Color(238, 238, 238));

		Integer[] choices = { 4, 8 };
		JComboBox<Integer> sizeSelector = new JComboBox<Integer>(choices);
		sizeSelector.setFont(new Font("arial", 0, 35));
		sizeSelector.setBounds(10, 10, 100, 100);
		sizeSelector.setFocusable(false);
		sizeSelector.setSelectedIndex(0);

		JTextPane text = new JTextPane();
		text.setText("Choose a size for the convertion");
		text.setEditable(false);
		text.setFocusable(false);
		text.setFont(new Font("arial", 0, 27));
		text.setBounds(130, 10, 170, 100);
		text.setBackground(new Color(238, 238, 238));

		JButton okButton = new JButton("OK");
		okButton.setFocusable(false);
		okButton.setFont(new Font("arial", 0, 35));
		okButton.setBounds(100, 120, 100, 80);
		okButton.setForeground(new Color(2, 137, 0));
		okButton.setContentAreaFilled(false);
		okButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				okButton.setForeground(new Color(116, 214, 0));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				okButton.setForeground(new Color(2, 137, 0));
			}

		});
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				size = (int) sizeSelector.getSelectedItem();
				frame.dispose();
				try {
					Converter.asciiToBinary(file);
				} catch (NoSuchAlgorithmException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		panel.add(sizeSelector);
		panel.add(text);
		panel.add(okButton);

		frame.pack();
		frame.setLocationRelativeTo(Viewer.frame);
		frame.setVisible(true);
	}

}
