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
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.v1_1;

import java.util.Set;
import javax.xml.namespace.QName;

import org.geotools.se.v1_1.SE;
import org.geotools.xml.XSD;

/**
 * This interface contains the qualified names of all the types,elements, and 
 * attributes in the http://www.opengis.net/sld schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public final class SLD extends XSD {

    /** singleton instance */
    private static final SLD instance = new SLD();
    
    /**
     * Returns the singleton instance.
     */
    public static final SLD getInstance() {
       return instance;
    }
    
    /**
     * private constructor
     */
    private SLD() {
    }
    
    protected void addDependencies(Set dependencies) {
        dependencies.add(SE.getInstance());
    }
    
    /**
     * Returns 'http://www.opengis.net/sld'.
     */
    public String getNamespaceURI() {
       return NAMESPACE;
    }
    
    /**
     * Returns the location of 'all.xsd.'.
     */
    public String getSchemaLocation() {
       return getClass().getResource("StyledLayerDescriptor.xsd").toString();
       //return getClass().getResource("all.xsd").toString();
    }
    
    /** @generated */
    public static final String NAMESPACE = "http://www.opengis.net/sld";
    
    /* Type Definitions */
    /** @generated */
    public static final QName DescribeLayerResponseType = 
        new QName("http://www.opengis.net/sld","DescribeLayerResponseType");
    /** @generated */
    public static final QName ElevationType = 
        new QName("http://www.opengis.net/sld","ElevationType");
    /** @generated */
    public static final QName ExceptionsType = 
        new QName("http://www.opengis.net/sld","ExceptionsType");
    /** @generated */
    public static final QName GetMapType = 
        new QName("http://www.opengis.net/sld","GetMapType");
    /** @generated */
    public static final QName IntervalType = 
        new QName("http://www.opengis.net/sld","IntervalType");
    /** @generated */
    public static final QName LayerDescriptionType = 
        new QName("http://www.opengis.net/sld","LayerDescriptionType");
    /** @generated */
    public static final QName OutputType = 
        new QName("http://www.opengis.net/sld","OutputType");
    /** @generated */
    public static final QName owsTypeType = 
        new QName("http://www.opengis.net/sld","owsTypeType");
    /** @generated */
    public static final QName TypeNameType = 
        new QName("http://www.opengis.net/sld","TypeNameType");
    /** @generated */
    public static final QName _CoverageConstraint = 
        new QName("http://www.opengis.net/sld","_CoverageConstraint");
    /** @generated */
    public static final QName _CoverageExtent = 
        new QName("http://www.opengis.net/sld","_CoverageExtent");
    /** @generated */
    public static final QName _Extent = 
        new QName("http://www.opengis.net/sld","_Extent");
    /** @generated */
    public static final QName _FeatureTypeConstraint = 
        new QName("http://www.opengis.net/sld","_FeatureTypeConstraint");
    /** @generated */
    public static final QName _InlineFeature = 
        new QName("http://www.opengis.net/sld","_InlineFeature");
    /** @generated */
    public static final QName _LayerCoverageConstraints = 
        new QName("http://www.opengis.net/sld","_LayerCoverageConstraints");
    /** @generated */
    public static final QName _LayerFeatureConstraints = 
        new QName("http://www.opengis.net/sld","_LayerFeatureConstraints");
    /** @generated */
    public static final QName _NamedLayer = 
        new QName("http://www.opengis.net/sld","_NamedLayer");
    /** @generated */
    public static final QName _NamedStyle = 
        new QName("http://www.opengis.net/sld","_NamedStyle");
    /** @generated */
    public static final QName _RangeAxis = 
        new QName("http://www.opengis.net/sld","_RangeAxis");
    /** @generated */
    public static final QName _RemoteOWS = 
        new QName("http://www.opengis.net/sld","_RemoteOWS");
    /** @generated */
    public static final QName _Service = 
        new QName("http://www.opengis.net/sld","_Service");
    /** @generated */
    public static final QName _StyledLayerDescriptor = 
        new QName("http://www.opengis.net/sld","_StyledLayerDescriptor");
    /** @generated */
    public static final QName _UserLayer = 
        new QName("http://www.opengis.net/sld","_UserLayer");
    /** @generated */
    public static final QName _UserStyle = 
        new QName("http://www.opengis.net/sld","_UserStyle");
    /** @generated */
    public static final QName _UseSLDLibrary = 
        new QName("http://www.opengis.net/sld","_UseSLDLibrary");
    /** @generated */
    public static final QName OutputType_Size = 
        new QName("http://www.opengis.net/sld","OutputType_Size");

    /* Elements */
    /** @generated */
    public static final QName CoverageConstraint = 
        new QName("http://www.opengis.net/sld","CoverageConstraint");
    /** @generated */
    public static final QName CoverageExtent = 
        new QName("http://www.opengis.net/sld","CoverageExtent");
    /** @generated */
    public static final QName DescribeLayerResponse = 
        new QName("http://www.opengis.net/sld","DescribeLayerResponse");
    /** @generated */
    public static final QName Extent = 
        new QName("http://www.opengis.net/sld","Extent");
    /** @generated */
    public static final QName FeatureTypeConstraint = 
        new QName("http://www.opengis.net/sld","FeatureTypeConstraint");
    /** @generated */
    public static final QName GetMap = 
        new QName("http://www.opengis.net/sld","GetMap");
    /** @generated */
    public static final QName InlineFeature = 
        new QName("http://www.opengis.net/sld","InlineFeature");
    /** @generated */
    public static final QName IsDefault = 
        new QName("http://www.opengis.net/sld","IsDefault");
    /** @generated */
    public static final QName LayerCoverageConstraints = 
        new QName("http://www.opengis.net/sld","LayerCoverageConstraints");
    /** @generated */
    public static final QName LayerFeatureConstraints = 
        new QName("http://www.opengis.net/sld","LayerFeatureConstraints");
    /** @generated */
    public static final QName NamedLayer = 
        new QName("http://www.opengis.net/sld","NamedLayer");
    /** @generated */
    public static final QName NamedStyle = 
        new QName("http://www.opengis.net/sld","NamedStyle");
    /** @generated */
    public static final QName RangeAxis = 
        new QName("http://www.opengis.net/sld","RangeAxis");
    /** @generated */
    public static final QName RemoteOWS = 
        new QName("http://www.opengis.net/sld","RemoteOWS");
    /** @generated */
    public static final QName Service = 
        new QName("http://www.opengis.net/sld","Service");
    /** @generated */
    public static final QName StyledLayerDescriptor = 
        new QName("http://www.opengis.net/sld","StyledLayerDescriptor");
    /** @generated */
    public static final QName TimePeriod = 
        new QName("http://www.opengis.net/sld","TimePeriod");
    /** @generated */
    public static final QName UserLayer = 
        new QName("http://www.opengis.net/sld","UserLayer");
    /** @generated */
    public static final QName UserStyle = 
        new QName("http://www.opengis.net/sld","UserStyle");
    /** @generated */
    public static final QName UseSLDLibrary = 
        new QName("http://www.opengis.net/sld","UseSLDLibrary");
    /** @generated */
    public static final QName Value = 
        new QName("http://www.opengis.net/sld","Value");

    /* Attributes */

}
    
