package gl;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

import options.Options;
import screenShot.ScreenShot;

public class InputListener implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

	private Viewer v;

	private Point mousePreviousPosition;

	private double zoom, x, y;
	private float xRotation, yRotation;

	private ScreenShot screen = new ScreenShot();

	public InputListener(Viewer v) {
		this.v = v;
		resetValues();
	}

	public void resetValues() {
		xRotation = 0.0f;
		yRotation = 0.0f;
		zoom = 0.3;
		x = 0.0;
		y = 0.0;
	}

	public double getZoom() {
		return zoom;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public float getxRotation() {
		return xRotation;
	}

	public float getyRotation() {
		return yRotation;
	}

	public Point getMousePreviousPosition() {
		return mousePreviousPosition;
	}

	/*
	 * MouseWheelListener
	 */

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		zoom += 0.05f * e.getWheelRotation();
		if (zoom < 0.05)
			zoom = 0.05;
		else if (zoom > 2.0)
			zoom = 2.0;
	}

	/*
	 * MouseMotionListener
	 */

	@Override
	public void mouseDragged(MouseEvent e) {
		float speed = 0.005f;

		int currentX = e.getX();
		int currentY = e.getY();

		int diffX = (int) Math.abs(mousePreviousPosition.getX() - currentX);
		int diffY = (int) Math.abs(mousePreviousPosition.getY() - currentY);

		if (SwingUtilities.isLeftMouseButton(e)) {
			double limiter = 5.0 - zoom * 2;
			if (currentX < mousePreviousPosition.getX())
				x += -speed * diffX / limiter;
			else if (currentX > mousePreviousPosition.getX())
				x += speed * diffX / limiter;
			if (currentY < mousePreviousPosition.getY())
				y += speed * diffY / limiter;
			else if (currentY > mousePreviousPosition.getY())
				y += -speed * diffY / limiter;
		} else if (SwingUtilities.isRightMouseButton(e)) {
			double multiplier = 35.0;
			if (currentX < mousePreviousPosition.getX())
				yRotation += -speed * diffX * multiplier;
			else if (currentX > mousePreviousPosition.getX())
				yRotation += speed * diffX * multiplier;
			if (currentY < mousePreviousPosition.getY())
				xRotation += speed * diffY * multiplier;
			else if (currentY > mousePreviousPosition.getY())
				xRotation += -speed * diffY * multiplier;
		}

		mousePreviousPosition = new Point(currentX, currentY);
	}

	/*
	 * MouseListener
	 */

	@Override
	public void mousePressed(MouseEvent e) {
		mousePreviousPosition = new Point(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	/*
	 * KeyListener
	 */

	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {

		float speedTranslation = 0.05f;
		float speedRotation = 3f;

		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_Z:
		case KeyEvent.VK_W:
			y += speedTranslation;
			break;
		case KeyEvent.VK_S:
			y += -speedTranslation;
			break;
		case KeyEvent.VK_Q:
		case KeyEvent.VK_A:
			x += -speedTranslation;
			break;
		case KeyEvent.VK_D:
			x += speedTranslation;
			break;

		case KeyEvent.VK_UP:
			xRotation += -speedRotation;
			break;
		case KeyEvent.VK_DOWN:
			xRotation += speedRotation;
			break;
		case KeyEvent.VK_LEFT:
			yRotation += -speedRotation;
			break;
		case KeyEvent.VK_RIGHT:
			yRotation += speedRotation;
			break;

		case KeyEvent.VK_F1:
			try {
				screen.registerScreenShot(v);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
			break;
		case KeyEvent.VK_F2:
			new Options();
			break;
		case KeyEvent.VK_F3:
			Viewer.resetColor();
			break;
		case KeyEvent.VK_F4:
			Viewer.switchBackgroundColor();
			break;
		case KeyEvent.VK_F5:
			resetValues();
			break;

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
