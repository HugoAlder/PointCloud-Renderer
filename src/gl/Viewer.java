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
	private Point mousePreviousPosition;
	private Mat pointCloud;

	public Viewer(GLCanvas canvas, Mat pointCloud) {
		this.pointCloud = pointCloud;
		JFrame frame = new JFrame("JOGL Program");
		frame.setLayout(new BorderLayout());
		frame.setSize(800, 800);

		frame.add(canvas);

		frame.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() == 1)
					zTranslation = -0.3f;
				else
					zTranslation = 0.3f;
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

				if (currentX < mousePreviousPosition.getX()) {
					xTranslation = -speed * diffX / 10.0f;
				} else if (currentX > mousePreviousPosition.getX()) {
					xTranslation = speed * diffX / 10.0f;
				}

				if (currentY < mousePreviousPosition.getY()) {
					yTranslation = speed * diffY / 10.0f;
				} else if (currentY > mousePreviousPosition.getY()) {
					yTranslation = -speed * diffY / 10.0f;
				}

				mousePreviousPosition = new Point(currentX, currentY);
			}

		});

		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				mousePreviousPosition = new Point(e.getX(), e.getY());
			}

		});

		frame.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {

				int key = e.getKeyCode();
				switch (key) {
				case KeyEvent.VK_Z:
					yTranslation = 0.01f;
					break;
				case KeyEvent.VK_S:
					yTranslation = -0.01f;
					break;

				case KeyEvent.VK_Q:
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

	private void update() {
		// Put the changes to do (ex: change the coordonate of a point)
	}

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0f, 0f, 0f, 0f);
		gl.glPushMatrix();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glTranslatef(xTranslation, yTranslation, zTranslation);
		zTranslation = 0;
		xTranslation = 0;
		yTranslation = 0;

		// gl.glRotatef(2f, 0, 1, 0);

		gl.glBegin(GL2.GL_POINTS);
		for (int i = 0; i < pointCloud.rows(); i++) {
			gl.glColor3d(0, 1, 0);
			gl.glVertex3d(pointCloud.get(i, 0)[0], pointCloud.get(i, 1)[0], pointCloud.get(i, 2)[0]);
		}
		gl.glEnd();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		update();
		render(drawable);
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
