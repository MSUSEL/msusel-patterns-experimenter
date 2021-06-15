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
package com.finalist.jag.util;


import com.finalist.jaggenerator.ConsoleLogger;

import java.io.*;


/**
 * Class Log
 *
 * @author Wendel D. de Witte
 */
public class Log {

   /** Field out           */
   private static PrintStream out = System.out;

   private static ConsoleLogger console;

   /**
    * Method log
    *
    * @param s
    */
   public static void log(String s) {
      if (console == null) {
         out.println(s);
      } else {
         console.log(s);
      }
   }

   public static void setLogger(ConsoleLogger logger) {
      console = logger;
   }

   /**
    * Appends an red error log to the end of the console. 
    * 
    * @param message
    *            the log message.
    */
    public static void error(String message) {
        if (console == null) {
            out.println(message);
        } else {
            console.error(message);
        }
    }

    /**
     * Appends an yellow debug log to the end of the console. 
     * 
     * @param message
     *            the log message.
     */
     public static void debug(String message) {
         if (console == null) {
             out.println(message);
         } else {
             console.debug(message);
         }
     }
    
}