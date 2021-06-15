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
package com.jaspersoft.ireport.designer.actions;


import com.jaspersoft.ireport.designer.widgets.JRDesignElementWidget;
import com.jaspersoft.ireport.designer.widgets.SelectionWidget;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.InplaceEditorProvider.EditorController;
import org.netbeans.api.visual.action.InplaceEditorProvider.ExpansionDirection;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.EnumSet;
import javax.swing.border.LineBorder;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignTextElement;

/**
 * This class derives from the David Kaspar's implemenation. Unfortunately that class is final.
 * The new implementation is used to modify the textfield for based on the widget...
 * @author Giulio Toffoli
 */
public class ReportTextElementInplaceEditorProvider implements InplaceEditorProvider<JTextArea> {

    private TextFieldInplaceEditor editor;
    private EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections;

    private KeyListener keyListener;
    private FocusListener focusListener;
    private DocumentListener documentListener;

    public ReportTextElementInplaceEditorProvider (TextFieldInplaceEditor editor) {
        this.editor = editor;
        this.expansionDirections = null;
    }

    public JTextArea createEditorComponent (EditorController controller, Widget widget) {
        if (! editor.isEnabled (widget))
            return null;
        JTextArea field = new JTextArea (editor.getText (widget));
        field.setBorder(new LineBorder(Color.GRAY, 1));
        field.selectAll ();
        
        Font font = getElementFont(widget);
        if (font != null)
        {
            field.setFont(font);
        }
        
        field.setAlignmentY(  getElementHorizontalAlignment(widget)  );
        field.setAlignmentX(  getElementVerticalAlignment(widget)  );
        return field;
    }

    private Font getElementFont(Widget widget)
    {
        
        JRDesignTextElement element = getTextElement(widget);
        Scene scene = widget.getScene();
        if (element != null)
        {
           int style = Font.PLAIN;
           if (element.isBold()) style |= Font.BOLD;
           if (element.isItalic()) style |= Font.ITALIC;
           
           float size = ((float) element.getFontSize() * (float) scene.getZoomFactor());
           if (size < 5f) size = 5f;
           
           Font textFont = new Font(element.getFontName(), style, (int)size);
           textFont = textFont.deriveFont(size);
           return textFont;
        }
        
        if (scene.getZoomFactor() > 1.0)
        {
            Font font = scene.getDefaultFont();
            font = font.deriveFont((float) (font.getSize2D() * scene.getZoomFactor()));
        }
        return null;
    }
    
    private float getElementHorizontalAlignment(Widget widget)
    {
        JRDesignTextElement element = getTextElement(widget);
        if (element != null)
        {
            switch (element.getHorizontalAlignment())
            {
                    case JRAlignment.HORIZONTAL_ALIGN_CENTER:
                        return JTextArea.CENTER_ALIGNMENT;
                    case JRAlignment.HORIZONTAL_ALIGN_LEFT:
                        return JTextArea.LEFT_ALIGNMENT;
                    case JRAlignment.HORIZONTAL_ALIGN_RIGHT:
                        return JTextArea.RIGHT_ALIGNMENT;
                    //case JRAlignment.HORIZONTAL_ALIGN_JUSTIFIED:
                    //    return JTextArea.;
            }
        }
        return JTextArea.LEFT_ALIGNMENT;
    }
    
    private float getElementVerticalAlignment(Widget widget)
    {
        JRDesignTextElement element = getTextElement(widget);
        if (element != null)
        {
            switch (element.getHorizontalAlignment())
            {
                    case JRAlignment.VERTICAL_ALIGN_MIDDLE:
                        return JTextArea.CENTER_ALIGNMENT;
                    case JRAlignment.VERTICAL_ALIGN_TOP:
                        return JTextArea.TOP_ALIGNMENT;
                    case JRAlignment.VERTICAL_ALIGN_BOTTOM:
                        return JTextArea.BOTTOM_ALIGNMENT;
                    //case JRAlignment.VERTICAL_ALIGN_JUSTIFIED:
                    //    return JTextArea.;
            }
        }
        return JTextArea.TOP_ALIGNMENT;
    }
    
    private JRDesignTextElement getTextElement(Widget widget) {
        
        if (widget instanceof JRDesignElementWidget)
        {
            JRDesignElement element = ((JRDesignElementWidget)widget).getElement();
            if (element instanceof JRDesignTextElement) return (JRDesignTextElement) element;
        }
        else if (widget instanceof SelectionWidget)
        {
            return getTextElement( ((SelectionWidget)widget).getRealWidget() );
        }
        return null;
    }
    
    

    public void notifyOpened (final EditorController controller, Widget widget, JTextArea editor) {
        editor.setMinimumSize (new Dimension (64, 19));
        keyListener = new KeyAdapter() {
            public void keyPressed (KeyEvent e) {
                switch (e.getKeyChar ()) {
                    case KeyEvent.VK_ESCAPE:
                        e.consume ();
                        controller.closeEditor (false);
                        break;
                    case KeyEvent.VK_ENTER:
                        if (e.isMetaDown() || e.isAltDown())
                        {
                            e.setModifiers(0);
                        }
                        else
                        {
                            e.consume ();
                            controller.closeEditor (true);
                        }
                        break;
                }
            }
        };
        focusListener = new FocusAdapter() {
            public void focusLost (FocusEvent e) {
                controller.closeEditor (true);
            }
        };
        documentListener = new DocumentListener () {
            public void insertUpdate (DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged ();
            }

            public void removeUpdate (DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged ();
            }

            public void changedUpdate (DocumentEvent e) {
                controller.notifyEditorComponentBoundsChanged ();
            }
        };
        editor.addKeyListener (keyListener);
        editor.addFocusListener (focusListener);
        editor.getDocument ().addDocumentListener (documentListener);
        editor.selectAll ();
    }

    public void notifyClosing (EditorController controller, Widget widget, JTextArea editor, boolean commit) {
        editor.getDocument ().removeDocumentListener (documentListener);
        editor.removeFocusListener (focusListener);
        editor.removeKeyListener (keyListener);
        if (commit) {
            this.editor.setText (widget, editor.getText ());
            if (widget != null)
                widget.getScene ().validate ();
        }
    }

    public Rectangle getInitialEditorComponentBounds(EditorController controller, Widget widget, JTextArea editor, Rectangle viewBounds) {
        return null;
    }

    public EnumSet<ExpansionDirection> getExpansionDirections (EditorController controller, Widget widget, JTextArea editor) {
        return expansionDirections;
    }

}
