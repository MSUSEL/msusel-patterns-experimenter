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
package org.apache.hadoop.tools.rumen;

import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * {@link Node} represents a node in the cluster topology. A node can be a
 * {@link MachineNode}, or a {@link RackNode}, etc.
 */
public class Node implements Comparable<Node> {
  private static final SortedSet<Node> EMPTY_SET = 
    Collections.unmodifiableSortedSet(new TreeSet<Node>());
  private Node parent;
  private final String name;
  private final int level;
  private SortedSet<Node> children;

  /**
   * @param name
   *          A unique name to identify a node in the cluster.
   * @param level
   *          The level of the node in the cluster
   */
  public Node(String name, int level) {
    if (name == null) {
      throw new IllegalArgumentException("Node name cannot be null");
    }

    if (level < 0) {
      throw new IllegalArgumentException("Level cannot be negative");
    }

    this.name = name;
    this.level = level;
  }

  /**
   * Get the name of the node.
   * 
   * @return The name of the node.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the level of the node.
   * @return The level of the node.
   */
  public int getLevel() {
    return level;
  }
  
  private void checkChildren() {
    if (children == null) {
      children = new TreeSet<Node>();
    }
  }

  /**
   * Add a child node to this node.
   * @param child The child node to be added. The child node should currently not be belong to another cluster topology.
   * @return Boolean indicating whether the node is successfully added.
   */
  public synchronized boolean addChild(Node child) {
    if (child.parent != null) {
      throw new IllegalArgumentException(
          "The child is already under another node:" + child.parent);
    }
    checkChildren();
    boolean retval = children.add(child);
    if (retval) child.parent = this;
    return retval;
  }

  /**
   * Does this node have any children?
   * @return Boolean indicate whether this node has any children.
   */
  public synchronized boolean hasChildren() {
    return children != null && !children.isEmpty();
  }

  /**
   * Get the children of this node.
   * 
   * @return The children of this node. If no child, an empty set will be
   *         returned. The returned set is read-only.
   */
  public synchronized Set<Node> getChildren() {
    return (children == null) ? EMPTY_SET : 
      Collections.unmodifiableSortedSet(children);
  }
  
  /**
   * Get the parent node.
   * @return the parent node. If root node, return null.
   */
  public Node getParent() {
    return parent;
  }
  
  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (obj.getClass() != this.getClass())
      return false;
    Node other = (Node) obj;
    return name.equals(other.name);
  }
  
  @Override
  public String toString() {
    return "(" + name +", " + level +")";
  }

  @Override
  public int compareTo(Node o) {
    return name.compareTo(o.name);
  }
}
