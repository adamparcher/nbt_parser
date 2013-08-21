package ap.minecraft.chunkthing.nbt;

import static org.junit.Assert.*;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagTest {
	private static final byte byteTestData = (byte)0x89;
	private MockInputStream mis;
	
	@Before
	public void setUp() throws Exception {
		mis = new MockInputStream();
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
		byte theValue = -119;
		String theName = "Byte Tag 123";
		
		mis.add((byte) 1);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(theValue); 
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.BYTE, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(theValue, t.getNumberTagContents());
	}
	
	@Test
	public void testShortTagContents() {
		short theValue = 12451;
		String theName = "Short Tag 123";
		
		mis.add((byte)2);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(theValue);
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.SHORT, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(theValue , t.getNumberTagContents());
	}
	
	@Test
	public void testIntTagContents() {
		int theValue = 24234789;
		String theName = "Int Tag 123";
		
		mis.add((byte)3);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(theValue);
				
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.INT, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(24234789, t.getNumberTagContents());
	}
	
	@Test
	public void testLongTagContents() {
		long theLong = 0x8765432112345678l;
		String theName = "Long Tag 123";

		mis.add((byte)4);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(theLong);
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.LONG, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(theLong, t.getNumberTagContents());
	}
	
	@Test
	public void testFloatTagContents() {
		float theFloat = 1523.287890f;
		String theName = "Float Tag 123";
		
		mis.add((byte)5);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(theFloat);
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.FLOAT, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(theFloat, t.getFloatTagContents(), 0f);
	}
	
	@Test
	public void testDoubleTagContents() {
		double theDouble = 1234.6743;
		String theName = "Double Tag 123";
				
		mis.add((byte)6);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(theDouble);

		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.DOUBLE, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(theDouble, t.getDoubleTagContents(), 0);
	}
	
	@Test
	public void testByteArrayTagContents() {
		final int size = 4;
		final byte[] bytes = {(byte)0x7B, (byte)0xB2, (byte)0xFE, (byte)0xC5};
		String theName = "Byte Array Tag 123";
		
		mis.add((byte)7);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(size);
		for(Byte b : bytes) {
			mis.add(b);
		}
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.BYTE_ARRAY, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(size, t.getContentsSize());
		assertArrayEquals(bytes, t.getByteArrayContents());
	}
	
	@Test
	public void testStringTagContents() {

		final short size = 6;
		final String theString = "Eatit!";
		String theName = "String Tag 123";
		
		mis.add((byte)8);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(size);
		mis.add(theString);

		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.STRING, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(size, t.getContentsSize());
		assertEquals(theString, t.getStringContents());
	}
	
	@Test
	public void testListTagContents() {

		final byte tagType = TagType.BYTE;
		final int size = 5;
		final Tag[] contents = { new Tag(TagType.BYTE, 1), new Tag(TagType.BYTE, 1), new Tag(TagType.BYTE, 2),
				new Tag(TagType.BYTE, 3), new Tag(TagType.BYTE, 5) };
		String theName = "List Tag 123";
		
		
		mis.add((byte)9);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(tagType);
		mis.add(size);
		for(Tag t : contents){
			mis.add((byte)t.getNumberTagContents());
		}

		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.LIST, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(tagType, t.getListTagType());
		assertEquals(size, t.getContentsSize());
		assertArrayEquals(contents, t.getListContents());
	}
	
	@Test
	public void testCompoundTagContents() {

		final Tag[] contents = { new Tag("byte tag 1", TagType.BYTE).setNumberContents(1),
				new Tag("short byte 2", TagType.SHORT).setNumberContents(1),
				new Tag("string tag 3", TagType.STRING).setStringContents("my string is great"),
				new Tag("long tag 4", TagType.LONG).setNumberContents(12376823467889l)};
		String theName = "Compound Tag 123";

		mis.add((byte)10);
		mis.add((short)theName.length());
		mis.add(theName);
		for(Tag t : contents){
			mis.add((byte)t.getTagType());
			mis.add((short)t.getName().length());
			mis.add(t.getName());
			if(t.getTagType() == TagType.BYTE) {
				mis.add((byte)t.getNumberTagContents());
			} else if(t.getTagType() == TagType.SHORT){
				mis.add((short)t.getNumberTagContents());
			} else if(t.getTagType() == TagType.STRING){
				mis.add((short)t.getStringContents().length());
				mis.add(t.getStringContents());
			} else if(t.getTagType() == TagType.LONG) {
				mis.add((long)t.getNumberTagContents());
			}
		}
		mis.add((byte)0); // for the END tag

		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.COMPOUND, t.getTagType());
		assertEquals(theName, t.getName());
		assertArrayEquals(contents, t.getListContents());
	}
	
	@Test
	public void testIntArrayTagContents() {
		final int size = 4;
		final int[] ints = {19810738, 2378912, -1237987, -9748639};
		String theName = "Int Array Tag 123";
		
		mis.add((byte)11);
		mis.add((short)theName.length());
		mis.add(theName);
		mis.add(size);
		for(int i : ints) {
			mis.add(i);
		}
		
		Tag t = new Tag();
		t.readTag(mis);
		assertEquals(TagType.INT_ARRAY, t.getTagType());
		assertEquals(theName, t.getName());
		assertEquals(size, t.getContentsSize());
		assertArrayEquals(ints, t.getIntArrayContents());
	}
	

}
