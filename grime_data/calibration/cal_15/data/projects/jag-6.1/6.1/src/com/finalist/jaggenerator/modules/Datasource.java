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
package com.finalist.jaggenerator.modules;

import com.finalist.jag.util.TemplateString;
import com.finalist.jaggenerator.Database;
import com.finalist.jaggenerator.DatabaseManager;
import com.finalist.jaggenerator.JagGenerator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;

/**
 * The 'JagBean' for handling datasource configuration.
 *
 * @author  Michael O'Connor - Finalist IT Group
 */
public class Datasource extends DefaultMutableTreeNode implements JagBean {
   private static final String[] URL_TEMPLATES = new String[] {
      "",
      "jdbc:oracle:thin:@<host>:<port>:<sid>",
      "jdbc:mysql://<host>/<database>",
      "jdbc:postgresql://<host>:<port>/<database>",
      "jdbc:postgresql:net",
      "jdbc:hsqldb:mem:.",
      "jdbc:hsqldb:hsql://<host>",
      "jdbc:hsqldb:<database>",
      "jdbc:hsqldb:hsql://<host>",
      "jdbc:sybase:Tds:<host>:<port>/<database>",
      "jdbc:db2://<host>:<port>/<database>",
      "jdbc:db2:<database>",
      "jdbc:microsoft:sqlserver://<host>:<port>;DatabaseName=<database>",
      "jdbc:idb:<propertyFile>",
      "jdbc:mckoi://<host>/",
      "jdbc:Cache://<host>:<port>/<namespace>",
      "jdbc:informix-sqli://<host>:<port>/<database>:informixserver=<dbservername>",
      "jdbc:pointbase:server://<host>/<database>"
   };

   private boolean constructing = true;


   /** Creates new form BeanForm */
   public Datasource() {
      init();
      constructing = false;
   }

   public Datasource(Element el) {
      init();

      NodeList nl = el.getElementsByTagName("module-data");
      for (int i = 0; i < nl.getLength(); i++) {
         Element child = (Element) nl.item(i);
         String attName = child.getAttribute("name");
         String value = null;
         if (child.getFirstChild() == null)
            value = null;
         else
            value = child.getFirstChild().getNodeValue();
         if (value != null) {
            if (attName.equalsIgnoreCase("jndi-name")) {
               jndiText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("jdbc-url")) {
               jdbcURLCombo.setSelectedItem(value);
               continue;
            }
            if (attName.equalsIgnoreCase("user-name")) {
               userNameText.setText(value);
               continue;
            }
            if (attName.equalsIgnoreCase("password")) {
               passwordText.setText(value);
               continue;
            }

            if (attName.equalsIgnoreCase("mapping")) {
               for (int j = 0; j < mappingCombo.getItemCount(); j++) {
                  String item = ((Database) mappingCombo.getItemAt(j)).getDbName();
                  if (value.equalsIgnoreCase(item)) {
                     mappingCombo.setSelectedIndex(j);
                     break;
                  }
               }
            }
         }
      }
      constructing = false;
   }


   public String toString() {
      return "Datasource";
   }

   public JPanel getPanel() {
      return panel;
   }

   public void getXML(Element el) throws ParserConfigurationException {

      Document doc = el.getOwnerDocument();
      Element module = doc.createElement("module");
      module.setAttribute("name", "datasource");

      Element jndi = doc.createElement("module-data");
      jndi.setAttribute("name", "jndi-name");
      if (jndiText.getText() != null) {
        jndi.appendChild(doc.createTextNode(jndiText.getText()));
      }
      module.appendChild(jndi);

      Element mapping = doc.createElement("module-data");
      mapping.setAttribute("name", "mapping");

      mapping.appendChild(doc.createTextNode(mappingCombo.getModel().getSelectedItem().toString()));
      module.appendChild(mapping);

      Element jdbcUrl = doc.createElement("module-data");
      jdbcUrl.setAttribute("name", "jdbc-url");
      jdbcUrl.appendChild(doc.createTextNode(jdbcURLCombo.getEditor().getItem().toString()));
      module.appendChild(jdbcUrl);

      Element userName = doc.createElement("module-data");
      userName.setAttribute("name", "user-name");
      if (userNameText.getText() != null) {
        userName.appendChild(doc.createTextNode(userNameText.getText()));
      }
      module.appendChild(userName);

      Element password = doc.createElement("module-data");
      password.setAttribute("name", "password");
      if (passwordText.getText() != null) {
        password.appendChild(doc.createTextNode(passwordText.getText()));
      }
      module.appendChild(password);

      el.appendChild(module);
   }

   public TemplateString getJndiName() {
      return new TemplateString(jndiText.getText());
   }

   public void setMapping(String text) {
      mappingCombo.setSelectedItem(text);
      for (int j = 0; j < mappingCombo.getItemCount(); j++) {
         String item = ((Database) mappingCombo.getItemAt(j)).getDbName();
         if (text.equalsIgnoreCase(item)) {
            mappingCombo.setSelectedIndex(j);
            break;
         }
      }
   }

   public void setJdbcUrl(String jdbcUrlText) {
      jdbcURLCombo.setSelectedItem(jdbcUrlText);
   }

   public void setJndi(String jndiText) {
      this.jndiText.setText(jndiText);
   }

   public void setPassword(String passwordText) {
      this.passwordText.setText(passwordText);
   }

   public void setUserName(String userNameText) {
      this.userNameText.setText(userNameText);
   }

   /**
    * Gets the database currently selected for the application.
    * @return the Database bean.
    */
   public Database getDatabase() {
      return (Database) mappingCombo.getModel().getSelectedItem();
   }

   /**
    * Convenience method: gets the database's appserver type mapping as a TemplateString.
    * @return
    */
   public TemplateString getTypeMapping() {
      return new TemplateString(getDatabase().getTypeMapping());
   }

   public TemplateString getJdbcUrl() {
      return new TemplateString((String) jdbcURLCombo.getEditor().getItem());
   }

   public TemplateString getUserName() {
      return new TemplateString(userNameText.getText());
   }

   public TemplateString getPassword() {
      return new TemplateString(passwordText.getText());
   }

   public String getRefName() {
      return "datasource";
   }


   private void init() {
      initComponents();
      Database[] dbs = DatabaseManager.getInstance().getSupportedDatabases();
      setSupportedDatabases(dbs);
      for (int i = 0; i < URL_TEMPLATES.length; i++) {
         jdbcURLCombo.addItem(URL_TEMPLATES[i]);
      }

   }

   /**
    * Resets the dropdown list of supported databases.
    * @param dbs
    */
   public void setSupportedDatabases(Database[] dbs) {
      mappingCombo.removeAllItems();
      for (int i = 0; i < dbs.length; i++) {
         mappingCombo.addItem(dbs[i]);
      }
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    private void initComponents() {//GEN-BEGIN:initComponents
        panel = new javax.swing.JPanel();
        jndiLabel = new javax.swing.JLabel();
        mappingLabel = new javax.swing.JLabel();
        jndiText = new javax.swing.JTextField();
        mappingCombo = new javax.swing.JComboBox();
        jdbcUrlLabel = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        userNameText = new javax.swing.JTextField();
        passwordText = new javax.swing.JTextField();
        driverManagerButton = new javax.swing.JButton();
        jdbcURLCombo = new javax.swing.JComboBox();

        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jndiLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jndiLabel.setText("JNDI name: ");
        panel.add(jndiLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 90, -1));

        mappingLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        mappingLabel.setText("Database type:");
        panel.add(mappingLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));

        jndiText.setText("jdbc/");
        jndiText.setToolTipText("JNDI name for the datasource that can be used to lookup from the application");
        jndiText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jndiTextFocusLost(evt);
            }
        });

        panel.add(jndiText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 260, -1));

        mappingCombo.setToolTipText("Select a database type. To add a new type, use the Database Driver Manager");
        mappingCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mappingComboActionPerformed(evt);
            }
        });

        panel.add(mappingCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 260, 20));

        jdbcUrlLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jdbcUrlLabel.setText("JDBC url: ");
        panel.add(jdbcUrlLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, -1));

        userNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        userNameLabel.setText("User name:");
        panel.add(userNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, -1));

        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        passwordLabel.setText("Password: ");
        panel.add(passwordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 90, -1));

        userNameText.setToolTipText("Set a user name to connect to the database");
        userNameText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                userNameTextFocusLost(evt);
            }
        });

        panel.add(userNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 260, 20));

        passwordText.setToolTipText("Set the password to connect to the database");
        passwordText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordTextFocusLost(evt);
            }
        });

        panel.add(passwordText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 260, -1));

        driverManagerButton.setText("Database Driver Manager..");
        driverManagerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                driverManagerButtonActionPerformed(evt);
            }
        });

        panel.add(driverManagerButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, -1, -1));

        jdbcURLCombo.setEditable(true);
        jdbcURLCombo.setToolTipText("Configure the JDBC url");
        jdbcURLCombo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jdbcURLComboFocusLost(evt);
            }
        });

        panel.add(jdbcURLCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 260, 20));

    }//GEN-END:initComponents

    private void jdbcURLComboFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jdbcURLComboFocusLost
        // Add your handling code here:
    }//GEN-LAST:event_jdbcURLComboFocusLost

    private void driverManagerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_driverManagerButtonActionPerformed
       DatabaseManagerFrame.getInstance().show();
    }//GEN-LAST:event_driverManagerButtonActionPerformed

   private void passwordTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_passwordTextFocusLost
      JagGenerator.stateChanged(false);
   }//GEN-LAST:event_passwordTextFocusLost

   private void userNameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userNameTextFocusLost
      JagGenerator.stateChanged(false);
   }//GEN-LAST:event_userNameTextFocusLost

   private void mappingComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mappingComboActionPerformed
      JagGenerator.stateChanged(false);
      if (!constructing) {
         JagGenerator.normaliseSQLTypesWithChosenDatabase();
      }
   }//GEN-LAST:event_mappingComboActionPerformed

   private void jndiTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jndiTextFocusLost
      JagGenerator.stateChanged(false);
   }//GEN-LAST:event_jndiTextFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton driverManagerButton;
    public javax.swing.JComboBox jdbcURLCombo;
    private javax.swing.JLabel jdbcUrlLabel;
    private javax.swing.JLabel jndiLabel;
    public javax.swing.JTextField jndiText;
    public javax.swing.JComboBox mappingCombo;
    private javax.swing.JLabel mappingLabel;
    public javax.swing.JPanel panel;
    private javax.swing.JLabel passwordLabel;
    public javax.swing.JTextField passwordText;
    private javax.swing.JLabel userNameLabel;
    public javax.swing.JTextField userNameText;
    // End of variables declaration//GEN-END:variables
}

