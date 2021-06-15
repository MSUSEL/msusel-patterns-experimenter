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
package org.geotools.graph.traverse.standard;

import java.util.Iterator;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.SourceGraphIterator;

/**
 * Iterates over the nodes of a graph starting from a specified node, stopping 
 * at a bifurcation. A <B>bifurcation</B> is defined as a node of degree > 2. 
 * The following figures illustrate examples of the iterator.<BR>
 * <BR>
 * <IMG src="doc-files/nobif_0.gif"/><BR>
 * <BR>
 * <BR>
 * <BR>
 * <IMG src="doc-files/nobif_1.gif"/><BR>
 * <BR>
 * 
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public class NoBifurcationIterator extends SourceGraphIterator {
  
  /** the next node to be returned by the iterator **/
  private Node m_next;
  
  /**
   * Does nothing. 
   * 
   * @see GraphIterator#init(Graph)
   */
  public void init(Graph graph, GraphTraversal traversal) {
    //do nothing
  }

  /**
   * Sets the source of the traversal. This must be a node of degree less than
   * or equal to 2. 
   * 
   * @param source node of degree less than or equal 2
   * 
   * @see SourceGraphIterator#setSource(Graphable)
   * @throws IllegalStateException
   */
  public void setSource(Graphable source) {
    //check that source node if of degree <= 2
    if (((Node)source).getDegree() > 2) {
      throw new IllegalStateException(
        "Cannot start a no bifurcation traversal  on a node that " +
        "bifurcates."
      );   
    }
    super.setSource(source);
    m_next = (Node)getSource();
  }

  /**
   * The next node in the iteration is the first node found adjacent to the 
   * current node that is non visited and of degree less than 2.
   * 
   * @see org.geotools.graph.traverse.GraphIterator#next()
   */
  public Graphable next(GraphTraversal traversal) {
    return(m_next);  
  }
  
  /**
   * Searches for the next node to be returned in the iteration. The next node 
   * is the first node (of two) related to the current node that is non visited
   * and of degree <= 2.
   * 
   * @see org.geotools.graph.traverse.GraphIterator#cont(Graphable)
   */
  public void cont(Graphable current, GraphTraversal traversal) {
    //find a related node that is non visited and degree <= 2
    Iterator itr = current.getRelated();
    m_next = (Node)itr.next();
    if (itr.hasNext() && (traversal.isVisited(m_next) || m_next.getDegree() > 2)) 
      m_next = (Node)itr.next();
    if (traversal.isVisited(m_next) || m_next.getDegree() > 2) m_next = null;
  }

  /**
   * Kills the current branch of the iteration by explicitly setting the next
   * node to be returned to null. This call always ends the traversal.
   * 
   * @see org.geotools.graph.traverse.GraphIterator#killBranch(Graphable)
   */
  public void killBranch(Graphable current, GraphTraversal traversal) {
    m_next = null;
  }
}
