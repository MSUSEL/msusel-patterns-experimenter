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
package com.finalist.jag.util;


/**
 * Class TreeItem
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 *
 *
 *
 */
public class TreeItem {

   /** Field down */
   private TreeItem up = null;

   /** Field down */
   private TreeItem down = null;

   /** Field right */
   private TreeItem right = null;

   /** Field right */
   private TreeItem left = null;

   /** Field dataField           */
   private Object dataField = null;


   /**
    * Add a node to the end of the child list for this node
    *
    * @param node
    */
   public void addChild(TreeItem node) {

      if (node == null) {
         return;
      }

      TreeItem t = this.down;

      if (t != null) {
         // only the most left must point to the parent
         while (t.right != null) t = t.right;
         t.right = (TreeItem) node;
         node.left = t;
         node.up = null;
      }
      else {
         // don't reset the right pointer;
         this.down = (TreeItem) node;
         node.left = null;
         node.up = this;
      }
   }


   /**
    *  Get the first child of this node; null if not children
    *
    *  @return
    */
   public TreeItem getFirstChild() {
      return down;
   }


   /**
    * Get the next sibling in line after this one
    *
    * @return
    */
   public TreeItem getNextSibling() {
      return right;
   }


   /**
    * Get the next sibling in line after this one
    *
    * @return
    */
   public TreeItem getPrevSibling() {
      return left;
   }


   public TreeItem getLastChild() {
      TreeItem childItem = getFirstChild();
      while (childItem != null && childItem.getNextSibling() != null)
         childItem = childItem.getNextSibling();
      return childItem;
   }


   public TreeItem getParent() {
      TreeItem tmp = this;
      while (tmp.getPrevSibling() != null)
         tmp = tmp.getPrevSibling();
      return tmp.up;
   }


   public TreeItem getRoot() {
      TreeItem tmp = this;
      while (tmp.getParent() != null)
         tmp = tmp.getParent();
      return tmp;
   }


   /** Remove all children */
   public TreeItem disconnectChildren() {
      TreeItem treeitem = down;
      if (down != null) {
         treeitem.up = null;
         down = null;
      }
      return treeitem;
   }


   public TreeItem disconnectLastChild() {
      TreeItem child = this.getFirstChild();
      while (child != null) {
         if (child.getNextSibling() == null) {
            if (child.left == null)
               return disconnectChildren();

            return child.left.disconnectSiblings();
         }
         child = child.getNextSibling();
      }
      return null;
   }


   /** Remove all children */
   public TreeItem disconnectSiblings() {
      TreeItem treeitem = right;
      if (right != null) {
         right.left = null;
         right = null;
      }
      return treeitem;
   }


   /**
    * Method setFirstChild
    *
    *
    * @param c
    *
    */
   public TreeItem setFirstChild(TreeItem c) {
      TreeItem oldChild = disconnectChildren();
      down = (TreeItem) c;
      return oldChild;
   }


   /**
    * Method setNextSibling
    *
    * @param n
    */
   public TreeItem setNextSibling(TreeItem n) {
      TreeItem oldSibling = disconnectSiblings();
      right = n;
      n.left = this;
      return oldSibling;
   }


   /**
    * Method getDataItem
    *
    *
    * @return
    *
    */
   public Object getDataItem() {
      return dataField;
   }


   /**
    * Method setDataItem
    *
    *
    * @param dataField
    *
    */
   public void setDataItem(Object dataField) {
      this.dataField = dataField;
   }


   public String toString() {
      String buffer = new String();
      buffer += "up : ";
      buffer += (up != null) ? "1" : "null";
      buffer += " down : ";
      buffer += (down != null) ? "1" : "null";
      buffer += " right : ";
      buffer += (right != null) ? "1" : "null";
      buffer += " left : ";
      buffer += (left != null) ? "1" : "null";
      return buffer;
   }
}

;