package com.javaxyq.android.common.io;

import java.io.ByteArrayInputStream;

/**
 * 可跳到指定位置的ByteArrayInputStrem<br>
 * seek(int pos)
 * 
 * @author chenyang
 * 
 */
public class RandomAcessInputStream extends ByteArrayInputStream {

	public RandomAcessInputStream(byte[] buf) {
		super(buf);
	}

	public void seek(int pos) {
		if (pos < 0 || pos > this.count) {
			throw new IndexOutOfBoundsException("" + pos + ":" + this.count);
		}
		this.pos = pos;
	}

	public short readUnsignedShort() {
		int ch1 = read();
		int ch2 = read();
		return (short) ((ch2 << 8) + ch1);
	}

	public int readInt() {
		int ch1 = read();
		int ch2 = read();
		int ch3 = read();
		int ch4 = read();
		return (ch1 + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
	}
}
