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

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.tools.rumen.JobStoryProducer;
import org.apache.hadoop.mapred.gridmix.Statistics.JobStats;
import org.apache.hadoop.mapred.gridmix.Statistics.ClusterStats;

import java.util.concurrent.CountDownLatch;
import java.io.IOException;

enum GridmixJobSubmissionPolicy {

  REPLAY("REPLAY",320000) {
    @Override
    public JobFactory<ClusterStats> createJobFactory(
      JobSubmitter submitter, JobStoryProducer producer, Path scratchDir,
      Configuration conf, CountDownLatch startFlag, UserResolver userResolver)
      throws IOException {
      return new ReplayJobFactory(
        submitter, producer, scratchDir, conf, startFlag,userResolver);
    }},

  STRESS("STRESS",5000) {
    @Override
    public JobFactory<ClusterStats> createJobFactory(
      JobSubmitter submitter, JobStoryProducer producer, Path scratchDir,
      Configuration conf, CountDownLatch startFlag, UserResolver userResolver)
      throws IOException {
      return new StressJobFactory(
        submitter, producer, scratchDir, conf, startFlag,userResolver);
    }},

  SERIAL("SERIAL",0) {
    @Override
    public JobFactory<JobStats> createJobFactory(
      JobSubmitter submitter, JobStoryProducer producer, Path scratchDir,
      Configuration conf, CountDownLatch startFlag, UserResolver userResolver)
      throws IOException {
      return new SerialJobFactory(
        submitter, producer, scratchDir, conf, startFlag,userResolver);
    }
  };

  public static final String JOB_SUBMISSION_POLICY =
    "gridmix.job-submission.policy";

  private final String name;
  private final int pollingInterval;

  GridmixJobSubmissionPolicy(String name,int pollingInterval) {
    this.name = name;
    this.pollingInterval = pollingInterval;
  }

  public abstract JobFactory createJobFactory(
    JobSubmitter submitter, JobStoryProducer producer, Path scratchDir,
    Configuration conf, CountDownLatch startFlag, UserResolver userResolver)
    throws IOException;

  public int getPollingInterval() {
    return pollingInterval;
  }

  public static GridmixJobSubmissionPolicy getPolicy(
    Configuration conf, GridmixJobSubmissionPolicy defaultPolicy) {
    String policy = conf.get(JOB_SUBMISSION_POLICY, defaultPolicy.name());
    return valueOf(policy.toUpperCase());
  }
}
