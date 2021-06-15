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

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author  m.oconnor, r. ekkelenkamp
 */
public class RelationPanel extends javax.swing.JPanel {
   private static final String ONE_TO_ONE = "one to one";
   private static final String ONE_TO_MANY = "one to many";
   private static final String MANY_TO_ONE = "many to one";   
   private Relation model;
   private boolean constructed = false;

   /**
    * Creates new form RelationPanel
    *
    * @param relation The Relation that this object views.
    * @param waitForInitSignal if <code>true</code> delay initialisation until notified.
    */
   public RelationPanel(Relation relation, final boolean waitForInitSignal) {
      model = relation;
      initComponents();
      if (waitForInitSignal) {
         new Thread("RelationPanel(" + model.getName() + ")") {
            public void run() {
               initValues(true);
            }
         }.start();
      } else {
         initValues(false);
      }
   }

   public void initValues(boolean waitForInitSignal) {
      constructed = false;
      final boolean initFromXML = !JagGenerator.isDatabaseConnected();
      nameField.setText(model.getName());

      //init list of selected tables (excluding local table)
      foreignTableCombo.removeAllItems();
      List tableList = null;
      if (initFromXML) {
         tableList = new ArrayList();
         tableList.add(model.getForeignTable());
         foreignTableCombo.setEditable(true);
      } else {
         foreignTableCombo.setEditable(false);
         tableList = DatabaseUtils.getTables();
      }

      Iterator tables = tableList.iterator();
      while (tables.hasNext()) {
         String tableName = (String) tables.next();
         TemplateString localTableName = model.getLocalEntity().getLocalTableName();
         if (tableName != null && localTableName != null &&  !tableName.equals(localTableName.toString())) {
            foreignTableCombo.addItem(tableName);
         }
      }
      foreignTableCombo.setSelectedItem(model.getForeignTable());

      initColumnsList();

      //cardinalityCombo.addItem(ONE_TO_ONE);
      cardinalityCombo.addItem(MANY_TO_ONE);
      cardinalityCombo.setSelectedItem(MANY_TO_ONE);
      /*
       * Currently only Many to one is supported.
      if (model.isTargetMultiple()) {
         cardinalityCombo.setSelectedItem(MANY_TO_ONE);
      } else {
         cardinalityCombo.setSelectedItem(ONE_TO_ONE);
      }
       */
      if (model.isBidirectional()) {
         bidirectionalCheckbox.setSelected(true);
      } else {
         bidirectionalCheckbox.setSelected(false);         
      }

      if (waitForInitSignal) {
         synchronized(this) {
            try {
               //wait for the local-side entity to finish being constructed before finishing off..
               wait();
               updateFieldsCombo();

            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      } else {
         updateFieldsCombo();
      }

      //initially the field referred to by this relation might not be aware it's a foreign key - make it so..
      Iterator parentFields = model.getLocalEntity().getFields().iterator();
      while (parentFields.hasNext()) {
         Field field = (Field) parentFields.next();
         if (field.getName().toString().equals(fkFieldCombo.getSelectedItem())) {
            field.setForeignKey(true);
         }
      }

      constructed = true;
   }


   /**
    * Sometimes it is necessary to force an update of the relation name in the view
    * from the model.  This method does that!
    * @param relationName the new name.
    */
   public void setName(String relationName) {
      nameField.setText(relationName);
   }

   public void updateFieldName(String oldName, String newName) {
      Object selected = fkFieldCombo.getSelectedItem();
      ArrayList contents = new ArrayList();
      for (int i = 0; i < fkFieldCombo.getModel().getSize(); i++) {
         Object field = fkFieldCombo.getModel().getElementAt(i);
         if (field.equals(oldName)) {
            contents.add(newName);
            if (selected.equals(field)) {
               selected = newName;
            }
         } else {
            contents.add(field);
         }
      }

      fkFieldCombo.removeAllItems();
      Iterator newList = contents.iterator();
      while (newList.hasNext()) {
         Object o = newList.next();
         fkFieldCombo.addItem(o);
      }
      fkFieldCombo.setSelectedItem(selected);
   }

   private void updateFieldsCombo() {
      //init list of fields from local entity bean
      fkFieldCombo.removeAllItems();
      Iterator fields = model.getLocalEntity().getFields().iterator();
      while (fields.hasNext()) {
         Field field = (Field) fields.next();
         fkFieldCombo.addItem(field.getName().toString());
         if (field.getName().equals(model.getFieldName())) {
            fkFieldCombo.setSelectedItem(field.getName().toString());
         }
      }
   }

   private void initColumnsList() {
      foreignPkCombo.removeAllItems();
      String selectedTable = (String) foreignTableCombo.getSelectedItem();
      List columnsList = null;
      if (JagGenerator.isDatabaseConnected()) {
         columnsList = DatabaseUtils.getColumns(selectedTable);
      }
      if (columnsList == null || columnsList.isEmpty()) {
         foreignPkCombo.addItem(model.getForeignColumn());
         foreignPkCombo.setEditable(true);
      } else {
         foreignPkCombo.setEditable(false);
         Iterator columns = columnsList.iterator();
         while (columns.hasNext()) {
            Column column = (Column) columns.next();
            foreignPkCombo.addItem(column.getName());
            if (column.isPrimaryKey()) {
               foreignPkCombo.setSelectedItem(column.getName());
            }
         }
      }
   }

   private void formPropertyChange() {
      if (constructed) {
         model.setName(nameField.getText());
         String oldFkField = model.getFieldName().toString();
         String newFkField = getTextFromJComboBox(fkFieldCombo);
         model.setFieldName(newFkField);
         if (oldFkField != null && !oldFkField.equals(newFkField)) {
            JagGenerator.setForeignKeyInField(model.getLocalEntity().getLocalTableName().toString(), newFkField);
         }
         model.setForeignTable(getTextFromJComboBox(foreignTableCombo));
         model.setForeignColumn(getTextFromJComboBox(foreignPkCombo));
         model.setForeignPkFieldName(Utils.format(getTextFromJComboBox(foreignPkCombo)));
         model.setTargetMultiple(MANY_TO_ONE.equals(getTextFromJComboBox(cardinalityCombo)));
         model.setBidirectional(bidirectionalCheckbox.getSelectedObjects() != null);
      }
      JagGenerator.stateChanged(false);
   }


   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    private void initComponents() {//GEN-BEGIN:initComponents
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        fkFieldLabel = new javax.swing.JLabel();
        foreignTableCombo = new javax.swing.JComboBox();
        foreignTableLabel = new javax.swing.JLabel();
        fkFieldCombo = new javax.swing.JComboBox();
        foreignPkLabel = new javax.swing.JLabel();
        foreignPkCombo = new javax.swing.JComboBox();
        bidirectionalCheckbox = new javax.swing.JCheckBox();
        cardinalityLabel = new javax.swing.JLabel();
        cardinalityCombo = new javax.swing.JComboBox();

        setLayout(null);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("- relation -");
        add(jLabel1);
        jLabel1.setBounds(110, 20, 380, 15);

        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldFocusLost(evt);
            }
        });

        add(nameField);
        nameField.setBounds(190, 70, 260, 20);

        nameLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        nameLabel.setText("relation name:");
        nameLabel.setToolTipText("The name used internally to represent this (end of the) relation.");
        add(nameLabel);
        nameLabel.setBounds(20, 70, 120, 20);

        fkFieldLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        fkFieldLabel.setText("foreign key field:");
        add(fkFieldLabel);
        fkFieldLabel.setBounds(20, 100, 120, 20);

        foreignTableCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foreignTableComboActionPerformed(evt);
            }
        });
        foreignTableCombo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                foreignTableComboFocusLost(evt);
            }
        });

        add(foreignTableCombo);
        foreignTableCombo.setBounds(190, 160, 260, 23);

        foreignTableLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        foreignTableLabel.setText("foreign table:");
        add(foreignTableLabel);
        foreignTableLabel.setBounds(20, 160, 120, 20);

        fkFieldCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fkFieldComboActionPerformed(evt);
            }
        });

        add(fkFieldCombo);
        fkFieldCombo.setBounds(190, 100, 260, 23);

        foreignPkLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        foreignPkLabel.setText("foreign table's primary key:");
        add(foreignPkLabel);
        foreignPkLabel.setBounds(20, 190, 160, 20);

        foreignPkCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                foreignPkComboActionPerformed(evt);
            }
        });
        foreignPkCombo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                foreignPkComboFocusLost(evt);
            }
        });

        add(foreignPkCombo);
        foreignPkCombo.setBounds(190, 190, 260, 23);

        bidirectionalCheckbox.setFont(new java.awt.Font("Dialog", 0, 12));
        bidirectionalCheckbox.setText("bi-directional relationship?");
        add(bidirectionalCheckbox);
        bidirectionalCheckbox.setBounds(20, 280, 190, 25);

        cardinalityLabel.setFont(new java.awt.Font("Dialog", 0, 12));
        cardinalityLabel.setText("cardinality:");
        add(cardinalityLabel);
        cardinalityLabel.setBounds(20, 250, 160, 20);

        cardinalityCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cardinalityComboActionPerformed(evt);
            }
        });

        add(cardinalityCombo);
        cardinalityCombo.setBounds(190, 250, 260, 23);

    }//GEN-END:initComponents

    private void cardinalityComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cardinalityComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cardinalityComboActionPerformed

    private void foreignPkComboFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_foreignPkComboFocusLost
      formPropertyChange();
      JagGenerator.stateChanged(false);
    }//GEN-LAST:event_foreignPkComboFocusLost

    private void foreignTableComboFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_foreignTableComboFocusLost
      formPropertyChange();
      JagGenerator.stateChanged(false);
    }//GEN-LAST:event_foreignTableComboFocusLost

   private void fkFieldComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fkFieldComboActionPerformed
      formPropertyChange();
   }//GEN-LAST:event_fkFieldComboActionPerformed

   private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
      //this is necessary to make sure the field currenlty having focus
      //is synched with the panel when the File > Save menu is selected.
      formPropertyChange();
   }//GEN-LAST:event_formMouseExited

   private void foreignPkComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foreignPkComboActionPerformed
      formPropertyChange();
   }//GEN-LAST:event_foreignPkComboActionPerformed

   private void nameFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldFocusLost
      formPropertyChange();
      JagGenerator.stateChanged(true);
   }//GEN-LAST:event_nameFieldFocusLost

   private void foreignTableComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_foreignTableComboActionPerformed
      evt = null;
      formPropertyChange();
      initColumnsList();
      JagGenerator.stateChanged(false);
   }//GEN-LAST:event_foreignTableComboActionPerformed

   private String getTextFromJComboBox(JComboBox combo) {
      String a = (String) combo.getEditor().getItem();
      String b = (String) combo.getSelectedItem();
      return combo.isEditable() ? a : b;
   }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox bidirectionalCheckbox;
    private javax.swing.JComboBox cardinalityCombo;
    private javax.swing.JLabel cardinalityLabel;
    private javax.swing.JComboBox fkFieldCombo;
    private javax.swing.JLabel fkFieldLabel;
    private javax.swing.JComboBox foreignPkCombo;
    private javax.swing.JLabel foreignPkLabel;
    private javax.swing.JComboBox foreignTableCombo;
    private javax.swing.JLabel foreignTableLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    // End of variables declaration//GEN-END:variables

}
