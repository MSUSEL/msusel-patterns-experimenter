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
package org.apache.hadoop.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsShell;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.mapred.MiniMRCluster;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Level;

/**
 * test {@link HadoopArchives}
 */
public class TestHadoopArchives extends TestCase {
  private static final String inputDir = "input";

  private Path inputPath;
  private MiniDFSCluster dfscluster;
  private MiniMRCluster mapred;
  private FileSystem fs;
  private Path archivePath;
  
  static private Path createFile(Path dir, String filename, FileSystem fs
      ) throws IOException {
    final Path f = new Path(dir, filename);
    final FSDataOutputStream out = fs.create(f); 
    out.write(filename.getBytes());
    out.close();
    return f;
  }
  
  protected void setUp() throws Exception {
    super.setUp();
    dfscluster = new MiniDFSCluster(new Configuration(), 2, true, null);
    fs = dfscluster.getFileSystem();
    mapred = new MiniMRCluster(2, fs.getUri().toString(), 1);
    inputPath = new Path(fs.getHomeDirectory(), inputDir); 
    archivePath = new Path(fs.getHomeDirectory(), "archive");
    fs.mkdirs(inputPath);
    createFile(inputPath, "a", fs);
    createFile(inputPath, "b", fs);
    createFile(inputPath, "c", fs);
  }
  
  protected void tearDown() throws Exception {
    try {
      if (mapred != null) {
        mapred.shutdown();
      }
      if (dfscluster != null) {
        dfscluster.shutdown();
      }
    } catch(Exception e) {
      System.err.println(e);
    }
    super.tearDown();
  }
  
  
  public void testPathWithSpaces() throws Exception {
    fs.delete(archivePath, true);

    //create files/directories with spaces
    createFile(inputPath, "c c", fs);
    final Path sub1 = new Path(inputPath, "sub 1");
    fs.mkdirs(sub1);
    createFile(sub1, "file x y z", fs);
    createFile(sub1, "file", fs);
    createFile(sub1, "x", fs);
    createFile(sub1, "y", fs);
    createFile(sub1, "z", fs);
    final Path sub2 = new Path(inputPath, "sub 1 with suffix");
    fs.mkdirs(sub2);
    createFile(sub2, "z", fs);
    final Configuration conf = mapred.createJobConf();
    final FsShell shell = new FsShell(conf);

    final String inputPathStr = inputPath.toUri().getPath();

    final List<String> originalPaths = lsr(shell, inputPathStr);
    final URI uri = fs.getUri();
    final String prefix = "har://hdfs-" + uri.getHost() +":" + uri.getPort()
        + archivePath.toUri().getPath() + Path.SEPARATOR;

    {//Enable space replacement
      final String harName = "foo.har";
      final String[] args = {
          "-archiveName",
          harName,
          "-p",
          inputPathStr,
          "*",
          archivePath.toString()
      };
      final HadoopArchives har = new HadoopArchives(mapred.createJobConf());
      assertEquals(0, ToolRunner.run(har, args));

      //compare results
      final List<String> harPaths = lsr(shell, prefix + harName);
      assertEquals(originalPaths, harPaths);
    }

  }

  private static List<String> lsr(final FsShell shell, String dir
      ) throws Exception {
    final ByteArrayOutputStream bytes = new ByteArrayOutputStream(); 
    final PrintStream out = new PrintStream(bytes);
    final PrintStream oldOut = System.out;
    final PrintStream oldErr = System.err;
    System.setOut(out);
    System.setErr(out);
    final String results;
    try {
      assertEquals(0, shell.run(new String[]{"-lsr", dir}));
      results = bytes.toString();
    } finally {
      IOUtils.closeStream(out);
      System.setOut(oldOut);
      System.setErr(oldErr);
    }

    final String dirname = dir.substring(dir.lastIndexOf(Path.SEPARATOR));
    final List<String> paths = new ArrayList<String>();
    for(StringTokenizer t = new StringTokenizer(results, "\n");
        t.hasMoreTokens(); ) {
      final String s = t.nextToken();
      final int i = s.indexOf(dirname);
      if (i >= 0) {
        paths.add(s.substring(i + dirname.length()));
      }
    }
    Collections.sort(paths);
    return paths;
  }
}
