package gl;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.opencv.core.Mat;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import pcdLoader.PCDFile;

@SuppressWarnings("serial")
public class Viewer extends JFrame implements GLEventListener {

	private GLU glu;
	private InputListener input = new InputListener(this);

	private PCDFile file;
	private Mat pointCloud;
	private static boolean whiteBackground = false;
	private static Color forcedColor = null;

	public static JFrame frame;
	public static int WIDTH = 800;
	public static int HEIGHT = 600;
	public static GL2 gl;

	public Viewer(GLCanvas canvas, PCDFile file) {
		super("JOGL Program");
		this.file = file;
		pointCloud = file.getData();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		add(canvas);

		canvas.addMouseWheelListener(input);
		canvas.addMouseMotionListener(input);
		canvas.addMouseListener(input);
		canvas.addKeyListener(input);

		this.setVisible(true);

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();
		canvas.requestFocus();

	}

	@Override
	public void display(GLAutoDrawable drawable) {

		gl = drawable.getGL().getGL2();

		if (!whiteBackground)
			gl.glClearColor(0f, 0f, 0f, 0f);
		else
			gl.glClearColor(1f, 1f, 1f, 0f);

		gl.glPushMatrix();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glTranslatef(input.getxTranslation(), input.getyTranslation(), input.getzTranslation());
		
		gl.glRotatef(input.getxRotation(), 1.0f, 0.0f, 0.0f);
		gl.glRotatef(input.getyRotation(), 0.0f, 1.0f, 0.0f);
		
		gl.glPointSize(1.5f);
		gl.glBegin(GL2.GL_POINTS);
		for (int i = 0; i < pointCloud.rows(); i++) {
			if (forcedColor == null) {

				gl.glColor3d(0, 1, 0);

				if (file.isColored()) {
					float rgb = (float) pointCloud.get(i, pointCloud.cols() - 1)[0];
					int rgb2 = Float.floatToIntBits(rgb);
					byte[] colors = colorReader(new Color(rgb2));
					gl.glColor3ub(colors[0], colors[1], colors[2]);
				}
			} else {
				byte[] colors = colorReader(forcedColor);
				gl.glColor3ub(colors[0], colors[1], colors[2]);
			}

			gl.glVertex3d(pointCloud.get(i, 0)[0], pointCloud.get(i, 1)[0], pointCloud.get(i, 2)[0]);	
		}
		gl.glEnd();

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

	public byte[] colorReader(Color c) {
		byte[] out = new byte[3];
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		out[0] = (byte) r;
		out[1] = (byte) g;
		out[2] = (byte) b;
		return out;
	}

	public static void forceColor(Color c) {
		forcedColor = c;
	}

	public static void resetColor() {
		forcedColor = null;
	}
	
	public static void switchBackgroundColor() {
		whiteBackground = !whiteBackground;
	}
	
}
