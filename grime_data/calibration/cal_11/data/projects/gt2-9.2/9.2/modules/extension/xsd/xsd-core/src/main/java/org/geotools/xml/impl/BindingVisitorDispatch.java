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
package org.geotools.xml.impl;

import org.eclipse.xsd.XSDFeature;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xml.impl.BindingWalker.Visitor;
import org.opengis.feature.ComplexAttribute;
import org.picocontainer.MutablePicoContainer;

/**
 * Methods for the dispatch of binding visitors that first check for type mismatches between binding
 * Java types and instance types.
 * 
 * <p>
 * 
 * If a mismatched biding is found for a complex attribute, the binding for xs:anyType is visited.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 *
 *
 * @source $URL$
 */
public class BindingVisitorDispatch {

    /**
     * This is a static method class, not to be instantiated.
     */
    private BindingVisitorDispatch() {
    }

    public static void walk(Object object, BindingWalker bindingWalker, XSDFeature component,
            Visitor visitor, MutablePicoContainer context) {
        walk(object, bindingWalker, component, visitor, null, context);
    }

    public static void walk(Object object, BindingWalker bindingWalker, XSDFeature component,
            Visitor visitor, XSDTypeDefinition container, MutablePicoContainer context) {
        // do not test simple bindings as they are often mismatched and rely on converters 
        if (object instanceof ComplexAttribute) {
            MismatchedBindingFinder finder = new MismatchedBindingFinder(object);
            bindingWalker.walk(component, finder, container, context);
            if (finder.foundMismatchedBinding()) {
                // if a mismatched binding is found, just visit xs:anyType binding
                visitor.visit(bindingWalker.getAnyTypeBinding());
                return;
            }
        }
        bindingWalker.walk(component, visitor, container, context);
    }

}
