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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.hdfs.server.common.StorageInfo;
import org.apache.hadoop.io.WritableComparable;

/**
 * A unique signature intended to identify checkpoint transactions.
 */
public class CheckpointSignature extends StorageInfo 
                      implements WritableComparable<CheckpointSignature> {
  private static final String FIELD_SEPARATOR = ":";
  long editsTime = -1L;
  long checkpointTime = -1L;

  CheckpointSignature() {}

  CheckpointSignature(FSImage fsImage) {
    super(fsImage);
    editsTime = fsImage.getEditLog().getFsEditTime();
    checkpointTime = fsImage.checkpointTime;
  }

  CheckpointSignature(String str) {
    String[] fields = str.split(FIELD_SEPARATOR);
    assert fields.length == 5 : "Must be 5 fields in CheckpointSignature";
    layoutVersion = Integer.valueOf(fields[0]);
    namespaceID = Integer.valueOf(fields[1]);
    cTime = Long.valueOf(fields[2]);
    editsTime = Long.valueOf(fields[3]);
    checkpointTime = Long.valueOf(fields[4]);
  }

  public String toString() {
    return String.valueOf(layoutVersion) + FIELD_SEPARATOR
         + String.valueOf(namespaceID) + FIELD_SEPARATOR
         + String.valueOf(cTime) + FIELD_SEPARATOR
         + String.valueOf(editsTime) + FIELD_SEPARATOR
         + String.valueOf(checkpointTime);
  }

  void validateStorageInfo(StorageInfo si) throws IOException {
    if(layoutVersion != si.layoutVersion
        || namespaceID != si.namespaceID || cTime != si.cTime) {
      // checkpointTime can change when the image is saved - do not compare
      throw new IOException("Inconsistent checkpoint fileds. "
          + "LV = " + layoutVersion + " namespaceID = " + namespaceID
          + " cTime = " + cTime + ". Expecting respectively: "
          + si.layoutVersion + "; " + si.namespaceID + "; " + si.cTime);
    }
  }

  //
  // Comparable interface
  //
  public int compareTo(CheckpointSignature o) {
    return 
      (layoutVersion < o.layoutVersion) ? -1 : 
                  (layoutVersion > o.layoutVersion) ? 1 :
      (namespaceID < o.namespaceID) ? -1 : (namespaceID > o.namespaceID) ? 1 :
      (cTime < o.cTime) ? -1 : (cTime > o.cTime) ? 1 :
      (editsTime < o.editsTime) ? -1 : (editsTime > o.editsTime) ? 1 :
      (checkpointTime < o.checkpointTime) ? -1 : 
                  (checkpointTime > o.checkpointTime) ? 1 : 0;
  }

  public boolean equals(Object o) {
    if (!(o instanceof CheckpointSignature)) {
      return false;
    }
    return compareTo((CheckpointSignature)o) == 0;
  }

  public int hashCode() {
    return layoutVersion ^ namespaceID ^
            (int)(cTime ^ editsTime ^ checkpointTime);
  }

  /////////////////////////////////////////////////
  // Writable
  /////////////////////////////////////////////////
  public void write(DataOutput out) throws IOException {
    out.writeInt(getLayoutVersion());
    out.writeInt(getNamespaceID());
    out.writeLong(getCTime());
    out.writeLong(editsTime);
    out.writeLong(checkpointTime);
  }

  public void readFields(DataInput in) throws IOException {
    layoutVersion = in.readInt();
    namespaceID = in.readInt();
    cTime = in.readLong();
    editsTime = in.readLong();
    checkpointTime = in.readLong();
  }
}
