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
package com.finalist.jaggenerator;

import java.text.StringCharacterIterator;

/**
 *
 * @author  hillie
 */
public class Utils {

   public static String toClassName(String unformatted) {
      try {
         if (unformatted.startsWith("T_")) {
            unformatted = unformatted.substring(2);
         }
         String s = format(unformatted);
         s = initCap(s);
         return s;
      } catch (Exception e) {
         return unformatted;
      }
   }

   /**
    * Format the name by only allowing lowercase.
    */
   public static String formatLowercase(String unformatted) {
      try {
         if (unformatted == null) {
             return null;
         }
         StringBuffer sb = new StringBuffer(unformatted.toLowerCase());
         StringBuffer formattedString = new StringBuffer();
         //remove underscores
         for (int i = 0; i < sb.length(); i++) {
             char c = sb.charAt(i);
             if (c >= 'a' && c <= 'z') {
                 formattedString.append(c);
             }
         }        
         return formattedString.toString();
      } catch (Exception e) {
         return unformatted;
      }
   }

   /**
    * Format the name by only allowing lowercase and uppercase and 
    * always start with uppercase.
    */
   public static String formatLowerAndUpperCase(String unformatted) {
      try {
         if (unformatted == null) {
             return null;
         }
         StringBuffer sb = new StringBuffer(unformatted);
         StringBuffer formattedString = new StringBuffer();
         //remove underscores
         for (int i = 0; i < sb.length(); i++) {
             char c = sb.charAt(i);
             if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
                 formattedString.append(c);
             }
         }        
         return firstToUpperCase(formattedString.toString());
      } catch (Exception e) {
         return unformatted;
      }
   }
   
   public static String format(String unformatted) {
      try {
         StringBuffer sb = new StringBuffer(unformatted.toLowerCase());
         //remove underscore and capitalize nest character
         int i = sb.toString().indexOf("_");
         while (i >= 0) {
            if (i > -1) sb.replace(i, i + 2, sb.substring(i + 1, i + 2).toUpperCase());
            i = sb.toString().indexOf("_");
         }
         //remove dash and capitalize nest character
         i = sb.toString().indexOf("-");
         while (i >= 0) {
            if (i > -1) sb.replace(i, i + 2, sb.substring(i + 1, i + 2).toUpperCase());
            i = sb.toString().indexOf("-");
         }
         return sb.toString();
      } catch (Exception e) {
         return unformatted;
      }
   }

   /**
    * Undoes a format.  Everything is capitalised and any uppercase character
    * forces a '_' to be prefixed, e.g. "theColumnName" is unformatted to "THE_COLUMN_NAME", and
    * "theATrain" unformats to "THE_A_TRAIN".
    * @param formatted
    * @return the unformatted String.
    */
   public static String unformat(String formatted) {
      StringBuffer result = new StringBuffer();
      for (int i = 0; i < formatted.length(); i++) {
         if (Character.isUpperCase(formatted.charAt(i))) {
            result.append('_');
         }
         result.append(Character.toUpperCase(formatted.charAt(i)));
      }
      return result.toString();
   }

   public static String initCap(String unformatted) {
      StringBuffer sb = new StringBuffer(unformatted);
      //capitalize first character
      if ((unformatted != null) && (unformatted.length() > 0)) {
         sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
         return sb.toString();
      } else
         return unformatted;
   }

   public static String formatFKName(String fkColumnName) {
      return format(fkColumnName) + "FK";
   }

   public static String firstToLowerCase(String text) {
      return (text == null || text.length() == 0) ? "" : Character.toLowerCase(text.charAt(0)) +
            (text.length() > 1 ? text.substring(1) : "");
   }
   
   public static String firstToUpperCase(String text) {
      return (text == null || text.length() == 0) ? "" : Character.toUpperCase(text.charAt(0)) +
            (text.length() > 1 ? text.substring(1) : "");
   }
   
}
