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
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.util.CheckedArrayList;

/**
 * 
 * @author Gabriel Roldan (Axios Engineering)
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 *
 *
 *
 * @source $URL$
 * @since 2.4
 */
public class TypeMapping implements Serializable {

    private static final long serialVersionUID = 1444252634598922057L;

    private String sourceDataStore;

    private String sourceTypeName;
  
    private String itemXpath;

    /**
      * True if we don't want to create a new feature, but want to add attributes to the feature
      * returned from the backend Data access.
      */
    private boolean isXmlDataStore;
  
    private String targetElementName;

    private List attributeMappings = Collections.EMPTY_LIST;

    /**
     * Optional unique identifier for a FeatureTypeMapping, useful for multiple mappings of the
     * same type. 
     */
    private String mappingName;

    public TypeMapping() {
        // no-op
    }

    public List getAttributeMappings() {
        return new ArrayList(attributeMappings);
    }

    public void setAttributeMappings(List attributeMappings) {
        this.attributeMappings = new CheckedArrayList(AttributeMapping.class);
        if (attributeMappings != null) {
            this.attributeMappings.addAll(attributeMappings);
        }
    }

    public String getSourceDataStore() {
        return sourceDataStore;
    }

    public void setSourceDataStore(String sourceDataStore) {
        this.sourceDataStore = sourceDataStore;
    }

    public String getSourceTypeName() {
        return sourceTypeName;
    }

    public void setSourceTypeName(String sourceTypeName) {
        this.sourceTypeName = sourceTypeName;
    }

    public String getTargetElementName() {
        return targetElementName;
    }

    public void setTargetElementName(String targetElementName) {
        this.targetElementName = targetElementName;
    }
    
    public String getItemXpath() {
        return itemXpath;
    }

    public void setItemXpath(String itemXpath) {
        this.itemXpath = itemXpath;
    }

    public void setXmlDataStore(String isXmlDataStore) {
        this.isXmlDataStore = Boolean.valueOf(isXmlDataStore).booleanValue();
    }

    public boolean isXmlDataStore() {
        return isXmlDataStore;
    } 
    
    public void setMappingName(final String mappingName) {
        this.mappingName = mappingName;   
    }
    
    public String getMappingName() {
        return mappingName;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("TypeMappingDTO[");
        if (mappingName != null) {
            sb.append("mappingName=").append(mappingName).append(",\n ");
        }
        sb.append("sourceDataStore=").append(sourceDataStore).append(
                ",\n sourceTypeName=").append(sourceTypeName).append(",\n targetElementName=")
                .append(targetElementName).append(",\n attributeMappings=").append(
                        attributeMappings).append("]");
        return sb.toString();

    }

}
