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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.After;

/**
 * A test-cluster based on {@link MiniMRCluster} that is started with
 * CapacityTaskScheduler. It provides knobs to configure both the cluster as
 * well as the scheduler. Any test that intends to test capacity-scheduler
 * should extend this.
 * 
 */
public class ClusterWithCapacityScheduler {

  static final Log LOG = LogFactory.getLog(ClusterWithCapacityScheduler.class);
  private MiniMRCluster mrCluster;

  private JobConf jobConf;

  static final String MY_SCHEDULER_CONF_PATH_PROPERTY = "my.resource.path";

  protected void startCluster()
      throws IOException {
    startCluster(null, null);
  }

  /**
   * Start the cluster with two TaskTrackers and two DataNodes and configure the
   * cluster with clusterProperties and the scheduler with schedulerProperties.
   * Uses default configuration whenever user provided properties are missing
   * (null/empty)
   * 
   * @param clusterProperties
   * @param schedulerProperties
   * @throws IOException
   */
  protected void startCluster(Properties clusterProperties,
      Properties schedulerProperties)
      throws IOException {
    startCluster(2, clusterProperties, schedulerProperties);
  }

  /**
   * Start the cluster with numTaskTrackers TaskTrackers and numDataNodes
   * DataNodes and configure the cluster with clusterProperties and the
   * scheduler with schedulerProperties. Uses default configuration whenever
   * user provided properties are missing (null/empty)
   * 
   * @param numTaskTrackers
   * @param clusterProperties
   * @param schedulerProperties
   * @throws IOException
   */
  protected void startCluster(int numTaskTrackers,
      Properties clusterProperties, Properties schedulerProperties)
      throws IOException {
    Thread.currentThread().setContextClassLoader(
        new ClusterWithCapacityScheduler.MyClassLoader());
    JobConf clusterConf = new JobConf();
    if (clusterProperties != null) {
      for (Enumeration<?> e = clusterProperties.propertyNames(); e
          .hasMoreElements();) {
        String key = (String) e.nextElement();
        clusterConf.set(key, (String) clusterProperties.get(key));
      }
    }

    if (schedulerProperties != null) {
      setUpSchedulerConfigFile(schedulerProperties);
    }

    clusterConf.set("mapred.jobtracker.taskScheduler",
        CapacityTaskScheduler.class.getName());
    mrCluster =
        new MiniMRCluster(numTaskTrackers, "file:///", 1, null, null,
            clusterConf);

    this.jobConf = mrCluster.createJobConf(clusterConf);
  }

  private void setUpSchedulerConfigFile(Properties schedulerConfProps)
      throws IOException {
    LocalFileSystem fs = FileSystem.getLocal(new Configuration());

    String myResourcePath = System.getProperty("test.build.data");
    Path schedulerConfigFilePath =
        new Path(myResourcePath, CapacitySchedulerConf.SCHEDULER_CONF_FILE);
    OutputStream out = fs.create(schedulerConfigFilePath);

    Configuration config = new Configuration(false);
    for (Enumeration<?> e = schedulerConfProps.propertyNames(); e
        .hasMoreElements();) {
      String key = (String) e.nextElement();
      LOG.debug("Adding " + key + schedulerConfProps.getProperty(key));
      config.set(key, schedulerConfProps.getProperty(key));
    }

    config.writeXml(out);
    out.close();

    LOG.info("setting resource path where capacity-scheduler's config file "
        + "is placed to " + myResourcePath);
    System.setProperty(MY_SCHEDULER_CONF_PATH_PROPERTY, myResourcePath);
  }

  private void cleanUpSchedulerConfigFile() throws IOException {
    LocalFileSystem fs = FileSystem.getLocal(new Configuration());

    String myResourcePath = System.getProperty("test.build.data");
    Path schedulerConfigFilePath =
        new Path(myResourcePath, CapacitySchedulerConf.SCHEDULER_CONF_FILE);
    fs.delete(schedulerConfigFilePath, false);
  }

  protected JobConf getJobConf() {
    return new JobConf(this.jobConf);
  }

  protected JobTracker getJobTracker() {
    return this.mrCluster.getJobTrackerRunner().getJobTracker();
  }

  @After
  public void tearDown()
      throws Exception {
    cleanUpSchedulerConfigFile();
    
    if (mrCluster != null) {
      mrCluster.shutdown();
    }
  }

  /**
   * Wait till all the slots in the cluster are occupied with respect to the
   * tasks of type specified isMap.
   * 
   * <p>
   * 
   * <b>Also, it is assumed that the tasks won't finish any time soon, like in
   * the case of tasks of {@link ControlledMapReduceJob}</b>.
   * 
   * @param isMap
   */
  protected void waitTillAllSlotsAreOccupied(boolean isMap)
      throws InterruptedException {
    JobTracker jt = this.mrCluster.getJobTrackerRunner().getJobTracker();
    ClusterStatus clusterStatus = jt.getClusterStatus();
    int currentTasks =
        (isMap ? clusterStatus.getMapTasks() : clusterStatus.getReduceTasks());
    int maxTasks =
        (isMap ? clusterStatus.getMaxMapTasks() : clusterStatus
            .getMaxReduceTasks());
    while (currentTasks != maxTasks) {
      Thread.sleep(1000);
      clusterStatus = jt.getClusterStatus();
      currentTasks =
          (isMap ? clusterStatus.getMapTasks() : clusterStatus
              .getReduceTasks());
      maxTasks =
          (isMap ? clusterStatus.getMaxMapTasks() : clusterStatus
              .getMaxReduceTasks());
      LOG.info("Waiting till cluster reaches steady state. currentTasks : "
          + currentTasks + " total cluster capacity : " + maxTasks);
    }
  }

  /**
   * @return the mrCluster
   */
  public MiniMRCluster getMrCluster() {
    return mrCluster;
  }

  static class MyClassLoader extends ClassLoader {
    @Override
    public URL getResource(String name) {
      if (!name.equals(CapacitySchedulerConf.SCHEDULER_CONF_FILE)) {
        return super.getResource(name);
      }
      return findResource(name);
    }

    @Override
    protected URL findResource(String name) {
      try {
        String resourcePath =
            System
                .getProperty(ClusterWithCapacityScheduler.MY_SCHEDULER_CONF_PATH_PROPERTY);
        // Check the resourcePath directory
        File file = new File(resourcePath, name);
        if (file.exists()) {
          return new URL("file://" + file.getAbsolutePath());
        }
      } catch (MalformedURLException mue) {
        LOG.warn("exception : " + mue);
      }
      return super.findResource(name);
    }
  }
}
