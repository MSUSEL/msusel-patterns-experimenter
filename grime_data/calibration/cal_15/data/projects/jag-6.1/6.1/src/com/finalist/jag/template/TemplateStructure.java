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
package com.finalist.jag.template;


import java.util.*;

import com.finalist.jag.util.*;


/**
 * Class TemplateStructure
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateStructure implements Cloneable {

   /** Field rootNode           */
   private TemplateTreeItem rootNode = null;


   /**
    * Constructor TemplateStructure
    *
    *
    */
   public TemplateStructure() {
   }


   /**
    * Constructor TemplateStructure
    *
    *
    * @param n
    *
    */
   public TemplateStructure(TemplateStructure n) {
      rootNode = new TemplateTreeItem(n.getRoot());
   }


   /**
    * Method setRoot
    *
    *
    * @param rootNode
    *
    */
   public void setRoot(TemplateTreeItem rootNode) {
      this.rootNode = rootNode;
   }


   /**
    * Method getRoot
    *
    *
    * @return
    *
    */
   public final TemplateTreeItem getRoot() {

      if (rootNode == null) {
         rootNode = new TemplateTreeItem();
      }

      return rootNode;
   }


   /**
    * Method getTextBlockList
    *
    *
    * @return
    *
    */
   public final TemplateTextBlockList getTextBlockList() {

      TemplateTextBlockList list = new TemplateTextBlockList();

      getTextBlockList(list, getRoot());

      return list;
   }


   /**
    * Method cut
    *
    * Requirement : both nodes must be in the same branche.
    *
    * @param first
    * @param last
    *
    * @return
    *
    */
   public TemplateStructure cut(TemplateTreeItem first,
      TemplateTreeItem last) {

      TemplateStructure returnValue = new TemplateStructure();
      TemplateTreeItem tmp = first;

      if (((first == null) || (last == null))
         || (first.getParent() != last.getParent())) {
         return returnValue;
      }

      // Disconnect first and last from the hive in order to
      // create their own collective.
      if (first.getPrevSibling() != null) {
         TreeItem prevItem = first.getPrevSibling();

         prevItem.setNextSibling(last.getNextSibling());
      }
      else if (first.getParent() != null) {
         TreeItem parentItem = first.getParent();

         parentItem.setFirstChild(last.getNextSibling());
      }

      last.disconnectSiblings();

      // return the new hive
      returnValue.getRoot().setFirstChild(first);

      return returnValue;
   }


   /**
    * Method getTextBlockList
    *
    *
    * @param list
    * @param item
    *
    */
   private void getTextBlockList(TemplateTextBlockList list,
      TemplateTreeItem item) {

      if (item.getTextBlock() != null) {
         list.add(item.getTextBlock());
      }

      TreeItem childItem = item.getFirstChild();

      while (childItem != null) {
         getTextBlockList(list, (TemplateTreeItem) childItem);

         childItem = childItem.getNextSibling();
      }
   }


   /**
    * Method getTemplateTreeItem
    *
    *
    * @param textBlock
    *
    * @return
    *
    */
   public TemplateTreeItem getTemplateTreeItem(TemplateTextBlock textBlock) {

      Iterator iterator = getCollection().iterator();

      while (iterator.hasNext()) {
         TemplateTreeItem item = (TemplateTreeItem) iterator.next();

         if (item.getTextBlock() == textBlock) {
            return item;
         }
      }

      return null;
   }


   /**
    * Method getCollection
    *
    *
    * @return
    *
    */
   public Collection getCollection() {

      LinkedList list = new LinkedList();

      getCollection(list, getRoot());

      return list;
   }


   /**
    * Method getCollection
    *
    *
    * @param list
    * @param item
    *
    */
   private void getCollection(LinkedList list, TemplateTreeItem item) {

      list.add(item);

      TreeItem childItem = item.getFirstChild();

      while (childItem != null) {
         getCollection(list, (TemplateTreeItem) childItem);

         childItem = childItem.getNextSibling();
      }
   }


   /**
    * Method clearBodyText
    *
    *
    *
    */
   public void clearBodyText() {
      clearBodyText(rootNode);
   }


   /**
    * Method clearBodyText
    *
    *
    *
    */
   private void clearBodyText(TemplateTreeItem parentItem) {

      if (parentItem == null) {
         return;
      }
      parentItem.getTextBlock().set();

      TemplateTreeItem templateItem =
         (TemplateTreeItem) parentItem.getFirstChild();

      while (templateItem != null) {
         clearBodyText(templateItem);
         templateItem = (TemplateTreeItem) templateItem.getNextSibling();
      }
   }
}
