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
package org.geotools.graph.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 *
 * @source $URL$
 */
public class Stack implements Collection, Queue {
  private static final int DEFAULT_SIZE = 10;
  
  /** underlying array **/
  private Object[] m_values;
  
  /** queue pointer **/
  private int m_sp;
  
  public Stack() {
    this(DEFAULT_SIZE);
  }
  
  public Stack(int size) {
    m_values = new Object[size];
    m_sp = 0;
  }
  
  //TODO: document that enq methods do not check bounds
  public void push(Object element) {
    m_values[m_sp++] = element;
  }
  
  public void pushAll(Collection elements) {
    for (Iterator itr = elements.iterator(); itr.hasNext();) {
      m_values[m_sp++] = itr.next();
    }
  }
  
  public Object pop() {
    return(m_values[--m_sp]);  
  }
  public int size() {
    return(m_sp);
  }

  public void clear() {
    m_sp = 0;  
  }

  public boolean isEmpty() {
    return(m_sp == 0);
  }

  public Object[] toArray() {
    return(m_values);  
  }

  public boolean add(Object o) {
    push(o);
    return(true);
  }

  public boolean contains(Object o) {
    for (int i = 0; i < m_sp; i++) {
      if (m_values[i].equals(o)) return(true);  
    }
    return(false);
  }

  public boolean remove(Object o) {
    throw new UnsupportedOperationException("remove(Object)");
  }

  public boolean addAll(Collection c) {
    pushAll(c);
    return(true);
  }

  public boolean containsAll(Collection c) {
    for (Iterator itr = c.iterator(); itr.hasNext();) {
      if (!contains(itr.next())) return(false);
    }
    return(true);
  }

  public boolean removeAll(Collection c) {
    throw new UnsupportedOperationException("removeAll(Collection)");
  }

  public boolean retainAll(Collection c) {
    throw new UnsupportedOperationException("retainAll(Collection)");
  }

  public Iterator iterator() {
    return(new StackIterator());
  
  }

  public Object[] toArray(Object[] a) {
    if (a.length < m_sp) {
      a = (Object[])java.lang.reflect.Array.newInstance(
        a.getClass().getComponentType(), m_sp
      );
    }
    
    for (int i = 0; i < m_sp; i++) {
      a[i] = m_values[i];  
    }
    
    if (a.length > 	m_sp) {
      a[m_sp] = null;  
    }
    
    return(a);
  }
  
  //queue implementation
  
  public void enq(Object object) {
    push(object);
  }

  public Object deq() {
    return(pop());
  }
  
  public class StackIterator implements Iterator {
    int m_index = 0;
    
    private StackIterator() {}
    
    public void remove() {
      throw new UnsupportedOperationException("remove()");  
    }

    public boolean hasNext() {
      return(m_index < m_sp);  
    }

    public Object next() {
      return(m_values[m_index++]);
    }
  }
}
