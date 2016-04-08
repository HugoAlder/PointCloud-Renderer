package gl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.opencv.core.Mat;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import pcdLoader.PCDFile;

public class Viewer implements GLEventListener {

	private GLU glu;
	private InputListener input = new InputListener();

	private PCDFile file;
	private Mat pointCloud;

	public static JFrame frame;
	public static int WIDTH = 800;
	public static int HEIGHT = 800;
	public static GL2 GLD;
	
	
	public Viewer(GLCanvas canvas, PCDFile file) {
		this.file = file;
		pointCloud = file.getData();
		frame = new JFrame("JOGL Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(WIDTH, HEIGHT);
		frame.add(canvas);

		canvas.addMouseWheelListener(input);
		canvas.addMouseMotionListener(input);
		canvas.addMouseListener(input);
		canvas.addKeyListener(input);

		frame.setVisible(true);

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();
		canvas.requestFocus();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1f, 1f, 1f, 0f);
		gl.glPushMatrix();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	
		gl.glPointSize(1.5f);
		gl.glBegin(GL2.GL_POINTS);
		for (int i = 0; i < pointCloud.rows(); i++) {
			gl.glColor3d(0, 1, 0);

			if (file.isColored()) {
				float rgb = (float) pointCloud.get(i, pointCloud.cols() - 1)[0];
				int rgb2 = Float.floatToIntBits(rgb);
				Color c = new Color(rgb2);
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				byte r2 = (byte) r;
				byte g2 = (byte) g;
				byte b2 = (byte) b;
				gl.glColor3ub(r2, g2, b2);
			}

			gl.glVertex3d(pointCloud.get(i, 0)[0], pointCloud.get(i, 1)[0], pointCloud.get(i, 2)[0]);
		}
		GLD = gl;
		gl.glEnd();

		gl.glTranslatef(input.getxTranslation(), input.getyTranslation(), input.getzTranslation());
		// gl.glRotatef(1, input.getxRotation(), input.getyRotation(), 0.0f);
		input.resetValues();

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		glu = new GLU();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		float aspect = (float) width / (float) height;
		glu.gluPerspective(3.0, aspect, 1.0, 300.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	}

}
