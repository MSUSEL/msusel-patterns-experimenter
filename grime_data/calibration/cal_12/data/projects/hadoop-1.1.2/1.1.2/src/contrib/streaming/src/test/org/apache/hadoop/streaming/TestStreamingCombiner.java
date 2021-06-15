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

import java.io.IOException;

import org.apache.hadoop.mapred.Counters;

public class TestStreamingCombiner extends TestStreaming {

  protected String combine = StreamUtil.makeJavaCommand(UniqApp.class, new String[]{""});
  
  public TestStreamingCombiner() throws IOException {
    super();
  }
  
  protected String[] genArgs() {
    return new String[] {
      "-input", INPUT_FILE.getAbsolutePath(),
      "-output", OUTPUT_DIR.getAbsolutePath(),
      "-mapper", map,
      "-reducer", reduce,
      "-combiner", combine,
      "-jobconf", "stream.tmpdir="+System.getProperty("test.build.data","/tmp")
    };
  }

  public void testCommandLine() throws IOException {
    super.testCommandLine();
    // validate combiner counters
    String counterGrp = "org.apache.hadoop.mapred.Task$Counter";
    Counters counters = job.running_.getCounters();
    assertTrue(counters.findCounter(
               counterGrp, "COMBINE_INPUT_RECORDS").getValue() != 0);
    assertTrue(counters.findCounter(
               counterGrp, "COMBINE_OUTPUT_RECORDS").getValue() != 0);
  }

  public static void main(String[]args) throws Exception
  {
    new TestStreamingCombiner().testCommandLine();
  }

}
