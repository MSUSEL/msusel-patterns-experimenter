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

import java.util.logging.Level;

/**
 * A class for making custom levels for logging.
 *
 * @author Ronald Kramp - Finalist IT Group
 * @version $Revision: 1.1 $, $Date: 2004/11/12 14:06:44 $
 */
class CustomLevel extends Level {

   /** a Level for DEBUG */
   public static final Level DEBUG = new CustomLevel("DEBUG", Level.INFO.intValue() - 1);

   /** a Level for ERROR */
   public static final Level ERROR = new CustomLevel("ERROR", Level.SEVERE.intValue() - 1);

   /** a Level for FATAL */
   public static final Level FATAL = new CustomLevel("FATAL", Level.SEVERE.intValue() + 1);


   /**
    * Constrcutor for making a custom level.
    * @param name, the name of the Level
    * @param value, the value for the level
    */
   public CustomLevel(String name, int value) {
      super(name, value);
   }


   /**
    * Parse a levelName to a Level object.
    * @param levelName the name of the Level to parse to a Level obhect
    * @return Level the Level object parsed
    */
   public static Level parse(String levelName) {
      if (levelName.equals("DEBUG")) {
         return DEBUG;
      }
      else if (levelName.equals("ERROR")) {
         return ERROR;
      }
      else if (levelName.equals("FATAL")) {
         return FATAL;
      }
      else {
         return Level.parse(levelName);
      }
   }
}