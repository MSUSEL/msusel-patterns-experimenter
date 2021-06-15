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
import java.util.List;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;

/**
 * Interface to manage the remote processes in the cluster.
 */
public interface ClusterProcessManager {

  /**
   * Initialization method to pass the configuration object which is required 
   * by the ClusterProcessManager to manage the cluster.<br/>
   * Configuration object should typically contain all the parameters which are 
   * required by the implementations.<br/>
   *  
   * @param conf configuration containing values of the specific keys which 
   * are required by the implementation of the cluster process manger.
   * 
   * @throws IOException when initialization fails.
   */
  void init(Configuration conf) throws IOException;

  /**
   * Get the list of RemoteProcess handles of all the remote processes.
   */
  List<RemoteProcess> getAllProcesses();
  
  /**
   * Get a RemoteProcess given the hostname
   * @param The hostname for which the object instance needs to be obtained.
   * @param The role of process example are JT,TT,DN,NN
   */
  RemoteProcess getDaemonProcess(String hostname, Enum<?> role);

  /**
   * Get all the roles this cluster's daemon processes have.
   */
  Set<Enum<?>> getRoles();

  /**
   * Method to start all the remote daemons.<br/>
   * 
   * @throws IOException if startup procedure fails.
   */
  void start() throws IOException;

  /**
   * Starts the daemon from the user specified conf dir.
   * @param newConfLocation the dir where the new conf files reside.
   * @throws IOException if start from new conf fails. 
   */
  void start(String newConfLocation) throws IOException;

  /**
   * Stops the daemon running from user specified conf dir.
   * 
   * @param newConfLocation the dir where the new conf files reside.
   * @throws IOException if stop from new conf fails. 
   */
  void stop(String newConfLocation) throws IOException;

  /**
   * Method to shutdown all the remote daemons.<br/>
   * 
   * @throws IOException if shutdown procedure fails.
   */
  void stop() throws IOException;
  
  /**
   * Gets if multi-user support is enabled for this cluster. 
   * <br/>
   * @return true if multi-user support is enabled.
   * @throws IOException if RPC returns error. 
   */
  boolean isMultiUserSupported() throws IOException;

  /**
   * The pushConfig is used to push a new config to the daemons.
   * @param localDir
   * @return is the remoteDir location where config will be pushed
   * @throws IOException if pushConfig fails.
   */
  String pushConfig(String localDir) throws IOException;
}
