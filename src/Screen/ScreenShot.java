package Screen;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import gl.Viewer;

public class ScreenShot {
	int WIDTH = Viewer.WIDTH;
	int HEIGHT = Viewer.HEIGHT;
	GL2 GL11 = Viewer.GLD;
	
 public ScreenShot(){
	 
 }
	
	public static BufferedImage getScreenShot(Component component) {

	BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		// call the Component's paint method, using
		// the Graphics object of the image.
		component.paintAll(image.getGraphics()); // alternately use .printAll(..)
		return image;
	}

	/*public void registerScreenShot(JFrame frame) {
		BufferedImage img = getScreenShot(frame.getContentPane());
		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(
				img.getScaledInstance(img.getWidth(null) / 2, img.getHeight(null) / 2, Image.SCALE_SMOOTH))));
		try {
			// write the image as a PNG
			ImageIO.write(img, "png", new File("screenshot.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
}*/
 
 public void saveScreenshot() {
	    // read current buffer
	    FloatBuffer imageData = Buffers.newDirectFloatBuffer(WIDTH * HEIGHT * 3); 
	    GL11.glReadPixels(
	        0, 0, WIDTH, HEIGHT, GL11.GL_RGB, GL11.GL_FLOAT, imageData
	    );
	    imageData.rewind();

	    // fill rgbArray for BufferedImage
	    int[] rgbArray = new int[WIDTH * HEIGHT];
	    for(int y = 0; y < HEIGHT; ++y) {
	        for(int x = 0; x < WIDTH; ++x) {
	            int r = (int)(imageData.get() * 255) << 16;
	            int g = (int)(imageData.get() * 255) << 8;
	            int b = (int)(imageData.get() * 255);
	            int i = ((HEIGHT - 1) - y) * WIDTH + x;
	            rgbArray[i] = r + g + b;
	        }
	    }

	    // create and save image
	    BufferedImage image = new BufferedImage(
	         WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB
	    );
	    image.setRGB(0, 0, WIDTH, HEIGHT, rgbArray, 0, WIDTH);
	    File outputfile = getNextScreenFile();
	    
	  image = getScreenShot(Viewer.frame.getContentPane());
		JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(
				image.getScaledInstance(image.getWidth(null) / 2, image.getHeight(null) / 2, Image.SCALE_SMOOTH))));
	    
	    try {
	        ImageIO.write(image, "png", outputfile);
	       // ImageIO.write(image, "png", new File("screenshot.png"));
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.err.println("Can not save screenshot!");
	    }
	}

	private File getNextScreenFile() {
	    // create image name
	    String fileName = "screenshot_" + getSystemTime(false);
	    File imageToSave = new File(fileName + ".png");

	    // check for duplicates
	    int duplicate = 0;
	    while(imageToSave.exists()) {
	        imageToSave = new File(fileName + "_" + ++duplicate + ".png");
	    }

	    return imageToSave;
	}

	// format the time
	public static String getSystemTime(boolean getTimeOnly) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat(
	      getTimeOnly?"HH-mm-ss":"yyyy-MM-dd'T'HH-mm-ss"
	    );
	    return dateFormat.format(new Date());
	}
}
