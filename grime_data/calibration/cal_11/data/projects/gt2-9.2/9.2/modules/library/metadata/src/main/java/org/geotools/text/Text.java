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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.text;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.geotools.util.GrowableInternationalString;
import org.geotools.util.MapEntry;
import org.geotools.util.ResourceInternationalString;
import org.geotools.util.SimpleInternationalString;
import org.opengis.util.InternationalString;

/**
 * Helper class for working with InternaionalString and other forms of Text.
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class Text {
    // additional methods needed to register additional
    // properties files at a later time.
    /**
     * Create a international string based on the provided English text.
     * <p>
     * We will hook up this method to a properties file at a later time,
     * making other translations available via the Factory SPI mechanism.
     * 
     * @param english
     * @return SimpleInternationalString
     */
    public static InternationalString text(String english){
        return new SimpleInternationalString( english );
    }
    
    /**
     * Create an international string based on provided key, and resource bundle.
     * 
     * @param key The key for the resource to fetch
     * @param resources The name of the resource bundle, as a fully qualified class name.
     * @return ResourceInternationalString
     */
    public static InternationalString text(String key, String resourceBundle ){
        return new ResourceInternationalString( resourceBundle, key );
    }
    
    public static InternationalString text(String key, Map<String,String> translations ){
        GrowableInternationalString text = new GrowableInternationalString();
        
        for( Entry<String, String> entry : translations.entrySet() ){
            text.add( key, entry.getKey(), entry.getValue() );
        }
        return text;
    }
    
    public static InternationalString text(String key, Properties properties ){
        GrowableInternationalString text = new GrowableInternationalString();
        
        for( Entry<Object, Object> entry : properties.entrySet() ){
            text.add( key, (String) entry.getKey(), (String) entry.getValue() );
        }
        return text;
    }
}
