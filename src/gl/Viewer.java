package gl;

import java.awt.BorderLayout;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

import org.opencv.core.Mat;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

public class Viewer implements GLEventListener {

	private GLU glu;
	float z = 0;

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
					z = -0.3f;
				else
					z = 0.3f;
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

		gl.glTranslatef(0, 0, z);
		z = 0;

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
		float aspect = width / height;
		glu.gluPerspective(3.0, aspect, 1.0, 300.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	}

}
