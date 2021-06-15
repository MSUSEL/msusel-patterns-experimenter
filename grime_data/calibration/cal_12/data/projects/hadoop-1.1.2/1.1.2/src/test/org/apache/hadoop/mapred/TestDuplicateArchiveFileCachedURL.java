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

import java.io.IOException;
import java.io.FileNotFoundException;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.TestCase;

import org.junit.Test;

import org.apache.hadoop.filecache.DistributedCache;

import org.apache.hadoop.util.Tool;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapred.lib.IdentityMapper;
import org.apache.hadoop.mapred.lib.IdentityReducer;

public class TestDuplicateArchiveFileCachedURL extends TestCase {
  private static String DUPLICATED_URL_CORE = "file://foo/myapp/map.zip";
  private static String UNDUPLICATED_URL_CORE = "file://foo/myapp/something-else.zip";

  private enum Symbolicness { SYMLINK, NOLINK };
  private enum Erroneasness { ERROR, NO_ERROR };

  int outputDirectoryIndex = 0;

  @Test
  public void testArchivesFilesJobSubmisisions() throws Exception {
    final Symbolicness SYMLINK = Symbolicness.SYMLINK;
    final Symbolicness NOLINK = Symbolicness.NOLINK;

    final Erroneasness ERROR = Erroneasness.ERROR;
    final Erroneasness NO_ERROR = Erroneasness.NO_ERROR;

    URI fileURI = new URI(DUPLICATED_URL_CORE);
    URI symlinkURI = new URI(DUPLICATED_URL_CORE + "#symlink");
    URI nonConflictingURI = new URI(UNDUPLICATED_URL_CORE);

    testSubmission(null, null, NOLINK, NO_ERROR);
    testSubmission(null, null, SYMLINK, NO_ERROR);

    testSubmission(fileURI, nonConflictingURI, NOLINK, NO_ERROR);
    testSubmission(fileURI, nonConflictingURI, SYMLINK, NO_ERROR);

    testSubmission(null, nonConflictingURI, NOLINK, NO_ERROR);
    testSubmission(null, nonConflictingURI, SYMLINK, NO_ERROR);

    testSubmission(fileURI, fileURI, NOLINK, ERROR);
    testSubmission(fileURI, symlinkURI, NOLINK, NO_ERROR);
    testSubmission(fileURI, symlinkURI, SYMLINK, ERROR);
  }

  private static class NullMapper
      implements Mapper<NullWritable,Text,NullWritable,Text> {
    public void map(NullWritable key, Text val,
        OutputCollector<NullWritable,Text> output, Reporter reporter)
        throws IOException {
      output.collect(NullWritable.get(), val);
    }
    public void configure(JobConf conf) { }
    public void close() { }
  }

  private void testSubmission(URI archive, URI file,
                              Symbolicness symbolicness,
                              Erroneasness expectError) {
    JobConf conf = null;
    final String testDescription
      = (" archives = {" + (archive == null ? "" : archive.toString())
         + "}, file = {"
         + (file == null ? "" : file.toString()) + "}, "
         + symbolicness);

    try {
      conf = new JobConf(TestDuplicateArchiveFileCachedURL.class);
      final FileSystem fs = FileSystem.getLocal(conf);
      final Path testdir
        = new Path(System.getProperty("test.build.data","/tmp")).makeQualified(fs);
      Path inFile = new Path(testdir, "nullin/blah");
      SequenceFile.Writer w
        = SequenceFile.createWriter(fs, conf, inFile,
                                    NullWritable.class, Text.class,
                                    SequenceFile.CompressionType.NONE);
      Text t = new Text();
      t.set("AAAAAAAAAAAAAA"); w.append(NullWritable.get(), t);
      t.set("BBBBBBBBBBBBBB"); w.append(NullWritable.get(), t);
      t.set("CCCCCCCCCCCCCC"); w.append(NullWritable.get(), t);
      t.set("DDDDDDDDDDDDDD"); w.append(NullWritable.get(), t);
      t.set("EEEEEEEEEEEEEE"); w.append(NullWritable.get(), t);
      t.set("FFFFFFFFFFFFFF"); w.append(NullWritable.get(), t);
      t.set("GGGGGGGGGGGGGG"); w.append(NullWritable.get(), t);
      t.set("HHHHHHHHHHHHHH"); w.append(NullWritable.get(), t);
      w.close();
      FileInputFormat.setInputPaths(conf, inFile);
      FileOutputFormat.setOutputPath
        (conf, new Path(testdir, "nullout" + ++outputDirectoryIndex));
      conf.setMapperClass(NullMapper.class);
      conf.setReducerClass(IdentityReducer.class);
      conf.setOutputKeyClass(NullWritable.class);
      conf.setOutputValueClass(Text.class);
      conf.setInputFormat(SequenceFileInputFormat.class);
      conf.setOutputFormat(SequenceFileOutputFormat.class);
      conf.setNumReduceTasks(1);

      if (symbolicness == Symbolicness.SYMLINK) {
        DistributedCache.createSymlink(conf);
      }

      if (archive != null) {
        System.out.println("adding archive: " + archive);
        DistributedCache.addCacheArchive(archive, conf);
      }

      if (file != null) {
        DistributedCache.addCacheFile(file, conf);
      }
    } catch (IOException e) {
      System.out.println("testSubmission -- got exception setting up a job.");
      e.printStackTrace();
    }

    try {
      JobClient.runJob(conf);

      assertTrue("A test, " + testDescription
                 + ", succeeded but should have failed.",
                 expectError == Erroneasness.NO_ERROR);
      System.out.println(testDescription
                         + " succeeded, as we expected.");
    } catch (InvalidJobConfException e) {
      assertTrue("A test, " + testDescription
                 + ", succeeded but should have failed.",
                 expectError == Erroneasness.ERROR);
      System.out.println(testDescription
                         + " failed on duplicated cached files,"
                         + " as we expected.");
    } catch (FileNotFoundException e) {
      assertEquals(testDescription
                   + "We shouldn't be unpacking files if there's a clash",
                   Erroneasness.NO_ERROR, expectError);
      System.out.println(testDescription + " got an expected "
                         + "FileNotFoundException because we"
                         + " don't provide cached files");
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue("During a test, " + testDescription
                    + ", runJob throws an IOException other"
                    + "than an InvalidJobConfException.",
                 false);
    }
  }
}
