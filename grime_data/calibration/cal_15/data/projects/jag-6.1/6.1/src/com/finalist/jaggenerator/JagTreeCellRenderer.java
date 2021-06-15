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
package com.finalist.jaggenerator;

import com.finalist.jaggenerator.modules.*;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.*;
import java.awt.*;

/**
 * A custom TreeCellRenderer that uses a different icons for the various JAG objects.
 *
 * @author Michael O'Connor - Finalist IT Group
 */
public class JagTreeCellRenderer extends DefaultTreeCellRenderer {
   ImageIcon relationIcon;
   ImageIcon entityIcon;
   ImageIcon associationEntityIcon;
   ImageIcon sessionIcon;
   ImageIcon configIcon;
   ImageIcon rootIcon;
   ImageIcon businessMethodIcon;
   ImageIcon fieldIcon;
   ImageIcon pkFieldIcon;

   public JagTreeCellRenderer() {
      relationIcon = new ImageIcon("../images/relation.png");
      entityIcon = new ImageIcon("../images/entity.png");
      associationEntityIcon = new ImageIcon("../images/associationEntity.png");
      sessionIcon = new ImageIcon("../images/session.png");
      configIcon = new ImageIcon("../images/config.png");
      rootIcon = new ImageIcon("../images/root.png");
      businessMethodIcon = new ImageIcon("../images/business.png");
      fieldIcon = new ImageIcon("../images/field.png");
      pkFieldIcon = new ImageIcon("../images/pkfield.png");
   }

   public Component getTreeCellRendererComponent(JTree tree,
                                                 Object value,
                                                 boolean sel,
                                                 boolean expanded,
                                                 boolean leaf,
                                                 int row,
                                                 boolean hasFocus) {
      super.getTreeCellRendererComponent(tree, value, sel,
                                         expanded, leaf, row,
                                         hasFocus);
      super.setIcon(rootIcon);

      if (leaf && value instanceof Relation) {
         setIcon(relationIcon);
      }
       if (leaf && value instanceof BusinessMethod) {
          setIcon(businessMethodIcon);
       }
      else if (value instanceof Entity) {
         if ("true".equals(((Entity) value).getIsAssociationEntity())) {
            setIcon(associationEntityIcon);
         } else {
            setIcon(entityIcon);
         }
      }
      else if (value instanceof Session) {
         setIcon(sessionIcon);
      }
      else if (leaf && value instanceof Config) {
         setIcon(configIcon);
      }
      else if (leaf && value instanceof App) {
         setIcon(configIcon);
      }
      else if (leaf && value instanceof Datasource) {
         setIcon(configIcon);
      }
      else if (leaf && value instanceof Paths) {
         setIcon(configIcon);
      }
      else if (leaf && value instanceof Field) {
         if ( ((Field) value).isPrimaryKey() ) {
            setIcon(pkFieldIcon);
         } else {
            setIcon(fieldIcon);
         }
      }
      return this;
   }

}
