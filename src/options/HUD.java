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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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

	private HashMap<String, JLabel> buttons = new HashMap<String, JLabel>();
	private HashMap<String, ImageIcon> images = new HashMap<String, ImageIcon>();

	private JLabel mouseOverText = new JLabel();
	private boolean optionsAlwaysShowed = true;
	private JLabel optionsShow = new JLabel();
	private JLabel colorBackground = new JLabel();
	private JLabel fullScreen = new JLabel();
	private JLabel colorChooser = new JLabel();

	public HUD() {

		File f = new File("res/images/");
		for (String s : f.list())
			images.put(s.substring(0, s.length() - 4), new ImageIcon("res/images/" + s));

		this.areOptionsOpen = false;
		setLayout(null);
		setFocusable(false);
		setOpaque(false);

		createButton("options", "gear");
		buttons.get("options").addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (areOptionsOpen)
					closeOptionsMenu();
				else
					openOptionsMenu();
			}
		});

		createButton("home");
		buttons.get("home").addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Viewer.resetValues();
			}
		});

		createButton("screenshot");
		buttons.get("screenshot").addMouseListener(new MouseAdapter() {
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

		createButton("search");
		buttons.get("search").addMouseListener(new MouseAdapter() {
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

		createButton("convert");
		buttons.get("convert").addMouseListener(new MouseAdapter() {
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

		createButton("location");
		buttons.get("location").addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

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
					if (!buttons.containsValue(c) && c != mouseOverText)
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

		for (String key : buttons.keySet())
			add(buttons.get(key));
		add(colorBackground);
		add(optionsShow);
		add(fullScreen);
		add(colorChooser);
		add(mouseOverText);

		closeOptionsMenu();
	}

	public void createButton(String name) {
		createButton(name, name);
	}

	public void createButton(String name, String image) {
		JLabel res = new JLabel();

		int y = 20;
		y += 120 * buttons.size();

		res.setBounds(20, y, 100, 100);
		res.setFocusable(false);
		res.setIcon(images.get(image));
		res.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					res.setIcon(images.get(image));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (!optionsAlwaysShowed && !areOptionsOpen)
					res.setIcon(null);
			}
		});

		buttons.put(name, res);
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
		for (String key : buttons.keySet())
			if (key != "options")
				buttons.get(key).setIcon(null);
	}

	public void closeOptionsMenu() {
		areOptionsOpen = false;
		buttons.get("options").setIcon(images.get("gear"));

		for (Component c : getComponents()) {
			c.setVisible(false);
		}
		for (String key : buttons.keySet())
			buttons.get(key).setVisible(true);

		if (optionsAlwaysShowed) {
			for (String key : buttons.keySet())
				if (key != "options")
					buttons.get(key).setIcon(images.get(key));
		} else {
			for (String key : buttons.keySet())
				if (key != "options")
					buttons.get(key).setIcon(null);
		}

	}

	public void openOptionsMenu() {
		areOptionsOpen = true;
		buttons.get("options").setIcon(images.get("cross"));
		for (Component c : getComponents()) {
			c.setVisible(true);
		}
		for (String key : buttons.keySet())
			if (key != "options")
				buttons.get(key).setVisible(false);
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
