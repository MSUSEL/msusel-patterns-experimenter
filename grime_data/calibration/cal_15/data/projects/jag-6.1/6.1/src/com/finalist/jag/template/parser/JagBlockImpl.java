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

import com.finalist.jag.util.*;

import java.io.*;


/**
 * Class JagBlockImpl
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class JagBlockImpl implements JagBlock, Serializable {

   /** Field down           */
   private JagBlockImpl down;

   /** Field right           */
   private JagBlockImpl right;

   /** Field text           */
   private String text = "";

   /** Field type           */
   private int type;

   /** Field startPos           */
   private int startPos;

   /** Field endPos           */
   private int endPos;

   /** Field verboseStringConversion           */
   private static boolean verboseStringConversion = false;

   /** Field tokenNames           */
   private static String[] tokenNames = null;


   /**
    * Add a node to the end of the child list for this node
    *
    * @param node
    */
   public void addChild(JagBlock node) {
      if (node == null) return;

      JagBlockImpl t = this.down;

      if (t != null) {
         while (t.right != null) {
            t = t.right;
         }

         t.right = (JagBlockImpl) node;
      }
      else {
         this.down = (JagBlockImpl) node;
      }
   }


   /**
    * Is node t equal to this in terms of token type and text?
    *
    * @param t
    *
    * @return
    */
   public boolean equals(JagBlock t) {
      if (t == null) return false;

      return this.getText().equals(t.getText())
         && (this.getType() == t.getType());
   }


   /**
    * Get the first child of this node; null if not children
    *
    * @return
    */
   public JagBlock getFirstChild() {
      return down;
   }


   /**
    * Get the next sibling in line after this one
    *
    * @return
    */
   public JagBlock getNextSibling() {
      return right;
   }


   /**
    * Get the token text for this node
    *
    * @return
    */
   public String getText() {
      return text;
   }


   /**
    * Get the token type for this node
    *
    * @return
    */
   public int getType() {
      return type;
   }


   /**
    * Set the startposition of the token
    *
    * @return
    */
   public int getStartPos() {
      return startPos;
   }


   /**
    * Set the endposition of the token
    *
    * @return
    */
   public int getEndPos() {
      return endPos;
   }


   /**
    * Method initialize
    *
    *
    * @param t
    * @param txt
    *
    */
   public void initialize(int t, String txt) {
      setType(t);
      setText(txt);
   }


   /**
    * Method initialize
    *
    *
    * @param t
    *
    */
   public void initialize(JagBlock t) {
      initialize(t.getType(), t.getText());
   }


   /** Remove all children */
   public void removeChildren() {
      down = null;
   }


   /**
    * Set the first child of a node.
    *
    * @param c
    */
   public void setFirstChild(JagBlock c) {
      down = (JagBlockImpl) c;
   }


   /**
    * Method setNextSibling
    *
    *
    * @param n
    *
    */
   public void setNextSibling(JagBlock n) {
      right = (JagBlockImpl) n;
   }


   /**
    * Set the token text for this node
    *
    * @param text
    */
   public void setText(String text) {
      this.text = text;
   }


   /**
    * Set the token type for this node
    *
    * @param type
    */
   public void setType(int type) {
      this.type = type;
   }


   /**
    * Set the startposition of the token
    *
    * @param startPos
    */
   public void setStartPos(int startPos) {
      this.startPos = startPos;
   }


   /**
    * Set the endposition of the token
    *
    * @param endPos
    */
   public void setEndPos(int endPos) {
      this.endPos = endPos;
   }


   /**
    * Method setVerboseStringConversion
    *
    *
    * @param verbose
    * @param names
    *
    */
   public static void setVerboseStringConversion(boolean verbose, String[] names) {
      verboseStringConversion = verbose;
      tokenNames = names;
   }


   /**
    * Method toString
    *
    *
    * @return
    *
    */
   public String toString() {
      StringBuffer b = new StringBuffer();

      // if verbose and type name not same as text(keyword probably)
      if (verboseStringConversion && !getText()
         .equalsIgnoreCase(tokenNames[getType()]) && !getText()
         .equalsIgnoreCase(Tools
         .stripFrontBack(tokenNames[getType()], "\"", "\""))) {
         b.append('[');
         b.append(getText());
         b.append(",<");
         b.append(tokenNames[getType()]);
         b.append(">]");
         return b.toString();
      }
      return getText();
   }
}

;
