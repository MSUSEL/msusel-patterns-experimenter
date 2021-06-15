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

import java.io.*;
import junit.framework.TestCase;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;

import org.apache.hadoop.mapred.lib.*;

public class TestMultipleTextOutputFormat extends TestCase {
  private static JobConf defaultConf = new JobConf();

  private static FileSystem localFs = null;
  static {
    try {
      localFs = FileSystem.getLocal(defaultConf);
    } catch (IOException e) {
      throw new RuntimeException("init failure", e);
    }
  }

  // A random task attempt id for testing.
  private static String attempt = "attempt_200707121733_0001_m_000000_0";

  private static Path workDir = 
    new Path(new Path(
                      new Path(System.getProperty("test.build.data", "."), 
                               "data"), 
                      FileOutputCommitter.TEMP_DIR_NAME), "_" + attempt);

  private static void writeData(RecordWriter<Text, Text> rw) throws IOException {
    for (int i = 10; i < 40; i++) {
      String k = "" + i;
      String v = "" + i;
      rw.write(new Text(k), new Text(v));
    }
  }
  
  static class KeyBasedMultipleTextOutputFormat extends MultipleTextOutputFormat<Text, Text> {
    protected String generateFileNameForKeyValue(Text key, Text v, String name) {
      
      return key.toString().substring(0, 1) + "-" + name;
    }
  }
  
  private static void test1(JobConf job) throws IOException {
    FileSystem fs = FileSystem.getLocal(job);
    String name = "part-00000";
    KeyBasedMultipleTextOutputFormat theOutputFormat = new KeyBasedMultipleTextOutputFormat();
    RecordWriter<Text, Text> rw = theOutputFormat.getRecordWriter(fs, job, name, null);
    writeData(rw);
    rw.close(null);
  }
  
  private static void test2(JobConf job) throws IOException {
    FileSystem fs = FileSystem.getLocal(job);
    String name = "part-00000";
    //pretend that we have input file with 1/2/3 as the suffix
    job.set("map.input.file", "1/2/3");
    // we use the last two legs of the input file as the output file
    job.set("mapred.outputformat.numOfTrailingLegs", "2");
    MultipleTextOutputFormat<Text, Text> theOutputFormat = new MultipleTextOutputFormat<Text, Text>();
    RecordWriter<Text, Text> rw = theOutputFormat.getRecordWriter(fs, job, name, null);
    writeData(rw);
    rw.close(null);
  }
  
  public void testFormat() throws Exception {
    JobConf job = new JobConf();
    job.set("mapred.task.id", attempt);
    FileOutputFormat.setOutputPath(job, workDir.getParent().getParent());
    FileOutputFormat.setWorkOutputPath(job, workDir);
    FileSystem fs = workDir.getFileSystem(job);
    if (!fs.mkdirs(workDir)) {
      fail("Failed to create output directory");
    }
    //System.out.printf("workdir: %s\n", workDir.toString());
    TestMultipleTextOutputFormat.test1(job);
    TestMultipleTextOutputFormat.test2(job);
    String file_11 = "1-part-00000";
    
    File expectedFile_11 = new File(new Path(workDir, file_11).toString()); 

    //System.out.printf("expectedFile_11: %s\n", new Path(workDir, file_11).toString());
    StringBuffer expectedOutput = new StringBuffer();
    for (int i = 10; i < 20; i++) {
      expectedOutput.append(""+i).append('\t').append(""+i).append("\n");
    }
    String output = UtilsForTests.slurp(expectedFile_11);
    //System.out.printf("File_2 output: %s\n", output);
    assertEquals(output, expectedOutput.toString());
    
    String file_12 = "2-part-00000";
    
    File expectedFile_12 = new File(new Path(workDir, file_12).toString()); 
    //System.out.printf("expectedFile_12: %s\n", new Path(workDir, file_12).toString());
    expectedOutput = new StringBuffer();
    for (int i = 20; i < 30; i++) {
      expectedOutput.append(""+i).append('\t').append(""+i).append("\n");
    }
    output = UtilsForTests.slurp(expectedFile_12);
    //System.out.printf("File_2 output: %s\n", output);
    assertEquals(output, expectedOutput.toString());
    
    String file_13 = "3-part-00000";
    
    File expectedFile_13 = new File(new Path(workDir, file_13).toString()); 
    //System.out.printf("expectedFile_13: %s\n", new Path(workDir, file_13).toString());
    expectedOutput = new StringBuffer();
    for (int i = 30; i < 40; i++) {
      expectedOutput.append(""+i).append('\t').append(""+i).append("\n");
    }
    output = UtilsForTests.slurp(expectedFile_13);
    //System.out.printf("File_2 output: %s\n", output);
    assertEquals(output, expectedOutput.toString());
    
    String file_2 = "2/3";
    
    File expectedFile_2 = new File(new Path(workDir, file_2).toString()); 
    //System.out.printf("expectedFile_2: %s\n", new Path(workDir, file_2).toString());
    expectedOutput = new StringBuffer();
    for (int i = 10; i < 40; i++) {
      expectedOutput.append(""+i).append('\t').append(""+i).append("\n");
    }
    output = UtilsForTests.slurp(expectedFile_2);
    //System.out.printf("File_2 output: %s\n", output);
    assertEquals(output, expectedOutput.toString());
  }

  public static void main(String[] args) throws Exception {
    new TestMultipleTextOutputFormat().testFormat();
  }
}
