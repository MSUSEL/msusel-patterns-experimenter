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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.net.URI;

import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.IdentityMapper;
import org.apache.hadoop.mapred.lib.IdentityReducer;

import org.apache.hadoop.filecache.DistributedCache;

public class TestDuplicateArchiveFileCachedURLMinicluster extends ClusterMapReduceTestCase {
  
  enum EnumCounter { MAP_RECORDS }
  
  public void testDuplicationsMinicluster() throws Exception {
    OutputStream os = getFileSystem().create(new Path(getInputDir(), "text.txt"));
    Writer wr = new OutputStreamWriter(os);
    wr.write("hello1\n");
    wr.write("hello2\n");
    wr.write("hello3\n");
    wr.write("hello4\n");
    wr.close();

    JobConf conf = createJobConf();
    conf.setJobName("counters");
    
    conf.setInputFormat(TextInputFormat.class);

    conf.setMapOutputKeyClass(LongWritable.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputFormat(TextOutputFormat.class);
    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(IdentityMapper.class);
    conf.setReducerClass(IdentityReducer.class);

    FileInputFormat.setInputPaths(conf, getInputDir());

    FileOutputFormat.setOutputPath(conf, getOutputDir());

    Path inputRoot = getInputDir().makeQualified(getFileSystem());
    Path unqualifiedInputRoot = getInputDir();
    System.out.println("The qualified input dir is " + inputRoot.toString());
    System.out.println("The unqualified input dir is " + unqualifiedInputRoot.toString());

    Path duplicatedPath = new Path(inputRoot, "text.txt");
    URI duplicatedURI = duplicatedPath.toUri();

    Path unqualifiedDuplicatedPath = new Path(unqualifiedInputRoot, "text.txt");
    URI unqualifiedDuplicatedURI = unqualifiedDuplicatedPath.toUri();

    System.out.println("The duplicated Path is " + duplicatedPath);
    System.out.println("The duplicated URI is " + duplicatedURI);
    System.out.println("The unqualified duplicated URI is " + unqualifiedDuplicatedURI);

    DistributedCache.addCacheArchive(duplicatedURI, conf);
    DistributedCache.addCacheFile(unqualifiedDuplicatedURI, conf);

    try {
      RunningJob runningJob = JobClient.runJob(conf);

      assertFalse("The job completed, which is wrong since there's a duplication", true);
    } catch (InvalidJobConfException e) {
      System.out.println("We expect to see a stack trace here.");
      e.printStackTrace(System.out);
    }
  }
  
  public void testApparentDuplicationsMinicluster() throws Exception {
    OutputStream os = getFileSystem().create(new Path(getInputDir(), "text2.txt"));
    Writer wr = new OutputStreamWriter(os);
    wr.write("hello1\n");
    wr.write("hello2\n");
    wr.write("hello3\n");
    wr.write("hello4\n");
    wr.close();

    JobConf conf = createJobConf();
    conf.setJobName("counters");
    
    conf.setInputFormat(TextInputFormat.class);

    conf.setMapOutputKeyClass(LongWritable.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputFormat(TextOutputFormat.class);
    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(IdentityMapper.class);
    conf.setReducerClass(IdentityReducer.class);

    final FileSystem lfs = FileSystem.getLocal(conf);

    FileInputFormat.setInputPaths(conf, getInputDir());

    FileOutputFormat.setOutputPath(conf, getOutputDir());

    Path localInputRoot = getInputDir().makeQualified(lfs);
    Path dfsInputRoot = getInputDir().makeQualified(getFileSystem());
    Path unqualifiedInputRoot = getInputDir();
    System.out.println("The qualified input dir is " + dfsInputRoot.toString());
    System.out.println("The unqualified input dir is " + unqualifiedInputRoot.toString());

    Path dfsUnqualPath = new Path(unqualifiedInputRoot, "text2.txt");
    Path dfsQualPath = new Path(dfsInputRoot, "test2.text");
    Path localQualPath = new Path(localInputRoot, "test2.text");

    System.out.println("The dfs unqualified Path is " + dfsUnqualPath);
    System.out.println("The dfs qualified Path is " + dfsQualPath);
    System.out.println("The local qualified path is " + localQualPath);

    DistributedCache.addCacheArchive(localQualPath.toUri(), conf);
    DistributedCache.addCacheFile(dfsUnqualPath.toUri(), conf);
    DistributedCache.addCacheFile(dfsQualPath.toUri(), conf);

    try {
      RunningJob runningJob = JobClient.runJob(conf);

      assertFalse("The job completed, which is wrong since there's no local cached file", true);
    } catch (InvalidJobConfException e) {
      System.out.println("We expect to see a stack trace here.");
      e.printStackTrace(System.out);
      assertFalse("This error should not occur.", true);
    } catch (FileNotFoundException e) {
      System.out.println(" got an expected FileNotFoundException because we didn't provide cached files");
    }
  }
}
