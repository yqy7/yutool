package com.github.yqy7.yutool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLittleEndianDataOutputStream {
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private LittleEndianDataOutputStream out = new LittleEndianDataOutputStream(baos);

    @Test
    public void testWriteLittleEndian() throws IOException {

        /* Write out various test values in LITTLE ENDIAN FORMAT */
        out.write(new byte[] {-100, 100});
        out.writeBoolean(true);
        out.writeBoolean(false);
        out.writeByte(100);
        out.writeByte(-100);
        out.writeByte((byte) 200);
        out.writeChar('a');
        out.writeShort((short) -30000);
        out.writeShort((short) 50000);
        out.writeInt(0xCAFEBABE);
        out.writeLong(0xDEADBEEFCAFEBABEL);
        out.writeUTF("Herby Derby");
        out.writeFloat(Float.intBitsToFloat(0xCAFEBABE));
        out.writeDouble(Double.longBitsToDouble(0xDEADBEEFCAFEBABEL));

        byte[] data = baos.toByteArray();

        /* Setup input streams */
        DataInput in = new DataInputStream(new ByteArrayInputStream(data));

        /* Read in various values NORMALLY */
        byte[] b = new byte[2];
        in.readFully(b);
        assertEquals(-100, b[0]);
        assertEquals(100, b[1]);
        assertEquals(true, in.readBoolean());
        assertEquals(false, in.readBoolean());
        assertEquals(100, in.readByte());
        assertEquals(-100, in.readByte());
        assertEquals(200, in.readUnsignedByte());
        assertEquals('\u6100', in.readChar());
        assertEquals(-12150, in.readShort());
        assertEquals(20675, in.readUnsignedShort());
        assertEquals(0xBEBAFECA, in.readInt());
        assertEquals(0xBEBAFECAEFBEADDEL, in.readLong());
        assertEquals("Herby Derby", in.readUTF());
        assertEquals(0xBEBAFECA, Float.floatToIntBits(in.readFloat()));
        assertEquals(0xBEBAFECAEFBEADDEL, Double.doubleToLongBits(in.readDouble()));
    }

    @Test
    public void testWriteBytes() throws IOException {

        /* Write out various test values in LITTLE ENDIAN FORMAT */
        out.writeBytes("r\u00C9sum\u00C9");

        byte[] data = baos.toByteArray();

        /* Setup input streams */
        DataInput in = new DataInputStream(new ByteArrayInputStream(data));

        /* Read in various values NORMALLY */
        byte[] b = new byte[6];
        in.readFully(b);
        assertBytesEquals("r\u00C9sum\u00C9".getBytes(Charsets.ISO_8859_1), b);
    }

    @Test
    public void testWriteBytes_discardHighOrderBytes() throws IOException {

        /* Write out various test values in LITTLE ENDIAN FORMAT */
        out.writeBytes("\uAAAA\uAABB\uAACC");

        byte[] data = baos.toByteArray();

        /* Setup input streams */
        DataInput in = new DataInputStream(new ByteArrayInputStream(data));

        /* Read in various values NORMALLY */
        byte[] b = new byte[3];
        in.readFully(b);
        byte[] expected = {(byte) 0xAA, (byte) 0xBB, (byte) 0xCC};
        assertBytesEquals(expected, b);
    }

    @Test
    public void testWriteChars() throws IOException {

        /* Write out various test values in LITTLE ENDIAN FORMAT */
        out.writeChars("r\u00C9sum\u00C9");

        byte[] data = baos.toByteArray();

        /* Setup input streams */
        DataInput in = new DataInputStream(new ByteArrayInputStream(data));

        /* Read in various values NORMALLY */
        byte[] actual = new byte[12];
        in.readFully(actual);
        assertEquals('r', actual[0]);
        assertEquals(0, actual[1]);
        assertEquals((byte) 0xC9, actual[2]);
        assertEquals(0, actual[3]);
        assertEquals('s', actual[4]);
        assertEquals(0, actual[5]);
        assertEquals('u', actual[6]);
        assertEquals(0, actual[7]);
        assertEquals('m', actual[8]);
        assertEquals(0, actual[9]);
        assertEquals((byte) 0xC9, actual[10]);
        assertEquals(0, actual[11]);
    }

    private static void assertBytesEquals(byte[] expected, byte[] actual) {
        assertEquals(Bytes.asList(expected), Bytes.asList(actual));
    }
}
