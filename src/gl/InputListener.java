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

	private Viewer v;

	private Point mousePreviousPosition;

	private ScreenShot screen = new ScreenShot();

	public InputListener(Viewer v) {
		this.v = v;
		resetValues();
	}

	public void resetValues() {
		v.xRotation = 0.0f;
		v.yRotation = 0.0f;
		v.zoom = 0.3;
		v.x = 0.0;
		v.y = 0.0;
	}

	/*
	 * MouseWheelListener
	 */

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		v.zoom += 0.05f * e.getWheelRotation();
		if (v.zoom < 0.05)
			v.zoom = 0.05;
		else if (v.zoom > 2.0)
			v.zoom = 2.0;
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
			double limiter = 6.0 - v.zoom * 2.0;
			if (currentX < mousePreviousPosition.getX())
				v.x += -speed * diffX / limiter;
			else if (currentX > mousePreviousPosition.getX())
				v.x += speed * diffX / limiter;
			if (currentY < mousePreviousPosition.getY())
				v.y += speed * diffY / limiter;
			else if (currentY > mousePreviousPosition.getY())
				v.y += -speed * diffY / limiter;
		} else if (SwingUtilities.isRightMouseButton(e)) {
			double multiplier = 35.0;
			if (currentX < mousePreviousPosition.getX())
				v.yRotation += -speed * diffX * multiplier;
			else if (currentX > mousePreviousPosition.getX())
				v.yRotation += speed * diffX * multiplier;
			if (currentY < mousePreviousPosition.getY())
				v.xRotation += speed * diffY * multiplier;
			else if (currentY > mousePreviousPosition.getY())
				v.xRotation += -speed * diffY * multiplier;
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
			v.y += speedTranslation;
			break;
		case KeyEvent.VK_S:
			v.y += -speedTranslation;
			break;
		case KeyEvent.VK_Q:
		case KeyEvent.VK_A:
			v.x += -speedTranslation;
			break;
		case KeyEvent.VK_D:
			v.x += speedTranslation;
			break;

		case KeyEvent.VK_UP:
			v.xRotation += -speedRotation;
			break;
		case KeyEvent.VK_DOWN:
			v.xRotation += speedRotation;
			break;
		case KeyEvent.VK_LEFT:
			v.yRotation += -speedRotation;
			break;
		case KeyEvent.VK_RIGHT:
			v.yRotation += speedRotation;
			break;

		case KeyEvent.VK_F1:
			try {
				screen.registerScreenShot(v.frame);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
			break;
		case KeyEvent.VK_F2:
			new ColorChanger();
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
		case KeyEvent.VK_F11:
			if (v.frame.isUndecorated())
				v.setWindowed();
			else
				v.setFullScreen();

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
