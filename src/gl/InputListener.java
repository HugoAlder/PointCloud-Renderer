package gl;

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

public class InputListener implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

	private float zTranslation = 0.0f;
	private float xTranslation = 0.0f;
	private float yTranslation = 0.0f;
	private float xRotation = 0.0f;
	private float yRotation = 0.0f;
	private Point mousePreviousPosition;

	public void resetValues() {
		zTranslation = 0.0f;
		xTranslation = 0.0f;
		yTranslation = 0.0f;
		xRotation = 0.0f;
		yRotation = 0.0f;
	}

	public float getzTranslation() {
		return zTranslation;
	}

	public float getxTranslation() {
		return xTranslation;
	}

	public float getyTranslation() {
		return yTranslation;
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
		zTranslation = -0.3f * e.getWheelRotation();
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
			if (currentX < mousePreviousPosition.getX())
				xTranslation = -speed * diffX / 10.0f;
			else if (currentX > mousePreviousPosition.getX())
				xTranslation = speed * diffX / 10.0f;
			if (currentY < mousePreviousPosition.getY())
				yTranslation = speed * diffY / 10.0f;
			else if (currentY > mousePreviousPosition.getY())
				yTranslation = -speed * diffY / 10.0f;
		} else if (SwingUtilities.isRightMouseButton(e)) {
			if (currentX < mousePreviousPosition.getX())
				yRotation = -speed * diffX * 10;
			else if (currentX > mousePreviousPosition.getX())
				yRotation = speed * diffX * 10;
			if (currentY < mousePreviousPosition.getY())
				xRotation = speed * diffY * 10;
			else if (currentY > mousePreviousPosition.getY())
				xRotation = -speed * diffY * 10;
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

		case KeyEvent.VK_UP:
			xRotation = -0.05f;
			break;
		case KeyEvent.VK_DOWN:
			xRotation = 0.05f;
			break;
		case KeyEvent.VK_LEFT:
			yRotation = -0.05f;
			break;
		case KeyEvent.VK_RIGHT:
			yRotation = 0.05f;
			break;

		case KeyEvent.VK_F1:
			Viewer.switchBackgroundColor();
			break;
		case KeyEvent.VK_F2:
			new Options();
			break;
		case KeyEvent.VK_F3:
			Viewer.resetColor();
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
