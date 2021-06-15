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
package org.apache.hadoop.mapreduce.lib.input;

import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;

public class TestFileInputFormat extends TestCase {

  public void testAddInputPath() throws IOException {
    final Configuration conf = new Configuration();
    conf.set("fs.default.name", "s3://abc:xyz@hostname/");
    final Job j = new Job(conf);

    //setup default fs
    final FileSystem defaultfs = FileSystem.get(conf);
    System.out.println("defaultfs.getUri() = " + defaultfs.getUri());

    {
      //test addInputPath
      final Path original = new Path("file:/foo");
      System.out.println("original = " + original);
      FileInputFormat.addInputPath(j, original);
      final Path[] results = FileInputFormat.getInputPaths(j);
      System.out.println("results = " + Arrays.asList(results));
      assertEquals(1, results.length);
      assertEquals(original, results[0]);
    }

    {
      //test setInputPaths
      final Path original = new Path("file:/bar");
      System.out.println("original = " + original);
      FileInputFormat.setInputPaths(j, original);
      final Path[] results = FileInputFormat.getInputPaths(j);
      System.out.println("results = " + Arrays.asList(results));
      assertEquals(1, results.length);
      assertEquals(original, results[0]);
    }
  }

}
