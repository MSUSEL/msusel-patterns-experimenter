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
package org.apache.hadoop.eclipse.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;

public enum ConfProp {
  /**
   * Property name for the Hadoop location name
   */
  PI_LOCATION_NAME(true, "location.name", "New Hadoop location"),

  /**
   * Property name for the master host name (the Job tracker)
   */
  PI_JOB_TRACKER_HOST(true, "jobtracker.host", "localhost"),

  /**
   * Property name for the DFS master host name (the Name node)
   */
  PI_NAME_NODE_HOST(true, "namenode.host", "localhost"),

  /**
   * Property name for the installation directory on the master node
   */
  // PI_INSTALL_DIR(true, "install.dir", "/dir/hadoop-version/"),
  /**
   * User name to use for Hadoop operations
   */
  PI_USER_NAME(true, "user.name", System.getProperty("user.name",
      "who are you?")),

  /**
   * Property name for SOCKS proxy activation
   */
  PI_SOCKS_PROXY_ENABLE(true, "socks.proxy.enable", "no"),

  /**
   * Property name for the SOCKS proxy host
   */
  PI_SOCKS_PROXY_HOST(true, "socks.proxy.host", "host"),

  /**
   * Property name for the SOCKS proxy port
   */
  PI_SOCKS_PROXY_PORT(true, "socks.proxy.port", "1080"),

  /**
   * TCP port number for the name node
   */
  PI_NAME_NODE_PORT(true, "namenode.port", "50040"),

  /**
   * TCP port number for the job tracker
   */
  PI_JOB_TRACKER_PORT(true, "jobtracker.port", "50020"),

  /**
   * Are the Map/Reduce and the Distributed FS masters hosted on the same
   * machine?
   */
  PI_COLOCATE_MASTERS(true, "masters.colocate", "yes"),

  /**
   * Property name for naming the job tracker (URI). This property is related
   * to {@link #PI_MASTER_HOST_NAME}
   */
  JOB_TRACKER_URI(false, "mapred.job.tracker", "localhost:50020"),

  /**
   * Property name for naming the default file system (URI).
   */
  FS_DEFAULT_URI(false, "fs.default.name", "hdfs://localhost:50040/"),

  /**
   * Property name for the default socket factory:
   */
  SOCKET_FACTORY_DEFAULT(false, "hadoop.rpc.socket.factory.class.default",
      "org.apache.hadoop.net.StandardSocketFactory"),

  /**
   * Property name for the SOCKS server URI.
   */
  SOCKS_SERVER(false, "hadoop.socks.server", "host:1080"),

  ;

  /**
   * Map <property name> -> ConfProp
   */
  private static Map<String, ConfProp> map;

  private static synchronized void registerProperty(String name,
      ConfProp prop) {

    if (ConfProp.map == null)
      ConfProp.map = new HashMap<String, ConfProp>();

    ConfProp.map.put(name, prop);
  }

  public static ConfProp getByName(String propName) {
    return map.get(propName);
  }

  public final String name;

  public final String defVal;

  ConfProp(boolean internal, String name, String defVal) {
    if (internal)
      name = "eclipse.plug-in." + name;
    this.name = name;
    this.defVal = defVal;

    ConfProp.registerProperty(name, this);
  }

  String get(Configuration conf) {
    return conf.get(name);
  }

  void set(Configuration conf, String value) {
    assert value != null;
    conf.set(name, value);
  }

}
