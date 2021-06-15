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

/**
 * A listener for changes in a {@link JobInProgress job}'s lifecycle in the
 * {@link JobTracker}.
 */
abstract class JobInProgressListener {

  /**
   * Invoked when a new job has been added to the {@link JobTracker}.
   * @param job The added job.
   * @throws IOException 
   */
  public abstract void jobAdded(JobInProgress job) throws IOException;

  /**
   * Invoked when a job has been removed from the {@link JobTracker}.
   * @param job The removed job.
   */
  public abstract void jobRemoved(JobInProgress job);
  
  /**
   * Invoked when a job has been updated in the {@link JobTracker}.
   * This change in the job is tracker using {@link JobChangeEvent}.
   * @param event the event that tracks the change
   */
  public abstract void jobUpdated(JobChangeEvent event);
}
