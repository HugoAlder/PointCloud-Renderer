package gl;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Viewer implements GLEventListener{

	public static void main(String[] args) {
		GLProfile.initSingleton();
		
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);
		
		JFrame frame = new JFrame("JOGL Program");
		frame.setLayout(new BorderLayout());
		frame.setSize(800, 800);
		frame.add(canvas);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		canvas.addGLEventListener(new Viewer());

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();
	}
	
	private void update() {
		// Put the changes to do (ex: change the coordonate of a point)
	}

	private void render(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);

		gl.glBegin(GL.GL_TRIANGLES);
		gl.glColor3f(1, 0, 0);
		gl.glVertex2f(-1, -1);
		gl.glColor3f(0, 1, 0);
		gl.glVertex2f(0, 1);
		gl.glColor3f(0, 0, 1);
		gl.glVertex2f(1, -1);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
