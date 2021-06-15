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
package org.apache.hadoop.io.serializer;

import static org.apache.hadoop.io.TestGenericWritable.CONF_TEST_KEY;
import static org.apache.hadoop.io.TestGenericWritable.CONF_TEST_VALUE;
import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DataInputBuffer;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.TestGenericWritable.Baz;
import org.apache.hadoop.io.TestGenericWritable.FooGenericWritable;
import org.apache.hadoop.util.GenericsUtil;

public class TestWritableSerialization extends TestCase {

  private static final Configuration conf = new Configuration();
  
  static {
    conf.set("io.serializations"
        , "org.apache.hadoop.io.serializer.WritableSerialization");
  }
  
  public void testWritableSerialization() throws Exception {
    Text before = new Text("test writable"); 
    testSerialization(conf, before);
  }
  
  
  public void testWritableConfigurable() throws Exception {
    
    //set the configuration parameter
    conf.set(CONF_TEST_KEY, CONF_TEST_VALUE);

    //reuse TestGenericWritable inner classes to test 
    //writables that also implement Configurable.
    FooGenericWritable generic = new FooGenericWritable();
    generic.setConf(conf);
    Baz baz = new Baz();
    generic.set(baz);
    Baz result = testSerialization(conf, baz);
    assertNotNull(result.getConf());
  }
  
  /**
   * A utility that tests serialization/deserialization. 
   * @param <K> the class of the item
   * @param conf configuration to use, "io.serializations" is read to 
   * determine the serialization
   * @param before item to (de)serialize
   * @return deserialized item
   */
  public static<K> K testSerialization(Configuration conf, K before) 
    throws Exception {
    
    SerializationFactory factory = new SerializationFactory(conf);
    Serializer<K> serializer 
      = factory.getSerializer(GenericsUtil.getClass(before));
    Deserializer<K> deserializer 
      = factory.getDeserializer(GenericsUtil.getClass(before));
   
    DataOutputBuffer out = new DataOutputBuffer();
    serializer.open(out);
    serializer.serialize(before);
    serializer.close();
    
    DataInputBuffer in = new DataInputBuffer();
    in.reset(out.getData(), out.getLength());
    deserializer.open(in);
    K after = deserializer.deserialize(null);
    deserializer.close();
    
    assertEquals(before, after);
    return after;
  }
  
}
