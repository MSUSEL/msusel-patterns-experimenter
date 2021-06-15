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

import java.io.File;
import java.io.FileNotFoundException;

/**
 *
 * @author mcoudert
 *
 *
 *
 *
 * @source $URL$
 */
public class  EdigeoFileFactory {
       
    /**
     * 
     */
    public EdigeoFileFactory() {
    }
    
    
    /**
     * Check the path name of the EDIGEO file, looking for specified extension file if it's not.
     *
     * @param path The full path of the specified extension file (with or without extension)
     * @param ext The extension file to check
     *
     * @throws FileNotFoundException
     */
    public static File setFile(String path, String ext, boolean mustExist)
        throws FileNotFoundException {
        
    	File file = new File(path);

        if (file.isDirectory()) {
        	throw new FileNotFoundException(path + " is a directory");
        }
        
        if (file.exists()) {
            return file;
        }
     
        String fName=getEdigeofName(file.getName(),ext);
        file = file.getParentFile(); 
        file = getFileHandler(file, fName, ext, mustExist);
          
        return file;
    }

    /**
     * Returns the name of a specified file without extension
     *
     * @param fName The file name, possibly with an extension
     * 
     * @pparam ext The expected file extension
     *
     * @return The name with no extension
     *
     * @throws FileNotFoundException if extension was other than expected
     */
    private static String getEdigeofName(String fName, String ext)
        throws FileNotFoundException {
        int fext = fName.lastIndexOf(".");

        if (fext > 0) {
            String theExt = fName.substring(fext + 1).toLowerCase();

            if (!(theExt.equals(ext))) {
                throw new FileNotFoundException(
                    "Please specify a ."+ext+" file extension.");
            }

            fName = fName.substring(0, fext);
        }

        return fName;
    }

    /**
     * Utility function for initFiles - returns a File given a parent path, the
     * file name without extension and the extension Tests different extension
     * case for case-sensitive filesystems
     *
     * @param path Directory containing the file
     * @param fileName Name of the file with no extension
     * @param ext extension with trailing "."
     * @param mustExist If true, raises an excaption if the file does not exist
     *
     * @return The File object
     *
     * @throws FileNotFoundException
     */
    private static File getFileHandler(File path, String fileName,
        String ext, boolean mustExist) throws FileNotFoundException {
        File file = new File(path, fileName + ext);

        if (file.exists() || !mustExist) {
            return file;
        }

        file = new File(path, fileName+"."+ext.toUpperCase());

        if (file.exists()) {
            return file;
        }
        
        file = new File(path, fileName+"."+ext.toLowerCase());

        if (file.exists()) {
            return file;
        }
        
        throw new FileNotFoundException("Can't find file: " + file.getName());
    }

}
