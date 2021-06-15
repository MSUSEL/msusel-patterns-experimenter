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

import com.finalist.jaggenerator.*;
import com.finalist.jag.util.TemplateString;
import java.awt.event.ActionListener;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*; 
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author  hillie
 */
public class Session extends DefaultMutableTreeNode implements JagBean {

   private DefaultListModel listModel = new DefaultListModel();
   private ArrayList relationRefs = new ArrayList();
//   private ArrayList businessMethods = new ArrayList();
   static Log log = LogFactory.getLog(JagGenerator.class);


   /** Creates new form BeanForm */
   public Session(String rootPackage) {
      initComponents();
      refList.setModel(listModel);
      rootPackageText.setText(rootPackage + ".session");
   }

   public Session(Element el) {
      initComponents();
      refList.setModel(listModel);
      NodeList nl = el.getElementsByTagName("module-data");
      for (int i = 0; i < nl.getLength(); i++) {
         Element child = (Element) nl.item(i);
         String attName = child.getAttribute("name");
         try {
            String value = child.getFirstChild().getNodeValue();
            if (value != null) {
               if (attName.equalsIgnoreCase("name")) {
                  nameText.setText(value);
               } else if (attName.equalsIgnoreCase("root-package")) {
                  rootPackageText.setText(value);
               } else if (attName.equalsIgnoreCase("description")) {
                  descriptionText.setText(value);
               } else if (attName.equalsIgnoreCase("relation-ref")) {
                  NodeList subList = child.getElementsByTagName("module-data");
                  for (int j = 0; j < subList.getLength(); j++) {
                     String entityName = (subList.item(j)).getFirstChild().getNodeValue();
                     relationRefs.add(entityName);
                  }
               }
            }
         } catch (NullPointerException e) {
         }
      }
      nl = el.getElementsByTagName("ref-name");
      if (nl.getLength() > 0) {
         Node node = nl.item(0).getFirstChild();
         if (node != null) refNameText.setText(node.getNodeValue());
      }
      nl = el.getElementsByTagName("ref");
      for (int i = 0; i < nl.getLength(); i++) {
         Node node = nl.item(i).getFirstChild();
         addRef(node.getNodeValue());
      }
      
      nl = el.getElementsByTagName("business-methods");

      for (int i = 0; i < nl.getLength(); i++) {
         Element child = (Element) nl.item(i);
         NodeList bm = child.getElementsByTagName("business-method");
         // Now, we got the list with business methods..
         log.debug("Number of business methods: "+ bm.getLength());
         for (int j=0; j < bm.getLength(); j++) {
            Element bmNode = (Element) bm.item(j);
            BusinessMethod newBusinessMethod = new BusinessMethod(this, bmNode);
            add(newBusinessMethod);
         }
      }
   }

   public ArrayList getBusinessMethods() {
      ArrayList refs = new ArrayList();
      for (int i = 0; i < getChildCount(); i++) {
         JagBean child = (JagBean) getChildAt(i);
         if (child instanceof BusinessMethod) {
            refs.add(child);
         }
      }
      return refs;
   }

   /** set Business methods. */
   public void setBusinessMethods(ArrayList businessMethods) {
      for (int i = 0; i < businessMethods.size(); i++) {
         BusinessMethod child = (BusinessMethod) businessMethods.get(i);
         add(child);
      }
   }

   /**
    * check if there are any business methods defines in this Session.
    * @return true if this session has any business methods.
    */
   public Boolean hasBusinessMethods() {
      if (getBusinessMethods().size() > 0) {
         return new Boolean(true);
      } else {
         return new Boolean(false);
      }
   }

   /**
    * Gets all entities 'covered' by this session bean.
    * @return a list of the entity beans' reference names (String).
    */
   public ArrayList getEntityRefs() {
      ArrayList entityRefs = new ArrayList();
      if (listModel != null) {
         for (int i = 0; i < listModel.size(); i++) {
            entityRefs.add(listModel.get(i));
         }
      }
      return entityRefs;
   }


   /**
    * Sets all entities 'covered' by this session bean.
    */
   public void setEntityRefs(ArrayList refs) {
      if (refs != null) {
         for (int i = 0; i < refs.size(); i++) {
            addRef((String) refs.get(i));
         }
      }
   }

   /**
    * Gets all entities 'covered' by this session bean, excluding those that
    * were only added to support a container-managed relation.
    * @return a list of the entity beans' reference names (String).
    */
   public ArrayList getNonRelationEntityRefs() {
      ArrayList entityRefs = new ArrayList();
      if (listModel != null) {
         for (int i = 0; i < listModel.size(); i++) {
            String entity = (String) listModel.get(i);
            if (!relationRefs.contains(entity)) {
               entityRefs.add(entity);
            }
         }
      }
      return entityRefs;
   }
   public String toString() {
      return "Service - " + getRefName();
   }

   public JPanel getPanel() {
      return panel;
   }

   public void getXML(Element el) throws ParserConfigurationException {
      Document doc = el.getOwnerDocument();
      Element module = doc.createElement("module");
      module.setAttribute("name", "session");

      Element name = doc.createElement("module-data");
      name.setAttribute("name", "name");
      if (nameText.getText() != null) {
        name.appendChild(doc.createTextNode(nameText.getText()));
      }
      module.appendChild(name);

      Element description = doc.createElement("module-data");
      description.setAttribute("name", "description");
      if (descriptionText.getText() != null) {
        description.appendChild(doc.createTextNode(descriptionText.getText()));
      }
      module.appendChild(description);

      Element rootPackage = doc.createElement("module-data");
      rootPackage.setAttribute("name", "root-package");
      if (rootPackageText.getText() != null) {
        rootPackage.appendChild(doc.createTextNode(rootPackageText.getText()));
      }
      module.appendChild(rootPackage);

      Element refName = doc.createElement("ref-name");
      if (refNameText.getText() != null) {
        refName.appendChild(doc.createTextNode(refNameText.getText()));
         module.appendChild(refName);

      } else {
         // Only add in case of references..
      }

      Enumeration refs = listModel.elements();
      if (refs != null) {
         while (refs.hasMoreElements()) {
            Element refNode = doc.createElement("ref");
            String ref = (String) refs.nextElement();
            if (ref != null) {
               refNode.appendChild(doc.createTextNode(ref));
            }
            module.appendChild(refNode);
         }
      }

      // Create the business methods.
      if (getChildCount() > 0) {
         Element businessMethods = doc.createElement("business-methods");
         for (int i= 0; i< getChildCount(); i++) {
            BusinessMethod bm = (BusinessMethod) getChildAt(i);
            bm.getXML(businessMethods);
         }
         module.appendChild(businessMethods);
      }

      if (!relationRefs.isEmpty()) {
         Iterator relRefs = relationRefs.iterator();
         while (relRefs.hasNext()) {
            Element refNode = doc.createElement("module-data");
            refNode.setAttribute("name", "relation-ref");
            Element child = doc.createElement("module-data");
            child.setAttribute("name", "entity-name");
            child.appendChild(doc.createTextNode((String) relRefs.next()));
            refNode.appendChild(child);
            module.appendChild(refNode);
         }
      }

      el.appendChild(module);
   }

   public String getRootPath() {
      return getRootPackage().toString().replace('.', '/');
   }

   public TemplateString getName() {
      return new TemplateString(nameText.getText());
   }

    public TemplateString getUpperCaseName() {
       return new TemplateString(nameText.getText().toUpperCase());
    }

   public void setName(String text) {
      nameText.setText(text);
   }

   public TemplateString getDescription() {
      return new TemplateString(descriptionText.getText());
   }
   
   public void setDescription(String text) {
      descriptionText.setText(text);
   }

   
   public TemplateString getRootPackage() {
      return new TemplateString(rootPackageText.getText());
   }

   public void setRootPackage(String text) {
      rootPackageText.setText(text);
   }


   /**
    * Returns a List of all Entities in this Session, but not those that are only
    * a 'relation reference'.
    * @return
    */
   public List getEntities() {
      List entities = getEntitiesAndReferences();
      entities.removeAll(getReferencedEntities());
      return entities;
   }

   public List getReferencedEntities() {
      ArrayList entities = new ArrayList();
      Iterator relRefs = relationRefs.iterator();
      while (relRefs.hasNext()) {
         String ref = (String) relRefs.next();
         Entity entity = JagGenerator.getEntityByRefName(ref);
         entities.add(entity);
      }
      return entities;
   }

   public List getEntitiesAndReferences() {
      ArrayList entities = new ArrayList();
      Enumeration refs = listModel.elements();
      while (refs.hasMoreElements()) {
         String ref = (String) refs.nextElement();
         Entity entity = JagGenerator.getEntityByRefName(ref);
         entities.add(entity);
      }
      return entities;
   }
   /**
    *
    * @return
    */
   public String getRefName() {
      return refNameText.getText();
   }

   /**
    *
    * @param text
    */
   public void setRefName(String text) {
      refNameText.setText(text);
   }

   /**
    *
     * @param ref
    */
   public void addRef(String ref) {
      listModel.addElement(ref);
   }

   public void addRelationRef(String entity) {
      addRef(entity);
      relationRefs.add(entity);
   }


   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    private void initComponents() {//GEN-BEGIN:initComponents
        panel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        desciptionLabel = new javax.swing.JLabel();
        rootPackageLabel = new javax.swing.JLabel();
        refNameLabel = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        descriptionText = new javax.swing.JTextField();
        rootPackageText = new javax.swing.JTextField();
        refNameText = new javax.swing.JTextField();
        refsLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        refList = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        removeButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();

        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameLabel.setText("Name: ");
        panel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 90, -1));

        desciptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        desciptionLabel.setText("Description: ");
        panel.add(desciptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));

        rootPackageLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        rootPackageLabel.setText("Root-package: ");
        panel.add(rootPackageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 90, -1));

        refNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        refNameLabel.setText("Ref-name: ");
        panel.add(refNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, -1));

        nameText.setAutoscrolls(false);
        nameText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameTextFocusLost(evt);
            }
        });

        panel.add(nameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 260, -1));

        descriptionText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                descriptionTextFocusLost(evt);
            }
        });

        panel.add(descriptionText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 260, -1));

        rootPackageText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rootPackageTextFocusLost(evt);
            }
        });

        panel.add(rootPackageText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 70, 260, -1));

        refNameText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                refNameTextFocusLost(evt);
            }
        });

        panel.add(refNameText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 260, -1));

        refsLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        refsLabel.setText("Refs: ");
        panel.add(refsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 90, -1));

        refList.setBorder(new javax.swing.border.EtchedBorder());
        jScrollPane1.setViewportView(refList);

        panel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 180, 170));

        buttonPanel.setLayout(new java.awt.BorderLayout());

        removeButton.setText("Remove");
        removeButton.setMaximumSize(new java.awt.Dimension(56, 26));
        removeButton.setMinimumSize(new java.awt.Dimension(56, 26));
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(removeButton, java.awt.BorderLayout.SOUTH);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        buttonPanel.add(addButton, java.awt.BorderLayout.CENTER);

        panel.add(buttonPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, -1, -1));

    }//GEN-END:initComponents

   private void refNameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_refNameTextFocusLost
      JagGenerator.stateChanged(false);
   }//GEN-LAST:event_refNameTextFocusLost

   private void rootPackageTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rootPackageTextFocusLost
      JagGenerator.stateChanged(false);
   }//GEN-LAST:event_rootPackageTextFocusLost

   private void descriptionTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionTextFocusLost
      JagGenerator.stateChanged(false);
   }//GEN-LAST:event_descriptionTextFocusLost

   private void nameTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameTextFocusLost
      // Add your handling code here:
      if ((nameText.getText() != null) && (nameText.getText().length() > 0)) {
         nameText.setText(Utils.initCap(nameText.getText()));
         this.descriptionText.setText(Utils.initCap(nameText.getText()));
         this.refNameText.setText(Utils.initCap(nameText.getText()));
      }
      JagGenerator.stateChanged(true);
   }//GEN-LAST:event_nameTextFocusLost


   private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
      if (((Root) getRoot()).getRefs().isEmpty()) {
         JOptionPane.showMessageDialog(getPanel(),
               "There are no entity beans specified in the current application, \n" +
               "so it is not possible to add anything to this service bean yet.", "No entity beans!", javax.swing.JOptionPane.ERROR_MESSAGE);
         return;
      }
      ArrayList refs = new ArrayList();
      JFrame tmpFrame = new JFrame();
      new SelectRefs(tmpFrame, this, refs).show();
      if (refs == null)
         return;

      for (int i = 0; i < refs.size(); i++) {
         listModel.addElement(refs.get(i));
      }

      refList.setModel(listModel);
      JagGenerator.stateChanged(false);
   }//GEN-LAST:event_addButtonActionPerformed

   
   private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
      int[] selection = refList.getSelectedIndices();
      if (selection != null) {
         for (int i = selection.length - 1; i >= 0; i--) {
            relationRefs.remove(listModel.elementAt(selection[i]));
            listModel.removeElementAt(selection[i]);
         }
         refList.setModel(listModel);
         JagGenerator.stateChanged(false);
      }
   }//GEN-LAST:event_removeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JLabel desciptionLabel;
    public javax.swing.JTextField descriptionText;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nameLabel;
    public javax.swing.JTextField nameText;
    private javax.swing.JPanel panel;
    private javax.swing.JList refList;
    private javax.swing.JLabel refNameLabel;
    public javax.swing.JTextField refNameText;
    private javax.swing.JLabel refsLabel;
    private javax.swing.JButton removeButton;
    private javax.swing.JLabel rootPackageLabel;
    private javax.swing.JTextField rootPackageText;
    // End of variables declaration//GEN-END:variables
}

