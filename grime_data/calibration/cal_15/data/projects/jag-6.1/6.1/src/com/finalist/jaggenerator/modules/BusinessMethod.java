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

import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Helper class for representing business methods.
 *
 * @author Rudie Ekkelenkamp - Finalist IT Group
 * @version $Revision: 1.6 $, $Date: 2005/11/25 12:45:19 $
 */
public class BusinessMethod extends DefaultMutableTreeNode implements JagBean {
   
   static Log log = LogFactory.getLog(BusinessMethod.class);
    
   private Session parentEntity;

   /** Creates new form BeanForm */
   public BusinessMethod(Session parent) {
        initComponents();
      parentEntity = parent;
   }
   
   public BusinessMethod() {
        initComponents();
   }
   
   /** Use for building up the entity field gui component */
   public BusinessMethod(Session parent,Element node) {
        initComponents();
      parentEntity = parent;
      try {
            NodeList rt = node.getElementsByTagName("return-type");
            if (rt.getLength() == 1) {
               String returnType = "";
               if (rt.item(0).getFirstChild() != null) {
                  returnType = rt.item(0).getFirstChild().getNodeValue();
               }
               log.debug("The return type is: " + returnType);
               setReturnType(returnType);
            }
            NodeList mn = node.getElementsByTagName("method-name");
            if (mn.getLength() == 1) {
               String methodName = "";
               if (mn.item(0).getFirstChild() != null) {
                  methodName = mn.item(0).getFirstChild().getNodeValue();
               }
               log.debug("The method name is: " + methodName);
               setMethodName(methodName);
            }
            NodeList md = node.getElementsByTagName("description");
            if (md.getLength() == 1) {

               String methodDescription = "";
               if (md.item(0).getFirstChild() != null) {
                  methodDescription = md.item(0).getFirstChild().getNodeValue();
               }
               log.debug("The method description is: " + methodDescription);
               setDescription(methodDescription);
            }
            ArrayList businessArguments = new ArrayList();
            NodeList params = node.getElementsByTagName("parameter");
            for (int k = 0; k < params.getLength(); k++) {
               BusinessArgument businessArgument = new BusinessArgument();
               Element param = (Element) params.item(k);
               NodeList type = param.getElementsByTagName("type");
               if (type.getLength() == 1) {
                  String baType = "";
                  if (type.item(0).getFirstChild() != null) {
                     baType = type.item(0).getFirstChild().getNodeValue();
                  }
                  log.debug("The param type is: " + baType);
                  businessArgument.setType(baType);
               }
               NodeList name = param.getElementsByTagName("name");
               if (name.getLength() == 1) {
                  String baName = "";
                  if (name.item(0).getFirstChild() != null) {
                     baName = name.item(0).getFirstChild().getNodeValue();
                  }
                  log.debug("The param name is: " + baName);
                  businessArgument.setName(baName);
               }
               businessArguments.add(businessArgument);
            }
            setArgumentList(businessArguments);
            log.debug("Added a businessmethod to the list");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
    
    
   public javax.swing.JPanel getPanel() {
       return panel;
   }

   
   public String getRefName() {
       return getSignature();
   }

   
   public String getSignature() {
       StringBuffer signature = new StringBuffer();
       signature.append(getReturnType());
       signature.append(" ");
       signature.append(getMethodName());
       signature.append("(");
       for(Iterator i = getArgumentList().iterator(); i.hasNext(); ) {
           BusinessArgument next = (BusinessArgument) i.next();
           signature.append(next.getType());
           signature.append(" ");
           signature.append(next.getName());
           if(i.hasNext()) {
               signature.append(",");
           }
       }
       signature.append(")");
       return signature.toString();
   }

   public void getXML(Element el) throws ParserConfigurationException {
      Document doc = el.getOwnerDocument();
        Element businessMethod = doc.createElement("business-method");

        Element returnType = doc.createElement("return-type");
        if (getReturnType() == null) {
           returnType.appendChild(doc.createTextNode("void"));
        }  else {
           returnType.appendChild(doc.createTextNode(getReturnType()));
        }
        businessMethod.appendChild(returnType);

        Element methodName = doc.createElement("method-name");
        String methodString = getMethodName();
        if (methodString == null) {
           methodString = "";
        }
        methodName.appendChild(doc.createTextNode(methodString));
        businessMethod.appendChild(methodName);

        Element desc = doc.createElement("description");
        String descString = getDescription();
        if (descString == null) {
           descString = "";
        }
        desc.appendChild(doc.createTextNode(descString));
        businessMethod.appendChild(desc);

        for (int j = 0; j < getArgumentList().size(); j++) {
           Element parameter = doc.createElement("parameter");
           BusinessArgument arg = (BusinessArgument) getArgumentList().get(j);

           Element type = doc.createElement("type");
           String typeString = arg.getType();
           if (typeString == null) {
              typeString = "";
           }

           type.appendChild(doc.createTextNode(typeString));
           parameter.appendChild(type);

           Element pname = doc.createElement("name");
           String nameString = arg.getName();
           if (nameString == null) {
              nameString = "";
           }
           pname.appendChild(doc.createTextNode(nameString));
           parameter.appendChild(pname);

           businessMethod.appendChild(parameter);
        }
        el.appendChild(businessMethod);
   }
    
   
    
    /**
     * the return type of the method.
     * @return
     */
    public String getReturnType() {
        return returnTypeInput.getText();
    }
    
    /**
     * Set the return type of the business method.
     *
     * @param returnType
     */
    public void setReturnType(String returnType) {
        returnTypeInput.setText(returnType);
    }
    
    /**
     * Get the business method name.
     *
     * @return the name.
     */
    public String getMethodName() {
        return methodNameInput.getText();
    }
    
    /**
     * Get the business method name in uppercase notation.
     *
     * @return the name.
     */
    public String getMethodNameUpper() {
        String methodName = getMethodName();
        if (methodName == null) {
            return methodName;
        }
        if (methodName.length() > 1) {
            return new String(methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
        }
        return methodName.toUpperCase();
        
    }
    
    /**
     * Set the method name.
     *
     * @param methodName the name.
     */
    public void setMethodName(String methodName) {
        methodNameInput.setText(methodName);
    }
    
    /**
     * Get the method description.
     * @return description.
     */
    public String getDescription() {
        return descriptionInput.getText();
    }
    
    /**
     * Set the method description.
     *
     * @param description
     */
    public void setDescription(String description) {
        descriptionInput.setText(description);
    }
    
    /**
     * get a list of all arguments of the BusinessArgument class.
     *
     * @return Collection of BusinessArgument classes.
     */
    public ArrayList getArgumentList() {
        DefaultTableModel model = (DefaultTableModel)parametersTable.getModel();
        
        ArrayList argumentList = new ArrayList();
        
        for(int count = 0; count < model.getRowCount(); count++) {
           BusinessArgument argument = new BusinessArgument();
           if (model.getValueAt(count, 0) != null) {
            argument.setName(model.getValueAt(count, 0).toString());
           }
           if (model.getValueAt(count, 1) != null) {
            argument.setType(model.getValueAt(count, 1).toString());
           }
           argumentList.add(argument);
        }
        
        return argumentList;
    }
    
    /**
     * Set the argument list.
     * @param argumentList
     */
    public void setArgumentList(ArrayList argumentList) {
        Object data[][] = new Object[argumentList.size()][2];
        
        for(int count = 0; count < argumentList.size(); count++) {
            BusinessArgument arg = (BusinessArgument) argumentList.get(count);
            data[count][0] = arg.getName();
            data[count][1] = arg.getType();
        }
        
        parametersTable.setModel(new javax.swing.table.DefaultTableModel(
            data,
            new String [] {
                "Parameter", "Type"
            }
        ));
    }
    
    public String toString() {
        return getRefName();
    }


   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    private void initComponents() {//GEN-BEGIN:initComponents
        panel = new javax.swing.JPanel();
        returnTypeLabel = new javax.swing.JLabel();
        methodNameLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        parametersLabel = new javax.swing.JLabel();
        returnTypeInput = new javax.swing.JTextArea();
        methodNameScrollPane = new javax.swing.JScrollPane();
        methodNameInput = new javax.swing.JTextArea();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionInput = new javax.swing.JTextArea();
        parametersScrollPane = new javax.swing.JScrollPane();
        parametersTable = new javax.swing.JTable();
        addParameterButton = new javax.swing.JButton();
        deleteParameterButton = new javax.swing.JButton();

        panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        returnTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnTypeLabel.setText("Return type:");
        panel.add(returnTypeLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 90, -1));

        methodNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        methodNameLabel.setText("Method name:");
        panel.add(methodNameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 90, -1));

        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        descriptionLabel.setText("Description:");
        panel.add(descriptionLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 90, -1));

        parametersLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        parametersLabel.setText("Parameters:");
        panel.add(parametersLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 90, -1));

        returnTypeInput.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        returnTypeInput.setAutoscrolls(false);
        returnTypeInput.setBorder(new javax.swing.border.EtchedBorder());
        returnTypeInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                inputKeyReleased(evt);
            }
        });

        panel.add(returnTypeInput, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 260, -1));

        methodNameScrollPane.setBorder(null);
        methodNameInput.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        methodNameInput.setLineWrap(true);
        methodNameInput.setWrapStyleWord(true);
        methodNameInput.setBorder(new javax.swing.border.EtchedBorder());
        methodNameScrollPane.setViewportView(methodNameInput);

        panel.add(methodNameScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 260, 40));

        descriptionScrollPane.setBorder(null);
        descriptionScrollPane.setFocusable(false);
        descriptionScrollPane.setHorizontalScrollBar(descriptionScrollPane.getHorizontalScrollBar());
        descriptionInput.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 11));
        descriptionInput.setLineWrap(true);
        descriptionInput.setWrapStyleWord(true);
        descriptionInput.setBorder(new javax.swing.border.EtchedBorder());
        descriptionScrollPane.setViewportView(descriptionInput);

        panel.add(descriptionScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 260, 80));

        parametersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Parameter", "Type"
            }
        ));
        parametersScrollPane.setViewportView(parametersTable);

        panel.add(parametersScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, 260, 110));

        addParameterButton.setText("Add");
        addParameterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addParameterButtonActionPerformed(evt);
            }
        });

        panel.add(addParameterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 310, -1, -1));

        deleteParameterButton.setText("Delete");
        deleteParameterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteParameterButtonActionPerformed(evt);
            }
        });

        panel.add(deleteParameterButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, -1, -1));

    }//GEN-END:initComponents

    private void inputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputKeyReleased
      JagGenerator.stateChanged(false);
    }//GEN-LAST:event_inputKeyReleased

    private void deleteParameterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteParameterButtonActionPerformed
        int selectedRows[] = parametersTable.getSelectedRows();
        
        ArrayList argumentList = getArgumentList();
        for(int count = selectedRows.length -1; count >= 0; count--) {
            int deleteCol = selectedRows[count];
            System.out.println("deleting: "+deleteCol);
            
            argumentList.remove(deleteCol);
        }
        setArgumentList(argumentList);
      JagGenerator.stateChanged(false);
        
    }//GEN-LAST:event_deleteParameterButtonActionPerformed

    private void addParameterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addParameterButtonActionPerformed
        BusinessArgument ba = new BusinessArgument();
        ArrayList argumentList = getArgumentList();
        argumentList.add(ba);
        setArgumentList(argumentList);
      JagGenerator.stateChanged(false);
        
    }//GEN-LAST:event_addParameterButtonActionPerformed

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
      JagGenerator.stateChanged(false);

   }//GEN-LAST:event_nameTextFocusLost


   private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
   }//GEN-LAST:event_addButtonActionPerformed

   

   private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
   }//GEN-LAST:event_removeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addParameterButton;
    private javax.swing.JButton deleteParameterButton;
    private javax.swing.JTextArea descriptionInput;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JTextArea methodNameInput;
    private javax.swing.JLabel methodNameLabel;
    private javax.swing.JScrollPane methodNameScrollPane;
    private javax.swing.JPanel panel;
    private javax.swing.JLabel parametersLabel;
    private javax.swing.JScrollPane parametersScrollPane;
    private javax.swing.JTable parametersTable;
    private javax.swing.JTextArea returnTypeInput;
    private javax.swing.JLabel returnTypeLabel;
    // End of variables declaration//GEN-END:variables
}
