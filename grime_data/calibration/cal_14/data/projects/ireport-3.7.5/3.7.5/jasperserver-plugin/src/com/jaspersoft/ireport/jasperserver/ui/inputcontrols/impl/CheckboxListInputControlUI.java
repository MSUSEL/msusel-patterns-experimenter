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
package com.jaspersoft.ireport.jasperserver.ui.inputcontrols.impl;

import com.jaspersoft.ireport.jasperserver.ui.inputcontrols.ListItemWrapper;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;

/**
 *
 * @author  gtoffoli
 */
public class CheckboxListInputControlUI extends javax.swing.JPanel implements InputControlUI {

    private java.util.HashMap itemValues = new java.util.HashMap();
    private List<ActionListener> listeners = new ArrayList<ActionListener>();
    
    
    /** Creates new form BasicInputControlUI */
    public CheckboxListInputControlUI() {
        initComponents();
    }
    
    public Object getValue()
    {
        java.util.List list = new java.util.ArrayList();
        
        Iterator it = itemValues.keySet().iterator();
        while (it.hasNext())
        {
            JCheckBox bm = (JCheckBox)it.next();
            Object val = itemValues.get(bm);
            if (bm.isSelected())
            {
                list.add(val);
            }
        }
        
        return list;
    }
    
    public void setValue(Object v)
    {
        if (v != null && v instanceof Collection)
        {
            Iterator values = ((Collection)v).iterator();
            while (values.hasNext())
            {
                Object val = values.next();
                if (val == null) continue;
                if (itemValues.containsValue(val))
                {
                    Iterator it = itemValues.keySet().iterator();
                    while (it.hasNext())
                    {
                        JCheckBox bm = (JCheckBox)it.next();
                        Object val2 = itemValues.get(bm);
                        if (val2 != null && val2.equals(val))
                        {
                            bm.setSelected(true);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabelName = new javax.swing.JLabel();
        jPanelList = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();

        setLayout(new java.awt.GridBagLayout());

        setPreferredSize(new java.awt.Dimension(250, 40));
        jLabelName.setText("jLabel1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(jLabelName, gridBagConstraints);

        jPanelList.setLayout(new javax.swing.BoxLayout(jPanelList, javax.swing.BoxLayout.Y_AXIS));

        jRadioButton1.setText("jRadioButton1");
        jRadioButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton1.setMargin(new java.awt.Insets(4, 0, 0, 0));
        jRadioButton1.setMaximumSize(new java.awt.Dimension(85, 19));
        jRadioButton1.setMinimumSize(new java.awt.Dimension(85, 19));
        jRadioButton1.setPreferredSize(new java.awt.Dimension(85, 19));
        jPanelList.add(jRadioButton1);

        jRadioButton2.setText("jRadioButton2");
        jRadioButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jRadioButton2.setMargin(new java.awt.Insets(4, 0, 0, 0));
        jRadioButton2.setMaximumSize(new java.awt.Dimension(85, 19));
        jRadioButton2.setMinimumSize(new java.awt.Dimension(85, 19));
        jRadioButton2.setPreferredSize(new java.awt.Dimension(85, 19));
        jPanelList.add(jRadioButton2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 14, 4, 4);
        add(jPanelList, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents
    
    public void setLabel(String lbl)
    {
        this.jLabelName.setText(lbl);
    }
    
    /**
     *  Expected a list of ListItemWrapper
     */
    public void setHistory(java.util.List values){
        
        if (values == null) return;
        
        
        jPanelList.removeAll();
        this.buttonGroup1 = new ButtonGroup();
        
        //java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        //gridBagConstraints.gridx = 0;
        //gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        //gridBagConstraints.weightx = 1.0;
        //gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        
        // Try to understand how much columns...
        
        int w = 100;
        Dimension d = jPanelList.getPreferredSize();
        for (int i=0; i<values.size(); ++i)
        {
            ListItemWrapper iw =  (ListItemWrapper)values.get(i);

            JCheckBox rb = new JCheckBox(iw.getItem().getLabel());
            rb.setSelected(false);
            itemValues.put(rb, iw.getItem().getValue() );
            
            rb.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
            rb.setMargin(new java.awt.Insets(0, 0, 0, 0));
            
            if (rb.getPreferredSize().width > w) w = rb.getPreferredSize().width;
            d = new Dimension(w,(rb.getPreferredSize().height)* values.size());
            
            jPanelList.add(rb); //, gridBagConstraints);

            for (ActionListener listener : listeners)
            {
                rb.addActionListener(listener);
            }
        }
        
        jPanelList.setPreferredSize(d);
        jPanelList.setMinimumSize(d);
        d.height += jLabelName.getPreferredSize().height+6;
        this.setPreferredSize(d);
        this.setMinimumSize(d);
        
        jPanelList.updateUI();
        this.updateUI();
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabelName;
    private javax.swing.JPanel jPanelList;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    // End of variables declaration//GEN-END:variables
    
    public void setReadOnly( boolean b )
    {
        
        for (int i=0; i<jPanelList.getComponentCount(); ++i)
        {
            Component c = jPanelList.getComponent(i);
            if (c instanceof JCheckBox)
            {
               ((JCheckBox)c).setEnabled(!b);
            }
        }
    }

    public void addActionListener(ActionListener listener) {
        listeners.add(listener);
        for (int i=0; i<jPanelList.getComponentCount(); ++i)
        {
            Component c = jPanelList.getComponent(i);
            if (c instanceof JCheckBox)
            {
               ((JCheckBox)c).addActionListener(listener);
            }
        }
    }

}
