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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.xs.XS;

import junit.framework.TestCase;

/**
 * 
 *
 * @source $URL$
 */
public class SchemasTest extends TestCase {

    File tmp,sub;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        tmp = File.createTempFile("schemas", "xsd");
        tmp.delete();
        tmp.mkdir();
        tmp.deleteOnExit();

        sub = new File( tmp, "sub" );
        sub.mkdir();
        sub.deleteOnExit();
        
        File f = new File( tmp, "root.xsd" );
        String xsd = 
            "<xsd:schema xmlns='http://geotools.org/test' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/test'> " + 
                "<xsd:import namespace='http://geotools/org/import1' " + 
                    "schemaLocation='import1.xsd'/>" + 
                "<xsd:import namespace='http://geotools/org/import2' " + 
                    "schemaLocation='import2.xsd'/>" +
                "<xsd:include location='include1.xsd'/>" +
                "<xsd:include location='include2.xsd'/>" +
            "</xsd:schema>";
        write( f, xsd );
        
        f = new File( tmp, "import1.xsd" );
        xsd = 
            "<xsd:schema xmlns='http://geotools.org/import1' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/import1'> " + 
            "</xsd:schema>";
        write( f , xsd );
        
        f = new File( sub, "import2.xsd" );
        xsd = 
            "<xsd:schema xmlns='http://geotools.org/import2' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/import2'> " + 
            "</xsd:schema>";
        write( f , xsd );
        
        f = new File( tmp, "include1.xsd" );
        xsd = 
            "<xsd:schema xmlns='http://geotools.org/test' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/test'> " + 
            "</xsd:schema>";
        write( f, xsd );
        
        f = new File( sub, "include2.xsd" );
        xsd = 
            "<xsd:schema xmlns='http://geotools.org/test' " +
                "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
                "targetNamespace='http://geotools.org/test'> " + 
            "</xsd:schema>";
        write( f, xsd );
            
    }
    
    void write( File f, String xsd ) throws IOException {
        f.deleteOnExit();
        f.createNewFile();
        FileWriter w = new FileWriter( f );
        w.write( xsd );
        w.flush();
        w.close();
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
        
        new File( tmp, "root.xsd" ).delete();
        new File( tmp, "import1.xsd" ).delete();
        new File( sub, "import2.xsd" ).delete();
        new File( tmp, "include1.xsd" ).delete();
        new File( sub, "include2.xsd" ).delete();
        
        sub.delete();
        tmp.delete();
    }
    
    
    public void testValidateImportsIncludes() throws Exception {
       String location = new File( tmp, "root.xsd").getAbsolutePath();
       List errors = Schemas.validateImportsIncludes( location );
       assertEquals( 2, errors.size() );
    
       SchemaLocationResolver resolver1 = new SchemaLocationResolver(XS.getInstance()) {
         
        public boolean canHandle(XSDSchema schema, String uri, String location) {
            if ( location.endsWith("import2.xsd") ) {
                return true;
            }
            
            return false;
        }
           
         public String resolveSchemaLocation(XSDSchema schema, String uri, String location) {
             return new File( sub, "import2.xsd" ).getAbsolutePath();
         }
       };
       SchemaLocationResolver resolver2 = new SchemaLocationResolver(XS.getInstance()) {
           
           public boolean canHandle(XSDSchema schema, String uri, String location) {
               if ( location.endsWith("include2.xsd") ) {
                   return true;
               }
               
               return false;
           }
              
            public String resolveSchemaLocation(XSDSchema schema, String uri, String location) {
                return new File( sub, "include2.xsd" ).getAbsolutePath();
            }
       };
          
       
      errors = Schemas.validateImportsIncludes( location, null, new XSDSchemaLocationResolver[]{resolver1,resolver2} );
      assertEquals( 0, errors.size() );
      
    }
}
