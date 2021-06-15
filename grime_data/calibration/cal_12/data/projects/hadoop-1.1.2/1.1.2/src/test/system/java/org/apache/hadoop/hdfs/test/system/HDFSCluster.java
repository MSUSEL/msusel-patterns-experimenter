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
package org.apache.hadoop.hdfs.test.system;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.test.system.AbstractDaemonClient;
import org.apache.hadoop.test.system.AbstractDaemonCluster;
import org.apache.hadoop.test.system.process.ClusterProcessManager;
import org.apache.hadoop.test.system.process.HadoopDaemonRemoteCluster;
import org.apache.hadoop.test.system.process.MultiUserHadoopDaemonRemoteCluster;
import org.apache.hadoop.test.system.process.RemoteProcess;
import org.apache.hadoop.test.system.process.HadoopDaemonRemoteCluster.HadoopDaemonInfo;

public class HDFSCluster extends AbstractDaemonCluster {

  static {
    Configuration.addDefaultResource("hdfs-site.xml");
  }

  private static final Log LOG = LogFactory.getLog(HDFSCluster.class);
  public static final String CLUSTER_PROCESS_MGR_IMPL =
    "test.system.hdfs.clusterprocess.impl.class";

  private HDFSCluster(Configuration conf, ClusterProcessManager rCluster)
    throws IOException {
    super(conf, rCluster);
  }

  /**
   * Key is used to to point to the file containing hostnames of tasktrackers
   */
  public static final String CONF_HADOOP_DN_HOSTFILE_NAME =
    "test.system.hdrc.dn.hostfile";

  private static List<HadoopDaemonInfo> hdfsDaemonInfos;

  private static String nnHostName;
  private static String DN_hostFileName;

  protected enum Role {NN, DN}

  @Override
  protected AbstractDaemonClient
    createClient(RemoteProcess process) throws IOException {
    Enum<?> pRole = process.getRole();
    if (Role.NN.equals(pRole)) {
      return createNNClient(process);
    } else if (Role.DN.equals(pRole)) {
      return createDNClient(process);
    } else throw new IOException("Role " + pRole +
      " is not supported by HDFSCluster");
  }

  protected DNClient createDNClient(RemoteProcess dnDaemon) throws IOException {
    return new DNClient(getConf(), dnDaemon);
  }

  protected NNClient createNNClient(RemoteProcess nnDaemon) throws IOException {
    return new NNClient(getConf(), nnDaemon);
  }

  public NNClient getNNClient () {
    Iterator<AbstractDaemonClient> iter = getDaemons().get(Role.NN).iterator();
    return (NNClient) iter.next();
  }

  public List<DNClient> getDNClients () {
    return (List) getDaemons().get(Role.DN);
  }

  public DNClient getDNClient (String hostname) {
    for (DNClient dnC : getDNClients()) {
      if (dnC.getHostName().equals(hostname))
        return dnC;
    }
    return null;
  }

  public static class HDFSProcessManager extends HadoopDaemonRemoteCluster {
    public HDFSProcessManager() {
      super(hdfsDaemonInfos);
    }
  }

  public static class MultiUserHDFSProcessManager
      extends MultiUserHadoopDaemonRemoteCluster {
    public MultiUserHDFSProcessManager() {
      super(hdfsDaemonInfos);
    }
  }


  public static HDFSCluster createCluster(Configuration conf) throws Exception {
    conf.addResource("system-test.xml");
    String sockAddrStr = FileSystem.getDefaultUri(conf).getAuthority();
    if (sockAddrStr == null) {
      throw new IllegalArgumentException("Namenode IPC address is not set");
    }
    String[] splits = sockAddrStr.split(":");
    if (splits.length != 2) {
      throw new IllegalArgumentException(
          "Namenode report IPC is not correctly configured");
    }
    nnHostName = splits[0];
    DN_hostFileName = conf.get(CONF_HADOOP_DN_HOSTFILE_NAME, "slaves");

    hdfsDaemonInfos = new ArrayList<HadoopDaemonInfo>();
    hdfsDaemonInfos.add(new HadoopDaemonInfo("namenode", 
        Role.NN, Arrays.asList(new String[]{nnHostName})));
    hdfsDaemonInfos.add(new HadoopDaemonInfo("datanode", 
        Role.DN, DN_hostFileName));
    
    String implKlass = conf.get(CLUSTER_PROCESS_MGR_IMPL);
    if (implKlass == null || implKlass.isEmpty()) {
      implKlass = HDFSCluster.HDFSProcessManager.class.getName();
    }
    Class<ClusterProcessManager> klass =
      (Class<ClusterProcessManager>) Class.forName(implKlass);
    ClusterProcessManager clusterProcessMgr = klass.newInstance();
    LOG.info("Created ClusterProcessManager as " + implKlass);
    clusterProcessMgr.init(conf);
    return new HDFSCluster(conf, clusterProcessMgr);
  }
}
