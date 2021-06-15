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

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

/**
 * A utility class. It provides
 *   - file-util
 *     - A path filter utility to filter out output/part files in the output dir
 */
public class Utils {
  public static class OutputFileUtils {
    /**
     * This class filters output(part) files from the given directory
     * It does not accept files with filenames _logs and _SUCCESS.
     * This can be used to list paths of output directory as follows:
     *   Path[] fileList = FileUtil.stat2Paths(fs.listStatus(outDir,
     *                                         new OutputFilesFilter()));
     */
    public static class OutputFilesFilter extends OutputLogFilter {
      public boolean accept(Path path) {
        return super.accept(path) 
               && !FileOutputCommitter.SUCCEEDED_FILE_NAME
                   .equals(path.getName());
      }
    }
    
    /**
     * This class filters log files from directory given
     * It doesnt accept paths having _logs.
     * This can be used to list paths of output directory as follows:
     *   Path[] fileList = FileUtil.stat2Paths(fs.listStatus(outDir,
     *                                   new OutputLogFilter()));
     */
    public static class OutputLogFilter implements PathFilter {
      public boolean accept(Path path) {
        return !(path.toString().contains("_logs"));
      }
    }
  }
}

