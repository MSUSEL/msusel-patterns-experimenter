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
package org.apache.hadoop.hdfs.protocol;

import java.io.IOException;

/** 
 * This exception is thrown when modification to HDFS results in violation
 * of a directory quota. A directory quota might be namespace quota (limit 
 * on number of files and directories) or a diskspace quota (limit on space 
 * taken by all the file under the directory tree). <br> <br>
 * 
 * The message for the exception specifies the directory where the quota
 * was violated and actual quotas. Specific message is generated in the 
 * corresponding Exception class: 
 *  DSQuotaExceededException or
 *  NSQuotaExceededException
 */
public class QuotaExceededException extends IOException {
  protected static final long serialVersionUID = 1L;
  protected String pathName=null;
  protected long quota; // quota
  protected long count; // actual value
  
  protected QuotaExceededException(String msg) {
    super(msg);
  }
  
  protected QuotaExceededException(long quota, long count) {
    this.quota = quota;
    this.count = count;
  }
  
  public void setPathName(String path) {
    this.pathName = path;
  }
  
  public String getMessage() {
    return super.getMessage();
  }
}
