package com.github.yqy7.yutool;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LittleEndianDataOutputStream extends FilterOutputStream implements DataOutput {
    private DataOutputStream dataOutputStream;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);

    public LittleEndianDataOutputStream(OutputStream out) {
        super(out);
        dataOutputStream = new DataOutputStream(out);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        dataOutputStream.writeBoolean(v);
    }

    @Override
    public void writeByte(int v) throws IOException {
        dataOutputStream.writeByte(v);
    }

    @Override
    public void writeShort(int v) throws IOException {
        dataOutputStream.write((v >>> 0) & 0xFF);
        dataOutputStream.write((v >>> 8) & 0xFF);
    }

    @Override
    public void writeChar(int v) throws IOException {
        writeShort(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        byteBuffer.putInt(v);
        dataOutputStream.write(byteBuffer.array(),0,Integer.BYTES);
        byteBuffer.rewind();
    }

    @Override
    public void writeLong(long v) throws IOException {
        byteBuffer.putLong(v);
        dataOutputStream.write(byteBuffer.array(),0,Long.BYTES);
        byteBuffer.rewind();
    }

    @Override
    public void writeFloat(float v) throws IOException {
        byteBuffer.putFloat(v);
        dataOutputStream.write(byteBuffer.array(), 0,Float.BYTES);
        byteBuffer.rewind();
    }

    @Override
    public void writeDouble(double v) throws IOException {
        byteBuffer.putDouble(v);
        dataOutputStream.write(byteBuffer.array(),0, Double.BYTES);
        byteBuffer.rewind();
    }

    @Override
    public void writeBytes(String s) throws IOException {
        dataOutputStream.writeBytes(s);
    }

    @Override
    public void writeChars(String s) throws IOException {
        for (int i = 0; i < s.length(); i++) {
            writeChar(s.charAt(i));
        }
    }

    @Override
    public void writeUTF(String s) throws IOException {
        dataOutputStream.writeUTF(s);
    }
}
