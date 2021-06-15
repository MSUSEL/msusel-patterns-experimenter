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
package com.jaspersoft.ireport.jasperserver.ui;

import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author  gtoffoli
 */
public class ServerDialog extends javax.swing.JDialog {
    
    private JServer jServer = null;
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    
    /** Creates new form ServerDialog */
    public ServerDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("JasperServer Plugin");
        this.setLocationRelativeTo(null);
        
        jTextFieldURL.setText(JasperServerManager.getMainInstance().getBrandingProperties().getProperty("irplugin.server.url")  );
        if (IReportManager.getPreferences().getBoolean("proMode", false))
        {
            jTextFieldURL.setText(JasperServerManager.getMainInstance().getBrandingProperties().getProperty("irplugin.server.url_pro"));
        }


        jTextFieldServerName.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateButtons();
            }
            public void insertUpdate(DocumentEvent e) {
                updateButtons();
            }
            public void removeUpdate(DocumentEvent e) {
                updateButtons();
            }
        });
        
        jTextFieldUsername.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateButtons();
            }
            public void insertUpdate(DocumentEvent e) {
                updateButtons();
            }
            public void removeUpdate(DocumentEvent e) {
                updateButtons();
            }
        });

        jPanelPro.setVisible( IReportManager.getPreferences().getBoolean("proMode", false) );
        
        applyI18n();

        // check is the domains plugin is installed...
    }
    
    
    public void applyI18n()
    {
        jButtonCancel.setText( JasperServerManager.getString("serverDialog.buttonCancel","Cancel"));
        jButtonSave.setText( JasperServerManager.getString("serverDialog.buttonSave","Save"));
        
        jLabel1.setText( JasperServerManager.getString("serverDialog.title","JasperServer Access Configuration"));
        jLabel3.setText( JasperServerManager.getString("serverDialog.labelName","Name"));
        jLabel4.setText( JasperServerManager.getString("serverDialog.labelURL","JasperServer URL"));
        jLabel5.setText( JasperServerManager.getString("serverDialog.labelUsername","Username"));
        jLabel6.setText( JasperServerManager.getString("serverDialog.labelPassword","Password"));
        jLabelOrganization.setText( JasperServerManager.getString("serverDialog.labelOrganization","Organization"));
        
        ((TitledBorder)jPanel2.getBorder()).setTitle( JasperServerManager.getString("serverDialog.account","Account") );
        ((TitledBorder)jPanel3.getBorder()).setTitle( JasperServerManager.getString("serverDialog.serverInformation","Server information") );
        
    }
    
    private void updateButtons()
    {
        if (jTextFieldServerName.getText().trim().length() > 0 &&
            jTextFieldUsername.getText().trim().length() > 0)
        {
            jButtonSave.setEnabled(true);
            //jButtonTest.setEnabled(true);
        }
        else
        {
            jButtonSave.setEnabled(false);
            //jButtonTest.setEnabled(false);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldServerName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldURL = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanelPro = new javax.swing.JPanel();
        jLabelOrganization = new javax.swing.JLabel();
        jTextFieldOrganization = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel4 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMinimumSize(new java.awt.Dimension(10, 50));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 50));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/settings.png"))); // NOI18N
        jLabel1.setText("JasperServer Access Configuration");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator1.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jSeparator1, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Server information"));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel3.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanel3.add(jTextFieldServerName, gridBagConstraints);

        jLabel4.setText("JasperServer URL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel3.add(jLabel4, gridBagConstraints);

        jTextFieldURL.setText("http://localhost:8080/jasperserver/services/repository");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel3.add(jTextFieldURL, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Account"));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanelPro.setLayout(new java.awt.GridBagLayout());

        jLabelOrganization.setText("Organization");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanelPro.add(jLabelOrganization, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        jPanelPro.add(jTextFieldOrganization, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanelPro, gridBagConstraints);

        jLabel5.setText("Username");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel2.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jTextFieldUsername, gridBagConstraints);

        jLabel6.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel2.add(jLabel6, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jPasswordField, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jSeparator2, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 23));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jButtonSave.setText("Save");
        jButtonSave.setEnabled(false);
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel4.add(jButtonSave, gridBagConstraints);

        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        jPanel4.add(jButtonCancel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        getContentPane().add(jPanel4, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed

        this.setDialogResult(JOptionPane.CANCEL_OPTION);
        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        jServer = new JServer();
        jServer.setName(  jTextFieldServerName.getText() );
        jServer.setUrl( jTextFieldURL.getText());
        String username = jTextFieldUsername.getText();
        if (jTextFieldOrganization.getText().length() > 0)
        {
            username += "|" + jTextFieldOrganization.getText();
        }
        jServer.setUsername(username);
        jServer.setPassword( new String(jPasswordField.getPassword()));
        jServer.setLocale( Locale.getDefault().toString() );

        this.setDialogResult(JOptionPane.OK_OPTION);
        this.setVisible(false);
        this.dispose();
        
    }//GEN-LAST:event_jButtonSaveActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
   

    public JServer getJServer() {
        return jServer;
    }

    public void setJServer(JServer jServer) {
        
        this.jTextFieldServerName.setText(  jServer.getName());
        this.jTextFieldURL.setText(  jServer.getUrl());
        String username = jServer.getUsername();
        String organization = "";
        if (username.indexOf("|") > 0)
        {
            organization = username.substring(username.indexOf("|")+1);
            username = username.substring(0, username.indexOf("|"));
        }
        if (IReportManager.getPreferences().getBoolean("proMode", false))
        {
            this.jTextFieldUsername.setText( username );
            this.jTextFieldOrganization.setText( organization );
        }
        else
        {
            if (organization.length() > 0)
            {
                username = username + "|" + organization;
            }
            this.jTextFieldUsername.setText(username);
            this.jTextFieldOrganization.setText("");
        }
        this.jPasswordField.setText(  jServer.getPassword());
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelOrganization;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelPro;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextFieldOrganization;
    private javax.swing.JTextField jTextFieldServerName;
    private javax.swing.JTextField jTextFieldURL;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables
    
}
