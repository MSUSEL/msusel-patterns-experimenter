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
package org.apache.hadoop.mapred;

import org.apache.hadoop.security.RefreshUserMappingsProtocol;
import org.apache.hadoop.security.authorize.PolicyProvider;
import org.apache.hadoop.security.authorize.RefreshAuthorizationPolicyProtocol;
import org.apache.hadoop.security.authorize.Service;

/**
 * {@link PolicyProvider} for Map-Reduce protocols.
 */
public class MapReducePolicyProvider extends PolicyProvider {
  private static final Service[] mapReduceServices = 
    new Service[] {
      new Service("security.inter.tracker.protocol.acl", 
                  InterTrackerProtocol.class),
      new Service("security.job.submission.protocol.acl",
                  JobSubmissionProtocol.class),
      new Service("security.task.umbilical.protocol.acl", 
                  TaskUmbilicalProtocol.class),
      new Service("security.refresh.policy.protocol.acl", 
                  RefreshAuthorizationPolicyProtocol.class),
      new Service("security.refresh.usertogroups.mappings.protocol.acl", 
                  RefreshUserMappingsProtocol.class),
      new Service("security.admin.operations.protocol.acl", 
                  AdminOperationsProtocol.class),
  };
  
  @Override
  public Service[] getServices() {
    return mapReduceServices;
  }

}
