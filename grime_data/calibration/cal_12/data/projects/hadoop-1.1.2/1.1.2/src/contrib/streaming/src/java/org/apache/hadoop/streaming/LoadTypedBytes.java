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

import java.io.DataInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.typedbytes.TypedBytesInput;
import org.apache.hadoop.typedbytes.TypedBytesWritable;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Utility program that reads typed bytes from standard input and stores them in
 * a sequence file for which the path is given as an argument.
 */
public class LoadTypedBytes implements Tool {

  private Configuration conf;

  public LoadTypedBytes(Configuration conf) {
    this.conf = conf;
  }
  
  public LoadTypedBytes() {
    this(new Configuration());
  }
  
  public Configuration getConf() {
    return conf;
  }

  public void setConf(Configuration conf) {
    this.conf = conf;
  }
  
  /**
   * The main driver for <code>LoadTypedBytes</code>.
   */
  public int run(String[] args) throws Exception {
    Path path = new Path(args[0]);
    FileSystem fs = path.getFileSystem(getConf());
    if (fs.exists(path)) {
      System.err.println("given path exists already!");
      return -1;
    }
    TypedBytesInput tbinput = new TypedBytesInput(new DataInputStream(System.in));
    SequenceFile.Writer writer = SequenceFile.createWriter(fs, conf, path,
      TypedBytesWritable.class, TypedBytesWritable.class);
    try {
      TypedBytesWritable key = new TypedBytesWritable();
      TypedBytesWritable value = new TypedBytesWritable();
      byte[] rawKey = tbinput.readRaw();
      while (rawKey != null) {
        byte[] rawValue = tbinput.readRaw();
        key.set(rawKey, 0, rawKey.length);
        value.set(rawValue, 0, rawValue.length);
        writer.append(key, value);
        rawKey = tbinput.readRaw();
      }
    } finally {
      writer.close();
    }
    return 0;
  }

  public static void main(String[] args) throws Exception {
    LoadTypedBytes loadtb = new LoadTypedBytes();
    int res = ToolRunner.run(loadtb, args);
    System.exit(res);
  }

}
