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
package com.jaspersoft.ireport.designer.wizards;

import com.jaspersoft.ireport.locale.I18n;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.design.JRDesignField;

public final class GroupingVisualPanel extends JPanel {

    private GroupingWizardPanel panel = null;
    
    private List<JComboBox> comboboxComponents = null;
    private List<JLabel> labelComponents = null;
    
    
    
    /** Creates new form NewJrxmlVisualPanel2 */
    public GroupingVisualPanel(GroupingWizardPanel panel) {
        this.panel = panel;
        initComponents();
        
        comboboxComponents = new ArrayList<JComboBox>();
        comboboxComponents.add(jComboBoxGroup1);
        comboboxComponents.add(jComboBoxGroup2);
        comboboxComponents.add(jComboBoxGroup3);
        comboboxComponents.add(jComboBoxGroup4);
        
        labelComponents = new ArrayList<JLabel>();
        labelComponents.add(jLabelGroup1);
        labelComponents.add(jLabelGroup2);
        labelComponents.add(jLabelGroup3);
        labelComponents.add(jLabelGroup4);
        
        for (JComboBox cb : comboboxComponents)
        {
            cb.setRenderer(new GroupingVisualPanel.FieldListCellRenderer());
        }
    }

    @Override
    public String getName() {
        return I18n.getString("GroupingVisualPanel.Name.GroupBy");
    }
    
    
    public void updateLists()
    {
        jComboBoxGroup1.removeAllItems();
        
        jComboBoxGroup1.addItem("");
        List<JRDesignField> selectedFields = (List<JRDesignField>) panel.getWizard().getProperty("selectedFields");
        if (selectedFields != null)
        {
            for (JRDesignField field : selectedFields)
            {
                jComboBoxGroup1.addItem(field);
            }
        }
    }

    
    private void updateCombos(int num)
    {
        if (comboboxComponents.get(num).getSelectedIndex() <= 0)
        {
            comboboxComponents.get(num+1).removeAllItems();
            comboboxComponents.get(num+1).setEnabled(false);
            labelComponents.get(num+1).setEnabled(false);
        }
        else
        {
            Object obj = comboboxComponents.get(num).getSelectedItem();
            comboboxComponents.get(num+1).removeAllItems();
            for (int i=0; i<comboboxComponents.get(num).getItemCount(); ++i)
            {
                Object item = comboboxComponents.get(num).getItemAt(i);
                if (item != null && !item.equals(obj))
                {
                    comboboxComponents.get(num+1).addItem(item);
                }
                comboboxComponents.get(num+1).setEnabled(true);
                labelComponents.get(num+1).setEnabled(true);
            }
            comboboxComponents.get(num+1).setSelectedIndex(0);
        }
        
    }
    
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelGroup1 = new javax.swing.JLabel();
        jComboBoxGroup1 = new javax.swing.JComboBox();
        jLabelGroup2 = new javax.swing.JLabel();
        jComboBoxGroup2 = new javax.swing.JComboBox();
        jLabelGroup3 = new javax.swing.JLabel();
        jComboBoxGroup3 = new javax.swing.JComboBox();
        jLabelGroup4 = new javax.swing.JLabel();
        jComboBoxGroup4 = new javax.swing.JComboBox();

        setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(jLabelGroup1, I18n.getString("GroupingVisualPanel.Label.Group1")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        add(jLabelGroup1, gridBagConstraints);

        jComboBoxGroup1.setMinimumSize(new java.awt.Dimension(28, 20));
        jComboBoxGroup1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGroup1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(jComboBoxGroup1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelGroup2, I18n.getString("GroupingVisualPanel.Label.Group2")); // NOI18N
        jLabelGroup2.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        add(jLabelGroup2, gridBagConstraints);

        jComboBoxGroup2.setEnabled(false);
        jComboBoxGroup2.setMinimumSize(new java.awt.Dimension(28, 20));
        jComboBoxGroup2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGroup2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(jComboBoxGroup2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelGroup3, I18n.getString("GroupingVisualPanel.Label.Group3")); // NOI18N
        jLabelGroup3.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        add(jLabelGroup3, gridBagConstraints);

        jComboBoxGroup3.setEnabled(false);
        jComboBoxGroup3.setMinimumSize(new java.awt.Dimension(28, 20));
        jComboBoxGroup3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxGroup3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(jComboBoxGroup3, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(jLabelGroup4, I18n.getString("GroupingVisualPanel.Label.Group4")); // NOI18N
        jLabelGroup4.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        add(jLabelGroup4, gridBagConstraints);

        jComboBoxGroup4.setEnabled(false);
        jComboBoxGroup4.setMinimumSize(new java.awt.Dimension(28, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(jComboBoxGroup4, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxGroup1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGroup1ActionPerformed
        updateCombos(0);
    }//GEN-LAST:event_jComboBoxGroup1ActionPerformed

    private void jComboBoxGroup2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGroup2ActionPerformed
        updateCombos(1);
    }//GEN-LAST:event_jComboBoxGroup2ActionPerformed

    private void jComboBoxGroup3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxGroup3ActionPerformed
        updateCombos(2);
    }//GEN-LAST:event_jComboBoxGroup3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBoxGroup1;
    private javax.swing.JComboBox jComboBoxGroup2;
    private javax.swing.JComboBox jComboBoxGroup3;
    private javax.swing.JComboBox jComboBoxGroup4;
    private javax.swing.JLabel jLabelGroup1;
    private javax.swing.JLabel jLabelGroup2;
    private javax.swing.JLabel jLabelGroup3;
    private javax.swing.JLabel jLabelGroup4;
    // End of variables declaration//GEN-END:variables

    
    static class FieldListCellRenderer extends DefaultListCellRenderer
    {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
            if (value instanceof JRDesignField)
            {
                value = ((JRDesignField)value).getName();
            }
           
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
        
    }
    
    public List<JRDesignField> getGroupFields()
    {
        List<JRDesignField> list = new ArrayList<JRDesignField>();
        for (JComboBox cb : comboboxComponents)
        {
            if (cb.getSelectedIndex() > 0)
            {
                list.add((JRDesignField)cb.getSelectedItem());
            }
        }
        return list;
    }

}

