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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class TestMultiFileSplit extends TestCase{

    public void testReadWrite() throws Exception {
      MultiFileSplit split = new MultiFileSplit(new JobConf(), new Path[] {new Path("/test/path/1"), new Path("/test/path/2")}, new long[] {100,200});
        
      ByteArrayOutputStream bos = null;
      byte[] result = null;
      try {    
        bos = new ByteArrayOutputStream();
        split.write(new DataOutputStream(bos));
        result = bos.toByteArray();
      } finally {
        IOUtils.closeStream(bos);
      }
      
      MultiFileSplit readSplit = new MultiFileSplit();
      ByteArrayInputStream bis = null;
      try {
        bis = new ByteArrayInputStream(result);
        readSplit.readFields(new DataInputStream(bis));
      } finally {
        IOUtils.closeStream(bis);
      }
      
      assertTrue(split.getLength() != 0);
      assertEquals(split.getLength(), readSplit.getLength());
      assertTrue(Arrays.equals(split.getPaths(), readSplit.getPaths()));
      assertTrue(Arrays.equals(split.getLengths(), readSplit.getLengths()));
      System.out.println(split.toString());
    }
}
