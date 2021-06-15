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
package org.apache.hadoop.mapred.lib;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class TestChainMapReduce extends HadoopTestCase {

  private static Path getFlagDir(boolean local) {
    Path flagDir = new Path("testing/chain/flags");

    // Hack for local FS that does not have the concept of a 'mounting point'
    if (local) {
      String localPathRoot = System.getProperty("test.build.data", "/tmp")
        .replace(' ', '+');
      flagDir = new Path(localPathRoot, flagDir);
    }
    return flagDir;
  }

  private static void cleanFlags(JobConf conf) throws IOException {
    FileSystem fs = FileSystem.get(conf);
    fs.delete(getFlagDir(conf.getBoolean("localFS", true)), true);
    fs.mkdirs(getFlagDir(conf.getBoolean("localFS", true)));
  }

  private static void writeFlag(JobConf conf, String flag) throws IOException {
    FileSystem fs = FileSystem.get(conf);
    if (getFlag(conf, flag)) {
      fail("Flag " + flag + " already exists");
    }
    DataOutputStream file =
      fs.create(new Path(getFlagDir(conf.getBoolean("localFS", true)), flag));
    file.close();
  }

  private static boolean getFlag(JobConf conf, String flag) throws IOException {
    FileSystem fs = FileSystem.get(conf);
    return fs
      .exists(new Path(getFlagDir(conf.getBoolean("localFS", true)), flag));
  }

  public TestChainMapReduce() throws IOException {
    super(HadoopTestCase.LOCAL_MR, HadoopTestCase.LOCAL_FS, 1, 1);
  }

  public void testChain() throws Exception {
    Path inDir = new Path("testing/chain/input");
    Path outDir = new Path("testing/chain/output");

    // Hack for local FS that does not have the concept of a 'mounting point'
    if (isLocalFS()) {
      String localPathRoot = System.getProperty("test.build.data", "/tmp")
        .replace(' ', '+');
      inDir = new Path(localPathRoot, inDir);
      outDir = new Path(localPathRoot, outDir);
    }


    JobConf conf = createJobConf();
    conf.setBoolean("localFS", isLocalFS());

    cleanFlags(conf);

    FileSystem fs = FileSystem.get(conf);

    fs.delete(outDir, true);
    if (!fs.mkdirs(inDir)) {
      throw new IOException("Mkdirs failed to create " + inDir.toString());
    }

    DataOutputStream file = fs.create(new Path(inDir, "part-0"));
    file.writeBytes("1\n2\n");
    file.close();

    conf.setJobName("chain");
    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    conf.set("a", "X");

    JobConf mapAConf = new JobConf(false);
    mapAConf.set("a", "A");
    ChainMapper.addMapper(conf, AMap.class, LongWritable.class, Text.class,
                          LongWritable.class, Text.class, true, mapAConf);

    ChainMapper.addMapper(conf, BMap.class, LongWritable.class, Text.class,
                          LongWritable.class, Text.class, false, null);

    JobConf reduceConf = new JobConf(false);
    reduceConf.set("a", "C");
    ChainReducer.setReducer(conf, CReduce.class, LongWritable.class, Text.class,
                            LongWritable.class, Text.class, true, reduceConf);

    ChainReducer.addMapper(conf, DMap.class, LongWritable.class, Text.class,
                           LongWritable.class, Text.class, false, null);

    JobConf mapEConf = new JobConf(false);
    mapEConf.set("a", "E");
    ChainReducer.addMapper(conf, EMap.class, LongWritable.class, Text.class,
                           LongWritable.class, Text.class, true, mapEConf);

    FileInputFormat.setInputPaths(conf, inDir);
    FileOutputFormat.setOutputPath(conf, outDir);

    JobClient jc = new JobClient(conf);
    RunningJob job = jc.submitJob(conf);
    while (!job.isComplete()) {
      Thread.sleep(100);
    }

    assertTrue(getFlag(conf, "configure.A"));
    assertTrue(getFlag(conf, "configure.B"));
    assertTrue(getFlag(conf, "configure.C"));
    assertTrue(getFlag(conf, "configure.D"));
    assertTrue(getFlag(conf, "configure.E"));

    assertTrue(getFlag(conf, "map.A.value.1"));
    assertTrue(getFlag(conf, "map.A.value.2"));
    assertTrue(getFlag(conf, "map.B.value.1"));
    assertTrue(getFlag(conf, "map.B.value.2"));
    assertTrue(getFlag(conf, "reduce.C.value.2"));
    assertTrue(getFlag(conf, "reduce.C.value.1"));
    assertTrue(getFlag(conf, "map.D.value.1"));
    assertTrue(getFlag(conf, "map.D.value.2"));
    assertTrue(getFlag(conf, "map.E.value.1"));
    assertTrue(getFlag(conf, "map.E.value.2"));

    assertTrue(getFlag(conf, "close.A"));
    assertTrue(getFlag(conf, "close.B"));
    assertTrue(getFlag(conf, "close.C"));
    assertTrue(getFlag(conf, "close.D"));
    assertTrue(getFlag(conf, "close.E"));
  }

  public static class AMap extends IDMap {
    public AMap() {
      super("A", "A", true);
    }
  }

  public static class BMap extends IDMap {
    public BMap() {
      super("B", "X", false);
    }
  }

  public static class CReduce extends IDReduce {
    public CReduce() {
      super("C", "C");
    }
  }

  public static class DMap extends IDMap {
    public DMap() {
      super("D", "X", false);
    }
  }

  public static class EMap extends IDMap {
    public EMap() {
      super("E", "E", true);
    }
  }

  public static class IDMap
    implements Mapper<LongWritable, Text, LongWritable, Text> {
    private JobConf conf;
    private String name;
    private String prop;
    private boolean byValue;

    public IDMap(String name, String prop, boolean byValue) {
      this.name = name;
      this.prop = prop;
      this.byValue = byValue;
    }

    public void configure(JobConf conf) {
      this.conf = conf;
      assertEquals(prop, conf.get("a"));
      try {
        writeFlag(conf, "configure." + name);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }

    public void map(LongWritable key, Text value,
                    OutputCollector<LongWritable, Text> output,
                    Reporter reporter) throws IOException {
      writeFlag(conf, "map." + name + ".value." + value);
      key.set(10);
      output.collect(key, value);
      if (byValue) {
        assertEquals(10, key.get());
      } else {
        assertNotSame(10, key.get());
      }
      key.set(11);
    }

    public void close() throws IOException {
      try {
        writeFlag(conf, "close." + name);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  public static class IDReduce
    implements Reducer<LongWritable, Text, LongWritable, Text> {

    private JobConf conf;
    private String name;
    private String prop;
    private boolean byValue = false;

    public IDReduce(String name, String prop) {
      this.name = name;
      this.prop = prop;
    }

    public void configure(JobConf conf) {
      this.conf = conf;
      assertEquals(prop, conf.get("a"));
      try {
        writeFlag(conf, "configure." + name);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }

    public void reduce(LongWritable key, Iterator<Text> values,
                       OutputCollector<LongWritable, Text> output,
                       Reporter reporter) throws IOException {
      while (values.hasNext()) {
        Text value = values.next();
        writeFlag(conf, "reduce." + name + ".value." + value);
        key.set(10);
        output.collect(key, value);
        if (byValue) {
          assertEquals(10, key.get());
        } else {
          assertNotSame(10, key.get());
        }
        key.set(11);
      }
    }

    public void close() throws IOException {
      try {
        writeFlag(conf, "close." + name);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

}
