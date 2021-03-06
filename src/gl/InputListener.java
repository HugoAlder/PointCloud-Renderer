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

import options.ColorChanger;
import screenShot.ScreenShot;

public class InputListener implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

	private Point mousePreviousPosition;

	/*
	 * MouseWheelListener
	 */

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Viewer.zoom += 0.05f * e.getWheelRotation();
		if (Viewer.zoom < 0.05)
			Viewer.zoom = 0.05;
	}

	/*
	 * MouseMotionListener
	 */

	@Override
	public void mouseDragged(MouseEvent e) {

		int currentX = e.getX();
		int currentY = e.getY();

		int diffX = (int) Math.abs(mousePreviousPosition.getX() - currentX);
		int diffY = (int) Math.abs(mousePreviousPosition.getY() - currentY);

		if (SwingUtilities.isLeftMouseButton(e)) {
			if (currentX < mousePreviousPosition.getX())
				Viewer.x -= diffX / (double) Viewer.frame.getWidth();
			else if (currentX > mousePreviousPosition.getX())
				Viewer.x += diffX / (double) Viewer.frame.getWidth();
			if (currentY < mousePreviousPosition.getY())
				Viewer.y += diffY / (double) Viewer.frame.getHeight();
			else if (currentY > mousePreviousPosition.getY())
				Viewer.y -= diffY / (double) Viewer.frame.getHeight();
		} else if (SwingUtilities.isRightMouseButton(e)) {
			double limiter = 5;
			if (currentX < mousePreviousPosition.getX())
				Viewer.yRotation -= diffX / limiter;
			else if (currentX > mousePreviousPosition.getX())
				Viewer.yRotation += diffX / limiter;
			if (currentY < mousePreviousPosition.getY())
				Viewer.xRotation -= diffY / limiter;
			else if (currentY > mousePreviousPosition.getY())
				Viewer.xRotation += diffY / limiter;
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
			Viewer.y += speedTranslation;
			break;
		case KeyEvent.VK_S:
			Viewer.y += -speedTranslation;
			break;
		case KeyEvent.VK_Q:
		case KeyEvent.VK_A:
			Viewer.x += -speedTranslation;
			break;
		case KeyEvent.VK_D:
			Viewer.x += speedTranslation;
			break;

		case KeyEvent.VK_UP:
			Viewer.xRotation += -speedRotation;
			break;
		case KeyEvent.VK_DOWN:
			Viewer.xRotation += speedRotation;
			break;
		case KeyEvent.VK_LEFT:
			Viewer.yRotation += -speedRotation;
			break;
		case KeyEvent.VK_RIGHT:
			Viewer.yRotation += speedRotation;
			break;

		case KeyEvent.VK_F1:
			try {
				ScreenShot.registerScreenShot(Viewer.frame);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
			break;
		case KeyEvent.VK_F2:
			if (!Viewer.isColorFrameOpen)
				new ColorChanger();
			break;
		case KeyEvent.VK_F3:
			Viewer.resetColor();
			break;
		case KeyEvent.VK_F4:
			Viewer.switchBackgroundColor();
			break;
		case KeyEvent.VK_F5:
			Viewer.resetValues();
			break;
		case KeyEvent.VK_F11:
			if (Viewer.frame.isUndecorated())
				Viewer.setWindowed();
			else
				Viewer.setFullScreen();
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
