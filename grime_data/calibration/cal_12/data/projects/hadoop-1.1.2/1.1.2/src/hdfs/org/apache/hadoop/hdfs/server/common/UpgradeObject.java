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
package org.apache.hadoop.hdfs.server.common;

import java.io.IOException;

import org.apache.hadoop.hdfs.server.common.UpgradeObjectCollection.UOSignature;

/**
 * Abstract upgrade object.
 * 
 * Contains default implementation of common methods of {@link Upgradeable}
 * interface.
 */
public abstract class UpgradeObject implements Upgradeable {
  protected short status;
  
  public short getUpgradeStatus() {
    return status;
  }

  public String getDescription() {
    return "Upgrade object for " + getType() + " layout version " + getVersion();
  }

  public UpgradeStatusReport getUpgradeStatusReport(boolean details) 
                                                    throws IOException {
    return new UpgradeStatusReport(getVersion(), getUpgradeStatus(), false);
  }

  public int compareTo(Upgradeable o) {
    if(this.getVersion() != o.getVersion())
      return (getVersion() > o.getVersion() ? -1 : 1);
    int res = this.getType().toString().compareTo(o.getType().toString());
    if(res != 0)
      return res;
    return getClass().getCanonicalName().compareTo(
                    o.getClass().getCanonicalName());
  }

  public boolean equals(Object o) {
    if (!(o instanceof UpgradeObject)) {
      return false;
    }
    return this.compareTo((UpgradeObject)o) == 0;
  }

  public int hashCode() {
    return new UOSignature(this).hashCode(); 
  }
}
