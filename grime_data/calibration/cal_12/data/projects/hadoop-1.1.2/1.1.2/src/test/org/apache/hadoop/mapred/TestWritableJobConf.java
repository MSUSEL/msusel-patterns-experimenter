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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.serializer.Deserializer;
import org.apache.hadoop.io.serializer.SerializationFactory;
import org.apache.hadoop.io.serializer.Serializer;
import org.apache.hadoop.util.GenericsUtil;

public class TestWritableJobConf extends TestCase {

  private static final Configuration CONF = new Configuration();

  private <K> K serDeser(K conf) throws Exception {
    SerializationFactory factory = new SerializationFactory(CONF);
    Serializer<K> serializer =
      factory.getSerializer(GenericsUtil.getClass(conf));
    Deserializer<K> deserializer =
      factory.getDeserializer(GenericsUtil.getClass(conf));

    DataOutputBuffer out = new DataOutputBuffer();
    serializer.open(out);
    serializer.serialize(conf);
    serializer.close();

    DataInputBuffer in = new DataInputBuffer();
    in.reset(out.getData(), out.getLength());
    deserializer.open(in);
    K after = deserializer.deserialize(null);
    deserializer.close();
    return after;
  }

  private void assertEquals(Configuration conf1, Configuration conf2) {
    assertEquals(conf1.size(), conf2.size());

    Iterator<Map.Entry<String, String>> iterator1 = conf1.iterator();
    Map<String, String> map1 = new HashMap<String,String>();
    while (iterator1.hasNext()) {
      Map.Entry<String, String> entry = iterator1.next();
      map1.put(entry.getKey(), entry.getValue());
    }

    Iterator<Map.Entry<String, String>> iterator2 = conf1.iterator();
    Map<String, String> map2 = new HashMap<String,String>();
    while (iterator2.hasNext()) {
      Map.Entry<String, String> entry = iterator2.next();
      map2.put(entry.getKey(), entry.getValue());
    }

    assertEquals(map1, map2);
  }

  public void testEmptyConfiguration() throws Exception {
    JobConf conf = new JobConf();
    Configuration deser = serDeser(conf);
    assertEquals(conf, deser);
  }

  public void testNonEmptyConfiguration() throws Exception {
    JobConf conf = new JobConf();
    conf.set("a", "A");
    conf.set("b", "B");
    Configuration deser = serDeser(conf);
    assertEquals(conf, deser);
  }

  public void testConfigurationWithDefaults() throws Exception {
    JobConf conf = new JobConf(false);
    conf.set("a", "A");
    conf.set("b", "B");
    Configuration deser = serDeser(conf);
    assertEquals(conf, deser);
  }
  
}
