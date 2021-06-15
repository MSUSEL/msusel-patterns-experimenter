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
package org.archive.util;

/**
 * Utility functions to escape or unescape Java literal strings.
 *
 * @author gojomo
 *
 */
public class JavaLiterals {

  public static String escape(String raw) {
    StringBuffer escaped = new StringBuffer();
    for(int i = 0; i<raw.length(); i++) {
      char c = raw.charAt(i);
      switch (c) {
        case '\b':
          escaped.append("\\b");
          break;
        case '\t':
          escaped.append("\\t");
          break;
        case '\n':
          escaped.append("\\n");
          break;
        case '\f':
          escaped.append("\\f");
          break;
        case '\r':
          escaped.append("\\r");
          break;
        case '\"':
          escaped.append("\\\"");
          break;
        case '\'':
          escaped.append("\\'");
          break;
        case '\\':
          escaped.append("\\\\");
          break;
        default:
          if(Character.getType(c)==Character.CONTROL) {
            String unicode = Integer.toHexString((int)c);
            while(unicode.length()<4) {
              unicode = "0"+unicode;
            }
            escaped.append("\\u"+unicode);
          } else {
            escaped.append(c);
          }
      }

    }
    return escaped.toString();
  }

  public static String unescape(String escaped) {
    StringBuffer raw = new StringBuffer();
    for(int i = 0; i<escaped.length(); i++) {
      char c = escaped.charAt(i);
      if (c!='\\') {
        raw.append(c);
      } else {
        i++;
        if(i>=escaped.length()) {
          // trailing '/'
          raw.append(c);
          continue;
        }
        c = escaped.charAt(i);
        switch (c) {
          case 'b':
            raw.append('\b');
            break;
          case 't':
            raw.append('\t');
            break;
          case 'n':
            raw.append('\n');
            break;
          case 'f':
            raw.append('\f');
            break;
          case 'r':
            raw.append('r');
            break;
          case '"':
            raw.append('\"');
            break;
          case '\'':
            raw.append('\'');
            break;
          case '\\':
            raw.append('\\');
            break;
          case 'u':
            // unicode hex escape
            try {
              int unicode = Integer.parseInt(escaped.substring(i+1,i+5),16);
              raw.append((char)unicode);
              i = i + 4;
            } catch (IndexOutOfBoundsException e) {
              // err
              raw.append("\\u");
            }
            break;
          default:
              if(Character.isDigit(c)) {
                // octal escape
                int end = Math.min(i+4,escaped.length());
                int octal = Integer.parseInt(escaped.substring(i+1,end),8);
                if(octal<256) {
                  raw.append((char)octal);
                  i = end - 1;
                } else {
                  // err
                  raw.append('\\');
                  raw.append(c);
                }
              }
              break;
        }
      }
    }
    return raw.toString();
  }
}
