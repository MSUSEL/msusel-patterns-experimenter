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
package com.jaspersoft.ireport.designer.connection.gui;

import com.jaspersoft.ireport.locale.I18n;
import com.jaspersoft.ireport.designer.IReportConnection;
import com.jaspersoft.ireport.designer.connection.JDBCConnection;
import com.jaspersoft.ireport.designer.IReportConnectionEditor;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author  gtoffoli
 */
public class JDBCConnectionEditor extends javax.swing.JPanel implements IReportConnectionEditor {
    
    private IReportConnection iReportConnection = null;

    public static JDBCDriverDefinition[] definitions = new JDBCDriverDefinition[]{
         	new JDBCDriverDefinition("Cloudscape","COM.cloudscape.JDBCDriver","jdbc:cloudscape:/{1}"),
                new JDBCDriverDefinition("IBM DB2","COM.ibm.db2.jdbc.app.DB2Driver","jdbc:db2:{0}/{1}"),
		new JDBCDriverDefinition("inetdae7","com.inet.tds.TdsDriver","jdbc:inetdae7:{0}:1433/{1}"),
                new JDBCDriverDefinition("Informix","com.informix.jdbc.IfxDriver","jdbc:informix-sqli://{0}:informixserver={1}"),
		new JDBCDriverDefinition("Ingres","com.ingres.jdbc.IngresDriver","jdbc:ingres://{0}:II7/{1}"),
                new JDBCDriverDefinition("HSQLDB (file)","org.hsqldb.jdbcDriver","jdbc:hsqldb:[PATH_TO_DB_FILES]/{1}"),
		new JDBCDriverDefinition("HSQLDB (server)","org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://{0}"),
                new JDBCDriverDefinition("JDBC-ODBC Bridge","sun.jdbc.odbc.JdbcOdbcDriver","jdbc:odbc:{1}","DSNAME"),
		new JDBCDriverDefinition("JDBC-ODBC Bridge","com.ms.jdbc.odbc.JdbcOdbcDriver","jdbc:odbc:{1}","DSNAME"),
		new JDBCDriverDefinition("MS SQLServer","com.internetcds.jdbc.tds.Driver","jdbc:freetds:sqlserver://{0}/{1}"),
		new JDBCDriverDefinition("MS SQLServer (2000)","com.microsoft.jdbc.sqlserver.SQLServerDriver","jdbc:microsoft:sqlserver://{0}:1433;DatabaseName={1}"),
		new JDBCDriverDefinition("MS SQLServer (2005)","com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://{0}:1433;databaseName={1}"),
		new JDBCDriverDefinition("MS SQLServer","net.sourceforge.jtds.jdbc.Driver","jdbc:jtds:sqlserver://{0}/{1}"),
                new JDBCDriverDefinition("MS SQLServer","com.merant.datadirect.jdbc.sqlserver.SQLServerDriver","jdbc:sqlserver://{0}:1433/{1}"),
                new JDBCDriverDefinition("MySQL","org.gjt.mm.mysql.Driver","jdbc:mysql://{0}/{1}"),
		new JDBCDriverDefinition("MySQL","com.mysql.jdbc.Driver","jdbc:mysql://{0}/{1}"),
		new JDBCDriverDefinition("Oracle","oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@{0}:1521:{1}"),
		new JDBCDriverDefinition("PostgreSQL","org.postgresql.Driver","jdbc:postgresql://{0}:5432/{1}"),
                new JDBCDriverDefinition("Sybase","com.sybase.jdbc2.jdbc.SybDriver","jdbc:sybase:Tds:{0}:2638/{1}"),
                new JDBCDriverDefinition("Vertica","com.vertica.Driver","jdbc:vertica://{0}:5433/{1}")
		};

    /** Creates new form JDBCConnectionEditor */
    public JDBCConnectionEditor() {
        initComponents();
        //applyI18n();

        
        //Arrays.sort(definitions);

         jComboBoxJDBCDriver.setRenderer(new JDBCDriverListRenderer());
         jComboBoxJDBCDriver.setModel(new DefaultComboBoxModel(definitions));
         // Select the first MySQL driver...

         jComboBoxJDBCDriver.setSelectedIndex(15);
         jLabel1.setText(I18n.getString("XMLADataSourceConnectionEditor.Label.Warning"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelJDBC = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxJDBCDriver = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldJDBCUrl = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldDBName = new javax.swing.JTextField();
        jTextFieldServerAddress = new javax.swing.JTextField();
        jButtonWizard = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldPassword = new javax.swing.JPasswordField();
        jTextFieldUsername = new javax.swing.JTextField();
        jCheckBoxSavePassword = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanelJDBC.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText(I18n.getString("JDBCConnectionEditor.Label.JDBC_Driver")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jLabel2, gridBagConstraints);

        jComboBoxJDBCDriver.setEditable(true);
        jComboBoxJDBCDriver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxJDBCDriverActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelJDBC.add(jComboBoxJDBCDriver, gridBagConstraints);

        jLabel3.setText(I18n.getString("JDBCConnectionEditor.Label.JDBC_URL")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelJDBC.add(jTextFieldJDBCUrl, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("JDBC URL Wizard"));
        jPanel2.setMinimumSize(new java.awt.Dimension(179, 70));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel14.setText(I18n.getString("JDBCConnectionEditor.Label.Server_Address")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel2.add(jLabel14, gridBagConstraints);

        jLabel5.setText(I18n.getString("JDBCConnectionEditor.Label.Database")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel2.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel2.add(jTextFieldDBName, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanel2.add(jTextFieldServerAddress, gridBagConstraints);

        jButtonWizard.setText(I18n.getString("Global.Button.Wizard")); // NOI18N
        jButtonWizard.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButtonWizard.setMaximumSize(new java.awt.Dimension(60, 23));
        jButtonWizard.setMinimumSize(new java.awt.Dimension(60, 23));
        jButtonWizard.setPreferredSize(new java.awt.Dimension(60, 23));
        jButtonWizard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWizardActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 2, 0);
        jPanel2.add(jButtonWizard, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelJDBC.add(jPanel2, gridBagConstraints);

        jLabel6.setText(I18n.getString("Global.Label.Username")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jLabel6, gridBagConstraints);

        jLabel7.setText(I18n.getString("Global.Label.Password")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jLabel7, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jTextFieldPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 0);
        jPanelJDBC.add(jTextFieldUsername, gridBagConstraints);

        jCheckBoxSavePassword.setText(I18n.getString("Global.CheckBox.Save_password")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 6, 0, 6);
        jPanelJDBC.add(jCheckBoxSavePassword, gridBagConstraints);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/designer/connection/gui/warning.png"))); // NOI18N
        jLabel1.setText("Attention! Passwords are saved in clear text.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        jPanelJDBC.add(jLabel1, gridBagConstraints);

        add(jPanelJDBC, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxJDBCDriverActionPerformed(java.awt.event.ActionEvent evt) {                                                    
        // Run the wizard to populate Connection URL
        jButtonWizardActionPerformed(null);
    }
    
    private void jButtonWizardActionPerformed(java.awt.event.ActionEvent evt) {                                              
          
        if (jComboBoxJDBCDriver.getSelectedIndex() < 0) return;

        if (jComboBoxJDBCDriver.getSelectedItem() instanceof JDBCDriverDefinition)
        {
            String server = jTextFieldServerAddress.getText().trim();
            if( server.length()==0 ) {
                server = "localhost";
            }
            String databaseName = jTextFieldDBName.getText().trim();
            jTextFieldJDBCUrl.setText( ((JDBCDriverDefinition)jComboBoxJDBCDriver.getSelectedItem()).getUrl(server, databaseName) );
        }
        
    }
    
    public void setIReportConnection(IReportConnection c) {
        this.iReportConnection = c;
        
        if (iReportConnection instanceof JDBCConnection)
        {
            JDBCConnection con = (JDBCConnection)iReportConnection;
            // Find if the is a good definition..

            boolean found = false;
            for (int i=0; i<definitions.length; ++i)
            {
                if (definitions[i].getDriverName().equals(con.getJDBCDriver()))
                {
                    this.jComboBoxJDBCDriver.setSelectedItem(definitions[i]);
                    found = true;
                }
            }
            if (!found)
            {
                this.jComboBoxJDBCDriver.setSelectedItem(con.getJDBCDriver());
            }
            
            this.jTextFieldJDBCUrl.setText( con.getUrl());
            this.jTextFieldServerAddress.setText( con.getServerAddress() );
            this.jTextFieldDBName.setText( con.getDatabase() );
            this.jTextFieldUsername.setText( con.getUsername());
            if (con.isSavePassword())
                this.jTextFieldPassword.setText( con.getPassword());
            else 
                this.jTextFieldPassword.setText( "");
            this.jCheckBoxSavePassword.setSelected( con.isSavePassword());
        }
        
    }

    public IReportConnection getIReportConnection() {
        
        IReportConnection irConn = new JDBCConnection();
        //irConn.setName( this.jTextFieldName.getText().trim() );
        ((JDBCConnection)irConn).setServerAddress( this.jTextFieldServerAddress.getText().trim() );
        ((JDBCConnection)irConn).setDatabase( this.jTextFieldDBName.getText().trim() );
        ((JDBCConnection)irConn).setUsername( this.jTextFieldUsername.getText().trim() );
        if (jCheckBoxSavePassword.isSelected())
            ((JDBCConnection)irConn).setPassword( new String( this.jTextFieldPassword.getPassword() ) );
        else
            ((JDBCConnection)irConn).setPassword("");
        ((JDBCConnection)irConn).setSavePassword( jCheckBoxSavePassword.isSelected() );
        
        Object obj = jComboBoxJDBCDriver.getSelectedItem();
        String driver = obj+"";
        if (obj instanceof JDBCDriverDefinition)
        {
            driver = ((JDBCDriverDefinition)obj).getDriverName();
        }
        
        ((JDBCConnection)irConn).setJDBCDriver( driver.trim() );
        if (driver.trim().length() == 0 ||
            driver.indexOf(" ") >= 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("JDBCConnectionEditor.Message.JDBCDriver"), //"messages.connectionDialog.jdbc.invalidDriver"
                    I18n.getString("JDBCConnectionEditor.Message.InvalidDriver"), //"messages.connectionDialog.jdbc.invalidDriverCaption"
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            return null;
        }

        if (this.jTextFieldJDBCUrl.getText().trim().length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    I18n.getString("JDBCConnectionEditor.Message.JDBCurl"), //"messages.connectionDialog.jdbc.invalidUrl"
                    I18n.getString("JDBCConnectionEditor.Message.InvalidUrl"), //"messages.connectionDialog.jdbc.invalidUrlCaption"
                    javax.swing.JOptionPane.WARNING_MESSAGE );
            return null;
        }
        ((JDBCConnection)irConn).setUrl(this.jTextFieldJDBCUrl.getText().trim());
            
        iReportConnection = irConn;
        return iReportConnection;
    }
    
    /*
    public void applyI18n(){
                // Start autogenerated code ----------------------
                jCheckBoxSavePassword.setText(I18n.getString("connectionDialog.checkBoxSavePassword","Save password"));
                jButtonWizard.setText(I18n.getString("connectionDialog.buttonWizard","Wizard"));
                jLabel14.setText(I18n.getString("connectionDialog.label14","Server Address"));
                jLabel2.setText(I18n.getString("connectionDialog.label2","JDBC Driver"));
                jLabel3.setText(I18n.getString("connectionDialog.label3","JDBC URL"));
                jLabel5.setText(I18n.getString("connectionDialog.label5","Database"));
                jLabel6.setText(I18n.getString("connectionDialog.label6","Username"));
                jLabel7.setText(I18n.getString("connectionDialog.label7","Password"));
                
                ((javax.swing.border.TitledBorder)jPanel2.getBorder()).setTitle( it.businesslogic.ireport.util.I18n.getString("connectionDialog.panelBorder.jdbcUrlWizard","JDBC URL Wizard") );
                
                jLabel1.setText( "<html>" + I18n.getString("connectionDialog.textArea1","ATTENTION! Passwords are stored in clear text. If you dont specify a password now, iReport will ask you for one only when required and will not save it."));
                
    }
    */
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonWizard;
    private javax.swing.JCheckBox jCheckBoxSavePassword;
    private javax.swing.JComboBox jComboBoxJDBCDriver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelJDBC;
    private javax.swing.JTextField jTextFieldDBName;
    private javax.swing.JTextField jTextFieldJDBCUrl;
    private javax.swing.JPasswordField jTextFieldPassword;
    private javax.swing.JTextField jTextFieldServerAddress;
    private javax.swing.JTextField jTextFieldUsername;
    // End of variables declaration//GEN-END:variables
    
}
