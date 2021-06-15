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
package org.geotools.styling;


import java.util.HashMap;
import java.util.Map;

import org.opengis.filter.expression.Expression;
import org.opengis.style.StyleVisitor;


/**
 * ExtensioSymbolizer capturing a vendor specific extension.
 * <p>
 * This is a default placeholder to record a vendor specific extension; in case an implementation
 * could not be found on the classpath.
 * 
 * @author James Macgill, CCG
 * @author Johann Sorel (Geomatys)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class VendorSymbolizerImpl extends AbstractSymbolizer implements ExtensionSymbolizer {
    
    private String extensionName;
    private Map<String, Expression> parameters = new HashMap<String, Expression>();

    /**
     * Creates a new instance of DefaultPolygonStyler
     */
    protected VendorSymbolizerImpl() {
    }
    
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((extensionName == null) ? 0 : extensionName.hashCode());
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        VendorSymbolizerImpl other = (VendorSymbolizerImpl) obj;
        if (extensionName == null) {
            if (other.extensionName != null)
                return false;
        } else if (!extensionName.equals(other.extensionName))
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        return true;
    }



    static VendorSymbolizerImpl cast(org.opengis.style.Symbolizer symbolizer) {
        if( symbolizer == null ){
            return null;
        }
        else if (symbolizer instanceof VendorSymbolizerImpl){
            return (VendorSymbolizerImpl) symbolizer;
        }
        else if( symbolizer instanceof org.opengis.style.ExtensionSymbolizer ){
            org.opengis.style.ExtensionSymbolizer extensionSymbolizer = (org.opengis.style.ExtensionSymbolizer) symbolizer;
            VendorSymbolizerImpl copy = new VendorSymbolizerImpl();
            copy.setDescription( extensionSymbolizer.getDescription() );
            copy.setGeometryPropertyName( extensionSymbolizer.getGeometryPropertyName());
            copy.setName(extensionSymbolizer.getName());
            copy.setUnitOfMeasure( extensionSymbolizer.getUnitOfMeasure());
            
            return copy;
        }
        else {
            return null; // not possible
        }
    }

    public String getExtensionName() {
        return extensionName;
    }

    public Map<String, Expression> getParameters() {
        return parameters;
    }

    public void setExtensionName(String name) {
        this.extensionName = name;
    }

    public Object accept(StyleVisitor visitor, Object data) {
        return visitor.visit( this, data );
    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }

}
