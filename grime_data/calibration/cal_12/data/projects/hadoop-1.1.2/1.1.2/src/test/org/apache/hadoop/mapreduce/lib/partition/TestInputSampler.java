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
package org.apache.hadoop.mapreduce.lib.partition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class TestInputSampler {

  static class SequentialSplit extends InputSplit {
    private int i;
    SequentialSplit(int i) {
      this.i = i;
    }
    public long getLength() { return 0; }
    public String[] getLocations() { return new String[0]; }
    public int getInit() { return i; }
  }

  static class TestInputSamplerIF
      extends InputFormat<IntWritable,NullWritable> {

    final int maxDepth;
    final ArrayList<InputSplit> splits = new ArrayList<InputSplit>();

    TestInputSamplerIF(int maxDepth, int numSplits, int... splitInit) {
      this.maxDepth = maxDepth;
      assert splitInit.length == numSplits;
      for (int i = 0; i < numSplits; ++i) {
        splits.add(new SequentialSplit(splitInit[i]));
      }
    }

    public List<InputSplit> getSplits(JobContext context)
        throws IOException, InterruptedException {
      return splits;
    }

    public RecordReader<IntWritable,NullWritable> createRecordReader(
        final InputSplit split, TaskAttemptContext context)
        throws IOException, InterruptedException {
      return new RecordReader<IntWritable,NullWritable>() {
        private int maxVal;
        private final IntWritable i = new IntWritable();
        public void initialize(InputSplit split, TaskAttemptContext context)
            throws IOException, InterruptedException {
          i.set(((SequentialSplit)split).getInit() - 1);
          maxVal = i.get() + maxDepth + 1;
        }
        public boolean nextKeyValue() {
          i.set(i.get() + 1);
          return i.get() < maxVal;
        }
        public IntWritable getCurrentKey() { return i; }
        public NullWritable getCurrentValue() { return NullWritable.get(); }
        public float getProgress() { return 1.0f; }
        public void close() { }
      };
    }

  }

  /**
   * Verify SplitSampler contract, that an equal number of records are taken
   * from the first splits.
   */
  @Test
  @SuppressWarnings("unchecked") // IntWritable comparator not typesafe
  public void testSplitSampler() throws Exception {
    final int TOT_SPLITS = 15;
    final int NUM_SPLITS = 5;
    final int STEP_SAMPLE = 5;
    final int NUM_SAMPLES = NUM_SPLITS * STEP_SAMPLE;
    InputSampler.Sampler<IntWritable,NullWritable> sampler =
      new InputSampler.SplitSampler<IntWritable,NullWritable>(
          NUM_SAMPLES, NUM_SPLITS);
    int inits[] = new int[TOT_SPLITS];
    for (int i = 0; i < TOT_SPLITS; ++i) {
      inits[i] = i * STEP_SAMPLE;
    }
    Job ignored = new Job();
    Object[] samples = sampler.getSample(
        new TestInputSamplerIF(100000, TOT_SPLITS, inits), ignored);
    assertEquals(NUM_SAMPLES, samples.length);
    Arrays.sort(samples, new IntWritable.Comparator());
    for (int i = 0; i < NUM_SAMPLES; ++i) {
      assertEquals(i, ((IntWritable)samples[i]).get());
    }
  }

  /**
   * Verify IntervalSampler contract, that samples are taken at regular
   * intervals from the given splits.
   */
  @Test
  @SuppressWarnings("unchecked") // IntWritable comparator not typesafe
  public void testIntervalSampler() throws Exception {
    final int TOT_SPLITS = 16;
    final int PER_SPLIT_SAMPLE = 4;
    final int NUM_SAMPLES = TOT_SPLITS * PER_SPLIT_SAMPLE;
    final double FREQ = 1.0 / TOT_SPLITS;
    InputSampler.Sampler<IntWritable,NullWritable> sampler =
      new InputSampler.IntervalSampler<IntWritable,NullWritable>(
          FREQ, NUM_SAMPLES);
    int inits[] = new int[TOT_SPLITS];
    for (int i = 0; i < TOT_SPLITS; ++i) {
      inits[i] = i;
    }
    Job ignored = new Job();
    Object[] samples = sampler.getSample(new TestInputSamplerIF(
          NUM_SAMPLES, TOT_SPLITS, inits), ignored);
    assertEquals(NUM_SAMPLES, samples.length);
    Arrays.sort(samples, new IntWritable.Comparator());
    for (int i = 0; i < NUM_SAMPLES; ++i) {
      assertEquals(i, ((IntWritable)samples[i]).get());
    }
  }

}
