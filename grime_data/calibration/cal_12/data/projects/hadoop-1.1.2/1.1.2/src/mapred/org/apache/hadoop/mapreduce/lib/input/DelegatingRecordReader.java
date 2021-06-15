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

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * This is a delegating RecordReader, which delegates the functionality to the
 * underlying record reader in {@link TaggedInputSplit}  
 */
@InterfaceAudience.Private
@InterfaceStability.Unstable
public class DelegatingRecordReader<K, V> extends RecordReader<K, V> {
  RecordReader<K, V> originalRR;

  /**
   * Constructs the DelegatingRecordReader.
   * 
   * @param split TaggegInputSplit object
   * @param context TaskAttemptContext object
   *  
   * @throws IOException
   * @throws InterruptedException
   */
  @SuppressWarnings("unchecked")
  public DelegatingRecordReader(InputSplit split, TaskAttemptContext context)
      throws IOException, InterruptedException {
    // Find the InputFormat and then the RecordReader from the
    // TaggedInputSplit.
    TaggedInputSplit taggedInputSplit = (TaggedInputSplit) split;
    InputFormat<K, V> inputFormat = (InputFormat<K, V>) ReflectionUtils
        .newInstance(taggedInputSplit.getInputFormatClass(), context
            .getConfiguration());
    originalRR = inputFormat.createRecordReader(taggedInputSplit
        .getInputSplit(), context);
  }

  @Override
  public void close() throws IOException {
    originalRR.close();
  }

  @Override
  public K getCurrentKey() throws IOException, InterruptedException {
    return originalRR.getCurrentKey();
  }

  @Override
  public V getCurrentValue() throws IOException, InterruptedException {
    return originalRR.getCurrentValue();
  }

  @Override
  public float getProgress() throws IOException, InterruptedException {
    return originalRR.getProgress();
  }

  @Override
  public void initialize(InputSplit split, TaskAttemptContext context)
      throws IOException, InterruptedException {
    originalRR.initialize(((TaggedInputSplit) split).getInputSplit(), context);
  }

  @Override
  public boolean nextKeyValue() throws IOException, InterruptedException {
    return originalRR.nextKeyValue();
  }

}
