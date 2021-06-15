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
package org.apache.hadoop.fs.permission;

import java.util.regex.Pattern;

import org.apache.hadoop.fs.FileStatus;

/**
 * Parse a permission mode passed in from a chmod command and apply that
 * mode against an existing file.
 */
public class ChmodParser extends PermissionParser {
  private static Pattern chmodOctalPattern =
    Pattern.compile("^\\s*[+]?([0-7]{3})\\s*$");
  private static Pattern chmodNormalPattern =
    Pattern.compile("\\G\\s*([ugoa]*)([+=-]+)([rwxX]+)([,\\s]*)\\s*");
  
  public ChmodParser(String modeStr) throws IllegalArgumentException {
    super(modeStr, chmodNormalPattern, chmodOctalPattern);
  }

  /**
   * Apply permission against specified file and determine what the
   * new mode would be
   * @param file File against which to apply mode
   * @return File's new mode if applied.
   */
  public short applyNewPermission(FileStatus file) {
    FsPermission perms = file.getPermission();
    int existing = perms.toShort();
    boolean exeOk = file.isDir() || (existing & 0111) != 0;
    
    return (short)combineModes(existing, exeOk);
  }
}
