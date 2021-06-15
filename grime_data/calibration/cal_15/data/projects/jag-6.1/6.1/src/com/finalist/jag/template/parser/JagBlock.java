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


import java.util.Collection;
import java.util.ArrayList;

import java.awt.Point;


/**
 * Interface JagBlock
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public interface JagBlock {

   /**
    * Add a (rightmost) child to this node
    *
    * @param c
    */
   public void addChild(JagBlock c);


   /**
    * Get the first child of this node; null if no children
    *
    * @return
    */
   public JagBlock getFirstChild();


   /**
    * Get the next sibling in line after this one
    *
    * @return
    */
   public JagBlock getNextSibling();


   /**
    * Get the token text for this node
    *
    * @return
    */
   public String getText();


   /**
    * Get the token type for this node
    *
    * @return
    */
   public int getType();


   /**
    * Set the startposition of the token
    *
    * @return
    */
   public int getStartPos();


   /**
    * Set the endposition of the token
    *
    * @return
    */
   public int getEndPos();


   /**
    * Method equals
    *
    *
    * @param t
    *
    * @return
    *
    */
   public boolean equals(JagBlock t);


   /**
    * Method initialize
    *
    *
    * @param t
    * @param txt
    *
    */
   public void initialize(int t, String txt);


   /**
    * Method initialize
    *
    *
    * @param t
    *
    */
   public void initialize(JagBlock t);


   /**
    * Set the first child of a node.
    *
    * @param c
    */
   public void setFirstChild(JagBlock c);


   /**
    * Set the next sibling after this one.
    *
    * @param n
    */
   public void setNextSibling(JagBlock n);


   /**
    * Set the token text for this node
    *
    * @param text
    */
   public void setText(String text);


   /**
    * Set the     token type for this     node
    *
    * @param ttype
    */
   public void setType(int ttype);


   /**
    * Set the startposition of the token
    *
    * @param startPos
    */
   public void setStartPos(int startPos);


   /**
    * Set the endposition of the token
    *
    * @param endPos
    */
   public void setEndPos(int endPos);


   /**
    * Method toString
    *
    *
    * @return
    *
    */
   public String toString();
}