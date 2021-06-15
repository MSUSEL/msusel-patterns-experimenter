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
package org.apache.hadoop.hdfs;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.test.system.DaemonProtocol;
import org.apache.hadoop.hdfs.test.system.DNProtocol;
import org.apache.hadoop.hdfs.test.system.NNProtocol;
import org.apache.hadoop.security.authorize.Service;
import org.apache.hadoop.security.authorize.ServiceAuthorizationManager;

/**
 * This aspect adds two HDFS Herriot specific protocols tp the list of 'authorized'
 * Herriot protocols.
 * Protocol descriptors i.e. 'security.nn.protocol.acl' have to be added to
 * <code>hadoop-policy.xml</code> if present
 */
public privileged aspect HDFSPolicyProviderAspect {
  private static final Log LOG = LogFactory
      .getLog(HDFSPolicyProviderAspect.class);

  ArrayList<Service> herriotHDFSServices = null;

  pointcut updateHDFSServices() :
    execution (public Service[] HDFSPolicyProvider.getServices());

  Service[] around() : updateHDFSServices () {
    herriotHDFSServices = new ArrayList<Service>();
    for (Service s : HDFSPolicyProvider.hdfsServices) {
      LOG.debug("Copying configured protocol to "
          + s.getProtocol().getCanonicalName());
      herriotHDFSServices.add(s);
    }
    herriotHDFSServices.add(new Service("security.daemon.protocol.acl",
        DaemonProtocol.class));
    herriotHDFSServices.add(new Service("security.nn.protocol.acl",
        NNProtocol.class));
    herriotHDFSServices.add(new Service("security.dn.protocol.acl",
        DNProtocol.class));
    final Service[] retArray = herriotHDFSServices
        .toArray(new Service[herriotHDFSServices.size()]);
    LOG.debug("Number of configured protocols to return: " + retArray.length);
    return retArray;
  }
}
