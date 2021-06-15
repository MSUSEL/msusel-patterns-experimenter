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
package org.apache.hadoop.tools.rumen;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

/**
 * Producing {@link JobStory}s from job trace.
 */
public class ZombieJobProducer implements JobStoryProducer {
  private final JobTraceReader reader;
  private final ZombieCluster cluster;

  private boolean hasRandomSeed = false;
  private long randomSeed = 0;
      
  private ZombieJobProducer(JobTraceReader reader, ZombieCluster cluster,
      boolean hasRandomSeed, long randomSeed) {
    this.reader = reader;
    this.cluster = cluster;
    this.hasRandomSeed = hasRandomSeed;
    this.randomSeed = (hasRandomSeed) ? randomSeed : System.nanoTime();
  }

  /**
   * Constructor
   * 
   * @param path
   *          Path to the JSON trace file, possibly compressed.
   * @param cluster
   *          The topology of the cluster that corresponds to the jobs in the
   *          trace. The argument can be null if we do not have knowledge of the
   *          cluster topology.
   * @param conf
   * @throws IOException
   */
  public ZombieJobProducer(Path path, ZombieCluster cluster, Configuration conf)
      throws IOException {
    this(new JobTraceReader(path, conf), cluster, false, -1);
  }

  
  /**
   * Constructor
   * 
   * @param path
   *          Path to the JSON trace file, possibly compressed.
   * @param cluster
   *          The topology of the cluster that corresponds to the jobs in the
   *          trace. The argument can be null if we do not have knowledge of the
   *          cluster topology.
   * @param conf
   * @param randomSeed
   *          use a deterministic seed.
   * @throws IOException
   */
  public ZombieJobProducer(Path path, ZombieCluster cluster,
      Configuration conf, long randomSeed) throws IOException {
    this(new JobTraceReader(path, conf), cluster, true, randomSeed);
  }
  
  /**
   * Constructor
   * 
   * @param input
   *          The input stream for the JSON trace.
   * @param cluster
   *          The topology of the cluster that corresponds to the jobs in the
   *          trace. The argument can be null if we do not have knowledge of the
   *          cluster topology.
   * @throws IOException
   */
  public ZombieJobProducer(InputStream input, ZombieCluster cluster)
      throws IOException {
    this(new JobTraceReader(input), cluster, false, -1);
  }

  /**
   * Constructor
   * 
   * @param input
   *          The input stream for the JSON trace.
   * @param cluster
   *          The topology of the cluster that corresponds to the jobs in the
   *          trace. The argument can be null if we do not have knowledge of the
   *          cluster topology.
   * @param randomSeed
   *          use a deterministic seed.
   * @throws IOException
   */
  public ZombieJobProducer(InputStream input, ZombieCluster cluster,
      long randomSeed) throws IOException {
    this(new JobTraceReader(input), cluster, true, randomSeed);
  }

  @Override
  public ZombieJob getNextJob() throws IOException {
    LoggedJob job = reader.getNext();
    if (job == null) {
      return null;
    } else if (hasRandomSeed) {
      long subRandomSeed = RandomSeedGenerator.getSeed(
            "forZombieJob" + job.getJobID(), randomSeed);
      return new ZombieJob(job, cluster, subRandomSeed);
    } else {
      return new ZombieJob(job, cluster);
    }
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }
}
