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
package org.apache.hadoop.mapred;

import junit.framework.TestCase;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.StringUtils;

public class TestInputPath extends TestCase {
  public void testInputPath() throws Exception {
    JobConf jobConf = new JobConf();
    Path workingDir = jobConf.getWorkingDirectory();
    
    Path path = new Path(workingDir, 
        "xx{y"+StringUtils.COMMA_STR+"z}");
    FileInputFormat.setInputPaths(jobConf, path);
    Path[] paths = FileInputFormat.getInputPaths(jobConf);
    assertEquals(1, paths.length);
    assertEquals(path.toString(), paths[0].toString());
	    
    StringBuilder pathStr = new StringBuilder();
    pathStr.append(StringUtils.ESCAPE_CHAR);
    pathStr.append(StringUtils.ESCAPE_CHAR);
    pathStr.append(StringUtils.COMMA);
    pathStr.append(StringUtils.COMMA);
    pathStr.append('a');
    path = new Path(workingDir, pathStr.toString());
    FileInputFormat.setInputPaths(jobConf, path);
    paths = FileInputFormat.getInputPaths(jobConf);
    assertEquals(1, paths.length);
    assertEquals(path.toString(), paths[0].toString());
		    
    pathStr.setLength(0);
    pathStr.append(StringUtils.ESCAPE_CHAR);
    pathStr.append("xx");
    pathStr.append(StringUtils.ESCAPE_CHAR);
    path = new Path(workingDir, pathStr.toString());
    Path path1 = new Path(workingDir,
        "yy"+StringUtils.COMMA_STR+"zz");
    FileInputFormat.setInputPaths(jobConf, path);
    FileInputFormat.addInputPath(jobConf, path1);
    paths = FileInputFormat.getInputPaths(jobConf);
    assertEquals(2, paths.length);
    assertEquals(path.toString(), paths[0].toString());
    assertEquals(path1.toString(), paths[1].toString());

    FileInputFormat.setInputPaths(jobConf, path, path1);
    paths = FileInputFormat.getInputPaths(jobConf);
    assertEquals(2, paths.length);
    assertEquals(path.toString(), paths[0].toString());
    assertEquals(path1.toString(), paths[1].toString());

    Path[] input = new Path[] {path, path1};
    FileInputFormat.setInputPaths(jobConf, input);
    paths = FileInputFormat.getInputPaths(jobConf);
    assertEquals(2, paths.length);
    assertEquals(path.toString(), paths[0].toString());
    assertEquals(path1.toString(), paths[1].toString());
    
    pathStr.setLength(0);
    String str1 = "{a{b,c},de}";
    String str2 = "xyz";
    String str3 = "x{y,z}";
    pathStr.append(str1);
    pathStr.append(StringUtils.COMMA);
    pathStr.append(str2);
    pathStr.append(StringUtils.COMMA);
    pathStr.append(str3);
    FileInputFormat.setInputPaths(jobConf, pathStr.toString());
    paths = FileInputFormat.getInputPaths(jobConf);
    assertEquals(3, paths.length);
    assertEquals(new Path(workingDir, str1).toString(), paths[0].toString());
    assertEquals(new Path(workingDir, str2).toString(), paths[1].toString());
    assertEquals(new Path(workingDir, str3).toString(), paths[2].toString());

    pathStr.setLength(0);
    String str4 = "abc";
    String str5 = "pq{r,s}";
    pathStr.append(str4);
    pathStr.append(StringUtils.COMMA);
    pathStr.append(str5);
    FileInputFormat.addInputPaths(jobConf, pathStr.toString());
    paths = FileInputFormat.getInputPaths(jobConf);
    assertEquals(5, paths.length);
    assertEquals(new Path(workingDir, str1).toString(), paths[0].toString());
    assertEquals(new Path(workingDir, str2).toString(), paths[1].toString());
    assertEquals(new Path(workingDir, str3).toString(), paths[2].toString());
    assertEquals(new Path(workingDir, str4).toString(), paths[3].toString());
    assertEquals(new Path(workingDir, str5).toString(), paths[4].toString());
  }
}
