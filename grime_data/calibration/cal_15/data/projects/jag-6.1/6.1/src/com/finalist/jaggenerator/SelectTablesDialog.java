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
package com.finalist.jaggenerator;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author  hillie
 */
public class SelectTablesDialog extends JDialog {
   private DefaultListModel listModel = new DefaultListModel();

   private static final ArrayList tableList = new ArrayList();
   private static final ArrayList alreadySelected = new ArrayList();


   /** Creates new form SelectTablesDialog */
   public SelectTablesDialog(JagGenerator parent) {
      super(parent, true);
      initComponents();
      this.setTitle("Select tables..");
      setBounds(50, 10, 200, 700);

      ArrayList tables = DatabaseUtils.getTables();
      if (tables != null) {
         for (Iterator it = tables.iterator(); it.hasNext();) {
            String table = (String) it.next();
            if (!alreadySelected.contains(table)) {
               listModel.addElement(table);
            }
         }
         list.setModel(listModel);
      }
   }

   /**
    * Once the user has selected tables using this dialog, this method returns the selected tables.
    * @return the ArrayList of selected table names (String), never <code>null</code>.
    */
   public static ArrayList getTablelist() {
      return tableList;
   }

   /**
    * By adding table names to this list, those tables are excluded from appearing in future dialogues.
    *
    * @return
    */
   public static List getAlreadyselected() {
      return alreadySelected;
   }

   /**
    * clears all lists.
    */
   public static void clear() {
      tableList.clear();
      alreadySelected.clear();
   }

   /** This method is called from within the constructor to
    * initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is
    * always regenerated by the Form Editor.
    */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        selectButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });

        jScrollPane1.setViewportView(list);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        selectButton.setText("Select");
        selectButton.setSelected(true);
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        jPanel1.add(selectButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jPanel1.add(cancelButton);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel1, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listMouseClicked
       if (evt.getClickCount() > 1) {
          selectButtonActionPerformed(null);
       }
    }//GEN-LAST:event_listMouseClicked

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        closeDialog(null);
    }//GEN-LAST:event_cancelButtonActionPerformed


   private void selectButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
      Object[] tables = list.getSelectedValues();
      evt = null;
      tableList.clear();
      tableList.addAll(Arrays.asList(tables));
      dispose();
   }//GEN-LAST:event_selectButtonActionPerformed


   /** Closes the dialog */
   private void closeDialog(WindowEvent evt) {//GEN-FIRST:event_closeDialog
      tableList.clear();
      evt = null;
      this.dispose();
   }//GEN-LAST:event_closeDialog

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list;
    private javax.swing.JButton selectButton;
   // End of variables declaration//GEN-END:variables


}
