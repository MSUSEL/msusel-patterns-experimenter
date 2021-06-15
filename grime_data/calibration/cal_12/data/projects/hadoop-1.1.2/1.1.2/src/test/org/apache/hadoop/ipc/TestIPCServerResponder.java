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
package org.apache.hadoop.ipc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.net.NetUtils;

/**
 * This test provokes partial writes in the server, which is 
 * serving multiple clients.
 */
public class TestIPCServerResponder extends TestCase {

  public static final Log LOG = 
            LogFactory.getLog(TestIPCServerResponder.class);

  private static Configuration conf = new Configuration();

  public TestIPCServerResponder(final String name) {
    super(name);
  }

  private static final Random RANDOM = new Random();

  private static final String ADDRESS = "0.0.0.0";

  private static final int BYTE_COUNT = 1024;
  private static final byte[] BYTES = new byte[BYTE_COUNT];
  static {
    for (int i = 0; i < BYTE_COUNT; i++)
      BYTES[i] = (byte) ('a' + (i % 26));
  }

  private static class TestServer extends Server {

    private boolean sleep;

    public TestServer(final int handlerCount, final boolean sleep) 
                                              throws IOException {
      super(ADDRESS, 0, BytesWritable.class, handlerCount, conf);
      // Set the buffer size to half of the maximum parameter/result size 
      // to force the socket to block
      this.setSocketSendBufSize(BYTE_COUNT / 2);
      this.sleep = sleep;
    }

    @Override
    public Writable call(Class<?> protocol, Writable param, long receiveTime)
        throws IOException {
      if (sleep) {
        try {
          Thread.sleep(RANDOM.nextInt(20)); // sleep a bit
        } catch (InterruptedException e) {}
      }
      return param;
    }
  }

  private static class Caller extends Thread {

    private Client client;
    private int count;
    private InetSocketAddress address;
    private boolean failed;

    public Caller(final Client client, final InetSocketAddress address, 
                                       final int count) {
      this.client = client;
      this.address = address;
      this.count = count;
    }

    @Override
    public void run() {
      for (int i = 0; i < count; i++) {
        try {
          int byteSize = RANDOM.nextInt(BYTE_COUNT);
          byte[] bytes = new byte[byteSize];
          System.arraycopy(BYTES, 0, bytes, 0, byteSize);
          Writable param = new BytesWritable(bytes);
          Writable value = client.call(param, address);
          Thread.sleep(RANDOM.nextInt(20));
        } catch (Exception e) {
          LOG.fatal("Caught: " + e);
          failed = true;
        }
      }
    }
  }

  public void testResponseBuffer() throws Exception {
    Server.INITIAL_RESP_BUF_SIZE = 1;
    conf.setInt(Server.IPC_SERVER_RPC_MAX_RESPONSE_SIZE_KEY,
                1);
    testServerResponder(1, true, 1, 1, 5);
    conf = new Configuration(); // reset configuration
  }

  public void testServerResponder() throws Exception {
    testServerResponder(10, true, 1, 10, 200);
  }

  public void testServerResponder(final int handlerCount, 
                                  final boolean handlerSleep, 
                                  final int clientCount,
                                  final int callerCount,
                                  final int callCount) throws Exception {
    Server server = new TestServer(handlerCount, handlerSleep);
    server.start();

    InetSocketAddress address = NetUtils.getConnectAddress(server);
    Client[] clients = new Client[clientCount];
    for (int i = 0; i < clientCount; i++) {
      clients[i] = new Client(BytesWritable.class, conf);
    }

    Caller[] callers = new Caller[callerCount];
    for (int i = 0; i < callerCount; i++) {
      callers[i] = new Caller(clients[i % clientCount], address, callCount);
      callers[i].start();
    }
    for (int i = 0; i < callerCount; i++) {
      callers[i].join();
      assertFalse(callers[i].failed);
    }
    for (int i = 0; i < clientCount; i++) {
      clients[i].stop();
    }
    server.stop();
  }

}
