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
package org.apache.hadoop.tools;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.InvalidInputException;
import org.apache.hadoop.mapred.JobConf;

/**
 * An abstract class for distributed tool for file related operations.
 */
abstract class DistTool implements org.apache.hadoop.util.Tool {
  protected static final Log LOG = LogFactory.getLog(DistTool.class);

  protected JobConf jobconf;

  /** {@inheritDoc} */
  public void setConf(Configuration conf) {
    if (jobconf != conf) {
      jobconf = conf instanceof JobConf? (JobConf)conf: new JobConf(conf);
    }
  }

  /** {@inheritDoc} */
  public JobConf getConf() {return jobconf;}

  protected DistTool(Configuration conf) {setConf(conf);}

  private static final Random RANDOM = new Random();
  protected static String getRandomId() {
    return Integer.toString(RANDOM.nextInt(Integer.MAX_VALUE), 36);
  }

  /** Sanity check for source */
  protected static void checkSource(Configuration conf, List<Path> srcs
      ) throws InvalidInputException {
    List<IOException> ioes = new ArrayList<IOException>();
    for(Path p : srcs) {
      try {
        if (!p.getFileSystem(conf).exists(p)) {
          ioes.add(new FileNotFoundException("Source "+p+" does not exist."));
        }
      }
      catch(IOException e) {ioes.add(e);}
    }
    if (!ioes.isEmpty()) {
      throw new InvalidInputException(ioes);
    }
  }

  protected static String readString(DataInput in) throws IOException {
    if (in.readBoolean()) {
      return Text.readString(in);
    }
    return null;
  }

  protected static void writeString(DataOutput out, String s
      ) throws IOException {
    boolean b = s != null;
    out.writeBoolean(b);
    if (b) {Text.writeString(out, s);}
  }

  protected static List<String> readFile(Configuration conf, Path inputfile
      ) throws IOException {
    List<String> result = new ArrayList<String>();
    FileSystem fs = inputfile.getFileSystem(conf);
    BufferedReader input = null;
    try {
      input = new BufferedReader(new InputStreamReader(fs.open(inputfile)));
      for(String line; (line = input.readLine()) != null;) {
        result.add(line);
      }
    } finally {
      input.close();
    }
    return result;
  }

  /** An exception class for duplicated source files. */
  public static class DuplicationException extends IOException {
    private static final long serialVersionUID = 1L;
    /** Error code for this exception */
    public static final int ERROR_CODE = -2;
    DuplicationException(String message) {super(message);}
  }
}