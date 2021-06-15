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
package org.apache.hadoop.mapred.gridmix.test.system;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.JobID;
import org.apache.hadoop.tools.rumen.ZombieJobProducer;
import org.apache.hadoop.tools.rumen.ZombieJob;
import org.apache.hadoop.conf.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Build the job stories with a given trace file. 
 */
public class GridmixJobStory {
  private static Log LOG = LogFactory.getLog(GridmixJobStory.class);
  private Path path;
  private Map<JobID, ZombieJob> zombieJobs;
  private Configuration conf;
  
  public GridmixJobStory(Path path, Configuration conf) {
    this.path = path;
    this.conf = conf;
    try {
       zombieJobs = buildJobStories();
       if(zombieJobs == null) {
          throw new NullPointerException("No jobs found in a " 
              + " given trace file.");
       }
    } catch (IOException ioe) {
      LOG.warn("Error:" + ioe.getMessage());
    } catch (NullPointerException npe) {
      LOG.warn("Error:" + npe.getMessage());
    }
  }
  
  /**
   * Get the zombie jobs as a map.
   * @return the zombie jobs map.
   */
  public Map<JobID, ZombieJob> getZombieJobs() {
    return zombieJobs;
  }
  
  /**
   * Get the zombie job of a given job id.
   * @param jobId - gridmix job id.
   * @return - the zombie job object.
   */
  public ZombieJob getZombieJob(JobID jobId) {
    return zombieJobs.get(jobId);
  }
  
  private Map<JobID, ZombieJob> buildJobStories() throws IOException {
    ZombieJobProducer zjp = new ZombieJobProducer(path,null, conf);
    Map<JobID, ZombieJob> hm = new HashMap<JobID, ZombieJob>();
    ZombieJob zj = zjp.getNextJob();
    while (zj != null) {
      hm.put(zj.getJobID(),zj);
      zj = zjp.getNextJob();
    }
    if (hm.size() == 0) {
      return null;
    } else {
      return hm;
    }
  }
}
