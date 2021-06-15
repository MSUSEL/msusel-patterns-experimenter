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
package org.geotools.gml3.v3_2;

import javax.xml.namespace.QName;

import org.geotools.gml2.SrsSyntax;
import org.geotools.gml2.bindings.GMLCoordinatesTypeBinding;
import org.geotools.gml3.bindings.AbstractFeatureCollectionTypeBinding;
import org.geotools.gml3.bindings.AbstractFeatureTypeBinding;
import org.geotools.gml3.bindings.AbstractGeometryTypeBinding;
import org.geotools.gml3.bindings.AbstractRingPropertyTypeBinding;
import org.geotools.gml3.bindings.ArcStringTypeBinding;
import org.geotools.gml3.bindings.ArcTypeBinding;
import org.geotools.gml3.bindings.BoundingShapeTypeBinding;
import org.geotools.gml3.bindings.CircleTypeBinding;
import org.geotools.gml3.bindings.ComplexSupportXSAnyTypeBinding;
import org.geotools.gml3.bindings.CurveArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.CurvePropertyTypeBinding;
import org.geotools.gml3.bindings.CurveSegmentArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.CurveTypeBinding;
import org.geotools.gml3.bindings.DirectPositionListTypeBinding;
import org.geotools.gml3.bindings.DirectPositionTypeBinding;
import org.geotools.gml3.bindings.FeatureArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.FeaturePropertyTypeBinding;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.gml3.bindings.GeometryPropertyTypeBinding;
import org.geotools.gml3.bindings.IntegerListBinding;
import org.geotools.gml3.bindings.LineStringSegmentTypeBinding;
import org.geotools.gml3.bindings.LineStringTypeBinding;
import org.geotools.gml3.bindings.LinearRingPropertyTypeBinding;
import org.geotools.gml3.bindings.LocationPropertyTypeBinding;
import org.geotools.gml3.bindings.MeasureTypeBinding;
import org.geotools.gml3.bindings.MultiCurvePropertyTypeBinding;
import org.geotools.gml3.bindings.MultiCurveTypeBinding;
import org.geotools.gml3.bindings.MultiPointPropertyTypeBinding;
import org.geotools.gml3.bindings.MultiPointTypeBinding;
import org.geotools.gml3.bindings.MultiSurfacePropertyTypeBinding;
import org.geotools.gml3.bindings.MultiSurfaceTypeBinding;
import org.geotools.gml3.bindings.PointArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.PointPropertyTypeBinding;
import org.geotools.gml3.bindings.PointTypeBinding;
import org.geotools.gml3.bindings.PolygonPatchTypeBinding;
import org.geotools.gml3.bindings.PolygonTypeBinding;
import org.geotools.gml3.bindings.ReferenceTypeBinding;
import org.geotools.gml3.bindings.RingTypeBinding;
import org.geotools.gml3.bindings.SurfaceArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.SurfacePatchArrayPropertyTypeBinding;
import org.geotools.gml3.bindings.SurfacePropertyTypeBinding;
import org.geotools.gml3.bindings.SurfaceTypeBinding;
import org.geotools.gml3.bindings.TimeInstantPropertyTypeBinding;
import org.geotools.gml3.bindings.TimeInstantTypeBinding;
import org.geotools.gml3.bindings.TimePeriodTypeBinding;
import org.geotools.gml3.bindings.TimePositionTypeBinding;
import org.geotools.gml3.bindings.TimePositionUnionBinding;
import org.geotools.gml3.v3_2.bindings.AbstractRingTypeBinding;
import org.geotools.gml3.v3_2.bindings.DoubleListBinding;
import org.geotools.gml3.v3_2.bindings.EnvelopeTypeBinding;
import org.geotools.gml3.v3_2.bindings.GML32EncodingUtils;
import org.geotools.gml3.v3_2.bindings.LinearRingTypeBinding;
import org.geotools.xml.Configuration;
import org.geotools.xs.XS;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/gml/3.2 schema.
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class GMLConfiguration extends Configuration {

    /**
     * Boolean property which controls whether geometry and envelope objects are encoded with an 
     * srs dimension attribute.
     */
    public static final QName NO_SRS_DIMENSION = org.geotools.gml3.GMLConfiguration.NO_SRS_DIMENSION;

    /**
     * gml3 configuration used to delegate to for configuration
     */
    org.geotools.gml3.GMLConfiguration delegate;

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public GMLConfiguration() {
        this(false);
    }
    
    /**
     * Creates a new configuration specifying whether to enable extended arc/surface support.
     * 
     */  
    public GMLConfiguration(boolean arcSurfaceSupport) {
       super(GML.getInstance());
       delegate = new org.geotools.gml3.GMLConfiguration(arcSurfaceSupport);
       delegate.setSrsSyntax(SrsSyntax.OGC_URN);
    }
    
    /**
     * Flag that when set triggers extended support for arcs and surfaces.
     */
    public void setExtendedArcSurfaceSupport(boolean extArcSurfaceSupport) {
        delegate.setExtendedArcSurfaceSupport(extArcSurfaceSupport);
    }
    
    public boolean isExtendedArcSurfaceSupport() {
        return delegate.isExtendedArcSurfaceSupport();
    }

    /**
     * Sets the syntax to use for encoding srs uris.
     * <p>
     * If this method is not explicitly called {@link SrsSyntax#URN2} is used as the default.
     * </p>
     */
    public void setSrsSyntax(SrsSyntax srsSyntax) {
        delegate.setSrsSyntax(srsSyntax);
    }

    /**
     * Returns the syntax to use for encoding srs uris.
     */
    public SrsSyntax getSrsSyntax() {
        return delegate.getSrsSyntax();
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
      //Types
        container.registerComponentImplementation(GML.AbstractFeatureType,
            AbstractFeatureTypeBinding.class);
        container.registerComponentImplementation(GML.AbstractFeatureCollectionType,
            AbstractFeatureCollectionTypeBinding.class);
        container.registerComponentImplementation(GML.AbstractGeometryType,
            AbstractGeometryTypeBinding.class);
        container.registerComponentImplementation(GML.AbstractRingPropertyType,
            AbstractRingPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.AbstractRingType,
                AbstractRingTypeBinding.class);
        container.registerComponentImplementation(GML.BoundingShapeType,
            BoundingShapeTypeBinding.class);
        container.registerComponentImplementation(GML.CoordinatesType,
            GMLCoordinatesTypeBinding.class);

        container.registerComponentImplementation(GML.CurveArrayPropertyType,
            CurveArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.CurveType, CurveTypeBinding.class);
        container.registerComponentImplementation(GML.CurvePropertyType,
            CurvePropertyTypeBinding.class);
        container.registerComponentImplementation(GML.CurveSegmentArrayPropertyType,
            CurveSegmentArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.DirectPositionListType,
            DirectPositionListTypeBinding.class);
        container.registerComponentImplementation(GML.DirectPositionType,
            DirectPositionTypeBinding.class);
        container.registerComponentImplementation(GML.doubleList, DoubleListBinding.class);
        container.registerComponentImplementation(GML.EnvelopeType, EnvelopeTypeBinding.class);
        container.registerComponentImplementation(GML.FeatureArrayPropertyType,
            FeatureArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.FeaturePropertyType,
            FeaturePropertyTypeBinding.class);
        container.registerComponentImplementation(GML.GeometryPropertyType,
            GeometryPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.integerList, IntegerListBinding.class);
        container.registerComponentImplementation(GML.LinearRingPropertyType,
            LinearRingPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.LinearRingType, LinearRingTypeBinding.class);
       
        container.registerComponentImplementation(GML.LineStringSegmentType,
            LineStringSegmentTypeBinding.class);
        container.registerComponentImplementation(GML.LineStringType, LineStringTypeBinding.class);
        container.registerComponentImplementation(GML.LocationPropertyType,
            LocationPropertyTypeBinding.class);

        container.registerComponentImplementation(GML.MeasureType, MeasureTypeBinding.class);
        container.registerComponentImplementation(GML.MultiCurveType, MultiCurveTypeBinding.class);
        container.registerComponentImplementation(GML.MultiCurvePropertyType,
            MultiCurvePropertyTypeBinding.class);
      
        container.registerComponentImplementation(GML.MultiPointPropertyType,
            MultiPointPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.MultiPointType, MultiPointTypeBinding.class);
      
        container.registerComponentImplementation(GML.MultiSurfaceType,
            MultiSurfaceTypeBinding.class);
        container.registerComponentImplementation(GML.MultiSurfacePropertyType,
            MultiSurfacePropertyTypeBinding.class);
        container.registerComponentImplementation(GML.PointArrayPropertyType,
            PointArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.PointPropertyType,
            PointPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.PointType, PointTypeBinding.class);
      
        container.registerComponentImplementation(GML.PolygonType, PolygonTypeBinding.class);
        container.registerComponentImplementation(GML.ReferenceType, ReferenceTypeBinding.class);
        container.registerComponentImplementation(GML.SurfaceArrayPropertyType,
            SurfaceArrayPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.SurfacePropertyType,
            SurfacePropertyTypeBinding.class);
        container.registerComponentImplementation(XS.ANYTYPE, ComplexSupportXSAnyTypeBinding.class);
       
        container.registerComponentImplementation(GML.TimeInstantType, TimeInstantTypeBinding.class);
        container.registerComponentImplementation(GML.TimeInstantPropertyType, TimeInstantPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.TimePeriodType, TimePeriodTypeBinding.class);
        container.registerComponentImplementation(GML.TimePositionType, TimePositionTypeBinding.class);
        container.registerComponentImplementation(GML.TimePositionUnion, TimePositionUnionBinding.class);
        
        //extended bindings for arc/surface support
        if (isExtendedArcSurfaceSupport()) {
            container.registerComponentImplementation(GML.ArcStringType,
                    ArcStringTypeBinding.class);
            container.registerComponentImplementation(GML.ArcType,
                    ArcTypeBinding.class);
            container.registerComponentImplementation(GML.CircleType,
                    CircleTypeBinding.class);
            container.registerComponentImplementation(GML.PolygonPatchType,
                    PolygonPatchTypeBinding.class);
            container.registerComponentImplementation(GML.RingType, RingTypeBinding.class);
            container.registerComponentImplementation(GML.SurfacePatchArrayPropertyType,
                    SurfacePatchArrayPropertyTypeBinding.class);
            container.registerComponentImplementation(GML.SurfaceType, SurfaceTypeBinding.class);
            container.registerComponentImplementation(GML.CurveArrayPropertyType, 
                    org.geotools.gml3.bindings.ext.CurveArrayPropertyTypeBinding.class);
            container.registerComponentImplementation(GML.CurvePropertyType, 
                    org.geotools.gml3.bindings.ext.CurvePropertyTypeBinding.class);
            container.registerComponentImplementation(GML.CurveType, 
                    org.geotools.gml3.bindings.ext.CurveTypeBinding.class);
            container.registerComponentImplementation(GML.MultiCurveType, 
                    org.geotools.gml3.bindings.ext.MultiCurveTypeBinding.class);
            //container.registerComponentImplementation(GML.MultiPolygonType, 
            //        org.geotools.gml3.bindings.ext.MultiPolygonTypeBinding.class);
            container.registerComponentImplementation(GML.MultiSurfaceType, 
                    org.geotools.gml3.bindings.ext.MultiSurfaceTypeBinding.class);
            container.registerComponentImplementation(GML.PolygonPatchType, 
                    org.geotools.gml3.bindings.ext.PolygonPatchTypeBinding.class);
            container.registerComponentImplementation(GML.SurfaceArrayPropertyType, 
                    org.geotools.gml3.bindings.ext.SurfaceArrayPropertyTypeBinding.class);
            container.registerComponentImplementation(GML.SurfacePatchArrayPropertyType, 
                    org.geotools.gml3.bindings.ext.SurfacePatchArrayPropertyTypeBinding.class);
            container.registerComponentImplementation(GML.SurfacePropertyType, 
                    org.geotools.gml3.bindings.ext.SurfacePropertyTypeBinding.class);
            container.registerComponentImplementation(GML.SurfaceType, 
                    org.geotools.gml3.bindings.ext.SurfaceTypeBinding.class);
        }
    }
    
    @Override
    protected void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        delegate.configureContext(container);
        container.unregisterComponent(GML3EncodingUtils.class);
        container.registerComponentInstance(new GML32EncodingUtils());
    }
} 
