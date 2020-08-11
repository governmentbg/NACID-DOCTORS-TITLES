package com.nacid.data.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BinaryStream {
	InputStream inputStream;
	int streamSize;
	public BinaryStream(InputStream inputStream, int fileSize) {
		this.inputStream = inputStream;
		this.streamSize = fileSize;
	}
	public boolean isEmpty() {
		return getInputStream() == null;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public int getStreamSize() {
		return streamSize;
	}
	public void setStreamSize(int fileSize) {
		this.streamSize = fileSize;
	}
	
	public static BinaryStream createBinaryStreamFromByteArray(byte[] arr) {
	    InputStream is = new ByteArrayInputStream(arr);
	    return new BinaryStream(is , arr.length);
	}
	
}
