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

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.tools.rumen.JobStory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@InterfaceAudience.Private
@InterfaceStability.Evolving
public enum JobCreator {

  LOADJOB {
    @Override
    public GridmixJob createGridmixJob(
      Configuration gridmixConf, long submissionMillis, JobStory jobdesc,
      Path outRoot, UserGroupInformation ugi, int seq) throws IOException {

      // Build configuration for this simulated job
      Configuration conf = new Configuration(gridmixConf);
      dce.configureDistCacheFiles(conf, jobdesc.getJobConf());
      return new LoadJob(conf, submissionMillis, jobdesc, outRoot, ugi, seq);
    }

    @Override
    public boolean canEmulateDistCacheLoad() {
      return true;
    }
  },

  SLEEPJOB {
    private String[] hosts;
      
    @Override
    public GridmixJob createGridmixJob(
      Configuration conf, long submissionMillis, JobStory jobdesc, Path outRoot,
      UserGroupInformation ugi, int seq) throws IOException {
      int numLocations = conf.getInt(SLEEPJOB_RANDOM_LOCATIONS, 0);
      if (numLocations < 0) numLocations=0;
      if ((numLocations > 0) && (hosts == null)) {
        final JobClient client = new JobClient(new JobConf(conf));
        ClusterStatus stat = client.getClusterStatus(true);
        final int nTrackers = stat.getTaskTrackers();
        final ArrayList<String> hostList = new ArrayList<String>(nTrackers);
        final Pattern trackerPattern = Pattern.compile("tracker_([^:]*):.*");
        final Matcher m = trackerPattern.matcher("");
        for (String tracker : stat.getActiveTrackerNames()) {
          m.reset(tracker);
          if (!m.find()) {
            continue;
          }
          final String name = m.group(1);
          hostList.add(name);
        }
        hosts = hostList.toArray(new String[hostList.size()]);
      }
      return new SleepJob(conf, submissionMillis, jobdesc, outRoot, ugi, seq,
          numLocations, hosts);
    }

    @Override
    public boolean canEmulateDistCacheLoad() {
      return false;
    }
  };

  public static final String GRIDMIX_JOB_TYPE = "gridmix.job.type";
  public static final String SLEEPJOB_RANDOM_LOCATIONS = 
    "gridmix.sleep.fake-locations";

  /**
   * Create Gridmix simulated job.
   * @param conf configuration of simulated job
   * @param submissionMillis At what time submission of this simulated job be
   *                         done
   * @param jobdesc JobStory obtained from trace
   * @param outRoot gridmix output directory
   * @param ugi UGI of job submitter of this simulated job
   * @param seq job sequence number
   * @return the created simulated job
   * @throws IOException
   */
  public abstract GridmixJob createGridmixJob(
    final Configuration conf, long submissionMillis, final JobStory jobdesc,
    Path outRoot, UserGroupInformation ugi, final int seq) throws IOException;

  public static JobCreator getPolicy(
    Configuration conf, JobCreator defaultPolicy) {
    return conf.getEnum(GRIDMIX_JOB_TYPE, defaultPolicy);
  }

  /**
   * @return true if gridmix simulated jobs of this job type can emulate
   *         distributed cache load
   */
  abstract boolean canEmulateDistCacheLoad();

  DistributedCacheEmulator dce;
  /**
   * This method is to be called before calling any other method in JobCreator
   * except canEmulateDistCacheLoad(), especially if canEmulateDistCacheLoad()
   * returns true for that job type.
   * @param e Distributed Cache Emulator
   */
  void setDistCacheEmulator(DistributedCacheEmulator e) {
    this.dce = e;
  }
}
