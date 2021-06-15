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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.SimpleType;

/**
 * 
 *
 * @source $URL$
 */
public class OGCSchema implements Schema {
    
    private static OGCSchema instance = new OGCSchema();
    
    protected OGCSchema() {
        //do nothing
    }
    
    public static OGCSchema getInstance() {
        return instance;
    }

    public static final URI NAMESPACE = loadNS();
    private static URI loadNS() {
        try {
            return new URI("http://www.opengis.net/ows");
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public int getBlockDefault() {
        return 0;
    }

    public int getFinalDefault() {
        return 0;
    }

    public String getId() {
        return "null";
    }
    private static Schema[] imports = null;

    public Schema[] getImports() {
        if (imports == null) {
            imports = new Schema[]{
                    GMLSchema.getInstance()
            };
        }
        return imports;
    }

    public String getPrefix() {
        return "ogc";
    }

    public URI getTargetNamespace() {
        return NAMESPACE;
    }

    public URI getURI() {
        return NAMESPACE;
    }

    public String getVersion() {
        return "null";
    }

    public boolean includesURI( URI uri ) {
        // // TODO fill me in!
        return false; // // safer
    }

    public boolean isAttributeFormDefault() {
        return false;
    }

    public boolean isElementFormDefault() {
        return false;
    }

    public AttributeGroup[] getAttributeGroups() {
        return null;
    }
    public Attribute[] getAttributes() {
        return null;
    }
    /**
     * TODO comment here
     */
    private static ComplexType[] complexTypes = null;
    public ComplexType[] getComplexTypes() {
        if (complexTypes == null) {
            complexTypes = new ComplexType[]{ogcComplexTypes.VendorType.getInstance()};
        }
        return complexTypes;
    }
    /**
     * TODO comment here
     */
    private static Element[] elements = null;
    public static final int GET_CAPABILITIES = 0;
    public static final int GET_MAP = 1;
    public static final int GET_FEATURE_INFO = 2;
    public Element[] getElements() {
        if (elements == null) {
            elements = new Element[3];
            elements[GET_CAPABILITIES] = new ogcElement("GetCapabilities",ogcComplexTypes._GetCapabilities.getInstance(),null,1,1);
            elements[GET_MAP] = new ogcElement("GetMap", ogcComplexTypes._GetMap.getInstance(), null, 1, 1);
            elements[GET_FEATURE_INFO] = new ogcElement("ogc:GetFeatureInfo", ogcComplexTypes._GetFeatureInfo.getInstance(), null, 1, 1);
        }
        return elements;
    }
    public Group[] getGroups() {
        return null;
    }
    /**
     * TODO comment here
     */
    private static SimpleType[] simpleTypes = null;
    public SimpleType[] getSimpleTypes() {
        if (simpleTypes == null) {
            simpleTypes = new SimpleType[]{
                    ogcSimpleTypes.CapabilitiesSectionType.getInstance(),
                    ogcSimpleTypes.FormatType.getInstance(),
                    ogcSimpleTypes.OWSType.getInstance(),
                    ogcSimpleTypes.ExceptionsType.getInstance()};
        }
        return simpleTypes;
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     */
    public Map getImplementationHints() {
        return Collections.EMPTY_MAP;
    }
}
