package options;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import gl.Viewer;
import screenShot.ScreenShot;

@SuppressWarnings("serial")
public class HUD extends JPanel {

	private boolean areOptionsOpen;

	private JLabel options = new JLabel();
	private ImageIcon gearImage = new ImageIcon("res/images/gear.png");
	private ImageIcon crossImage = new ImageIcon("res/images/cross.png");
	private JLabel home = new JLabel();
	private ImageIcon homeImage = new ImageIcon("res/images/home.png");
	private JLabel screenshot = new JLabel();
	private ImageIcon screenshotImage = new ImageIcon("res/images/screenshot.png");

	private JLabel mouseOverText = new JLabel();
	private boolean optionsAlwaysShowed = true;
	private JLabel optionsShow = new JLabel();
	private JLabel colorBackground = new JLabel();
	private JLabel fullScreen = new JLabel();
	private JLabel colorChooser = new JLabel();

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

		home.setBounds(20, 140, 100, 100);
		home.setFocusable(false);
		home.setIcon(homeImage);
		home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Viewer.resetValues();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					home.setIcon(homeImage);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					home.setIcon(null);
			}
		});

		screenshot.setBounds(20, 260, 100, 100);
		screenshot.setFocusable(false);
		screenshot.setIcon(screenshotImage);
		screenshot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				options.setVisible(false);
				home.setVisible(false);
				screenshot.setVisible(false);
				try {
					ScreenShot.registerScreenShot(Viewer.frame);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
				options.setVisible(true);
				home.setVisible(true);
				screenshot.setVisible(true);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					screenshot.setIcon(screenshotImage);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					screenshot.setIcon(null);
			}
		});

		mouseOverText.setBounds(150, 0, 650, 45);
		mouseOverText.setFocusable(false);
		mouseOverText.setForeground(Viewer.whiteBackground ? Color.BLACK : Color.WHITE);
		mouseOverText.setFont(new Font("arial", 1, 20));
		mouseOverText.setHorizontalAlignment(SwingConstants.CENTER);
		mouseOverText.setVerticalAlignment(SwingConstants.CENTER);

		colorBackground = createOption(150, 45, null, "Change background color");
		colorBackground.setBackground(Viewer.whiteBackground ? Color.WHITE : Color.BLACK);
		colorBackground.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Viewer.whiteBackground = !Viewer.whiteBackground;
				colorBackground.setBackground(Viewer.whiteBackground ? Color.WHITE : Color.BLACK);
				mouseOverText.setForeground(Viewer.whiteBackground ? Color.BLACK : Color.WHITE);
				for (Component c : getComponents()) {
					if (c != options && c != mouseOverText && c != home && c != screenshot)
						((JComponent) c).setBorder(
								BorderFactory.createLineBorder(Viewer.whiteBackground ? Color.BLACK : Color.WHITE, 4));
				}
			}

		});

		optionsShow = createOption(225, 45, optionsAlwaysShowed, "Always show options button");
		optionsShow.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				optionsAlwaysShowed = !optionsAlwaysShowed;
				optionsShow.setBackground(optionsAlwaysShowed ? Color.GREEN : Color.GRAY);
			}

		});

		fullScreen = createOption(300, 45, Viewer.frame.isUndecorated(), "Activate fullscreen");
		fullScreen.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (Viewer.frame.isUndecorated())
					Viewer.setWindowed();
				else
					Viewer.setFullScreen();
				fullScreen.setBackground(Viewer.frame.isUndecorated() ? Color.GREEN : Color.GRAY);
				mouseOverText.setVisible(false);
			}

		});

		colorChooser = createOption(375, 45, null, "Change points color");
		colorChooser.setBackground(new Color(28, 138, 219));
		colorChooser.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!Viewer.isColorFrameOpen) {
					ColorChanger colorFrame = new ColorChanger();
					colorFrame.addWindowListener(new WindowAdapter() {

						@Override
						public void windowClosing(WindowEvent e) {
							colorChooser.setBackground(Viewer.forcedColor);
						}
					});
				}
			}

		});

		add(options);
		add(home);
		add(screenshot);
		add(colorBackground);
		add(optionsShow);
		add(fullScreen);
		add(colorChooser);
		add(mouseOverText);

		closeOptionsMenu();
	}

	public JLabel createOption(int x, int y, Boolean value, String description) {
		JLabel res = new JLabel();
		res.setBounds(x, y, 50, 50);
		res.setFocusable(false);
		res.setOpaque(true);
		if (value != null)
			res.setBackground(value ? Color.GREEN : Color.GRAY);
		res.setBorder(BorderFactory.createLineBorder(Viewer.whiteBackground ? Color.BLACK : Color.WHITE, 4));

		res.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				mouseOverText.setText(description);
				mouseOverText.setVisible(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseOverText.setVisible(false);
			}

		});

		return res;
	}

	public void closeOptionsMenu() {
		areOptionsOpen = false;
		options.setIcon(gearImage);

		for (Component c : getComponents()) {
			if (c != options)
				c.setVisible(false);
		}
		home.setVisible(true);
		screenshot.setVisible(true);
		if (optionsAlwaysShowed) {
			home.setIcon(homeImage);
			screenshot.setIcon(screenshotImage);
		} else {
			home.setIcon(null);
			screenshot.setIcon(null);
		}

	}

	public void openOptionsMenu() {
		areOptionsOpen = true;
		options.setIcon(crossImage);
		for (Component c : getComponents()) {
			if (c != options)
				c.setVisible(true);
		}
		home.setVisible(false);
		screenshot.setVisible(false);
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
