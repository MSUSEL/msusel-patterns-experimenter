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
package org.apache.hadoop.test.system.process;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;

/**
 * Interface to manage the remote process.
 */
public interface RemoteProcess {
  /**
   * Get the host on which the daemon process is running/stopped.<br/>
   * 
   * @return hostname on which process is running/stopped.
   */
  String getHostName();

  /**
   * Start a given daemon process.<br/>
   * 
   * @throws IOException if startup fails.
   */
  void start() throws IOException;
  /**
   * Starts a daemon from user specified conf dir. 
   * @param newConfLocation is dir where new conf resides. 
   * @throws IOException if start of process fails from new location.
   */
  void start(String newConfLocation) throws IOException;
  /**
   * Stop a given daemon process.<br/>
   * 
   * @throws IOException if shutdown fails.
   */
  void kill() throws IOException;
  
  /**
   * Stops a given daemon running from user specified 
   * conf dir. </br>
   * @throws IOException if kill fails from new conf location.
   * @param newconfLocation dir location where new conf resides. 
   */
   void kill(String newConfLocation) throws IOException;
  /**
   * Get the role of the Daemon in the cluster.
   * 
   * @return Enum
   */
  Enum<?> getRole();
  
  /**
   * Pushed the configuration to new configuration directory 
   * @param localDir The local directory which has config files that will be 
   * pushed to the remote location
   * @throws IOException is thrown if the pushConfig results in a error. 
   * @return The newconfdir location will be returned
   */
  String pushConfig(String localDir) throws IOException;
}
