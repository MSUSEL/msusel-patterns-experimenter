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
package org.archive.crawler.settings;

/**
 * A type that holds a Java type.
 *
 * @author John Erik Halse
 *
 */
public class SimpleType extends Type {

    private static final long serialVersionUID = -5134952907004648419L;

    private final String description;
    private Object[] legalValues = null;

    /**
     * Create a new instance of SimpleType.
     *
     * @param name the name of the type.
     * @param description a description suitable for the UI.
     * @param defaultValue the default value for this type. This also set what
     *            kind of Java type that this Type can hold.
     */
    public SimpleType(String name, String description, Object defaultValue) {
        super(name, defaultValue);
        this.description = description;
    }

    /**
     * Create a new instance of SimpleType.
     *
     * @param name the name of the type.
     * @param description a description suitable for the UI.
     * @param defaultValue the default value for this type. This also set what
     *            kind of Java type that this Type can hold.
     * @param legalValues an array of legal values for this simple type. The
     *            objects in this array must be of the same type as the default
     *            value.
     */
    public SimpleType(String name, String description, Object defaultValue,
            Object[] legalValues) {
        this(name, description, defaultValue);
        setLegalValues(legalValues);
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.Type#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.archive.crawler.settings.Type#getDefaultValue()
     */
    public Object getDefaultValue() {
        return getValue();
    }

    /**
     * Get the array of legal values for this Type.
     */
    public Object[] getLegalValues() {
        return legalValues;
    }

    /**
     * Set the array of legal values for this type.
     * <p>
     *
     * The objects in this array must be of the same type as the default value.
     *
     * @param legalValues
     */
    public void setLegalValues(Object[] legalValues) {
        this.legalValues = legalValues;
        if (legalValues != null) {
            addConstraint(new LegalValueListConstraint());
        }
    }
}
