package ap.minecraft.chunkthing.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

class MockInputStream extends InputStream {
	private static int DEFAULT_CONTENTS_SIZE = 128;

	private int[] contents;
	private int i;
	private int loadIndex;

	public MockInputStream(int[] _contents) {
		this.contents = _contents;

		for (int i = 0; i < _contents.length; i++) { // TODO: Refactor, this
														// constructor is ugly
			this.contents[i] = (byte) _contents[i];
		}
		this.i = 0;
	}

	public MockInputStream(byte[] _contents) {
		this.contents = new int[_contents.length];
		for (int i = 0; i < _contents.length; i++) { // TODO: Refactor, this
			// constructor is ugly
			this.contents[i] = (byte) _contents[i];
			}
		this.i = 0;
	}

	public MockInputStream() {
		this.i = 0;
		this.contents = new int[DEFAULT_CONTENTS_SIZE];
		this.loadIndex = 0;
	}

	@Override
	public int read() throws IOException {
		int retval = contents[i++]; 
		if(retval < 0) { 
			retval += 256; // if the value is < 0, that means it rolled over to negative, and we actually want it to come back around to positive
		} 
		return retval;
	}

	/**
	 * This method adds the byte to the contents array and increments the
	 * loading position by 1.
	 * 
	 * @param j
	 */
	public void add(byte j) {
		this.contents[loadIndex] = j;
		loadIndex++;
	}

	/**
	 * This method adds the short to the contents array and increments the
	 * loading position by 2.
	 * 
	 * @param theValue
	 */
	public void add(short s) {
		ByteBuffer bb = ByteBuffer.allocate(64);
		bb.putShort(s);
		bb.rewind();
		for (int i = 0; i < 2; i++) {
			this.contents[loadIndex] = bb.get();
			loadIndex++;
		}

	}

	/**
	 * This method adds the short to the contents array and increments the
	 * loading position by 4.
	 * 
	 * @param theValue
	 */
	public void add(int i) {
		ByteBuffer bb = ByteBuffer.allocate(64);
		bb.putInt(i);
		bb.rewind();
		for (int j = 0; j < 4; j++) {
			this.contents[loadIndex] = bb.get();
			loadIndex++;
		}
	}

	/**
	 * This method adds the long to the contents array and increments the
	 * loading position by 8.
	 * 
	 * @param theLong
	 */
	public void add(long theLong) {
		ByteBuffer bb = ByteBuffer.allocate(64);
		bb.putLong(theLong);
		bb.rewind();
		for (int j = 0; j < 8; j++) {
			this.contents[loadIndex] = bb.get();
			loadIndex++;
		}
	}

	/** 
	 * This method adds the float to the contents array and increments the loading position by 4.
	 * 
	 * @param theFloat
	 */
	public void add(float theFloat) {
		ByteBuffer bb = ByteBuffer.allocate(64);
		bb.putFloat(theFloat);
		bb.rewind();
		for (int j = 0; j < 4; j++) {
			this.contents[loadIndex] = bb.get();
			loadIndex++;
		}
	}

	/** 
	 * This method adds the double to the contents array and increments the loading position by 8.
	 * @param theDouble
	 */
	public void add(double theDouble) {

		ByteBuffer bb = ByteBuffer.allocate(64);
		bb.putDouble(theDouble);
		bb.rewind();
		for (int j = 0; j < 8; j++) {
			this.contents[loadIndex] = bb.get();
			loadIndex++;
		}
	}

	/**
	 * This method adds the String, one char at a time, to the contents array, and increments the loading position by the length of the string.
	 * @param theString
	 */
	public void add(String theString) {
		if(theString == null) return;
		
		for(char c : theString.toCharArray()) {
			this.add((byte)c);
		}
	}
}