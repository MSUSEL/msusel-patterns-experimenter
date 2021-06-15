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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EFactory;
import org.geotools.xml.impl.InstanceBinding;

/**
 * A binding implementation which parses / encodes objects from an EMF model.
 * <p>
 * This binding implementation uses EMF reflection to implement all methods of 
 * the api. All that is needed is the 'target' of the binding. 
 * </p>
 * <p>
 * These bindings are "instance" bindings in that they are instantiated before
 * the parser is run (see {@link Configuration#registerBindings(java.util.Map)}) 
 * and not at runtime.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 * @see Configuration#registerBindings(java.util.Map)
 *
 *
 *
 * @source $URL$
 */
public class ComplexEMFBinding extends AbstractComplexEMFBinding
    implements InstanceBinding {

    /**
     * The name of the element or type of the binding.
     */
    QName target;
    
    /**
     * Creates the binding.
     * 
     * @param factory The factory for the emf model.
     * @param target The qualified name of the type in the emf model that this
     * binding works against. 
     */
    public ComplexEMFBinding( EFactory factory, QName target ) {
        super( factory );
        this.target = target;
    }
    
    /**
     * Creates the binding specifying the type of the object its boiund to.
     * <p>
     * The type is specified in cases where it can not be inferred from the 
     * qname alone. Such cases occur when EMF runs into a name clash.
     * </p>
     * 
     * @param factory The factory for the emf model.
     * @param target The qualified name of the type in the emf model that this
     * binding works against. 
     * @param type The type of the object this binding is mapped to.
     */
    public ComplexEMFBinding( EFactory factory, QName target, Class type ) {
        super( factory, type );
        this.target = target;
    }
    
    public QName getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "ComplexEMFBinding [target=" + target + ", factory=" + factory + ", type=" + type
                + "]";
    }

    
}
