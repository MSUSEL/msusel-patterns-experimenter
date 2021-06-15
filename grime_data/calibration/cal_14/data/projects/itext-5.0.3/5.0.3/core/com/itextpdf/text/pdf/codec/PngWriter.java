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
package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.DocWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * Writes a PNG image.
 *
 * @author  Paulo Soares
 * @since 5.0.3
 */
public class PngWriter {
    private static final byte[] PNG_SIGNTURE = {(byte)137, 80, 78, 71, 13, 10, 26, 10};

    private static final byte[] IHDR = DocWriter.getISOBytes("IHDR");
    private static final byte[] PLTE = DocWriter.getISOBytes("PLTE");
    private static final byte[] IDAT = DocWriter.getISOBytes("IDAT");
    private static final byte[] IEND = DocWriter.getISOBytes("IEND");
    private static final byte[] iCCP = DocWriter.getISOBytes("iCCP");

    private static long[] crc_table;

    private OutputStream outp;

    public PngWriter(OutputStream outp) throws IOException {
        this.outp = outp;
        outp.write(PNG_SIGNTURE);
    }

    public void writeHeader(int width, int height, int bitDepth, int colorType) throws IOException {
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        outputInt(width, ms);
        outputInt(height, ms);
        ms.write(bitDepth);
        ms.write(colorType);
        ms.write(0);
        ms.write(0);
        ms.write(0);
        writeChunk(IHDR, ms.toByteArray());
    }

    public void writeEnd() throws IOException {
        writeChunk(IEND, new byte[0]);
    }

    public void writeData(byte[] data, int stride) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DeflaterOutputStream zip = new DeflaterOutputStream(stream);
        for (int k = 0; k < data.length; k += stride) {
            zip.write(0);
            zip.write(data, k, stride);
        }
        zip.finish();
        writeChunk(IDAT, stream.toByteArray());
    }

    public void writePalette(byte[] data) throws IOException {
        writeChunk(PLTE, data);
    }

    public void writeIccProfile(byte[] data) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write((byte)'I');
        stream.write((byte)'C');
        stream.write((byte)'C');
        stream.write(0);
        stream.write(0);
        DeflaterOutputStream zip = new DeflaterOutputStream(stream);
        zip.write(data);
        zip.finish();
        writeChunk(iCCP, stream.toByteArray());
    }

    private static void make_crc_table() {
        if (crc_table != null)
            return;
        long[] crc2 = new long[256];
        for (int n = 0; n < 256; n++) {
            long c = n;
            for (int k = 0; k < 8; k++) {
                if ((c & 1) != 0)
                    c = 0xedb88320L ^ (c >> 1);
                else
                    c = c >> 1;
            }
            crc2[n] = c;
        }
        crc_table = crc2;
    }

    private static long update_crc(long crc, byte[] buf, int offset, int len) {
        long c = crc;

        if (crc_table == null)
            make_crc_table();
        for (int n = 0; n < len; n++) {
            c = crc_table[(int)((c ^ buf[n + offset]) & 0xff)] ^ (c >> 8);
        }
        return c;
    }

    private static long crc(byte[] buf, int offset, int len) {
        return update_crc(0xffffffffL, buf, offset, len) ^ 0xffffffffL;
    }

    private static long crc(byte[] buf) {
        return update_crc(0xffffffffL, buf, 0, buf.length) ^ 0xffffffffL;
    }

    public void outputInt(int n) throws IOException {
        outputInt(n, outp);
    }

    public static void outputInt(int n, OutputStream s) throws IOException {
        s.write((byte)(n >> 24));
        s.write((byte)(n >> 16));
        s.write((byte)(n >> 8));
        s.write((byte)n);
    }

    public void writeChunk(byte[] chunkType, byte[] data) throws IOException {
        outputInt(data.length);
        outp.write(chunkType, 0, 4);
        outp.write(data);
        long c = update_crc(0xffffffffL, chunkType, 0, chunkType.length);
        c = update_crc(c, data, 0, data.length) ^ 0xffffffffL;
        outputInt((int)c);
    }
}
