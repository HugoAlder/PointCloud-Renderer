package gl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;

import org.opencv.core.Mat;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

import pcdLoader.PCDFile;

public class Viewer implements GLEventListener {

	public static JFrame frame;

	private static PCDFile file;
	private static Mat pointCloud;
	public static boolean whiteBackground = false;
	public static Color forcedColor = null;
	public static boolean isColorFrameOpen = false;

	private GL2 gl;
	private GLU glu;

	private float aspect = 1.0f;

	public static double zoom, x, y;
	public static float xRotation, yRotation;

	public Viewer(JFrame frame, PCDFile file) {
		Viewer.frame = frame;
		Viewer.file = file;
		pointCloud = file.getData();
		resetValues();
	}

	@Override
	public void display(GLAutoDrawable drawable) {

		Viewer.frame.setTitle("FPS: " + (int) drawable.getAnimator().getLastFPS());

		gl = drawable.getGL().getGL2();

		if (!whiteBackground)
			gl.glClearColor(0f, 0f, 0f, 0f);
		else
			gl.glClearColor(1f, 1f, 1f, 0f);

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		double left = -zoom - x;
		double right = zoom - x;
		double bottom = -zoom - y;
		double top = zoom - y;

		if (aspect >= 1.0) {
			left *= aspect;
			right *= aspect;
		} else {
			bottom /= aspect;
			top /= aspect;
		}
		
		gl.glOrtho(left, right, bottom, top, 1, -1);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glLoadIdentity();

		gl.glRotatef(xRotation, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(yRotation, 0.0f, 1.0f, 0.0f);

		gl.glPointSize(2.5f);
		gl.glBegin(GL2.GL_POINTS);
		for (int i = 0; i < pointCloud.rows(); i++) {
			if (forcedColor == null) {

				gl.glColor3d(0.1098, 0.5412, 0.8588);

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

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	@Override
	public void init(GLAutoDrawable drawable) {
		glu = new GLU();
		drawable.getAnimator().setUpdateFPSFrames(40, null);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		aspect = (float) width / (float) height;

		GL2 gl = drawable.getGL().getGL2();

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();

		glu.gluPerspective(3.0, aspect, 1.0, 300.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(0.0, 0.0, 10.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
	}

	private byte[] colorReader(Color c) {
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

	public static void setFullScreen() {
		frame.dispose();
		frame.setUndecorated(true);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		// canvas.requestFocus();
	}

	public static void setWindowed() {
		frame.dispose();
		frame.setUndecorated(false);
		frame.setExtendedState(JFrame.NORMAL);
		frame.setSize(800, 800);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		// canvas.requestFocus();
	}

	public static void resetValues() {
		xRotation = 0.0f;
		yRotation = 0.0f;
		zoom = 0.3;
		x = 0.0;
		y = 0.0;
	}

	public static void changeFile(PCDFile newFile) {
		file = newFile;
		pointCloud = file.getData();
		resetValues();
	}

	public static void openPositionSelector() {
		JDialog frame = new JDialog(Viewer.frame);
		frame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		JPanel panel = new JPanel(null);
		frame.setContentPane(panel);
		panel.setPreferredSize(new Dimension(300, 200));
		frame.setResizable(false);
		panel.setBackground(new Color(238, 238, 238));

		JLabel xLabel = new JLabel("X");
		xLabel.setFont(new Font("arial", 0, 35));
		xLabel.setBounds(10, 10, 25, 25);
		xLabel.setFocusable(false);

		JLabel yLabel = new JLabel("Y");
		yLabel.setFont(new Font("arial", 0, 35));
		yLabel.setBounds(10, 50, 25, 25);
		yLabel.setFocusable(false);

		JLabel zLabel = new JLabel("Z");
		zLabel.setFont(new Font("arial", 0, 35));
		zLabel.setBounds(10, 90, 25, 25);
		zLabel.setFocusable(false);

		SpinnerNumberModel modelX = new SpinnerNumberModel(Viewer.x, -100.0, 100.0, 0.05);
		JSpinner xSpinner = new JSpinner(modelX);
		xSpinner.setFocusable(false);
		xSpinner.setBounds(40, 10, 75, 25);

		SpinnerNumberModel modelY = new SpinnerNumberModel(Viewer.y, -100.0, 100.0, 0.05);
		JSpinner ySpinner = new JSpinner(modelY);
		ySpinner.setFocusable(false);
		ySpinner.setBounds(40, 50, 75, 25);

		SpinnerNumberModel modelZ = new SpinnerNumberModel(Viewer.zoom, 0.05, 100.0, 0.05);
		JSpinner zSpinner = new JSpinner(modelZ);
		zSpinner.setFocusable(false);
		zSpinner.setBounds(40, 90, 75, 25);

		JTextPane text = new JTextPane();
		text.setText("Enter the coordinates to change the camera");
		text.setEditable(false);
		text.setFocusable(false);
		text.setFont(new Font("arial", 0, 20));
		text.setBounds(130, 10, 170, 100);
		text.setBackground(new Color(238, 238, 238));

		JButton okButton = new JButton("OK");
		okButton.setFocusable(false);
		okButton.setFont(new Font("arial", 0, 35));
		okButton.setBounds(100, 120, 100, 80);
		okButton.setForeground(new Color(2, 137, 0));
		okButton.setContentAreaFilled(false);
		okButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				okButton.setForeground(new Color(116, 214, 0));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				okButton.setForeground(new Color(2, 137, 0));
			}

		});
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					xSpinner.commitEdit();
					ySpinner.commitEdit();
					zSpinner.commitEdit();
				} catch (ParseException e1) {
					return;
				}

				Viewer.x = Double.parseDouble(xSpinner.getValue().toString());
				Viewer.y = Double.parseDouble(ySpinner.getValue().toString());
				Viewer.zoom = Double.parseDouble(zSpinner.getValue().toString());

				frame.dispose();
			}
		});

		panel.add(xLabel);
		panel.add(yLabel);
		panel.add(zLabel);
		panel.add(xSpinner);
		panel.add(ySpinner);
		panel.add(zSpinner);
		panel.add(text);
		panel.add(okButton);

		frame.pack();
		frame.setLocationRelativeTo(Viewer.frame);
		frame.setVisible(true);
	}

}
