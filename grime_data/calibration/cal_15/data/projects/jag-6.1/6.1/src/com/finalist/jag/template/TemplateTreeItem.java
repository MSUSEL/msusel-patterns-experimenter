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


/**
 * Class TemplateTreeItem
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateTreeItem extends TreeItem {

   /** Field tag           */
   public TemplateTag tag = null;

   /** Field textBlock           */
   public TemplateTextBlock textBlock = null;


   /**
    * Constructor TemplateTreeItem
    *
    *
    */
   public TemplateTreeItem() {
      textBlock = new TemplateTextBlock("");
   }


   /**
    * Constructor TemplateTreeItem
    *
    *
    * @param root
    *
    */
   public TemplateTreeItem(TemplateTreeItem root) {

      // Copy the textBlocks
      textBlock = new TemplateTextBlock(root.textBlock);

      // Copy the Tag and set the textbuffers.
      if (root.tag != null) {
         tag = new TemplateTag(root.tag);

         tag.setTextBuffer(textBlock);
         tag.setClosingTextBuffer(null);
      }

      TreeItem childItem = root.getFirstChild();

      while (childItem != null) {
         addChild(new TemplateTreeItem((TemplateTreeItem) childItem));

         childItem = childItem.getNextSibling();
      }

      if ((tag != null) && (getLastChild() != null)) {
         TemplateTreeItem closingBlock =
            (TemplateTreeItem) getLastChild();
         TemplateTextBlock closingText = closingBlock.getTextBlock();

         tag.setClosingTextBuffer(closingText);
      }
   }


   /**
    * Method setTag
    *
    *
    * @param tag
    *
    */
   public void setTag(TemplateTag tag) {
      this.tag = tag;
   }


   /**
    * Method setTextBlock
    *
    *
    * @param textBlock
    *
    */
   public void setTextBlock(TemplateTextBlock textBlock) {
      this.textBlock = textBlock;
   }


   /**
    * Method getTag
    *
    *
    * @return
    *
    */
   public TemplateTag getTag() {
      return (this.tag);
   }


   /**
    * Method getTextBlock
    *
    *
    * @return
    *
    */
   public TemplateTextBlock getTextBlock() {
      return (this.textBlock);
   }
}

;