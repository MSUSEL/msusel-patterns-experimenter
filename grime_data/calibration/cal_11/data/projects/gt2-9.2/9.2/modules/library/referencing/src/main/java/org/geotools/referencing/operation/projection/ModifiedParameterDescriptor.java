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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.projection;

import java.util.Collection;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.referencing.operation.MathTransformProvider;  // For javadoc


/**
 * A parameter descriptor identical to the supplied one except for the
 * default value. The constructor expects a model created by one of the
 * {@linkplain MathTransformProvider#createDescriptor(Identifier[],double,double,double,Unit)
 * provider methods}, usually using some neutral default value. For example the base class for
 * map projection providers defines a set of
 * {@linkplain org.geotools.referencing.operation.projection.MapProjection.Provider#SEMI_MAJOR
 * commonly used parameter descriptors}. However some map projections are specific to a
 * particular area (for example the {@linkplain NewZealandMapGrid New Zealand map grid}
 * and may wish to override the neutral default values with some default value appropriate
 * for that area.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class ModifiedParameterDescriptor extends DefaultParameterDescriptor {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = -616587615222354457L;

    /**
     * The original parameter descriptor. Used for comparaisons purpose only.
     */
    private final ParameterDescriptor original;

    /**
     * The new default value.
     */
    private final Double defaultValue;

    /**
     * Creates a parameter descriptor wrapping the specified one with the specified
     * default value.
     */
    public ModifiedParameterDescriptor(final ParameterDescriptor original,
                                       final double defaultValue)
    {
        super(original);
        this.original     = original;
        this.defaultValue = Double.valueOf(defaultValue);
    }

    /**
     * Returns the default value for the parameter.
     */
    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns {@code true} if the specified collection contains the specified descriptor. Invoking
     * this method is similar to invoking {@code set.contains(descriptor)}, except that instance of
     * {@link ModifiedParameterDescriptor} are unwrapped to their original descriptor. The drawback
     * is that this method is slower than {@code set.contains(descriptor)}, so it should be invoked
     * only if the former fails.
     */
    public static boolean contains(final Collection<GeneralParameterDescriptor> set,
                                   ParameterDescriptor descriptor)
    {
        if (descriptor instanceof ModifiedParameterDescriptor) {
            descriptor = ((ModifiedParameterDescriptor) descriptor).original;
        }
        for (GeneralParameterDescriptor candidate : set) {
            if (candidate instanceof ModifiedParameterDescriptor) {
                candidate = ((ModifiedParameterDescriptor) candidate).original;
            }
            if (descriptor.equals(candidate)) {
                return true;
            }
        }
        assert !set.contains(descriptor);
        return false;
    }
}
