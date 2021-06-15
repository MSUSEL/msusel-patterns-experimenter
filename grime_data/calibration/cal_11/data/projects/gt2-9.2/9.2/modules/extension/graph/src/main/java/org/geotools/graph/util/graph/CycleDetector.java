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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.util.graph;

import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.traverse.GraphIterator;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.standard.BreadthFirstTopologicalIterator;

/**
 * Detects cycles in a graph. A topological iteration 
 * of the nodes of the graph is performed. If the iteration includes all nodes 
 * in the graph then the graph is cycle free, otherwise a cycle exists. 
 * 
 * @see org.geotools.graph.traverse.standard.BreadthFirstTopologicalIterator
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public class CycleDetector implements GraphWalker {
  
  /** the graph to be tested for cycle exisitance **/
  private Graph m_graph;
  
  /** counter to keep track of the number of nodes visited in the iteration **/ 
  private int m_nvisited;
  
  /** iteration to perform on nodes of graph **/
  private GraphIterator m_iterator;
  
  /** 
   * Constructs a new CycleDetector.
   * 
   * @param graph The graph to be tested for cycle existance. 
   */
  public CycleDetector(Graph graph) {
    m_graph = graph;
    m_nvisited = 0;
    m_iterator = createIterator();
  }
  
  /**
   * Performs the iteration to determine if a cycle exits in the graph.
   *
   * @return True if a cycle exists, false if not.
   */
  public boolean containsCycle() {
    //initialize visited counter
    m_nvisited = 0; 
    
    //create the traversal that uses the topological iterator
    GraphTraversal traversal = new BasicGraphTraversal(
      m_graph, this, m_iterator
    );
    traversal.init();
    traversal.traverse();
        
    //if all nodes visited then no cycle
    if (m_graph.getNodes().size() == m_nvisited) return(false);
    return(true);
  }
  
  /**
   * Increments the count of nodes visited.
   * 
   * @see GraphWalker#visit(Graphable, GraphTraversal)
   */
  public int visit(Graphable element, GraphTraversal traversal) {
    m_nvisited++;
    return(GraphTraversal.CONTINUE);
  }
  
  /**
   * Does nothing.
   * 
   * @see GraphWalker#finish()
   */
  public void finish() {}
  
  /**
   * Creates the iterator to be used in the cycle detection.
   * 
   * @return a BreathFirstToplogicalIterator.
   */
  protected GraphIterator createIterator() {
    return(new BreadthFirstTopologicalIterator());
  }

}
