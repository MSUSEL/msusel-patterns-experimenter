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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wcs;

import java.util.Map;

import javax.xml.namespace.QName;

import net.opengis.wcs10.Wcs10Factory;

import org.eclipse.emf.ecore.EFactory;
import org.geotools.gml4wcs.GMLConfiguration;
import org.geotools.wcs.bindings.AbstractDescriptionBaseTypeBinding;
import org.geotools.wcs.bindings.AbstractDescriptionTypeBinding;
import org.geotools.wcs.bindings.AxisDescriptionTypeBinding;
import org.geotools.wcs.bindings.CapabilitiesSectionTypeBinding;
import org.geotools.wcs.bindings.InterpolationMethodTypeBinding;
import org.geotools.wcs.bindings.LonLatEnvelopeBaseTypeBinding;
import org.geotools.wcs.bindings.LonLatEnvelopeTypeBinding;
import org.geotools.wcs.bindings.RangeSubsetType_axisSubsetBinding;
import org.geotools.wcs.bindings.TimePeriodTypeBinding;
import org.geotools.wcs.bindings.TimeSequenceTypeBinding;
import org.geotools.wcs.bindings.TypedLiteralTypeBinding;
import org.geotools.wcs.bindings.ValueEnumBaseTypeBinding;
import org.geotools.wcs.bindings.ValueEnumTypeBinding;
import org.geotools.wcs.bindings.ValueRangeTypeBinding;
import org.geotools.wcs.bindings._axisDescriptionBinding;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/wcs schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class WCSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public WCSConfiguration() {
       super(WCS.getInstance());
       
       addDependency(new GMLConfiguration());
    }
    
    @Override
    protected void registerBindings(Map bindings) {
        super.registerBindings(bindings);
        
        final EFactory wcsFactory = Wcs10Factory.eINSTANCE;
        register(bindings, wcsFactory, WCS._GetCapabilities);
        register(bindings, wcsFactory, WCS._DescribeCoverage);
        register(bindings, wcsFactory, WCS._GetCoverage);

        bindings.put(WCS._axisDescription, new _axisDescriptionBinding());

        bindings.put(WCS.AbstractDescriptionBaseType, new AbstractDescriptionBaseTypeBinding());
        bindings.put(WCS.AbstractDescriptionType, new AbstractDescriptionTypeBinding());
        
        register(bindings, wcsFactory, WCS.DomainSubsetType);
        register(bindings, wcsFactory, WCS.SpatialSubsetType);

        register(bindings, wcsFactory, WCS.RangeSetType);
        register(bindings, wcsFactory, WCS.RangeSubsetType);
        bindings.put(WCS.RangeSubsetType_axisSubset, new RangeSubsetType_axisSubsetBinding());
        bindings.put(WCS.AxisDescriptionType, new AxisDescriptionTypeBinding());
        bindings.put(WCS.TypedLiteralType, new TypedLiteralTypeBinding());
        
        bindings.put(WCS.valueEnumBaseType, new ValueEnumBaseTypeBinding());
        bindings.put(WCS.valueEnumType, new ValueEnumTypeBinding());
        bindings.put(WCS.valueRangeType, new ValueRangeTypeBinding());

        register(bindings, wcsFactory, WCS.OutputType);
        register(bindings, wcsFactory, WCS.SupportedCRSsType);
        register(bindings, wcsFactory, WCS.SupportedFormatsType);
        register(bindings, wcsFactory, WCS.SupportedInterpolationsType);
        bindings.put(WCS.InterpolationMethodType, new InterpolationMethodTypeBinding());

        register(bindings, wcsFactory, WCS.DCPTypeType);
        register(bindings, wcsFactory, WCS.DCPTypeType_HTTP);

        bindings.put(WCS.CapabilitiesSectionType, new CapabilitiesSectionTypeBinding());
//        register(bindings, wcsFactory, WCS.WCS_CapabilitiesType);
//        register(bindings, wcsFactory, WCS.WCSCapabilityType);
//        register(bindings, wcsFactory, WCS.WCSCapabilityType_Exception);
//        register(bindings, wcsFactory, WCS.WCSCapabilityType_Request);
//        register(bindings, wcsFactory, WCS.WCSCapabilityType_VendorSpecificCapabilities);

        bindings.put(WCS.LonLatEnvelopeBaseType, new LonLatEnvelopeBaseTypeBinding());
        bindings.put(WCS.LonLatEnvelopeType, new LonLatEnvelopeTypeBinding());
        bindings.put(WCS.TimePeriodType, new TimePeriodTypeBinding());
        bindings.put(WCS.TimeSequenceType, new TimeSequenceTypeBinding());
    }
    
    private void register(Map bindings, EFactory factory, QName qname) {
        bindings.put(qname, new ComplexEMFBinding(factory, qname));
    }

    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Wcs10Factory.eINSTANCE);
    }
} 
