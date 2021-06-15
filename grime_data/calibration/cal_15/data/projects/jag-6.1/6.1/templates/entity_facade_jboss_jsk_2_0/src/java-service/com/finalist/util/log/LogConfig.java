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
package com.finalist.util.log;

/**
 * Value object with log settings
 * @author Ronald Kramp - Finalist IT Group
 * @version $Revision: 1.1 $, $Date: 2004/11/12 14:06:44 $
 */
public class LogConfig {

   /** the logfile to log to */
   private String logFile = "finalist%g.log";

   /** append to the logfile */
   private boolean append = true;

   /** maximum number of logfiles to create */
   private int maxBackupIndex = 1;

   /** maxfilesize for each created logfile */
   private int maxFileSize = 50;

   /** the number of packages to show for a class */
   private int showNumberOfLastPackages = 0;

   /** the datepattern to log */
   private String datePattern = "yyyy-MM-dd HH:mm:ss,SSS";

   /** the messagespearator */
   private String messageSeparator = "-";

   /** the loglevel for logging certain messages */
   private String logLevel = "INFO";


   /**
    * Constrcutor for making a LogConfig
    * All settings for a appender
    * @param logFile the name of the logfile, default finalist%g.log
    * @param append default true
    * @param maxBackupIndex the number of files to create, must be greater than 0, default 1
    * @param maxFileSize the size of a log file in megabytes, must be greater than 0, default 50
    * @param showNumberOfLastPackages the number of pacakges of a class to show, cannot be negative, default 0
    * @param datePattern the date to log, default yyyy-MM-dd HH:mm:ss,SSS
    * @param messageSeparator default -
    * @param logLevel default INFO
    */
   public LogConfig(String logFile,
         boolean append,
         int maxBackupIndex,
         int maxFileSize,
         int showNumberOfLastPackages,
         String datePattern,
         String messageSeparator,
         String logLevel) {
      if ((logFile != null) && (!logFile.equals(""))) {
         this.logFile = logFile;
      }
      this.append = append;
      if (maxBackupIndex > 0) {
         this.maxBackupIndex = maxBackupIndex;
      }
      if (maxFileSize > 0) {
         this.maxFileSize = maxFileSize;
      }
      if (showNumberOfLastPackages > -1) {
         this.showNumberOfLastPackages = showNumberOfLastPackages;
      }
      if ((datePattern != null) && (!datePattern.equals(""))) {
         this.datePattern = datePattern;
      }
      if ((messageSeparator != null) && (!messageSeparator.equals(""))) {
         this.messageSeparator = messageSeparator;
      }
      if ((logLevel != null) && (!logLevel.equals(""))) {
         this.logLevel = logLevel;
      }
   }


   /**
    * Get the name of the logfile
    * @return String, the name of the log file
    */
   public String getLogFile() {
      return this.logFile;
   }


   /**
    * Check to see if the logfile is appendable
    * @return boolean, logfile is appendable
    */
   public boolean isAppendable() {
      return this.append;
   }


   /**
    * Get the maxBackupIndex
    * @return int, the maxBackupIndex
    */
   public int getMaxBackupIndex() {
      return this.maxBackupIndex;
   }


   /**
    * Get the maxFileSize
    * @return int, maxFileSize
    */
   public int getMaxFileSize() {
      return this.maxFileSize;
   }


   /**
    * Get the showNumberOfLastPackages
    * @return int, the showNumberOfLastPackages
    */
   public int getShowNumberOfLastPackages() {
      return this.showNumberOfLastPackages;
   }


   /**
    * Get the datePattern
    * @return String, the datePattern
    */
   public String getDatePattern() {
      return this.datePattern;
   }


   /**
    * Get the messageSeparator
    * @return String, the messageSeparator
    */
   public String getMessageSeparator() {
      return this.messageSeparator;
   }


   /**
    * Get the logLevel
    * @return String, the logLevel
    */
   public String getLogLevel() {
      return this.logLevel;
   }


   /**
    * returning the string with all values
    * @return String, all values as a String
    */
   public String toString() {
      return "[logFile=" + this.logFile + "]" +
            ", [append=" + this.append + "]" +
            ", [maxBackupIndex=" + this.maxBackupIndex + "]" +
            ", [maxFileSize=" + this.maxFileSize + "]" +
            ", [showNumberOfLastPackages=" + this.showNumberOfLastPackages + "]" +
            ", [datePattern=" + this.datePattern + "]" +
            ", [messageSeparator=" + this.messageSeparator + "]" +
            ", [logLevel=" + this.logLevel + "]";
   }
}