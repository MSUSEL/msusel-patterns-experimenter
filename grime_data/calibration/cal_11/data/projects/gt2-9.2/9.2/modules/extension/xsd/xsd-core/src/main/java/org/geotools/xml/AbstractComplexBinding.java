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
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xml;

import org.eclipse.xsd.XSDElementDeclaration;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;
import javax.xml.namespace.QName;


/**
 * Base class for complex bindings.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class AbstractComplexBinding implements ComplexBinding {
    /**
     * Does nothing, subclasses should override this method.
     */
    public void initializeChildContext(ElementInstance childInstance, Node node,
        MutablePicoContainer context) {
        //does nothing, subclasses should override
    }

    /**
     * Does nothing, subclasses should override this method.
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
        //does nothing, subclasses should override
    }

    /**
     * This implementation returns {@link Binding#OVERRIDE}.
     * <p>
     * Subclasses should override to change this behaviour.
     * </p>
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * Subclasses should override this method, the default implementation
     * return <code>null</code>.
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return null;
    }

    /**
     * Subclasses should ovverride this method if need be, the default implementation
     * returns <param>value</param>.
     *
     * @see ComplexBinding#encode(Object, Document, Element).
     */
    public Element encode(Object object, Document document, Element value)
        throws Exception {
        return value;
    }

    /**
     * Subclasses should override this method if need be, the default implementation
     * returns <code>null</code>.
     *
     * @see ComplexBinding#getProperty(Object, QName)
     */
    public Object getProperty(Object object, QName name)
        throws Exception {
        //do nothing, subclasses should override
        return null;
    }

    /**
     * Subclasses should override this method if need be, the default implementation
     * returns <code>null</code>.
     * <p>
     * Note that this method only needs to be implemented for schema types which
     * are open-ended in which the contents are not specifically specified by
     * the schema.
     * </p>
     *
     * @see ComplexBinding#getProperties(Object)
     * @deprecated use {@link #getProperties(Object, XSDElementDeclaration)}
     */
    public List getProperties(Object object) throws Exception {
        // do nothing, subclasses should override.
        return null;
    }
    
    /**
     * Subclasses should override this method if need be, the default implementation
     * returns <code>null</code>.
     * <p>
     * Note that this method only needs to be implemented for schema types which
     * are open-ended in which the contents are not specifically specified by
     * the schema.
     * </p>
     *
     * @see ComplexBinding#getProperties(Object)
     */
    public List getProperties(Object object, XSDElementDeclaration element)
            throws Exception {
        // do nothing, subclasses should override
        return null;
    }
}
