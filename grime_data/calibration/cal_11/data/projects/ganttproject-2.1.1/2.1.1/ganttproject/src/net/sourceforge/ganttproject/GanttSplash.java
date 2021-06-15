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
/***************************************************************************
 GanttSplash.java  -  description
 -------------------
 begin                : dec 2002
 copyright            : (C) 2002 by Thomas Alexandre
 email                : alexthomas(at)ganttproject.org
 ***************************************************************************/

/***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/

package net.sourceforge.ganttproject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.sourceforge.ganttproject.font.Fonts;
import net.sourceforge.ganttproject.gui.DialogAligner;
import net.sourceforge.ganttproject.util.TextLengthCalculatorImpl;

/**
 * Class to put a splash before lunch the soft
 */

public class GanttSplash extends JFrame {

    private JLabel mySplashComponent;

    public GanttSplash() {
        super("GanttProject Start");

        ImageIcon splashImage = new ImageIcon(getClass().getResource(
                "/icons/splash.png"));
        mySplashComponent = new JLabel(splashImage) {
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                Font font = Fonts.SPLASH_FONT;
                g2.setFont(font);
                
                int textLength = TextLengthCalculatorImpl.getTextLength(g,
                        GanttProject.version);
                drawTextWithShadow(g2, GPVersion.V2_0_X, (int) (getSize().getWidth()
                        - textLength - 9), 287);
                drawTextWithShadow(g2, "ganttproject.biz", 40, 287);
            }
        };
    }

    private void drawTextWithShadow(Graphics2D graphics, String text, int xpos, int ypos) {
        graphics.setColor(Color.white);
        graphics.drawString(text, xpos , ypos);
    }
    public void setVisible(boolean b) {
        if (b) {
            getContentPane().add(mySplashComponent, BorderLayout.CENTER);
            pack();
            DialogAligner.center(this);
        }
        super.setVisible(b);
    }

    protected void frameInit() {
        super.frameInit();
        ImageIcon icon = new ImageIcon(getClass().getResource(
                "/icons/ganttproject.png"));
        setIconImage(icon.getImage()); // set the ganttproject icon
        setUndecorated(true);
    }

    public void close() {
        setVisible(false);
        dispose();
    }

    public JLabel getSplashComponent() {
        return mySplashComponent;
    }
}
