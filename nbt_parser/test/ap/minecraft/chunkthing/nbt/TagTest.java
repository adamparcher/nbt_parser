package ap.minecraft.chunkthing.nbt;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagTest {
	private static final byte byteTestData = (byte)0x89;
	
	class MockInputStream extends InputStream {
		private int[] contents;
		private int i;
		public MockInputStream(int[] _contents) {
			this.contents = _contents;
			this.i = 0;
		}

		@Override
		public int read() throws IOException {
			return contents[i++];
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTagType() {
		assertEquals(TagType.END, new Tag((byte) 0).getTagType());
		assertEquals(TagType.BYTE, new Tag((byte) 1).getTagType());
		assertEquals(TagType.INT_ARRAY, new Tag((byte) 11).getTagType());
		assertEquals(TagType.FLOAT, new Tag((byte) 5).getTagType());
	}
	
	@Test
	public void testByteTagContents() {
		// Mock input stream
		int[] testData1 = {1, -119, 0, 0, 0, 0};
		MockInputStream mis = new MockInputStream(testData1);
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.BYTE, t.getTagType());
		assertEquals(-119, t.getNumberTagContents());
		
		
		

		// Mock input stream
		int[] testData2 = {1, 115, 0, 0, 0, 0};
		mis = new MockInputStream(testData2);
		
		t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.BYTE, t.getTagType());
		assertEquals(115, t.getNumberTagContents());
	}
	
	@Test
	public void testShortTagContents() {
		// Mock input stream
		int[] testData1 = {2, 0x30, 0xA3, 0, 0, 0};
		MockInputStream mis = new MockInputStream(testData1);
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.SHORT, t.getTagType());
		assertEquals(12451 , t.getNumberTagContents());
	}
	
	@Test
	public void testIntTagContents() {
		// Mock input stream
		int[] testData1 = {3, 0x01, 0x71, 0xCB, 0x25, 0, 0, 0};// int 24234789 
		MockInputStream mis = new MockInputStream(testData1);
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.INT, t.getTagType());
		assertEquals(24234789, t.getNumberTagContents());
	}
	
	@Test
	public void testLongTagContents() {
		// Mock input stream
		long theLong = 0x8765432112345678l;
		int[] testData1 = {4, 0x87, 0x65, 0x43, 0x21, 0x12, 0x34, 0x56, 0x78, 0, 0, 0}; 
		MockInputStream mis = new MockInputStream(testData1);
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.LONG, t.getTagType());
		assertEquals(theLong, t.getNumberTagContents());
	}
	
	@Test
	public void testFloatTagContents() {
		// Mock input stream
		int[] testData1 = {5, 0x44, 0xbe, 0x69, 0x36, 0, 0, 0, 0}; // this is 1523.287890 as a floating point
		MockInputStream mis = new MockInputStream(testData1);

		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.FLOAT, t.getTagType());
		assertEquals(1523.287890f, t.getFloatTagContents(), 0f);
	}
	
	@Test
	public void testDoubleTagContents() {
		// Mock input stream
		int[] testData1 = {6, 0x40, 0x93, 0x4A, 0xB2, 0x7B, 0xB2, 0xFE, 0xC5 , 0, 0, 0, 0}; // this is 1523.287890 as a floating point
		MockInputStream mis = new MockInputStream(testData1);

		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.DOUBLE, t.getTagType());
		assertEquals(1234.6743, t.getDoubleTagContents(), 0);
	}
	
	@Test
	public void testByteArrayTagContents() {
		// Mock input stream
		final int[] testData1 = {7, 0x00, 0x00, 0x00, 0x04, 0x7B, 0xB2, 0xFE, 0xC5, 0, 0, 0, 0}; 
		final int size = 4;
		final byte[] bytes = {(byte)0x7B, (byte)0xB2, (byte)0xFE, (byte)0xC5};
		MockInputStream mis = new MockInputStream(testData1);

		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.BYTE_ARRAY, t.getTagType());
		assertEquals(size, t.getContentsSize());
		assertArrayEquals(bytes, t.getByteArrayContents());
	}
	

	
	@Test
	public void testStringTagContents() {
		// Mock input stream
		final int[] testData1 = {8, 0x00, 0x06, (byte)'E', (byte)'a', (byte)'t', (byte)'i', (byte)'t', (byte)'!', 0, 0, 0}; 
		final int size = 6;
		final String theString = "Eatit!";
		MockInputStream mis = new MockInputStream(testData1);

		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.STRING, t.getTagType());
		assertEquals(size, t.getContentsSize());
		assertEquals(theString, t.getStringContents());
	}
	
	@Test
	public void testListTagContents() {
		// Mock input stream
		final int[] testData1 = {9, 0x00, 0x06, (byte)'E', (byte)'a', (byte)'t', (byte)'i', (byte)'t', (byte)'!', 0, 0, 0}; 
		final int size = 6;
		final String theString = "Eatit!";
		MockInputStream mis = new MockInputStream(testData1);

		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.STRING, t.getTagType());
		assertEquals(size, t.getContentsSize());
		assertEquals(theString, t.getStringContents());
	}
	

}
