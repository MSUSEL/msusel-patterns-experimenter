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
package org.apache.hadoop.hdfs.server.datanode;

import static org.apache.hadoop.fs.CommonConfigurationKeys.HADOOP_SECURITY_AUTHENTICATION;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.server.common.HdfsConstants;
import org.apache.hadoop.http.HttpServer;
import org.mortbay.jetty.nio.SelectChannelConnector;

/**
 * Utility class to start a datanode in a secure cluster, first obtaining 
 * privileged resources before main startup and handing them to the datanode.
 */
public class SecureDataNodeStarter implements Daemon {
  /**
   * Stash necessary resources needed for datanode operation in a secure env.
   */
  public static class SecureResources {
    private final ServerSocket streamingSocket;
    private final SelectChannelConnector listener;
    public SecureResources(ServerSocket streamingSocket,
        SelectChannelConnector listener) {

      this.streamingSocket = streamingSocket;
      this.listener = listener;
    }

    public ServerSocket getStreamingSocket() { return streamingSocket; }

    public SelectChannelConnector getListener() { return listener; }
  }
  
  private String [] args;
  private SecureResources resources;
  
  @Override
  public void init(DaemonContext context) throws Exception {
    System.err.println("Initializing secure datanode resources");
    // We should only start up a secure datanode in a Kerberos-secured cluster
    Configuration conf = new Configuration(); // Skip UGI method to not log in
    if(!conf.get(HADOOP_SECURITY_AUTHENTICATION).equals("kerberos"))
      throw new RuntimeException("Cannot start secure datanode in unsecure cluster");
    
    // Stash command-line arguments for regular datanode
    args = context.getArguments();
    
    // Obtain secure port for data streaming to datanode
    InetSocketAddress socAddr = DataNode.getStreamingAddr(conf);
    int socketWriteTimeout = conf.getInt("dfs.datanode.socket.write.timeout",
        HdfsConstants.WRITE_TIMEOUT);
    
    ServerSocket ss = (socketWriteTimeout > 0) ? 
        ServerSocketChannel.open().socket() : new ServerSocket();
    ss.bind(socAddr, 0);
    
    // Check that we got the port we need
    if(ss.getLocalPort() != socAddr.getPort())
      throw new RuntimeException("Unable to bind on specified streaming port in secure " +
      		"context. Needed " + socAddr.getPort() + ", got " + ss.getLocalPort());

    // Obtain secure listener for web server
    SelectChannelConnector listener = 
                   (SelectChannelConnector)HttpServer.createDefaultChannelConnector();
    InetSocketAddress infoSocAddr = DataNode.getInfoAddr(conf);
    listener.setHost(infoSocAddr.getHostName());
    listener.setPort(infoSocAddr.getPort());
    // Open listener here in order to bind to port as root
    listener.open(); 
    if(listener.getPort() != infoSocAddr.getPort())
      throw new RuntimeException("Unable to bind on specified info port in secure " +
          "context. Needed " + socAddr.getPort() + ", got " + ss.getLocalPort());
   
    if(ss.getLocalPort() >= 1023 || listener.getPort() >= 1023)
      throw new RuntimeException("Cannot start secure datanode on non-privileged "
         +" ports. (streaming port = " + ss + " ) (http listener port = " + 
         listener.getConnection() + "). Exiting.");
 
    System.err.println("Successfully obtained privileged resources (streaming port = "
        + ss + " ) (http listener port = " + listener.getConnection() +")");
    
    resources = new SecureResources(ss, listener);
  }

  @Override
  public void start() throws Exception {
    System.err.println("Starting regular datanode initialization");
    DataNode.secureMain(args, resources);
  }
  
  @Override public void destroy() { /* Nothing to do */ }
  @Override public void stop() throws Exception { /* Nothing to do */ }
}
