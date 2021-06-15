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
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author mcoudert
 *
 *
 *
 *
 * @source $URL$
 */
public class EdigeoSCD {
    
    private File scdFile = null;
    
    private static final String SCDExtension = "scd";
    
    private static final String DS = ":";
    private static final String VS = ";";
    
    /**
     * <p>
     * This constructor opens an existing THF file
     * </p>
     *
     * @param path Full pathName of the thf file, can be specified without the
     *        .thf extension
     *
     * @throws IOException If the specified thf file could not be opened
     */
    public EdigeoSCD(String path) throws IOException {
        super();
        scdFile = EdigeoFileFactory.setFile(path, SCDExtension, true);
    }
    
    /**
     * Get defined attributes in Edigeo schema for the given object 
     * @param obj {@link String}
     * @return {@link HashMap}
     */
    public HashMap<String,String> readSCDFile(String obj) throws IOException {
        EdigeoParser scdParser = new EdigeoParser(scdFile);
        String idAtt = null;
        String idDic = null;
        int nbAtt;
        HashMap<String,String> attIds = new HashMap<String, String>();

        while (scdParser.readLine()) {
            if (scdParser.line.contains(DS + obj)) {
                while(scdParser.readLine()) {
                    if (scdParser.line.contains("AACSN")) {
                        nbAtt = Integer.parseInt(scdParser.getValue("AACSN"));
                        for (int i = 0; i < nbAtt; i++) {
                            scdParser.readLine();
                            idAtt = scdParser.getValue("AAPCP");
                            idAtt = idAtt.substring(idAtt.lastIndexOf(VS)+1);
                            idDic = getDicAtt(idAtt);
                            attIds.put(idAtt, idDic);
                        }
                        break;
                    }                    
                }
                break;
            }
        }
        scdParser.close();
        return attIds;
    }
    
    /**
     * 
     * @param att
     * @return
     */
    protected String getDicAtt(String att) throws FileNotFoundException {
        EdigeoParser parser = new EdigeoParser(scdFile);
        String dicAtt = null; 
        
        while (parser.readLine()) {
            if (parser.line.contains(DS+att)) {
                parser.readLine();
                dicAtt = parser.getValue("DIPCP")
                        .substring(parser.getValue("DIPCP").lastIndexOf(VS)+1);
                break;
            }
        }
        parser.close();
        return dicAtt;
    }

}
