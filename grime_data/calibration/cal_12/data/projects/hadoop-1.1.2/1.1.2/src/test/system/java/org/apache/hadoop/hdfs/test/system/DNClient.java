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
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSConfigKeys;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.test.system.process.RemoteProcess;

import javax.management.*;

/**
 * Datanode client for system tests. Assumption of the class is that the
 * configuration key is set for the configuration key : {@code
 * DFSConfigKeys.DFS_DATANODE_IPC_ADDRESS_KEY} is set, only the port portion of
 * the address is used.
 */
public class DNClient extends HDFSDaemonClient<DNProtocol> {

  DNProtocol proxy;

  public DNClient(Configuration conf, RemoteProcess process) throws IOException {
    super(conf, process);
  }

  @Override
  public void connect() throws IOException {
    if (isConnected()) {
      return;
    }
    String sockAddrStr = getConf().get(DFSConfigKeys.DFS_DATANODE_IPC_ADDRESS_KEY);
    if (sockAddrStr == null) {
      throw new IllegalArgumentException("Datanode IPC address is not set."
          + "Check if " + DFSConfigKeys.DFS_DATANODE_IPC_ADDRESS_KEY
          + " is configured.");
    }
    String[] splits = sockAddrStr.split(":");
    if (splits.length != 2) {
      throw new IllegalArgumentException(
          "Datanode IPC address is not correctly configured");
    }
    String port = splits[1];
    String sockAddr = getHostName() + ":" + port;
    InetSocketAddress bindAddr = NetUtils.createSocketAddr(sockAddr);
    proxy = (DNProtocol) RPC.getProxy(DNProtocol.class, DNProtocol.versionID,
        bindAddr, getConf());
    setConnected(true);
  }

  @Override
  public void disconnect() throws IOException {
    RPC.stopProxy(proxy);
    setConnected(false);
  }

  @Override
  protected DNProtocol getProxy() {
    return proxy;
  }

  public Configuration getDatanodeConfig() throws IOException {
    return getProxy().getDaemonConf();
  }

  /**
   * Concrete implementation of abstract super class method
   * @param attributeName name of the attribute to be retrieved
   * @return Object value of the given attribute
   * @throws IOException is thrown in case of communication errors
   */
  @Override
  public Object getDaemonAttribute (String attributeName) throws IOException {
    return getJmxAttribute("DataNode", "DataNodeInfo", attributeName);
  }
}
