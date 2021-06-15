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
package org.apache.hadoop.mapreduce.split;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

public class TestJobSplitWriter {

  static final String TEST_ROOT = System.getProperty("test.build.data", "/tmp");
  static final Path TEST_DIR =
    new Path(TEST_ROOT, TestJobSplitWriter.class.getSimpleName());

  @AfterClass
  public static void cleanup() throws IOException {
    final FileSystem fs = FileSystem.getLocal(new Configuration()).getRaw();
    fs.delete(TEST_DIR, true);
  }

  static abstract class NewSplit extends InputSplit implements Writable {
    @Override public long getLength() { return 42L; }
    @Override public void readFields(DataInput in) throws IOException { }
    @Override public void write(DataOutput in) throws IOException { }
  }

  @Test
  public void testSplitLocationLimit()
      throws IOException, InterruptedException  {
    final int SPLITS = 5;
    final int MAX_LOC = 10;
    final Path outdir = new Path(TEST_DIR, "testSplitLocationLimit");
    final String[] locs = getLoc(MAX_LOC + 5);
    final Configuration conf = new Configuration();
    final FileSystem rfs = FileSystem.getLocal(conf).getRaw();
    final InputSplit split = new NewSplit() {
      @Override public String[] getLocations() { return locs; }
    };
    List<InputSplit> splits = Collections.nCopies(SPLITS, split);

    conf.setInt(JobSplitWriter.MAX_SPLIT_LOCATIONS, MAX_LOC);
    JobSplitWriter.createSplitFiles(outdir, conf,
        FileSystem.getLocal(conf).getRaw(), splits);

    checkMeta(MAX_LOC,
        SplitMetaInfoReader.readSplitMetaInfo(null, rfs, conf, outdir),
        Arrays.copyOf(locs, MAX_LOC));

    conf.setInt(JobSplitWriter.MAX_SPLIT_LOCATIONS, MAX_LOC / 2);
    try {
      SplitMetaInfoReader.readSplitMetaInfo(null, rfs, conf, outdir);
      fail("Reader failed to detect location limit");
    } catch (IOException e) { }
  }

  static abstract class OldSplit
      implements org.apache.hadoop.mapred.InputSplit {
    @Override public long getLength() { return 42L; }
    @Override public void readFields(DataInput in) throws IOException { }
    @Override public void write(DataOutput in) throws IOException { }
  }

  @Test
  public void testSplitLocationLimitOldApi() throws IOException {
    final int SPLITS = 5;
    final int MAX_LOC = 10;
    final Path outdir = new Path(TEST_DIR, "testSplitLocationLimitOldApi");
    final String[] locs = getLoc(MAX_LOC + 5);
    final Configuration conf = new Configuration();
    final FileSystem rfs = FileSystem.getLocal(conf).getRaw();
    final org.apache.hadoop.mapred.InputSplit split = new OldSplit() {
      @Override public String[] getLocations() { return locs; }
    };
    org.apache.hadoop.mapred.InputSplit[] splits =
      new org.apache.hadoop.mapred.InputSplit[SPLITS];
    Arrays.fill(splits, split);

    conf.setInt(JobSplitWriter.MAX_SPLIT_LOCATIONS, MAX_LOC);
    JobSplitWriter.createSplitFiles(outdir, conf,
        FileSystem.getLocal(conf).getRaw(), splits);
    checkMeta(MAX_LOC,
        SplitMetaInfoReader.readSplitMetaInfo(null, rfs, conf, outdir),
        Arrays.copyOf(locs, MAX_LOC));

    conf.setInt(JobSplitWriter.MAX_SPLIT_LOCATIONS, MAX_LOC / 2);
    try {
      SplitMetaInfoReader.readSplitMetaInfo(null, rfs, conf, outdir);
      fail("Reader failed to detect location limit");
    } catch (IOException e) { }
  }

  private static void checkMeta(int MAX_LOC,
      JobSplit.TaskSplitMetaInfo[] metaSplits, String[] chk_locs) {
    for (JobSplit.TaskSplitMetaInfo meta : metaSplits) {
      final String[] meta_locs = meta.getLocations();
      assertEquals(MAX_LOC, meta_locs.length);
      assertArrayEquals(chk_locs, meta_locs);
    }
  }

  private static String[] getLoc(int locations) {
    final String ret[] = new String[locations];
    for (int i = 0; i < locations; ++i) {
      ret[i] = "LOC" + i;
    }
    return ret;
  }

}
