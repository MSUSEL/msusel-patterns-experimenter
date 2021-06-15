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
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.geotools.factory.GeoTools;

/**
 * 
 *
 * @source $URL$
 */
public class JDBCJNDITestSetup extends JDBCDelegatingTestSetup {

    public JDBCJNDITestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected void setupJNDIEnvironment() throws IOException {
        
        File jndi = new File( "target/jndi");
        jndi.mkdirs();
        
        OutputStreamWriter out = new OutputStreamWriter( new FileOutputStream( new File( jndi, "ds.properties")) );
        
        //can't use Properties.store because it escapes colons, which throws
        // of the jdbc urls
        for ( Map.Entry e : fixture.entrySet() ) {
            out.write(e.getKey().toString()+"="+e.getValue().toString()+"\n");
        }
        if (!fixture.containsKey("password") && fixture.containsKey("passwd")) {
            out.write("password=" + fixture.get("passwd")+"\n");
        }
        out.write( "type=javax.sql.DataSource\n");
        out.flush();
        out.close();
        
        String IC_FACTORY_PROPERTY = "java.naming.factory.initial";
        String JNDI_ROOT = "org.osjava.sj.root";
        String JNDI_DELIM = "org.osjava.jndi.delimiter";

        if (System.getProperty(IC_FACTORY_PROPERTY) == null) {
            System.setProperty(IC_FACTORY_PROPERTY, "org.osjava.sj.SimpleContextFactory");
        }

        if (System.getProperty(JNDI_ROOT) == null) {
            System.setProperty(JNDI_ROOT, jndi.getAbsolutePath());
        }
        
        if (System.getProperty(JNDI_DELIM) == null)
            System.setProperty(JNDI_DELIM, "/");
        
        LOGGER.fine( IC_FACTORY_PROPERTY + " = " + System.getProperty(IC_FACTORY_PROPERTY) );
        LOGGER.fine( JNDI_ROOT + " = " + System.getProperty(JNDI_ROOT) );
        LOGGER.fine( JNDI_DELIM + " = " + System.getProperty(JNDI_DELIM) );
    }

    @Override
    protected DataSource createDataSource() throws IOException {
        setupJNDIEnvironment();
        
        DataSource ds = null;
        try {
            //JD: we need to "reset" the naming context because if there is a 
            // context that already exists (this mostly happens due to the epsg
            // hsql database), then it will not have been affected by our special
            // jndi environment variables
            GeoTools.init( new InitialContext() );
            Context ctx = GeoTools.getInitialContext(GeoTools.getDefaultHints());
            ds = (DataSource) ctx.lookup("ds");
        } 
        catch (NamingException e) {
            e.printStackTrace();
        }
        
        return ds;
    }

}
