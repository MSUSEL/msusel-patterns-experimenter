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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.ipc.RemoteException;

/**
 * An abstract class for the execution of a file system command
 */
abstract public class Command extends Configured {
  protected String[] args;
  
  /** Constructor */
  protected Command(Configuration conf) {
    super(conf);
  }
  
  /** Return the command's name excluding the leading character - */
  abstract public String getCommandName();
  
  /** 
   * Execute the command on the input path
   * 
   * @param path the input path
   * @throws IOException if any error occurs
   */
  abstract protected void run(Path path) throws IOException;
  
  /** 
   * For each source path, execute the command
   * 
   * @return 0 if it runs successfully; -1 if it fails
   */
  public int runAll() {
    int exitCode = 0;
    for (String src : args) {
      try {
        Path srcPath = new Path(src);
        FileSystem fs = srcPath.getFileSystem(getConf());
        FileStatus[] statuses = fs.globStatus(srcPath);
        if (statuses == null) {
          System.err.println("Can not find listing for " + src);
          exitCode = -1;
        } else {
          for(FileStatus s : statuses) {
            run(s.getPath());
          }
        }
      } catch (RemoteException re) {
        exitCode = -1;
        String content = re.getLocalizedMessage();
        int eol = content.indexOf('\n');
        if (eol>=0) {
          content = content.substring(0, eol);
        }
        System.err.println(getCommandName() + ": " + content);
      } catch (IOException e) {
        exitCode = -1;
        System.err.println(getCommandName() + ": " + e.getLocalizedMessage());
      }
    }
    return exitCode;
  }
}
