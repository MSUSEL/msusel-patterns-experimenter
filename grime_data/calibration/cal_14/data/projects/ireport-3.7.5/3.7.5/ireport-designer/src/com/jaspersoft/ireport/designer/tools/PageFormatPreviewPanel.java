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
package com.jaspersoft.ireport.designer.tools;

import com.jaspersoft.ireport.designer.ReportObjectScene;
import com.jaspersoft.ireport.designer.utils.RoundGradientPaint;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author gtoffoli
 */
public class PageFormatPreviewPanel extends javax.swing.JPanel {
    private int pageWidth = 0;
    private int pageHeight = 0;
    private int marginTop = 0;
    private int marginLeft = 0;
    private int marginBottom = 0;
    private int marginRight = 0;
    private int columns = 0;
    private int columnWidth = 0;
    private int columnSpace = 0;



    

    /** Creates new form PageFormatPreview */
    public PageFormatPreviewPanel() {
        initComponents();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        try {

            if (getPageHeight() == 0 || getPageWidth() == 0) return;
            double wRatio = (this.getWidth() - 20)/(double)getPageWidth();
            double hRatio = (this.getHeight() - 20)/(double)getPageHeight();

            double ratio = Math.min(wRatio, hRatio);

            int leftGap = (int)((getWidth() - getPageWidth()*ratio) / 2);
            int topGap = (int)((getHeight() - getPageHeight()*ratio) / 2);

            Graphics2D g2 = (Graphics2D)g;

            paintPageShadow(g, leftGap-10,topGap-10, getPageWidth()*ratio+20, getPageHeight()*ratio+20);

            g2.setColor(Color.WHITE);
            GradientPaint gp = new GradientPaint(
                 leftGap+10,topGap+10, Color.WHITE,
                 (float)(getPageWidth()*ratio),(float)(getPageHeight()*ratio),  new Color(208,218,229)); //

            g2.setPaint(gp);
            g2.fill( new Rectangle2D.Double(leftGap,topGap, getPageWidth()*ratio, getPageHeight()*ratio));

            g2.setColor(ReportObjectScene.DESIGN_LINE_COLOR);
            g2.draw( new Rectangle2D.Double(leftGap,topGap, getPageWidth()*ratio, getPageHeight()*ratio));

            g2.draw( new Line2D.Double(leftGap+getMarginLeft()*ratio , topGap, leftGap+getMarginLeft()*ratio, getHeight()-topGap));
            g2.draw( new Line2D.Double(leftGap+(getPageWidth()-getMarginRight())*ratio ,topGap, leftGap+(getPageWidth()-getMarginRight())*ratio, getHeight()-topGap));

            g2.draw( new Line2D.Double(leftGap, topGap+getMarginTop()*ratio , getWidth()-leftGap, topGap+getMarginTop()*ratio));
            g2.draw( new Line2D.Double(leftGap, getHeight() - topGap - getMarginBottom()*ratio , getWidth()-leftGap, getHeight() - topGap - getMarginBottom()*ratio));

            // Columns...
            if (getColumns() > 1)
            {
                int x = getMarginLeft();
                for (int i=0; i<getColumns(); ++i)
                {
                    x += getColumnWidth();
                    g2.draw( new Line2D.Double(leftGap+x*ratio , topGap, leftGap+x*ratio, getHeight()-topGap));
                    if (i < getColumns()-1)
                    {
                        x += getColumnSpace();
                        g2.draw( new Line2D.Double(leftGap+x*ratio , topGap, leftGap+x*ratio, getHeight()-topGap));
                    }
                }
            }

        } catch (Exception ex) { }
    }

    public void paintPageShadow(Graphics g, double x, double y, double width, double height)
    {
        // TOP ______________________________________________
        Rectangle2D r = new Rectangle2D.Double(x+10, y, width-20, 10);
        GradientPaint gp = new GradientPaint(
                 0f, (float)(y+2), new Color(0,0,0,0),
                 0f, (float)(y+9.5),  new Color(0,0,0,60)); //


        ((Graphics2D)g).setPaint(gp);
        ((Graphics2D)g).fill(r);

        // BOTTOM ______________________________________________
        r = new Rectangle2D.Double(x+10, y+height-10, width-20, 10);
        gp = new GradientPaint(
                 0f, (float)(r.getY()), new Color(0,0,0,60),
                 0f, (float)(r.getY()+7.5),  new Color(0,0,0,0)); //
        ((Graphics2D)g).setPaint(gp);
        ((Graphics2D)g).fill(r);

        // LEFT ______________________________________________
        r = new Rectangle2D.Double(x, y+10, 10, height-20);
        gp = new GradientPaint(
                 (float)(r.getX()+2),0f,  new Color(0,0,0,0),
                 (float)(r.getX()+9.5), 0f, new Color(0,0,0,60)); //
        ((Graphics2D)g).setPaint(gp);
        ((Graphics2D)g).fill(r);

        // RIGHT ______________________________________________
        r = new Rectangle2D.Double(x+width-10, y+10, 10, height-20);
        gp = new GradientPaint(
                 (float)(r.getX()),0f,  new Color(0,0,0,60),
                 (float)(r.getX()+7.5), 0f, new Color(0,0,0,0)); //
        ((Graphics2D)g).setPaint(gp);
        ((Graphics2D)g).fill(r);

        // TOP LEFT ______________________________________________
        r = new Rectangle2D.Double(x, y, 10, 10);
        RoundGradientPaint rgp = new RoundGradientPaint(x+9.8f, y+9.8f, new Color(0,0,0,60),
                new Point2D.Float(0,6.8f), new Color(0,0,0,0));

        ((Graphics2D)g).setPaint(rgp);
        ((Graphics2D)g).fill(r);

        // TOP RIGHT ______________________________________________
        r = new Rectangle2D.Double(x+width-10, y, 10, 10);
        rgp = new RoundGradientPaint(r.getX()+0.5, r.getY()+9.5f, new Color(0,0,0,60),
                new Point2D.Float(0,6.5f), new Color(0,0,0,0));

        ((Graphics2D)g).setPaint(rgp);
        ((Graphics2D)g).fill(r);

         // BOTTOM RIGHT ______________________________________________
        r = new Rectangle2D.Double(x+width-10,  y+height-10, 10, 10);
        rgp = new RoundGradientPaint(r.getX()+0.5, r.getY()+0.5f, new Color(0,0,0,60),
                new Point2D.Float(0,6.5f), new Color(0,0,0,0));

        ((Graphics2D)g).setPaint(rgp);
        ((Graphics2D)g).fill(r);

        r = new Rectangle2D.Double(x,  y+height-10, 10, 10);
        rgp = new RoundGradientPaint(r.getX()+9.5, r.getY()+0.5f, new Color(0,0,0,60),
                new Point2D.Float(0,6.5f), new Color(0,0,0,0));

        ((Graphics2D)g).setPaint(rgp);
        ((Graphics2D)g).fill(r);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @return the pageWidth
     */
    protected int getPageWidth() {
        return pageWidth;
    }

    /**
     * @param pageWidth the pageWidth to set
     */
    protected void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    /**
     * @return the pageHeight
     */
    protected int getPageHeight() {
        return pageHeight;
    }

    /**
     * @param pageHeight the pageHeight to set
     */
    protected void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    /**
     * @return the marginTop
     */
    protected int getMarginTop() {
        return marginTop;
    }

    /**
     * @param marginTop the marginTop to set
     */
    protected void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    /**
     * @return the marginLeft
     */
    protected int getMarginLeft() {
        return marginLeft;
    }

    /**
     * @param marginLeft the marginLeft to set
     */
    protected void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    /**
     * @return the marginBottom
     */
    protected int getMarginBottom() {
        return marginBottom;
    }

    /**
     * @param marginBottom the marginBottom to set
     */
    protected void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    /**
     * @return the marginRight
     */
    protected int getMarginRight() {
        return marginRight;
    }

    /**
     * @param marginRight the marginRight to set
     */
    protected void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    /**
     * @return the columns
     */
    protected int getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    protected void setColumns(int columns) {
        this.columns = columns;
    }

    /**
     * @return the columnWidth
     */
    protected int getColumnWidth() {
        return columnWidth;
    }

    /**
     * @param columnWidth the columnWidth to set
     */
    protected void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    /**
     * @return the columnSpace
     */
    protected int getColumnSpace() {
        return columnSpace;
    }

    /**
     * @param columnSpace the columnSpace to set
     */
    protected void setColumnSpace(int columnSpace) {
        this.columnSpace = columnSpace;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
