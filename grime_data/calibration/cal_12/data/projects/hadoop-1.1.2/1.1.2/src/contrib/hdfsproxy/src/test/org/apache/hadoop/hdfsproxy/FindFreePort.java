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
package org.apache.hadoop.hdfsproxy;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.BindException;
import java.util.Random;

public class FindFreePort {
  private static final int MIN_AVAILABLE_PORT = 10000;
  private static final int MAX_AVAILABLE_PORT = 65535;
  private static Random random = new Random();
  /**
   * 
   * @param num <= 0, find a single free port
   * @return free port next to port (>port)
   * @throws IOException
   */
  public static int findFreePort(int port) throws IOException {
    ServerSocket server;
    if (port < 0) {
      server =  new ServerSocket(0);      
    } else {
      int freePort = port+1;
      while (true) {
        try {
          server =  new ServerSocket(freePort);
          break;
        } catch (IOException e) {
          if (e instanceof BindException) {
            if (freePort >= MAX_AVAILABLE_PORT || 
                freePort < MIN_AVAILABLE_PORT) {
              throw e;
            }
          } else {
            throw e;
          }
          freePort += 1;
        }
      }
    }
    int fport = server.getLocalPort();
    server.close();
    return fport;    
  }
 /**
  * 
  * @return
  * @throws IOException
  */
  public static int findFreePortRandom() throws IOException {
    return findFreePort(MIN_AVAILABLE_PORT + random.nextInt(MAX_AVAILABLE_PORT - MIN_AVAILABLE_PORT + 1));
  }
   

  public static void main(String[] args) throws Exception {
    if(args.length < 1) {       
      System.err.println("Usage: FindFreePort < -random / <#port> >");        
      System.exit(0);      
    }
    int j = 0;
    String cmd = args[j++];
    if ("-random".equals(cmd)) {
      System.out.println(findFreePortRandom());
    } else {
      System.out.println(findFreePort(Integer.parseInt(cmd)));
    }   
  }
        
}
