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
package org.apache.hadoop.hdfs.server.namenode;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Host2NodesMap {
  private HashMap<String, DatanodeDescriptor[]> map
    = new HashMap<String, DatanodeDescriptor[]>();
  private Random r = new Random();
  private ReadWriteLock hostmapLock = new ReentrantReadWriteLock();
                      
  /** Check if node is already in the map. */
  boolean contains(DatanodeDescriptor node) {
    if (node==null) {
      return false;
    }
      
    String host = node.getHost();
    hostmapLock.readLock().lock();
    try {
      DatanodeDescriptor[] nodes = map.get(host);
      if (nodes != null) {
        for(DatanodeDescriptor containedNode:nodes) {
          if (node==containedNode) {
            return true;
          }
        }
      }
    } finally {
      hostmapLock.readLock().unlock();
    }
    return false;
  }
    
  /** add node to the map 
   * return true if the node is added; false otherwise.
   */
  boolean add(DatanodeDescriptor node) {
    hostmapLock.writeLock().lock();
    try {
      if (node==null || contains(node)) {
        return false;
      }
      
      String host = node.getHost();
      DatanodeDescriptor[] nodes = map.get(host);
      DatanodeDescriptor[] newNodes;
      if (nodes==null) {
        newNodes = new DatanodeDescriptor[1];
        newNodes[0]=node;
      } else { // rare case: more than one datanode on the host
        newNodes = new DatanodeDescriptor[nodes.length+1];
        System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
        newNodes[nodes.length] = node;
      }
      map.put(host, newNodes);
      return true;
    } finally {
      hostmapLock.writeLock().unlock();
    }
  }
    
  /** remove node from the map 
   * return true if the node is removed; false otherwise.
   */
  boolean remove(DatanodeDescriptor node) {
    if (node==null) {
      return false;
    }
      
    String host = node.getHost();
    hostmapLock.writeLock().lock();
    try {

      DatanodeDescriptor[] nodes = map.get(host);
      if (nodes==null) {
        return false;
      }
      if (nodes.length==1) {
        if (nodes[0]==node) {
          map.remove(host);
          return true;
        } else {
          return false;
        }
      }
      //rare case
      int i=0;
      for(; i<nodes.length; i++) {
        if (nodes[i]==node) {
          break;
        }
      }
      if (i==nodes.length) {
        return false;
      } else {
        DatanodeDescriptor[] newNodes;
        newNodes = new DatanodeDescriptor[nodes.length-1];
        System.arraycopy(nodes, 0, newNodes, 0, i);
        System.arraycopy(nodes, i+1, newNodes, i, nodes.length-i-1);
        map.put(host, newNodes);
        return true;
      }
    } finally {
      hostmapLock.writeLock().unlock();
    }
  }
    
  /** get a data node by its host.
   * @return DatanodeDescriptor if found; otherwise null.
   */
  DatanodeDescriptor getDatanodeByHost(String host) {
    if (host==null) {
      return null;
    }
      
    hostmapLock.readLock().lock();
    try {
      DatanodeDescriptor[] nodes = map.get(host);
      // no entry
      if (nodes== null) {
        return null;
      }
      // one node
      if (nodes.length == 1) {
        return nodes[0];
      }
      // more than one node
      return nodes[r.nextInt(nodes.length)];
    } finally {
      hostmapLock.readLock().unlock();
    }
  }
    
  /**
   * Find data node by its name.
   * 
   * @return DatanodeDescriptor if found or null otherwise 
   */
  public DatanodeDescriptor getDatanodeByName(String name) {
    if (name==null) {
      return null;
    }
      
    int colon = name.indexOf(":");
    String host;
    if (colon < 0) {
      host = name;
    } else {
      host = name.substring(0, colon);
    }

    hostmapLock.readLock().lock();
    try {
      DatanodeDescriptor[] nodes = map.get(host);
      // no entry
      if (nodes== null) {
        return null;
      }
      for(DatanodeDescriptor containedNode:nodes) {
        if (name.equals(containedNode.getName())) {
          return containedNode;
        }
      }
      return null;
    } finally {
      hostmapLock.readLock().unlock();
    }
  }
  
  @Override
  public String toString() {
    final StringBuilder b = new StringBuilder(getClass().getSimpleName())
        .append("[");
    for(Map.Entry<String, DatanodeDescriptor[]> e : map.entrySet()) {
      b.append("\n  " + e.getKey() + " => " + Arrays.asList(e.getValue()));
    }
    return b.append("\n]").toString();
  }
}
