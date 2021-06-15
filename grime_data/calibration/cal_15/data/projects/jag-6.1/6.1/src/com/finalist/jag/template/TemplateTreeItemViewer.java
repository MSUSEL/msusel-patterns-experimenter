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


import com.finalist.jag.util.*;

import java.io.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;


/**
 * Class TemplateTreeItemViewer
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateTreeItemViewer {

   /**
    * Method show
    *
    *
    * @param dataObj
    *
    */
   public static void show(TemplateTreeItem dataObj) {
      show(dataObj, "TemplateTreeItemViewer");
   }


   /**
    * Method show
    *
    *
    * @param dataObj
    * @param title
    *
    */
   public static void show(TemplateTreeItem dataObj, String title) {

      javax.swing.JFrame frame = new javax.swing.JFrame();

      frame.getContentPane().add(createSwingTree(dataObj),
         java.awt.BorderLayout.CENTER);
      frame.setSize(400, 400);
      frame.setTitle(title);
      frame.setVisible(true);
   }


   /**
    * Method createSwingTree
    *
    *
    * @param dataObj
    *
    * @return
    *
    */
   private static JScrollPane createSwingTree(TemplateTreeItem dataObj) {

      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

      createSwingTree(root, dataObj);

      JTree tree = new JTree(root) {

         public java.awt.Insets getInsets() {
            return new java.awt.Insets(5, 5, 5, 5);
         }
      };

      return new JScrollPane(tree);
   }


   /**
    * Method createSwingTree
    *
    *
    * @param parent
    * @param dataObj
    *
    */
   private static void createSwingTree(DefaultMutableTreeNode parent,
      TemplateTreeItem dataObj) {

      TemplateTreeItem childItem =
         (TemplateTreeItem) dataObj.getFirstChild();

      while (childItem != null) {
         String sText = (childItem.getTag() == null)
            ? "[TextBlock] "
            : "[TextBlock][TAG] ";

         sText += childItem.getTextBlock().getText();

         DefaultMutableTreeNode child = new DefaultMutableTreeNode(sText);

         parent.add(child);
         createSwingTree(child, childItem);

         childItem = (TemplateTreeItem) childItem.getNextSibling();
      }
   }
}
