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
 * Created on 26.12.2004
 */
package net.sourceforge.ganttproject.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * @author bard
 */
public class TextLengthCalculatorImpl implements TextLengthCalculator {
    private Graphics2D myGraphics;

    private State myState;

    public static int getTextLength(Graphics g, String text) {
        if(text.length() == 0)
            return 0;
        Graphics2D g2 = (Graphics2D) g;
        FontRenderContext frc = g2.getFontRenderContext();
        Font font = g.getFont();
        TextLayout layout = new TextLayout(text, font, frc);
        Rectangle2D bounds = layout.getBounds();
        return (int) bounds.getWidth() + 1;
    }

    public TextLengthCalculatorImpl(Graphics g) {
        if (g != null) {
            setGraphics(g);
        }
    }

    public void setGraphics(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        myGraphics = g2;
        myState = null;
    }

    public int getTextLength(String text) {
        return getTextLength(myGraphics, text);
    }

    public Object getState() {
        if (myState == null) {
            myState = new State(myGraphics.getFontRenderContext(), myGraphics
                    .getFont());
        }
        return myState;
    }

    static class State {
        Object context;

        Object font;

        State(Object context, Object font) {
            this.context = context;
            this.font = font;
            if (context == null) {
                throw new NullPointerException();
            }
            if (font == null) {
                throw new NullPointerException();
            }
        }

        public boolean equals(Object o) {
            State rvalue = (State) o;
            if (rvalue == null) {
                return false;
            }
            return rvalue.context.equals(this.context)
                    && rvalue.font.equals(this.font);
        }

        public int hashCode() {
            return font.hashCode();
        }
    }

}
