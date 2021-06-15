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

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;

public class TestDBOutputFormat extends TestCase {
  
  private String[] fieldNames = new String[] { "id", "name", "value" };
  private String[] nullFieldNames = new String[] { null, null, null };
  private String expected = "INSERT INTO hadoop_output " +
                             "(id,name,value) VALUES (?,?,?);";
  private String nullExpected = "INSERT INTO hadoop_output VALUES (?,?,?);"; 
  
  private DBOutputFormat<DBWritable, NullWritable> format 
    = new DBOutputFormat<DBWritable, NullWritable>();
  
  public void testConstructQuery() {  
    String actual = format.constructQuery("hadoop_output", fieldNames);
    assertEquals(expected, actual);
    
    actual = format.constructQuery("hadoop_output", nullFieldNames);
    assertEquals(nullExpected, actual);
  }
  
  public void testSetOutput() throws IOException {
    Job job = new Job(new Configuration());
    DBOutputFormat.setOutput(job, "hadoop_output", fieldNames);
    
    DBConfiguration dbConf = new DBConfiguration(job.getConfiguration());
    String actual = format.constructQuery(dbConf.getOutputTableName()
        , dbConf.getOutputFieldNames());
    
    assertEquals(expected, actual);
    
    job = new Job(new Configuration());
    dbConf = new DBConfiguration(job.getConfiguration());
    DBOutputFormat.setOutput(job, "hadoop_output", nullFieldNames.length);
    assertNull(dbConf.getOutputFieldNames());
    assertEquals(nullFieldNames.length, dbConf.getOutputFieldCount());
    
    actual = format.constructQuery(dbConf.getOutputTableName()
        , new String[dbConf.getOutputFieldCount()]);
    
    assertEquals(nullExpected, actual);
  }
  
}
