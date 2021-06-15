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

import java.io.File;
import java.io.IOException;
import java.util.AbstractList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.test.system.DNProtocol;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.test.system.DaemonProtocol;
import org.apache.hadoop.hdfs.server.datanode.SecureDataNodeStarter.SecureResources;

public privileged aspect DataNodeAspect {
  declare parents : DataNode implements DNProtocol;

  public Configuration DataNode.getDaemonConf() {
    return super.getConf();
  }

  pointcut dnConstructorPointcut(Configuration conf, AbstractList<File> dirs,
      SecureResources resources) :
    call(DataNode.new(Configuration, AbstractList<File>, SecureResources))
    && args(conf, dirs, resources);

  after(Configuration conf, AbstractList<File> dirs, SecureResources resources)
    returning (DataNode datanode):
    dnConstructorPointcut(conf, dirs, resources) {
    try {
      UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
      datanode.setUser(ugi.getShortUserName());
    } catch (IOException e) {
      datanode.LOG.warn("Unable to get the user information for the " +
          "DataNode");
    }
    datanode.setReady(true);
  }

  pointcut getVersionAspect(String protocol, long clientVersion) :
    execution(public long DataNode.getProtocolVersion(String ,
      long) throws IOException) && args(protocol, clientVersion);

  long around(String protocol, long clientVersion) :
    getVersionAspect(protocol, clientVersion) {
    if(protocol.equals(DaemonProtocol.class.getName())) {
      return DaemonProtocol.versionID;
    } else if(protocol.equals(DNProtocol.class.getName())) {
      return DNProtocol.versionID;
    } else {
      return proceed(protocol, clientVersion);
    }
  }
}