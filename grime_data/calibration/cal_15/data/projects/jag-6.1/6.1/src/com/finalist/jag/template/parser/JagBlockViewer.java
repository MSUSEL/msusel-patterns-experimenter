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
package com.finalist.jag.template.parser;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.accessibility.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

/**
 * Class JagBlockViewer
 *
 * Test class for viewing the JagBlockTree structure in a Swing component.
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class JagBlockViewer extends javax.swing.JFrame {
   /** Field rootBlock           */
   JagBlock rootBlock = null;

   /** Field buffer           */
   StringBuffer buffer;


   /**
    * Constructor JagBlockTree
    *
    *
    * @param rootBlock
    *
    */
   public JagBlockViewer(JagBlock rootBlock) {
      buffer = new StringBuffer();
      this.rootBlock = rootBlock;

      getContentPane().add(createTree(), BorderLayout.CENTER);
      setSize(400, 400);
      setVisible(true);
   }


   /**
    * Method getTextString
    *
    *
    * @return
    *
    */
   public final String getTextString() {
      return buffer.toString();
   }


   /**
    * Method createTree
    *
    *
    * @return
    *
    */
   public JScrollPane createTree() {
      DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
      createTree(root, rootBlock);

      JTree tree = new JTree(root) {
         public Insets getInsets() {
            return new Insets(5, 5, 5, 5);
         }
      };

      return new JScrollPane(tree);
   }


   /**
    * Method createTree
    *
    *
    * @param parent
    * @param block
    *
    */
   protected void createTree(DefaultMutableTreeNode parent, JagBlock block) {
      JagBlock blockChild = block.getFirstChild();

      while (blockChild != null) {
         DefaultMutableTreeNode child =
            new DefaultMutableTreeNode(blockChild.getText());

         if (blockChild.getType() == JagParserConstants.TAGSTART) {
            buffer.append("TAG");
         }

         if (blockChild.getType() == JagParserConstants.TEXT) {
            buffer.append(blockChild.getText());
         }

         createTree(child, blockChild);
         parent.add(child);

         blockChild = blockChild.getNextSibling();
      }
   }
}