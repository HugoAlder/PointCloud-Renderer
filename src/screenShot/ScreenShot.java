package screenShot;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;

public class ScreenShot {

	public static BufferedImage getScreenShot(Component component) {
		BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		component.paintAll(image.getGraphics());
		return image;
	}

	public static void registerScreenShot(JFrame frame) throws AWTException {

		File outputfile = getNextScreenFile();

		Robot bot = new Robot();
		JRootPane rootPane = frame.getRootPane();
		Rectangle bounds = new Rectangle(rootPane.getSize());
		bounds.setLocation(rootPane.getLocationOnScreen());
		BufferedImage img = bot.createScreenCapture(bounds);

		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(
				img.getScaledInstance(img.getWidth(null) / 2, img.getHeight(null) / 2, Image.SCALE_SMOOTH))));
		try {
			ImageIO.write(img, "png", outputfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File getNextScreenFile() {

		String fileName = "screenshot_" + getSystemTime(false);
		File imageToSave = new File(fileName + ".png");
		int duplicate = 0;
		while (imageToSave.exists()) {
			imageToSave = new File(fileName + "_" + ++duplicate + ".png");
		}

		return imageToSave;
	}

	public static String getSystemTime(boolean getTimeOnly) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(getTimeOnly ? "HH-mm-ss" : "yyyy-MM-dd'T'HH-mm-ss");
		return dateFormat.format(new Date());
	}
}
