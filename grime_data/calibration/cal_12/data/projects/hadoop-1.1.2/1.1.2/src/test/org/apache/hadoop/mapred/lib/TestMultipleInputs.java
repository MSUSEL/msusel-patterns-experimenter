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

import java.io.IOException;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.KeyValueTextInputFormat;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;

/**
 * @see TestDelegatingInputFormat
 */
public class TestMultipleInputs extends TestCase {
  
  public void testAddInputPathWithFormat() {
    final JobConf conf = new JobConf();
    MultipleInputs.addInputPath(conf, new Path("/foo"), TextInputFormat.class);
    MultipleInputs.addInputPath(conf, new Path("/bar"),
        KeyValueTextInputFormat.class);
    final Map<Path, InputFormat> inputs = MultipleInputs
       .getInputFormatMap(conf);
    assertEquals(TextInputFormat.class, inputs.get(new Path("/foo")).getClass());
    assertEquals(KeyValueTextInputFormat.class, inputs.get(new Path("/bar"))
       .getClass());
  }

  public void testAddInputPathWithMapper() {
    final JobConf conf = new JobConf();
    MultipleInputs.addInputPath(conf, new Path("/foo"), TextInputFormat.class,
       MapClass.class);
    MultipleInputs.addInputPath(conf, new Path("/bar"),
       KeyValueTextInputFormat.class, MapClass2.class);
    final Map<Path, InputFormat> inputs = MultipleInputs
       .getInputFormatMap(conf);
    final Map<Path, Class<? extends Mapper>> maps = MultipleInputs
       .getMapperTypeMap(conf);

    assertEquals(TextInputFormat.class, inputs.get(new Path("/foo")).getClass());
    assertEquals(KeyValueTextInputFormat.class, inputs.get(new Path("/bar"))
       .getClass());
    assertEquals(MapClass.class, maps.get(new Path("/foo")));
    assertEquals(MapClass2.class, maps.get(new Path("/bar")));
  }

  static class MapClass implements Mapper<String, String, String, String> {

    public void map(String key, String value,
       OutputCollector<String, String> output, Reporter reporter)
       throws IOException {
    }

    public void configure(JobConf job) {
    }

    public void close() throws IOException {
    }
  }

  static class MapClass2 extends MapClass {
  }
}
