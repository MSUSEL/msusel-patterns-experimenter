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
package com.ivata.groupware.container.persistence;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.ivata.mask.util.StringHandling;
import com.ivata.mask.valueobject.ValueObject;

/**
 * <p>
 * This data object class is inherited by all others.
 * </p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since Mar 27, 2004
 * @version $Revision: 1.4 $
 */
public abstract class BaseDO implements Serializable, ValueObject {
    /**
     * <p>
     * Unique identifier of this data object.
     * </p>
     */
    private Integer id;

    /**
     * @see Object
     */
    public boolean equals(final Object compare) {
        return hashCode() == compare.hashCode();
    }

    /**
     * <p>
     * This default implementation simply throws an exception.
     * </p>
     *
     * @see com.ivata.mask.valueobject.ValueObject#getDisplayValue()
     * @throws UnsupportedOperationException always, with a description of the
     *     subclass for which <code>getDisplayValue</code> was not overridden.
     */
    public String getDisplayValue() {
        throw new UnsupportedOperationException(
                "ERROR: getDisplayValue not implemented for "
                + this.getClass());
    }

    /**
     * <p>
     * Override this method to return <code>getIdImpl</code> and set the
     * sequence name for hibernate.
     * </p>
     *
     * @hibernate.id
     *      generator-class = "native"
     * @return current value of the unique identifier of this dependent value
     * object.
     */
    public Integer getId() {
        return id;
    }

    /**
     * <p>
     * Implementation for ivata masks interface <code>ValueObject</code>.
     * </p>
     *
     * <p>
     * Identifies this value object uniquely. This value may only be
     * <code>null</code> for a new value object.
     * </p>
     *
     * @return unique identifier for this value object.
     */
    public final String getIdString() {
        return id == null ? null : id.toString();
    }

    /**
     * @see Object
     */
    public int hashCode() {
        String modifier = getClass().getName();
        int result = 17;
        if (getId() == null) {
            result = 37 * result;
        } else {
            result = 37 * result + (int) getId().hashCode();
        }
        result = 37 * result + (int) modifier.hashCode();
        return result;
    }

    /**
     * <p>Serialize the object from the input stream provided.</p>
     *
     * @param ois the input stream to serialize the object from
     * @throws IOException thrown by
     *  <code>ObjectInputStream.defaultReadObject()</code>.
     * @throws ClassNotFoundException thrown by
     *  <code>ObjectInputStream.defaultReadObject()</code>.
     */
    private void readObject(final ObjectInputStream ois)
            throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

    /**
     * <p>
     * Unique identifier of this data object.
     * </p>
     * @param id current value of the unique identifier of this dependent value
     * object.
     */
    public final void setId(final Integer id) {
        this.id = id;
    }
    /**
     * Set the value as a string. The string must represent a number or a
     * <code>RuntimeException</code> will be thrown.
     *
     * @param idString string representing the id of this object.
     */
    public final void setIdString(final String idString) {
        id = StringHandling.integerValue(idString);
    }


    /**
     * <p>
     * Provide helpful debugging info about this data object.
     * </p>
     *
     * @return clear text describing this object.
     */
    public String toString() {
        String className = getClass().getName();
        // don't output package - just the class name
        if (className.lastIndexOf('.') >= 0) {
            className = className.substring(className.lastIndexOf('.') + 1);
        }
        return  className
            + "(id "
            + getId()
            + ")";
    }

    /**
     * <p>Serialize the object to the output stream provided.</p>
     *
     * @param oos the output stream to serialize the object to
     * @throws IOException thrown by <code>ObjectOutputStream.defaultWriteObject(  )</code>
     */
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }
}