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
package org.geotools.data.wms.xml;

import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.impl.FacetGT;

/**
 * 
 *
 * @source $URL$
 */
public class ogcSimpleTypes {

    protected static class CapabilitiesSectionType extends ogcSimpleType {
        private static SimpleType instance = new CapabilitiesSectionType();
        public static SimpleType getInstance() {
            return instance;
        }
        private static SimpleType[] parents = new SimpleType[]{
                org.geotools.xml.xsi.XSISimpleTypes.String.getInstance()/* simpleType name is string */
        };
        private static Facet[] facets = new Facet[]{
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_ALL),
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_SERVICE),
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_OPERATIONS),
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_CONTENT),
            new FacetGT(Facet.ENUMERATION, GetCapabilitiesRequest.SECTION_COMMON)
        };

        private CapabilitiesSectionType() {
            super("ogc:CapabilitiesSectionType", 4, parents, facets);
        }
        
        
    }

    protected static class FormatType extends ogcSimpleType {
        private static SimpleType instance = new FormatType();
        public static SimpleType getInstance() {
            return instance;
        }
        private static SimpleType[] parents = new SimpleType[]{org.geotools.xml.xsi.XSISimpleTypes.String
                .getInstance()/* simpleType name is string */
        };
        private static Facet[] facets = new Facet[]{
            new FacetGT(Facet.ENUMERATION, "image/gif"),
            new FacetGT(Facet.ENUMERATION, "image/jpg"),
            new FacetGT(Facet.ENUMERATION, "image/png")
        };

        private FormatType() {
            super("FormatType", 4, parents, facets);
        }
    }
    protected static class OWSType extends ogcSimpleType {
        private static SimpleType instance = new OWSType();
        public static SimpleType getInstance() {
            return instance;
        }
        private static SimpleType[] parents = new SimpleType[]{org.geotools.xml.xsi.XSISimpleTypes.String
                .getInstance()/* simpleType name is string */
        };
        private static Facet[] facets = new Facet[]{
            new FacetGT(Facet.ENUMERATION, "WMS")
        };

        private OWSType() {
            super("OWSType", 4, parents, facets);
        }
    }
    protected static class ExceptionsType extends ogcSimpleType {
        private static SimpleType instance = new ExceptionsType();
        public static SimpleType getInstance() {
            return instance;
        }
        private static SimpleType[] parents = new SimpleType[]{org.geotools.xml.xsi.XSISimpleTypes.String
                .getInstance()/* simpleType name is string */
        };
        private static Facet[] facets = new Facet[]{
            new FacetGT(Facet.ENUMERATION, "application/vnd.ogc.se+inimage"),
            new FacetGT(Facet.ENUMERATION, "application/vnd.ogc.se+xml")
        };

        private ExceptionsType() {
            super("ExceptionsType", 4, parents, facets);
        }
        
        
    }
}
