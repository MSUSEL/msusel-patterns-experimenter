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
package org.apache.hadoop.mapred.gridmix;

import java.io.IOException;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;

/**
 * Maps users in the trace to a set of valid target users on the test cluster.
 */
@InterfaceAudience.Private
@InterfaceStability.Evolving
public interface UserResolver {

  /**
   * Configure the user map given the URI and configuration. The resolver's
   * contract will define how the resource will be interpreted, but the default
   * will typically interpret the URI as a {@link org.apache.hadoop.fs.Path}
   * listing target users.
   * This method should be called only if {@link #needsTargetUsersList()}
   * returns true.
   * @param userdesc URI from which user information may be loaded per the
   * subclass contract.
   * @param conf The tool configuration.
   * @return true if the resource provided was used in building the list of
   * target users
   */
  public boolean setTargetUsers(URI userdesc, Configuration conf)
    throws IOException;

  /**
   * Map the given UGI to another per the subclass contract.
   * @param ugi User information from the trace.
   */
  public UserGroupInformation getTargetUgi(UserGroupInformation ugi);

  /**
   * Indicates whether this user resolver needs a list of target users to be
   * provided.
   *
   * @return true if a list of target users is to be provided for this
   * user resolver
   */
  public boolean needsTargetUsersList();

}
