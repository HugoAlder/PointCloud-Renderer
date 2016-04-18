package launch;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import gl.InputListener;
import gl.Viewer;
import pcdLoader.Finder;
import pcdLoader.PCDFile;
import pcdLoader.Reader;

public class Launcher {

	public static void main(String[] args) {
		Finder.setLookAndFeel();
		// PCDFile file = Finder.findFile();
		PCDFile file = Reader.readFile("res/bunny.pcd");

		GLProfile.initSingleton();
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);

		JFrame frame = new JFrame("JOGL Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);

		Viewer viewer = new Viewer(frame, file);
		canvas.addGLEventListener(viewer);

		JLayeredPane mainPanel = new JLayeredPane();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setPreferredSize(frame.getSize());
		frame.add(mainPanel);

		mainPanel.add(canvas);

		InputListener input = new InputListener(viewer);
		canvas.addMouseWheelListener(input);
		canvas.addMouseMotionListener(input);
		canvas.addMouseListener(input);
		canvas.addKeyListener(input);

		frame.pack();
		frame.setVisible(true);

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();

		canvas.requestFocus();
	}

}
