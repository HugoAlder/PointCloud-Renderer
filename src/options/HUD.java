package options;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import gl.Viewer;

@SuppressWarnings("serial")
public class HUD extends JPanel {

	private boolean areOptionsOpen;

	private JLabel options = new JLabel();
	private ImageIcon gearImage = new ImageIcon("res/gear2.png");
	private ImageIcon crossImage = new ImageIcon("res/cross.png");

	private boolean optionsAlwaysShowed = false;
	private JLabel optionsShow = new JLabel();
	private JLabel colorBackground = new JLabel();

	public HUD() {
		this.areOptionsOpen = false;
		setLayout(null);
		setFocusable(false);
		setOpaque(false);

		options.setBounds(20, 20, 100, 100);
		options.setFocusable(false);
		options.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (areOptionsOpen)
					closeOptionsMenu();
				else
					openOptionsMenu();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					options.setIcon(gearImage);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					options.setIcon(null);
			}
		});

		colorBackground.setBounds(150, 45, 50, 50);
		colorBackground.setFocusable(false);
		colorBackground.setOpaque(true);
		colorBackground.setBackground(Viewer.whiteBackground ? Color.WHITE : Color.BLACK);
		colorBackground
				.setBorder(BorderFactory.createLineBorder(Viewer.whiteBackground ? Color.BLACK : Color.WHITE, 4));
		colorBackground.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Viewer.whiteBackground = !Viewer.whiteBackground;
				colorBackground.setBackground(Viewer.whiteBackground ? Color.WHITE : Color.BLACK);
				for (Component c : getComponents()) {
					if (c != options)
						((JComponent) c).setBorder(
								BorderFactory.createLineBorder(Viewer.whiteBackground ? Color.BLACK : Color.WHITE, 4));
				}
			}

		});

		optionsShow.setBounds(225, 45, 50, 50);
		optionsShow.setFocusable(false);
		optionsShow.setOpaque(true);
		optionsShow.setBackground(optionsAlwaysShowed ? Color.GREEN : Color.GRAY);
		optionsShow.setBorder(BorderFactory.createLineBorder(Viewer.whiteBackground ? Color.BLACK : Color.WHITE, 4));
		optionsShow.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				optionsAlwaysShowed = !optionsAlwaysShowed;
				optionsShow.setBackground(optionsAlwaysShowed ? Color.GREEN : Color.GRAY);
			}

		});

		add(options);
		add(colorBackground);
		add(optionsShow);

		closeOptionsMenu();
	}

	public void closeOptionsMenu() {
		areOptionsOpen = false;
		options.setIcon(gearImage);
		for (Component c : getComponents()) {
			if (c != options)
				c.setVisible(false);
		}
	}

	public void openOptionsMenu() {
		areOptionsOpen = true;
		options.setIcon(crossImage);
		for (Component c : getComponents()) {
			if (c != options)
				c.setVisible(true);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (areOptionsOpen) {
			final Graphics2D g2d = (Graphics2D) g.create();
			if (Viewer.whiteBackground)
				g2d.setColor(new Color(130, 130, 155, 150));
			else
				g2d.setColor(new Color(100, 100, 125, 150));
			g2d.fill(new Area(new Rectangle(new Point(0, 0), getSize())));
			g2d.dispose();
		}
	}

}
