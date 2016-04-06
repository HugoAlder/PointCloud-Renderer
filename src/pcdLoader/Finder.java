package pcdLoader;

import java.awt.Dimension;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Finder extends JFrame {

	private static final long serialVersionUID = 1L;

	public Finder() {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PCDFile fileFinder() {

		PCDFile chosenFile = null;
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Select your PCD file");
		Dimension d = new Dimension(800, 600);
		fc.setPreferredSize(d);
		FileFilter filter = new FileNameExtensionFilter("PCD files", "pcd");
		fc.setFileFilter(filter);

		int status = fc.showOpenDialog(null);

		if (status == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			chosenFile = Reader.readFile(f.getAbsolutePath());
		}

		return chosenFile;
	}
	
	public static void main(String[] args) {
		Finder c = new Finder();
		c.fileFinder();
	}

}
