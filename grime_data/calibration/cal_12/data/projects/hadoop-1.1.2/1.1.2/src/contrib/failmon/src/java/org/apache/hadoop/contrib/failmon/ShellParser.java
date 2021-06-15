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
package org.apache.hadoop.contrib.failmon;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**********************************************************
 * Objects of this class parse the output of system command-line
 * utilities that can give information about the state of  
 * various hardware components in the system. Typically, each such
 * object either invokes a command and reads its output or reads the 
 * output of one such command from a file on the disk. Currently 
 * supported utilities include ifconfig, smartmontools, lm-sensors,
 * /proc/cpuinfo.
 *
 **********************************************************/

public abstract class ShellParser implements Monitored {

  /**
   * Find the first occurence ofa pattern in a piece of text 
   * and return a specific group.
   * 
   *  @param strPattern the regular expression to match
   *  @param text the text to search
   *  @param grp the number of the matching group to return
   *  
   *  @return a String containing the matched group of the regular expression
   */
  protected String findPattern(String strPattern, String text, int grp) {

    Pattern pattern = Pattern.compile(strPattern, Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(text);

    if (matcher.find(0))
      return matcher.group(grp);

    return null;
  }

  /**
   * Finds all occurences of a pattern in a piece of text and returns 
   * the matching groups.
   * 
   *  @param strPattern the regular expression to match
   *  @param text the text to search
   *  @param grp the number of the matching group to return
   *  @param separator the string that separates occurences in the returned value
   *  
   *  @return a String that contains all occurences of strPattern in text, 
   *  separated by separator
   */
  protected String findAll(String strPattern, String text, int grp,
      String separator) {

    String retval = "";
    boolean firstTime = true;

    Pattern pattern = Pattern.compile(strPattern);
    Matcher matcher = pattern.matcher(text);

    while (matcher.find()) {
      retval += (firstTime ? "" : separator) + matcher.group(grp);
      firstTime = false;
    }

    return retval;
  }

  /**
   * Insert all EventRecords that can be extracted for
   * the represented hardware component into a LocalStore.
   * 
   * @param ls the LocalStore into which the EventRecords 
   * are to be stored.
   */
  public void monitor(LocalStore ls) {
    ls.insert(monitor());
  }

  abstract public EventRecord[] monitor();

  abstract public EventRecord query(String s) throws Exception;

}
