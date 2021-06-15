/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.hsqldb.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Atomic transmission packet from HyperSQL server to ODBC client.
 *
 * Sample usage
 * <CODE>
 *     outPacket = OdbcPacketOutputStream.newOdbcPacketOutputStream();
 *     ...
 *     // For each packet you need to transmit:
 *     outPacket.reset();
 *     outPacket.write(this);
 *     outPacket.write(that);
 *     outPacket.xmit('X', hsqlDataOutputStream);
 * </CODE>
 */
class OdbcPacketOutputStream extends DataOutputStream {
    private ByteArrayOutputStream byteArrayOutputStream;
    private ByteArrayOutputStream stringWriterOS = new ByteArrayOutputStream();
    private DataOutputStream stringWriterDos =
        new DataOutputStream(stringWriterOS);
    private int packetStart = 0; // Stream's "written" at start of packet.

    public int getSize() {
        return written - packetStart;
    }

    /**
     * Wrapper method to write a null-terminated String.
     */
    synchronized void write(String s) throws IOException {
        write(s, true);
    }

    synchronized void write(String s, boolean nullTerm)
    throws IOException {
        stringWriterDos.writeUTF(s);
        write(stringWriterOS.toByteArray(), 2, stringWriterOS.size() - 2);
        stringWriterOS.reset();
        if (nullTerm) {
            writeByte(0);
        }
    }

    synchronized void writeSized(String s) throws IOException {
        stringWriterDos.writeUTF(s);
        byte[] ba = stringWriterOS.toByteArray();
        stringWriterOS.reset();

        writeInt(ba.length - 2);
        write(ba, 2, ba.length - 2);
    }

    synchronized void reset() throws IOException {
        byteArrayOutputStream.reset();
        packetStart = written;
        writeInt(-1); // length placeholder
    }

    static OdbcPacketOutputStream newOdbcPacketOutputStream()
    throws IOException {
        return new OdbcPacketOutputStream(new ByteArrayOutputStream());
    }

    protected OdbcPacketOutputStream(
    ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        super(byteArrayOutputStream);
        this.byteArrayOutputStream = byteArrayOutputStream;
        reset();
    }

    /**
     * @return packet size (which does not count the type byte).
     */
    synchronized int xmit(
    char packetType, org.hsqldb.lib.DataOutputStream destinationStream)
    throws IOException {
        byte[] ba = byteArrayOutputStream.toByteArray();
        ba[0] = (byte) (ba.length >> 24);
        ba[1] = (byte) (ba.length >> 16);
        ba[2] = (byte) (ba.length >> 8);
        ba[3] = (byte) ba.length;
        reset();
        destinationStream.writeByte(packetType);
        destinationStream.write(ba);
        destinationStream.flush();
        return ba.length;
    }

    synchronized public void close() throws IOException {
        super.close();
        stringWriterDos.close();
    }

    /**
     * The behavior here is purposefully different from
     * java.io.DataOutputStream.writeChar(int), which writes 2 bytes.
     *
     * We are supporting only 1-byte characters, or don't care about the
     * high bits.
     */
    synchronized public void writeByteChar(char c) throws IOException {
        writeByte(c);
    }
}
