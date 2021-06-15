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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.typedbytes.TypedBytesOutput;
import org.apache.hadoop.typedbytes.TypedBytesWritableOutput;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Utility program that fetches all files that match a given pattern and dumps
 * their content to stdout as typed bytes. This works for all files that can be
 * handled by {@link org.apache.hadoop.streaming.AutoInputFormat}.
 */
public class DumpTypedBytes implements Tool {

  private Configuration conf;

  public DumpTypedBytes(Configuration conf) {
    this.conf = conf;
  }
  
  public DumpTypedBytes() {
    this(new Configuration());
  }

  public Configuration getConf() {
    return conf;
  }

  public void setConf(Configuration conf) {
    this.conf = conf;
  }

  /**
   * The main driver for <code>DumpTypedBytes</code>.
   */
  public int run(String[] args) throws Exception {
    Path pattern = new Path(args[0]);
    FileSystem fs = pattern.getFileSystem(getConf());
    fs.setVerifyChecksum(true);
    for (Path p : FileUtil.stat2Paths(fs.globStatus(pattern), pattern)) {
      List<FileStatus> inputFiles = new ArrayList<FileStatus>();
      FileStatus status = fs.getFileStatus(p);
      if (status.isDir()) {
        FileStatus[] files = fs.listStatus(p);
        Collections.addAll(inputFiles, files);
      } else {
        inputFiles.add(status);
      }
      return dumpTypedBytes(inputFiles);
    }
    return -1;
  }

  /**
   * Dump given list of files to standard output as typed bytes.
   */
  @SuppressWarnings("unchecked")
  private int dumpTypedBytes(List<FileStatus> files) throws IOException {
    JobConf job = new JobConf(getConf()); 
    DataOutputStream dout = new DataOutputStream(System.out);
    AutoInputFormat autoInputFormat = new AutoInputFormat();
    for (FileStatus fileStatus : files) {
      FileSplit split = new FileSplit(fileStatus.getPath(), 0,
        fileStatus.getLen() * fileStatus.getBlockSize(),
        (String[]) null);
      RecordReader recReader = null;
      try {
        recReader = autoInputFormat.getRecordReader(split, job, Reporter.NULL);
        Object key = recReader.createKey();
        Object value = recReader.createValue();
        while (recReader.next(key, value)) {
          if (key instanceof Writable) {
            TypedBytesWritableOutput.get(dout).write((Writable) key);
          } else {
            TypedBytesOutput.get(dout).write(key);
          }
          if (value instanceof Writable) {
            TypedBytesWritableOutput.get(dout).write((Writable) value);
          } else {
            TypedBytesOutput.get(dout).write(value);
          }
        }
      } finally {
        if (recReader != null) {
          recReader.close();
        }
      }
    }
    dout.flush();
    return 0;
  }

  public static void main(String[] args) throws Exception {
    DumpTypedBytes dumptb = new DumpTypedBytes();
    int res = ToolRunner.run(dumptb, args);
    System.exit(res);
  }
  
}
