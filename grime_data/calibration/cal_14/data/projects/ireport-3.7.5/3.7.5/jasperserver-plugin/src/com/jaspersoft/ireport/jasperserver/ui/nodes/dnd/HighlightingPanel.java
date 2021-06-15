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
/*
 * HighlightingPanel.java
 *
 * Created on 31-ago-2010, 17.33.44
 */

package com.jaspersoft.ireport.jasperserver.ui.nodes.dnd;

import com.jaspersoft.ireport.jasperserver.ui.RepositoryListCellRenderer;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author gtoffoli
 */
public class HighlightingPanel extends javax.swing.JPanel {

    public static final int TYPE_COPY_REPLACE = 0;
    public static final int TYPE_DONT_COPY = 1;
    public static final int TYPE_COPY = 2;

    public static final int MODE_COPY = 1;
    public static final int MODE_MOVE = 2;

    boolean pressed = false;
    boolean over = false;
    private int mode = MODE_COPY;


    Color originalBG = null;
    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    /** Creates new form HighlightingPanel */
    public HighlightingPanel() {
        initComponents();
        this.originalBG = this.getBackground();
        EmptyBorder border = new EmptyBorder(0,0,4,4);
        setBorder(border);
    }

    public void setResource(ResourceDescriptor rd, int buttonType, String newName)
    {

        if (rd != null)
        {
            jLabelNameCR.setText("<html><b>" +  rd.getLabel() );
            jLabelUriCR.setText( rd.getUriString() );
            jLabelIconCR.setIcon(RepositoryListCellRenderer.getResourceIcon(rd));
        }
        
        switch (buttonType)
        {
            case TYPE_COPY_REPLACE:
            {
                if (mode == MODE_COPY)
                {
                    jLabelTitle.setText("<html><font size=\"4\">Copy and Replace");
                }
                else
                {
                    jLabelTitle.setText("<html><font size=\"4\">Move and Replace");
                }
                jLabelDescription.setText("Replace the resource in the destination folder with the resource you are copying:");
                break;
            }
            case TYPE_DONT_COPY:
            {
                if (mode == MODE_COPY)
                {
                    jLabelTitle.setText("<html><font size=\"4\">Don't copy");
                }
                else
                {
                    jLabelTitle.setText("<html><font size=\"4\">Don't move");
                }
                jLabelDescription.setText("No resources will be changed. Leave this resource in the destination folder:");
                break;
            }
            case TYPE_COPY:
            {
                if (mode == MODE_COPY)
                {
                    jLabelTitle.setText("<html><font size=\"4\">Copy, but keep both resources");
                    jLabelDescription.setText( (new MessageFormat("The resource you are copying will be renamed \"{0}\"")).format(new Object[]{newName}) );

                }
                else
                {
                    jLabelTitle.setText("<html><font size=\"4\">Move, but keep both resources");
                    jLabelDescription.setText( (new MessageFormat("The resource you are moving will be renamed \"{0}\"")).format(new Object[]{newName}) );

                }
                
                jLabelIconCR.setVisible(false);
                jLabelNameCR.setVisible(false);
                jLabelUriCR.setVisible(false);

                break;
            }
        }

    }

    public void bindActions()
    {
        for (int i=0; i<getComponentCount(); ++i)
        {
            Component c = getComponent(i);
            if (c instanceof JComponent)
            {
                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        formMouseClicked(e);
                    }
                });
            }
        }
    }
    
    public void addActionListener(ActionListener listener)
    {
        if (!listeners.contains(listener)) listeners.add(listener);
    }
    
    public void removeActionListener(ActionListener listener)
    {
        listeners.remove(listener);
    }

    private void fireAction()
    {
        for (ActionListener listener : listeners)
        {
            ActionEvent event = new ActionEvent(this, 0, "");
            listener.actionPerformed(event);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelIconCR = new javax.swing.JLabel();
        jLabelUriCR = new javax.swing.JLabel();
        jLabelNameCR = new javax.swing.JLabel();
        jLabelDescription = new javax.swing.JLabel();
        jLabelTitle = new javax.swing.JLabel();
        jLabel1CR = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                formMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        jLabelIconCR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelIconCR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/res/unknow.png"))); // NOI18N
        jLabelIconCR.setText(org.openide.util.NbBundle.getMessage(HighlightingPanel.class, "HighlightingPanel.jLabelIconCR.text")); // NOI18N
        jLabelIconCR.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(207, 206, 206), 1, true));

        jLabelUriCR.setText(org.openide.util.NbBundle.getMessage(HighlightingPanel.class, "HighlightingPanel.jLabelUriCR.text")); // NOI18N

        jLabelNameCR.setText(org.openide.util.NbBundle.getMessage(HighlightingPanel.class, "HighlightingPanel.jLabelNameCR.text")); // NOI18N

        jLabelDescription.setText(org.openide.util.NbBundle.getMessage(HighlightingPanel.class, "HighlightingPanel.jLabelDescription.text")); // NOI18N

        jLabelTitle.setForeground(new java.awt.Color(0, 0, 153));
        jLabelTitle.setText(org.openide.util.NbBundle.getMessage(HighlightingPanel.class, "HighlightingPanel.jLabelTitle.text")); // NOI18N

        jLabel1CR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/jaspersoft/ireport/jasperserver/ui/nodes/dnd/arrow_right.png"))); // NOI18N
        jLabel1CR.setText(org.openide.util.NbBundle.getMessage(HighlightingPanel.class, "HighlightingPanel.jLabel1CR.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1CR)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabelIconCR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabelNameCR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabelUriCR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(278, 278, 278))
                    .add(jLabelDescription)
                    .add(layout.createSequentialGroup()
                        .add(jLabelTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                        .add(212, 212, 212)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1CR)
                    .add(jLabelTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabelDescription)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(jLabelNameCR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabelUriCR, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabelIconCR, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked

        fireAction();
    }//GEN-LAST:event_formMouseClicked

    private void formMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseEntered
        //this.setBackground(Color.red);
        over=true;
        repaint();
    }//GEN-LAST:event_formMouseEntered

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        //this.setBackground(originalBG);
        over=false;
        repaint();
    }//GEN-LAST:event_formMouseExited

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        EmptyBorder border = new EmptyBorder(4,4,0,0);
        setBorder(border);
        pressed = true;
        repaint();
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        EmptyBorder border = new EmptyBorder(0,0,4,4);
        setBorder(border);
        pressed = false;
        repaint();
    }//GEN-LAST:event_formMouseReleased


    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (!over && !pressed) return;
        
        RenderingHints ri = ((Graphics2D)g).getRenderingHints();
        Paint p = ((Graphics2D)g).getPaint();

        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Insets insets = new Insets(0,0,0,0);
        if (getBorder() != null)
        {
            insets = getBorder().getBorderInsets(this);
        }
        
        GradientPaint gradient = new GradientPaint(new Point(insets.left,insets.top), new Color(255,255,255,0),
                                                   new Point(insets.left,getSize().height-insets.top - insets.bottom), 
                                                   (pressed) ? new Color(171,217,217) : new Color(191,224,224));

        ((Graphics2D)g).setPaint(gradient);


        ((Graphics2D)g).fillRoundRect(insets.left, insets.top, getSize().width - insets.left - insets.right-1,
                                      getSize().height - insets.top - insets.bottom-1, 20,20);

        ((Graphics2D)g).setPaint(new Color(117,198,217));

        ((Graphics2D)g).drawRoundRect(insets.left, insets.top, getSize().width - insets.left - insets.right-1,
                                      getSize().height - insets.top - insets.bottom-1, 20,20);

        ((Graphics2D)g).setRenderingHints(ri);
        ((Graphics2D)g).setPaint(p);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1CR;
    private javax.swing.JLabel jLabelDescription;
    private javax.swing.JLabel jLabelIconCR;
    private javax.swing.JLabel jLabelNameCR;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelUriCR;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the type
     */
    public int getMode() {
        return mode;
    }

    /**
     * @param type the type to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

}
