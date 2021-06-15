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

import java.io.IOException;

import org.apache.hadoop.ipc.VersionedProtocol;
import org.apache.hadoop.security.KerberosInfo;

/**
 * Protocol for admin operations. This is a framework-public interface and is
 * NOT_TO_BE_USED_BY_USERS_DIRECTLY.
 */
@KerberosInfo(
    serverPrincipal = JobTracker.JT_USER_NAME)
public interface AdminOperationsProtocol extends VersionedProtocol {
  
  /**
   * Version 1: Initial version. Added refreshQueueAcls.
   * Version 2: Added node refresh facility
   * Version 3: Changed refreshQueueAcls to refreshQueues
   */
  public static final long versionID = 3L;

  /**
   * Refresh the queue acls in use currently.
   * Refresh the queues used by the jobtracker and scheduler.
   *
   * Access control lists and queue states are refreshed.
   */
  void refreshQueues() throws IOException;
  
  /**
   * Refresh the node list at the {@link JobTracker} 
   */
  void refreshNodes() throws IOException;
  
  /**
   * Set safe mode for the JobTracker.
   * @param safeModeAction safe mode action
   * @return current safemode
   * @throws IOException
   */
  boolean setSafeMode(JobTracker.SafeModeAction safeModeAction) 
      throws IOException;
}
