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
 * $Id$
 */

package org.geotools.data.dxf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author Matthijs Laan, B3Partners
 *
 *
 *
 * @source $URL$
 */
public class DXFDataStoreFactory implements FileDataStoreFactorySpi {
    private static final Log log = LogFactory.getLog(DXFDataStoreFactory.class);

    public static final DataStoreFactorySpi.Param PARAM_URL = new Param("url", URL.class, "url to a .dxf file");    
    public static final DataStoreFactorySpi.Param PARAM_SRS = new Param("srs", String.class, "override srs");    
    
    public String getDisplayName() {
        return "DXF File";
    }

    public String getDescription() {
        return "Autodesk DXF format";
    }

    public String[] getFileExtensions() {
        return new String[] {".dxf"};
    }

    /**
     * @return true if the file of the f parameter exists
     */
    public boolean canProcess(URL f) {
        return f.getFile().toLowerCase().endsWith(".dxf");  
    }

    /**
     * @return true if srs can be resolved
     */
    public boolean canProcess(String srs) throws NoSuchAuthorityCodeException, FactoryException {
        return CRS.decode(srs) != null;
    }

    /**
     * @return true if the file in the url param exists
     */
    public boolean canProcess(Map params) {
        boolean result = false;
        if (params.containsKey(PARAM_URL.key)) {
            try {
                URL url = (URL)PARAM_URL.lookUp(params);
                result = canProcess(url);
            } catch (IOException ioe) {
                /* return false on any exception */
            }
        }
        if (result && params.containsKey(PARAM_SRS.key)) {
            try {
                String srs = (String) PARAM_SRS.lookUp(params);
                result = canProcess(srs);
            } catch (NoSuchAuthorityCodeException ex) {
                /* return false on any exception */
            } catch (FactoryException ex) {
                /* return false on any exception */
            } catch (IOException ioe) {
                /* return false on any exception */
            }
        }
        return result;
    }

    /*
     * Always returns true, no additional libraries needed
     */
    public boolean isAvailable() {
        return true;
    }

    public Param[] getParametersInfo() {
        return new Param[] {PARAM_URL};
    }

    public Map getImplementationHints() {
        /* XXX do we need to put something in this map? */
        return Collections.EMPTY_MAP;
    }

    public String getTypeName(URL url) throws IOException {
        return DXFDataStore.getURLTypeName(url);
    }

    public FileDataStore createDataStore(URL url) throws IOException {
        Map params = new HashMap();
        params.put(PARAM_URL.key, url);
        
        boolean isLocal = url.getProtocol().equalsIgnoreCase("file");
        if(isLocal && !(new File(url.getFile()).exists())){
            throw new UnsupportedOperationException("Specified DXF file \"" + url + "\" does not exist, this plugin is read-only so no new file will be created");
        } else {
            return createDataStore(params);
        }        
    }

    public FileDataStore createDataStore(Map params) throws IOException {
        if(!canProcess(params)) {
            throw new FileNotFoundException( "DXF file not found: " + params);
        }
        return new DXFDataStore((URL)params.get(PARAM_URL.key), (String)params.get(PARAM_SRS.key));
    }

    public DataStore createNewDataStore(Map params) throws IOException {
        throw new UnsupportedOperationException("This plugin is read-only");
    }
}
