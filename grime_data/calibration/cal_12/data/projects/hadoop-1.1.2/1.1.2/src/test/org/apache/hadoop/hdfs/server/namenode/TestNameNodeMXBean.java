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

import static org.junit.Assert.*;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Map;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.server.namenode.FSNamesystem;
import org.apache.hadoop.util.VersionInfo;

import org.junit.Test;
import org.mortbay.util.ajax.JSON;

import junit.framework.Assert;

/**
 * Class for testing {@link NameNodeMXBean} implementation
 */
public class TestNameNodeMXBean {
  @SuppressWarnings({ "unchecked", "deprecation" })
  @Test
  public void testNameNodeMXBeanInfo() throws Exception {
    Configuration conf = new Configuration();
    MiniDFSCluster cluster = null;

    try {
      cluster = new MiniDFSCluster(conf, 1, true, null);
      cluster.waitActive();

      FSNamesystem fsn = cluster.getNameNode().namesystem;

      MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
      ObjectName mxbeanName = new ObjectName(
        "Hadoop:service=NameNode,name=NameNodeInfo");
      // get attribute "HostName"
      String hostname = (String) mbs.getAttribute(mxbeanName, "HostName");
      Assert.assertEquals(fsn.getHostName(), hostname);
      // get attribute "Version"
      String version = (String) mbs.getAttribute(mxbeanName, "Version");
      Assert.assertEquals(fsn.getVersion(), version);
      Assert.assertTrue(version.equals(VersionInfo.getVersion()
              + ", r" + VersionInfo.getRevision()));
      // get attribute "Used"
      Long used = (Long) mbs.getAttribute(mxbeanName, "Used");
      Assert.assertEquals(fsn.getUsed(), used.longValue());
      // get attribute "Total"
      Long total = (Long) mbs.getAttribute(mxbeanName, "Total");
      Assert.assertEquals(fsn.getTotal(), total.longValue());
      // get attribute "safemode"
      String safemode = (String) mbs.getAttribute(mxbeanName, "Safemode");
      Assert.assertEquals(fsn.getSafemode(), safemode);
      // get attribute nondfs
      Long nondfs = (Long) (mbs.getAttribute(mxbeanName, "NonDfsUsedSpace"));
      Assert.assertEquals(fsn.getNonDfsUsedSpace(), nondfs.longValue());
      // get attribute percentremaining
      Float percentremaining = (Float) (mbs.getAttribute(mxbeanName,
          "PercentRemaining"));
      Assert.assertEquals(fsn.getPercentRemaining(), percentremaining
          .floatValue());
      // get attribute Totalblocks
      Long totalblocks = (Long) (mbs.getAttribute(mxbeanName, "TotalBlocks"));
      Assert.assertEquals(fsn.getTotalBlocks(), totalblocks.longValue());
      // get attribute alivenodeinfo
      String alivenodeinfo = (String) (mbs.getAttribute(mxbeanName,
          "LiveNodes"));
      Assert.assertEquals(fsn.getLiveNodes(), alivenodeinfo);
      // get attribute deadnodeinfo
      String deadnodeinfo = (String) (mbs.getAttribute(mxbeanName,
          "DeadNodes"));
      Assert.assertEquals(fsn.getDeadNodes(), deadnodeinfo);
      // get attribute NameDirStatuses
      String nameDirStatuses = (String) (mbs.getAttribute(mxbeanName,
          "NameDirStatuses"));
      Assert.assertEquals(fsn.getNameDirStatuses(), nameDirStatuses);
      Map<String, Map<String, String>> statusMap =
        (Map<String, Map<String, String>>) JSON.parse(nameDirStatuses);
      Collection<File> nameDirs = cluster.getNameDirs();
      for (File nameDir : nameDirs) {
        System.out.println("Checking for the presence of " + nameDir +
            " in active name dirs.");
        assertTrue(statusMap.get("active").containsKey(nameDir.getAbsolutePath()));
      }
      assertEquals(2, statusMap.get("active").size());
      assertEquals(0, statusMap.get("failed").size());
      
      // This will cause the first dir to fail.
      File failedNameDir = nameDirs.toArray(new File[0])[0];
      assertEquals(0, FileUtil.chmod(failedNameDir.getAbsolutePath(), "000"));
      cluster.getNameNode().rollEditLog();
      
      nameDirStatuses = (String) (mbs.getAttribute(mxbeanName,
          "NameDirStatuses"));
      statusMap = (Map<String, Map<String, String>>) JSON.parse(nameDirStatuses);
      for (File nameDir : nameDirs) {
        String expectedStatus =
            nameDir.equals(failedNameDir) ? "failed" : "active";
        System.out.println("Checking for the presence of " + nameDir +
            " in " + expectedStatus + " name dirs.");
        assertTrue(statusMap.get(expectedStatus).containsKey(
            nameDir.getAbsolutePath()));
      }
      assertEquals(1, statusMap.get("active").size());
      assertEquals(1, statusMap.get("failed").size());
    } finally {
      if (cluster != null) {
        for (File dir : cluster.getNameDirs()) {
          FileUtil.chmod(dir.toString(), "700");
        }
        cluster.shutdown();
      }
    }
  }
}
