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

import com.jaspersoft.ireport.designer.menu.OpenQueryDialogAction;
import com.jaspersoft.ireport.designer.menu.ZoomInAction;
import com.jaspersoft.ireport.designer.menu.ZoomOutAction;
import com.jaspersoft.ireport.designer.actions.CompileReportAction;
import com.jaspersoft.ireport.designer.toolbars.TextElementsToolbar;
import javax.swing.JToolBar;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author  gtoffoli
 */
public class JrxmlEditorToolbar extends JToolBar {
    
    /** Creates new form JrxmlEditorToolbar */
    public JrxmlEditorToolbar() {
        initComponents();
        add(new TextElementsToolbar());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnOpenQuery = add(SystemAction.get(OpenQueryDialogAction.class));
        btnZoomIn = add(SystemAction.get(ZoomInAction.class));
        btnZoomOut = add(SystemAction.get(ZoomOutAction.class));
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnCompile = add(SystemAction.get(CompileReportAction.class));
        jSeparator2 = new javax.swing.JToolBar.Separator();

        setBorder(null);
        setRollover(true);
        add(jSeparator1);

        btnOpenQuery.setText(org.openide.util.NbBundle.getMessage(JrxmlEditorToolbar.class, "JrxmlEditorToolbar.btnOpenQuery.text")); // NOI18N
        btnOpenQuery.setFocusable(false);
        btnOpenQuery.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnOpenQuery.setMaximumSize(new java.awt.Dimension(23, 23));
        btnOpenQuery.setMinimumSize(new java.awt.Dimension(23, 23));
        btnOpenQuery.setPreferredSize(new java.awt.Dimension(23, 23));
        btnOpenQuery.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(btnOpenQuery);

        btnZoomIn.setText(org.openide.util.NbBundle.getMessage(JrxmlEditorToolbar.class, "JrxmlEditorToolbar.btnZoomIn.text")); // NOI18N
        btnZoomIn.setFocusable(false);
        btnZoomIn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomIn.setMaximumSize(new java.awt.Dimension(23, 23));
        btnZoomIn.setMinimumSize(new java.awt.Dimension(23, 23));
        btnZoomIn.setPreferredSize(new java.awt.Dimension(23, 23));
        btnZoomIn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(btnZoomIn);

        btnZoomOut.setText(org.openide.util.NbBundle.getMessage(JrxmlEditorToolbar.class, "JrxmlEditorToolbar.btnZoomOut.text")); // NOI18N
        btnZoomOut.setFocusable(false);
        btnZoomOut.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnZoomOut.setMaximumSize(new java.awt.Dimension(23, 23));
        btnZoomOut.setMinimumSize(new java.awt.Dimension(23, 23));
        btnZoomOut.setPreferredSize(new java.awt.Dimension(23, 23));
        btnZoomOut.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(btnZoomOut);
        add(jSeparator3);

        btnCompile.setText(org.openide.util.NbBundle.getMessage(JrxmlEditorToolbar.class, "JrxmlEditorToolbar.btnCompile.text")); // NOI18N
        btnCompile.setFocusable(false);
        btnCompile.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCompile.setMaximumSize(new java.awt.Dimension(23, 23));
        btnCompile.setMinimumSize(new java.awt.Dimension(23, 23));
        btnCompile.setPreferredSize(new java.awt.Dimension(23, 23));
        btnCompile.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add(btnCompile);
        add(jSeparator2);
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCompile;
    private javax.swing.JButton btnOpenQuery;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    // End of variables declaration//GEN-END:variables
    
}
