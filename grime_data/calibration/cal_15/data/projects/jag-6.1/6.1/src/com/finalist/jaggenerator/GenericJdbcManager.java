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

import javax.swing.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author  hillie
 */
public class GenericJdbcManager {

   private String url;
   private String schema;
   private String username;
   private String password;
   private String clazz;
   private String dbName = "";
   private String[] displayTypes = null;
   private static final String SCHEMA_NAME_COLUMN = "TABLE_SCHEM";


   public GenericJdbcManager(String url, String username, String password, String clazz, String[] displayTypes) {
      this.url = url;
      this.username = username;
      this.password = password;
      this.clazz = clazz;
      this.displayTypes = displayTypes;

      int dbIndex = url.lastIndexOf("/");
      if (dbIndex != -1) {
         dbName = url.substring(dbIndex + 1);
      }

      //by default use the schema that maches the username - otherwise prompt for a schema..
      ArrayList allSchemas = new ArrayList();
      try {
         Connection cx = connect();
         ResultSet schemas = cx.getMetaData().getSchemas();
         while (schemas.next()) {
            String s = schemas.getString(SCHEMA_NAME_COLUMN);
            /* Do NOT ignore case here, otherwise the schema will not be selected correctly */
            if (username.equals(s)) {
               schema = username;
               break;
            }
            allSchemas.add(s);
         }

          // Only show if there are any schema's to select.
         if ((schema == null) && (allSchemas.size() != 0)) {
            schema = (String) JOptionPane.showInputDialog(
                  JagGenerator.jagGenerator,
                  "There is no schema called \"" + username + "\" in this database!\n\n" +
                  "Please choose the desired schema from this list, \n" +
                  "or press 'Cancel' to access all schemas.\n",
                  "Database schemas",
                  JOptionPane.QUESTION_MESSAGE,
                  null,
                  allSchemas.toArray(),
                  null);
         }

         JagGenerator.logToConsole("Using database schema: " + schema);

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public Connection connect() throws Exception {
      DriverManager.registerDriver((Driver) Class.forName(clazz).newInstance());
      return DriverManager.getConnection(url, username, password);
   }


   /** Getter for property url.
    * @return Value of property url.
    *
    */
   public String getUrl() {
      return this.url;
   }


   /** Setter for property url.
    * @param url New value of property url.
    *
    */
   public void setUrl(String url) {
      this.url = url;
   }


   /** Getter for property username.
    * @return Value of property username.
    *
    */
   public String getUsername() {
      return this.username;
   }


   /** Setter for property username.
    * @param username New value of property username.
    *
    */
   public void setUsername(String username) {
      this.username = username;
   }


   /** Getter for property password.
    * @return Value of property password.
    *
    */
   public String getPassword() {
      return this.password;
   }


   /** Setter for property password.
    * @param password New value of property password.
    *
    */
   public void setPassword(String password) {
      this.password = password;
   }


   /** Getter for property Database Name.
    * @return Value of database name.
    *
    *
    */
   public String getDBName() {
      return this.dbName;
   }


   /** Setter for property password.
    * @param dbName New value of property password.
    *
    *
    */
   public void setDBName(String dbName) {
      this.dbName = dbName;
   }

   /**
    * Return an array list of types that should be displayed while connecting
    * to the database. For example: TABLE, VIEW, SYNONYM
    */
   public String[] getDisplayTableTypes() {
      return this.displayTypes;
   }

   /**
    * Gets the currently database schema, or <code>null</code> if no schema is being used.
    * @return Value of database name.
    */
   public String getSchema() {
      return schema;
   }

}
