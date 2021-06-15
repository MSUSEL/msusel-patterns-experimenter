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
package org.apache.hadoop.hdfs.server.namenode;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.util.Daemon;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

public class TestLeaseManager extends TestCase {
  public static final Log LOG = LogFactory.getLog(TestLeaseManager.class);

  /*
   * test case: two leases are added for a singler holder, should use
   * the internalReleaseOne method
   */
  public void testMultiPathLeaseRecovery()
    throws IOException, InterruptedException {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = new MiniDFSCluster(conf, 3, true, null);
    NameNode namenode = cluster.getNameNode();
    FSNamesystem spyNamesystem = spy(namenode.getNamesystem());
    LeaseManager leaseManager = new LeaseManager(spyNamesystem);
    
    spyNamesystem.leaseManager = leaseManager;
    spyNamesystem.lmthread.interrupt();
    
    String holder = "client-1";
    String path1 = "/file-1";
    String path2 = "/file-2";
    
    CalledAnswer internalReleaseCalled = new CalledAnswer();
    CalledAnswer internalReleaseOneCalled = new CalledAnswer();
    doAnswer(internalReleaseCalled)
        .when(spyNamesystem)
        .internalReleaseLease((LeaseManager.Lease) anyObject(), anyString());
    doAnswer(internalReleaseOneCalled)
        .when(spyNamesystem)
        .internalReleaseLeaseOne((LeaseManager.Lease) anyObject(), anyString());
    
    leaseManager.setLeasePeriod(1, 2);
    leaseManager.addLease(holder, path1);
    leaseManager.addLease(holder, path2);
    Thread.sleep(1000);

    synchronized (spyNamesystem) { // checkLeases is always called with FSN lock
      leaseManager.checkLeases();
    }
    
    assertTrue("internalReleaseOne not called", internalReleaseOneCalled.isCalled());
    assertFalse("internalRelease called", internalReleaseCalled.isCalled());
  }
  
  private static class CalledAnswer<T> implements Answer<T>{
    private volatile boolean called = false;

    @Override
    public T answer(InvocationOnMock invocationOnMock) throws Throwable {
      called = true;
      
      return (T)invocationOnMock.callRealMethod();
    }

    public boolean isCalled() {
      return called;
    }
  }
}