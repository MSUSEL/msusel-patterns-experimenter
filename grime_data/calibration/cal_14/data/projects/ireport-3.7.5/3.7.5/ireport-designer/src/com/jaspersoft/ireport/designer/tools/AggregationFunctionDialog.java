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

import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.Misc;
import com.jaspersoft.ireport.locale.I18n;
import net.sf.jasperreports.engine.design.JRDesignVariable;

/**
 *
 * @author gtoffoli
 */
public class AggregationFunctionDialog extends java.awt.Dialog {

    public static int STRING_SET = 0;
    public static int NUMERIC_SET = 1;

    public static int DEFAULT_AS_FUNCTION = 0;
    public static int DEFAULT_AS_VALUE = 1;


    /** Creates new form AggregationFunctionDialog */
    public AggregationFunctionDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);

        jLabel1.setText(I18n.getString("AggregationFunctionDialog.jLabel1.text")); // NOI18N
        jRadioButtonValue.setText(I18n.getString("AggregationFunctionDialog.jRadioButtonValue.text")); // NOI18N
        jButtonOk.setText(I18n.getString("AggregationFunctionDialog.jButtonOk.text")); // NOI18N
        jRadioButtonAggregation.setText(I18n.getString("AggregationFunctionDialog.jRadioButtonAggregation.text")); // NOI18N
        setTitle("");
    }

    public void setDefaultSelection(int defaultAs)
    {
        if (defaultAs == DEFAULT_AS_FUNCTION) jRadioButtonAggregation.setSelected(true);
        if (defaultAs == DEFAULT_AS_VALUE) jRadioButtonValue.setSelected(true);

    }

    public void setFunctionSet(int set)
    {
        jComboBox1.removeAllItems();

        jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_COUNT), I18n.getString("VariableNode.Property.Count")));
        jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_DISTINCT_COUNT), I18n.getString("VariableNode.Property.DistinctCount")));

        jComboBox1.setSelectedIndex(0);

        if (set == NUMERIC_SET)
        {
            //jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_NOTHING), I18n.getString("VariableNode.Property.Nothing")));
            jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_SUM), I18n.getString("VariableNode.Property.Sum")));
            jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_AVERAGE), I18n.getString("VariableNode.Property.Average")));
            jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_LOWEST), I18n.getString("VariableNode.Property.Lowest")));
            jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_HIGHEST), I18n.getString("VariableNode.Property.Highest")));
            jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_STANDARD_DEVIATION), I18n.getString("VariableNode.Property.StandardDeviation")));
            jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_VARIANCE), I18n.getString("VariableNode.Property.Variance")));
            jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_SYSTEM), I18n.getString("VariableNode.Property.System")));
            jComboBox1.addItem(new Tag(new Byte(JRDesignVariable.CALCULATION_FIRST), I18n.getString("VariableNode.Property.First")));
            setSelectedFunction(JRDesignVariable.CALCULATION_SUM);
        }

    }

    public void setSelectedFunction(String function)
    {
        if (function == null) return;
        if (function.equals("Count")) setSelectedFunction(JRDesignVariable.CALCULATION_COUNT);
        if (function.equals("DistinctCount")) setSelectedFunction(JRDesignVariable.CALCULATION_COUNT);
        if (function.equals("Sum")) setSelectedFunction(JRDesignVariable.CALCULATION_SUM);
        if (function.equals("Average")) setSelectedFunction(JRDesignVariable.CALCULATION_AVERAGE);
        if (function.equals("Lowest")) setSelectedFunction(JRDesignVariable.CALCULATION_LOWEST);
        if (function.equals("Highest")) setSelectedFunction(JRDesignVariable.CALCULATION_HIGHEST);
        if (function.equals("standardDeviation")) setSelectedFunction(JRDesignVariable.CALCULATION_STANDARD_DEVIATION);
        if (function.equals("variance")) setSelectedFunction(JRDesignVariable.CALCULATION_VARIANCE);
        if (function.equals("first")) setSelectedFunction(JRDesignVariable.CALCULATION_FIRST);

        // No function found...
        return;
    }
    public void setSelectedFunction(Byte function)
    {
        if (function == null) return;
        Misc.setComboboxSelectedTagValue(jComboBox1, function);
    }

    public Byte getSelectedFunction()
    {
        if (jRadioButtonAggregation.isSelected())
        {
            Object obj = jComboBox1.getSelectedItem();
            if (obj != null)
            {
                Tag t = (Tag)obj;
                return (Byte)t.getValue();
            }
        }

        return null;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jRadioButtonValue = new javax.swing.JRadioButton();
        jRadioButtonAggregation = new javax.swing.JRadioButton();
        jComboBox1 = new javax.swing.JComboBox();
        jButtonOk = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setTitle(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jLabel1.setText("What...");

        buttonGroup1.add(jRadioButtonValue);
        jRadioButtonValue.setSelected(true);
        jRadioButtonValue.setText("Value");

        buttonGroup1.add(jRadioButtonAggregation);
        jRadioButtonAggregation.setText("Aggregation");

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButtonOk.setText("Ok");
        jButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOkActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jRadioButtonValue)
                        .addContainerGap())
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(jRadioButtonAggregation)
                            .addContainerGap())
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                .addContainerGap())
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                                .addContainerGap())
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(jButtonOk, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())))))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(31, 31, 31)
                .add(jComboBox1, 0, 225, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jLabel1)
                .add(8, 8, 8)
                .add(jRadioButtonValue)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jRadioButtonAggregation)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButtonOk)
                .add(21, 21, 21))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed

        jRadioButtonAggregation.setSelected(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOkActionPerformed
       this.setVisible(true);
       this.dispose();
    }//GEN-LAST:event_jButtonOkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonOk;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButtonAggregation;
    private javax.swing.JRadioButton jRadioButtonValue;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

}
