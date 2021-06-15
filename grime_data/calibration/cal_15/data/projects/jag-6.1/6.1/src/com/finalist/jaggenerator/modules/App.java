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

import java.text.SimpleDateFormat;

import com.finalist.jag.util.TemplateString;
import com.finalist.jaggenerator.JagGenerator;
import com.finalist.jaggenerator.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author hillie
 */
public class App extends DefaultMutableTreeNode implements JagBean {
    private static final String XMLTAG_MODULE_DATA = "module-data";

    // Check if date is a supported calendar date.
    public String isCalendarDate() {
        if (!"".equals(getCalendarDateFormat())) {
            return "true";
        }
        return "false";
    }

    public String getCalendarDateFormat() {
        if ("dd/MM/yyyy".equals(getDateFormat()))
        {
            return "%d/%m/%Y";
        }
        if ("dd-MM-yyyy".equals(getDateFormat()))
        {
            return "%d-%m-%Y";
        }
        if ("yyyy/MM/dd".equals(getDateFormat()))
        {
            return "%Y/%m/%d";
        }
        if ("yyyy-MM-dd".equals(getDateFormat()))
        {
            return "%Y-%m-%d";
        }

        if ("MM/dd/yyyy".equals(getDateFormat()))
        {
            return "%m/%d/%Y";
        }
        if ("MM-dd-yyyy".equals(getDateFormat()))
        {
            return "%m-%d-%Y";
        }
        // Unsupported date format.
        return "";
    }

    // Check if time is a supported calendar date.
    public String isCalendarTime() {
        if (!"".equals(getCalendarTimeFormat())) {
            return "true";
        }
        return "false";
    }

    public String getCalendarTimeFormat() {
        if ("dd/MM/yyyy HH:mm:ss".equals(getTimestampFormat()))
        {
            return "%d/%m/%Y %H:%M:%S";
        }
        if ("dd-MM-yyyy HH:mm:ss".equals(getTimestampFormat()))
        {
            return "%d-%m-%Y %H:%M:%S";
        }
        if ("yyyy/MM/dd HH:mm:ss".equals(getTimestampFormat()))
        {
            return "%Y/%m/%d %H:%M:%S";
        }
        if ("yyyy-MM-dd HH:mm:ss".equals(getTimestampFormat()))
        {
            return "%Y-%m-%d %H:%M:%S";
        }

        if ("MM/dd/yyyy HH:mm:ss".equals(getTimestampFormat()))
        {
            return "%m/%d/%Y %H:%M:%S";
        }
        if ("MM-dd-yyyy HH:mm:ss".equals(getTimestampFormat()))
        {
            return "%m-%d-%Y %H:%M:%S";
        }

        // Unsupported date format.
        return "";
    }


   /**
    * Get the current date formatted using the date format.
    *
    * @return formatted current date.
    */
   public String getCurrentDate() {
      SimpleDateFormat format = new SimpleDateFormat(getDateFormat());
      return format.format(new java.util.Date());
   }
    /**
     * Creates new form BeanForm
     */
    public App() {
        initComponents();
        nameText.requestFocus();
        rootPackageText.setText("com.finalist");
    }

    public App(Element el) {
        initComponents();
        NodeList nl = el.getElementsByTagName(XMLTAG_MODULE_DATA);
        for (int i = 0; i < nl.getLength(); i++) {
            Element child = (Element) nl.item(i);
            String attName = child.getAttribute("name");
            String value = null;
            if (child.getFirstChild() == null)
                value = null;
            else
                value = child.getFirstChild().getNodeValue();
            if (value != null) {
                if (attName.equalsIgnoreCase("name")) {
                    nameText.setText(value);
                    continue;
                }
                if (attName.equalsIgnoreCase("version")) {
                    versionText.setText(value);
                    continue;
                }
                if (attName.equalsIgnoreCase("description")) {
                    descriptionText.setText(value);
                    continue;
                }
                if (attName.equalsIgnoreCase("root-package")) {
                    rootPackageText.setText(value);
                    continue;
                }
                if (attName.equalsIgnoreCase("log-framework")) {
                    loggingFrameworkCombo.setSelectedItem(value);
                    continue;
                }
                if (attName.equalsIgnoreCase("date-format")) {
                    dateFormat.setText(value);
                    continue;
                }
                if (attName.equalsIgnoreCase("timestamp-format")) {
                    timestampFormat.setText(value);
                    continue;
                }
            }
        }
        nameText.requestFocus();
    }


    public void setName(String text) {
        this.nameText.setText(text);
    }

    public TemplateString getName() {
        return new TemplateString(nameText.getText());
    }

    public String getVersion() {
        return versionText.getText();
    }

    public void setVersion(String text) {
        this.versionText.setText(text);
    }

    public String getDescription() {
        return descriptionText.getText();
    }

    public void setDescription(String text) {
        descriptionText.setText(text);
    }

    public String getRootPackage() {
        return rootPackageText.getText();
    }

    public void setRootPackage(String text) {
        this.rootPackageText.setText(text);
    }

    public String getRootPath() {
        return rootPackageText.getText().replace('.', '/');
    }

    public String getLogFramework() {
        return (String) loggingFrameworkCombo.getSelectedItem();
    }

    public void setLogFramework(String text) {
        this.loggingFrameworkCombo.setSelectedItem(text);
    }

    public String getTimestampFormat() {
        return timestampFormat.getText();
    }

    public void setTimestampFormat(String format) {
        timestampFormat.setText(format);
    }

    public String getDateFormat() {
        return dateFormat.getText();
    }

    public void setDateFormat(String format) {
        dateFormat.setText(format);
    }

    public String toString() {
        return "Application settings";
    }

    public JPanel getPanel() {
        return panel;
    }

    public void getXML(Element el) throws ParserConfigurationException {
        Document doc = el.getOwnerDocument();
        Element module = doc.createElement("module");
        module.setAttribute("name", "app");

        Element name = doc.createElement(XMLTAG_MODULE_DATA);
        name.setAttribute("name", "name");
        if (nameText.getText() != null) {
            name.appendChild(doc.createTextNode(nameText.getText()));
        }
        module.appendChild(name);

        Element version = doc.createElement(XMLTAG_MODULE_DATA);
        version.setAttribute("name", "version");
        if (versionText.getText() != null) {
            version.appendChild(doc.createTextNode(versionText.getText()));
        }
        module.appendChild(version);

        Element description = doc.createElement(XMLTAG_MODULE_DATA);
        description.setAttribute("name", "description");
        if (descriptionText.getText() != null) {
            description.appendChild(doc.createTextNode(descriptionText.getText()));
        }
        module.appendChild(description);

        Element rootPackage = doc.createElement(XMLTAG_MODULE_DATA);
        rootPackage.setAttribute("name", "root-package");
        if (rootPackageText.getText() != null) {
            rootPackage.appendChild(doc.createTextNode(rootPackageText.getText()));
        }
        module.appendChild(rootPackage);

        Element loggingFramework = doc.createElement(XMLTAG_MODULE_DATA);
        loggingFramework.setAttribute("name", "log-framework");
        if (loggingFrameworkCombo.getSelectedItem() != null) {
            loggingFramework.appendChild(doc.createTextNode((String) loggingFrameworkCombo.getSelectedItem()));
        }
        module.appendChild(loggingFramework);

        Element dateFormat = doc.createElement(XMLTAG_MODULE_DATA);
        dateFormat.setAttribute("name", "date-format");

        if (getDateFormat() != null) {
            dateFormat.appendChild(doc.createTextNode(getDateFormat()));
        }
        module.appendChild(dateFormat);

        Element tsFormat = doc.createElement(XMLTAG_MODULE_DATA);
        tsFormat.setAttribute("name", "timestamp-format");
        if (getTimestampFormat() != null) {
            tsFormat.appendChild(doc.createTextNode(getTimestampFormat()));
        }
        module.appendChild(tsFormat);

        el.appendChild(module);
    }

    public String getRefName() {
        return "app";
    }


    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        panel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        desciptionLabel = new javax.swing.JLabel();
        rootPackageLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        versionText = new javax.swing.JTextField();
        descriptionText = new javax.swing.JTextField();
        rootPackageText = new javax.swing.JTextField();
        loggingFrameworkLabel = new javax.swing.JLabel();
        loggingFrameworkCombo = new javax.swing.JComboBox();
        dateFormatLabel = new javax.swing.JLabel();
        dateFormat = new javax.swing.JTextField();
        timestampFormatLabel = new javax.swing.JLabel();
        timestampFormat = new javax.swing.JTextField();

        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameLabel.setText("Application Name: ");
        panel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 110, -1));

        versionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        versionLabel.setText("Version: ");
        panel.add(versionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 110, -1));

        desciptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        desciptionLabel.setText("Description: ");
        panel.add(desciptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 110, -1));

        rootPackageLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        rootPackageLabel.setText("Root-package: ");
        panel.add(rootPackageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 110, -1));

        nameText.setToolTipText("Name should be lowercase and characters only!");
        nameText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameTextFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextFocusLost(evt);
            }
        });

        panel.add(nameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 260, -1));

        versionText.setText("1.0");
        versionText.setToolTipText("Version number of the application");
        versionText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                versionTextFocusLost(evt);
            }
        });

        panel.add(versionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 260, -1));

        descriptionText.setToolTipText("Description is used for class names, so make sure it starts with a capital and only contains characters");
        descriptionText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                descriptionTextFocusLost(evt);
            }
        });

        panel.add(descriptionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 70, 260, -1));

        rootPackageText.setText("com.finalist.");
        rootPackageText.setToolTipText("Root package name for your application");
        rootPackageText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rootPackageTextFocusLost(evt);
            }
        });

        panel.add(rootPackageText, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 260, -1));

        loggingFrameworkLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        loggingFrameworkLabel.setText("Logging:");
        panel.add(loggingFrameworkLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 110, -1));

        loggingFrameworkCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"log4j", "jdklogging"}));
        loggingFrameworkCombo.setToolTipText("Select logging method");
        loggingFrameworkCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loggingFrameworkComboActionPerformed(evt);
            }
        });

        panel.add(loggingFrameworkCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 260, -1));

        dateFormatLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dateFormatLabel.setText("Date format:");
        panel.add(dateFormatLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 110, -1));

        dateFormat.setText("dd/MM/yyyy");
        dateFormat.setToolTipText("Date format used for displaying dates");
        panel.add(dateFormat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 260, -1));

        timestampFormatLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        timestampFormatLabel.setText("Timestamp format:");
        panel.add(timestampFormatLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 110, -1));

        timestampFormat.setText("dd/MM/yyyy HH:mm:ss");
        timestampFormat.setToolTipText("Timestamp format used for rendering timestamps");
        panel.add(timestampFormat, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, 260, -1));

    }//GEN-END:initComponents

    private void nameTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextFocusGained

    private void descriptionTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionTextFocusLost
        // Make sure we only use characters and all in lowercase..
        String name = descriptionText.getText();
        if ((name == null) || (name.length() == 0))
            return;
        String formattedName = Utils.formatLowerAndUpperCase(name);
        descriptionText.setText(formattedName);
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_descriptionTextFocusLost

    private void versionTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_versionTextFocusLost
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_versionTextFocusLost

    private void loggingFrameworkComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loggingFrameworkComboActionPerformed
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_loggingFrameworkComboActionPerformed

    private void rootPackageTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rootPackageTextFocusLost
        Root root = (Root) getParent();
        root.setRootPackage(rootPackageText.getText());
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_rootPackageTextFocusLost


    private void nameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFocusLost
        // Make sure we only use characters and all in lowercase..
        String name = nameText.getText();
        if ((name == null) || (name.length() == 0))
            return;
        String formattedName = Utils.formatLowercase(name);
        nameText.setText(formattedName);
        JagGenerator.stateChanged(false);
    }//GEN-LAST:event_nameTextFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextField dateFormat;
    private javax.swing.JLabel dateFormatLabel;
    private javax.swing.JLabel desciptionLabel;
    public javax.swing.JTextField descriptionText;
    public javax.swing.JComboBox loggingFrameworkCombo;
    private javax.swing.JLabel loggingFrameworkLabel;
    private javax.swing.JLabel nameLabel;
    public javax.swing.JTextField nameText;
    public javax.swing.JPanel panel;
    private javax.swing.JLabel rootPackageLabel;
    public javax.swing.JTextField rootPackageText;
    public javax.swing.JTextField timestampFormat;
    private javax.swing.JLabel timestampFormatLabel;
    private javax.swing.JLabel versionLabel;
    private javax.swing.JTextField versionText;
    // End of variables declaration//GEN-END:variables
}

