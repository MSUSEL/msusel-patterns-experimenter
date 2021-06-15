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
package org.apache.hadoop.mapreduce.lib.fieldsel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MapReduceTestUtil;

import junit.framework.TestCase;
import java.text.NumberFormat;

public class TestMRFieldSelection extends TestCase {

private static NumberFormat idFormat = NumberFormat.getInstance();
  static {
    idFormat.setMinimumIntegerDigits(4);
    idFormat.setGroupingUsed(false);
  }

  public void testFieldSelection() throws Exception {
    launch();
  }
  private static Path testDir = new Path(
    System.getProperty("test.build.data", "/tmp"), "field");
  
  public static void launch() throws Exception {
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(conf);
    int numOfInputLines = 10;

    Path outDir = new Path(testDir, "output_for_field_selection_test");
    Path inDir = new Path(testDir, "input_for_field_selection_test");

    StringBuffer inputData = new StringBuffer();
    StringBuffer expectedOutput = new StringBuffer();
    constructInputOutputData(inputData, expectedOutput, numOfInputLines);
    
    conf.set(FieldSelectionHelper.DATA_FIELD_SEPERATOR, "-");
    conf.set(FieldSelectionHelper.MAP_OUTPUT_KEY_VALUE_SPEC, "6,5,1-3:0-");
    conf.set(
      FieldSelectionHelper.REDUCE_OUTPUT_KEY_VALUE_SPEC, ":4,3,2,1,0,0-");
    Job job = MapReduceTestUtil.createJob(conf, inDir, outDir,
      1, 1, inputData.toString());
    job.setMapperClass(FieldSelectionMapper.class);
    job.setReducerClass(FieldSelectionReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    job.setNumReduceTasks(1);

    job.waitForCompletion(true);
    assertTrue("Job Failed!", job.isSuccessful());

    //
    // Finally, we compare the reconstructed answer key with the
    // original one.  Remember, we need to ignore zero-count items
    // in the original key.
    //
    String outdata = MapReduceTestUtil.readOutput(outDir, conf);
    assertEquals("Outputs doesnt match.",expectedOutput.toString(), outdata);
    fs.delete(outDir, true);
  }

  public static void constructInputOutputData(StringBuffer inputData,
      StringBuffer expectedOutput, int numOfInputLines) {
    for (int i = 0; i < numOfInputLines; i++) {
      inputData.append(idFormat.format(i));
      inputData.append("-").append(idFormat.format(i+1));
      inputData.append("-").append(idFormat.format(i+2));
      inputData.append("-").append(idFormat.format(i+3));
      inputData.append("-").append(idFormat.format(i+4));
      inputData.append("-").append(idFormat.format(i+5));
      inputData.append("-").append(idFormat.format(i+6));
      inputData.append("\n");

      expectedOutput.append(idFormat.format(i+3));
      expectedOutput.append("-" ).append (idFormat.format(i+2));
      expectedOutput.append("-" ).append (idFormat.format(i+1));
      expectedOutput.append("-" ).append (idFormat.format(i+5));
      expectedOutput.append("-" ).append (idFormat.format(i+6));

      expectedOutput.append("-" ).append (idFormat.format(i+6));
      expectedOutput.append("-" ).append (idFormat.format(i+5));
      expectedOutput.append("-" ).append (idFormat.format(i+1));
      expectedOutput.append("-" ).append (idFormat.format(i+2));
      expectedOutput.append("-" ).append (idFormat.format(i+3));
      expectedOutput.append("-" ).append (idFormat.format(i+0));
      expectedOutput.append("-" ).append (idFormat.format(i+1));
      expectedOutput.append("-" ).append (idFormat.format(i+2));
      expectedOutput.append("-" ).append (idFormat.format(i+3));
      expectedOutput.append("-" ).append (idFormat.format(i+4));
      expectedOutput.append("-" ).append (idFormat.format(i+5));
      expectedOutput.append("-" ).append (idFormat.format(i+6));
      expectedOutput.append("\n");
    }
    System.out.println("inputData:");
    System.out.println(inputData.toString());
    System.out.println("ExpectedData:");
    System.out.println(expectedOutput.toString());
  }
  
  /**
   * Launches all the tasks in order.
   */
  public static void main(String[] argv) throws Exception {
    launch();
  }
}
