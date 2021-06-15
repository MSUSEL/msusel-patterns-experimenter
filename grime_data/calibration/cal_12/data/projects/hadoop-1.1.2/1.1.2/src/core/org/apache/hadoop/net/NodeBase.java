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
package org.apache.hadoop.net;

/** A base class that implements interface Node
 * 
 */

public class NodeBase implements Node {
  public final static char PATH_SEPARATOR = '/';
  public final static String PATH_SEPARATOR_STR = "/";
  public final static String ROOT = ""; // string representation of root
  
  protected String name; //host:port#
  protected String location; //string representation of this node's location
  protected int level; //which level of the tree the node resides
  protected Node parent; //its parent
  
  /** Default constructor */
  public NodeBase() {
  }
  
  /** Construct a node from its path
   * @param path 
   *   a concatenation of this node's location, the path seperator, and its name 
   */
  public NodeBase(String path) {
    path = normalize(path);
    int index = path.lastIndexOf(PATH_SEPARATOR);
    if (index== -1) {
      set(ROOT, path);
    } else {
      set(path.substring(index+1), path.substring(0, index));
    }
  }
  
  /** Construct a node from its name and its location
   * @param name this node's name 
   * @param location this node's location 
   */
  public NodeBase(String name, String location) {
    set(name, normalize(location));
  }
  
  /** Construct a node from its name and its location
   * @param name this node's name 
   * @param location this node's location 
   * @param parent this node's parent node
   * @param level this node's level in the tree
   */
  public NodeBase(String name, String location, Node parent, int level) {
    set(name, normalize(location));
    this.parent = parent;
    this.level = level;
  }

  /* set this node's name and location */
  private void set(String name, String location) {
    if (name != null && name.contains(PATH_SEPARATOR_STR))
      throw new IllegalArgumentException(
                                         "Network location name contains /: "+name);
    this.name = (name==null)?"":name;
    this.location = location;      
  }
  
  /** Return this node's name */
  public String getName() { return name; }
  
  /** Return this node's network location */
  public String getNetworkLocation() { return location; }
  
  /** Set this node's network location */
  public void setNetworkLocation(String location) { this.location = location; }
  
  /** Return this node's path */
  public static String getPath(Node node) {
    return node.getNetworkLocation()+PATH_SEPARATOR_STR+node.getName();
  }
  
  /** Return this node's string representation */
  public String toString() {
    return getPath(this);
  }

  /** Normalize a path */
  static public String normalize(String path) {
    if (path == null || path.length() == 0) return ROOT;
    
    if (path.charAt(0) != PATH_SEPARATOR) {
      throw new IllegalArgumentException(
                                         "Network Location path does not start with "
                                         +PATH_SEPARATOR_STR+ ": "+path);
    }
    
    int len = path.length();
    if (path.charAt(len-1) == PATH_SEPARATOR) {
      return path.substring(0, len-1);
    }
    return path;
  }
  
  /** Return this node's parent */
  public Node getParent() { return parent; }
  
  /** Set this node's parent */
  public void setParent(Node parent) {
    this.parent = parent;
  }
  
  /** Return this node's level in the tree.
   * E.g. the root of a tree returns 0 and its children return 1
   */
  public int getLevel() { return level; }
  
  /** Set this node's level in the tree */
  public void setLevel(int level) {
    this.level = level;
  }
}
