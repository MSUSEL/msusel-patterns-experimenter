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
package org.apache.hadoop.contrib.index.mapred;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.contrib.index.lucene.FileSystemDirectory;
import org.apache.hadoop.contrib.index.lucene.LuceneUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

/**
 * An implementation of an index updater interface which creates a Map/Reduce
 * job configuration and run the Map/Reduce job to analyze documents and update
 * Lucene instances in parallel.
 */
public class IndexUpdater implements IIndexUpdater {
  public static final Log LOG = LogFactory.getLog(IndexUpdater.class);

  public IndexUpdater() {
  }

  /* (non-Javadoc)
   * @see org.apache.hadoop.contrib.index.mapred.IIndexUpdater#run(org.apache.hadoop.conf.Configuration, org.apache.hadoop.fs.Path[], org.apache.hadoop.fs.Path, int, org.apache.hadoop.contrib.index.mapred.Shard[])
   */
  public void run(Configuration conf, Path[] inputPaths, Path outputPath,
      int numMapTasks, Shard[] shards) throws IOException {
    JobConf jobConf =
        createJob(conf, inputPaths, outputPath, numMapTasks, shards);
    JobClient.runJob(jobConf);
  }

  JobConf createJob(Configuration conf, Path[] inputPaths, Path outputPath,
      int numMapTasks, Shard[] shards) throws IOException {
    // set the starting generation for each shard
    // when a reduce task fails, a new reduce task
    // has to know where to re-start
    setShardGeneration(conf, shards);

    // iconf.set sets properties in conf
    IndexUpdateConfiguration iconf = new IndexUpdateConfiguration(conf);
    Shard.setIndexShards(iconf, shards);

    // MapTask.MapOutputBuffer uses "io.sort.mb" to decide its max buffer size
    // (max buffer size = 1/2 * "io.sort.mb").
    // Here we half-en "io.sort.mb" because we use the other half memory to
    // build an intermediate form/index in Combiner.
    iconf.setIOSortMB(iconf.getIOSortMB() / 2);

    // create the job configuration
    JobConf jobConf = new JobConf(conf, IndexUpdater.class);
    jobConf.setJobName(this.getClass().getName() + "_"
        + System.currentTimeMillis());

    // provided by application
    FileInputFormat.setInputPaths(jobConf, inputPaths);
    FileOutputFormat.setOutputPath(jobConf, outputPath);

    jobConf.setNumMapTasks(numMapTasks);

    // already set shards
    jobConf.setNumReduceTasks(shards.length);

    jobConf.setInputFormat(iconf.getIndexInputFormatClass());

    Path[] inputs = FileInputFormat.getInputPaths(jobConf);
    StringBuilder buffer = new StringBuilder(inputs[0].toString());
    for (int i = 1; i < inputs.length; i++) {
      buffer.append(",");
      buffer.append(inputs[i].toString());
    }
    LOG.info("mapred.input.dir = " + buffer.toString());
    LOG.info("mapred.output.dir = " + 
             FileOutputFormat.getOutputPath(jobConf).toString());
    LOG.info("mapred.map.tasks = " + jobConf.getNumMapTasks());
    LOG.info("mapred.reduce.tasks = " + jobConf.getNumReduceTasks());
    LOG.info(shards.length + " shards = " + iconf.getIndexShards());
    // better if we don't create the input format instance
    LOG.info("mapred.input.format.class = "
        + jobConf.getInputFormat().getClass().getName());

    // set by the system
    jobConf.setMapOutputKeyClass(IndexUpdateMapper.getMapOutputKeyClass());
    jobConf.setMapOutputValueClass(IndexUpdateMapper.getMapOutputValueClass());
    jobConf.setOutputKeyClass(IndexUpdateReducer.getOutputKeyClass());
    jobConf.setOutputValueClass(IndexUpdateReducer.getOutputValueClass());

    jobConf.setMapperClass(IndexUpdateMapper.class);
    jobConf.setPartitionerClass(IndexUpdatePartitioner.class);
    jobConf.setCombinerClass(IndexUpdateCombiner.class);
    jobConf.setReducerClass(IndexUpdateReducer.class);

    jobConf.setOutputFormat(IndexUpdateOutputFormat.class);

    return jobConf;
  }

  void setShardGeneration(Configuration conf, Shard[] shards)
      throws IOException {
    FileSystem fs = FileSystem.get(conf);

    for (int i = 0; i < shards.length; i++) {
      Path path = new Path(shards[i].getDirectory());
      long generation = -1;

      if (fs.exists(path)) {
        FileSystemDirectory dir = null;

        try {
          dir = new FileSystemDirectory(fs, path, false, conf);
          generation = LuceneUtil.getCurrentSegmentGeneration(dir);
        } finally {
          if (dir != null) {
            dir.close();
          }
        }
      }

      if (generation != shards[i].getGeneration()) {
        // set the starting generation for the shard
        shards[i] =
            new Shard(shards[i].getVersion(), shards[i].getDirectory(),
                generation);
      }
    }
  }
}
