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
package org.apache.hadoop.hdfs.server.namenode;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.test.system.NNProtocol;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.test.system.DaemonProtocol;

public privileged aspect NameNodeAspect {
  declare parents : NameNode implements NNProtocol;

  // Namename doesn't store a copy of its configuration
  // because it can be changed through the life cycle of the object
  // So, the an exposed reference needs to be added and updated after
  // new NameNode(Configuration conf) is complete
  Configuration NameNode.configRef = null;

  // Method simply assign a reference to the NameNode configuration object
  void NameNode.setRef (Configuration conf) {
    if (configRef == null)
      configRef = conf;
  }

  public Configuration NameNode.getDaemonConf() {
    return configRef;
  }

  pointcut nnConstructorPointcut(Configuration conf) :
    call(NameNode.new(Configuration)) && args(conf);

  after(Configuration conf) returning (NameNode namenode):
    nnConstructorPointcut(conf) {
    try {
      UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
      namenode.setUser(ugi.getShortUserName());
    } catch (IOException e) {
      namenode.LOG.warn("Unable to get the user information for the " +
          "Jobtracker");
    }
    namenode.setRef(conf);
    namenode.setReady(true);
  }

  pointcut getVersionAspect(String protocol, long clientVersion) :
    execution(public long NameNode.getProtocolVersion(String ,
      long) throws IOException) && args(protocol, clientVersion);

  long around(String protocol, long clientVersion) :
    getVersionAspect(protocol, clientVersion) {
    if(protocol.equals(DaemonProtocol.class.getName())) {
      return DaemonProtocol.versionID;
    } else if(protocol.equals(NNProtocol.class.getName())) {
      return NNProtocol.versionID;
    } else {
      return proceed(protocol, clientVersion);
    }
  }
}