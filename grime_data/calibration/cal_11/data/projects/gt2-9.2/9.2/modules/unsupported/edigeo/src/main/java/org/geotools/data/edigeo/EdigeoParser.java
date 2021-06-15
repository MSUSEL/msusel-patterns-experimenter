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
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * 
 *
 * @source $URL$
 */
public class EdigeoParser {
	
    public BufferedReader reader = null; 
    public String line = ""; // Current line buffer
    public int lineNumber = 0;  // not sure to be useful?
    
    /**
     * to comment....
     *
     */
    public EdigeoParser(File file) throws FileNotFoundException {  
    	super();
        reader = new BufferedReader(new FileReader(file));
    }
    
    /**
     * Stores the next non-null line from file buffer in the line buffer
     *
     * @return True if a non-null string was read, false if EOF or error
     */
    public boolean readLine() {
        String buffer = "";

        do {
            try {
                buffer = reader.readLine();
               
                if (buffer == null) {
                    return readLine(""); //EOF
                }
            } catch (IOException e) {
                return readLine("");
            }

            lineNumber++;
            buffer = buffer.trim();
        } while (buffer.length() == 0);

        return readLine(buffer);
    }

    /**
     * "Reads" a line from the given line, and initializes the token.
     *
     * @param line
     *
     * @return true if could read a non empty line (i.e. line != "")
     */
    public boolean readLine(String line) {

        if (line == null) {
            this.line = "";
        } else {
            this.line = ltrim(line);
        }

        return (!line.equals(""));
    }

    // Can't use String.trim() when Delimiter is \t
    // TODO use stringBuffer and a better algorithm
    public static String ltrim(String untrimmed) {
        while ((untrimmed.length() > 0) && (untrimmed.charAt(0) == ' ')) {
            untrimmed = untrimmed.substring(1);
        }

        return untrimmed;
    }
    
    /**
     * Gets value of the specified descriptor
     * 
     * @param target Descriptor
     * @return String
     */
    public String getValue(String target) {
        int index = line.indexOf(target);
        int nbchar = Integer.parseInt(line.substring(index + 5, index + 7));
        if (index + nbchar + 8 > line.length()) {
            return "";
        }
        String value = line.substring(index + 8, index + nbchar + 8);
        return value;
    }
    
    
    /**
     * Closes the associated reader.
     */
    public void close() {
        try {
            reader.close();
            reader = null ;
        } catch (IOException e) {
        }
    }
    
}
