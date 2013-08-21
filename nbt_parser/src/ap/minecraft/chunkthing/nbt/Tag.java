package ap.minecraft.chunkthing.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Operators;

public class Tag {
	private int type;
	private long numberContents;
	private float floatContents;
	private double doubleContents;
	private int size;
	private byte[] byteContents;
	private String stringContents;
	private Tag[] listContents;
	private byte listTagType;
	private String name;
	private int[] intContents;

	public Tag(byte b) {
		this.type = b;
	}

	public Tag() {
		// TODO Auto-generated constructor stub
	}

	public Tag(int _type, int _contents) {
		this.type = _type;
		this.numberContents = _contents;
	}

	public Tag(String _name, int _type) {
		this.name = _name;
		this.type = _type;
	}

	public int getTagType() {
		return this.type;
	}

	public void readTag(InputStream is) {
		byte[] bytes;
		try {
			this.type = is.read();
			this.name = this.readStringContents(is);
			this.readTagContents(is);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readTagContents(InputStream is) throws IOException {
		byte[] bytes;
		switch (this.type) {
		case TagType.END:
			break; // do nothing
		case TagType.BYTE:
			this.numberContents = (byte) is.read();
			break;
		case TagType.SHORT:
			bytes = new byte[2];
			is.read(bytes);
			this.numberContents = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
			break;
		case TagType.INT:
			bytes = new byte[4];
			is.read(bytes);
			this.numberContents = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
			break;
		case TagType.LONG:
			bytes = new byte[8];
			is.read(bytes);
			this.numberContents = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getLong();
			break;
		case TagType.FLOAT:
			bytes = new byte[4];
			is.read(bytes);
			this.floatContents = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getFloat();
			break;
		case TagType.DOUBLE:
			bytes = new byte[8];
			is.read(bytes);
			this.doubleContents = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getDouble();
			break;

		case TagType.BYTE_ARRAY:
			bytes = new byte[4];
			is.read(bytes);
			this.size = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
			this.byteContents = new byte[size];
			is.read(this.byteContents);
			break;

		case TagType.STRING:
			this.stringContents = this.readStringContents(is);
			break;

		case TagType.LIST:
			this.listTagType = (byte) is.read();
			bytes = new byte[4];
			is.read(bytes);
			this.size = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
			this.listContents = new Tag[this.size];
			for (int i = 0; i < this.size; i++) {
				this.listContents[i] = new Tag(this.listTagType);
				this.listContents[i].readTagContents(is);
			}

			break;

		case TagType.COMPOUND:
			List<Tag> list = new ArrayList<Tag>();
			Tag t; 
			do {
				t = new Tag();
				t.readTag(is);
				if(t.type != TagType.END) 
					list.add(t);
			} while (t.type != TagType.END);
			
			this.listContents = new Tag[list.size()];
			this.listContents = list.toArray(this.listContents);

			break;

		case TagType.INT_ARRAY:
			bytes = new byte[4];
			is.read(bytes);
			this.size = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
			this.intContents = new int[size];
			for(int i = 0; i < size; i++) {
				bytes = new byte[4];
				is.read(bytes);
				this.intContents[i] = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getInt();
			}
			break;
		default:
			break;
		}
	}

	private String readStringContents(InputStream is) throws IOException {
		byte[] bytes;
		bytes = new byte[2];
		is.read(bytes);
		this.size = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
		bytes = new byte[size];
		is.read(bytes);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < this.size; i++) {
			sb.append((char) bytes[i]);
		}
		return sb.toString();
	}

	public long getNumberTagContents() {
		return this.numberContents;
	}

	public float getFloatTagContents() {
		return this.floatContents;
	}

	public double getDoubleTagContents() {
		return this.doubleContents;
	}

	public int getContentsSize() {
		// TODO Auto-generated method stub
		return this.size;
	}

	public byte[] getByteArrayContents() {
		return this.byteContents;
	}

	public String getStringContents() {
		return this.stringContents;
	}

	public int getListTagType() {
		return this.listTagType;
	}

	public Tag[] getListContents() {
		return this.listContents;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tag) {
			Tag t = (Tag) obj;
			// Special section to check for null name
			boolean nameMatches = (this.name == t.name || 
					(this.name != null && this.name.equals(t.name)));
			if(!nameMatches) return false;
					

			if (this.type == t.type) {
				switch (this.type) {
				case TagType.BYTE:
				case TagType.SHORT:
				case TagType.INT:
				case TagType.LONG:
					return this.numberContents == t.numberContents;
				case TagType.FLOAT:
					return this.floatContents == t.floatContents;

				case TagType.DOUBLE:
					return this.doubleContents == t.doubleContents;
				case TagType.BYTE_ARRAY:
					return this.byteContents == t.byteContents;
				case TagType.STRING:
					return this.stringContents == t.stringContents || (this.stringContents != null && this.stringContents.equals(t.stringContents));
				case TagType.LIST:
					boolean match = true;
					if (this.listContents == t.listContents)
						return true;

					if (this.listTagType == t.listTagType) {
						if (this.listContents != null) {
							if (t.listContents != null) {
								if (this.listContents.length == t.listContents.length) {
									for (int i = 0; i < this.listContents.length; i++) {
										match = match && (this.listContents[i].equals(t.listContents[i]));
									}
								}
							}
						}
					}

				case TagType.COMPOUND:
					break;
				case TagType.INT_ARRAY:
					break;
				default:
					return false;
				}
			}
		}

		return false;
	}

	/**
	 * Returns the name of the Tag after reading in from the input.
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public Tag setNumberContents(long i) {
		this.numberContents = i;
		return this;
	}

	public Tag setStringContents(String string) {
		this.stringContents = string;
		return this;
	}

	public int[] getIntArrayContents() {
		return this.intContents;
	}

}
