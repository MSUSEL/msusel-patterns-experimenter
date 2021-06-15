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
package com.jaspersoft.ireport.designer.editor;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import org.openide.text.CloneableEditorSupport;

/**
 *
 * @author gtoffoli
 */
public class ExpressionEditorPane extends javax.swing.JEditorPane {

    private ExpressionContext expressionContext = null;

    public ExpressionEditorPane()
    {
        this(null);
    }

    public void removeHyperlinkEditorKitListeners()
    {
        KeyListener[] hls = this.getListeners(KeyListener.class);
        for (int i=0; i<hls.length; ++i)
        {
            if (hls[i].getClass().getName().equals("org.netbeans.modules.languages.features.HyperlinkListener"))
            {

                this.removeKeyListener(hls[i]);
            }
        }

        MouseMotionListener[] hls1 = this.getListeners(MouseMotionListener.class);
        for (int i=0; i<hls1.length; ++i)
        {
            if (hls1[i].getClass().getName().equals("org.netbeans.modules.languages.features.HyperlinkListener")) this.removeMouseMotionListener(hls1[i]);
        }

        MouseListener[] hls2 = this.getListeners(MouseListener.class);
        for (int i=0; i<hls2.length; ++i)
        {
            if (hls2[i].getClass().getName().equals("org.netbeans.modules.languages.features.HyperlinkListener")) this.removeMouseListener(hls2[i]);
        }

    }

    public ExpressionEditorPane(ExpressionContext context)
    {
        super();
        this.expressionContext = context;
        EditorKit kit = CloneableEditorSupport.getEditorKit("text/jrxml-expression");
        setEditorKit(kit);
        // List all listeners...
        removeHyperlinkEditorKitListeners();


        addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
                ExpressionContext.setGlobalContext(getExpressionContext());
                ExpressionContext.activeEditor = ExpressionEditorPane.this;
            }

            public void focusLost(FocusEvent e) {
            }
        });
    }
    
    public ExpressionContext getExpressionContext() {
        return expressionContext;
    }

    public void setExpressionContext(ExpressionContext expressionContext) {
        this.expressionContext = expressionContext;
    }
}
