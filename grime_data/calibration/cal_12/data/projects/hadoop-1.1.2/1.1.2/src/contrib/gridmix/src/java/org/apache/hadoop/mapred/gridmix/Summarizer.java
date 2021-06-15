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
package org.apache.hadoop.mapred.gridmix;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.gridmix.GenerateData.DataStatistics;

/**
 * Summarizes various aspects of a {@link Gridmix} run.
 */
class Summarizer {
  private ExecutionSummarizer executionSummarizer;
  private ClusterSummarizer clusterSummarizer;
  protected static final String NA = "N/A";
  
  Summarizer() {
    this(new String[]{NA});
  }
  
  Summarizer(String[] args) {
    executionSummarizer = new ExecutionSummarizer(args);
    clusterSummarizer = new ClusterSummarizer();
  }
  
  ExecutionSummarizer getExecutionSummarizer() {
    return executionSummarizer;
  }
  
  ClusterSummarizer getClusterSummarizer() {
    return clusterSummarizer;
  }
  
  void start(Configuration conf) {
    executionSummarizer.start(conf);
    clusterSummarizer.start(conf);
  }
  
  /**
   * This finalizes the summarizer.
   */
  @SuppressWarnings("unchecked")
  void finalize(JobFactory factory, String path, long size, 
                UserResolver resolver, DataStatistics stats, Configuration conf)
  throws IOException {
    executionSummarizer.finalize(factory, path, size, resolver, stats, conf);
  }
  
  /**
   * Summarizes the current {@link Gridmix} run and the cluster used. 
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(executionSummarizer.toString());
    builder.append(clusterSummarizer.toString());
    return builder.toString();
  }
}