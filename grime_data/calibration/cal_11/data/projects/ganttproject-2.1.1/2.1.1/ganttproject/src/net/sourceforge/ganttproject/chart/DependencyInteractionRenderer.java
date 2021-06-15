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
package net.sourceforge.ganttproject.chart;

import java.awt.Color;
import java.awt.Graphics;

/** Draw arrow between two points */
public class DependencyInteractionRenderer {

    private int x1, x2, y1, y2;

    private boolean draw;

    public DependencyInteractionRenderer() {
        x1 = x2 = y1 = y2 = 0;
        draw = false;
    }

    public DependencyInteractionRenderer(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.draw = true;
    }

    public void setDraw(boolean d) {
        draw = d;
    }

    public boolean getDraw() {
        return draw;
    }

    public void changePoint2(int x2, int y2) {
        this.x2 = x2;
        this.y2 = y2;
    }

    public void paint(Graphics g) {
        if (draw) {
            // draw the line
            g.setColor(Color.black);
            g.drawLine(x1, y1, x2, y2);
            // Draw the triangle
            int xPoints[] = new int[3];
            int yPoints[] = new int[3];
            int vx = x2 - x1;
            int vy = y2 - y1;
            int px = (int) (0.08f * (float) vx);
            int py = (int) (0.08f * (float) vy);
            int total = ((px < 0) ? -px : px) + ((py < 0) ? -py : py);
            px = (int) ((float) px * 10.f / (float) total);
            py = (int) ((float) py * 10.f / (float) total);
            xPoints[0] = x2;
            yPoints[0] = y2;
            xPoints[1] = x2 - px + py / 2;
            yPoints[1] = y2 - py - px / 2;
            xPoints[2] = x2 - px - py / 2;
            yPoints[2] = y2 - py + px / 2;
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }
}