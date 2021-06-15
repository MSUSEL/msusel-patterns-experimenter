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
package org.apache.hadoop.fs.shell;

import java.io.*;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Count the number of directories, files, bytes, quota, and remaining quota.
 */
public class Count extends Command {
  public static final String NAME = "count";
  public static final String USAGE = "-" + NAME + "[-q] <path>";
  public static final String DESCRIPTION = CommandUtils.formatDescription(USAGE, 
      "Count the number of directories, files and bytes under the paths",
      "that match the specified file pattern.  The output columns are:",
      "DIR_COUNT FILE_COUNT CONTENT_SIZE FILE_NAME or",
      "QUOTA REMAINING_QUATA SPACE_QUOTA REMAINING_SPACE_QUOTA ",
      "      DIR_COUNT FILE_COUNT CONTENT_SIZE FILE_NAME");
  
  private boolean qOption;

  /** Constructor
   * 
   * @param cmd the count command
   * @param pos the starting index of the arguments 
   */
  public Count(String[] cmd, int pos, Configuration conf) {
    super(conf);
    CommandFormat c = new CommandFormat(NAME, 1, Integer.MAX_VALUE, "q");
    List<String> parameters = c.parse(cmd, pos);
    this.args = parameters.toArray(new String[parameters.size()]);
    if (this.args.length == 0) { // default path is the current working directory
      this.args = new String[] {"."};
    }
    this.qOption = c.getOpt("q") ? true: false;
  }
  
  /** Check if a command is the count command
   * 
   * @param cmd A string representation of a command starting with "-"
   * @return true if this is a count command; false otherwise
   */
  public static boolean matches(String cmd) {
    return ("-" + NAME).equals(cmd); 
  }

  @Override
  public String getCommandName() {
    return NAME;
  }

  @Override
  protected void run(Path path) throws IOException {
    FileSystem fs = path.getFileSystem(getConf());
    System.out.println(fs.getContentSummary(path).toString(qOption) + path);
  }
}
