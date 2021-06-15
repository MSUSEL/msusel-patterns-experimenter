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
package org.apache.hadoop.util;

import org.apache.hadoop.conf.Configurable;

/**
 * A tool interface that supports handling of generic command-line options.
 * 
 * <p><code>Tool</code>, is the standard for any Map-Reduce tool/application. 
 * The tool/application should delegate the handling of 
 * <a href="{@docRoot}/org/apache/hadoop/util/GenericOptionsParser.html#GenericOptions">
 * standard command-line options</a> to {@link ToolRunner#run(Tool, String[])} 
 * and only handle its custom arguments.</p>
 * 
 * <p>Here is how a typical <code>Tool</code> is implemented:</p>
 * <p><blockquote><pre>
 *     public class MyApp extends Configured implements Tool {
 *     
 *       public int run(String[] args) throws Exception {
 *         // <code>Configuration</code> processed by <code>ToolRunner</code>
 *         Configuration conf = getConf();
 *         
 *         // Create a JobConf using the processed <code>conf</code>
 *         JobConf job = new JobConf(conf, MyApp.class);
 *         
 *         // Process custom command-line options
 *         Path in = new Path(args[1]);
 *         Path out = new Path(args[2]);
 *         
 *         // Specify various job-specific parameters     
 *         job.setJobName("my-app");
 *         job.setInputPath(in);
 *         job.setOutputPath(out);
 *         job.setMapperClass(MyApp.MyMapper.class);
 *         job.setReducerClass(MyApp.MyReducer.class);
 *
 *         // Submit the job, then poll for progress until the job is complete
 *         JobClient.runJob(job);
 *       }
 *       
 *       public static void main(String[] args) throws Exception {
 *         // Let <code>ToolRunner</code> handle generic command-line options 
 *         int res = ToolRunner.run(new Configuration(), new Sort(), args);
 *         
 *         System.exit(res);
 *       }
 *     }
 * </pre></blockquote></p>
 * 
 * @see GenericOptionsParser
 * @see ToolRunner
 */
public interface Tool extends Configurable {
  /**
   * Execute the command with the given arguments.
   * 
   * @param args command specific arguments.
   * @return exit code.
   * @throws Exception
   */
  int run(String [] args) throws Exception;
}
