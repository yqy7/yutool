package com.github.yqy7.yutool;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LittleEndianDataInputStream extends FilterInputStream implements DataInput {

    private DataInputStream dataInputStream;
    public LittleEndianDataInputStream(InputStream in) {
        super(in);
        dataInputStream = new DataInputStream(in);
    }

    private ByteBuffer buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);

    @Override
    public void readFully(byte[] b) throws IOException {
        dataInputStream.readFully(b);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        dataInputStream.readFully(b,off,len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return dataInputStream.skipBytes(n);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return dataInputStream.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return dataInputStream.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return dataInputStream.readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        return (short)readUnsignedShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch2 << 8) + (ch1 << 0);
    }

    @Override
    public char readChar() throws IOException {
        return (char)readUnsignedShort();
    }

    @Override
    public int readInt() throws IOException {
        int count = dataInputStream.read(buffer.array(), 0, Integer.BYTES);
        if (count < Integer.BYTES) {
            throw new EOFException();
        }

        int r = buffer.getInt();
        buffer.rewind();
        return r;
    }

    @Override
    public long readLong() throws IOException {
        int count = dataInputStream.read(buffer.array(), 0, Long.BYTES);
        if (count < Long.BYTES) {
            throw new EOFException();
        }

        long r = buffer.getLong();
        buffer.rewind();
        return r;
    }

    @Override
    public float readFloat() throws IOException {
        int count = dataInputStream.read(buffer.array(), 0, Float.BYTES);
        if (count < Float.BYTES) {
            throw new EOFException();
        }

        float r = buffer.getFloat();
        buffer.rewind();
        return r;
    }

    @Override
    public double readDouble() throws IOException {
        int count = dataInputStream.read(buffer.array(), 0, Double.BYTES);
        if (count < Double.BYTES) {
            throw new EOFException();
        }

        double r = buffer.getDouble();
        buffer.rewind();
        return r;
    }

    @Override
    public String readLine() throws IOException {
        return dataInputStream.readLine();
    }

    @Override
    public String readUTF() throws IOException {
        return dataInputStream.readUTF();
    }
}
