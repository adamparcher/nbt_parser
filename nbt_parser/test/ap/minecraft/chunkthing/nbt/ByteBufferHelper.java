package ap.minecraft.chunkthing.nbt;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public class ByteBufferHelper {
	public static ByteBuffer putStringToByteBuffer(ByteBuffer bb, String s) {
		if (s != null) {
			for(int i = 0; i < s.length(); i++) {
				bb.put((byte)s.charAt(i)); // Not using putChar here because we only want to put a single byte. Force the char into a byte.
			}
		}
		return bb;
	}
}
