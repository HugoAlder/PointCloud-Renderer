package launch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import gl.InputListener;
import gl.Viewer;
import options.HUD;
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
		GLJPanel glPanel = new GLJPanel(caps);

		JFrame frame = new JFrame("JOGL Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(800, 800));

		Viewer viewer = new Viewer(frame, file);
		glPanel.addGLEventListener(viewer);
		InputListener input = new InputListener();
		glPanel.addMouseWheelListener(input);
		glPanel.addMouseMotionListener(input);
		glPanel.addMouseListener(input);
		glPanel.addKeyListener(input);

		JPanel hud = new HUD();
		hud.setBounds(0, 0, 800, 800);

		glPanel.setBounds(0, 0, 800, 800);

		JLayeredPane mainPanel = new JLayeredPane();
		mainPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				glPanel.setSize(mainPanel.getSize());
				hud.setSize(mainPanel.getSize());
			}
		});

		frame.setContentPane(mainPanel);

		mainPanel.add(glPanel, new Integer(0));
		mainPanel.add(hud, new Integer(1));

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		FPSAnimator animator = new FPSAnimator(glPanel, 60);
		animator.start();

		glPanel.requestFocus();
	}

}
