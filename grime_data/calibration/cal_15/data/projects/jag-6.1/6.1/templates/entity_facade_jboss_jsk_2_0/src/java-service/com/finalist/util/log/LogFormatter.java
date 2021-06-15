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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * A class wich specifies the format for the log messages
 * @author Ronald Kramp - Finalist IT Group
 * @version $Revision: 1.1 $, $Date: 2004/11/12 14:06:44 $
 */
public final class LogFormatter extends Formatter {

   /** a variable for specifying a given data format */
   private SimpleDateFormat dateFormatter;

   /**
    * a variable the will set the number of last packages will be shown
    * example number=3 com.finalist.appname.util.log.LogFormatter
    * will be util.log.LogFormatter
    */
   private int showNumberOfLastPackages;

   /** a variable for specifying the separator between the Classname and the message */
   private String messageSeparator;


   /**
    * Constrcutor for making a LogFormatter
    * @param showNumberOfLastPackages the number of packages to log
    * @param datePattern the pattern of the date to log
    * @param messageSeparator the message separator
    */
   public LogFormatter(int showNumberOfLastPackages, String datePattern, String messageSeparator) {
      this.dateFormatter = new SimpleDateFormat(datePattern);
      this.messageSeparator = messageSeparator;
      this.showNumberOfLastPackages = showNumberOfLastPackages;
   }


   /**
    * Formatting a logrecord to a single line.
    * sequence of the logline: Logevel date Classname separator message exception
    * @param rec the log record to format
    * @return String the line to log
    */
   public String format(LogRecord rec) {
      StringBuffer buf = new StringBuffer(1000);
      StringBuffer packageClass = new StringBuffer(50);
      String loggerName = rec.getLoggerName();

      if (this.showNumberOfLastPackages <= 0) {
         packageClass.append(loggerName);
      }

      for (int i = 0; i < this.showNumberOfLastPackages; i++) {
         int index = loggerName.lastIndexOf(".");

         if (index > -1) {
            packageClass.insert(0, loggerName.substring(index, loggerName.length()));
            loggerName = loggerName.substring(0, index);
         }
         else {
            packageClass.insert(0, loggerName);
            break;
         }
      }
      if (packageClass.charAt(0) == '.') {
         packageClass.deleteCharAt(0);
      }
      //String level = rec.getLevel().toString();
      //level = (level.length() >= 5) ? level.substring(0, 5) : level + " ";

      buf.append(rec.getLevel());
      buf.append("     ");
      buf.delete(8, buf.length());
      buf.insert(8, dateFormatter.format(new Date(rec.getMillis())));
      buf.append(" ");
      buf.append(packageClass);
      buf.append(" ");
      buf.append(this.messageSeparator);
      buf.append(" ");
      buf.append(formatMessage(rec));
      buf.append((rec.getThrown() != null) ? rec.getThrown().toString() : "");
      buf.append('\n');
      return buf.toString();
   }
}