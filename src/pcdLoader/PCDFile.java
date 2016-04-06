package pcdLoader;

import org.opencv.core.Mat;

public class PCDFile {

	protected double version;
	protected String[] fields;
	protected int[] size;
	protected char[] type;
	protected int[] count;
	protected int width;
	protected int height;
	protected int[] viewpoint;
	protected int points;
	protected Mat data;

	public PCDFile() {

	}

	public double getVersion() {
		return version;
	}

	public String[] getFields() {
		return fields;
	}

	public int[] getSize() {
		return size;
	}

	public char[] getType() {
		return type;
	}

	public int[] getCount() {
		return count;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int[] getViewpoint() {
		return viewpoint;
	}

	public int getPoints() {
		return points;
	}

	public Mat getData() {
		return data;
	}

}
