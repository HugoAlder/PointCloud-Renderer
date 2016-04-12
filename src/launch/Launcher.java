package launch;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

import gl.Viewer;
import pcdLoader.Finder;
import pcdLoader.PCDFile;
import pcdLoader.Reader;

public class Launcher {

	public static void main(String[] args) {
		Finder.setLookAndFeel();
		PCDFile file = Finder.findFile();
		//PCDFile file = Reader.readFile("res/bunny.pcd");

		GLProfile.initSingleton();
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);
		canvas.addGLEventListener(new Viewer(canvas, file));
	}

}
