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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Resolves all UGIs to the submitting user.
 */
public class SubmitterUserResolver implements UserResolver {
  public static final Log LOG = LogFactory.getLog(SubmitterUserResolver.class);
  
  private UserGroupInformation ugi = null;

  public SubmitterUserResolver() throws IOException {
    LOG.info(" Current user resolver is SubmitterUserResolver ");
    ugi = UserGroupInformation.getLoginUser();
  }

  public synchronized boolean setTargetUsers(URI userdesc, Configuration conf)
      throws IOException {
    return false;
  }

  public synchronized UserGroupInformation getTargetUgi(
      UserGroupInformation ugi) {
    return this.ugi;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Since {@link SubmitterUserResolver} returns the user name who is running
   * gridmix, it doesn't need a target list of users.
   */
  public boolean needsTargetUsersList() {
    return false;
  }
}
