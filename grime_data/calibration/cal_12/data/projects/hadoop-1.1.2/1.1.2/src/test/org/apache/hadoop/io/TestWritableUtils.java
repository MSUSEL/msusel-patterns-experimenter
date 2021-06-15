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
package org.apache.hadoop.io;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

public class TestWritableUtils extends TestCase {
  private static final Log LOG = LogFactory.getLog(TestWritableUtils.class);

  public static void testValue(int val, int vintlen) throws IOException {
    DataOutputBuffer buf = new DataOutputBuffer();
    DataInputBuffer inbuf = new DataInputBuffer();
    WritableUtils.writeVInt(buf, val);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Value = " + val);
      BytesWritable printer = new BytesWritable();
      printer.set(buf.getData(), 0, buf.getLength());
      LOG.debug("Buffer = " + printer);
    }
    inbuf.reset(buf.getData(), 0, buf.getLength());
    assertEquals(val, WritableUtils.readVInt(inbuf));
    assertEquals(vintlen, buf.getLength());
    assertEquals(vintlen, WritableUtils.getVIntSize(val));
    assertEquals(vintlen, WritableUtils.decodeVIntSize(buf.getData()[0]));
  }

  public static void testVInt() throws Exception {
    testValue(12, 1);
    testValue(127, 1);
    testValue(-112, 1);
    testValue(-113, 2);
    testValue(-128, 2);
    testValue(128, 2);
    testValue(-129, 2);
    testValue(255, 2);
    testValue(-256, 2);
    testValue(256, 3);
    testValue(-257, 3);
    testValue(65535, 3);
    testValue(-65536, 3);
    testValue(65536, 4);
    testValue(-65537, 4);
  }
}
