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
package com.jaspersoft.ireport.designer.editor.functions;

import com.jaspersoft.ireport.designer.editor.ExpObjectCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @version $Id: FunctionAndExpObjectCellRenderer.java 0 2010-08-10 13:23:17 CET gtoffoli $
 * @author Giulio Toffoli (giulio@jaspersoft.com)
 *
 */
public class FunctionAndExpObjectCellRenderer extends ExpObjectCellRenderer {

    private Color selectionBackground;
    private Color background;

    // Create a style object and then set the style attributes
    Style methodStyle = null;
    Style returnTypeStyle = null;

    public FunctionAndExpObjectCellRenderer(JList list) {
        super();
        selectionBackground = list.getSelectionBackground();
        background = list.getBackground();
        StyledDocument doc = new DefaultStyledDocument();
        this.setDocument( doc );
        methodStyle = doc.addStyle("methodStyle", null);
        StyleConstants.setBold(methodStyle, true);
        returnTypeStyle = doc.addStyle("returnType", null);
        StyleConstants.setForeground(returnTypeStyle, Color.gray);
    }


    @Override
    public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, object, index, isSelected, cellHasFocus);
    
        updateText(object, isSelected);

        return this;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        updateText(value, isSelected);
        
        return this;
    }

    private void updateText(Object object, boolean isSelected)
    {
        if (object instanceof Function)
        {
            Function f = (Function)object;

            String s = f.getName() + "(";

            List<Parameter> params = f.getParameters();
            boolean first= true;
            for (Parameter p : params)
            {
                if (!first) s += ", ";
                first = false;
                s += p.getLabel();
            }
            s += ") " +  f.getReturnType();

            this.setText("");
            StyledDocument doc = (StyledDocument)this.getDocument();
            try {
            doc.insertString(doc.getLength(), s.substring(0, s.indexOf("(")), methodStyle);
            doc.insertString(doc.getLength(), s.substring(s.indexOf("("), s.lastIndexOf(")")+1), null);
            doc.insertString(doc.getLength(), s.substring(s.lastIndexOf(")")+1), returnTypeStyle);
            } catch (Exception ex){}
            setBackground(isSelected ? selectionBackground : background);
        }
    }



}
