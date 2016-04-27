package pcdLoader;

import java.io.File;

import org.opencv.core.Mat;

@SuppressWarnings("serial")
public class PCDFile extends File {

	public PCDFile(String pathname) {
		super(pathname);
	}

	protected double version;
	protected String[] fields;
	protected boolean colored;
	protected int[] size;
	protected char[] type;
	protected int[] count;
	protected int width;
	protected int height;
	protected int[] viewpoint;
	protected int points;
	protected String datatype;
	protected Mat data;
	
	public double getVersion() {
		return version;
	}

	public String[] getFields() {
		return fields;
	}
	
	public boolean isColored() {
		return colored;
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
	
	public String getDatatype() {
		return datatype;
	}

	public Mat getData() {
		return data;
	}

}