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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Base class for parsing either chmod permissions or umask permissions.
 * Includes common code needed by either operation as implemented in
 * UmaskParser and ChmodParser classes.
 */
class PermissionParser {
  protected boolean symbolic = false;
  protected short userMode;
  protected short groupMode;
  protected short othersMode;
  protected char userType = '+';
  protected char groupType = '+';
  protected char othersType = '+';
  
  /**
   * Begin parsing permission stored in modeStr
   * 
   * @param modeStr Permission mode, either octal or symbolic
   * @param symbolic Use-case specific symbolic pattern to match against
   * @throws IllegalArgumentException if unable to parse modeStr
   */
  public PermissionParser(String modeStr, Pattern symbolic, Pattern octal) 
       throws IllegalArgumentException {
    Matcher matcher = null;

    if ((matcher = symbolic.matcher(modeStr)).find()) {
      applyNormalPattern(modeStr, matcher);
    } else if ((matcher = octal.matcher(modeStr)).matches()) {
      applyOctalPattern(modeStr, matcher);
    } else {
      throw new IllegalArgumentException(modeStr);
    }
  }

  private void applyNormalPattern(String modeStr, Matcher matcher) {
    // Are there multiple permissions stored in one chmod?
    boolean commaSeperated = false;

    for (int i = 0; i < 1 || matcher.end() < modeStr.length(); i++) {
      if (i > 0 && (!commaSeperated || !matcher.find())) {
        throw new IllegalArgumentException(modeStr);
      }

      /*
       * groups : 1 : [ugoa]* 2 : [+-=] 3 : [rwxX]+ 4 : [,\s]*
       */

      String str = matcher.group(2);
      char type = str.charAt(str.length() - 1);

      boolean user, group, others;
      user = group = others = false;

      for (char c : matcher.group(1).toCharArray()) {
        switch (c) {
        case 'u':
          user = true;
          break;
        case 'g':
          group = true;
          break;
        case 'o':
          others = true;
          break;
        case 'a':
          break;
        default:
          throw new RuntimeException("Unexpected");
        }
      }

      if (!(user || group || others)) { // same as specifying 'a'
        user = group = others = true;
      }

      short mode = 0;

      for (char c : matcher.group(3).toCharArray()) {
        switch (c) {
        case 'r':
          mode |= 4;
          break;
        case 'w':
          mode |= 2;
          break;
        case 'x':
          mode |= 1;
          break;
        case 'X':
          mode |= 8;
          break;
        default:
          throw new RuntimeException("Unexpected");
        }
      }

      if (user) {
        userMode = mode;
        userType = type;
      }

      if (group) {
        groupMode = mode;
        groupType = type;
      }

      if (others) {
        othersMode = mode;
        othersType = type;
      }

      commaSeperated = matcher.group(4).contains(",");
    }
    symbolic = true;
  }

  private void applyOctalPattern(String modeStr, Matcher matcher) {
    userType = groupType = othersType = '=';

    String str = matcher.group(1);
    userMode = Short.valueOf(str.substring(0, 1));
    groupMode = Short.valueOf(str.substring(1, 2));
    othersMode = Short.valueOf(str.substring(2, 3));
  }

  protected int combineModes(int existing, boolean exeOk) {
    return   combineModeSegments(userType, userMode,
                (existing>>>6)&7, exeOk) << 6 |
             combineModeSegments(groupType, groupMode,
                (existing>>>3)&7, exeOk) << 3 |
             combineModeSegments(othersType, othersMode, existing&7, exeOk);
  }
  
  protected int combineModeSegments(char type, int mode, 
                                    int existing, boolean exeOk) {
    boolean capX = false;

    if ((mode&8) != 0) { // convert X to x;
      capX = true;
      mode &= ~8;
      mode |= 1;
    }

    switch (type) {
    case '+' : mode = mode | existing; break;
    case '-' : mode = (~mode) & existing; break;
    case '=' : break;
    default  : throw new RuntimeException("Unexpected");      
    }

    // if X is specified add 'x' only if exeOk or x was already set.
    if (capX && !exeOk && (mode&1) != 0 && (existing&1) == 0) {
      mode &= ~1; // remove x
    }

    return mode;
  }
}
