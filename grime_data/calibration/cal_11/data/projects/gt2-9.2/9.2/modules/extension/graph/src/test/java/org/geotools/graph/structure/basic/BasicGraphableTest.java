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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.graph.structure.basic;

import java.util.Iterator;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class BasicGraphableTest extends TestCase {

  private BasicGraphable m_graphable;
  
  public BasicGraphableTest(String name) {
    super(name);
  }
	
  protected void setUp() throws Exception {
    super.setUp();
  
    m_graphable = new BasicGraphable() {
      public Iterator getRelated() {
        return null;
      }
    };
  }
  
  /**
   * Test BasicGraphable#setVisited(boolean) method. <BR>
   * <BR>
   * Test: Clear visited flag, then reset.<BR>
   * Expected: Visited flag should be set.
   *
   */
  public void test_setVisited() {
    assertTrue(!m_graphable.isVisited());
    m_graphable.setVisited(true);
    assertTrue(m_graphable.isVisited());  
  }
  
  /**
   * Test BasicGraphable#setObject(Object). <BR>
   * <BR>
   * Test: Create a new object and set underlying object.
   * Expected: Underlying object should be equal to created object.
   *
   */
  public void test_setObject() {
    Object obj = new Integer(1);
    
    assertTrue(m_graphable.getObject() == null);	
    m_graphable.setObject(obj);
    
    assertTrue(m_graphable.getObject() == obj);
  }
  
  /**
   * Test BasicGraphable#setCount(int). <BR>
   * <BR>
   * Test: Change value of counter.<BR>
   * Expected: Counter equal to new value.
   *
   */
  public void test_setCount() {
    assertEquals(m_graphable.getCount(), -1);
    m_graphable.setCount(10);
    
    assertEquals(m_graphable.getCount(), 10);	
  }
  
  /**
   * Test BasicGraphable#setID(int). <BR>
   * <BR>
   * Test: Change id.<BR>
   * Expected: Id equal to new value. 
   *
   */
  public void test_setID() {
    m_graphable.setID(10);
    assertEquals(m_graphable.getID(), 10);	
  }
}
