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
package org.geotools.graph.structure.opt;

import java.util.ArrayList;
import java.util.Iterator;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Node;

/**
 * Optimized implementation of Edge. 
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 * @see Edge
 *
 *
 * @source $URL$
 */
public class OptEdge extends OptGraphable implements Edge {

  /** a node **/
  private OptNode m_nodeA;
  
  /** b node **/
  private OptNode m_nodeB;
  
  /**
   * Constructs a new optimized edge.
   * 
   * @param nodeA A node of edge.
   * @param nodeB B node of edge.
   */
  public OptEdge(OptNode nodeA, OptNode nodeB) {
    m_nodeA = nodeA;
    m_nodeB = nodeB;
  }
  
  /**
   * @see Edge#getNodeA()
   */
  public Node getNodeA() {
    return(m_nodeA);
  }

  /**
   * @see Edge#getNodeB()
   */
  public Node getNodeB() {
    return(m_nodeB);
  }

  /**
   * @see Edge#getOtherNode(Node)
   */
  public Node getOtherNode(Node node) {
    return(
      m_nodeA.equals(node) ? m_nodeB : m_nodeB.equals(node) ? m_nodeA : null
    );
  }

  /**
   * @see Edge#reverse()
   */
  public void reverse() {
    OptNode tmp = m_nodeA;
    m_nodeA = m_nodeB;
    m_nodeB = tmp;
  }

  /**
   * @see Edge#compareNodes(Edge)
   */
  public int compareNodes(Edge other) {
    if (m_nodeA.equals(other.getNodeA()) && m_nodeB.equals(other.getNodeB()))
      return(Edge.EQUAL_NODE_ORIENTATION);
      
    if (m_nodeB.equals(other.getNodeA()) && m_nodeA.equals(other.getNodeB()))
      return(Edge.OPPOSITE_NODE_ORIENTATION);
      
    return(Edge.UNEQUAL_NODE_ORIENTATION);
  }

  /**
   * @see org.geotools.graph.structure.Graphable#getRelated()
   */
  public Iterator getRelated() {
   return(new RelatedIterator(this));  
  }
  
  public class RelatedIterator implements Iterator {

    private Iterator m_itr;
    
    public RelatedIterator(OptEdge edge) {
      ArrayList edges = new ArrayList(
        m_nodeA.getDegree() + m_nodeB.getDegree() - 2 - 
        m_nodeA.getEdges(m_nodeB).size()
      );  
      
      //add all edges of node A except this edge
      for (int i = 0; i < m_nodeA.getEdgeArray().length; i++) {
        Edge e = (Edge)m_nodeA.getEdgeArray()[i];
        if (!e.equals(edge)) edges.add(m_nodeA.getEdgeArray()[i]);  
      }
      
      //add only edges from node b that are node shared with node a
      for (int i = 0; i < m_nodeB.getEdgeArray().length; i++) {
        Edge e = (Edge)m_nodeB.getEdgeArray()[i];
        if (!e.getOtherNode(m_nodeB).equals(m_nodeA)) edges.add(e);
      }
      
      m_itr = edges.iterator(); 
    }
    
    public void remove() {
      throw new UnsupportedOperationException(
        getClass().getName() + "#remove() not supported."
      ); 
    }

    public boolean hasNext() {
      return(m_itr.hasNext()); 
    }

    public Object next() {
      return(m_itr.next());
    }
    
  }
}
