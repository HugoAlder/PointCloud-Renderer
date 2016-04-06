package tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pcdLoader.PCDFile;
import pcdLoader.Reader;

public class ReaderTest {

	private PCDFile file = Reader.readFile("res/test.pcd");

	@Test
	public void versionTest() {
		assertTrue(file.getVersion() == 0.7);
	}

	@Test
	public void fieldsTest() {
		String[] expected = new String[] { "x", "y", "z", "rgb" };
		assertArrayEquals(expected, file.getFields());
	}

	@Test
	public void sizeTest() {
		int[] expected = new int[] { 4, 4, 4, 4 };
		assertArrayEquals(expected, file.getSize());
	}

	@Test
	public void typeTest() {
		char[] expected = new char[] { 'F', 'F', 'F', 'F' };
		assertArrayEquals(expected, file.getType());
	}

	@Test
	public void countTest() {
		int[] expected = new int[] { 1, 1, 1, 1 };
		assertArrayEquals(expected, file.getCount());
	}

	@Test
	public void widthTest() {
		assertEquals(213, file.getWidth());
	}

	@Test
	public void heightTest() {
		assertEquals(1, file.getHeight());
	}

	@Test
	public void viewpointTest() {
		int[] expected = new int[] { 0, 0, 0, 1, 0, 0, 0 };
		assertArrayEquals(expected, file.getViewpoint());
	}

	@Test
	public void pointsTest() {
		assertEquals(213, file.getPoints());
	}

}
