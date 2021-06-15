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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;

/**
 * A RecordReader that reads records from a SQL table.
 * Emits LongWritables containing the record number as 
 * key and DBWritables as value.  
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class DBRecordReader<T extends DBWritable> extends
    RecordReader<LongWritable, T> {

  private static final Log LOG = LogFactory.getLog(DBRecordReader.class);

  private ResultSet results = null;

  private Class<T> inputClass;

  private Configuration conf;

  private DBInputFormat.DBInputSplit split;

  private long pos = 0;
  
  private LongWritable key = null;
  
  private T value = null;

  private Connection connection;

  protected PreparedStatement statement;

  private DBConfiguration dbConf;

  private String conditions;

  private String [] fieldNames;

  private String tableName;

  /**
   * @param split The InputSplit to read data for
   * @throws SQLException 
   */
  public DBRecordReader(DBInputFormat.DBInputSplit split, 
      Class<T> inputClass, Configuration conf, Connection conn, DBConfiguration dbConfig,
      String cond, String [] fields, String table)
      throws SQLException {
    this.inputClass = inputClass;
    this.split = split;
    this.conf = conf;
    this.connection = conn;
    this.dbConf = dbConfig;
    this.conditions = cond;
    this.fieldNames = fields;
    this.tableName = table;
  }

  protected ResultSet executeQuery(String query) throws SQLException {
    this.statement = connection.prepareStatement(query,
        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    return statement.executeQuery();
  }

  /** Returns the query for selecting the records, 
   * subclasses can override this for custom behaviour.*/
  protected String getSelectQuery() {
    StringBuilder query = new StringBuilder();

    // Default codepath for MySQL, HSQLDB, etc. Relies on LIMIT/OFFSET for splits.
    if(dbConf.getInputQuery() == null) {
      query.append("SELECT ");
  
      for (int i = 0; i < fieldNames.length; i++) {
        query.append(fieldNames[i]);
        if (i != fieldNames.length -1) {
          query.append(", ");
        }
      }

      query.append(" FROM ").append(tableName);
      query.append(" AS ").append(tableName); //in hsqldb this is necessary
      if (conditions != null && conditions.length() > 0) {
        query.append(" WHERE (").append(conditions).append(")");
      }

      String orderBy = dbConf.getInputOrderBy();
      if (orderBy != null && orderBy.length() > 0) {
        query.append(" ORDER BY ").append(orderBy);
      }
    } else {
      //PREBUILT QUERY
      query.append(dbConf.getInputQuery());
    }
        
    try {
      query.append(" LIMIT ").append(split.getLength());
      query.append(" OFFSET ").append(split.getStart());
    } catch (IOException ex) {
      // Ignore, will not throw.
    }		

    return query.toString();
  }

  /** {@inheritDoc} */
  public void close() throws IOException {
    try {
      if (null != results) {
        results.close();
      }
      if (null != statement) {
        statement.close();
      }
      if (null != connection) {
        connection.commit();
        connection.close();
      }
    } catch (SQLException e) {
      throw new IOException(e.getMessage());
    }
  }

  public void initialize(InputSplit split, TaskAttemptContext context) 
      throws IOException, InterruptedException {
    //do nothing
  }

  /** {@inheritDoc} */
  public LongWritable getCurrentKey() {
    return key;  
  }

  /** {@inheritDoc} */
  public T getCurrentValue() {
    return value;
  }

  /**
   * @deprecated 
   */
  @Deprecated
  public T createValue() {
    return ReflectionUtils.newInstance(inputClass, conf);
  }

  /**
   * @deprecated 
   */
  @Deprecated
  public long getPos() throws IOException {
    return pos;
  }

  /**
   * @deprecated Use {@link #nextKeyValue()}
   */
  @Deprecated
  public boolean next(LongWritable key, T value) throws IOException {
    this.key = key;
    this.value = value;
    return nextKeyValue();
  }

  /** {@inheritDoc} */
  public float getProgress() throws IOException {
    return pos / (float)split.getLength();
  }

  /** {@inheritDoc} */
  public boolean nextKeyValue() throws IOException {
    try {
      if (key == null) {
        key = new LongWritable();
      }
      if (value == null) {
        value = createValue();
      }
      if (null == this.results) {
        // First time into this method, run the query.
        this.results = executeQuery(getSelectQuery());
      }
      if (!results.next())
        return false;

      // Set the key field value as the output key value
      key.set(pos + split.getStart());

      value.readFields(results);

      pos ++;
    } catch (SQLException e) {
      throw new IOException("SQLException in nextKeyValue", e);
    }
    return true;
  }

  protected DBInputFormat.DBInputSplit getSplit() {
    return split;
  }

  protected String [] getFieldNames() {
    return fieldNames;
  }

  protected String getTableName() {
    return tableName;
  }

  protected String getConditions() {
    return conditions;
  }

  protected DBConfiguration getDBConf() {
    return dbConf;
  }

  protected Connection getConnection() {
    return connection;
  }

  protected PreparedStatement getStatement() {
    return statement;
  }

  protected void setStatement(PreparedStatement stmt) {
    this.statement = stmt;
  }
}
