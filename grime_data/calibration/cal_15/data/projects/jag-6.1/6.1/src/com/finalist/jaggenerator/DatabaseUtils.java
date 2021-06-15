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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DatabaseMetaData;

/**
 * This class is a refactoring mid-step.  The original code contained duplicate methods for database access
 * in different classes - I've rehoused them here.
 *
 * @author Michael O'Connor, Rudie Ekkelenkamp - Finalist IT Group
 */
public class DatabaseUtils {
   static Log log = LogFactory.getLog(DatabaseUtils.class);

   //a map of table name (String) --> ArrayList of Column objects.
   private static final HashMap columnsCache = new HashMap();

   //a list of table names (String)
   private static ArrayList tablesCache;

   //I think this can be refactored out, once getPrimaryKeys() reference in JagGenerator is removed..
   private static final HashMap pkCache = new HashMap();
   private static final String TABLE_NAME = "TABLE_NAME";
   private static final String[] DEFAULT_TABLE_TYPES = new String[]{"TABLE"};


   /**
    * Gets all columns in the specified table, setting up a database connection if one isn't already available.
    *
    * @param tablename the name of the table.
    * @return an ArrayList of Column objects for all columns in the specified table,
    *         or <code>null</code> if the table/column doesn't exist.
    */
   public static ArrayList getColumns(String tablename) {
      return getColumns(tablename, true);
   }

   /**
    * Gets all columns in the specified table.
    *
    * @param tablename       the name of the table.
    * @param forceConnection set to <code>true</code> if this method should force a database connect,
    *                        if not already connected.
    * @return an ArrayList of Column objects for all columns in the specified table;
    *         or <code>null</code> if the table/column doesn't exist, or if no database connection was available and
    *         <code>forceConnection</code> was set to <code>false</code>.
    */
   public static ArrayList getColumns(String tablename, boolean forceConnection) {
      if (columnsCache.get(tablename) != null) {
         return (ArrayList) columnsCache.get(tablename);
      }

      if (!forceConnection && JagGenerator.getConManager() == null) {
         return null;
      }
      ArrayList pkeys = getPrimaryKeys(tablename);
      GenericJdbcManager conManager = JagGenerator.getConManager();
      Connection con = null;
      ArrayList list = new ArrayList();
      try {
         con = conManager.connect();
         DatabaseMetaData meta = con.getMetaData();
         ResultSet columns = meta.getColumns(null, conManager.getSchema(), tablename, "%");
         Column c = null;
         while (columns.next()) {
            c = new Column();
            switch (columns.getInt("NULLABLE")) {
               case DatabaseMetaData.columnNullable:
                  c.setNullable(true);
                  break;
               case DatabaseMetaData.columnNoNulls:
                  c.setNullable(false);
                  break;
               case DatabaseMetaData.columnNullableUnknown:
                  c.setNullable(false);
               default:
                  c.setNullable(true);
            }

            c.setName(columns.getString("COLUMN_NAME"));
            if (pkeys.contains(columns.getString("COLUMN_NAME"))) {
               c.setPrimaryKey(true);
            } else {
               c.setPrimaryKey(false);
            }

            c.setLength(columns.getInt("COLUMN_SIZE"));
            c.setPrecision(columns.getInt("COLUMN_SIZE"));
            c.setScale(columns.getInt("DECIMAL_DIGITS"));
            c.setSqlType(columns.getString("TYPE_NAME"));
            list.add(c);
         }
         columns.close();

      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (con != null) {
            try {
               con.close();
            } catch (SQLException e) {
            }
         }
      }

      columnsCache.put(tablename, list);
      return list;
   }

   /**
    * Gets information about any foreign keys that are imported into the specified table.
    *
    * @param tablename
    * @return a List of ForeignKey objects, never <code>null</code>..
    */
   public static List getForeignKeys(String tablename) {
      log.debug("Get the foreign keys for table: " + tablename);
      ArrayList fkeys = new ArrayList();
      GenericJdbcManager conManager = JagGenerator.getConManager();
      if (conManager == null) {
         JagGenerator.logToConsole("Can't retrieve foreign keys - no database connection!");
      } else {
         Connection con = null;
         try {
            con = conManager.connect();
            ResultSet foreignKeys = con.getMetaData().getImportedKeys("", conManager.getSchema(), tablename);

            while (foreignKeys.next()) {
               ForeignKey fk = new ForeignKey();
               try {
                  fk.setPkTableCat(foreignKeys.getString("PKTABLE_CAT"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {
                  fk.setPkTableSchem(foreignKeys.getString("PKTABLE_SCHEM"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {
                  fk.setPkTableName(foreignKeys.getString("PKTABLE_NAME"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {

                  fk.setPkColumnName(foreignKeys.getString("PKCOLUMN_NAME"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {
                  fk.setFkTableCat(foreignKeys.getString("FKTABLE_CAT"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {

                  fk.setFkTableSchem(foreignKeys.getString("FKTABLE_SCHEM"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {

                  fk.setFkTableName(foreignKeys.getString("FKTABLE_NAME"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {

                  fk.setFkColumnName(foreignKeys.getString("FKCOLUMN_NAME"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {

                  fk.setKeySeq(foreignKeys.getShort("KEY_SEQ"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {
                  fk.setUpdateRule(foreignKeys.getShort("UPDATE_RULE"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {

                  fk.setDeleteRule(foreignKeys.getShort("DELETE_RULE"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {

                  fk.setPkName(foreignKeys.getString("PK_NAME"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               try {

                  fk.setDeferrability(foreignKeys.getShort("DEFERRABILITY"));
               } catch (Exception e) {
                  // Ignore, in case the JDBC driver doesn't support this propery.
               }
               // Log the tables and columns from which relations are generated.
               log.debug("Foreign key table and column name: " + fk.getFkTableName() + " - " + fk.getFkColumnName());
               log.debug("foreign table and pk column name: " + fk.getPkTableName() + " - " + fk.getPkColumnName());

               // Since the fk name is not set, we use the column name to set it.s
               fk.setFkName(Utils.format(fk.getFkColumnName()));
               fkeys.add(fk);
            }
         } catch (Exception e) {
            e.printStackTrace();
         } finally {
            if (con != null) {
               try {
                  con.close();
               } catch (SQLException e) {
               }
            }
         }
      }

      return fkeys;
   }

   /**
    * A list with Strings of all primary key fields.
    *
    * @param tablename
    * @return an ArrayList of primary key column names for the specified table, never <code>null</code>.
    * @todo make this private - all primary key work should be done in this class.
    */
   public static ArrayList getPrimaryKeys(String tablename) {
      if (pkCache.get(tablename) != null) {
         return (ArrayList) pkCache.get(tablename);
      }
      GenericJdbcManager conManager = JagGenerator.getConManager();
      Connection con = null;
      ArrayList pkeys = new ArrayList();
      try {
         con = conManager.connect();
         ResultSet r = con.getMetaData().getPrimaryKeys(null, conManager.getSchema(), tablename);
         while (r.next()) {
            pkeys.add(r.getString("COLUMN_NAME"));
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         if (con != null) {
            try {
               con.close();
            } catch (SQLException e) {
            }
         }
      }

      pkCache.put(tablename, pkeys);
      return pkeys;
   }

   /**
    * Grabs the list of tables from the database.
    *
    * @return a List of table names (String), never <code>null</code>.
    */
   public static ArrayList getTables() {
      if (tablesCache == null) {
         tablesCache = new ArrayList();
         GenericJdbcManager conManager = JagGenerator.getConManager();
         String[] displayTableTypes = conManager.getDisplayTableTypes();
         if (displayTableTypes == null) {
            displayTableTypes = DEFAULT_TABLE_TYPES;
         }
         ResultSet tables = null;
         Connection con = null;
         try {
            con = conManager.connect();
            // Look for tables of the type table, not for synonyms: ,"SYNONYM"
            ResultSet schemas = con.getMetaData().getSchemas();
            while (schemas.next()) {
               // Do nothing.
            }
            tables = con.getMetaData().getTables(null, conManager.getSchema(), "%", displayTableTypes);
            while (tables.next()) {
               // Iterate over the tablenames and get the tablenames.
               String tableName = tables.getString(TABLE_NAME);
               if (tableName != null) {
                  tablesCache.add(tableName);
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
            JagGenerator.logToConsole("Error getting tables list: " + e.toString());
         } finally {
            if (tables != null)
               try {
                  tables.close();
               } catch (Exception e) {
               }

            if (con != null)
               try {
                  con.close();
               } catch (SQLException e) {
               }
         }
      }
      if (tablesCache != null)
         Collections.sort(tablesCache);
      return tablesCache;
   }

   /**
    * This needs to be called when databases are switched.
    */
   public static void clearCache() {
      tablesCache = null;
   }

   /**
    * Forces an update of a particular table's columns the next time they are required.
    *
    * @param tableName
    */
   public static void clearColumnsCacheForTable(String tableName) {
      columnsCache.remove(tableName);
   }

}
