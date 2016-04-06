package launch;

import pcdLoader.Finder;
import pcdLoader.PCDFile;

public class Launcher {

	public static void main(String[] args) {
		Finder.setLookAndFeel();
		PCDFile file = Finder.findFile();
	}
	
}
