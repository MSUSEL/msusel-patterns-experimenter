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

/**
 * This class is used to add special formatting capabilities to a String, as needed by the
 * JAG Velocity templates.  For example, in a template you can access the 'crazy struts'
 * format of an entity bean field name with something like <code>${entity.Name.CrazyStruts}</code>.
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class TemplateString {
   private String string;

   /**
    * Creates a TemplateString with a given String.
    *
    * @param string
    */
   public TemplateString(String string) {
      this.string = string;
   }

   /**
    * Lower cases the entire string.
    *
    * @return
    */
   public String getLower() {
      return string.toLowerCase();
   }

   /**
    * Upper cases the entire string.
    *
    * @return
    */
   public String getUpper() {
      return string.toUpperCase();
   }

   /**
    * Upper cases the first letter of the string.
    *
    * @return
    */
   public TemplateString getSentensized() {
      if (string.length() > 1) {
         return new TemplateString(string.substring(0, 1).toUpperCase() + string.substring(1));
      }
      return new TemplateString(string.toUpperCase());
   }

   /**
    * Lower cases the first letter of the string.
    *
    * @return
    */
   public TemplateString getDesentensized() {
      if (string.length() > 1) {
         return new TemplateString(string.substring(0, 1).toLowerCase() + string.substring(1));
      }
      return new TemplateString(string.toLowerCase());
   }

   /**
    * Formats the string to the strange format used by Struts:
    *
    * <li>anElephant --> anElephant</li>
    * <li>aHorse --> AHorse</li>
    *
    * @return
    */
   public String getCrazyStruts() {
      if (string.length() > 1 &&
            Character.isLowerCase(string.charAt(0)) && Character.isUpperCase(string.charAt(1))) {
         return Character.toUpperCase(string.charAt(0)) + string.substring(1);
      }
      return string;
   }

   /**
    * Applies a 'database-table to Java-classname' formatting, for example:
    * both "the_table" and "THE_TABLE" becomes "TheTable", and
    * "table-name" becomes "TableName".
    *
    * @return
    */
   public String getClassNameFormat() {
      if (string == null || string.trim().equals("")) {
         return string;
      }
      String temp = Character.toUpperCase(string.charAt(0)) +
            (string.length() > 1 ? string.substring(1).toLowerCase() : "");
      try {
         StringBuffer sb = new StringBuffer(temp);
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
         return string;
      }
   }

   public String toString() {
      return string;
   }

   /**
    * <b>NOTE:</b> To enable simple String comparisons, a TemplateString is equal to a String with the same value.
    * <p>
    * For example, the following template snippet is a valid equality check:<br>
    * <code>#if (${datasource.Mapping.equals("Oracle 8")}) ...</code>
    *
    * @param o
    * @return
    */
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof TemplateString || o instanceof String)) return false;
      final String other = o.toString();
      if (string != null ? !string.equals(other) : other != null) return false;
      return true;
   }

   public int hashCode() {
      return (string != null ? string.hashCode() : 0);
   }
}
