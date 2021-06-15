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
package org.geotools.filter.v1_0.capabilities;

import org.picocontainer.MutablePicoContainer;
import org.opengis.filter.FilterFactory;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.xml.Configuration;


/**
 * Parser configuration for the http://www.opengis.net/ogc schema.
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class OGCConfiguration extends Configuration {
    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public OGCConfiguration() {
        super(OGC.getInstance());
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(OGC.Arithmetic_OperatorsType,
            Arithmetic_OperatorsTypeBinding.class);
        container.registerComponentImplementation(OGC.Comparison_OperatorsType,
            Comparison_OperatorsTypeBinding.class);
        container.registerComponentImplementation(OGC.Function_NamesType,
            Function_NamesTypeBinding.class);
        container.registerComponentImplementation(OGC.Function_NameType,
            Function_NameTypeBinding.class);
        container.registerComponentImplementation(OGC.FunctionsType, FunctionsTypeBinding.class);
        container.registerComponentImplementation(OGC.Scalar_CapabilitiesType,
            Scalar_CapabilitiesTypeBinding.class);
        container.registerComponentImplementation(OGC.Spatial_CapabilitiesType,
            Spatial_CapabilitiesTypeBinding.class);
        container.registerComponentImplementation(OGC.Spatial_OperatorsType,
            Spatial_OperatorsTypeBinding.class);
        //container.registerComponentImplementation(OGC._BBOX,_BBOXBinding.class);
        //container.registerComponentImplementation(OGC._Between,_BetweenBinding.class);
        //container.registerComponentImplementation(OGC._Beyond,_BeyondBinding.class);
        //container.registerComponentImplementation(OGC._Contains,_ContainsBinding.class);
        //container.registerComponentImplementation(OGC._Crosses,_CrossesBinding.class);
        //container.registerComponentImplementation(OGC._Disjoint,_DisjointBinding.class);
        //container.registerComponentImplementation(OGC._DWithin,_DWithinBinding.class);
        //container.registerComponentImplementation(OGC._Equals,_EqualsBinding.class);
        container.registerComponentImplementation(OGC._Filter_Capabilities,
            _Filter_CapabilitiesBinding.class);

        //container.registerComponentImplementation(OGC._Intersect,_IntersectBinding.class);
        //container.registerComponentImplementation(OGC._Like,_LikeBinding.class);
        //container.registerComponentImplementation(OGC._Logical_Operators,_Logical_OperatorsBinding.class);
        //container.registerComponentImplementation(OGC._NullCheck,_NullCheckBinding.class);
        //container.registerComponentImplementation(OGC._Overlaps,_OverlapsBinding.class);
        //container.registerComponentImplementation(OGC._Simple_Arithmetic,_Simple_ArithmeticBinding.class);
        //container.registerComponentImplementation(OGC._Simple_Comparisons,_Simple_ComparisonsBinding.class);
        //container.registerComponentImplementation(OGC._Touches,_TouchesBinding.class);
        //container.registerComponentImplementation(OGC._Within,_WithinBinding.class);
    }

    protected void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentImplementation(FilterFactory.class, FilterFactoryImpl.class);
    }
}
