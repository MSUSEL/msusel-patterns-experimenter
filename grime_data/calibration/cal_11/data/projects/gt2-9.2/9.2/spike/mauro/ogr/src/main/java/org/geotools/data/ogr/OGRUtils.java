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
package org.geotools.data.ogr;

import static org.geotools.data.ogr.bridj.OgrLibrary.*;
import static org.geotools.data.ogr.bridj.CplErrorLibrary.*;

import java.io.IOException;

import org.bridj.Pointer;

/**
 * Common utilities to deal with OGR pointers
 * 
 * @author Andrea Aime - GeoSolutions
 */
@SuppressWarnings("rawtypes")
class OGRUtils {

    private static boolean HAS_L_GETNAME;

	public static void releaseDataSource(Pointer dataSet) {
        if (dataSet != null) {
            OGRReleaseDataSource(dataSet);            
            dataSet.release();
        }
    }

    public static void releaseLayer(Pointer layer) {
        if (layer != null) {
            // OGR_L_Dereference(layer);
            layer.release();
        }

    }

    public static void releaseDefinition(Pointer definition) {
        if (definition != null) {
            // OGR_FD_Destroy(definition);
            definition.release();
        }
    }

    public static void releaseSpatialReference(Pointer spatialReference) {
        if (spatialReference != null) {
            // OSRDestroySpatialReference(spatialReference);
            spatialReference.release();
        }
    }
    
    public static String getCString(Pointer<Byte> ptr) {
        if(ptr == null) {
            return null;
        } else {
            return ptr.getCString();
        }
    }
    
    /**
     * Gets a layer name in a version independent way
     * @param layer
     */
    public static String getLayerName(Pointer layer) {
    	Pointer<Byte> namePtr = null;
    	try {
    		// this one is more efficient but has been added recently
    		if(HAS_L_GETNAME) {
    			namePtr = OGR_L_GetName(layer);
    		}
    	} catch(Exception e) {
    		HAS_L_GETNAME = false;
    	}
    	if(namePtr == null) {
	    	Pointer layerDefinition = OGR_L_GetLayerDefn(layer);
			namePtr = OGR_FD_GetName(layerDefinition);
    	}
		return getCString(namePtr);
    }

    /**
     * Checks the OGRErr status code and throws java exceptions accordingly
     * 
     * @param ogrError
     * @throws IOException
     */
    public static void checkError(int ogrError) throws IOException {
        if (ogrError == OGRERR_NONE) {
            return;
        }
        
        String error = getCString(CPLGetLastErrorMsg());

        switch (ogrError) {
        case OGRERR_CORRUPT_DATA:
            throw new IOException("OGR reported a currupt data error: " + error);
        case OGRERR_FAILURE:
            throw new IOException("OGR reported a generic failure: " + error);
        case OGRERR_INVALID_HANDLE:
            throw new IOException("OGR reported an invalid handle error: " + error);
        case OGRERR_NOT_ENOUGH_DATA:
            throw new IOException("OGR reported not enough data was provided in the last call: " + error);
        case OGRERR_NOT_ENOUGH_MEMORY:
            throw new IOException("OGR reported not enough memory is available: " + error);
        case OGRERR_UNSUPPORTED_GEOMETRY_TYPE:
            throw new IOException("OGR reported a unsupported geometry type error: " + error);
        case OGRERR_UNSUPPORTED_OPERATION:
            throw new IOException("OGR reported a unsupported operation error: " + error);
        case OGRERR_UNSUPPORTED_SRS:
            throw new IOException("OGR reported a unsupported SRS error: " + error);
        default:
            throw new IOException("OGR reported an unrecognized error code: " + ogrError);
        }
    }

}
