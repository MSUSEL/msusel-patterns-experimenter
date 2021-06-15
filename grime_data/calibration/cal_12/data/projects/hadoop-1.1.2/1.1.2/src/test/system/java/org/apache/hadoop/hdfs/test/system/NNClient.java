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
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.net.NetUtils;
import org.apache.hadoop.test.system.process.RemoteProcess;

public class NNClient extends HDFSDaemonClient<NNProtocol> {
  
  NNProtocol proxy;

  public NNClient(Configuration conf, RemoteProcess process) throws IOException {
    super(conf, process);
  }

  @Override
  public void connect() throws IOException {
    if (isConnected())
      return;
    String sockAddrStr = FileSystem.getDefaultUri(getConf()).getAuthority();
    if (sockAddrStr == null) {
      throw new IllegalArgumentException("Namenode IPC address is not set");
    }
    String[] splits = sockAddrStr.split(":");
    if (splits.length != 2) {
      throw new IllegalArgumentException(
          "Namenode report IPC is not correctly configured");
    }
    String port = splits[1];
    String sockAddr = getHostName() + ":" + port;

    InetSocketAddress bindAddr = NetUtils.createSocketAddr(sockAddr);
    proxy = (NNProtocol) RPC.getProxy(NNProtocol.class, NNProtocol.versionID,
        bindAddr, getConf());
    setConnected(true);
  }

  @Override
  public void disconnect() throws IOException {
    RPC.stopProxy(proxy);
    setConnected(false);
  }

  @Override
  protected NNProtocol getProxy() {
    return proxy;
  }

  /**
   * Concrete implementation of abstract super class method
   * @param attributeName name of the attribute to be retrieved
   * @return Object value of the given attribute
   * @throws IOException is thrown in case of communication errors
   */
  @Override
  public Object getDaemonAttribute (String attributeName) throws IOException {
    return getJmxAttribute("NameNode", "NameNodeInfo", attributeName);
  }
}
