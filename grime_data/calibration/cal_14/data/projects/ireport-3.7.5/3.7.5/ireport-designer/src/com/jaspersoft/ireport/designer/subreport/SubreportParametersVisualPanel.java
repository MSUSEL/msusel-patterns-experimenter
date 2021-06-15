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
package com.jaspersoft.ireport.designer.subreport;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.editor.ExpObject;
import com.jaspersoft.ireport.designer.editor.ExpObjectCellRenderer;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.editor.ExpressionEditor;
import com.jaspersoft.ireport.designer.editor.ExpressionEditorArea;
import com.jaspersoft.ireport.designer.editor.ExpressionEditorPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JasperDesign;

public final class SubreportParametersVisualPanel extends JPanel {

    SubreportParametersWizardPanel panel = null;
    
    /** Creates new form SubreportParametersVisualPanel */
    public SubreportParametersVisualPanel(SubreportParametersWizardPanel panel) {
        initComponents();
        this.panel = panel;

        ExpObjectCellRenderer cr = new ExpObjectCellRenderer();
        cr.setShowObjectType(false);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(cr);
        jTable1.getColumnModel().getColumn(1).setCellRenderer(new ExpObjectCellRenderer());
        
        
        TableColumn col = jTable1.getColumnModel().getColumn(1);
        TableComboBoxEditor tcb = new TableComboBoxEditor(new java.util.Vector());
        JComboBox cb = (JComboBox)tcb.getComponent();
        cb.setEditable(true);
        cb.removeAllItems();
        JasperDesign jd = IReportManager.getInstance().getActiveReport();
        Vector items = new Vector();
        items.addAll(jd.getParametersList());
        items.addAll(jd.getFieldsList());
        items.addAll(jd.getVariablesList());
        cb.setRenderer( new ExpObjectCellRenderer());
        cb.setModel(new DefaultComboBoxModel(items));
        
        
                
        
        col.setCellEditor(tcb);
        
        jTable1.setRowHeight(20);
    }

    @Override
    public String getName() {
        return I18n.getString("SubreportParametersVisualPanel.Label.Name");
    }
    
    
    public void setParameters(JRParameter[] params)
    {
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        dtm.setRowCount(0);
        
        if (params == null) return;
        for (int i=0; i<params.length; ++i)
        {
            if (params[i].isSystemDefined()) continue;
            dtm.addRow(new Object[]{new ExpObject(params[i]), null});
        }
    }
    
    
    public String[] getExpressions()
    {
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        String[] expressions = new String[dtm.getRowCount()];
        for (int i=0; i<expressions.length; ++i)
        {
            expressions[i] = (dtm.getValueAt(i, 1) == null) ? "" : (String)dtm.getValueAt(i, 1);
        }
        
        return expressions;
    }
    
    public void setExpressions(String[] expressions)
    {
        if (expressions == null) return;
        DefaultTableModel dtm = (DefaultTableModel)jTable1.getModel();
        for (int i=0; i<expressions.length; ++i)
        {
            if (dtm.getRowCount() > i)
            {
                dtm.setValueAt(expressions[i], i, 1);
            }
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(200, 200));
        setLayout(new java.awt.GridBagLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter name", "Expression"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jScrollPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

