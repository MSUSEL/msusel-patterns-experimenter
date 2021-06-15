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
package org.geotools.process.factory;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * Grabbed from Geotools and generalized a bit, should go back into GeoTools once improved enough.
 * ProcessFactory for classes exposing simple processes as static methods
 * 
 * @since 2.7
 *
 * @source $URL$
 */
public class StaticMethodsProcessFactory<T> extends AnnotationDrivenProcessFactory {
    Class<T> targetClass;

    public StaticMethodsProcessFactory(InternationalString title, String namespace,
            Class<T> targetClass) {
        super(title, namespace);
        this.targetClass = targetClass;
    }

    /**
     * Finds the DescribeProcess description for the specified name
     * 
     * @param name
     * @return
     */
    protected DescribeProcess getProcessDescription(Name name) {
        Method method = method(name.getLocalPart());
        if (method == null) {
            return null;
        }
        DescribeProcess info = method.getAnnotation(DescribeProcess.class);
        return info;
    }

    public Method method(String name) {
        for (Method method : targetClass.getMethods()) {
            if (name.equalsIgnoreCase(method.getName())) {
                DescribeProcess dp = method.getAnnotation(DescribeProcess.class);
                if (dp != null) {
                    return method;
                }
            }
        }
        return null;
    }

    public Set<Name> getNames() {
        // look for the methods that have the DescribeProcess annotation. use
        // a linkedHashSet to make sure we don't report duplicate names
        Set<Name> names = new LinkedHashSet<Name>();
        for (Method method : targetClass.getMethods()) {
            DescribeProcess dp = method.getAnnotation(DescribeProcess.class);
            if (dp != null) {
                Name name = new NameImpl(namespace, method.getName());
                if (names.contains(name)) {
                    throw new IllegalStateException(targetClass.getName()
                            + " has two methods named " + method.getName()
                            + ", both annotated with DescribeProcess, this is an ambiguity. "
                            + "Please a different name");
                }
                names.add(name);
            }
        }
        return names;
    }

    @Override
    protected Object createProcessBean(Name name) {
        // they are all static methods
        return null;
    }

}
