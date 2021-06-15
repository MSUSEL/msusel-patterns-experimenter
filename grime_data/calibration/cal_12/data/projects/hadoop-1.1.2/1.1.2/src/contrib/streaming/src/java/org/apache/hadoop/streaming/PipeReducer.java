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
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.net.URLDecoder;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.SkipBadRecords;
import org.apache.hadoop.streaming.io.InputWriter;
import org.apache.hadoop.streaming.io.OutputReader;
import org.apache.hadoop.util.StringUtils;

import org.apache.hadoop.io.Writable;

/** A generic Reducer bridge.
 *  It delegates operations to an external program via stdin and stdout.
 */
public class PipeReducer extends PipeMapRed implements Reducer {

  private byte[] reduceOutFieldSeparator;
  private byte[] reduceInputFieldSeparator;
  private int numOfReduceOutputKeyFields = 1;
  private boolean skipping = false;
  
  String getPipeCommand(JobConf job) {
    String str = job.get("stream.reduce.streamprocessor");
    if (str == null) {
      return str;
    }
    try {
      return URLDecoder.decode(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      System.err.println("stream.reduce.streamprocessor in jobconf not found");
      return null;
    }
  }

  boolean getDoPipe() {
    String argv = getPipeCommand(job_);
    // Currently: null is identity reduce. REDUCE_NONE is no-map-outputs.
    return (argv != null) && !StreamJob.REDUCE_NONE.equals(argv);
  }

  public void configure(JobConf job) {
    super.configure(job);
    //disable the auto increment of the counter. For streaming, no of 
    //processed records could be different(equal or less) than the no of 
    //records input.
    SkipBadRecords.setAutoIncrReducerProcCount(job, false);
    skipping = job.getBoolean("mapred.skip.on", false);

    try {
      reduceOutFieldSeparator = job_.get("stream.reduce.output.field.separator", "\t").getBytes("UTF-8");
      reduceInputFieldSeparator = job_.get("stream.reduce.input.field.separator", "\t").getBytes("UTF-8");
      this.numOfReduceOutputKeyFields = job_.getInt("stream.num.reduce.output.key.fields", 1);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("The current system does not support UTF-8 encoding!", e);
    }
  }

  public void reduce(Object key, Iterator values, OutputCollector output,
                     Reporter reporter) throws IOException {

    // init
    if (doPipe_ && outThread_ == null) {
      startOutputThreads(output, reporter);
    }
    try {
      while (values.hasNext()) {
        Writable val = (Writable) values.next();
        numRecRead_++;
        maybeLogRecord();
        if (doPipe_) {
          if (outerrThreadsThrowable != null) {
            mapRedFinished();
            throw new IOException ("MROutput/MRErrThread failed:"
                                   + StringUtils.stringifyException(
                                                                    outerrThreadsThrowable));
          }
          inWriter_.writeKey(key);
          inWriter_.writeValue(val);
        } else {
          // "identity reduce"
          output.collect(key, val);
        }
      }
      if(doPipe_ && skipping) {
        //flush the streams on every record input if running in skip mode
        //so that we don't buffer other records surrounding a bad record. 
        clientOut_.flush();
      }
    } catch (IOException io) {
      // a common reason to get here is failure of the subprocess.
      // Document that fact, if possible.
      String extraInfo = "";
      try {
        int exitVal = sim.exitValue();
	if (exitVal == 0) {
	  extraInfo = "subprocess exited successfully\n";
	} else {
	  extraInfo = "subprocess exited with error code " + exitVal + "\n";
	};
      } catch (IllegalThreadStateException e) {
        // hmm, but child is still running.  go figure.
	extraInfo = "subprocess still running\n";
      };
      appendLogToJobLog("failure");
      mapRedFinished();
      throw new IOException(extraInfo + getContext() + io.getMessage());
    }
  }

  public void close() {
    appendLogToJobLog("success");
    mapRedFinished();
  }

  @Override
  public byte[] getInputSeparator() {
    return reduceInputFieldSeparator;
  }

  @Override
  public byte[] getFieldSeparator() {
    return reduceOutFieldSeparator;
  }
  
  @Override
  public int getNumOfKeyFields() {
    return numOfReduceOutputKeyFields;
  }
  
  @Override
  InputWriter createInputWriter() throws IOException {
    return super.createInputWriter(reduceInputWriterClass_);
  }

  @Override
  OutputReader createOutputReader() throws IOException {
    return super.createOutputReader(reduceOutputReaderClass_);
  }

}
