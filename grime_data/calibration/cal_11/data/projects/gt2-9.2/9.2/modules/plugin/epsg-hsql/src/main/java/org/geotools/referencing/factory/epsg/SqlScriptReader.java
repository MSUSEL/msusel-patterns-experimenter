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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reads a multiline SQL file extracting each command separately. Skips empty lines, assumes
 * comments start with "--" and are on their own line 
 *
 *
 *
 * @source $URL$
 */
public class SqlScriptReader {
    boolean fetched = true;
    StringBuilder builder = new StringBuilder();
    BufferedReader reader;

    public SqlScriptReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }
    
    public boolean hasNext() throws IOException {
        // do we have an un-fetched command?
        if(!fetched) {
            return builder.length() > 0;
        }
        
        builder.setLength(0);
        String line = null;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            // skip empty and comment lines
            if(!"".equals(line) && !line.startsWith("--"))
                builder.append(line).append("\n");
            if(line.endsWith(";")) {
                fetched = false;
                break;
            }
        }
        
        if(line == null && builder.length() > 0) {
            throw new IOException("The file ends with a non ; terminated command");
        }
        
        return line != null;
    }
    
    public String next() throws IOException  {
        if(fetched)
            throw new IOException("hasNext was not called, or was called and it returned false");
            
        fetched = true;
        return builder.toString();
    }
    
    public void dispose() {
        try {
            reader.close();
        } catch(IOException e) {
            // never mind
        }
    }
    

}
