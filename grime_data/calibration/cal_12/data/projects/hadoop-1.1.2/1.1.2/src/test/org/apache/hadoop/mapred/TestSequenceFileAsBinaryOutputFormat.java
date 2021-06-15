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

import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.SequenceFile.CompressionType;

import junit.framework.TestCase;
import org.apache.commons.logging.*;

public class TestSequenceFileAsBinaryOutputFormat extends TestCase {
  private static final Log LOG =
      LogFactory.getLog(TestSequenceFileAsBinaryOutputFormat.class.getName());

  private static final int RECORDS = 10000;
  // A random task attempt id for testing.
  private static final String attempt = "attempt_200707121733_0001_m_000000_0";

  public void testBinary() throws IOException {
    JobConf job = new JobConf();
    FileSystem fs = FileSystem.getLocal(job);
    
    Path dir = 
      new Path(new Path(new Path(System.getProperty("test.build.data",".")), 
                        FileOutputCommitter.TEMP_DIR_NAME), "_" + attempt);
    Path file = new Path(dir, "testbinary.seq");
    Random r = new Random();
    long seed = r.nextLong();
    r.setSeed(seed);

    fs.delete(dir, true);
    if (!fs.mkdirs(dir)) { 
      fail("Failed to create output directory");
    }

    job.set("mapred.task.id", attempt);
    FileOutputFormat.setOutputPath(job, dir.getParent().getParent());
    FileOutputFormat.setWorkOutputPath(job, dir);

    SequenceFileAsBinaryOutputFormat.setSequenceFileOutputKeyClass(job, 
                                          IntWritable.class );
    SequenceFileAsBinaryOutputFormat.setSequenceFileOutputValueClass(job, 
                                          DoubleWritable.class ); 

    SequenceFileAsBinaryOutputFormat.setCompressOutput(job, true);
    SequenceFileAsBinaryOutputFormat.setOutputCompressionType(job, 
                                                       CompressionType.BLOCK);

    BytesWritable bkey = new BytesWritable();
    BytesWritable bval = new BytesWritable();


    RecordWriter <BytesWritable, BytesWritable> writer = 
      new SequenceFileAsBinaryOutputFormat().getRecordWriter(fs, 
                                                       job, file.toString(),
                                                       Reporter.NULL);

    IntWritable iwritable = new IntWritable();
    DoubleWritable dwritable = new DoubleWritable();
    DataOutputBuffer outbuf = new DataOutputBuffer();
    LOG.info("Creating data by SequenceFileAsBinaryOutputFormat");
    try {
      for (int i = 0; i < RECORDS; ++i) {
        iwritable = new IntWritable(r.nextInt());
        iwritable.write(outbuf);
        bkey.set(outbuf.getData(), 0, outbuf.getLength());
        outbuf.reset();
        dwritable = new DoubleWritable(r.nextDouble());
        dwritable.write(outbuf);
        bval.set(outbuf.getData(), 0, outbuf.getLength());
        outbuf.reset();
        writer.write(bkey, bval);
      }
    } finally {
      writer.close(Reporter.NULL);
    }

    InputFormat<IntWritable,DoubleWritable> iformat =
                    new SequenceFileInputFormat<IntWritable,DoubleWritable>();
    int count = 0;
    r.setSeed(seed);
    DataInputBuffer buf = new DataInputBuffer();
    final int NUM_SPLITS = 3;
    SequenceFileInputFormat.addInputPath(job, file);
    LOG.info("Reading data by SequenceFileInputFormat");
    for (InputSplit split : iformat.getSplits(job, NUM_SPLITS)) {
      RecordReader<IntWritable,DoubleWritable> reader =
        iformat.getRecordReader(split, job, Reporter.NULL);
      try {
        int sourceInt;
        double sourceDouble;
        while (reader.next(iwritable, dwritable)) {
          sourceInt = r.nextInt();
          sourceDouble = r.nextDouble();
          assertEquals(
              "Keys don't match: " + "*" + iwritable.get() + ":" + 
                                           sourceInt + "*",
              sourceInt, iwritable.get());
          assertTrue(
              "Vals don't match: " + "*" + dwritable.get() + ":" +
                                           sourceDouble + "*",
              Double.compare(dwritable.get(), sourceDouble) == 0 );
          ++count;
        }
      } finally {
        reader.close();
      }
    }
    assertEquals("Some records not found", RECORDS, count);
  }

  public void testSequenceOutputClassDefaultsToMapRedOutputClass() 
         throws IOException {
    JobConf job = new JobConf();
    FileSystem fs = FileSystem.getLocal(job);

    // Setting Random class to test getSequenceFileOutput{Key,Value}Class
    job.setOutputKeyClass(FloatWritable.class);
    job.setOutputValueClass(BooleanWritable.class);

    assertEquals("SequenceFileOutputKeyClass should default to ouputKeyClass", 
             FloatWritable.class,
             SequenceFileAsBinaryOutputFormat.getSequenceFileOutputKeyClass(
                                                                         job));
    assertEquals("SequenceFileOutputValueClass should default to " 
             + "ouputValueClass", 
             BooleanWritable.class,
             SequenceFileAsBinaryOutputFormat.getSequenceFileOutputValueClass(
                                                                         job));

    SequenceFileAsBinaryOutputFormat.setSequenceFileOutputKeyClass(job, 
                                          IntWritable.class );
    SequenceFileAsBinaryOutputFormat.setSequenceFileOutputValueClass(job, 
                                          DoubleWritable.class ); 

    assertEquals("SequenceFileOutputKeyClass not updated", 
             IntWritable.class,
             SequenceFileAsBinaryOutputFormat.getSequenceFileOutputKeyClass(
                                                                         job));
    assertEquals("SequenceFileOutputValueClass not updated", 
             DoubleWritable.class,
             SequenceFileAsBinaryOutputFormat.getSequenceFileOutputValueClass(
                                                                         job));
  }

  public void testcheckOutputSpecsForbidRecordCompression() throws IOException {
    JobConf job = new JobConf();
    FileSystem fs = FileSystem.getLocal(job);
    Path dir = new Path(System.getProperty("test.build.data",".") + "/mapred");
    Path outputdir = new Path(System.getProperty("test.build.data",".") 
                              + "/output");

    fs.delete(dir, true);
    fs.delete(outputdir, true);
    if (!fs.mkdirs(dir)) { 
      fail("Failed to create output directory");
    }

    FileOutputFormat.setWorkOutputPath(job, dir);

    // Without outputpath, FileOutputFormat.checkoutputspecs will throw 
    // InvalidJobConfException
    FileOutputFormat.setOutputPath(job, outputdir);

    // SequenceFileAsBinaryOutputFormat doesn't support record compression
    // It should throw an exception when checked by checkOutputSpecs
    SequenceFileAsBinaryOutputFormat.setCompressOutput(job, true);

    SequenceFileAsBinaryOutputFormat.setOutputCompressionType(job, 
                                                       CompressionType.BLOCK);
    try {
      new SequenceFileAsBinaryOutputFormat().checkOutputSpecs(fs, job);
    } catch (Exception e) {
      fail("Block compression should be allowed for " 
                       + "SequenceFileAsBinaryOutputFormat:" 
                       + "Caught " + e.getClass().getName());
    }

    SequenceFileAsBinaryOutputFormat.setOutputCompressionType(job, 
                                                       CompressionType.RECORD);
    try {
      new SequenceFileAsBinaryOutputFormat().checkOutputSpecs(fs, job);
      fail("Record compression should not be allowed for " 
                           +"SequenceFileAsBinaryOutputFormat");
    } catch (InvalidJobConfException ie) {
      // expected
    } catch (Exception e) {
      fail("Expected " + InvalidJobConfException.class.getName() 
                       + "but caught " + e.getClass().getName() );
    }
  }
}
