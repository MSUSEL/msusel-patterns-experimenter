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
package com.jaspersoft.ireport.designer.undo;

import java.awt.Rectangle;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sf.jasperreports.engine.design.JRDesignElement;

/**
 *
 * @author gtoffoli
 */
public class ElementTransformationUndoableEdit extends AggregatedUndoableEdit {

    
    private JRDesignElement element = null;
    protected Rectangle oldBounds = null;
    protected Rectangle newBounds = null;
    
    public ElementTransformationUndoableEdit(JRDesignElement element, Rectangle oldBounds)
    {
        this.element = element;
        this.oldBounds = oldBounds;
        this.newBounds = new Rectangle( element.getX(), element.getY(), element.getWidth(), element.getHeight());
    }
    
    @Override
    public void redo() throws CannotRedoException 
    {
        super.redo();
        this.element.setX(  newBounds.x );
        this.element.setY(  newBounds.y );
        this.element.setWidth(  newBounds.width );
        this.element.setHeight(  newBounds.height );
    }

    @Override
    public void undo() throws CannotUndoException
    {
        super.undo();
        this.element.setX(  oldBounds.x );
        this.element.setY(  oldBounds.y );
        this.element.setWidth(  oldBounds.width );
        this.element.setHeight(  oldBounds.height );
    }

    public JRDesignElement getElement() {
        return element;
    }

    public void setElement(JRDesignElement element) {
        this.element = element;
    }
    
    
}
