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
package org.apache.hadoop.mapreduce.lib.output;

import java.io.IOException;
import java.io.DataOutputStream;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.SequenceFile.ValueBytes;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.mapred.InvalidJobConfException;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.ReflectionUtils;

/** 
 * An {@link org.apache.hadoop.mapreduce.OutputFormat} that writes keys, 
 * values to {@link SequenceFile}s in binary(raw) format
 */
@InterfaceAudience.Public
@InterfaceStability.Stable
public class SequenceFileAsBinaryOutputFormat 
    extends SequenceFileOutputFormat <BytesWritable,BytesWritable> {
  public static String KEY_CLASS = "mapreduce.output.seqbinaryoutputformat.key.class"; 
  public static String VALUE_CLASS = "mapreduce.output.seqbinaryoutputformat.value.class"; 

  /** 
   * Inner class used for appendRaw
   */
  static public class WritableValueBytes implements ValueBytes {
    private BytesWritable value;

    public WritableValueBytes() {
      this.value = null;
    }
    
    public WritableValueBytes(BytesWritable value) {
      this.value = value;
    }

    public void reset(BytesWritable value) {
      this.value = value;
    }

    public void writeUncompressedBytes(DataOutputStream outStream)
        throws IOException {
      outStream.write(value.getBytes(), 0, value.getLength());
    }

    public void writeCompressedBytes(DataOutputStream outStream)
        throws IllegalArgumentException, IOException {
      throw new UnsupportedOperationException(
        "WritableValueBytes doesn't support RECORD compression"); 
    }
    
    public int getSize(){
      return value.getLength();
    }
  }

  /**
   * Set the key class for the {@link SequenceFile}
   * <p>This allows the user to specify the key class to be different 
   * from the actual class ({@link BytesWritable}) used for writing </p>
   * 
   * @param job the {@link Job} to modify
   * @param theClass the SequenceFile output key class.
   */
  static public void setSequenceFileOutputKeyClass(Job job, 
      Class<?> theClass) {
    job.getConfiguration().setClass(KEY_CLASS,
      theClass, Object.class);
  }

  /**
   * Set the value class for the {@link SequenceFile}
   * <p>This allows the user to specify the value class to be different 
   * from the actual class ({@link BytesWritable}) used for writing </p>
   * 
   * @param job the {@link Job} to modify
   * @param theClass the SequenceFile output key class.
   */
  static public void setSequenceFileOutputValueClass(Job job, 
      Class<?> theClass) {
    job.getConfiguration().setClass(VALUE_CLASS, 
                  theClass, Object.class);
  }

  /**
   * Get the key class for the {@link SequenceFile}
   * 
   * @return the key class of the {@link SequenceFile}
   */
  static public Class<? extends WritableComparable> 
      getSequenceFileOutputKeyClass(JobContext job) { 
    return job.getConfiguration().getClass(KEY_CLASS,
      job.getOutputKeyClass().asSubclass(WritableComparable.class), 
      WritableComparable.class);
  }

  /**
   * Get the value class for the {@link SequenceFile}
   * 
   * @return the value class of the {@link SequenceFile}
   */
  static public Class<? extends Writable> getSequenceFileOutputValueClass(
      JobContext job) { 
    return job.getConfiguration().getClass(VALUE_CLASS, 
      job.getOutputValueClass().asSubclass(Writable.class), Writable.class);
  }
  
  @Override 
  public RecordWriter<BytesWritable, BytesWritable> getRecordWriter(
      TaskAttemptContext context) throws IOException {
    final SequenceFile.Writer out = getSequenceWriter(context,
      getSequenceFileOutputKeyClass(context),
      getSequenceFileOutputValueClass(context)); 

    return new RecordWriter<BytesWritable, BytesWritable>() {
      private WritableValueBytes wvaluebytes = new WritableValueBytes();

      public void write(BytesWritable bkey, BytesWritable bvalue)
        throws IOException {
        wvaluebytes.reset(bvalue);
        out.appendRaw(bkey.getBytes(), 0, bkey.getLength(), wvaluebytes);
        wvaluebytes.reset(null);
      }

      public void close(TaskAttemptContext context) throws IOException { 
        out.close();
      }
    };
  }

  protected SequenceFile.Writer getSequenceWriter(TaskAttemptContext context,
      Class<?> keyClass, Class<?> valueClass)
      throws IOException {
    Configuration conf = context.getConfiguration();

    CompressionCodec codec = null;
    CompressionType compressionType = CompressionType.NONE;
    if (getCompressOutput(context)) {
      // find the kind of compression to do
      compressionType = getOutputCompressionType(context);
      // find the right codec
      Class<?> codecClass = getOutputCompressorClass(context,
                                                     DefaultCodec.class);
      codec = (CompressionCodec)
        ReflectionUtils.newInstance(codecClass, conf);
    }
    // get the path of the temporary output file
    Path file = getDefaultWorkFile(context, "");
    FileSystem fs = file.getFileSystem(conf);
    return SequenceFile.createWriter(fs, conf, file,
             keyClass,
             valueClass,
             compressionType,
             codec,
             context);
  }

  @Override 
  public void checkOutputSpecs(JobContext job) throws IOException {
    super.checkOutputSpecs(job);
    if (getCompressOutput(job) && 
        getOutputCompressionType(job) == CompressionType.RECORD ) {
      throw new InvalidJobConfException("SequenceFileAsBinaryOutputFormat "
        + "doesn't support Record Compression" );
    }
  }
}
