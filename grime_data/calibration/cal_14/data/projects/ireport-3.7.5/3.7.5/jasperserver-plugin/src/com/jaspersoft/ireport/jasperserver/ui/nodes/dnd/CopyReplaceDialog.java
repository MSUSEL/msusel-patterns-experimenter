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
/*
 * CopyReplaceDialog.java
 *
 * Created on 31-ago-2010, 17.09.55
 */

package com.jaspersoft.ireport.jasperserver.ui.nodes.dnd;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

/**
 *
 * @author gtoffoli
 */
public class CopyReplaceDialog extends javax.swing.JDialog {

    private int dialogResult = HighlightingPanel.TYPE_DONT_COPY;
    /** Creates new form CopyReplaceDialog */
    public CopyReplaceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public void setResourceDescriptors(ResourceDescriptor origin, ResourceDescriptor dest, String newName)
    {
        setResourceDescriptors(origin, dest, newName, false);
    }

    public void setResourceDescriptors(ResourceDescriptor origin, ResourceDescriptor dest, String newName, boolean move)
    {
        this.setTitle( (move) ? "Move resource" : "Copy resource");
        highlightingPanelCR.setMode((move) ? HighlightingPanel.MODE_MOVE : HighlightingPanel.MODE_COPY);
        highlightingPanelDC.setMode((move) ? HighlightingPanel.MODE_MOVE : HighlightingPanel.MODE_COPY);
        highlightingPanelCC.setMode((move) ? HighlightingPanel.MODE_MOVE : HighlightingPanel.MODE_COPY);

        highlightingPanelCR.setResource(origin, HighlightingPanel.TYPE_COPY_REPLACE, null);
        highlightingPanelDC.setResource(dest, HighlightingPanel.TYPE_DONT_COPY, null);
        highlightingPanelCC.setResource(null, HighlightingPanel.TYPE_COPY, newName);
        this.pack();
        setLocationRelativeTo(null);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        highlightingPanelCR = new com.jaspersoft.ireport.jasperserver.ui.nodes.dnd.HighlightingPanel();
        highlightingPanelDC = new com.jaspersoft.ireport.jasperserver.ui.nodes.dnd.HighlightingPanel();
        highlightingPanelCC = new com.jaspersoft.ireport.jasperserver.ui.nodes.dnd.HighlightingPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButtonCancel.setText(org.openide.util.NbBundle.getMessage(CopyReplaceDialog.class, "CopyReplaceDialog.jButtonCancel.text")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        highlightingPanelCR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightingPanelCRActionPerformed(evt);
            }
        });

        highlightingPanelDC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightingPanelDCActionPerformed(evt);
            }
        });

        highlightingPanelCC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                highlightingPanelCCActionPerformed(evt);
            }
        });

        jLabel1.setForeground(new java.awt.Color(51, 0, 153));
        jLabel1.setText(org.openide.util.NbBundle.getMessage(CopyReplaceDialog.class, "CopyReplaceDialog.jLabel1.text")); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(CopyReplaceDialog.class, "CopyReplaceDialog.jLabel2.text")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, highlightingPanelCR, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, highlightingPanelCC, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, highlightingPanelDC, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(highlightingPanelCR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(highlightingPanelDC, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(highlightingPanelCC, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(401, Short.MAX_VALUE)
                .add(jButtonCancel)
                .addContainerGap())
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButtonCancel)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void highlightingPanelCRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightingPanelCRActionPerformed
        setDialogResult(HighlightingPanel.TYPE_COPY_REPLACE);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_highlightingPanelCRActionPerformed

    private void highlightingPanelDCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightingPanelDCActionPerformed
        setDialogResult(HighlightingPanel.TYPE_DONT_COPY);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_highlightingPanelDCActionPerformed

    private void highlightingPanelCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_highlightingPanelCCActionPerformed
        setDialogResult(HighlightingPanel.TYPE_COPY);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_highlightingPanelCCActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setDialogResult(HighlightingPanel.TYPE_DONT_COPY);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.jaspersoft.ireport.jasperserver.ui.nodes.dnd.HighlightingPanel highlightingPanelCC;
    private com.jaspersoft.ireport.jasperserver.ui.nodes.dnd.HighlightingPanel highlightingPanelCR;
    private com.jaspersoft.ireport.jasperserver.ui.nodes.dnd.HighlightingPanel highlightingPanelDC;
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the dialogResult
     */
    public int getDialogResult() {
        return dialogResult;
    }

    /**
     * @param dialogResult the dialogResult to set
     */
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

}
