package gl;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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
	private float zTranslation = 0;
	private float xTranslation = 0;
	private float yTranslation = 0;
	private float xRotation = 0;
	private float yRotation = 0;
	private float zRotation = 0;
	private Point mousePreviousPosition;
	private Mat pointCloud;

	private boolean isMouseClicked = false;

	public Viewer(GLCanvas canvas, Mat pointCloud) {
		this.pointCloud = pointCloud;
		JFrame frame = new JFrame("JOGL Program");
		frame.setLayout(new BorderLayout());
		frame.setSize(800, 800);

		frame.add(canvas);

		canvas.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				zTranslation = -0.3f * e.getWheelRotation();
			}
		});

		canvas.addMouseMotionListener(new MouseAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				float speed = 0.005f;

				int currentX = e.getX();
				int currentY = e.getY();

				int diffX = (int) Math.abs(mousePreviousPosition.getX() - currentX);
				int diffY = (int) Math.abs(mousePreviousPosition.getY() - currentY);

				if (SwingUtilities.isLeftMouseButton(e)) {
					if (currentX < mousePreviousPosition.getX())
						xTranslation = -speed * diffX / 10.0f;
					else if (currentX > mousePreviousPosition.getX())
						xTranslation = speed * diffX / 10.0f;
					if (currentY < mousePreviousPosition.getY())
						yTranslation = speed * diffY / 10.0f;
					else if (currentY > mousePreviousPosition.getY())
						yTranslation = -speed * diffY / 10.0f;
				} else if (SwingUtilities.isRightMouseButton(e)) {

				}

				mousePreviousPosition = new Point(currentX, currentY);
			}

		});

		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				mousePreviousPosition = new Point(e.getX(), e.getY());
				isMouseClicked = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				isMouseClicked = false;
			}

		});

		canvas.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {

				int key = e.getKeyCode();
				switch (key) {
				case KeyEvent.VK_Z:
				case KeyEvent.VK_W:
					yTranslation = 0.01f;
					break;
				case KeyEvent.VK_S:
					yTranslation = -0.01f;
					break;
				case KeyEvent.VK_Q:
				case KeyEvent.VK_A:
					xTranslation = -0.01f;
					break;
				case KeyEvent.VK_D:
					xTranslation = 0.01f;
					break;
				}
			}

		});

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();
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

		gl.glRotatef(1, xRotation, yRotation, zRotation);
		xRotation = 0;
		yRotation = 0;
		zRotation = 0;

		gl.glTranslatef(xTranslation, yTranslation, zTranslation);
		zTranslation = 0;
		xTranslation = 0;
		yTranslation = 0;

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

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
