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
package org.apache.hadoop.conf;

import junit.framework.Assert;

import org.apache.hadoop.mapred.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import java.io.*;

/**
 * This testcase tests that a JobConf without default values submits jobs
 * properly and the JT applies its own default values to it to make the job
 * run properly.
 */
public class TestNoDefaultsJobConf extends HadoopTestCase {

  public TestNoDefaultsJobConf() throws IOException {
    super(HadoopTestCase.CLUSTER_MR, HadoopTestCase.DFS_FS, 1, 1);
  }

  public void testNoDefaults() throws Exception {
    JobConf configuration = new JobConf();
    assertTrue(configuration.get("hadoop.tmp.dir", null) != null);

    configuration = new JobConf(false);
    assertTrue(configuration.get("hadoop.tmp.dir", null) == null);


    Path inDir = new Path("testing/jobconf/input");
    Path outDir = new Path("testing/jobconf/output");

    OutputStream os = getFileSystem().create(new Path(inDir, "text.txt"));
    Writer wr = new OutputStreamWriter(os);
    wr.write("hello\n");
    wr.write("hello\n");
    wr.close();

    JobConf conf = new JobConf(false);

    //seeding JT and NN info into non-defaults (empty jobconf)
    conf.set("mapred.job.tracker", createJobConf().get("mapred.job.tracker"));
    conf.set("fs.default.name", createJobConf().get("fs.default.name"));

    conf.setJobName("mr");

    conf.setInputFormat(TextInputFormat.class);

    conf.setMapOutputKeyClass(LongWritable.class);
    conf.setMapOutputValueClass(Text.class);

    conf.setOutputFormat(TextOutputFormat.class);
    conf.setOutputKeyClass(LongWritable.class);
    conf.setOutputValueClass(Text.class);

    conf.setMapperClass(org.apache.hadoop.mapred.lib.IdentityMapper.class);
    conf.setReducerClass(org.apache.hadoop.mapred.lib.IdentityReducer.class);

    FileInputFormat.setInputPaths(conf, inDir);

    FileOutputFormat.setOutputPath(conf, outDir);

    JobClient.runJob(conf);

    Path[] outputFiles = FileUtil.stat2Paths(
                   getFileSystem().listStatus(outDir,
                   new Utils.OutputFileUtils.OutputFilesFilter()));
    if (outputFiles.length > 0) {
      InputStream is = getFileSystem().open(outputFiles[0]);
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String line = reader.readLine();
      int counter = 0;
      while (line != null) {
        counter++;
        assertTrue(line.contains("hello"));
        line = reader.readLine();
      }
      reader.close();
      assertEquals(2, counter);
    }

  }

}