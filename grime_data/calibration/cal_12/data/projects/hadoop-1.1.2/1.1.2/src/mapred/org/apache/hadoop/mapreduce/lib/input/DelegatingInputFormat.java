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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.classification.InterfaceStability;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.util.ReflectionUtils;

/**
 * An {@link InputFormat} that delegates behavior of paths to multiple other
 * InputFormats.
 * 
 * @see MultipleInputs#addInputPath(Job, Path, Class, Class)
 */
@InterfaceAudience.Private
@InterfaceStability.Unstable
public class DelegatingInputFormat<K, V> extends InputFormat<K, V> {

  @SuppressWarnings("unchecked")
  public List<InputSplit> getSplits(JobContext job) 
      throws IOException, InterruptedException {
    Configuration conf = job.getConfiguration();
    Job jobCopy =new Job(conf);
    List<InputSplit> splits = new ArrayList<InputSplit>();
    Map<Path, InputFormat> formatMap = 
      MultipleInputs.getInputFormatMap(job);
    Map<Path, Class<? extends Mapper>> mapperMap = MultipleInputs
       .getMapperTypeMap(job);
    Map<Class<? extends InputFormat>, List<Path>> formatPaths
        = new HashMap<Class<? extends InputFormat>, List<Path>>();

    // First, build a map of InputFormats to Paths
    for (Entry<Path, InputFormat> entry : formatMap.entrySet()) {
      if (!formatPaths.containsKey(entry.getValue().getClass())) {
       formatPaths.put(entry.getValue().getClass(), new LinkedList<Path>());
      }

      formatPaths.get(entry.getValue().getClass()).add(entry.getKey());
    }

    for (Entry<Class<? extends InputFormat>, List<Path>> formatEntry : 
        formatPaths.entrySet()) {
      Class<? extends InputFormat> formatClass = formatEntry.getKey();
      InputFormat format = (InputFormat) ReflectionUtils.newInstance(
         formatClass, conf);
      List<Path> paths = formatEntry.getValue();

      Map<Class<? extends Mapper>, List<Path>> mapperPaths
          = new HashMap<Class<? extends Mapper>, List<Path>>();

      // Now, for each set of paths that have a common InputFormat, build
      // a map of Mappers to the paths they're used for
      for (Path path : paths) {
       Class<? extends Mapper> mapperClass = mapperMap.get(path);
       if (!mapperPaths.containsKey(mapperClass)) {
         mapperPaths.put(mapperClass, new LinkedList<Path>());
       }

       mapperPaths.get(mapperClass).add(path);
      }

      // Now each set of paths that has a common InputFormat and Mapper can
      // be added to the same job, and split together.
      for (Entry<Class<? extends Mapper>, List<Path>> mapEntry :
          mapperPaths.entrySet()) {
       paths = mapEntry.getValue();
       Class<? extends Mapper> mapperClass = mapEntry.getKey();

       if (mapperClass == null) {
         try {
           mapperClass = job.getMapperClass();
         } catch (ClassNotFoundException e) {
           throw new IOException("Mapper class is not found", e);
         }
       }

       FileInputFormat.setInputPaths(jobCopy, paths.toArray(new Path[paths
           .size()]));

       // Get splits for each input path and tag with InputFormat
       // and Mapper types by wrapping in a TaggedInputSplit.
       List<InputSplit> pathSplits = format.getSplits(jobCopy);
       for (InputSplit pathSplit : pathSplits) {
         splits.add(new TaggedInputSplit(pathSplit, conf, format.getClass(),
             mapperClass));
       }
      }
    }

    return splits;
  }

  @Override
  public RecordReader<K, V> createRecordReader(InputSplit split,
      TaskAttemptContext context) throws IOException, InterruptedException {
    return new DelegatingRecordReader<K, V>(split, context);
  }
}
