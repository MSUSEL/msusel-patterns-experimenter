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
package com.jaspersoft.ireport.designer.charts.datasets;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.utils.Misc;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;

/**
 *
 * @author  Administrator
 */
public class TimeSeriesDialog extends javax.swing.JDialog {
    
    private JRDesignExpression seriesExpression = null;
    private JRDesignExpression timePeriodExpression = null;
    private JRDesignExpression valueExpression = null;
    private JRDesignExpression labelExpression = null;
    
    private int dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
    
    /** Creates new form CategorySeriesDialog */
    public TimeSeriesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        //applyI18n();
        
        this.setSize(500,500);
        this.setLocationRelativeTo(null);
        
        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };
       
        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);


        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonOK);
    }
    
    
    /**
     * this method is used to pass the correct subdataset to the expression editor
     */
    public void setExpressionContext( ExpressionContext ec )
    {
        jRTextExpressionLabel.setExpressionContext(ec);
        jRTextExpressionSeries.setExpressionContext(ec);
        jRTextExpressionTimePeriod.setExpressionContext(ec);
        jRTextExpressionValue.setExpressionContext(ec);
        sectionItemHyperlinkPanel1.setExpressionContext( ec);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabelSeriesExpression = new javax.swing.JLabel();
        jLabelTimePeriodExpression = new javax.swing.JLabel();
        jLabelValueExpression = new javax.swing.JLabel();
        jLabelLabelExpression = new javax.swing.JLabel();
        jRTextExpressionSeries = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jRTextExpressionTimePeriod = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jRTextExpressionValue = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jRTextExpressionLabel = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        sectionItemHyperlinkPanel1 = new com.jaspersoft.ireport.designer.tools.HyperlinkPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(I18n.getString("TimeSeriesDialog.Title.Time_series")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabelSeriesExpression.setText(I18n.getString("TimeSeriesDialog.Label.Series_expression_(required)")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jLabelSeriesExpression, gridBagConstraints);

        jLabelTimePeriodExpression.setText(I18n.getString("TimeSeriesDialog.Label.Time_period_expression")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel1.add(jLabelTimePeriodExpression, gridBagConstraints);

        jLabelValueExpression.setText(I18n.getString("TimeSeriesDialog.Label.Value_expression_(required)")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel1.add(jLabelValueExpression, gridBagConstraints);

        jLabelLabelExpression.setText(I18n.getString("TimeSeriesDialog.Label.Label_expression")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        jPanel1.add(jLabelLabelExpression, gridBagConstraints);

        jRTextExpressionSeries.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionSeries.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionSeries.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionSeries, gridBagConstraints);

        jRTextExpressionTimePeriod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionTimePeriod.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionTimePeriod.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionTimePeriod, gridBagConstraints);

        jRTextExpressionValue.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionValue.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionValue.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionValue, gridBagConstraints);

        jRTextExpressionLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jRTextExpressionLabel.setMinimumSize(new java.awt.Dimension(10, 10));
        jRTextExpressionLabel.setPreferredSize(new java.awt.Dimension(10, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jRTextExpressionLabel, gridBagConstraints);

        jTabbedPane1.addTab("Data", jPanel1);
        jTabbedPane1.addTab(I18n.getString("TimeSeriesDialog.Pane.Item_hyperlink"), sectionItemHyperlinkPanel1); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        jPanel6.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jPanel7, gridBagConstraints);

        jButtonOK.setMnemonic('o');
        jButtonOK.setText(I18n.getString("Global.Button.Ok")); // NOI18N
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel6.add(jButtonOK, gridBagConstraints);

        jButtonCancel.setMnemonic('c');
        jButtonCancel.setText(I18n.getString("Global.Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        jPanel6.add(jButtonCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel6, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        sectionItemHyperlinkPanel1.openExtraWindows();
    }//GEN-LAST:event_formWindowOpened

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed

        seriesExpression =  Misc.createExpression(null,  jRTextExpressionSeries.getText());
        timePeriodExpression =  Misc.createExpression("java.util.Date",  jRTextExpressionTimePeriod.getText());// NOI18N
        valueExpression =  Misc.createExpression("java.lang.Number",  jRTextExpressionValue.getText()); //NOI18N
        labelExpression =  Misc.createExpression("java.lang.String",  jRTextExpressionLabel.getText()); //NOI18N
       
        java.text.MessageFormat formatter = new java.text.MessageFormat( I18n.getString("TimeSeriesDialog.Message.Cannot_be_blank!") );

        if (seriesExpression == null) {
            
           javax.swing.JOptionPane.showMessageDialog(this, formatter.format(new Object[]{I18n.getString("TimeSeriesDialog.Pane.Series_expression")}) ,I18n.getString("TimeSeriesDialog.Message.Invalid_expression"),javax.swing.JOptionPane.ERROR_MESSAGE);
           return;
        } 
        
        if (getTimePeriodExpression() == null) {
           javax.swing.JOptionPane.showMessageDialog(this, formatter.format(new Object[]{I18n.getString("TimeSeriesDialog..Pane.Time_period_expression")}) ,I18n.getString("TimeSeriesDialog.Message.Invalid_expression"),javax.swing.JOptionPane.ERROR_MESSAGE);
           return;
        } 
                
        if (valueExpression == null) {
           javax.swing.JOptionPane.showMessageDialog(this, formatter.format(new Object[]{I18n.getString("TimeSeriesDialog.Pane.Value_expression")}) ,I18n.getString("TimeSeriesDialog.Message.Invalid_expression"),javax.swing.JOptionPane.ERROR_MESSAGE);
           return;
        } 
        
        this.setDialogResult(javax.swing.JOptionPane.OK_OPTION);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed
    
    public JRDesignExpression getSeriesExpression() {
        return seriesExpression;
    }

    public void setSeriesExpression(JRDesignExpression exp) {
        this.seriesExpression = null;
        if (exp != null)
        {
            try {
                this.seriesExpression = (JRDesignExpression)exp.clone();
            }  catch (Exception ex) { }
        }
        jRTextExpressionSeries.setText(Misc.getExpressionText(exp));
    }

    public JRDesignExpression getValueExpression() {
        return valueExpression;
    }

    public void setValueExpression(JRDesignExpression exp) {
        this.valueExpression = null;
        if (exp != null)
        {
            try {
                this.valueExpression = (JRDesignExpression)exp.clone();
            }  catch (Exception ex) { }
        }
        jRTextExpressionValue.setText(Misc.getExpressionText(exp));
    }

    public JRDesignExpression getLabelExpression() {
        return labelExpression;
    }

    public void setLabelExpression(JRDesignExpression exp) {
        this.labelExpression = null;
        if (exp != null)
        {
            try {
                this.labelExpression = (JRDesignExpression)exp.clone();
            }  catch (Exception ex) { }
        }
        jRTextExpressionLabel.setText(Misc.getExpressionText(exp));
    }
    
    
    public JRDesignExpression getTimePeriodExpression() {
        return timePeriodExpression;
    }

    public void setTimePeriodExpression(JRDesignExpression exp) {
        this.timePeriodExpression = null;
        if (exp != null)
        {
            try {
                this.timePeriodExpression = (JRDesignExpression)exp.clone();
            }  catch (Exception ex) { }
        }
        jRTextExpressionTimePeriod.setText(Misc.getExpressionText(exp));
    }

    public void setSectionItemHyperlink(JRDesignHyperlink sectionItemHyperlink)
    {
        sectionItemHyperlinkPanel1.setHyperlink( sectionItemHyperlink );
    }
    
    public JRHyperlink getSectionItemHyperlink()
    {
        return sectionItemHyperlinkPanel1.getHyperlink();
    }
    
    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JLabel jLabelLabelExpression;
    private javax.swing.JLabel jLabelSeriesExpression;
    private javax.swing.JLabel jLabelTimePeriodExpression;
    private javax.swing.JLabel jLabelValueExpression;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionLabel;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionSeries;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionTimePeriod;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionValue;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.jaspersoft.ireport.designer.tools.HyperlinkPanel sectionItemHyperlinkPanel1;
    // End of variables declaration//GEN-END:variables
    
    /*
        public void applyI18n()
        {
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
            jButtonOK.setText( it.businesslogic.ireport.util.I18n.getString("ok","Ok"));
            jButtonCancel.setText( it.businesslogic.ireport.util.I18n.getString("cancel","Cancel"));
             
            jLabelTimePeriodExpression.setText( it.businesslogic.ireport.util.I18n.getString("charts.timePeriodExpression","Time period expression"));
            jLabelSeriesExpression.setText( it.businesslogic.ireport.util.I18n.getString("charts.seriesExpression","Series expression"));
            jLabelValueExpression.setText( it.businesslogic.ireport.util.I18n.getString("charts.valueExpression","Value expression"));
            jLabelLabelExpression.setText( it.businesslogic.ireport.util.I18n.getString("charts.labelExpression","Label expression"));
            
            jTabbedPane1.setTitleAt(0,I18n.getString("chartSeries.tab.Data","Data"));
            jTabbedPane1.setTitleAt(1,I18n.getString("chartSeries.tab.ItemHyperlink","Item hyperlink"));
            
            this.setTitle(I18n.getString("timeSeriesDialog.title","Time series"));
            jButtonCancel.setMnemonic(I18n.getString("timeSeriesDialog.buttonCancelMnemonic","c").charAt(0));
            jButtonOK.setMnemonic(I18n.getString("timeSeriesDialog.buttonOKMnemonic","o").charAt(0));
            
            this.getRootPane().updateUI();
        }
    */
    
    
    
    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_SERIES_EXPRESSION=1;
    public static final int COMPONENT_TIME_EXPRESSION=2;
    public static final int COMPONENT_VALUE_EXPRESSION=3;
    public static final int COMPONENT_LABEL_EXPRESSION=4;
    public static final int COMPONENT_HYPERLINK=100;
        
    /**
     * This method set the focus on a specific component.
     * 
     * expressionInfo[0] can be something like:
     * COMPONENT_SERIES_EXPRESSION, ...
     *
     * If it is COMPONENT_HYPERLINK, other parameters are expected...
     * otherInfo is used here only for COMPONENT_HYPERLINK
     * otherInfo[0] = expression ID
     * otherInfo[1] = parameter #
     * otherInfo[2] = parameter expression ID
     */
    public void setFocusedExpression(Object[] expressionInfo)
    {
        if (expressionInfo == null) return;
        int expID = ((Integer)expressionInfo[0]).intValue();
        
        switch (expID)
        {
            case COMPONENT_SERIES_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionSeries);
                break;
            case COMPONENT_TIME_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionTimePeriod);
                break;
            case COMPONENT_VALUE_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionValue);
                break;
            case COMPONENT_LABEL_EXPRESSION:
                Misc.selectTextAndFocusArea(jRTextExpressionLabel);
                break;
            case COMPONENT_HYPERLINK:
                jTabbedPane1.setSelectedComponent( sectionItemHyperlinkPanel1 );
                Object newInfo[] = new Object[expressionInfo.length -1 ];
                for (int i=1; i< expressionInfo.length; ++i) newInfo[i-1] = expressionInfo[i];
                sectionItemHyperlinkPanel1.setFocusedExpression(newInfo);
                break;
        }
    }
}
