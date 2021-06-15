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
package com.jaspersoft.ireport.designer.editor;

import com.jaspersoft.ireport.locale.I18n;
import javax.swing.JOptionPane;

/**
 *
 * @author  gtoffoli
 */
public class ExpressionEditorArea extends javax.swing.JPanel {
    
    public ExpressionContext getExpressionContext() {
        return this.expressionEditorPane1.getExpressionContext();
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionEditorPane1.setExpressionContext(expressionContext);
    }
    
    public void setText(String text) {
        this.expressionEditorPane1.setText(text);
    }
    
    public String getText( ) {
        return this.expressionEditorPane1.getText();
    }
    
    /** Creates new form ExpressionEditorArea */
    public ExpressionEditorArea() {
        initComponents();
    }
    
    public ExpressionEditorPane getExpressionEditorPane()
    {
        return this.expressionEditorPane1;
    }
    
    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        this.expressionEditorPane1.setEnabled(b);
        this.jButton1.setEnabled(b);
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
        expressionEditorPane1 = new com.jaspersoft.ireport.designer.editor.ExpressionEditorPane();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setViewportView(expressionEditorPane1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/editor/text_edit.png"))); // NOI18N
        jButton1.setText(I18n.getString("ExpressionEditorArea.jButton1.text")); // NOI18N
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        add(jButton1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
        ExpressionEditor ed = new ExpressionEditor();
        ed.setExpressionContext( this.expressionEditorPane1.getExpressionContext() );
        ed.setExpression( this.expressionEditorPane1.getText() );    
        if (ed.showDialog(this) == JOptionPane.OK_OPTION)
        {
            this.expressionEditorPane1.setText( ed.getExpression() );
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jaspersoft.ireport.designer.editor.ExpressionEditorPane expressionEditorPane1;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    
}
