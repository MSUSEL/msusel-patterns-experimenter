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
 * MyTreeModelListener.java
 *
 * Created on 16 september 2002, 22:03
 */

package com.finalist.jaggenerator;

import com.finalist.jaggenerator.modules.*;

import javax.swing.event.*;
import javax.swing.tree.*;

/**
 *
 * @author  hillie
 */
public class JagTreeModelListener implements TreeModelListener {

   /** Creates a new instance of MyTreeModelListener */
   public JagTreeModelListener() {
   }


   /** <p>Invoked after a node (or a set of siblings) has changed in some
    * way. The node(s) have not changed locations in the tree or
    * altered their children arrays, but other attributes have
    * changed and may affect presentation. Example: the name of a
    * file has changed, but it is in the same location in the file
    * system.</p>
    * <p>To indicate the root has changed, childIndices and children
    * will be null. </p>
    *
    * <p>Use <code>e.getPath()</code>
    * to get the parent of the changed node(s).
    * <code>e.getChildIndices()</code>
    * returns the index(es) of the changed node(s).</p>
    *
    */
   public void treeNodesChanged(TreeModelEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());
      /*
       * If the event lists children, then the changed
       * node is the child of the node we've already
       * gotten.  Otherwise, the changed node and the
       * specified node are the same.
       */
      try {
         int index = e.getChildIndices()[0];
         node = (DefaultMutableTreeNode) (node.getChildAt(index));
      }
      catch (NullPointerException exc) {
      }
   }


   /** <p>Invoked after nodes have been inserted into the tree.</p>
    *
    * <p>Use <code>e.getPath()</code>
    * to get the parent of the new node(s).
    * <code>e.getChildIndices()</code>
    * returns the index(es) of the new node(s)
    * in ascending order.</p>
    *
    */
   public void treeNodesInserted(TreeModelEvent e) {
   }


   /** <p>Invoked after nodes have been removed from the tree.  Note that
    * if a subtree is removed from the tree, this method may only be
    * invoked once for the root of the removed subtree, not once for
    * each individual set of siblings removed.</p>
    *
    * <p>Use <code>e.getPath()</code>
    * to get the former parent of the deleted node(s).
    * <code>e.getChildIndices()</code>
    * returns, in ascending order, the index(es)
    * the node(s) had before being deleted.</p>
    *
    */
   public void treeNodesRemoved(TreeModelEvent e) {
   }


   /** <p>Invoked after the tree has drastically changed structure from a
    * given node down.  If the path returned by e.getPath() is of length
    * one and the first element does not identify the current root node
    * the first element should become the new root of the tree.<p>
    *
    * <p>Use <code>e.getPath()</code>
    * to get the path to the node.
    * <code>e.getChildIndices()</code>
    * returns null.</p>
    *
    */
   public void treeStructureChanged(TreeModelEvent e) {
   }

}
