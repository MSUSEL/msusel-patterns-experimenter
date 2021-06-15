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

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.util.Progressable;

/**
 * This test exercises the ValueIterator.
 */
public class TestReduceTask extends TestCase {

  static class NullProgress implements Progressable {
    public void progress() { }
  }

  private static class Pair {
    String key;
    String value;
    Pair(String k, String v) {
      key = k;
      value = v;
    }
  }
  private static Pair[][] testCases =
    new Pair[][]{
      new Pair[]{
                 new Pair("k1", "v1"),
                 new Pair("k2", "v2"),
                 new Pair("k3", "v3"),
                 new Pair("k3", "v4"),
                 new Pair("k4", "v5"),
                 new Pair("k5", "v6"),
      },
      new Pair[]{
                 new Pair("", "v1"),
                 new Pair("k1", "v2"),
                 new Pair("k2", "v3"),
                 new Pair("k2", "v4"),
      },
      new Pair[] {},
      new Pair[]{
                 new Pair("k1", "v1"),
                 new Pair("k1", "v2"),
                 new Pair("k1", "v3"),
                 new Pair("k1", "v4"),
      }
    };
  
  public void runValueIterator(Path tmpDir, Pair[] vals, 
                               Configuration conf, 
                               CompressionCodec codec) throws IOException {
    FileSystem localFs = FileSystem.getLocal(conf);
    FileSystem rfs = ((LocalFileSystem)localFs).getRaw();
    Path path = new Path(tmpDir, "data.in");
    IFile.Writer<Text, Text> writer = 
      new IFile.Writer<Text, Text>(conf, rfs, path, Text.class, Text.class,
                                   codec, null);
    for(Pair p: vals) {
      writer.append(new Text(p.key), new Text(p.value));
    }
    writer.close();
    
    @SuppressWarnings("unchecked")
    RawKeyValueIterator rawItr = 
      Merger.merge(conf, rfs, Text.class, Text.class, codec, new Path[]{path}, 
                   false, conf.getInt("io.sort.factor", 100), tmpDir, 
                   new Text.Comparator(), new NullProgress(),null,null);
    @SuppressWarnings("unchecked") // WritableComparators are not generic
    ReduceTask.ValuesIterator valItr = 
      new ReduceTask.ValuesIterator<Text,Text>(rawItr,
          WritableComparator.get(Text.class), Text.class, Text.class,
          conf, new NullProgress());
    int i = 0;
    while (valItr.more()) {
      Object key = valItr.getKey();
      String keyString = key.toString();
      // make sure it matches!
      assertEquals(vals[i].key, keyString);
      // must have at least 1 value!
      assertTrue(valItr.hasNext());
      while (valItr.hasNext()) {
        String valueString = valItr.next().toString();
        // make sure the values match
        assertEquals(vals[i].value, valueString);
        // make sure the keys match
        assertEquals(vals[i].key, valItr.getKey().toString());
        i += 1;
      }
      // make sure the key hasn't changed under the hood
      assertEquals(keyString, valItr.getKey().toString());
      valItr.nextKey();
    }
    assertEquals(vals.length, i);
    // make sure we have progress equal to 1.0
    assertEquals(1.0f, rawItr.getProgress().get());
  }

  public void testValueIterator() throws Exception {
    Path tmpDir = new Path("build/test/test.reduce.task");
    Configuration conf = new Configuration();
    for (Pair[] testCase: testCases) {
      runValueIterator(tmpDir, testCase, conf, null);
    }
  }
  
  public void testValueIteratorWithCompression() throws Exception {
    Path tmpDir = new Path("build/test/test.reduce.task.compression");
    Configuration conf = new Configuration();
    DefaultCodec codec = new DefaultCodec();
    codec.setConf(conf);
    for (Pair[] testCase: testCases) {
      runValueIterator(tmpDir, testCase, conf, codec);
    }
  }
}
