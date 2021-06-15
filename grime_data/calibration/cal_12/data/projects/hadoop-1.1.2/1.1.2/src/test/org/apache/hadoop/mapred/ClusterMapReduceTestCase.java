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
package org.apache.hadoop.mapred;

import junit.framework.TestCase;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Test case to run a MapReduce job.
 * <p/>
 * It runs a 2 node cluster Hadoop with a 2 node DFS.
 * <p/>
 * The JobConf to use must be obtained via the creatJobConf() method.
 * <p/>
 * It creates a temporary directory -accessible via getTestRootDir()-
 * for both input and output.
 * <p/>
 * The input directory is accesible via getInputDir() and the output
 * directory via getOutputDir()
 * <p/>
 * The DFS filesystem is formated before the testcase starts and after it ends.
 */
public abstract class ClusterMapReduceTestCase extends TestCase {
  private MiniDFSCluster dfsCluster = null;
  private MiniMRCluster mrCluster = null;

  /**
   * Creates Hadoop Cluster and DFS before a test case is run.
   *
   * @throws Exception
   */
  protected void setUp() throws Exception {
    super.setUp();

    startCluster(true, null);
  }

  /**
   * Starts the cluster within a testcase with single mapred-local-dir per
   * TaskTracker.
   * <p/>
   * Note that the cluster is already started when the testcase method
   * is invoked. This method is useful if as part of the testcase the
   * cluster has to be shutdown and restarted again.
   * <p/>
   * If the cluster is already running this method does nothing.
   *
   * @param reformatDFS indicates if DFS has to be reformated
   * @param props configuration properties to inject to the mini cluster
   * @throws Exception if the cluster could not be started
   */
  protected synchronized void startCluster(boolean reformatDFS,
      Properties props) throws Exception {
    startCluster(reformatDFS, props, 1);
  }

  /**
   * Starts the cluster within a testcase with the given number of
   * mapred-local-dirs per TaskTracker.
   * <p/>
   * Note that the cluster is already started when the testcase method
   * is invoked. This method is useful if as part of the testcase the
   * cluster has to be shutdown and restarted again.
   * <p/>
   * If the cluster is already running this method does nothing.
   * @param reformatDFS indicates if DFS has to be reformated
   * @param props configuration properties to inject to the mini cluster
   * @param numDir 
   * @throws Exception if the cluster could not be started
   */
  protected synchronized void startCluster(boolean reformatDFS,
        Properties props, int numDir) throws Exception {
      
    if (dfsCluster == null) {
      JobConf conf = new JobConf();
      if (props != null) {
        for (Map.Entry entry : props.entrySet()) {
          conf.set((String) entry.getKey(), (String) entry.getValue());
        }
      }
      dfsCluster = new MiniDFSCluster(conf, 2, reformatDFS, null);

      ConfigurableMiniMRCluster.setConfiguration(props);
      //noinspection deprecation
      mrCluster = new ConfigurableMiniMRCluster(2, getFileSystem().getName(),
                                                numDir, conf);
    }
  }

  private static class ConfigurableMiniMRCluster extends MiniMRCluster {
    private static Properties config;

    public static void setConfiguration(Properties props) {
      config = props;
    }

    public ConfigurableMiniMRCluster(int numTaskTrackers, String namenode,
                                     int numDir, JobConf conf)
        throws Exception {
      super(0,0, numTaskTrackers, namenode, numDir, null, null, null, conf);
    }

    public JobConf createJobConf() {
      JobConf conf = super.createJobConf();
      if (config != null) {
        for (Map.Entry entry : config.entrySet()) {
          conf.set((String) entry.getKey(), (String) entry.getValue());
        }
      }
      return conf;
    }
  }

  /**
   * Stops the cluster within a testcase.
   * <p/>
   * Note that the cluster is already started when the testcase method
   * is invoked. This method is useful if as part of the testcase the
   * cluster has to be shutdown.
   * <p/>
   * If the cluster is already stopped this method does nothing.
   *
   * @throws Exception if the cluster could not be stopped
   */
  protected void stopCluster() throws Exception {
    if (mrCluster != null) {
      mrCluster.shutdown();
      mrCluster = null;
    }
    if (dfsCluster != null) {
      dfsCluster.shutdown();
      dfsCluster = null;
    }
  }

  /**
   * Destroys Hadoop Cluster and DFS after a test case is run.
   *
   * @throws Exception
   */
  protected void tearDown() throws Exception {
    stopCluster();
    super.tearDown();
  }

  /**
   * Returns a preconfigured Filesystem instance for test cases to read and
   * write files to it.
   * <p/>
   * TestCases should use this Filesystem instance.
   *
   * @return the filesystem used by Hadoop.
   * @throws IOException 
   */
  protected FileSystem getFileSystem() throws IOException {
    return dfsCluster.getFileSystem();
  }

  protected MiniMRCluster getMRCluster() {
    return mrCluster;
  }

  /**
   * Returns the path to the root directory for the testcase.
   *
   * @return path to the root directory for the testcase.
   */
  protected Path getTestRootDir() {
    return new Path("x").getParent();
  }

  /**
   * Returns a path to the input directory for the testcase.
   *
   * @return path to the input directory for the tescase.
   */
  protected Path getInputDir() {
    return new Path("input");
  }

  /**
   * Returns a path to the output directory for the testcase.
   *
   * @return path to the output directory for the tescase.
   */
  protected Path getOutputDir() {
    return new Path("output");
  }

  /**
   * Returns a job configuration preconfigured to run against the Hadoop
   * managed by the testcase.
   *
   * @return configuration that works on the testcase Hadoop instance
   */
  protected JobConf createJobConf() {
    return mrCluster.createJobConf();
  }

}
