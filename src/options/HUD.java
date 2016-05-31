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
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import convert.Converter;
import gl.Viewer;
import pcdLoader.Finder;
import pcdLoader.PCDFile;
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
	private JLabel searchFile = new JLabel();
	private ImageIcon searchImage = new ImageIcon("res/images/search.png");
	private JLabel convert = new JLabel();
	private ImageIcon convertImage = new ImageIcon("res/images/convert.png");

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

		options = createMenu(20, 20, gearImage);
		options.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (areOptionsOpen)
					closeOptionsMenu();
				else
					openOptionsMenu();
			}
		});

		home = createMenu(20, 140, homeImage);
		home.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Viewer.resetValues();
			}
		});

		screenshot = createMenu(20, 260, screenshotImage);
		screenshot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				hideHUD();
				try {
					ScreenShot.registerScreenShot(Viewer.frame);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
				closeOptionsMenu();
			}
		});

		searchFile = createMenu(20, 380, searchImage);
		searchFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Finder.setLookAndFeel();
				PCDFile file;
				try {
					file = Finder.findFile();
					if (file != null)
						Viewer.changeFile(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		convert = createMenu(20, 500, convertImage);
		convert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Finder.setLookAndFeel();
				PCDFile file;
				try {
					file = Finder.findFile();
					if (file != null)
						Converter.openSizeSelector(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
					if (c != options && c != mouseOverText && c != home && c != screenshot && c != searchFile)
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
		add(searchFile);
		add(convert);
		add(colorBackground);
		add(optionsShow);
		add(fullScreen);
		add(colorChooser);
		add(mouseOverText);

		closeOptionsMenu();
	}

	public JLabel createMenu(int x, int y, ImageIcon image) {
		JLabel res = new JLabel();
		res.setBounds(x, y, 100, 100);
		res.setFocusable(false);
		res.setIcon(image);
		res.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					res.setIcon(image);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					res.setIcon(null);
			}
		});

		return res;
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
				mouseOverText.setText("");
				mouseOverText.setVisible(false);
			}

		});

		return res;
	}

	public void hideHUD() {
		home.setIcon(null);
		screenshot.setIcon(null);
		searchFile.setIcon(null);
		convert.setIcon(null);
	}

	public void closeOptionsMenu() {
		areOptionsOpen = false;
		options.setIcon(gearImage);

		for (Component c : getComponents()) {
			c.setVisible(false);
		}
		options.setVisible(true);
		home.setVisible(true);
		screenshot.setVisible(true);
		searchFile.setVisible(true);
		convert.setVisible(true);
		if (optionsAlwaysShowed) {
			home.setIcon(homeImage);
			screenshot.setIcon(screenshotImage);
			searchFile.setIcon(searchImage);
			convert.setIcon(convertImage);
		} else {
			home.setIcon(null);
			screenshot.setIcon(null);
			searchFile.setIcon(null);
			convert.setIcon(null);
		}

	}

	public void openOptionsMenu() {
		areOptionsOpen = true;
		options.setIcon(crossImage);
		for (Component c : getComponents()) {
			c.setVisible(true);
		}
		home.setVisible(false);
		screenshot.setVisible(false);
		searchFile.setVisible(false);
		convert.setVisible(false);
		mouseOverText.setVisible(false);
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
