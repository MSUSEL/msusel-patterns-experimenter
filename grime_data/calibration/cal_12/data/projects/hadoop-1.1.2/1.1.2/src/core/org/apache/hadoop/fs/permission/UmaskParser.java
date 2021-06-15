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

/**
 * Parse umask value provided as a string, either in octal or symbolic
 * format and return it as a short value. Umask values are slightly
 * different from standard modes as they cannot specify X.
 */
class UmaskParser extends PermissionParser {
  private static Pattern chmodOctalPattern =
    Pattern.compile("^\\s*[+]?([0-7]{3})\\s*$");
  private static Pattern umaskSymbolicPattern =    /* not allow X */
    Pattern.compile("\\G\\s*([ugoa]*)([+=-]+)([rwx]*)([,\\s]*)\\s*");
  final short umaskMode;
  
  public UmaskParser(String modeStr) throws IllegalArgumentException {
    super(modeStr, umaskSymbolicPattern, chmodOctalPattern);

    umaskMode = (short)combineModes(0, false);
  }

  /**
   * To be used for file/directory creation only. Symbolic umask is applied
   * relative to file mode creation mask; the permission op characters '+'
   * results in clearing the corresponding bit in the mask, '-' results in bits
   * for indicated permission to be set in the mask.
   * 
   * For octal umask, the specified bits are set in the file mode creation mask.
   * 
   * @return umask
   */
  public short getUMask() {
    if (symbolic) {
      // Return the complement of octal equivalent of umask that was computed
      return (short) (~umaskMode & 0777);      
    }
    return umaskMode;
  }
}
