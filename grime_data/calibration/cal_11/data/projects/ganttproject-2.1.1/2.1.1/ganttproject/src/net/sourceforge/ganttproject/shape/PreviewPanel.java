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
package net.sourceforge.ganttproject.shape;

/*
 *@author Etienne L'kenfack (etienne.lkenfack@itcogita.com)
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class PreviewPanel extends JPanel {
    protected ShapePaint pattern = ShapeConstants.DEFAULT;

    public PreviewPanel() {
        setOpaque(true);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createTitledBorder("Preview"), BorderFactory
                .createEmptyBorder(0, 4, 8, 4)));
        setPreferredSize(new Dimension(70, 70));
    }

    public ShapePaint getPattern() {
        return new ShapePaint(pattern, getForeground(), getBackground());
    }

    public void setPattern(ShapePaint pattern) {
        this.pattern = pattern;
    }

    public void paintComponent(Graphics gc) {
        Graphics2D g = (Graphics2D) gc;
        int w = getSize().width;
        int h = getSize().height;
        g.setColor(getParent().getBackground());
        g.fillRect(0, 0, w, h);
        if (pattern == null)
            return;
        Insets insets = getInsets();
        Rectangle rect = new Rectangle(insets.left, insets.top, w
                - (insets.left + insets.right), h
                - (insets.top + insets.bottom));
        g.setPaint(new ShapePaint(pattern, getForeground(), getBackground()));
        g.fill(rect);

    }
}
