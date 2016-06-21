package pcdLoader;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Finder extends JFrame {

	public static void setLookAndFeel(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PCDFile findFile(String windowTitle) throws IOException{
		PCDFile chosenFile = null;
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir") + "/res");
		fc.setDialogTitle(windowTitle);
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
	
}
