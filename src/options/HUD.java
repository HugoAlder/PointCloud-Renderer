package options;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HUD extends JPanel {

	private boolean isOpen;

	private JLabel options;
	ImageIcon gearImage;
	ImageIcon crossImage;

	public HUD() {
		this.isOpen = false;
		setLayout(null);
		setFocusable(false);
		setOpaque(false);

		options = new JLabel();
		options.setBounds(20, 20, 100, 100);
		options.setFocusable(false);
		gearImage = new ImageIcon("res/gear2.png");
		crossImage = new ImageIcon("res/cross.png");

		options.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isOpen)
					closeOptionsMenu();
				else
					openOptionsMenu();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (!isOpen)
					options.setIcon(gearImage);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!isOpen)
					options.setIcon(null);
			}
		});

		add(options);

	}

	public void closeOptionsMenu() {
		isOpen = false;
		options.setIcon(gearImage);
	}

	public void openOptionsMenu() {
		options.setIcon(crossImage);
		isOpen = true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isOpen) {
			final Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(new Color(225, 225, 255, 150));
			g2d.fill(new Area(new Rectangle(new Point(0, 0), getSize())));
			g2d.dispose();
		}
	}

}
