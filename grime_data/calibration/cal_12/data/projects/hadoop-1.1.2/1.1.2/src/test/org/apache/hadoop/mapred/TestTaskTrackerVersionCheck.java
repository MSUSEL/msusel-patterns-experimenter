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

import java.io.IOException;

import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.util.VersionInfo;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test the version check the TT performs when connecting to the JT
 */
public class TestTaskTrackerVersionCheck {

  /**
   * Test the default TT version checking
   */
  @Test
  public void testDefaultVersionCheck() throws IOException {
    MiniMRCluster mr = null;
    try {
      JobConf jtConf = new JobConf();
      mr = new MiniMRCluster(1, "file:///", 1, null, null, jtConf);
      TaskTracker tt = mr.getTaskTrackerRunner(0).getTaskTracker();
      String currBuildVersion = VersionInfo.getBuildVersion();
      String currVersion = VersionInfo.getVersion();

      assertTrue(tt.isPermittedVersion(currBuildVersion, currVersion));
      assertFalse("We disallow different versions",
          tt.isPermittedVersion(currBuildVersion+"x", currVersion+"x"));
      assertFalse("We disallow different full versions with same version",
          tt.isPermittedVersion(currBuildVersion+"x", currVersion));      
      try {
        tt.isPermittedVersion(currBuildVersion, currVersion+"x");
        fail("Matched full version with mismatched version");
      } catch (AssertionError ae) {
        // Expected. The versions should always match if the full
        // versions match as the full version contains the version.
      }
    } finally {
      if (mr != null) {
        mr.shutdown();
      }
    }
  }

  /**
   * Test the "relaxed" TT version checking
   */
  @Test
  public void testRelaxedVersionCheck() throws IOException {
    MiniMRCluster mr = null;
    try {
      JobConf jtConf = new JobConf();
      jtConf.setBoolean(
          CommonConfigurationKeys.HADOOP_RELAXED_VERSION_CHECK_KEY, true);
      mr = new MiniMRCluster(1, "file:///", 1, null, null, jtConf);
      TaskTracker tt = mr.getTaskTrackerRunner(0).getTaskTracker();
      String currFullVersion = VersionInfo.getBuildVersion();
      String currVersion = VersionInfo.getVersion();

      assertTrue(tt.isPermittedVersion(currFullVersion, currVersion));
      assertFalse("We dissallow different versions",
          tt.isPermittedVersion(currFullVersion+"x", currVersion+"x"));
      assertTrue("We allow different full versions with same version",
          tt.isPermittedVersion(currFullVersion+"x", currVersion));
    } finally {
      if (mr != null) {
        mr.shutdown();
      }
    }
  }
}
