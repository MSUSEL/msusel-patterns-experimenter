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

public class TestTextOutputFormat extends TestCase {
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

  @SuppressWarnings("unchecked")
  public void testFormat() throws Exception {
    JobConf job = new JobConf();
    job.set("mapred.task.id", attempt);
    FileOutputFormat.setOutputPath(job, workDir.getParent().getParent());
    FileOutputFormat.setWorkOutputPath(job, workDir);
    FileSystem fs = workDir.getFileSystem(job);
    if (!fs.mkdirs(workDir)) {
      fail("Failed to create output directory");
    }
    String file = "test.txt";

    // A reporter that does nothing
    Reporter reporter = Reporter.NULL;

    TextOutputFormat theOutputFormat = new TextOutputFormat();
    RecordWriter theRecordWriter =
      theOutputFormat.getRecordWriter(localFs, job, file, reporter);

    Text key1 = new Text("key1");
    Text key2 = new Text("key2");
    Text val1 = new Text("val1");
    Text val2 = new Text("val2");
    NullWritable nullWritable = NullWritable.get();

    try {
      theRecordWriter.write(key1, val1);
      theRecordWriter.write(null, nullWritable);
      theRecordWriter.write(null, val1);
      theRecordWriter.write(nullWritable, val2);
      theRecordWriter.write(key2, nullWritable);
      theRecordWriter.write(key1, null);
      theRecordWriter.write(null, null);
      theRecordWriter.write(key2, val2);

    } finally {
      theRecordWriter.close(reporter);
    }
    File expectedFile = new File(new Path(workDir, file).toString());
    StringBuffer expectedOutput = new StringBuffer();
    expectedOutput.append(key1).append('\t').append(val1).append("\n");
    expectedOutput.append(val1).append("\n");
    expectedOutput.append(val2).append("\n");
    expectedOutput.append(key2).append("\n");
    expectedOutput.append(key1).append("\n");
    expectedOutput.append(key2).append('\t').append(val2).append("\n");
    String output = UtilsForTests.slurp(expectedFile);
    assertEquals(output, expectedOutput.toString());

  }

  @SuppressWarnings("unchecked")
  public void testFormatWithCustomSeparator() throws Exception {
    JobConf job = new JobConf();
    String separator = "\u0001";
    job.set("mapred.textoutputformat.separator", separator);
    job.set("mapred.task.id", attempt);
    FileOutputFormat.setOutputPath(job, workDir.getParent().getParent());
    FileOutputFormat.setWorkOutputPath(job, workDir);
    FileSystem fs = workDir.getFileSystem(job);
    if (!fs.mkdirs(workDir)) {
      fail("Failed to create output directory");
    }
    String file = "test.txt";

    // A reporter that does nothing
    Reporter reporter = Reporter.NULL;

    TextOutputFormat theOutputFormat = new TextOutputFormat();
    RecordWriter theRecordWriter =
      theOutputFormat.getRecordWriter(localFs, job, file, reporter);

    Text key1 = new Text("key1");
    Text key2 = new Text("key2");
    Text val1 = new Text("val1");
    Text val2 = new Text("val2");
    NullWritable nullWritable = NullWritable.get();

    try {
      theRecordWriter.write(key1, val1);
      theRecordWriter.write(null, nullWritable);
      theRecordWriter.write(null, val1);
      theRecordWriter.write(nullWritable, val2);
      theRecordWriter.write(key2, nullWritable);
      theRecordWriter.write(key1, null);
      theRecordWriter.write(null, null);
      theRecordWriter.write(key2, val2);

    } finally {
      theRecordWriter.close(reporter);
    }
    File expectedFile = new File(new Path(workDir, file).toString());
    StringBuffer expectedOutput = new StringBuffer();
    expectedOutput.append(key1).append(separator).append(val1).append("\n");
    expectedOutput.append(val1).append("\n");
    expectedOutput.append(val2).append("\n");
    expectedOutput.append(key2).append("\n");
    expectedOutput.append(key1).append("\n");
    expectedOutput.append(key2).append(separator).append(val2).append("\n");
    String output = UtilsForTests.slurp(expectedFile);
    assertEquals(output, expectedOutput.toString());

  }

  public static void main(String[] args) throws Exception {
    new TestTextOutputFormat().testFormat();
  }
}
