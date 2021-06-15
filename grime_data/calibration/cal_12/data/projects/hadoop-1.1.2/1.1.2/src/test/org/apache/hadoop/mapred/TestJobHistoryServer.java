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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.mapred.lib.NullOutputFormat;
import org.junit.Assert;
import junit.framework.TestCase;

import java.io.IOException;
import java.net.HttpURLConnection;

public class TestJobHistoryServer extends TestCase {
  private static final Log LOG = LogFactory.getLog(TestJobHistoryServer.class);
  private String inputPath = System.getProperty("test.build.data",
      "build/test/data") + "/TestJobHistoryServer";
  
  public void testHistoryServerEmbedded() {

    MiniMRCluster mrCluster = null;
    JobConf conf = new JobConf();
    try {
      conf.setLong("mapred.job.tracker.retiredjobs.cache.size", 1);
      conf.setLong("mapred.jobtracker.retirejob.interval", 0);
      conf.setLong("mapred.jobtracker.retirejob.check", 0);
      conf.setLong("mapred.jobtracker.completeuserjobs.maximum", 2);
      conf.set(JobHistoryServer.MAPRED_HISTORY_SERVER_HTTP_ADDRESS,
          "localhost:0");

      mrCluster = new MiniMRCluster(1, conf.get("fs.default.name"), 1,
          null, null, conf);
      String historyAddress = JobHistoryServer.getHistoryUrlPrefix(mrCluster.
          getJobTrackerRunner().getJobTracker().conf);
      LOG.info("******** History Address: " + historyAddress);

      conf = mrCluster.createJobConf();
      createInputFile(conf, inputPath);

      RunningJob job = runJob(conf);
      LOG.info("Job details: " + job);

      String redirectUrl = getRedirectUrl(job.getTrackingURL());
      Assert.assertEquals(redirectUrl.contains(historyAddress), true);

    } catch (IOException e) {
      LOG.error("Failure running test", e);
      Assert.fail(e.getMessage());
    } catch (InterruptedException e) {
      LOG.error("Exit due to being interrupted");
      Assert.fail(e.getMessage());
    } finally {
      if (mrCluster != null) mrCluster.shutdown();
    }
  }

  public void testHistoryServerStandalone() {

    MiniMRCluster mrCluster = null;
    JobConf conf = new JobConf();
    JobHistoryServer server = null;
    try {
      conf.setLong("mapred.job.tracker.retiredjobs.cache.size", 1);
      conf.setLong("mapred.jobtracker.retirejob.interval", 0);
      conf.setLong("mapred.jobtracker.retirejob.check", 0);
      conf.setLong("mapred.jobtracker.completeuserjobs.maximum", 2);
      conf.set(JobHistoryServer.MAPRED_HISTORY_SERVER_HTTP_ADDRESS,
          "localhost:8090");
      conf.setBoolean(JobHistoryServer.MAPRED_HISTORY_SERVER_EMBEDDED, false);

      mrCluster = new MiniMRCluster(1, conf.get("fs.default.name"), 1,
          null, null, conf);
      server = new JobHistoryServer(conf);
      server.start();

      String historyAddress = JobHistoryServer.getHistoryUrlPrefix(conf);
      LOG.info("******** History Address: " + historyAddress);

      conf = mrCluster.createJobConf();
      createInputFile(conf, inputPath);

      RunningJob job = runJob(conf);
      LOG.info("Job details: " + job);

      String redirectUrl = getRedirectUrl(job.getTrackingURL());
      Assert.assertEquals(redirectUrl.contains(historyAddress), true);

    } catch (IOException e) {
      LOG.error("Failure running test", e);
      Assert.fail(e.getMessage());
    } catch (InterruptedException e) {
      LOG.error("Exit due to being interrupted");
      Assert.fail(e.getMessage());
    } finally {
      if (mrCluster != null) mrCluster.shutdown();
      try {
        if (server != null) server.shutdown();
      } catch (Exception ignore) { }
    }
  }

  private void createInputFile(Configuration conf, String path)
      throws IOException {
    FileSystem fs = FileSystem.get(conf);
    FSDataOutputStream out = fs.create(new Path(path));
    try {
      out.write("hello world".getBytes());
    } finally {
      out.close();
    }
  }

  private synchronized RunningJob runJob(JobConf conf) throws IOException {
    conf.setJobName("History");

    conf.setInputFormat(TextInputFormat.class);

    conf.setMapOutputKeyClass(LongWritable.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputFormat(NullOutputFormat.class);
    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(org.apache.hadoop.mapred.lib.IdentityMapper.class);
    conf.setReducerClass(org.apache.hadoop.mapred.lib.IdentityReducer.class);

    FileInputFormat.setInputPaths(conf, inputPath);

    return JobClient.runJob(conf);
  }

  private String getRedirectUrl(String jobUrl) throws IOException, InterruptedException {
    HttpClient client = new HttpClient();
    GetMethod method = new GetMethod(jobUrl);
    method.setFollowRedirects(false);
    try {
      int status = client.executeMethod(method);
      if(status!=HttpURLConnection.HTTP_MOVED_TEMP) {
        int retryTimes = 4;
        for(int i = 1; i < retryTimes + 1; i++) {
          try {
            // Wait i sec
            Thread.sleep(i * 1000);
          } catch (InterruptedException e) {
            throw new InterruptedException("Exit due to being interrupted");
          }
          // Get the latest status
          status = client.executeMethod(method);
          if(status == HttpURLConnection.HTTP_MOVED_TEMP)
            break;
        }
      }

      Assert.assertEquals(status, HttpURLConnection.HTTP_MOVED_TEMP);

      LOG.info("Location: " + method.getResponseHeader("Location"));
      return method.getResponseHeader("Location").getValue();
    } finally {
      method.releaseConnection();
    }
  }

}
