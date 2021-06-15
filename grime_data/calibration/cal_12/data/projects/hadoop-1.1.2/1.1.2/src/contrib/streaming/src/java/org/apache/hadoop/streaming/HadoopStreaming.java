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

import java.util.Arrays;

import org.apache.hadoop.util.ToolRunner;

/** The main entrypoint. Usually invoked with the script bin/hadoopStreaming
 * or bin/hadoop har hadoop-streaming.jar args.
 * It passes all the args to StreamJob which handles all the arguments.
 */
public class HadoopStreaming {

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.err.println("No Arguments Given!");
      System.exit(1);
    }
    int returnStatus = 0;
    String cmd = args[0];
    String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);
    if (cmd.equalsIgnoreCase("dumptb")) {
      DumpTypedBytes dumptb = new DumpTypedBytes();
      returnStatus = ToolRunner.run(dumptb, remainingArgs);
    } else if (cmd.equalsIgnoreCase("loadtb")) {
      LoadTypedBytes loadtb = new LoadTypedBytes();
      returnStatus = ToolRunner.run(loadtb, remainingArgs);
    } else if (cmd.equalsIgnoreCase("streamjob")) {
      StreamJob job = new StreamJob();
      returnStatus = ToolRunner.run(job, remainingArgs);
    } else { // for backward compatibility
      StreamJob job = new StreamJob();
      returnStatus = ToolRunner.run(job, args);
    }
    if (returnStatus != 0) {
      System.err.println("Streaming Command Failed!");
      System.exit(returnStatus);
    }
  }
}
