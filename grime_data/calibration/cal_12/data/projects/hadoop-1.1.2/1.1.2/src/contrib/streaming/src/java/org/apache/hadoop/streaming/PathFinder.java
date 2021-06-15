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
package org.apache.hadoop.streaming;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * Maps a relative pathname to an absolute pathname using the
 * PATH enviroment.
 */
public class PathFinder
{
  String pathenv;        // a string of pathnames
  String pathSep;        // the path seperator
  String fileSep;        // the file seperator in a directory

  /**
   * Construct a PathFinder object using the path from
   * java.class.path
   */
  public PathFinder()
  {
    pathenv = System.getProperty("java.class.path");
    pathSep = System.getProperty("path.separator");
    fileSep = System.getProperty("file.separator");
  }

  /**
   * Construct a PathFinder object using the path from
   * the specified system environment variable.
   */
  public PathFinder(String envpath)
  {
    pathenv = System.getenv(envpath);
    pathSep = System.getProperty("path.separator");
    fileSep = System.getProperty("file.separator");
  }

  /**
   * Appends the specified component to the path list
   */
  public void prependPathComponent(String str)
  {
    pathenv = str + pathSep + pathenv;
  }

  /**
   * Returns the full path name of this file if it is listed in the
   * path
   */
  public File getAbsolutePath(String filename)
  {
    if (pathenv == null || pathSep == null  || fileSep == null)
      {
        return null;
      }
    int     val = -1;
    String    classvalue = pathenv + pathSep;

    while (((val = classvalue.indexOf(pathSep)) >= 0) &&
           classvalue.length() > 0) {
      //
      // Extract each entry from the pathenv
      //
      String entry = classvalue.substring(0, val).trim();
      File f = new File(entry);

      try {
        if (f.isDirectory()) {
          //
          // this entry in the pathenv is a directory.
          // see if the required file is in this directory
          //
          f = new File(entry + fileSep + filename);
        }
        //
        // see if the filename matches and  we can read it
        //
        if (f.isFile() && f.canRead()) {
          return f;
        }
      } catch (Exception exp){ }
      classvalue = classvalue.substring(val+1).trim();
    }
    return null;
  }

  /**
   * prints all environment variables for this process
   */
  private static void printEnvVariables() {
    System.out.println("Environment Variables: ");
    Map<String,String> map = System.getenv();
    Set<Entry<String, String>> entrySet = map.entrySet();
    for(Entry<String, String> entry : entrySet) {
      System.out.println(entry.getKey() + " = " + entry.getValue());
    }
  }

  /**
   * prints all system properties for this process
   */
  private static void printSystemProperties() {
    System.out.println("System properties: ");
    java.util.Properties p = System.getProperties();
    java.util.Enumeration keys = p.keys();
    while(keys.hasMoreElements()) {
      String thiskey = (String)keys.nextElement();
      String value = p.getProperty(thiskey);
      System.out.println(thiskey + " = " + value);
    }
  }

  public static void main(String args[]) throws IOException {

    if (args.length < 1) {
      System.out.println("Usage: java PathFinder <filename>");
      System.exit(1);
    }

    PathFinder finder = new PathFinder("PATH");
    File file = finder.getAbsolutePath(args[0]);
    if (file != null) {
      System.out.println("Full path name = " + file.getCanonicalPath());
    }
  }
}
