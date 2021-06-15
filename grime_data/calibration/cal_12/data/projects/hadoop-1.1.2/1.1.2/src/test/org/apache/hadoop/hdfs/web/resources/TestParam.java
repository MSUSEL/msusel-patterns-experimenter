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
package org.apache.hadoop.hdfs.web.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.junit.Assert;
import org.junit.Test;

public class TestParam {
  public static final Log LOG = LogFactory.getLog(TestParam.class);

  final Configuration conf = new Configuration();
 
  @Test
  public void testAccessTimeParam() {
    final AccessTimeParam p = new AccessTimeParam(AccessTimeParam.DEFAULT);
    Assert.assertEquals(-1L, p.getValue().longValue());

    new AccessTimeParam(-1L);

    try {
      new AccessTimeParam(-2L);
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }

  @Test
  public void testBlockSizeParam() {
    final BlockSizeParam p = new BlockSizeParam(BlockSizeParam.DEFAULT);
    Assert.assertEquals(null, p.getValue());
    Assert.assertEquals(
        conf.getLong(DFSConfigKeys.DFS_BLOCK_SIZE_KEY,
            DFSConfigKeys.DFS_BLOCK_SIZE_DEFAULT),
        p.getValue(conf));

    new BlockSizeParam(1L);

    try {
      new BlockSizeParam(0L);
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }

  @Test
  public void testBufferSizeParam() {
    final BufferSizeParam p = new BufferSizeParam(BufferSizeParam.DEFAULT);
    Assert.assertEquals(null, p.getValue());
    Assert.assertEquals(
        conf.getInt("io.file.buffer.size", 4096),
        p.getValue(conf));

    new BufferSizeParam(1);

    try {
      new BufferSizeParam(0);
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }

  @Test
  public void testDelegationParam() {
    final DelegationParam p = new DelegationParam(DelegationParam.DEFAULT);
    Assert.assertEquals(null, p.getValue());
  }

  @Test
  public void testDestinationParam() {
    final DestinationParam p = new DestinationParam(DestinationParam.DEFAULT);
    Assert.assertEquals(null, p.getValue());

    new DestinationParam("/abc");

    try {
      new DestinationParam("abc");
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }

  @Test
  public void testGroupParam() {
    final GroupParam p = new GroupParam(GroupParam.DEFAULT);
    Assert.assertEquals(null, p.getValue());
  }

  @Test
  public void testModificationTimeParam() {
    final ModificationTimeParam p = new ModificationTimeParam(ModificationTimeParam.DEFAULT);
    Assert.assertEquals(-1L, p.getValue().longValue());

    new ModificationTimeParam(-1L);

    try {
      new ModificationTimeParam(-2L);
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }

  @Test
  public void testOverwriteParam() {
    final OverwriteParam p = new OverwriteParam(OverwriteParam.DEFAULT);
    Assert.assertEquals(false, p.getValue());

    new OverwriteParam("trUe");

    try {
      new OverwriteParam("abc");
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }

  @Test
  public void testOwnerParam() {
    final OwnerParam p = new OwnerParam(OwnerParam.DEFAULT);
    Assert.assertEquals(null, p.getValue());
  }

  @Test
  public void testPermissionParam() {
    final PermissionParam p = new PermissionParam(PermissionParam.DEFAULT);
    Assert.assertEquals(new FsPermission((short)0755), p.getFsPermission());

    new PermissionParam("0");

    try {
      new PermissionParam("-1");
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }

    new PermissionParam("777");

    try {
      new PermissionParam("1000");
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }

    try {
      new PermissionParam("8");
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }

    try {
      new PermissionParam("abc");
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }

  @Test
  public void testRecursiveParam() {
    final RecursiveParam p = new RecursiveParam(RecursiveParam.DEFAULT);
    Assert.assertEquals(false, p.getValue());

    new RecursiveParam("falSe");

    try {
      new RecursiveParam("abc");
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }

  @Test
  public void testRenewerParam() {
    final RenewerParam p = new RenewerParam(RenewerParam.DEFAULT);
    Assert.assertEquals(null, p.getValue());
  }

  @Test
  public void testReplicationParam() {
    final ReplicationParam p = new ReplicationParam(ReplicationParam.DEFAULT);
    Assert.assertEquals(null, p.getValue());
    Assert.assertEquals(
        (short)conf.getInt(DFSConfigKeys.DFS_REPLICATION_KEY,
            DFSConfigKeys.DFS_REPLICATION_DEFAULT),
        p.getValue(conf));

    new ReplicationParam((short)1);

    try {
      new ReplicationParam((short)0);
      Assert.fail();
    } catch(IllegalArgumentException e) {
      LOG.info("EXPECTED: " + e);
    }
  }
}
