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
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wcs.v2_0;

import java.util.Map;

import net.opengis.wcs20.ScaleAxisByFactorType;
import net.opengis.wcs20.ScaleAxisType;
import net.opengis.wcs20.ScaleByFactorType;
import net.opengis.wcs20.ScaleToExtentType;
import net.opengis.wcs20.ScaleToSizeType;
import net.opengis.wcs20.ScalingType;
import net.opengis.wcs20.TargetAxisExtentType;
import net.opengis.wcs20.TargetAxisSizeType;
import net.opengis.wcs20.Wcs20Factory;

import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;

/**
 * Parser configuration for the http://www.opengis.net/WCS_service-extension_scaling/1.0 schema.
 * 
 * @generated
 */
public class ScalingConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public ScalingConfiguration() {
        super(Scaling.getInstance());
    }
    
    /**
     * Registers the bindings for the configuration.
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    protected final void registerBindings(Map bindings) {
        // manually setup bindings
        bindings.put(Scaling.ScalingType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScalingType, ScalingType.class));
        bindings.put(Scaling.ScaleByFactorType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleByFactorType, ScaleByFactorType.class));
        bindings.put(Scaling.ScaleAxesByFactorType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleAxesByFactorType, ScaleAxisByFactorType.class));
        bindings.put(Scaling.ScaleAxisType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleAxisType, ScaleAxisType.class));
        bindings.put(Scaling.ScaleToSizeType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleToSizeType, ScaleToSizeType.class));
        bindings.put(Scaling.TargetAxisSizeType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.TargetAxisSizeType, TargetAxisSizeType.class));
        bindings.put(Scaling.ScaleToExtentType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleToExtentType, ScaleToExtentType.class));
        bindings.put(Scaling.TargetAxisExtentType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.TargetAxisExtentType, TargetAxisExtentType.class));
    }

}