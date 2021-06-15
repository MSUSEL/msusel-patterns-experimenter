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
package com.jaspersoft.ireport.designer.fonts;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import java.util.*;
import javax.swing.JCheckBox;
/**
 *
 * @author  Administrator
 */
public class FontPathDialog extends javax.swing.JDialog {

    private int dialogResult = javax.swing.JOptionPane.CANCEL_OPTION;
    private boolean modifiedPath = false;
    /** Creates new form ClassPathDialog */
    
    public FontPathDialog(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    public FontPathDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initAll();
    }
    
    public void initAll()
    {
        initComponents();
        applyI18n();    
        jList1.setModel(new DefaultListModel());
        setLocationRelativeTo(null);


        javax.swing.KeyStroke escape =  javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0, false);
        javax.swing.Action escapeAction = new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                jButtonCancelActionPerformed(e);
            }
        };

        getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, I18n.getString("Global.Pane.Escape"));
        getRootPane().getActionMap().put(I18n.getString("Global.Pane.Escape"), escapeAction);


        jList1.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mousePressed(MouseEvent e)
                {
                   modifiedPath = true;
                }
             });

        //to make the default button ...
        this.getRootPane().setDefaultButton(this.jButtonSave);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabelClasspath = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new com.jaspersoft.ireport.designer.fonts.CheckBoxList();
        jPanel1 = new javax.swing.JPanel();
        jButtonSelectAll = new javax.swing.JButton();
        jButtonDeselectAll = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(I18n.getString("FontPathDialog.Title.fontPaths")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabelClasspath.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelClasspath.setText(I18n.getString("FontPathDialog.Label.FontsPath")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jLabelClasspath, gridBagConstraints);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(359, 260));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jPanel1.setMinimumSize(new java.awt.Dimension(120, 10));
        jPanel1.setPreferredSize(new java.awt.Dimension(120, 10));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonSelectAll.setText(I18n.getString("Global.Button.SelectAll")); // NOI18N
        jButtonSelectAll.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonSelectAll.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonSelectAll.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(jButtonSelectAll, gridBagConstraints);

        jButtonDeselectAll.setText(I18n.getString("Global.Button.DeselectAll")); // NOI18N
        jButtonDeselectAll.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonDeselectAll.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonDeselectAll.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonDeselectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeselectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(jButtonDeselectAll, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);

        jButtonSave.setText(I18n.getString("FontPathDialog.Button.SaveFontsPath")); // NOI18N
        jButtonSave.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonSave.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonSave.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(jButtonSave, gridBagConstraints);

        jButtonCancel.setText(I18n.getString("Global.Button.Cancel")); // NOI18N
        jButtonCancel.setMaximumSize(new java.awt.Dimension(200, 26));
        jButtonCancel.setMinimumSize(new java.awt.Dimension(90, 26));
        jButtonCancel.setPreferredSize(new java.awt.Dimension(120, 26));
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        jPanel1.add(jButtonCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDeselectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeselectAllActionPerformed
        DefaultListModel dlm = (DefaultListModel)jList1.getModel();

        for (int i=0; i<dlm.size(); ++i)
        {
            Object obj = dlm.getElementAt(i);
            if (obj instanceof JCheckBox)
            {
                JCheckBox checkbox = (JCheckBox)obj;
                checkbox.setSelected(false);
            }
        }
        jList1.updateUI();
    }//GEN-LAST:event_jButtonDeselectAllActionPerformed

    private void jButtonSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectAllActionPerformed

        DefaultListModel dlm = (DefaultListModel)jList1.getModel();

        for (int i=0; i<dlm.size(); ++i)
        {
            Object obj = dlm.getElementAt(i);
            if (obj instanceof JCheckBox)
            {
                JCheckBox checkbox = (JCheckBox)obj;
                checkbox.setSelected(true);
            }
        }
        jList1.updateUI();

    }//GEN-LAST:event_jButtonSelectAllActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        if (modifiedPath)
        {
            int ret = javax.swing.JOptionPane.showConfirmDialog(this, I18n.getString("FontPathDialog.Message.Confirm"));

            if (ret == javax.swing.JOptionPane.CANCEL_OPTION)
            {
                return;
            }

            jButtonSaveActionPerformed(null);
        }
        else
        {
            setVisible(false);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        //javax.swing.JOptionPane.showMessageDialog(this,"You have to restart iReport for the changes to take effect");

        setDialogResult(javax.swing.JOptionPane.OK_OPTION);
        setVisible(false);
    }//GEN-LAST:event_jButtonSaveActionPerformed

    @SuppressWarnings("unchecked")
    public void setFontspath(List<String> fontsPaths, List<String> cp)
    {
        @SuppressWarnings("unchecked")
        List<String> newcp = new ArrayList<String>();
        newcp.addAll(cp);
        
        List<String> cp_old = new ArrayList<String>();
        
        for (String s : fontsPaths) {
              if (!newcp.contains(s))
              {
                  newcp.add(s);
              }
        }

        Object[] allStrings = new Object[newcp.size()];
        allStrings = newcp.toArray(allStrings);

        Arrays.sort(allStrings);

        for (int i=0; i<allStrings.length; ++i)
        {
            String s = ""+allStrings[i];
            if (s.trim().length() == 0) continue;
            CheckBoxListEntry cble = new CheckBoxListEntry(s,fontsPaths.contains(s));

            if (!cp.contains(s) && !cp_old.contains(s))
            {
                cble.setRed(true);
            }

            ((DefaultListModel)jList1.getModel()).addElement( cble);
        }
    }


    @SuppressWarnings("unchecked")
    public List<String> getFontspath()
    {
         List<String> cp = new ArrayList<String>();
         java.util.List list = jList1.getCheckedItems();
         for (int i=0; i<list.size(); ++i )
         {
             CheckBoxListEntry cble = (CheckBoxListEntry)list.get( i );
             cp.add( cble.getValue() + "" );
         }

         return cp;
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonDeselectAll;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSelectAll;
    private javax.swing.JLabel jLabelClasspath;
    private com.jaspersoft.ireport.designer.fonts.CheckBoxList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public void applyI18n(){
                // Start autogenerated code ----------------------
                // End autogenerated code ----------------------
    }
}
