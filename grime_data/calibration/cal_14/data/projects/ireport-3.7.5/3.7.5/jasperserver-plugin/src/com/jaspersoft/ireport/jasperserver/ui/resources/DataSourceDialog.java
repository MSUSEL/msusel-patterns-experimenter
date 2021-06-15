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
package com.jaspersoft.ireport.jasperserver.ui.resources;

import com.jaspersoft.ireport.jasperserver.ui.*;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.IReportManager;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import com.jaspersoft.ireport.designer.connection.JDBCNBConnection;
import com.jaspersoft.ireport.jasperserver.JServer;
import com.jaspersoft.ireport.jasperserver.JasperServerManager;
import com.jaspersoft.ireport.jasperserver.RepositoryFolder;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.util.List;
import javax.swing.JOptionPane;
import org.netbeans.api.db.explorer.DatabaseConnection;

/**
 *
 * @author  gtoffoli
 */
public class DataSourceDialog extends javax.swing.JDialog {
    
    private int dialogResult = JOptionPane.CANCEL_OPTION;
    
    private JServer server = null;
    private String parentFolder = null;
    private RepositoryFolder datasourceResource = null;
    
    private ResourceDescriptor newResourceDescriptor = null;
    
    private boolean doNotStore = false;
    
    /**
     * Creates new form DataSourceDialog
     */
    public DataSourceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.setLocationRelativeTo(null);
        javax.swing.event.DocumentListener changesListener = new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent evt) {
                updateSaveButton();
            }
        };
        
        this.jTextFieldLabel.getDocument().addDocumentListener(changesListener);
        this.jTextFieldName.getDocument().addDocumentListener(changesListener);
        this.jTextFieldDriver.getDocument().addDocumentListener(changesListener);
        this.jTextFieldURL.getDocument().addDocumentListener(changesListener);
        this.jTextFieldUsername.getDocument().addDocumentListener(changesListener);
        this.jTextFieldServiceName.getDocument().addDocumentListener(changesListener);
        this.jPasswordField.getDocument().addDocumentListener(changesListener);
        this.jTextFieldBeanName.getDocument().addDocumentListener(changesListener);
        
        applyI18n();
        
        jTextFieldName.requestFocusInWindow();
        
    }
    
    public void applyI18n()
    {
        jButtonClose.setText( JasperServerManager.getString("dataSourceDialog.buttonCancel","Cancel"));
        jButtonImportConnection.setText( JasperServerManager.getString("dataSourceDialog.buttonImportConnection","Import from iReport"));
        jButtonSave.setText( JasperServerManager.getString("dataSourceDialog.buttonSave","Save"));
        jLabel1.setText( JasperServerManager.getString("dataSourceDialog.title","Data Source"));
        jLabelBeanMethod.setText( JasperServerManager.getString("dataSourceDialog.labelBeanMethod","Bean Method"));
        jLabelBeanName.setText( JasperServerManager.getString("dataSourceDialog.labelBeanName","Bean Name"));
        jLabelDescription.setText( JasperServerManager.getString("dataSourceDialog.labelDescription","Description"));
        jLabelDriver.setText( JasperServerManager.getString("dataSourceDialog.labelDriver","Driver"));
        jLabelLabel.setText( JasperServerManager.getString("dataSourceDialog.labelLabel","Label"));
        jLabelName.setText( JasperServerManager.getString("dataSourceDialog.labelName","Name"));
        jLabelUriString.setText( JasperServerManager.getString("dataSourceDialog.labelParentFolder","Parent folder"));
        jLabelPassword.setText( JasperServerManager.getString("dataSourceDialog.labelPassword","Password"));
        jLabelServiceName.setText( JasperServerManager.getString("dataSourceDialog.labelServiceName","Service Name"));
        jLabelURL.setText( JasperServerManager.getString("dataSourceDialog.labelURL","URL"));
        jLabelUsername.setText( JasperServerManager.getString("dataSourceDialog.labelUsername","Username"));
        jRadioButtonBean.setText( JasperServerManager.getString("dataSourceDialog.radioBean","Bean Data Source"));
        jRadioButtonJDBC.setText( JasperServerManager.getString("dataSourceDialog.radioJDBC","JDBC Data Source"));
        jRadioButtonJNDI.setText( JasperServerManager.getString("dataSourceDialog.radioJNDI","JNDI Data Source"));
        jTabbedPane1.setTitleAt(0, JasperServerManager.getString("dataSourceDialog.tabGeneral","General") );
        jTabbedPane1.setTitleAt(1, JasperServerManager.getString("dataSourceDialog.tabDetails","Data Source details") );
    }
    
    private void updateSaveButton()
    {
        if (jTextFieldLabel.getText().length() > 0 &&
            jTextFieldName.getText().length() > 0)
        {
            boolean ok = false;
            if (jRadioButtonJDBC.isSelected() &&
                jTextFieldDriver.getText().length() > 0 &&   
                jTextFieldURL.getText().length() > 0 &&
                jTextFieldUsername.getText().length() > 0) 
            {
                ok = true;
            }
            else if (jRadioButtonJNDI.isSelected() && 
                     jTextFieldServiceName.getText().length() > 0)
            {
                ok = true;
            }
            else if (jRadioButtonBean.isSelected() && 
                     jTextFieldBeanName.getText().length() > 0)
            {
                ok = true;
            }
            
            jButtonSave.setEnabled(ok);
        }
        else
        {
            jButtonSave.setEnabled(false);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabelUriString = new javax.swing.JLabel();
        jTextFieldUriString = new javax.swing.JTextField();
        jLabelName = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelLabel = new javax.swing.JLabel();
        jTextFieldLabel = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPaneDescription = new javax.swing.JEditorPane();
        jLabelDescription = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jRadioButtonJDBC = new javax.swing.JRadioButton();
        jLabelDriver = new javax.swing.JLabel();
        jTextFieldDriver = new javax.swing.JTextField();
        jLabelURL = new javax.swing.JLabel();
        jTextFieldURL = new javax.swing.JTextField();
        jLabelUsername = new javax.swing.JLabel();
        jTextFieldUsername = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        jPasswordField = new javax.swing.JPasswordField();
        jButtonImportConnection = new javax.swing.JButton();
        jRadioButtonJNDI = new javax.swing.JRadioButton();
        jLabelServiceName = new javax.swing.JLabel();
        jTextFieldServiceName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jRadioButtonBean = new javax.swing.JRadioButton();
        jLabelBeanName = new javax.swing.JLabel();
        jTextFieldBeanName = new javax.swing.JTextField();
        jLabelBeanMethod = new javax.swing.JLabel();
        jTextFieldBeanMethod = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("DataSource");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 12));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/datasource_new.png"))); // NOI18N
        jLabel1.setText("Datasource");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanel1, gridBagConstraints);

        jSeparator1.setMinimumSize(new java.awt.Dimension(2, 2));
        jSeparator1.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jSeparator1, gridBagConstraints);

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 185));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelUriString.setText("Parent folder");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jLabelUriString, gridBagConstraints);

        jTextFieldUriString.setEditable(false);
        jTextFieldUriString.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextFieldUriString.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jTextFieldUriString, gridBagConstraints);

        jLabelName.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jLabelName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jTextFieldName, gridBagConstraints);

        jSeparator2.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 6, 4);
        jPanel2.add(jSeparator2, gridBagConstraints);

        jLabelLabel.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jLabelLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanel2.add(jTextFieldLabel, gridBagConstraints);

        jScrollPane1.setViewportView(jEditorPaneDescription);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        jLabelDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        jPanel2.add(jLabelDescription, gridBagConstraints);

        jTabbedPane1.addTab("General", jPanel2);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        buttonGroup1.add(jRadioButtonJDBC);
        jRadioButtonJDBC.setText("JDBC Data Source");
        jRadioButtonJDBC.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonJDBC.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonJDBC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonJDBCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 4, 4);
        jPanel3.add(jRadioButtonJDBC, gridBagConstraints);

        jLabelDriver.setText("Driver");
        jLabelDriver.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelDriver, gridBagConstraints);

        jTextFieldDriver.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jTextFieldDriver, gridBagConstraints);

        jLabelURL.setText("URL");
        jLabelURL.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelURL, gridBagConstraints);

        jTextFieldURL.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jTextFieldURL, gridBagConstraints);

        jLabelUsername.setText("Username");
        jLabelUsername.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelUsername, gridBagConstraints);

        jTextFieldUsername.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jTextFieldUsername, gridBagConstraints);

        jLabelPassword.setText("Password");
        jLabelPassword.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelPassword, gridBagConstraints);

        jPasswordField.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jPasswordField, gridBagConstraints);

        jButtonImportConnection.setText("Import from iReport");
        jButtonImportConnection.setEnabled(false);
        jButtonImportConnection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonImportConnectionActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 8);
        jPanel3.add(jButtonImportConnection, gridBagConstraints);

        buttonGroup1.add(jRadioButtonJNDI);
        jRadioButtonJNDI.setSelected(true);
        jRadioButtonJNDI.setText("JNDI Data Source");
        jRadioButtonJNDI.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonJNDI.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonJNDI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonJNDIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 8, 4, 4);
        jPanel3.add(jRadioButtonJNDI, gridBagConstraints);

        jLabelServiceName.setText("Service Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelServiceName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jTextFieldServiceName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel5, gridBagConstraints);

        buttonGroup1.add(jRadioButtonBean);
        jRadioButtonBean.setText("Bean Data Source");
        jRadioButtonBean.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButtonBean.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jRadioButtonBean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonJNDIActionPerformed1(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(16, 8, 4, 4);
        jPanel3.add(jRadioButtonBean, gridBagConstraints);

        jLabelBeanName.setText("Bean Name");
        jLabelBeanName.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelBeanName, gridBagConstraints);

        jTextFieldBeanName.setToolTipText("Name of configured bean");
        jTextFieldBeanName.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jTextFieldBeanName, gridBagConstraints);

        jLabelBeanMethod.setText("Bean Method");
        jLabelBeanMethod.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 4, 0);
        jPanel3.add(jLabelBeanMethod, gridBagConstraints);

        jTextFieldBeanMethod.setToolTipText("Name of method on configured bean (optional)");
        jTextFieldBeanMethod.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 8);
        jPanel3.add(jTextFieldBeanMethod, gridBagConstraints);

        jTabbedPane1.addTab("Datasource details", jPanel3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(10, 30));
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 30));
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        jPanel4.add(jButtonSave, gridBagConstraints);

        jButtonClose.setText("Cancel");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });
        jPanel4.add(jButtonClose, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        getContentPane().add(jPanel4, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButtonJNDIActionPerformed1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonJNDIActionPerformed1
        updateDatasourceType();
    }//GEN-LAST:event_jRadioButtonJNDIActionPerformed1

    private void jRadioButtonJDBCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonJDBCActionPerformed
        updateDatasourceType();
    }//GEN-LAST:event_jRadioButtonJDBCActionPerformed

    private void jButtonImportConnectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonImportConnectionActionPerformed

        List<IReportConnection> iReportConnections = IReportManager.getInstance().getConnections();
        
        java.util.Vector validConnections = new java.util.Vector();
        for (IReportConnection conn : iReportConnections)
        {
            if (conn instanceof JDBCConnection ||
                conn instanceof JDBCNBConnection)
            {
                validConnections.add(conn);
            }
        }
        if (validConnections.size() == 0)
        {
            JOptionPane.showMessageDialog(this,
                    JasperServerManager.getString("dataSourceDialog.message.noJDBCconfigured","No JDBC connections currently configured in iReport.")
                    );
            return;
        }
        IReportConnection[] connections = new IReportConnection[validConnections.size()];
        for (int i=0; i<connections.length; ++i)
        {
            connections[i] = (IReportConnection)(validConnections.elementAt(i));
        }
        
        IReportConnection selectedCon = (IReportConnection)JOptionPane.showInputDialog(this,
                JasperServerManager.getString("dataSourceDialog.message.selectJDBC","Select a JDBC datasource:"),
                JasperServerManager.getString("dataSourceDialog.message.import","Import..."),
                JOptionPane.QUESTION_MESSAGE,null,connections, connections[0]);
        if (selectedCon != null)
        {
            if (selectedCon instanceof JDBCConnection)
            {
                jTextFieldDriver.setText( ((JDBCConnection)selectedCon).getJDBCDriver() );
                jTextFieldURL.setText( ((JDBCConnection)selectedCon).getUrl() );
                jTextFieldUsername.setText( ((JDBCConnection)selectedCon).getUsername() );
                jPasswordField.setText( ((JDBCConnection)selectedCon).getPassword() );
            }
            else if (selectedCon instanceof JDBCNBConnection)
            {
                DatabaseConnection dbconn = ((JDBCNBConnection)selectedCon).getDatabaseConnectionObject();
                if (dbconn != null)
                {
                    jTextFieldDriver.setText( dbconn.getDriverClass() );
                    jTextFieldURL.setText( dbconn.getDatabaseURL() );
                    jTextFieldUsername.setText( dbconn.getUser());
                    jPasswordField.setText( dbconn.getPassword() );
                }
            }
        }
        
    }//GEN-LAST:event_jButtonImportConnectionActionPerformed

    private void jRadioButtonJNDIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonJNDIActionPerformed

        updateDatasourceType();
    }//GEN-LAST:event_jRadioButtonJNDIActionPerformed

    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed

        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        
        try {
            ResourceDescriptor rd = new ResourceDescriptor();

            ValidationUtils.validateName( jTextFieldName.getText() );
            ValidationUtils.validateLabel(jTextFieldLabel.getText() );
            ValidationUtils.validateDesc( jEditorPaneDescription.getText() );
                    
            rd.setDescription( jEditorPaneDescription.getText().trim() ); //getResource().getDescriptor().getDescription()
            rd.setName( jTextFieldName.getText()  );
            String uri = getParentFolder();
            if (!uri.endsWith("/")) uri = uri + "/";
            uri += jTextFieldName.getText();
            rd.setUriString( uri );
            rd.setLabel(jTextFieldLabel.getText().trim() ); //getResource().getDescriptor().getLabel()  );
            rd.setParentFolder( getParentFolder() );
            rd.setIsNew(datasourceResource == null);

            if (jRadioButtonJDBC.isSelected())
            {
                rd.setWsType( ResourceDescriptor.TYPE_DATASOURCE_JDBC );
                rd.setDriverClass( jTextFieldDriver.getText());
                rd.setConnectionUrl( jTextFieldURL.getText());
                rd.setUsername( jTextFieldUsername.getText());
                rd.setPassword( new String(jPasswordField.getPassword()));
            }
            else if (jRadioButtonJNDI.isSelected())
            {
                rd.setWsType( ResourceDescriptor.TYPE_DATASOURCE_JNDI );
                rd.setJndiName( jTextFieldServiceName.getText() );
            }
            else if (jRadioButtonBean.isSelected())
            {
                rd.setWsType( ResourceDescriptor.TYPE_DATASOURCE_BEAN);
                rd.setBeanName( jTextFieldBeanName.getText() );
                rd.setBeanMethod( jTextFieldBeanMethod.getText() );
            }


            if (!doNotStore)
            {
                newResourceDescriptor = getServer().getWSClient().addOrModifyResource(rd, null);
            }
            else newResourceDescriptor = rd;
            
            setDialogResult(JOptionPane.OK_OPTION);
            
            if (datasourceResource != null)
            {
                datasourceResource.setDescriptor(newResourceDescriptor);
            }
            
            this.setVisible(false);
            this.dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,JasperServerManager.getFormattedString("messages.error.3", "Error:\n {0}", new Object[] {ex.getMessage()}));
            ex.printStackTrace();
            return;
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed
    

    public int getDialogResult() {
        return dialogResult; 
    }     
                
    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }
    
    
    public JServer getServer() {
        return server;
    }

    public void setServer(JServer server) {
        this.server = server;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
        this.jTextFieldUriString.setText(parentFolder);
    }

    public ResourceDescriptor getNewResourceDescriptor() {
        return newResourceDescriptor;
    }
    
    /**
     * Call this method to modify the specified resource...
     */
    public void setResource(RepositoryFolder resource)
    {
        this.datasourceResource = resource;
        if (resource != null)
        {
           setResource( resource.getDescriptor());
           jTextFieldName.setEditable(false);
           jTextFieldName.setOpaque(false);
        }


    }
        
    /**
     * Call this method to modify the specified resource...
     */
    public void setResource(ResourceDescriptor descriptor)
    {
        if (descriptor == null) return;
        
        setTitle( JasperServerManager.getFormattedString("properties.title", "{0} - Properties", new Object[]{descriptor.getName()}));

        jTextFieldName.setText( descriptor.getName());
        jTextFieldLabel.setText( descriptor.getLabel());
        jEditorPaneDescription.setText( descriptor.getDescription());
                
        if (descriptor.getWsType().equals( ResourceDescriptor.TYPE_DATASOURCE_JDBC))
        {
            jRadioButtonJDBC.setEnabled(true);
            jRadioButtonJDBC.setSelected(true);
            jRadioButtonJNDI.setEnabled(false);
            jRadioButtonJNDI.setSelected(false);
            jRadioButtonBean.setSelected(false);
            jRadioButtonBean.setEnabled(false);
            
            updateDatasourceType();          
            
            jTextFieldDriver.setText( descriptor.getDriverClass());
            jTextFieldURL.setText( descriptor.getConnectionUrl());
            jTextFieldUsername.setText( descriptor.getUsername());
            jPasswordField.setText( descriptor.getPassword());
        }
        else if (descriptor.getWsType().equals( ResourceDescriptor.TYPE_DATASOURCE_JNDI))
        {
            jRadioButtonJDBC.setEnabled(false);
            jRadioButtonJDBC.setSelected(false);
            jRadioButtonJNDI.setEnabled(true);
            jRadioButtonJNDI.setSelected(true);
            jRadioButtonBean.setSelected(false);
            jRadioButtonBean.setEnabled(false);
            updateDatasourceType();    
        
            jTextFieldServiceName.setText( descriptor.getJndiName());
        }
        else if (descriptor.getWsType().equals( ResourceDescriptor.TYPE_DATASOURCE_BEAN))
        {
            jRadioButtonJDBC.setEnabled(false);
            jRadioButtonJDBC.setSelected(false);
            jRadioButtonJNDI.setEnabled(false);
            jRadioButtonJNDI.setSelected(false);
            jRadioButtonBean.setSelected(true);
            jRadioButtonBean.setEnabled(true);
            updateDatasourceType();    
        
            jTextFieldBeanName.setText( descriptor.getBeanName());
            jTextFieldBeanMethod.setText( (descriptor.getBeanMethod() != null && descriptor.getBeanMethod().trim().length() > 0) ? 
                        descriptor.getBeanMethod() : "");
        }
        
        jButtonSave.setText("Save");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonImportConnection;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JEditorPane jEditorPaneDescription;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelBeanMethod;
    private javax.swing.JLabel jLabelBeanName;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelDriver;
    private javax.swing.JLabel jLabelLabel;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelServiceName;
    private javax.swing.JLabel jLabelURL;
    private javax.swing.JLabel jLabelUriString;
    private javax.swing.JLabel jLabelUsername;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JRadioButton jRadioButtonBean;
    private javax.swing.JRadioButton jRadioButtonJDBC;
    private javax.swing.JRadioButton jRadioButtonJNDI;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldBeanMethod;
    private javax.swing.JTextField jTextFieldBeanName;
    private javax.swing.JTextField jTextFieldDriver;
    private javax.swing.JTextField jTextFieldLabel;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldServiceName;
    private javax.swing.JTextField jTextFieldURL;
    private javax.swing.JTextField jTextFieldUriString;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables
    
    
    public void updateDatasourceType()
    {
        boolean b = jRadioButtonJDBC.isSelected();
        
        jTextFieldDriver.setEnabled( b ); 
        jTextFieldURL.setEnabled( b );         
        jTextFieldUsername.setEnabled( b );   
        jPasswordField.setEnabled( b ); 
        jButtonImportConnection.setEnabled( b ); 
        jLabelDriver.setEnabled( b ); 
        jLabelURL.setEnabled( b );         
        jLabelUsername.setEnabled( b );   
        jLabelPassword.setEnabled( b ); 
        
        b = jRadioButtonJNDI.isSelected();
        
        jTextFieldServiceName.setEnabled( b ); 
        jLabelServiceName.setEnabled( b ); 
        
        b = jRadioButtonBean.isSelected();
        jTextFieldBeanName.setEnabled( b ); 
        jLabelBeanName.setEnabled( b ); 
        jTextFieldBeanMethod.setEnabled( b ); 
        jLabelBeanMethod.setEnabled( b ); 
        
        updateSaveButton();
    }

    public boolean isDoNotStore() {
        return doNotStore;
    }

    public void setDoNotStore(boolean doNotStore) {
        this.doNotStore = doNotStore;
    }
}
