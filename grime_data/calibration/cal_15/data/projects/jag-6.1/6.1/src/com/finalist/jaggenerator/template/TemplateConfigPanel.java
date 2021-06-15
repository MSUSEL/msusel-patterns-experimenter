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
package com.finalist.jaggenerator.template;

import org.netbeans.lib.awtextra.AbsoluteConstraints;

import javax.swing.*;

import com.finalist.jaggenerator.JagGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a JPanel containing configuration settings derived from a particular
 * JAG application generation template.
 * 
 * @author Michael O'Connor - Finalist IT Group
 */
public class TemplateConfigPanel extends JPanel {

   private HashMap configComponents = new HashMap();


   public TemplateConfigPanel(TemplateConfigParameter[] params, String title) {
      super();
      setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      if (title != null) {
         JLabel titleLabel = new JLabel();
         titleLabel.setText(title);
         add(titleLabel, new AbsoluteConstraints(0, 0, 350, -1));
         titleLabel.setBorder(new javax.swing.border.TitledBorder("Selected template:"));
      }

      for (int i = 0; i < params.length; i++) {
         int y = (i * 25) + 45;
         JLabel jLabel1 = new JLabel();
         jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
         jLabel1.setText(params[i].getName() + ':');
         String description = params[i].getDescription();
         if (description != null) {
            jLabel1.setToolTipText(description);
         }
         add(jLabel1, new AbsoluteConstraints(0, y, 150, -1));

         JComponent component = null;
         if (params[i].getType() == TemplateConfigParameter.TYPE_TEXT) {
            component = new JTextField();
            component.setName(params[i].getId());
         }
         else if (params[i].getType() == TemplateConfigParameter.TYPE_CHECKBOX) {
            component = new JCheckBox();
            component.setName(params[i].getId());

         }
         else if (params[i].getType() == TemplateConfigParameter.TYPE_LIST) {
            component = new JComboBox(params[i].getPresetValues());
            component.setName(params[i].getId());

         }
         else if (params[i].getType() == TemplateConfigParameter.TYPE_EDITABLE_LIST) {
            component = new JComboBox(params[i].getPresetValues());
            component.setName(params[i].getId());
            ((JComboBox) component).setEditable(true);
         }
         else {
            JagGenerator.logToConsole("ERROR: Template's config contains an unknown parameter type.");
            continue;
         }

         if (description != null) {
            component.setToolTipText(description);
         }

         add(component, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, y, 215, -1));
         configComponents.put(params[i].getId(), component);
      }
   }

   /**
    * Gets the mapping of (String) parameter id -> JComponent for all the configurable parameters.
    *
    * @return
    */
   public Map getConfigComponents() {
      return configComponents;
   }

}
