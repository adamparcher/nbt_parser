package ap.minecraft.chunkthing.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Tag {
	private int type;
	private long numberContents;
	private float floatContents;
	private double doubleContents;
	private int size;
	private byte[] byteContents;
	private String stringContents;

	public Tag(byte b) {
		this.type = b;
	}

	public Tag() {
		// TODO Auto-generated constructor stub
	}

	public int getTagType() {
		return this.type;
	}

	public void readTag(InputStream is) {
		byte[] bytes;
		try {
			this.type = is.read();
			switch (this.type) {
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
				bytes = new byte[2];
				is.read(bytes);
				this.size = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).getShort();
				bytes = new byte[size];
				is.read(bytes);
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i < this.size; i++) {
					sb.append((char)bytes[i]);
				}
				this.stringContents = sb.toString();
				break;
			default:
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} // read one byte
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

}
