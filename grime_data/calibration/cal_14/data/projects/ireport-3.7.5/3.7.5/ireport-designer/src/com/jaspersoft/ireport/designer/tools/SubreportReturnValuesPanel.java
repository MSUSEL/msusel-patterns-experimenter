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
package com.jaspersoft.ireport.designer.tools;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.design.JRDesignSubreportReturnValue;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author  gtoffoli
 */
public class SubreportReturnValuesPanel extends javax.swing.JPanel {
    
    private List returnValues = new ArrayList();
    
    private ExpressionContext expressionContext = null;

    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }
    

    public List getReturnValues() {
        return returnValues;
    }

    /**
     * This method will duplicate the map. The panel will work on a copy of the map.
     **/
    @SuppressWarnings("unchecked")
    public void setReturnValues(List oldReturnVariables) {
        
        this.returnValues.clear();
        DefaultTableModel model = (DefaultTableModel)jTable.getModel();
        // Create a copy of the map content...
        Iterator iterator = oldReturnVariables.iterator();
        while (iterator.hasNext())
        {
            JRDesignSubreportReturnValue oldReturnValue = (JRDesignSubreportReturnValue)iterator.next();
            JRDesignSubreportReturnValue returnValue = new JRDesignSubreportReturnValue();
            returnValue.setSubreportVariable(oldReturnValue.getSubreportVariable() );
            returnValue.setCalculation(oldReturnValue.getCalculation() );
            returnValue.setIncrementerFactoryClassName(oldReturnValue.getIncrementerFactoryClassName() );
            returnValue.setToVariable(oldReturnValue.getToVariable() );
            returnValues.add(returnValue);
            model.addRow(new Object[]{returnValue, returnValue.getToVariable() });
        }
   }
    
    
    /** Creates new form SubreportParametersPanel */
    public SubreportReturnValuesPanel() {
        initComponents();
        
        javax.swing.DefaultListSelectionModel dlsm =  (javax.swing.DefaultListSelectionModel)this.jTable.getSelectionModel();
        dlsm.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent e)  {
                jTableSelectionValueChanged(e);
            }
        });
        
        jTable.getColumnModel().getColumn(0).setCellRenderer( new SubreportReturnValueCellRenderer());
        jLabelTitle.setText(I18n.getString("SubreportReturnValuesPanel.jLabelTitle.text"));
        jButtonAdd.setText(I18n.getString("Global.Button.Add"));
        jButtonModify.setText(I18n.getString("SubreportParametersPanel.jButtonModify.text"));
        jButtonDelete.setText(I18n.getString("Global.Button.Delete"));
        
        
    }
    
    public void jTableSelectionValueChanged(javax.swing.event.ListSelectionEvent e) {
        if (this.jTable.getSelectedRowCount() > 0) {
            this.jButtonModify.setEnabled(true);
            this.jButtonDelete.setEnabled(true);
        } else {
            this.jButtonModify.setEnabled(false);
            this.jButtonDelete.setEnabled(false);
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

        jLabelTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable = new org.jdesktop.swingx.JXTable();
        jPanel1 = new javax.swing.JPanel();
        jButtonAdd = new javax.swing.JButton();
        jButtonModify = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jLabelTitle.setText(I18n.getString("SubreportParametersPanel.jLabelTitle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        add(jLabelTitle, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(375, 275));

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subreport Variable", "Destination Variable"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable.setOpaque(false);
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        add(jScrollPane1, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonAdd.setText(I18n.getString("Global.Button.Add")); // NOI18N
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel1.add(jButtonAdd, gridBagConstraints);

        jButtonModify.setText(I18n.getString("SubreportParametersPanel.jButtonModify.text")); // NOI18N
        jButtonModify.setEnabled(false);
        jButtonModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonModifyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel1.add(jButtonModify, gridBagConstraints);

        jButtonDelete.setText(I18n.getString("Global.Button.Delete")); // NOI18N
        jButtonDelete.setEnabled(false);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel1.add(jButtonDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("unchecked")
    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        
        // Check if there is at least a local not system defined variable...
        List localVariables = IReportManager.getInstance().getActiveReport().getVariablesList();
        int count = 0;
        for (int i=0; count == 0 && i<localVariables.size(); ++i)
        {
            JRDesignVariable var = (JRDesignVariable)localVariables.get(i);
            if (!var.isSystemDefined()) count++;
        }
        
        if (count == 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this, 
                    I18n.getString("SubreportReturnValuesPanel.Message.Warning"),
                    I18n.getString("SubreportReturnValuesPanel.Message.Error"),
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Object pWin = SwingUtilities.getWindowAncestor(this);
        JRSubreportReturnValueDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRSubreportReturnValueDialog((Dialog)pWin);
        else jrpd = new JRSubreportReturnValueDialog((Frame)pWin);
        
        jrpd.updateVariables();
        jrpd.setVisible(true);
        
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            
            JRDesignSubreportReturnValue returnValue = jrpd.getSubreportReturnValue();
            returnValues.add(returnValue);
            
            DefaultTableModel model = (DefaultTableModel)jTable.getModel();
            model.addRow(new Object[]{returnValue, returnValue.getToVariable() });
        }
        
    }//GEN-LAST:event_jButtonAddActionPerformed

    @SuppressWarnings("unchecked")
    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        
        DefaultTableModel model = (DefaultTableModel)jTable.getModel();
        // Remove selected parameters...
        while (jTable.getSelectedRow() >= 0)
        {
            int row = jTable.getSelectedRow();
            row = ((JXTable)jTable).convertRowIndexToModel(row);
            returnValues.remove( model.getValueAt(row,0) );
            model.removeRow(row);
        }
        
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    @SuppressWarnings("unchecked")
    private void jButtonModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonModifyActionPerformed
        
        DefaultTableModel model = (DefaultTableModel)jTable.getModel();
        
        int row = jTable.getSelectedRow();
        if (row < 0) return;
        row = ((JXTable)jTable).convertRowIndexToModel(row);
        
        JRDesignSubreportReturnValue oldReturnValue = (JRDesignSubreportReturnValue)model.getValueAt(row,0);
        
        Window pWin = SwingUtilities.windowForComponent(this);
        
        JRSubreportReturnValueDialog jrpd = null;
        if (pWin instanceof Dialog) jrpd = new JRSubreportReturnValueDialog((Dialog)pWin);
        else if (pWin instanceof Frame) jrpd = new JRSubreportReturnValueDialog((Frame)pWin);
        else jrpd = new JRSubreportReturnValueDialog((Dialog)null);
        
        
        //JRSubreportReturnValueDialog jrpd = new JRSubreportReturnValueDialog(SwingUtilities.getWindowAncestor(this));
        jrpd.setSubreportReturnValue(oldReturnValue);
        jrpd.setVisible(true);
            
        if (jrpd.getDialogResult() == javax.swing.JOptionPane.OK_OPTION) {
            JRDesignSubreportReturnValue newReturnValue = jrpd.getSubreportReturnValue();
            returnValues.set(row, newReturnValue);
            model.setValueAt(newReturnValue, row, 0);
            model.setValueAt(newReturnValue.getToVariable() , row, 1);
            
            jTable.updateUI();
        }
    }//GEN-LAST:event_jButtonModifyActionPerformed

    @SuppressWarnings("unchecked")
    private void jTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableMouseClicked
        
        if (evt.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(evt))
        {
            if (jTable.getSelectedRowCount() > 0)
            {
                jButtonModifyActionPerformed(null);
            }
        }
        
    }//GEN-LAST:event_jTableMouseClicked
    
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonModify;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable;
    // End of variables declaration//GEN-END:variables
    
}
