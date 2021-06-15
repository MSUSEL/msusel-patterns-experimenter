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
package org.geotools.referencing.factory.epsg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Dumps the contents of the EPSG database out to a java property file for use by the wkt plugin.
 *
 *
 * @source $URL$
 */
public class PropertyDumper {
    public static void main(String[] args) throws Exception {
        String filename = "/tmp/wkt.properties";
        Properties diff = new Properties();
        String compare = null;
        if( args.length > 0 ){
            filename = args[0];
        }
        
        File original;
        if( args.length > 1 ){
            original = new File( args[1] );
        } else {
            original = new File("../epsg-wkt/src/main/resources/org/geotools/referencing/crs/epsg.properties");
        }
        if( original.exists() ){
            InputStream in = new FileInputStream( original );
            diff.load( in );
        }
        
        FileOutputStream out = new FileOutputStream( filename );
        Writer writer = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));
        writer.append("Generate from EPSG database version "+ThreadedHsqlEpsgFactory.VERSION );
        
        Properties props = new Properties();
        List<String> codes = new ArrayList(CRS.getSupportedCodes("EPSG"));
        Collections.sort(codes, new Comparator<String>() {

            public int compare(String c1, String c2) {
                try {
                    Long n1 = new Long(c1);
                    Long n2 = new Long(c2);
                    return n1.compareTo(n2);
                } catch(NumberFormatException e) {
                    return c1.compareTo(c2);
                }
            }
        });
        for (String code : codes) {
            try {
                CoordinateReferenceSystem crs = CRS.decode("EPSG:" + code, true);
                // use toString, it's more lenient that toWKT
                String wkt = crs.toString().replaceAll("\n", "").replaceAll("  ", "");
                // make sure we can parse back what we generated
                CRS.parseWKT(wkt);
                
                props.put(code, wkt);
                
                diff.remove(code);
                
            } catch (Exception e) {
                // we cannot actually decode all codes, but let's list what we can't
                String desc = "";
                try {
                    desc = CRS.getAuthorityFactory(true).getDescriptionText("EPSG:" + code).toString(Locale.ENGLISH);
                } catch(Exception ex) {
                    // fine, it's just to have a nicer description of the error
                }
                System.out.println("#"+code +  "(" + desc + ")" +" -> " + e.getMessage());
            }
        }
        props.store(out,"Generated from EPSG database version " + ThreadedHsqlEpsgFactory.VERSION);
        if(!diff.isEmpty() ){
            diff.store(out, "Extra Definitions Supplied from Community");
        }
        out.close();
    }
}
