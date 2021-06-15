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
package org.apache.hadoop.io;

import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

public class TestDefaultStringifier extends TestCase {

  private static Configuration conf = new Configuration();
  private static final Log LOG = LogFactory.getLog(TestDefaultStringifier.class);

  private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

  public void testWithWritable() throws Exception {

    conf.set("io.serializations", "org.apache.hadoop.io.serializer.WritableSerialization");

    LOG.info("Testing DefaultStringifier with Text");

    Random random = new Random();

    //test with a Text
    for(int i=0;i<10;i++) {
      //generate a random string
      StringBuilder builder = new StringBuilder();
      int strLen = random.nextInt(40);
      for(int j=0; j< strLen; j++) {
        builder.append(alphabet[random.nextInt(alphabet.length)]);
      }
      Text text = new Text(builder.toString());
      DefaultStringifier<Text> stringifier = new DefaultStringifier<Text>(conf, Text.class);

      String str = stringifier.toString(text);
      Text claimedText = stringifier.fromString(str);
      LOG.info("Object: " + text);
      LOG.info("String representation of the object: " + str);
      assertEquals(text, claimedText);
    }
  }

  public void testWithJavaSerialization() throws Exception {
    conf.set("io.serializations", "org.apache.hadoop.io.serializer.JavaSerialization");

    LOG.info("Testing DefaultStringifier with Serializable Integer");

    //Integer implements Serializable
    Integer testInt = Integer.valueOf(42);
    DefaultStringifier<Integer> stringifier = new DefaultStringifier<Integer>(conf, Integer.class);

    String str = stringifier.toString(testInt);
    Integer claimedInt = stringifier.fromString(str);
    LOG.info("String representation of the object: " + str);

    assertEquals(testInt, claimedInt);
  }

  public void testStoreLoad() throws IOException {

    LOG.info("Testing DefaultStringifier#store() and #load()");
    conf.set("io.serializations", "org.apache.hadoop.io.serializer.WritableSerialization");
    Text text = new Text("uninteresting test string");
    String keyName = "test.defaultstringifier.key1";

    DefaultStringifier.store(conf,text, keyName);

    Text claimedText = DefaultStringifier.load(conf, keyName, Text.class);
    assertEquals("DefaultStringifier#load() or #store() might be flawed"
        , text, claimedText);

  }

  public void testStoreLoadArray() throws IOException {
    LOG.info("Testing DefaultStringifier#storeArray() and #loadArray()");
    conf.set("io.serializations", "org.apache.hadoop.io.serializer.JavaSerialization");

    String keyName = "test.defaultstringifier.key2";

    Integer[] array = new Integer[] {1,2,3,4,5};


    DefaultStringifier.storeArray(conf, array, keyName);

    Integer[] claimedArray = DefaultStringifier.<Integer>loadArray(conf, keyName, Integer.class);
    for (int i = 0; i < array.length; i++) {
      assertEquals("two arrays are not equal", array[i], claimedArray[i]);
    }

  }

}
