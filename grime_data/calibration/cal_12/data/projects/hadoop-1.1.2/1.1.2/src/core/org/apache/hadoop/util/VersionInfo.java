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
package org.apache.hadoop.util;

import org.apache.hadoop.HadoopVersionAnnotation;

/**
 * This class finds the package info for Hadoop and the HadoopVersionAnnotation
 * information.
 */
public class VersionInfo {
  private static Package myPackage;
  private static HadoopVersionAnnotation version;
  
  static {
    myPackage = HadoopVersionAnnotation.class.getPackage();
    version = myPackage.getAnnotation(HadoopVersionAnnotation.class);
  }

  /**
   * Get the meta-data for the Hadoop package.
   * @return
   */
  static Package getPackage() {
    return myPackage;
  }
  
  /**
   * Get the Hadoop version.
   * @return the Hadoop version string, eg. "0.6.3-dev"
   */
  public static String getVersion() {
    return version != null ? version.version() : "Unknown";
  }
  
  /**
   * Get the subversion revision number for the root directory
   * @return the revision number, eg. "451451"
   */
  public static String getRevision() {
    return version != null ? version.revision() : "Unknown";
  }
  
  /**
   * The date that Hadoop was compiled.
   * @return the compilation date in unix date format
   */
  public static String getDate() {
    return version != null ? version.date() : "Unknown";
  }
  
  /**
   * The user that compiled Hadoop.
   * @return the username of the user
   */
  public static String getUser() {
    return version != null ? version.user() : "Unknown";
  }
  
  /**
   * Get the subversion URL for the root Hadoop directory.
   */
  public static String getUrl() {
    return version != null ? version.url() : "Unknown";
  }

  /**
   * Get the checksum of the source files from which Hadoop was
   * built.
   **/
  public static String getSrcChecksum() {
    return version != null ? version.srcChecksum() : "Unknown";
  }
  
  /**
   * Returns the full version string containing version,
   * revision, user and source checksum. 
   */
  public static String getBuildVersion(){
    return VersionInfo.getVersion() + 
    " from " + VersionInfo.getRevision() +
    " by " + VersionInfo.getUser() + 
    " source checksum " + VersionInfo.getSrcChecksum();
  }
  
  public static void main(String[] args) {
    System.out.println("Hadoop " + getVersion());
    System.out.println("Subversion " + getUrl() + " -r " + getRevision());
    System.out.println("Compiled by " + getUser() + " on " + getDate());
    System.out.println("From source with checksum " + getSrcChecksum());

  }
}
