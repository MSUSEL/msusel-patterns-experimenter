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

import java.lang.reflect.Method;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author gtoffoli
 */
public class ObjectPropertyUndoableEdit extends AggregatedUndoableEdit {

    private Object object;
    private String property;
    private Object oldValue;
    private Object newValue;
    private Class  propertyClass;
    
    /**
     * 
     * @param object It is the object on which call the set method
     * @param property The name of the method (without the initial "set", i.e. Name for setName, or Xyz for setXyz)
     * @param propertyClass The argument of the method. If the argument is primitive, specify the right type (i.e. Integer.TYPE)
     * @param oldValue The oldValue (used by the undo operation)
     * @param newValueThe oldValue (must be the current value of the attribute, it will be used for a redo operation)
     */
    public ObjectPropertyUndoableEdit(Object object, String property, Class propertyClass, Object oldValue, Object newValue)
    {
        super();
        this.object = object;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.propertyClass = propertyClass;

        setPresentationName(getProperty() + " of " + getObject());
    }
    
    @Override
    public void undo() throws CannotUndoException {
        
        super.undo();
        try {
            Method m = getObject().getClass().getMethod("set" + getProperty(),getPropertyClass());
            m.invoke(getObject(), new Object[]{getOldValue()});
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CannotUndoException();
        }
    }

    @Override
    public void redo() throws CannotRedoException {
        
        super.redo();
        try {
            Method m = getObject().getClass().getMethod("set" + getProperty(),getPropertyClass());
            m.invoke(getObject(), new Object[]{getNewValue()});
        } catch (Exception ex) {
            throw new CannotUndoException();
        }
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        this.oldValue = oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    public Class getPropertyClass() {
        return propertyClass;
    }

    public void setPropertyClass(Class propertyClass) {
        this.propertyClass = propertyClass;
    }

}
