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
 * Interface for the logger object, for logging messages to a logfile
 * @author Ronald Kramp - Finalist IT Group
 * @version $Revision: 1.1 $, $Date: 2004/11/12 14:06:44 $
 */
public interface Logger {

   /**
    * Logging a finest message
    * @param message the message to log
    */
   void finest(String message);

   /**
    * Logging a finer message
    * @param message the message to log
    */
   void finer(String message);

   /**
    * Logging a fine message
    * @param message the message to log
    */
   void fine(String message);

   /**
    * Logging a config message
    * @param message the message to log
    */
   void config(String message);

   /**
    * Logging an info message
    * @param message the message to log
    */
   void info(String message);


   /**
    * Logging a warning message
    * @param message the message to log
    */
   void warning(String message);

   /**
    * Logging a severe message
    * @param message the message to log
    */
   void severe(String message);

   //****************************************************
   //*  The methods from log4j also implemented below   *
   //****************************************************

   /**
    * Logging a debug message
    * @param message the message to log
    */
   void debug(String message);

   /**
    * Logging a debug message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void debug(String message, Throwable t);

   /**
    * Logging an info message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void info(String message, Throwable t);

   /**
    * Logging a warning message
    * @param message the message to log
    */
   void warn(String message);

   /**
    * Logging a warning message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void warn(String message, Throwable t);

   /**
    * Logging an error message
    * @param message the message to log
    */
   void error(String message);

   /**
    * Logging an error message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void error(String message, Throwable t);

   /**
    * Logging a fatal message
    * @param message the message to log
    */
   void fatal(String message);

   /**
    * Logging a fatal message with the throwable message
    * @param message the message to log
    * @param t the exception
    */
   void fatal(String message, Throwable t);
}