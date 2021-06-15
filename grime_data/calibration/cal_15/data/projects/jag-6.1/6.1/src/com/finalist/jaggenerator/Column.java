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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

/**
 *
 * @author  hillebrand
 */
public class Column implements Serializable {
   static Log log = LogFactory.getLog(Column.class);
   /** Holds value of property name. */
   private String name;

   /** Holds value of property sqlType. */
   private String sqlType;

   /** Holds value of property precision. */
   private int precision;

   /** Holds value of property scale. */
   private int scale;

   /** Holds value of property length. */
   private int length;

   /** Set to true if columns is primary key column. */
   private boolean primaryKey;

   /** True if the column may be null. */
   private boolean nullable;


   /** Creates a new instance of Column */
   public Column() {
   }


   /** Getter for property name.
    * @return Value of property name.
    *
    */
   public String getName() {
      return this.name;
   }


   /** Setter for property name.
    * @param name New value of property name.
    *
    */
   public void setName(String name) {
      this.name = name;
   }


   /** Setter for property sqlType.
    * @param sqlType New value of property sqlType.
    *
    */
   public void setSqlType(String sqlType) {

      // Check if a precision and / or scale have been passed.
      if (sqlType != null) {
          int index = sqlType.indexOf("(");
          if (index != -1) {
              this.sqlType = sqlType.substring(0, index);
              // Now als set the scale and precision...
              int index2 = sqlType.indexOf(")");
              if (index2 != -1) {
                  // There is a scale and / or precsions.
                  int index3 = sqlType.indexOf(",");
                  if (index3 != -1) {
                      // Scale AND precision!
                    String scale = sqlType.substring(index + 1, index3).trim();
                    String precision = sqlType.substring(index3 + 1, index2).trim();
                    int sc = 0;
                    int pr = 0;
                    try {
                     sc = Integer.parseInt(scale);
                     pr = Integer.parseInt(precision);
                    } catch (Exception e) {
                        log.warn("Error while parsing scale or precision for type " + sqlType);
                    }
                    setScale(sc);
                    setPrecision(pr);
                  } else {
                      // Only a scale..
                      String scale = sqlType.substring(index + 1, index2).trim();
                      int sc = 0;
                      try {
                       sc = Integer.parseInt(scale);
                      } catch (Exception e) {
                          log.warn("Error while parsing scale for type " + sqlType);
                      }
                      setScale(sc);

                  }
              }
          } else {
            this.sqlType = sqlType;
          }

      }
   }


   /** Getter for property precision.
    * @return Value of property precision.
    *
    */
   public int getPrecision() {
      return this.precision;
   }


   /** Setter for property precision.
    * @param precision New value of property precision.
    *
    */
   public void setPrecision(int precision) {
      this.precision = precision;
   }


   /** Getter for property scale.
    * @return Value of property scale.
    *
    */
   public int getScale() {
      return this.scale;
   }


   /** Setter for property scale.
    * @param scale New value of property scale.
    *
    */
   public void setScale(int scale) {
      this.scale = scale;
   }


   /** Getter for property length.
    * @return Value of property length.
    *
    */
   public int getLength() {
      return this.length;
   }


   /** Setter for property length.
    * @param length New value of property length.
    *
    */
   public void setLength(int length) {
      this.length = length;
   }


   /** Getter for property sqlType.
    * @return Value of property sqlType.
    *
    */
   public String getSqlType() {
      return this.sqlType;
   }


   /** Getter for property primary Key
    * @return Value of primary key.
    *
    */
   public boolean isPrimaryKey() {
      return this.primaryKey;
   }


   /** Setter for the primary key.
    * @param value to true if column is a primary key column
    *
    */
   public void setPrimaryKey(boolean value) {
      this.primaryKey = value;
   }

   /**
    * Gets the 'nullableString' property as a boolean.
    *
    * @return the boolean interpretation of the 'nullableString' property.
    */
   public boolean isNullable() {
      return nullable;
   }

   /**
    * Sets the 'nullable' property with a value from a database columns dumo.
    * @param nullable set to <code>true</code> if the column is nullable.
    */
   public void setNullable(boolean nullable) {
      this.nullable = nullable;
   }

   /** @see {@link java.lang.Object#toString()}. */
   public String toString() {
      return "Column(name=" + name + ", sqlType=" + sqlType + ", precision=" + precision +
            ", scale=" + scale + ", length=" + length + ", primaryKey=" + primaryKey + ", nullable=" + nullable + ")";
   }


}
