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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import junit.framework.TestCase;

public class TestTypedBytesStreaming extends TestCase {

  protected File INPUT_FILE = new File("input.txt");
  protected File OUTPUT_DIR = new File("out");
  protected String input = "roses.are.red\nviolets.are.blue\nbunnies.are.pink\n";
  protected String map = StreamUtil.makeJavaCommand(TypedBytesMapApp.class, new String[]{"."});
  protected String reduce = StreamUtil.makeJavaCommand(TypedBytesReduceApp.class, new String[0]);
  protected String outputExpect = "are\t3\nred\t1\nblue\t1\npink\t1\nroses\t1\nbunnies\t1\nviolets\t1\n";
  
  public TestTypedBytesStreaming() throws IOException {
    UtilTest utilTest = new UtilTest(getClass().getName());
    utilTest.checkUserDir();
    utilTest.redirectIfAntJunit();
  }

  protected void createInput() throws IOException {
    DataOutputStream out = new DataOutputStream(new FileOutputStream(INPUT_FILE.getAbsoluteFile()));
    out.write(input.getBytes("UTF-8"));
    out.close();
  }

  protected String[] genArgs() {
    return new String[] {
      "-input", INPUT_FILE.getAbsolutePath(),
      "-output", OUTPUT_DIR.getAbsolutePath(),
      "-mapper", map,
      "-reducer", reduce,
      "-jobconf", "keep.failed.task.files=true",
      "-jobconf", "stream.tmpdir="+System.getProperty("test.build.data","/tmp"),
      "-io", "typedbytes"
    };
  }
  
  public void testCommandLine() throws Exception {
    try {
      try {
        OUTPUT_DIR.getAbsoluteFile().delete();
      } catch (Exception e) {
      }

      createInput();
      OUTPUT_DIR.delete();

      // During tests, the default Configuration will use a local mapred
      // So don't specify -config or -cluster
      StreamJob job = new StreamJob();
      job.setConf(new Configuration());
      job.run(genArgs());
      File outFile = new File(OUTPUT_DIR, "part-00000").getAbsoluteFile();
      String output = StreamUtil.slurp(outFile);
      outFile.delete();
      System.out.println("   map=" + map);
      System.out.println("reduce=" + reduce);
      System.err.println("outEx1=" + outputExpect);
      System.err.println("  out1=" + output);
      assertEquals(outputExpect, output);
    } finally {
      File outFileCRC = new File(OUTPUT_DIR, ".part-00000.crc").getAbsoluteFile();
      INPUT_FILE.delete();
      outFileCRC.delete();
      OUTPUT_DIR.getAbsoluteFile().delete();
    }
  }
}
