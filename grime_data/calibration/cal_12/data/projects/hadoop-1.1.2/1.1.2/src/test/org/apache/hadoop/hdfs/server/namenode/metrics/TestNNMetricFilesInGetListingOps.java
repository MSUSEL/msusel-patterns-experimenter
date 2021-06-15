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
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hdfs.server.namenode.metrics;

import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSTestUtil;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.hdfs.protocol.HdfsFileStatus;
import org.apache.hadoop.hdfs.server.namenode.NameNode;

import static org.apache.hadoop.test.MetricsAsserts.*;

/**
 * Test case for FilesInGetListingOps metric in Namenode
 */
public class TestNNMetricFilesInGetListingOps extends TestCase {
  private static final Configuration CONF = new Configuration();
  static {
    CONF.setLong("dfs.block.size", 100);
    CONF.setInt("io.bytes.per.checksum", 1);
    CONF.setLong("dfs.heartbeat.interval", 1L);
    CONF.setInt("dfs.replication.interval", 1);
  }
     
  private MiniDFSCluster cluster;
  private NameNodeInstrumentation nnMetrics;
  private DistributedFileSystem fs;
  private Random rand = new Random();

  @Override
  protected void setUp() throws Exception {
    cluster = new MiniDFSCluster(CONF, 1, true, null);
    cluster.waitActive();
    cluster.getNameNode();
    nnMetrics = NameNode.getNameNodeMetrics();
    fs = (DistributedFileSystem) cluster.getFileSystem();
  }

  @Override
  protected void tearDown() throws Exception {
    cluster.shutdown();
  }

  /** create a file with a length of <code>fileLen</code> */
  private void createFile(String fileName, long fileLen, short replicas) throws IOException {
    Path filePath = new Path(fileName);
    DFSTestUtil.createFile(fs, filePath, fileLen, replicas, rand.nextLong());
  }
     

  public void testFilesInGetListingOps() throws Exception {
    createFile("/tmp1/t1", 3200, (short)3);
    createFile("/tmp1/t2", 3200, (short)3);
    createFile("/tmp2/t1", 3200, (short)3);
    createFile("/tmp2/t2", 3200, (short)3);
    cluster.getNameNode().getListing("/tmp1", HdfsFileStatus.EMPTY_NAME) ;
    assertCounter("FilesInGetListingOps", 2, nnMetrics);
    cluster.getNameNode().getListing("/tmp2", HdfsFileStatus.EMPTY_NAME) ;
    assertCounter("FilesInGetListingOps", 4, nnMetrics);
    // test non-existent path
    cluster.getNameNode().getListing("/tmp", HdfsFileStatus.EMPTY_NAME) ;
    assertCounter("FilesInGetListingOps", 4, nnMetrics);
    // test listing a file
    cluster.getNameNode().getListing("/tmp1/t1", HdfsFileStatus.EMPTY_NAME) ;
    assertCounter("FilesInGetListingOps", 5, nnMetrics);
  }
}

