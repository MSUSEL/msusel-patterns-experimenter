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
package org.apache.hadoop.tools.rumen;

@SuppressWarnings("all")
public enum EventType { 
  JOB_SUBMITTED, JOB_INITED, JOB_FINISHED, JOB_PRIORITY_CHANGED, JOB_STATUS_CHANGED, JOB_FAILED, JOB_KILLED, JOB_INFO_CHANGED, TASK_STARTED, TASK_FINISHED, TASK_FAILED, TASK_UPDATED, MAP_ATTEMPT_STARTED, MAP_ATTEMPT_FINISHED, MAP_ATTEMPT_FAILED, MAP_ATTEMPT_KILLED, REDUCE_ATTEMPT_STARTED, REDUCE_ATTEMPT_FINISHED, REDUCE_ATTEMPT_FAILED, REDUCE_ATTEMPT_KILLED, SETUP_ATTEMPT_STARTED, SETUP_ATTEMPT_FINISHED, SETUP_ATTEMPT_FAILED, SETUP_ATTEMPT_KILLED, CLEANUP_ATTEMPT_STARTED, CLEANUP_ATTEMPT_FINISHED, CLEANUP_ATTEMPT_FAILED, CLEANUP_ATTEMPT_KILLED
}
