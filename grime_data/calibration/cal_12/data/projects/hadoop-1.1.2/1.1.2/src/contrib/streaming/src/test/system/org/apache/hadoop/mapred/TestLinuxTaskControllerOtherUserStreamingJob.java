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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.test.system.MRCluster;
import org.apache.hadoop.mapreduce.test.system.JTProtocol;
import org.apache.hadoop.mapreduce.test.system.JTClient;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.examples.SleepJob;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.streaming.StreamJob;

/**
 * Verifying the job submissions with original user and other user
 * with Linux task Controller.Original user should succeed and other
 * user should not.
 */
public class TestLinuxTaskControllerOtherUserStreamingJob {
  private static final Log LOG = LogFactory.
      getLog(TestLinuxTaskControllerOtherUserStreamingJob.class);
  private static MRCluster cluster = null;
  private static JobClient jobClient = null;
  private static JTProtocol remoteJTClient = null;
  private static JTClient jtClient = null;
  private static Configuration conf = new Configuration();
  private static Path inputDir = new Path("input");
  private static Path outputDir = new Path("output");

  @BeforeClass
  public static void before() throws Exception {
    cluster = MRCluster.createCluster(conf);
    cluster.setUp();
    jtClient = cluster.getJTClient();
    jobClient = jtClient.getClient();
    remoteJTClient = cluster.getJTClient().getProxy();
    conf = remoteJTClient.getDaemonConf();
  }

  @AfterClass
  public static void after() throws Exception {
    cluster.tearDown();
    cleanup(inputDir, conf);
    cleanup(outputDir, conf);
  }
  /**
   * Submit a Streaming job with a correct user and verify it passes
   * Submit a streaming job as diferent user and verify it fails
   * @param none
   * @return void
   */
  @Test
  public void testStreamingJobSameAndDifferentUser() throws Exception {
    executeStreamingJob(true);
    executeStreamingJob(false);
  }

  //Executes streaming job as original user or other user.
  private void executeStreamingJob(boolean sameUser) throws Exception {
      conf = cluster.getConf();
    if (sameUser == true) {
      UserGroupInformation ugi = UserGroupInformation.getLoginUser();
      LOG.info("LoginUser:" + ugi);
    } else {
      conf.set("user.name","hadoop1");
      LOG.info("user name changed is :" + conf.get("user.name"));
    }

    if (conf.get("mapred.task.tracker.task-controller").
        equals("org.apache.hadoop.mapred.LinuxTaskController")) {
      StreamJob streamJob = new StreamJob();
      String shellFile = System.getProperty("user.dir") +
        "/src/test/system/scripts/StreamMapper.sh";
      String runtimeArgs [] = {
        "-D", "mapred.job.name=Streaming job",
        "-D", "mapred.map.tasks=1",
        "-D", "mapred.reduce.tasks=1",
        "-D", "mapred.tasktracker.tasks.sleeptime-before-sigkill=3000",
        "-input", inputDir.toString(),
        "-output", outputDir.toString(),
        "-mapper", "StreamMapper.sh",
        "-reducer","/bin/cat",
        "-file", shellFile
      };

      createInput(inputDir, conf);
      cleanup(outputDir, conf);

      //If job submtitted with same user, it should pass
      //If job submitted with different user, it should fail with assertion
      //the testcase should assert.
      if (sameUser == true) {
        Assert.assertEquals(0, ToolRunner.run(conf, streamJob, runtimeArgs));
      } else {
        if ((ToolRunner.run(conf, streamJob, runtimeArgs)) != 0) {
          LOG.info("Job failed as expected");
        } else {
          Assert.fail("Job passed with different user");
        }
      }
    }
  }

  //Create Input directory in dfs
  private static void createInput(Path inDir, Configuration conf)
      throws IOException {
    FileSystem fs = inDir.getFileSystem(conf);
    if (!fs.mkdirs(inDir)) {
      throw new IOException("Failed to create the input directory:"
          + inDir.toString());
    }
    fs.setPermission(inDir, new FsPermission(FsAction.ALL,
        FsAction.ALL, FsAction.ALL));
    DataOutputStream file = fs.create(new Path(inDir, "data.txt"));
    int i = 0;
    while(i++ < 200) {
      file.writeBytes(i + "\n");
    }
    file.close();
  }

  //Cleanup directories in dfs.
  private static void cleanup(Path dir, Configuration conf)
      throws IOException {
    FileSystem fs = dir.getFileSystem(conf);
    fs.delete(dir, true);
  }

}
