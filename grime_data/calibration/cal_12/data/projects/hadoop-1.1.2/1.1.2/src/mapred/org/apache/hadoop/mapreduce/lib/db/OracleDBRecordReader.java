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
package org.apache.hadoop.mapreduce.lib.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.lang.reflect.Method;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A RecordReader that reads records from an Oracle SQL table.
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class OracleDBRecordReader<T extends DBWritable> extends DBRecordReader<T> {

  /** Configuration key to set to a timezone string. */
  public static final String SESSION_TIMEZONE_KEY = "oracle.sessionTimeZone";

  private static final Log LOG = LogFactory.getLog(OracleDBRecordReader.class);

  public OracleDBRecordReader(DBInputFormat.DBInputSplit split, 
      Class<T> inputClass, Configuration conf, Connection conn, DBConfiguration dbConfig,
      String cond, String [] fields, String table) throws SQLException {
    super(split, inputClass, conf, conn, dbConfig, cond, fields, table);
    setSessionTimeZone(conf, conn);
  }

  /** Returns the query for selecting the records from an Oracle DB. */
  protected String getSelectQuery() {
    StringBuilder query = new StringBuilder();
    DBConfiguration dbConf = getDBConf();
    String conditions = getConditions();
    String tableName = getTableName();
    String [] fieldNames = getFieldNames();

    // Oracle-specific codepath to use rownum instead of LIMIT/OFFSET.
    if(dbConf.getInputQuery() == null) {
      query.append("SELECT ");
  
      for (int i = 0; i < fieldNames.length; i++) {
        query.append(fieldNames[i]);
        if (i != fieldNames.length -1) {
          query.append(", ");
        }
      }
  
      query.append(" FROM ").append(tableName);
      if (conditions != null && conditions.length() > 0)
        query.append(" WHERE ").append(conditions);
      String orderBy = dbConf.getInputOrderBy();
      if (orderBy != null && orderBy.length() > 0) {
        query.append(" ORDER BY ").append(orderBy);
      }
    } else {
      //PREBUILT QUERY
      query.append(dbConf.getInputQuery());
    }
        
    try {
      DBInputFormat.DBInputSplit split = getSplit();
      if (split.getLength() > 0 && split.getStart() > 0){
        String querystring = query.toString();

        query = new StringBuilder();
        query.append("SELECT * FROM (SELECT a.*,ROWNUM dbif_rno FROM ( ");
        query.append(querystring);
        query.append(" ) a WHERE rownum <= ").append(split.getStart());
        query.append(" + ").append(split.getLength());
        query.append(" ) WHERE dbif_rno >= ").append(split.getStart());
      }
    } catch (IOException ex) {
      // ignore, will not throw.
    }		      

    return query.toString();
  }

  /**
   * Set session time zone
   * @param conf The current configuration.
   * We read the 'oracle.sessionTimeZone' property from here.
   * @param conn The connection to alter the timezone properties of.
   */
  public static void setSessionTimeZone(Configuration conf,
      Connection conn) throws SQLException {
    // need to use reflection to call the method setSessionTimeZone on
    // the OracleConnection class because oracle specific java libraries are
    // not accessible in this context.
    Method method;
    try {
      method = conn.getClass().getMethod(
              "setSessionTimeZone", new Class [] {String.class});
    } catch (Exception ex) {
      LOG.error("Could not find method setSessionTimeZone in " + conn.getClass().getName(), ex);
      // rethrow SQLException
      throw new SQLException(ex);
    }

    // Need to set the time zone in order for Java
    // to correctly access the column "TIMESTAMP WITH LOCAL TIME ZONE".
    // We can't easily get the correct Oracle-specific timezone string
    // from Java; just let the user set the timezone in a property.
    String clientTimeZone = conf.get(SESSION_TIMEZONE_KEY, "GMT");
    try {
      method.setAccessible(true);
      method.invoke(conn, clientTimeZone);
      LOG.info("Time zone has been set to " + clientTimeZone);
    } catch (Exception ex) {
      LOG.warn("Time zone " + clientTimeZone +
               " could not be set on Oracle database.");
      LOG.warn("Setting default time zone: GMT");
      try {
        // "GMT" timezone is guaranteed to exist.
        method.invoke(conn, "GMT");
      } catch (Exception ex2) {
        LOG.error("Could not set time zone for oracle connection", ex2);
        // rethrow SQLException
        throw new SQLException(ex);
      }
    }
  }
}
