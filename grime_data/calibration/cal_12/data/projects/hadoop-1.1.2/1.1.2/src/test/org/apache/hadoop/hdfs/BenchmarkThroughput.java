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
package org.apache.hadoop.hdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.ChecksumFileSystem;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalDirAllocator;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.apache.log4j.Level;

/**
 * This class benchmarks the performance of the local file system, raw local
 * file system and HDFS at reading and writing files. The user should invoke
 * the main of this class and optionally include a repetition count.
 */
public class BenchmarkThroughput extends Configured implements Tool {

  // the property in the config that specifies a working directory
  private LocalDirAllocator dir;
  private long startTime;
  // the size of the buffer to use
  private int BUFFER_SIZE;

  private void resetMeasurements() {
    startTime = System.currentTimeMillis();
  }

  private void printMeasurements() {
    System.out.println(" time: " +
                       ((System.currentTimeMillis() - startTime)/1000));
  }

  private Path writeLocalFile(String name, Configuration conf,
                                     long total) throws IOException {
    Path path = dir.getLocalPathForWrite(name, total, conf);
    System.out.print("Writing " + name);
    resetMeasurements();
    OutputStream out = new FileOutputStream(new File(path.toString()));
    byte[] data = new byte[BUFFER_SIZE];
    for(long size=0; size < total; size += BUFFER_SIZE) {
      out.write(data);
    }
    out.close();
    printMeasurements();
    return path;
  }

  private void readLocalFile(Path path,
                                    String name,
                                    Configuration conf) throws IOException {
    System.out.print("Reading " + name);
    resetMeasurements();
    InputStream in = new FileInputStream(new File(path.toString()));
    byte[] data = new byte[BUFFER_SIZE];
    long size = 0;
    while (size >= 0) {
      size = in.read(data);
    }
    in.close();
    printMeasurements();
  }

  private void writeAndReadLocalFile(String name,
                                            Configuration conf,
                                            long size
                                           ) throws IOException {
    Path f = null;
    try {
      f = writeLocalFile(name, conf, size);
      readLocalFile(f, name, conf);
    } finally {
      if (f != null) {
        new File(f.toString()).delete();
      }
    }
  }

  private Path writeFile(FileSystem fs,
                                String name,
                                Configuration conf,
                                long total
                                ) throws IOException {
    Path f = dir.getLocalPathForWrite(name, total, conf);
    System.out.print("Writing " + name);
    resetMeasurements();
    OutputStream out = fs.create(f);
    byte[] data = new byte[BUFFER_SIZE];
    for(long size = 0; size < total; size += BUFFER_SIZE) {
      out.write(data);
    }
    out.close();
    printMeasurements();
    return f;
  }

  private void readFile(FileSystem fs,
                               Path f,
                               String name,
                               Configuration conf
                               ) throws IOException {
    System.out.print("Reading " + name);
    resetMeasurements();
    InputStream in = fs.open(f);
    byte[] data = new byte[BUFFER_SIZE];
    long val = 0;
    while (val >= 0) {
      val = in.read(data);
    }
    in.close();
    printMeasurements();
  }

  private void writeAndReadFile(FileSystem fs,
                                       String name,
                                       Configuration conf,
                                       long size
                                       ) throws IOException {
    Path f = null;
    try {
      f = writeFile(fs, name, conf, size);
      readFile(fs, f, name, conf);
    } finally {
      try {
        if (f != null) {
          fs.delete(f, true);
        }
      } catch (IOException ie) {
        // IGNORE
      }
    }
  }

  private static void printUsage() {
    ToolRunner.printGenericCommandUsage(System.err);
    System.err.println("Usage: dfsthroughput [#reps]");
    System.err.println("Config properties:\n" +
      "  dfsthroughput.file.size:\tsize of each write/read (10GB)\n" +
      "  dfsthroughput.buffer.size:\tbuffer size for write/read (4k)\n");
  }

  public int run(String[] args) throws IOException {
    // silence the minidfs cluster
    Log hadoopLog = LogFactory.getLog("org");
    if (hadoopLog instanceof Log4JLogger) {
      ((Log4JLogger) hadoopLog).getLogger().setLevel(Level.WARN);
    }
    int reps = 1;
    if (args.length == 1) {
      try {
        reps = Integer.parseInt(args[0]);
      } catch (NumberFormatException e) {
        printUsage();
        return -1;
      }
    } else if (args.length > 1) {
      printUsage();
      return -1;
    }
    Configuration conf = getConf();
    // the size of the file to write
    long SIZE = conf.getLong("dfsthroughput.file.size",
        10L * 1024 * 1024 * 1024);
    BUFFER_SIZE = conf.getInt("dfsthroughput.buffer.size", 4 * 1024);

    String localDir = conf.get("mapred.temp.dir");
    dir = new LocalDirAllocator("mapred.temp.dir");

    System.setProperty("test.build.data", localDir);
    System.out.println("Local = " + localDir);
    ChecksumFileSystem checkedLocal = FileSystem.getLocal(conf);
    FileSystem rawLocal = checkedLocal.getRawFileSystem();
    for(int i=0; i < reps; ++i) {
      writeAndReadLocalFile("local", conf, SIZE);
      writeAndReadFile(rawLocal, "raw", conf, SIZE);
      writeAndReadFile(checkedLocal, "checked", conf, SIZE);
    }
    MiniDFSCluster cluster = null;
    try {
      cluster = new MiniDFSCluster(conf, 1, true, new String[]{"/foo"});
      cluster.waitActive();
      FileSystem dfs = cluster.getFileSystem();
      for(int i=0; i < reps; ++i) {
        writeAndReadFile(dfs, "dfs", conf, SIZE);
      }
    } finally {
      if (cluster != null) {
        cluster.shutdown();
        // clean up minidfs junk
        rawLocal.delete(new Path(localDir, "dfs"), true);
      }
    }
    return 0;
  }

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(),
        new BenchmarkThroughput(), args);
    System.exit(res);
  }

}
