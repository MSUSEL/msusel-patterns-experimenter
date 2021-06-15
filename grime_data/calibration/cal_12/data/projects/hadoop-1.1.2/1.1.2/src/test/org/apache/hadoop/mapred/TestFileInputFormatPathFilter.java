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

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import java.io.IOException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.HashSet;

public class TestFileInputFormatPathFilter extends TestCase {

  public static class DummyFileInputFormat extends FileInputFormat {

    public RecordReader getRecordReader(InputSplit split, JobConf job,
                                        Reporter reporter) throws IOException {
      return null;
    }

  }

  private static FileSystem localFs = null;

  static {
    try {
      localFs = FileSystem.getLocal(new JobConf());
    } catch (IOException e) {
      throw new RuntimeException("init failure", e);
    }
  }

  private static Path workDir =
      new Path(new Path(System.getProperty("test.build.data", "."), "data"),
          "TestFileInputFormatPathFilter");


  public void setUp() throws Exception {
    tearDown();
    localFs.mkdirs(workDir);
  }

  public void tearDown() throws Exception {
    if (localFs.exists(workDir)) {
      localFs.delete(workDir, true);
    }
  }

  protected Path createFile(String fileName) throws IOException {
    Path file = new Path(workDir, fileName);
    Writer writer = new OutputStreamWriter(localFs.create(file));
    writer.write("");
    writer.close();
    return localFs.makeQualified(file);
  }

  protected Set<Path> createFiles() throws IOException {
    Set<Path> files = new HashSet<Path>();
    files.add(createFile("a"));
    files.add(createFile("b"));
    files.add(createFile("aa"));
    files.add(createFile("bb"));
    files.add(createFile("_hello"));
    files.add(createFile(".hello"));
    return files;
  }


  public static class TestPathFilter implements PathFilter {

    public boolean accept(Path path) {
      String name = path.getName();
      return name.equals("TestFileInputFormatPathFilter") || name.length() == 1;
    }
  }

  private void _testInputFiles(boolean withFilter, boolean withGlob) throws Exception {
    Set<Path> createdFiles = createFiles();
    JobConf conf = new JobConf();

    Path inputDir = (withGlob) ? new Path(workDir, "a*") : workDir;
    FileInputFormat.setInputPaths(conf, inputDir);
    conf.setInputFormat(DummyFileInputFormat.class);

    if (withFilter) {
      FileInputFormat.setInputPathFilter(conf, TestPathFilter.class);
    }

    DummyFileInputFormat inputFormat =
        (DummyFileInputFormat) conf.getInputFormat();
    Set<Path> computedFiles = new HashSet<Path>();
    for (FileStatus file : inputFormat.listStatus(conf)) {
      computedFiles.add(file.getPath());
    }

    createdFiles.remove(localFs.makeQualified(new Path(workDir, "_hello")));
    createdFiles.remove(localFs.makeQualified(new Path(workDir, ".hello")));
    
    if (withFilter) {
      createdFiles.remove(localFs.makeQualified(new Path(workDir, "aa")));
      createdFiles.remove(localFs.makeQualified(new Path(workDir, "bb")));
    }

    if (withGlob) {
      createdFiles.remove(localFs.makeQualified(new Path(workDir, "b")));
      createdFiles.remove(localFs.makeQualified(new Path(workDir, "bb")));
    }
    assertEquals(createdFiles, computedFiles);
  }

  public void testWithoutPathFilterWithoutGlob() throws Exception {
    _testInputFiles(false, false);
  }

  public void testWithoutPathFilterWithGlob() throws Exception {
    _testInputFiles(false, true);
  }

  public void testWithPathFilterWithoutGlob() throws Exception {
    _testInputFiles(true, false);
  }

  public void testWithPathFilterWithGlob() throws Exception {
    _testInputFiles(true, true);
  }
}
