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
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jaspersoft.ireport.designer.standalone.actions;

import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.WizardDescriptor;
import org.openide.util.NbBundle;

public final class ImportSettingsVisualPanel1PlatformSelection extends JPanel {

    ImportSettingsWizardPanel1PlatformSelection wizard = null;
    private boolean init = false;

    /** Creates new form ImportSettingsVisualPanel1 */
    public ImportSettingsVisualPanel1PlatformSelection(ImportSettingsWizardPanel1PlatformSelection wizard) {
        initComponents();

        this.wizard = wizard;
        jListPlatforms.setModel(new DefaultListModel());

        jTextFieldDirectory.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
               notifyChange();
            }

            public void removeUpdate(DocumentEvent e) {
                notifyChange();
            }

            public void changedUpdate(DocumentEvent e) {
                notifyChange();
            }
        });
    }

    public void notifyChange()
    {
        if (wizard != null)
        {
            wizard.fireChangeEvent();
        }
    }

    @Override
    public String getName() {
        return org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.name");
    }


    public void validateForm()
    {
        if (jRadioButtonVersion.isSelected())
        {
            if (jListPlatforms.getSelectedValue() == null)
            {
                throw new IllegalArgumentException(org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.error.selectValidPlatform"));
            }
        }
        else if (jRadioButtonUserDir.isSelected())
        {
            String s = jTextFieldDirectory.getText();
            try {
                File f = new File(s);
                if (!ImportSettingsUtilities.isValidConfigurationDirectory(f))
                {
                    throw new IllegalArgumentException(org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.error.invalidFile"));
                }

            } catch (Exception ex)
            {
                throw new IllegalArgumentException(NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.error.invalidFile"));
            }
        }

        
    }

    public void updateVersions()
    {
        ((DefaultListModel)jListPlatforms.getModel()).removeAllElements();

        String[] versions = ImportSettingsUtilities.getAvailableVersions();

        for (String version : versions)
        {
            ((DefaultListModel)jListPlatforms.getModel()).addElement(version);
        }
        jListPlatforms.updateUI();
    }

    public void readSettings(Object settings) {

        updateVersions();
        String selectedVersion = (String)((WizardDescriptor) settings).getProperty("version");
        if (selectedVersion  != null)
        {
            jListPlatforms.setSelectedValue(selectedVersion, true);
        }

        String selectedPath = (String)((WizardDescriptor) settings).getProperty("path");
        if (selectedPath  != null)
        {
            jTextFieldDirectory.setText(selectedPath);
        }

        String selectedOption = (String)((WizardDescriptor) settings).getProperty("selectedOption");
        boolean b = selectedOption == null || selectedOption.equals("0");

        jRadioButtonVersion.setSelected(b);
        jRadioButtonUserDir.setSelected(!b);
    }

    public void storeSettings(Object settings) {

        if (jListPlatforms.getSelectedValue() != null)
        {
            ((WizardDescriptor) settings).putProperty("version", jListPlatforms.getSelectedValue());
        }
        else
        {
            ((WizardDescriptor) settings).putProperty("version", null);
        }

        ((WizardDescriptor) settings).putProperty("path",jTextFieldDirectory.getText());

        ((WizardDescriptor) settings).putProperty("selectedOption", jRadioButtonVersion.isSelected() ? "0" : "1");

    }

    private void updateControls()
    {
        jListPlatforms.setEnabled(jRadioButtonVersion.isSelected());
        jTextFieldDirectory.setEnabled(!jRadioButtonVersion.isSelected());
        jButtonBrowse.setEnabled(!jRadioButtonVersion.isSelected());
        notifyChange();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListPlatforms = new javax.swing.JList();
        jTextFieldDirectory = new javax.swing.JTextField();
        jButtonBrowse = new javax.swing.JButton();
        jRadioButtonVersion = new javax.swing.JRadioButton();
        jRadioButtonUserDir = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jListPlatforms.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListPlatformsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListPlatforms);

        jTextFieldDirectory.setText(org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.jTextFieldDirectory.text")); // NOI18N
        jTextFieldDirectory.setEnabled(false);

        org.openide.awt.Mnemonics.setLocalizedText(jButtonBrowse, org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.jButtonBrowse.text")); // NOI18N
        jButtonBrowse.setEnabled(false);
        jButtonBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBrowseActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonVersion);
        jRadioButtonVersion.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonVersion, org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.jRadioButtonVersion.text")); // NOI18N
        jRadioButtonVersion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonVersionActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButtonUserDir);
        org.openide.awt.Mnemonics.setLocalizedText(jRadioButtonUserDir, org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.jRadioButtonUserDir.text")); // NOI18N
        jRadioButtonUserDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonUserDirActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.jLabel2.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(21, 21, 21)
                        .add(jTextFieldDirectory, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonBrowse))
                    .add(jRadioButtonUserDir)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(15, 15, 15)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                    .add(jRadioButtonVersion))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jRadioButtonVersion)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(jRadioButtonUserDir)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextFieldDirectory, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonBrowse))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jListPlatformsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListPlatformsValueChanged
        notifyChange();
    }//GEN-LAST:event_jListPlatformsValueChanged

    private void jRadioButtonUserDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonUserDirActionPerformed
        updateControls();
    }//GEN-LAST:event_jRadioButtonUserDirActionPerformed

    private void jRadioButtonVersionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonVersionActionPerformed
        updateControls();
    }//GEN-LAST:event_jRadioButtonVersionActionPerformed

    private void jButtonBrowseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBrowseActionPerformed


        String fileName = null;

        if (jTextFieldDirectory.getText() != null)
        {
            File f = new File(jTextFieldDirectory.getText());
            if (f.exists() && f.isDirectory())
            {
                fileName = f.getPath();
            }
        }

        if (fileName == null)
        {
            fileName = System.getProperty("netbeans.home"); //NOI18N
        }
            
        
        javax.swing.JFileChooser jfc = new javax.swing.JFileChooser(fileName);

        jfc.setDialogTitle(NbBundle.getMessage(ImportSettingsVisualPanel1PlatformSelection.class, "ImportSettingsVisualPanel1PlatformSelection.JFileChooser.title"));//NOI18N

        jfc.setAcceptAllFileFilterUsed(true);
        jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY  );
        
        jfc.setMultiSelectionEnabled(false);

        jfc.setDialogType( javax.swing.JFileChooser.OPEN_DIALOG);
        if  (jfc.showOpenDialog( this) == javax.swing.JOptionPane.OK_OPTION) {
            java.io.File file = jfc.getSelectedFile();
            jTextFieldDirectory.setText( file.getPath() );
            notifyChange();
        }


    }//GEN-LAST:event_jButtonBrowseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonBrowse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jListPlatforms;
    private javax.swing.JRadioButton jRadioButtonUserDir;
    private javax.swing.JRadioButton jRadioButtonVersion;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextFieldDirectory;
    // End of variables declaration//GEN-END:variables
}

