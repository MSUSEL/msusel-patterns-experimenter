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
 
import java.io.Serializable;

import org.geotools.graph.structure.Graphable;

/**
 * Basic implementation of Graphable. This class serves as the root in the 
 * hierarchy of basic graph components. <BR>
 * <BR>
 * Components in the basic hierarchy implement the Serializable interface. 
 * However serialization will fail if a Graphable object contains a reference
 * to a non serializable object.
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public abstract class BasicGraphable implements Graphable, Serializable {
  
    /** Used to generate id's for graph components */
    private static int id = 0;

    /** underlying object of component **/
    private Object m_obj;
    
    /** Flag to indicate wether the component has been visited */
    private boolean m_visited;

    /** A counter to track how many times a component has been visited */
    private int m_nvisited;
    
    /** Id for component. */
    private int m_id;

    /**
     * Constrcuts a new a graph component. Sets the visited flag to false,
     * counter to -1, and generates a new id.
     */
    public BasicGraphable () {
       m_visited = false;
       m_nvisited = -1;
       m_id = id++;
    }
    
    /**
     * @see Graphable#getID()
     */
    public int getID() {
      return(m_id);
    }
    
    /**
     * @see Graphable#setID(int)
     */
    public void setID(int id) {
      m_id = id;  
    }
      
    /**
     * @see Graphable#getObject()
     */
    public Object getObject() {
      return(m_obj);
    }
    
    /**
     * @see Graphable#setObject(Object)
     */
    public void setObject(Object obj) {
      m_obj = obj;  
    }
    
    /**
     * @see Graphable#isVisited()
     */
    public boolean isVisited() {
      return (m_visited);
    }

    /**
     * @see Graphable#setVisited(boolean)
     */
    public void setVisited(boolean visited) {
      m_visited = visited;
    }

    /**
     * @see Graphable#getCount()
     */
    public int getCount() {
      return (m_nvisited);
    }

    /**
     * @see Graphable#setCount(int)
     */
    public void setCount(int count) {
      m_nvisited = count;
    }
    
    /**
     * Returns the id of the component as a string.
     * 
     * @see Graphable#getID()
     */
    public String toString() {
      return(String.valueOf(m_id));
	  }
}
