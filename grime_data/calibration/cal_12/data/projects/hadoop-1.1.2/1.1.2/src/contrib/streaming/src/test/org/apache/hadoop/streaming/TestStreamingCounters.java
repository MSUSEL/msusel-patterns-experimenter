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
package org.apache.hadoop.streaming;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.Counters.Counter;
import org.apache.hadoop.mapred.Counters.Group;

/**
 * This class tests streaming counters in MapReduce local mode.
 */
public class TestStreamingCounters extends TestStreaming {

  public TestStreamingCounters() throws IOException {
    super();
  }

  public void testCommandLine() throws IOException
  {
    try {
      try {
        OUTPUT_DIR.getAbsoluteFile().delete();
      } catch (Exception e) {
      }

      createInput();
      boolean mayExit = false;

      // During tests, the default Configuration will use a local mapred
      // So don't specify -config or -cluster
      StreamJob job = new StreamJob(genArgs(), mayExit);      
      job.go();
      File outFile = new File(OUTPUT_DIR, "part-00000").getAbsoluteFile();
      String output = StreamUtil.slurp(outFile);
      outFile.delete();
      assertEquals(outputExpect, output);
      
      Counters counters = job.running_.getCounters();
      assertNotNull("Counters", counters);
      Group group = counters.getGroup("UserCounters");
      assertNotNull("Group", group);
      Counter counter = group.getCounterForName("InputLines");
      assertNotNull("Counter", counter);
      assertEquals(3, counter.getCounter());
    } finally {
      File outFileCRC = new File(OUTPUT_DIR, ".part-00000.crc").getAbsoluteFile();
      INPUT_FILE.delete();
      outFileCRC.delete();
      OUTPUT_DIR.getAbsoluteFile().delete();
    }
  }
  
}
