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

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.finalist.jaggenerator.JagGenerator;
import com.finalist.jaggenerator.template.Template;
import com.finalist.jaggenerator.template.TemplateConfigException;
import com.finalist.jaggenerator.template.TemplateConfigPanel;

/**
 * @author hillie
 */
public class Config extends DefaultMutableTreeNode implements JagBean, ListSelectionListener
 {
   private DefaultListModel listModel = new DefaultListModel();
   private static final File DEFAULT_TEMPLATE = new File("../templates/java5_2_tier");
   private Template template;
   public Template selectedTemplate = null;
   private TemplateConfigPanel templatePanel;
   private static final String XMLTAG_CONFIG_PARAM = "config-param";
   private static final String NAME_ATTRIBUTE = "name";
   private static final String VALUE_ATTRIBUTE = "value";


   /**
    * Creates new form Config
    */
   public Config() {
      initComponents();
      setTemplate(DEFAULT_TEMPLATE);
      templateList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
      templateList.setModel(listModel);
      templateList.addListSelectionListener(this);
   }


   public Config(Element el) {
      initComponents();
      templateList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
      templateList.setModel(listModel);
      templateList.addListSelectionListener(this);
      NodeList nl = el.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
         if (nl.item(i) instanceof Element) {
            Element child = (Element) nl.item(i);
            String nodeName = child.getNodeName();
            if (nodeName.equalsIgnoreCase("author")) {
               Node node = child.getFirstChild();
               if (node != null) {
                  authorText.setText(node.getNodeValue());
               }
               continue;
            }
            if (nodeName.equalsIgnoreCase("version")) {
               Node node = child.getFirstChild();
               if (node != null) {
                  versionText.setText(node.getNodeValue());
               }
               continue;
            }
            if (nodeName.equalsIgnoreCase("company")) {
               Node node = child.getFirstChild();
               if (node != null) {
                  companyText.setText(node.getNodeValue());
               }
               continue;
            }
            if (nodeName.equalsIgnoreCase("templates")) {
               NodeList templates = child.getElementsByTagName("template-root");
               for (int j = 0; j < templates.getLength(); j++) {
                  Node templateRootText = templates.item(j).getFirstChild();
                  setTemplate(new File(templateRootText.getNodeValue()));
               }

               NodeList params = child.getElementsByTagName(XMLTAG_CONFIG_PARAM);
               for (int j = 0; j < params.getLength(); j++) {
                  Node paramNode = params.item(j);
                  String paramName = paramNode.getAttributes().getNamedItem(NAME_ATTRIBUTE).getNodeValue();
                  String paramValue = paramNode.getAttributes().getNamedItem(VALUE_ATTRIBUTE).getNodeValue();
                  Map components = templatePanel.getConfigComponents();
                  JComponent component = (JComponent) components.get(paramName);
                  if (component == null) {
                     JagGenerator
                        .logToConsole("Application file contains an unrecognised template config-param: " + paramName);
                  }
                  else {
                     if (component instanceof JTextField) {
                        ((JTextField) component).setText(paramValue);
                     }
                     else if (component instanceof JCheckBox) {
                        ((JCheckBox) component).setSelected(new Boolean(paramValue).booleanValue());
                     }
                     else if (component instanceof JComboBox) {
                        ((JComboBox) component).setSelectedItem(paramValue);
                     }
                  }
               }
            }
         }
      }
   }


   public void setAuthor(String text) {
      this.authorText.setText(text);
   }


   public void setVersion(String text) {
      this.versionText.setText(text);
   }


   public void setCompany(String text) {
      this.companyText.setText(text);
   }


   /**
    * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
    * content of this method is always regenerated by the Form Editor.
    */
   private void initComponents() {
      panel = new javax.swing.JPanel();
      authorLabel = new javax.swing.JLabel();
      versionLabel = new javax.swing.JLabel();
      companyLabel = new javax.swing.JLabel();
      rootPackageLabel = new javax.swing.JLabel();
      authorText = new javax.swing.JTextField();
      versionText = new javax.swing.JTextField();
      companyText = new javax.swing.JTextField();
      editButton = new javax.swing.JButton();
      scrollPane = new javax.swing.JScrollPane();
      templateList = new javax.swing.JList();
      templateSettingsPanel = new javax.swing.JPanel();
      templateSettingsScrollPane = new javax.swing.JScrollPane();

      panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

      authorLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
      authorLabel.setText("Author: ");
      panel.add(authorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 90, -1));

      versionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
      versionLabel.setText("Version: ");
      panel.add(versionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));

      companyLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
      companyLabel.setText("Company: ");
      panel.add(companyLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, -1));

      rootPackageLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
      rootPackageLabel.setText("Template: ");
      panel.add(rootPackageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 90, -1));

      authorText.addFocusListener(new java.awt.event.FocusAdapter() {
         public void focusLost(java.awt.event.FocusEvent evt) {
            authorTextFocusLost(evt);
         }
      });

      panel.add(authorText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 320, -1));

      versionText.setText("1.0");
      versionText.addFocusListener(new java.awt.event.FocusAdapter() {
         public void focusLost(java.awt.event.FocusEvent evt) {
            versionTextFocusLost(evt);
         }
      });

      panel.add(versionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 320, -1));

      companyText.setText("Finalist IT Group");
      companyText.addFocusListener(new java.awt.event.FocusAdapter() {
         public void focusLost(java.awt.event.FocusEvent evt) {
            companyTextFocusLost(evt);
         }
      });

      panel.add(companyText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 320, -1));

      editButton.setText("Select generation template");
      editButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
      editButton.setMaximumSize(new java.awt.Dimension(400, 26));
      editButton.setMinimumSize(new java.awt.Dimension(400, 26));
      editButton.setPreferredSize(new java.awt.Dimension(400, 26));
      editButton.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
            editButtonActionPerformed(evt);
         }
      });

      panel.add(editButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 190, 20));

      templateList.setBorder(new javax.swing.border.EtchedBorder());
      scrollPane.setViewportView(templateList);

      panel.add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 320, 60));

      templateSettingsPanel.setLayout(new java.awt.BorderLayout());

      templateSettingsPanel.setBorder(new javax.swing.border.TitledBorder("Template settings:"));

      templateSettingsPanel.add(templateSettingsScrollPane, java.awt.BorderLayout.CENTER);

      panel.add(templateSettingsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 400, 250));

   }


   private void companyTextFocusLost(java.awt.event.FocusEvent evt) {
      JagGenerator.stateChanged(false);
   }


   private void versionTextFocusLost(java.awt.event.FocusEvent evt) {
      JagGenerator.stateChanged(false);
   }


   private void authorTextFocusLost(java.awt.event.FocusEvent evt) {
      JagGenerator.stateChanged(false);
   }


   private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {
      int fileChooserStatus;

      String path = ((Template) listModel.get(0)).getTemplateDir().getAbsolutePath();
      JFileChooser fileChooser = new JFileChooser(path);
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fileChooser.setDialogTitle("Select a generation template (directory)..");
      fileChooserStatus = fileChooser.showDialog(null, "Select");
      if (fileChooserStatus == JFileChooser.APPROVE_OPTION) {
         File file = fileChooser.getSelectedFile();
         setTemplate(file);
      }
      JagGenerator.stateChanged(false);
   }

// Handler for list selection changes
   public void valueChanged( ListSelectionEvent event )
   {
      // See if this is a listbox selection and the
      // event stream has settled
     if( event.getSource() == templateList
                 && !event.getValueIsAdjusting() )
     {
        // Get the current selection and place it in the
        // edit field
        Template template = (Template)templateList.getSelectedValue();
        if( template != null ) {
           selectedTemplate = template;
           setTemplate(selectedTemplate.getTemplateDir());
        }
     }
   }

   private void setTemplate(File path) {
      try {
         listModel.clear();
         File parent = path.getParentFile();
         File[] fileList = parent.listFiles();
         int index = 0;
         for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
               // Now check if there is a template.xml file in the dir.
               File templateFile = new File(fileList[i] + File.separator + "template.xml");
               if (templateFile.exists()) {
                  template = new Template(fileList[i]);
                  if (fileList[i].equals(path)) {
                     selectedTemplate = template;
                  }
                  listModel.add(index, template);
                  index++;
               }
            }
         }
         if (selectedTemplate == null) {
            System.out.println("No templates found!");
            return;
         }
         templatePanel = new TemplateConfigPanel(selectedTemplate.getConfigParams(), selectedTemplate.getName());
         // templatePanel.setBorder(new javax.swing.border.TitledBorder("Template settings: " + selectedTemplate.getName()));
         TitledBorder border = (TitledBorder) templateSettingsPanel.getBorder();
         // border.setTitle("Template settings: " + selectedTemplate.getName());
         templateSettingsScrollPane.setViewportView(templatePanel);

      }
      catch (TemplateConfigException tce) {
         JOptionPane.showMessageDialog(JagGenerator.jagGenerator,
                                       tce.getMessage(), "Invalid template!", javax.swing.JOptionPane.ERROR_MESSAGE);
      }
   }


   public String toString() {
      return "Configuration";
   }


   public JPanel getPanel() {
      return panel;
   }


   public void getXML(Element el) throws ParserConfigurationException {
      Document doc = el.getOwnerDocument();
      Element config = doc.createElement("config");

      Element author = doc.createElement("author");
      if (authorText.getText() != null) {
         author.appendChild(doc.createTextNode(authorText.getText()));
      }
      config.appendChild(author);

      Element version = doc.createElement("version");
      if (versionText.getText() != null) {
         version.appendChild(doc.createTextNode(versionText.getText()));
      }
      config.appendChild(version);

      Element company = doc.createElement("company");
      if (companyText.getText() != null) {
         company.appendChild(doc.createTextNode(companyText.getText()));
      }
      config.appendChild(company);

      Element templates = doc.createElement("templates");
      Enumeration children = listModel.elements();
      while (children.hasMoreElements()) {
         String templatePath = ".";
         Element templateRoot = doc.createElement("template-root");

         if (selectedTemplate != null) {
            templatePath = selectedTemplate.getTemplateDir().getPath();
         } else {
            templatePath = ((Template) children.nextElement()).getTemplateDir().getPath();
         }
         templatePath = templatePath.replace('\\', '/');
         // Make sure that paths is stored os independent
         templateRoot.appendChild(doc.createTextNode(templatePath));
         templates.appendChild(templateRoot);
         break;
      }
      Map configSettings = getTemplateSettings();
      Iterator i = configSettings.entrySet().iterator();
      while (i.hasNext()) {
         Map.Entry entry = (Map.Entry) i.next();
         Element param = doc.createElement(XMLTAG_CONFIG_PARAM);
         param.setAttribute(NAME_ATTRIBUTE, (String) entry.getKey());
         param.setAttribute(VALUE_ATTRIBUTE, (String) entry.getValue());
         templates.appendChild(param);
      }

      config.appendChild(templates);

      el.appendChild(config);
   }


   /**
    * Gets the chosen application generaton template.
    * @return
    */
   public Template getTemplate() {
      return template;
   }


   public String getRefName() {
      return null;
   }


   /**
    * check if the Container-managed relations checkbox was checked.
    * @return
    */
   public Boolean useRelations() {
      String templateValue = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_USE_RELATIONS);
      if (templateValue == null || "false".equalsIgnoreCase(templateValue)) {
         return new Boolean(false);
      }
      else {
         return new Boolean(true);
      }
   }


   /**
    * check if the useMock checkbox was checked to generate a mock implementation.
    * @return
    */
   public Boolean useMock() {
      String templateValue = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_USE_MOCK);
      if (templateValue == null || "false".equalsIgnoreCase(templateValue)) {
         return new Boolean(false);
      }
      else {
         return new Boolean(true);
      }
   }


   /**
    * check if the useJava5 checkbox was checked to generate java5 support.
    * @return
    */
   public Boolean useJava5() {
      String templateValue = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_USE_JAVA5);
      if (templateValue == null || "false".equalsIgnoreCase(templateValue)) {
         return new Boolean(false);
      }
      else {
         return new Boolean(true);
      }
   }


   /**
    * check if the useWebService checkbox was checked to generate a webservice.
    * @return true if webservice has been enabled.
    */
   public Boolean useWebService() {
      String templateValue = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_USE_WEB_SERVICE);
      if (templateValue == null || "false".equalsIgnoreCase(templateValue)) {
         return new Boolean(false);
      }
      else {
         return new Boolean(true);
      }
   }


   /**
    * check if the useSecurity checkbox was checked to generate security.
    * @return true if security was enabled.
    */
   public Boolean useSecurity() {
      String templateValue = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_USE_SECURITY);
      if (templateValue == null || "false".equalsIgnoreCase(templateValue)) {
         return new Boolean(false);
      }
      else {
         return new Boolean(true);
      }
   }


   /**
    * check if the useSecurity checkbox was checked to generate security.
    * @return true if security was enabled.
    */
   public Boolean useRss() {
      return check("rssEnabled");
   }


   /**
    * Generic method for checking if a checkbox was enabled.
    * @return true if webservice has been enabled.
    */
   public Boolean check(String checkBoxField) {
      String templateValue = (String) getTemplateSettings().get(checkBoxField);
      if (templateValue == null || "false".equalsIgnoreCase(templateValue)) {
         return new Boolean(false);
      }
      else {
         return new Boolean(true);
      }
   }


   /**
    * Gets the configuration settings as set by the user in the GUI.
    * @return a Map of (String) configuration parameter id --> (String) value.
    */
   public Map getTemplateSettings() {
      String value = null;
      HashMap settings = new HashMap();
      Map componentsMap = templatePanel.getConfigComponents();
      Iterator components = componentsMap.entrySet().iterator();

      while (components.hasNext()) {
         Map.Entry entry = (Map.Entry) components.next();
         JComponent component = (JComponent) entry.getValue();
         if (component instanceof JTextField) {
            value = ((JTextField) component).getText();
         }
         else if (component instanceof JCheckBox) {
            value = "" + ((JCheckBox) component).isSelected();
         }
         else if (component instanceof JComboBox) {
            value = "" + ((JComboBox) component).getSelectedItem();
         }
         settings.put(entry.getKey(), value);
      }
      return settings;
   }


   /**
    * Set the template settings.
    */
   public void setTemplateSettings(Map templateSettings) {

      Map componentsMap = templatePanel.getConfigComponents();
      Iterator components = componentsMap.entrySet().iterator();

      while (components.hasNext()) {
         Map.Entry entry = (Map.Entry) components.next();
         JComponent component = (JComponent) entry.getValue();
         String name = component.getName();
         String value = (String) templateSettings.get(name);
         if (value != null) {
            if (component instanceof JTextField) {
               ((JTextField) component).setText(value);
            }
            else if (component instanceof JCheckBox) {
               if ("true".equalsIgnoreCase(value)) {
                  ((JCheckBox) component).setSelected(true);
               }
               else {
                  ((JCheckBox) component).setSelected(false);
               }
            }
            else if (component instanceof JComboBox) {
               ((JComboBox) component).setSelectedItem(value);
            }
         }
      }

   }


   /**
    * Helper method to determine if the selected appserver matches the passed string. A match is made in the selected
    * appserver equals or starts with the passed value ignoring cases. This method can be used to ignore version
    * numbers. So if you want to know that a JBoss appserver was selected, just match with "jboss".
    * @param value
    * @return Boolean true if there is a match.
    */
   public Boolean matchAppserver(String value) {
      String selectedAppserver = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_APPLICATION_SERVER);
      selectedAppserver = selectedAppserver.toLowerCase();
      value = value.toLowerCase();
      if (selectedAppserver == null || "".equals(selectedAppserver) || value == null || "".equals(value)) {
         return new Boolean(false);
      }
      if (selectedAppserver.equals(value) || selectedAppserver.startsWith(value)) {
         return new Boolean(true);
      }
      else {
         return new Boolean(false);
      }
   }


   /**
    * Helper method to determine if the selected business tier matches the passed string. A match is made in the
    * selected appserver equals or starts with the passed value ignoring cases. This method can be used to ignore
    * version numbers.
    * @param value
    * @return Boolean true if there is a match.
    */
   public Boolean matchBusinessTier(String value) {
      String selectedBusinessTier = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_BUSINESS_TIER);
      selectedBusinessTier = selectedBusinessTier.toLowerCase();
      value = value.toLowerCase();
      if (selectedBusinessTier == null || "".equals(selectedBusinessTier) || value == null || "".equals(value)) {
         return new Boolean(false);
      }
      if (selectedBusinessTier.equals(value) || selectedBusinessTier.startsWith(value)) {
         return new Boolean(true);
      }
      else {
         return new Boolean(false);
      }
   }


   /**
    * Helper method to determine if the selected service tier matches the passed string. A match is made if the selected
    * service tier equals or starts with the passed value ignoring cases. This method can be used to ignore version
    * numbers.
    * @param value
    * @return Boolean true if there is a match.
    */
   public Boolean matchServiceTier(String value) {
      String selectedServiceTier = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_SERVICE_TIER);
      selectedServiceTier = selectedServiceTier.toLowerCase();
      value = value.toLowerCase();
      if (selectedServiceTier == null || "".equals(selectedServiceTier) || value == null || "".equals(value)) {
         return new Boolean(false);
      }
      if (selectedServiceTier.equals(value) || selectedServiceTier.startsWith(value)) {
         return new Boolean(true);
      }
      else {
         return new Boolean(false);
      }
   }


   /**
    * Helper method to determine if the selected web tier matches the passed string. A match is made in the selected
    * appserver equals or starts with the passed value ignoring cases. This method can be used to ignore version
    * numbers.
    * @param value
    * @return Boolean true if there is a match.
    */
   public Boolean matchWebTier(String value) {
      String selectedWebTier = (String) getTemplateSettings().get(JagGenerator.TEMPLATE_WEB_TIER);
      selectedWebTier = selectedWebTier.toLowerCase();
      value = value.toLowerCase();
      if (selectedWebTier == null || "".equals(selectedWebTier) || value == null || "".equals(value)) {
         return new Boolean(false);
      }
      if (selectedWebTier.equals(value) || selectedWebTier.startsWith(value)) {
         return new Boolean(true);
      }
      else {
         return new Boolean(false);
      }
   }


   public String getAuthorText() {
      return authorText.getText().toString();
   }


   public String getVersionText() {
      return versionText.getText().toString();
   }


   public String getCompanyText() {
      return companyText.getText().toString();
   }


   // Variables declaration - do not modify
   private javax.swing.JLabel authorLabel;
   private javax.swing.JTextField authorText;
   private javax.swing.JLabel companyLabel;
   private javax.swing.JTextField companyText;
   private javax.swing.JButton editButton;
   private javax.swing.JPanel panel;
   private javax.swing.JLabel rootPackageLabel;
   private javax.swing.JScrollPane scrollPane;
   public javax.swing.JList templateList;
   private javax.swing.JPanel templateSettingsPanel;
   private javax.swing.JScrollPane templateSettingsScrollPane;
   private javax.swing.JLabel versionLabel;
   private javax.swing.JTextField versionText;
   // End of variables declaration
}

