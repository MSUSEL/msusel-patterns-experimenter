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
package org.apache.hadoop.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ChecksumException;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;

import junit.framework.TestCase;

public class TestIFileStreams extends TestCase {

  public void testIFileStream() throws Exception {
    final int DLEN = 100;
    DataOutputBuffer dob = new DataOutputBuffer(DLEN + 4);
    IFileOutputStream ifos = new IFileOutputStream(dob);
    for (int i = 0; i < DLEN; ++i) {
      ifos.write(i);
    }
    ifos.close();
    DataInputBuffer dib = new DataInputBuffer();
    dib.reset(dob.getData(), DLEN + 4);
    IFileInputStream ifis = new IFileInputStream(dib, 104, new Configuration());
    for (int i = 0; i < DLEN; ++i) {
      assertEquals(i, ifis.read());
    }
    ifis.close();
  }

  public void testBadIFileStream() throws Exception {
    final int DLEN = 100;
    DataOutputBuffer dob = new DataOutputBuffer(DLEN + 4);
    IFileOutputStream ifos = new IFileOutputStream(dob);
    for (int i = 0; i < DLEN; ++i) {
      ifos.write(i);
    }
    ifos.close();
    DataInputBuffer dib = new DataInputBuffer();
    final byte[] b = dob.getData();
    ++b[17];
    dib.reset(b, DLEN + 4);
    IFileInputStream ifis = new IFileInputStream(dib, 104, new Configuration());
    int i = 0;
    try {
      while (i < DLEN) {
        if (17 == i) {
          assertEquals(18, ifis.read());
        } else {
          assertEquals(i, ifis.read());
        }
        ++i;
      }
      ifis.close();
    } catch (ChecksumException e) {
      assertEquals("Unexpected bad checksum", DLEN - 1, i);
      return;
    }
    fail("Did not detect bad data in checksum");
  }

  public void testBadLength() throws Exception {
    final int DLEN = 100;
    DataOutputBuffer dob = new DataOutputBuffer(DLEN + 4);
    IFileOutputStream ifos = new IFileOutputStream(dob);
    for (int i = 0; i < DLEN; ++i) {
      ifos.write(i);
    }
    ifos.close();
    DataInputBuffer dib = new DataInputBuffer();
    dib.reset(dob.getData(), DLEN + 4);
    IFileInputStream ifis = new IFileInputStream(dib, 100, new Configuration());
    int i = 0;
    try {
      while (i < DLEN - 8) {
        assertEquals(i++, ifis.read());
      }
      ifis.close();
    } catch (ChecksumException e) {
      assertEquals("Checksum before close", i, DLEN - 8);
      return;
    }
    fail("Did not detect bad data in checksum");
  }

}
