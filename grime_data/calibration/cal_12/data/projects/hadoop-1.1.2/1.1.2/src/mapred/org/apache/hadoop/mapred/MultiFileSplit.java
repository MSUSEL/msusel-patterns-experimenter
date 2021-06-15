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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.CombineFileSplit;

/**
 * A sub-collection of input files. Unlike {@link FileSplit}, MultiFileSplit 
 * class does not represent a split of a file, but a split of input files 
 * into smaller sets. The atomic unit of split is a file. <br> 
 * MultiFileSplit can be used to implement {@link RecordReader}'s, with 
 * reading one record per file.
 * @see FileSplit
 * @see MultiFileInputFormat 
 * @deprecated Use {@link org.apache.hadoop.mapred.lib.CombineFileSplit} instead
 */
@Deprecated
public class MultiFileSplit extends CombineFileSplit {

  MultiFileSplit() {}
  
  public MultiFileSplit(JobConf job, Path[] files, long[] lengths) {
    super(job, files, lengths);
  }

  public String[] getLocations() throws IOException {
    HashSet<String> hostSet = new HashSet<String>();
    for (Path file : getPaths()) {
      FileSystem fs = file.getFileSystem(getJob());
      FileStatus status = fs.getFileStatus(file);
      BlockLocation[] blkLocations = fs.getFileBlockLocations(status,
                                          0, status.getLen());
      if (blkLocations != null && blkLocations.length > 0) {
        addToSet(hostSet, blkLocations[0].getHosts());
      }
    }
    return hostSet.toArray(new String[hostSet.size()]);
  }

  private void addToSet(Set<String> set, String[] array) {
    for(String s:array)
      set.add(s); 
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    for(int i=0; i < getPaths().length; i++) {
      sb.append(getPath(i).toUri().getPath() + ":0+" + getLength(i));
      if (i < getPaths().length -1) {
        sb.append("\n");
      }
    }

    return sb.toString();
  }
}

