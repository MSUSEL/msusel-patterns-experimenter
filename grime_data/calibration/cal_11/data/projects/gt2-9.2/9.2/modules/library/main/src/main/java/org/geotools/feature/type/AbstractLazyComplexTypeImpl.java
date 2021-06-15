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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.feature.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.feature.NameImpl;
import org.opengis.feature.Property;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.util.InternationalString;

/**
 * A replacement for {@link ComplexTypeImpl} with lazy evaluation of descriptors, to support
 * cyclically-defined types. Note that type equality is defined by name, so do not allow different
 * types with the same name to be put in any Collection.
 * 
 * <p>
 * 
 * Inspired by {@link ComplexTypeImpl}.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @see ComplexTypeImpl
 *
 * @source $URL$
 */
public abstract class AbstractLazyComplexTypeImpl extends AbstractLazyAttributeTypeImpl implements
        ComplexType {

    private Collection<PropertyDescriptor> descriptors;

    private Map<Name, PropertyDescriptor> descriptorMap;

    /**
     * Constructor arguments have the same meaning as in {@link ComplexTypeImpl}.
     * 
     * @param name
     * @param identified
     * @param isAbstract
     * @param restrictions
     * @param description
     */
    public AbstractLazyComplexTypeImpl(Name name, boolean identified, boolean isAbstract,
            List<Filter> restrictions, InternationalString description) {
        super(name, Collection.class, identified, isAbstract, restrictions, description);
    }

    /**
     * Subclasses must override this method to return the list of descriptors that define the
     * properties of this type. This method will only be called once at most.
     * 
     * <p>
     * 
     * If the type has no properties, return either an empty collection or null.
     * 
     * @return a collection of descriptors or null if empty
     */
    public abstract Collection<PropertyDescriptor> buildDescriptors();

    /**
     * Check whether descriptors have been built. If not, construct and sanitise them.
     */
    private void requireDescriptors() {
        if (descriptors == null) {
            Collection<PropertyDescriptor> builtDescriptors = buildDescriptors();
            if (builtDescriptors == null) {
                descriptors = Collections.emptyList();
                descriptorMap = Collections.emptyMap();
            } else {
                Collection<PropertyDescriptor> localDescriptors = new ArrayList<PropertyDescriptor>(
                        builtDescriptors);
                Map<Name, PropertyDescriptor> localDescriptorMap = new HashMap<Name, PropertyDescriptor>();
                for (PropertyDescriptor descriptor : localDescriptors) {
                    localDescriptorMap.put(descriptor.getName(), descriptor);
                }
                descriptors = Collections.unmodifiableCollection(localDescriptors);
                descriptorMap = Collections.unmodifiableMap(localDescriptorMap);
            }
        }
    }

    /**
     * @see org.geotools.feature.type.AbstractLazyAttributeTypeImpl#getBinding()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<Collection<Property>> getBinding() {
        return (Class<Collection<Property>>) super.getBinding();
    }

    /**
     * @see org.opengis.feature.type.ComplexType#getDescriptors()
     */
    public Collection<PropertyDescriptor> getDescriptors() {
        requireDescriptors();
        return descriptors;
    }

    /**
     * @see org.opengis.feature.type.ComplexType#getDescriptor(org.opengis.feature.type.Name)
     */
    public PropertyDescriptor getDescriptor(Name name) {
        requireDescriptors();
        return descriptorMap.get(name);
    }

    /**
     * The namespace-ignorant version of {@link #getDescriptor(Name)}. Note that we honour the same
     * permissive algorithm as {@link ComplexTypeImpl}: (1) try no-namespace, (2) try
     * container-namespace, (2) search for match ignoring namespace. <b>*Shudder*</b>
     * 
     * @see org.opengis.feature.type.ComplexType#getDescriptor(java.lang.String)
     * @deprecated Any code that uses this method instead of {@link #getDescriptor(Name)} is
     *             inherently unsafe.
     */
    @Deprecated
    public PropertyDescriptor getDescriptor(String name) {
        requireDescriptors();
        PropertyDescriptor result = getDescriptor(new NameImpl(name));
        if (result == null) {
            result = getDescriptor(new NameImpl(getName().getNamespaceURI(), name));
            if (result == null) {
                for (PropertyDescriptor pd : descriptors) {
                    if (pd.getName().getLocalPart().equals(name)) {
                        return pd;
                    }
                }
            }
        }
        return result;
    }

    /**
     * @see org.opengis.feature.type.ComplexType#isInline()
     */
    public boolean isInline() {
        return false;
    }

    /**
     * @see org.geotools.feature.type.AbstractLazyAttributeTypeImpl#toString()
     */
    @Override
    public String toString() {
        return "LazyComplexType: " + getName();
    }

}
