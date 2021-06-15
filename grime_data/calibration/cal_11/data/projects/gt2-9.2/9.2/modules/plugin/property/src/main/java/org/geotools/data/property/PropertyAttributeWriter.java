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
package org.geotools.data.property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.data.AttributeWriter;
import org.geotools.data.DataUtilities;
import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Simple AttributeWriter that produces Java properties files.
 * <p>
 * This AttributeWriter is part of the geotools2 DataStore tutorial, and should
 * be considered a Toy.
 * </p>
 * <p>
 * The content produced witll start with the property "_" with the value being
 * the typeSpec describing the featureType. Thereafter each line will represent
 * a Features with FeatureID as the property and the attribtues as the value
 * separated by | characters.
 * </p>
 * 
 * <pre>
 * <code>
 * _=id:Integer|name:String|geom:Geometry
 * fid1=1|Jody|<i>well known text</i>
 * fid2=2|Brent|<i>well known text</i>
 * fid3=3|Dave|<i>well known text</i>
 * </code>
 * </pre>
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class PropertyAttributeWriter implements AttributeWriter {
    BufferedWriter writer;

    SimpleFeatureType type;

    public PropertyAttributeWriter(File file, SimpleFeatureType featureType)
            throws IOException {
        writer = new BufferedWriter(new FileWriter(file));
        type = featureType;
        writer.write("_=");
        writer.write(DataUtilities.spec(type));
    }

    public int getAttributeCount() {
        return type.getAttributeCount();
    }

    public AttributeDescriptor getAttributeType(int index)
            throws ArrayIndexOutOfBoundsException {
        return type.getDescriptor(index);
    }
    // constructor end
    // next start
    public boolean hasNext() throws IOException {
        return false;
    }

    public void next() throws IOException {
        if (writer == null) {
            throw new IOException("Writer has been closed");
        }
        writer.newLine();
        writer.flush();
    }
    // next end

    // writeFeatureID start
    public void writeFeatureID(String fid) throws IOException {
        if (writer == null) {
            throw new IOException("Writer has been closed");
        }
        writer.write(fid);
    }
    // writeFeatureID end
    
    // write start
    public void write(int position, Object attribute) throws IOException {
        if (writer == null) {
            throw new IOException("Writer has been closed");
        }
        writer.write(position == 0 ? "=" : "|");
        if (attribute == null) {
            writer.write("<null>"); // nothing!
        } else if( attribute instanceof String){
            String txt = encodeString((String) attribute);
            writer.write( txt );
        } else if (attribute instanceof Geometry) {
            Geometry geometry = (Geometry) attribute;
            String txt = geometry.toText();
            
            txt = encodeString( txt );
            writer.write( txt );
        } else {
            String txt = Converters.convert( attribute, String.class );
            if( txt == null ){ // could not convert?
                txt = attribute.toString();
            }
            txt = encodeString( txt );
            writer.write( txt );
        }
    }
    
    /**
     * Used to encode common whitespace characters and | character for safe transport.
     * 
     * @param txt
     * @return txt encoded for storage
     * @see PropertyAttributeReader#decodeString(String)
     */
    String encodeString( String txt ){
        // encode our escaped characters
        // txt = txt.replace("\\", "\\\\");
        txt = txt.replace("|","\\|");

        // encode whitespace constants
        txt = txt.replace("\n", "\\n");
        txt = txt.replace("\r", "\\r");

        return txt;
    }
    // write end

    // close start
    public void close() throws IOException {
        if (writer == null) {
            throw new IOException("Writer has already been closed");
        }
        writer.close();
        writer = null;
        type = null;
    }
    // close end
    // echoLine start
    public void echoLine(String line) throws IOException {
        if (writer == null) {
            throw new IOException("Writer has been closed");
        }
        if (line == null) {
            return;
        }
        writer.write(line);
    }
    // echoLine end
}
