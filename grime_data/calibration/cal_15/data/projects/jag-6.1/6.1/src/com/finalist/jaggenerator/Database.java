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

import com.finalist.jaggenerator.modules.DatabaseManagerFrame;


/**
 * A bean that models a database supported by JAG.
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class Database {

   public static final String ENTER_DB_NAME = "-- type a name here --";

   private String dbName = ENTER_DB_NAME;
   private String driverClass;
   private String typeMapping = DatabaseManagerFrame.SELECT;
   private String filename;


   /**
    * Gets the human-friendly name of the database.
    * @return
    */
   public String getDbName() {
      return dbName;
   }

   public void setDbName(String dbName) {
      this.dbName = dbName;
   }

   /**
    * Gets the fully-qualified class name of the JDBC driver class used to access this database.
    * @return
    */
   public String getDriverClass() {
      return driverClass;
   }

   public void setDriverClass(String driverClass) {
      this.driverClass = driverClass;
   }

   /**
    * Gets the name of the 'type-mapping' needed by the application server
    * to know how to map this database's proprietary SQL functions and datatypes to Java.
    * @return
    * @todo this property may be application-server specific -maybe better to have a Map of these..?
    */
   public String getTypeMapping() {
      return typeMapping;
   }

   public void setTypeMapping(String typeMapping) {
      this.typeMapping = typeMapping;
   }

   /**
    * Gets the name (not the full file location) of the file containing the JDBC driver for this database.
    * <b>NOTE:</b> all instances of the character '\' (backslash) will be replaced by a forward slash!
    * @return
    */
   public String getFilename() {
      return filename.replace('\\', '/');
   }

   public void setFilename(String filename) {
      this.filename = filename;
   }


   /**
    * Display just the database name (used to represent the database in the select drop down list).
    *
    * @return just the name.
    */
   public String toString() {
      if (dbName != null) {
         return dbName;
      } else {
         return driverClass;
      }
   }


}
