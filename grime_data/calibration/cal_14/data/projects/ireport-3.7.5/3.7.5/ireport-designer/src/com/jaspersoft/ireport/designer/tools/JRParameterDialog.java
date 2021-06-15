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

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.editor.ExpressionContext;
import com.jaspersoft.ireport.designer.sheet.Tag;
import com.jaspersoft.ireport.designer.utils.Misc;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignParameter;

/**
 *
 * @author  Administrator
 */
public class JRParameterDialog extends javax.swing.JDialog {
    /** Creates new form JRParameterDialog */
    JRDesignParameter tmpParameter = null;
    private String oldName = null;
    private Map currentParameters = null;
    
    public JRParameterDialog(Dialog parent, Map currentParameters) 
    {
         super(parent);
         initAll(currentParameters);
    }

    /** Creates new form ReportQueryFrame */
    public JRParameterDialog(Frame parent, Map currentParameters) 
    {
         super(parent);
         initAll(currentParameters);
    }

    
    public void initAll(Map currentParameters) {
   
        setModal(true);
        initComponents();
        this.currentParameters = currentParameters;
        //applyI18n();
        this.jRTextExpressionAreaDefaultExpression.setText("");   
        
        // we have to force the context of the parameter.
        setLocationRelativeTo(null);
        
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
        jComboBox1.setRenderer(new ClassNameComboxCellRenderer());

        java.util.List classes = new ArrayList();
        classes.add(new Tag("java.lang.String","Text"));
        classes.add(new Tag("java.util.Date","Date"));
        classes.add(new Tag("java.sql.Time","Date/Time"));
        classes.add(new Tag("java.lang.Boolean","Boolean"));
        classes.add(new Tag("java.lang.Integer","Integer"));
        classes.add(new Tag("java.lang.Double","Double"));
        classes.add(new Tag("java.lang.Number","Number"));
        classes.add(new Tag("java.lang.Byte","Byte"));
        classes.add(new Tag("java.sql.Timestamp","Timestamp"));
        classes.add(new Tag("java.lang.Float","Float"));
        classes.add(new Tag("java.lang.Long","Long"));
        classes.add(new Tag("java.lang.Short","Short"));
        classes.add(new Tag("java.math.BigDecimal","Big Decimal"));
        classes.add(new Tag("java.util.Collection","Collection"));
        classes.add(new Tag("java.util.List","List"));
        classes.add(new Tag("java.lang.Object","Object"));
        classes.add(new Tag("java.io.InputStream"));
        classes.add(new Tag("net.sf.jasperreports.engine.JREmptyDataSource"));


        jComboBox1.setModel(new DefaultComboBoxModel(classes.toArray()));

        jComboBox1.setSelectedIndex(0);
        jComboBox1.updateUI();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jRTextExpressionAreaDefaultExpression = new com.jaspersoft.ireport.designer.editor.ExpressionEditorArea();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        setTitle("Add parameter");
        setModal(true);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButtonOK.setMnemonic('o');
        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonOK);

        jButtonCancel.setMnemonic('c');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setPreferredSize(new java.awt.Dimension(350, 250));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Parameter name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(jLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        jPanel2.add(jTextFieldName, gridBagConstraints);

        jLabel3.setText("Value expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jLabel3, gridBagConstraints);

        jRTextExpressionAreaDefaultExpression.setPreferredSize(new java.awt.Dimension(300, 80));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
        jPanel2.add(jRTextExpressionAreaDefaultExpression, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator1.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(jSeparator1, gridBagConstraints);

        jLabel2.setText("Default Value Expression");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 3);
        jPanel2.add(jLabel2, gridBagConstraints);

        jComboBox1.setEditable(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(jComboBox1, gridBagConstraints);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setBounds(0, 0, 320, 293);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.CANCEL_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOKActionPerformed
        
        String newName = this.jTextFieldName.getText().trim();
        if (newName.length() <= 0)
        {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("JRParameterDialog.Message.Warning"),
                    I18n.getString("JRParameterDialog.Message.Error"),
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            return;
        }
        
        if ( (oldName == null || !oldName.equals(newName)) && currentParameters != null &&
            currentParameters.containsKey(newName))
        {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("JRParameterDialog.Message.Warning2"),
                    I18n.getString("JRParameterDialog.Message.Error2"),
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            return;
        }

        String className = "";
        Object obj = jComboBox1.getSelectedItem();
        if (obj == null) className = "";
        else if (obj instanceof Tag) className = ((Tag)obj).getValue().toString();
        else className = Misc.nvl(obj,"");
         
        if (className.length() == 0 || !className.matches("(\\p{Alpha}\\p{Alnum}*\\_*\\.)*(\\p{Alpha}\\p{Alnum}*\\_*)"))
        {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("JRParameterDialog.Message.InvalidClassName"),
                    I18n.getString("JRParameterDialog.Message.InvalidClassNameTitle"),
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            return;
        }
        else
        {
            try {
                // Try to check if the class exists...
                Class.forName(className, false, IReportManager.getReportClassLoader());
            } catch (ClassNotFoundException ex) {

                if (javax.swing.JOptionPane.showConfirmDialog(this,
                    I18n.getString("JRParameterDialog.Message.UnknownClassName", className),
                    I18n.getString("JRParameterDialog.Message.UnknownClassNameTitle"),
                    javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                    javax.swing.JOptionPane.INFORMATION_MESSAGE) != JOptionPane.OK_OPTION)
                {

                    return;
                }
            }
            

        }

        tmpParameter = new JRDesignParameter();

        tmpParameter.setName(this.jTextFieldName.getText().trim());
        JRDesignExpression exp = new JRDesignExpression();
        exp.setText(this.jRTextExpressionAreaDefaultExpression.getText());
        tmpParameter.setValueClassName(className);
        exp.setValueClassName(className);
        tmpParameter.setDefaultValueExpression(exp);

        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.OK_OPTION);
        dispose();
    }//GEN-LAST:event_jButtonOKActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        this.setDialogResult( javax.swing.JOptionPane.CLOSED_OPTION);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /** Getter for property tmpParameter.
     * @return Value of property tmpParameter.
     *
     */
    public JRDesignParameter getParameter() {
        return tmpParameter;
    }    
    
    /** Setter for property tmpParameter.
     * @param tmpParameter New value of property tmpParameter.
     **/
    public void setParameter(JRDesignParameter tmpParameter) {
        this.jTextFieldName.setText( tmpParameter.getName() );
        
        oldName =tmpParameter.getName();
        
        String text = "";
        
        if (tmpParameter.getDefaultValueExpression() != null  &&
            tmpParameter.getDefaultValueExpression().getText() != null )
        {
            text = tmpParameter.getDefaultValueExpression().getText();
        }
        
        String className = tmpParameter.getValueClassName();
        
        if (className != null)
        {
            for (int i=0; i<jComboBox1.getItemCount(); ++i)
            {
                Object val=jComboBox1.getItemAt(i);
                if (val instanceof Tag && className.equals(((Tag)val).getValue()))
                {
                    jComboBox1.setSelectedIndex(i);
                    break;
                }
                else if (className.equals(val))
                {
                    jComboBox1.setSelectedIndex(i);
                    break;
                }
            }
        }

        this.jRTextExpressionAreaDefaultExpression.setText( text );                       
    }
     
    
    /** Getter for property dialogResult.
     * @return Value of property dialogResult.
     *
     */
    public int getDialogResult() {
        return dialogResult;
    }
    
    /** Setter for property dialogResult.
     * @param dialogResult New value of property dialogResult.
     *
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorArea jRTextExpressionAreaDefaultExpression;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables

    private int dialogResult;    
    
    /*
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jButtonCancel.setText(I18n.getString("jRSubreportParameterDialog.buttonCancel","Cancel"));
                jButtonOK.setText(I18n.getString("jRSubreportParameterDialog.buttonOK","OK"));
                jLabel1.setText(I18n.getString("jRSubreportParameterDialog.label1","Subreport parameter name"));
                jLabel3.setText(I18n.getString("jRSubreportParameterDialog.label3","Default value expression"));
                // End autogenerated code ----------------------
                this.setTitle(I18n.getString("jRSubreportParameterDialog.title","Add/modify parameter"));
                jButtonCancel.setMnemonic(I18n.getString("jRSubreportParameterDialog.buttonCancelMnemonic","c").charAt(0));
                jButtonOK.setMnemonic(I18n.getString("jRSubreportParameterDialog.buttonOKMnemonic","o").charAt(0));
    }
     */
    
    public static final int COMPONENT_NONE=0;
    public static final int COMPONENT_PARAM_NAME=1;
    public static final int COMPONENT_PARAM_EXPRESSION=2;
    
    /**
     * This method set the focus on a specific component.
     * Valid constants are something like:
     * COMPONENT_NONE, COMPONENT_PARAM_NAME, ...
     *
     */
    public void setFocusedExpression(int expID)
    {
        try {
            switch (expID)
            {
                case COMPONENT_PARAM_EXPRESSION:
                    Misc.selectTextAndFocusArea(jRTextExpressionAreaDefaultExpression);
                    break;
                case COMPONENT_PARAM_NAME:
                    Misc.selectTextAndFocusArea(jTextFieldName);
                    break;  
            }
        } catch (Exception ex) { }
    }    
    
    public void setExpressionContext(ExpressionContext context)
    {
        this.jRTextExpressionAreaDefaultExpression.setExpressionContext(context);
    }
}
