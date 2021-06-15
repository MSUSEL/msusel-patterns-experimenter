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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;

/**
 * TestCase for {@link GenericWritable} class.
 * @see TestWritable#testWritable(Writable)
 */
public class TestGenericWritable extends TestCase {

  private Configuration conf;
  public static final String CONF_TEST_KEY = "test.generic.writable";
  public static final String CONF_TEST_VALUE = "dummy";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    conf = new Configuration();
    //set the configuration parameter
    conf.set(CONF_TEST_KEY, CONF_TEST_VALUE);
  }

  /** Dummy class for testing {@link GenericWritable} */
  public static class Foo implements Writable {
    private String foo = "foo";
    public void readFields(DataInput in) throws IOException {
      foo = Text.readString(in);
    }
    public void write(DataOutput out) throws IOException {
      Text.writeString(out, foo);
    }
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Foo))
        return false;
      return this.foo.equals(((Foo)obj).foo);
    }
  }
  /** Dummy class for testing {@link GenericWritable} */
  public static class Bar implements Writable, Configurable {
    private int bar = 42; //The Answer to The Ultimate Question Of Life, the Universe and Everything
    private Configuration conf = null;
    public void readFields(DataInput in) throws IOException {
      bar = in.readInt();
    }
    public void write(DataOutput out) throws IOException {
      out.writeInt(bar);
    }
    public Configuration getConf() {
      return conf;
    }
    public void setConf(Configuration conf) {
      this.conf = conf;
    }
    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof Bar))
        return false;
      return this.bar == ((Bar)obj).bar;
    }
  }

  /** Dummy class for testing {@link GenericWritable} */
  public static class Baz extends Bar {
    @Override
    public void readFields(DataInput in) throws IOException {
      super.readFields(in);
      //needs a configuration parameter
      assertEquals("Configuration is not set for the wrapped object", 
          CONF_TEST_VALUE, getConf().get(CONF_TEST_KEY)); 
    }
    @Override
    public void write(DataOutput out) throws IOException {
      super.write(out);
    }
  }

  /** Dummy class for testing {@link GenericWritable} */ 
  public static class FooGenericWritable extends GenericWritable {
    @Override
    @SuppressWarnings("unchecked")
    protected Class<? extends Writable>[] getTypes() {
      return new Class[] {Foo.class, Bar.class, Baz.class};
    }
    @Override
    public boolean equals(Object obj) {
      if(! (obj instanceof FooGenericWritable))
        return false;
      return get().equals(((FooGenericWritable)obj).get());
    }
  }

  public void testFooWritable() throws Exception {
    System.out.println("Testing Writable wrapped in GenericWritable");
    FooGenericWritable generic = new FooGenericWritable();
    generic.setConf(conf);
    Foo foo = new Foo();
    generic.set(foo);
    TestWritable.testWritable(generic);
  }

  public void testBarWritable() throws Exception {
    System.out.println("Testing Writable, Configurable wrapped in GenericWritable");
    FooGenericWritable generic = new FooGenericWritable();
    generic.setConf(conf);
    Bar bar = new Bar();
    bar.setConf(conf);
    generic.set(bar);

    //test writing generic writable
    FooGenericWritable after 
    = (FooGenericWritable)TestWritable.testWritable(generic, conf);

    //test configuration
    System.out.println("Testing if Configuration is passed to wrapped classes");
    assertTrue(after.get() instanceof Configurable);
    assertNotNull(((Configurable)after.get()).getConf());
  }

  public void testBazWritable() throws Exception {
    System.out.println("Testing for GenericWritable to find class names");
    FooGenericWritable generic = new FooGenericWritable();
    generic.setConf(conf);
    Baz baz = new Baz();
    generic.set(baz);
    TestWritable.testWritable(generic, conf);
  }

  public void testSet() throws Exception {
    Foo foo = new Foo();
    FooGenericWritable generic = new FooGenericWritable();
    //exception should not occur
    generic.set(foo);

    try {
      //exception should occur, since IntWritable is not registered
      generic = new FooGenericWritable();
      generic.set(new IntWritable(1));
      fail("Generic writable should have thrown an exception for a Writable not registered");
    }catch (RuntimeException e) {
      //ignore
    }

  }

  public void testGet() throws Exception {
    Foo foo = new Foo();
    FooGenericWritable generic = new FooGenericWritable();
    generic.set(foo);
    assertEquals(foo, generic.get());
  }

}
