package gl;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.opencv.core.Mat;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class Viewer implements GLEventListener {

	private GLU glu;
	private InputListener input = new InputListener();

	private Mat pointCloud;

	public Viewer(GLCanvas canvas, Mat pointCloud) {
		this.pointCloud = pointCloud;
		JFrame frame = new JFrame("JOGL Program");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(800, 800);
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
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glPushMatrix();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glPointSize(1.5f);
		gl.glBegin(GL2.GL_POINTS);
		for (int i = 0; i < pointCloud.rows(); i++) {
			gl.glColor3d(0, 1, 0);
			gl.glVertex3d(pointCloud.get(i, 0)[0], pointCloud.get(i, 1)[0], pointCloud.get(i, 2)[0]);
		}
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
