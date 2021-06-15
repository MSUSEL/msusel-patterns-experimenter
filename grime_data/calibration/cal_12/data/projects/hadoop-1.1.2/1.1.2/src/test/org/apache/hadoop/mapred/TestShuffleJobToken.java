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


import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;

import org.apache.hadoop.http.HttpServer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.security.SecureShuffleUtils;
import org.apache.hadoop.mapreduce.security.token.JobTokenIdentifier;
import org.apache.hadoop.mapreduce.security.token.JobTokenSecretManager;
import org.apache.hadoop.security.token.Token;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class TestShuffleJobToken {
  private static HttpServer server;
  private static URL baseUrl;
  private static File dir;
  private static final String JOB_ID = "job_20091117075357176_0001";
  private static final String BAD_JOB_ID = "job_20091117075357176_0002";
  
  // create fake url
  private URL getMapOutputURL(String host)  throws MalformedURLException {
    // Get the base url
    StringBuffer url = new StringBuffer(host);
    url.append("mapOutput?");
    url.append("job=" + JOB_ID + "&");
    url.append("reduce=0&");
    url.append("map=attempt");

    return new URL(url.toString());
  }

  @Before
  public void setUp() throws Exception {
    dir = new File(System.getProperty("build.webapps", "build/webapps") + "/test");
    System.out.println("dir="+dir.getAbsolutePath());
    if(!dir.exists()) {
      assertTrue(dir.mkdirs());
    }
    server = new HttpServer("test", "0.0.0.0", 0, true);
    server.addServlet("shuffle", "/mapOutput", TaskTracker.MapOutputServlet.class);
    server.start();
    int port = server.getPort();
    baseUrl = new URL("http://localhost:" + port + "/");
  }

  @After
  public void tearDown() throws Exception {
    if(dir.exists())
      dir.delete();
    if(server!=null)
      server.stop();
  }

  
  /**
   * try positive and negative case with invalid urlHash
   */
  @Test
  public void testInvalidJobToken()
  throws IOException, GeneralSecurityException {
    
    URL url = getMapOutputURL(baseUrl.toString());
    String enc_str = SecureShuffleUtils.buildMsgFrom(url);
    URLConnection connectionGood = url.openConnection();
    
    TaskTracker tt  = new TaskTracker();
    JobTokenSecretManager jtSecretManager = new JobTokenSecretManager();
    // create fake TaskTracker - needed for keys storage
    JobTokenIdentifier identifier = new JobTokenIdentifier(new Text(JOB_ID));
    Token<JobTokenIdentifier> jt = new Token<JobTokenIdentifier>(identifier,
        jtSecretManager);
    SecretKey tokenSecret = JobTokenSecretManager.createSecretKey(jt.getPassword());
    addJobToken(tt, JOB_ID, jt); // fake id
    server.setAttribute("task.tracker", tt);

    // encode the url
    String urlHashGood = SecureShuffleUtils.generateHash(enc_str.getBytes(), tokenSecret); // valid hash
    
    // another the key
    JobTokenIdentifier badIdentifier = new JobTokenIdentifier(new Text(BAD_JOB_ID));
    Token<JobTokenIdentifier> badToken = new Token<JobTokenIdentifier>(badIdentifier,
        jtSecretManager);
    SecretKey badSecret = JobTokenSecretManager.createSecretKey(badToken.getPassword());
    String urlHashBad = SecureShuffleUtils.generateHash(enc_str.getBytes(), badSecret); // invalid hash 
    
    // put url hash into http header
    connectionGood.addRequestProperty(SecureShuffleUtils.HTTP_HEADER_URL_HASH, urlHashGood);
    
    // valid url hash should not fail with security error
    try {
      connectionGood.getInputStream();
    } catch (IOException ie) {
      String msg = ie.getLocalizedMessage();
      if(msg.contains("Server returned HTTP response code: 401 for URL:")) {
        fail("securtity failure with valid urlHash:"+ie);
      }
      System.out.println("valid urlhash passed validation");
    } 
    // invalid url hash
    URLConnection connectionBad = url.openConnection();
    connectionBad.addRequestProperty(SecureShuffleUtils.HTTP_HEADER_URL_HASH, urlHashBad);
    
    try {
      connectionBad.getInputStream();
      fail("Connection should've failed because of invalid urlHash");
    } catch (IOException ie) {
      String msg = ie.getLocalizedMessage();
      if(!msg.contains("Server returned HTTP response code: 401 for URL:")) {
        fail("connection failed with other then validation error:"+ie);
      }
      System.out.println("validation worked, failed with:"+ie);
    } 
  }
  /*Note that this method is there for a unit testcase (TestShuffleJobToken)*/
  void addJobToken(TaskTracker tt, String jobIdStr, Token<JobTokenIdentifier> token) {
    JobID jobId = JobID.forName(jobIdStr);
    TaskTracker.RunningJob rJob = new TaskTracker.RunningJob(jobId);
    synchronized (tt.runningJobs) {
      tt.runningJobs.put(jobId, rJob);
    }
    tt.getJobTokenSecretManager().addTokenForJob(jobIdStr, token);
  }

}
