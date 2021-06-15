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
import java.util.Iterator;

/**
 *
 * @author mcoudert
 *
 *
 *
 *
 * @source $URL$
 */
public class EdigeoDIC {
    
    private File dicFile = null;
    
    private static final String DICExtension = "dic";
    
    private static final String DS = ":";
    private static final String VS = ";";
    
    /**
     * <p>
     * This constructor opens an existing DIC file
     * </p>
     *
     * @param path Full pathName of the thf file, can be specified without the
     *        .thf extension
     *
     * @throws IOException If the specified thf file could not be opened
     */
    public EdigeoDIC(String path) throws IOException {
        super();
        dicFile = EdigeoFileFactory.setFile(path, DICExtension, true);
    }
    
     /**
     * <p>
     *	
     * </p>
     * @param obj
     * @return {@link HashMap}
     */
    public HashMap<String,HashMap<String,String>> readDICFile(HashMap<String,String> atts) 
            throws IOException {
        HashMap<String, HashMap<String,String>> attribut = 
                new HashMap<String, HashMap<String,String>>();
        
        Iterator<String> it = atts.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            EdigeoParser dicParser = new EdigeoParser(dicFile);
            HashMap<String, String> attParams = new HashMap<String, String>();
            while (dicParser.readLine()){
                if (dicParser.line.contains(atts.get(key))) {
                    while (dicParser.readLine()){
                        if (dicParser.line.contains("TYPSA")) {
                            attParams.put("type", dicParser.getValue("TYPSA"));
                        }
                        if (dicParser.line.contains("AVCSN")) {
                            int nbPrecoded = Integer.parseInt(dicParser.getValue("AVCSN"));
                            if (nbPrecoded > 0 ) {
                                attParams.put("precoded", "true");
                                String preKey = null;

                                for (int i = 0; i < nbPrecoded; i++) {
                                    while (dicParser.readLine()) {
                                        if (dicParser.line.contains("AVLSA")){
                                            preKey = dicParser.getValue("AVLSA");                              
                                            while (dicParser.readLine()) {
                                                if (dicParser.line.contains("AVDST")) {
                                                    attParams.put(preKey, dicParser.getValue("AVDST"));
                                                    break;
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }
                            } else {
                                attParams.put("precoded", "false");
                            }
                            break;
                        }
                    }                    
                    attribut.put(key, attParams);
                    break;
                }
            }
            dicParser.close();
        }
        return attribut;
    }
    
    /**
     * 
     * @param att
     * @return
     */
    protected String getDicAtt(String att) throws FileNotFoundException {
        EdigeoParser parser = new EdigeoParser(dicFile);
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
