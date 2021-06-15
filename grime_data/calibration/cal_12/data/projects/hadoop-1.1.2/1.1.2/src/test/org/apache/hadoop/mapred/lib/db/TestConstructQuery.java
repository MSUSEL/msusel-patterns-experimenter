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
package org.apache.hadoop.mapred.lib.db;

import org.apache.hadoop.io.NullWritable;

import junit.framework.TestCase;

public class TestConstructQuery extends TestCase {
  public void testConstructQuery() {
    DBOutputFormat<DBWritable, NullWritable> format = new DBOutputFormat<DBWritable, NullWritable>();
    String expected = "INSERT INTO hadoop_output (id,name,value) VALUES (?,?,?);";
    String[] fieldNames = new String[] { "id", "name", "value" };
    String actual = format.constructQuery("hadoop_output", fieldNames);
    assertEquals(expected, actual);
    expected = "INSERT INTO hadoop_output VALUES (?,?,?);";
    fieldNames = new String[] { null, null, null };
    actual = format.constructQuery("hadoop_output", fieldNames);
    assertEquals(expected, actual);
  }
}
