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
package org.apache.hadoop.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.hadoop.conf.Configuration;

public class TestGenericsUtil extends TestCase {

  public void testToArray() {
    
    //test a list of size 10
    List<Integer> list = new ArrayList<Integer>(); 
    
    for(int i=0; i<10; i++) {
      list.add(i);
    }
    
    Integer[] arr = GenericsUtil.toArray(list);
    
    for (int i = 0; i < arr.length; i++) {
      assertEquals(list.get(i), arr[i]);
    }
  }
  
  public void testWithEmptyList() {
    try {
      List<String> list = new ArrayList<String>();
      String[] arr = GenericsUtil.toArray(list);
      fail("Empty array should throw exception");
      System.out.println(arr); //use arr so that compiler will not complain
      
    }catch (IndexOutOfBoundsException ex) {
      //test case is successful
    }
  }
 
  public void testWithEmptyList2() {
    List<String> list = new ArrayList<String>();
    //this method should not throw IndexOutOfBoundsException
    String[] arr = GenericsUtil.<String>toArray(String.class, list);
    
    assertEquals(0, arr.length);
  }
  
  /** This class uses generics */
  private class GenericClass<T> {
    T dummy;
    List<T> list = new ArrayList<T>();
    
    void add(T item) {
      list.add(item);
    }
    
    T[] funcThatUsesToArray() {
      T[] arr = GenericsUtil.toArray(list);
      return arr;
    }
  }
  
  public void testWithGenericClass() {
    
    GenericClass<String> testSubject = new GenericClass<String>();
    
    testSubject.add("test1");
    testSubject.add("test2");
    
    try {
      //this cast would fail, if we had not used GenericsUtil.toArray, since the 
      //rmethod would return Object[] rather than String[]
      String[] arr = testSubject.funcThatUsesToArray();
      
      assertEquals("test1", arr[0]);
      assertEquals("test2", arr[1]);
      
    }catch (ClassCastException ex) {
      fail("GenericsUtil#toArray() is not working for generic classes");
    }
    
  }
  
  public void testGenericOptionsParser() throws Exception {
     GenericOptionsParser parser = new GenericOptionsParser(
        new Configuration(), new String[] {"-jt"});
    assertEquals(parser.getRemainingArgs().length, 0);
    
    // test if -D accepts -Dx=y=z
    parser = 
      new GenericOptionsParser(new Configuration(), 
                               new String[] {"-Dx=y=z"});
    assertEquals(parser.getConfiguration().get("x"), "y=z");
  }
  
  public void testGetClass() {
    
    //test with Integer
    Integer x = new Integer(42); 
    Class<Integer> c = GenericsUtil.getClass(x);
    assertEquals(Integer.class, c);
    
    //test with GenericClass<Integer>
    GenericClass<Integer> testSubject = new GenericClass<Integer>();
    Class<GenericClass<Integer>> c2 = GenericsUtil.getClass(testSubject);
    assertEquals(GenericClass.class, c2);
  }
  
}
