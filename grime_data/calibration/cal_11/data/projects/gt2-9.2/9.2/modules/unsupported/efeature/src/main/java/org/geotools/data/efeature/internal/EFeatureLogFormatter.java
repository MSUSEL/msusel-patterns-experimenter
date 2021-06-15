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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.efeature.internal;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * @author kengu - 6. juni 2011
 *
 *
 * @source $URL$
 */
public class EFeatureLogFormatter extends Formatter {
    
    private static boolean bMinimal = false;
    private static SimpleFormatter eStandard = new SimpleFormatter();

    
    
    @Override
    public String format(LogRecord record) {
        //
        // Apply minimal format?
        //
        if(bMinimal) {
            //
            // Create a StringBuffer to contain the 
            // formatted record start with the date.
            //
            StringBuffer sb = new StringBuffer();
//            //
//            // Get the date from the LogRecord and add it to the buffer
//            //
//            Date date = new Date(record.getMillis());
//            sb.append(date.toString());
//            sb.append(" ");
            //
            // Get the level name and add it to the buffer
            //
            sb.append(record.getLevel().getName());
            sb.append(": ");
            //
            // Get the formatted message (includes localization 
            // and substitution of parameters) and add it to the buffer
            //
            sb.append(formatMessage(record));
            sb.append("\n");
            //
            // Finished
            //
            return sb.toString();
        }
        //
        // Apply standard format
        //
        return eStandard.format(record);
    }
    
    public static final void setMinimal() {
        bMinimal = true;        
    }
    
    public static final void setStandard() {
        bMinimal = false;        
    }
    

}
