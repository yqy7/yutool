package com.github.yqy7.yutool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;

import com.google.common.primitives.Bytes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class TestLittleEndianDataInputStream {
    private byte[] data;

    @BeforeEach
    protected void setUp() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        initializeData(out);

        data = baos.toByteArray();
    }

    private void initializeData(DataOutputStream out) throws IOException {
        /* Write out various test values NORMALLY */
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
    }

    @Test
    public void testReadFully() throws IOException {
        DataInput in = new LittleEndianDataInputStream(new ByteArrayInputStream(data));
        byte[] b = new byte[data.length];
        in.readFully(b);
        assertEquals(Bytes.asList(data), Bytes.asList(b));
    }

    @Test
    public void testReadUnsignedByte_eof() throws IOException {
        DataInput in = new LittleEndianDataInputStream(new ByteArrayInputStream(new byte[0]));
        try {
            in.readUnsignedByte();
            fail();
        } catch (EOFException expected) {
        }
    }

    @Test
    public void testReadUnsignedShort_eof() throws IOException {
        byte[] buf = {23};
        DataInput in = new LittleEndianDataInputStream(new ByteArrayInputStream(buf));
        try {
            in.readUnsignedShort();
            fail();
        } catch (EOFException expected) {
        }
    }

    //@SuppressWarnings("DoNotCall")
    //public void testReadLine() throws IOException {
    //    DataInput in = new LittleEndianDataInputStream(new ByteArrayInputStream(data));
    //    try {
    //        in.readLine();
    //        fail();
    //    } catch (UnsupportedOperationException expected) {
    //        assertThat(expected).hasMessageThat().isEqualTo("readLine is not supported");
    //    }
    //}

    @Test
    public void testReadLittleEndian() throws IOException {
        DataInput in = new LittleEndianDataInputStream(new ByteArrayInputStream(data));

        /* Read in various values in LITTLE ENDIAN FORMAT */
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
    public void testSkipBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        /* Write out various test values NORMALLY */
        out.write(new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}); // 10 bytes of junk to skip
        initializeData(out);

        byte[] data = baos.toByteArray();

        DataInput in = new LittleEndianDataInputStream(new ByteArrayInputStream(data));
        int bytesSkipped = 0;
        while (bytesSkipped < 10) {
            bytesSkipped += in.skipBytes(10 - bytesSkipped);
        }

        /* Read in various values in LITTLE ENDIAN FORMAT */
        byte[] b = new byte[2];
        in.readFully(b);
        assertEquals(-100, b[0]);
        assertEquals(100, b[1]);
        assertTrue(in.readBoolean());
        assertFalse(in.readBoolean());
    }
}
