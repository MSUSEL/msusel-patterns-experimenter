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

import com.finalist.jag.template.parser.*;

/**
 * Class TemplateStructureFactory
 *
 *
 * @author Wendel D. de Witte
 * @version %I%, %G%
 */
public class TemplateStructureFactory implements JagParserConstants {

   private TemplateStructure templateTree = null;


   /**
    * Method create
    *
    *
    * @param jagblocks
    *
    */
   public void create(JagBlockCollection jagblocks) {

      templateTree = new TemplateStructure();
      createTree(templateTree.getRoot(), jagblocks);
   }


   public TemplateStructure getTemplateStructure() {
      return templateTree;
   }


   /**
    * Method createParamList
    *
    *
    * @param block
    *
    * @return
    *
    */
   protected JagParameter[] createParamList(JagBlock block) {

      Collection paramList = new ArrayList();
      JagBlock childBlock[] = {block.getFirstChild(), null};

      while (childBlock[0] != null) {
         if (childBlock[0].getType() == PARAMDEF) {
            String ident = null, value = null;

            childBlock[1] = childBlock[0].getFirstChild();
            while (childBlock[1] != null) {
               if (childBlock[1].getType() == IDENT) {
                  ident = childBlock[1].getText();
               }
               else if (childBlock[1].getType() == STRING) {
                  value = childBlock[1].getText();
               }
               childBlock[1] = childBlock[1].getNextSibling();
            }

            if ((ident != null) && (value != null)) {
               paramList.add(new JagParameter(ident, value));
            }
         }
         childBlock[0] = childBlock[0].getNextSibling();
      }

      return (JagParameter[])
         paramList.toArray(new JagParameter[paramList.size()]);
   }


   //////////////////////////////////////////////////////////////////////////////////
   protected void createTree(TemplateTreeItem parentItem, JagBlock block) {
      JagBlock childBlock = block.getFirstChild();

      for (; childBlock != null; childBlock = childBlock.getNextSibling()) {
         if (childBlock.getType() == HEADERDEF_BEGIN) {
            // skip end of line symbol
            trimFrontEOLSymbol(childBlock.getNextSibling());
            continue;
         }

         TemplateTextBlock textBlock = new TemplateTextBlock(childBlock.getText());
         TemplateTreeItem newItem = new TemplateTreeItem();
         newItem.setTextBlock(textBlock);
         parentItem.addChild(newItem);

         if (childBlock.getType() == TEXT) {
            continue;
         }
         else if (childBlock.getType() == TAGSTART) {
            TemplateTag tag = createTag(newItem, childBlock);
            if (tag != null)
               newItem.setTag(tag);
         }
      }
   }


   /**
    * Method trimFrontEOLSymbol
    *
    *
    * @param block
    *
    * @return
    *
    */
   protected void trimFrontEOLSymbol(JagBlock block) {
      if (block == null) return;

      String text = block.getText();
      if (text.length() > 0 && text.charAt(0) == '\r')
         text = text.substring(1);
      if (text.length() > 0 && text.charAt(0) == '\n')
         text = text.substring(1);
      block.setText(text);
   }


   /**
    * Method createTag
    *
    *
    * @param block
    * @param textBlock
    *
    * @return
    *
    */
   protected TemplateTag createTag(TemplateTreeItem parentItem,
      JagBlock block) {

      TemplateTextBlock textBlock = parentItem.getTextBlock();
      JagBlock blockChild = block.getFirstChild();
      String tagName = null;
      String tagAction = null;
      JagBlock blockSLIST = null;
      JagBlock blockTagEnd = null;
      TemplateTag tag = null;

      while (blockChild != null) {
         if (blockChild.getType() == TAGNAME) {
            tagName = blockChild.getText();
         }
         else if (blockChild.getType() == TAGACTION) {
            tagAction = blockChild.getText();
         }
         else if (blockChild.getType() == SLIST) {
            blockSLIST = blockChild;
         }
         else if (blockChild.getType() == TAGEND) {
            blockTagEnd = blockChild;
         }

         blockChild = blockChild.getNextSibling();
      }

      if ((tagName != null) && (tagAction != null)) {
         JagParameter[] paramArray = createParamList(block);

         tag = new TemplateTag(tagName, tagAction, paramArray, textBlock);
      }

      if (blockSLIST != null) {
         createTree(parentItem, blockSLIST);
      }

      if (blockTagEnd != null) {
         TemplateTextBlock endBuffer = new TemplateTextBlock(blockTagEnd.getText());
         TemplateTreeItem newItem = new TemplateTreeItem();
         newItem.setTextBlock(endBuffer);
         parentItem.addChild(newItem);
         tag.setClosingTextBuffer(endBuffer);
      }

      return tag;
   }
}