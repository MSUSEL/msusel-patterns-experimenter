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
/**
 * 
 */
package org.geotools.data.ogr;

import static org.geotools.data.ogr.bridj.OgrLibrary.OGRGetDriver;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGRGetDriverCount;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGROpen;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGRRegisterAll;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_DS_Destroy;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_DS_GetLayerByName;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_Dr_GetName;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_FD_GetFieldCount;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_FD_GetFieldDefn;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_F_Destroy;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_F_GetFieldAsDouble;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_F_GetFieldAsInteger;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_F_GetFieldAsString;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_Fld_GetType;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_L_Dereference;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_L_GetLayerDefn;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_L_GetNextFeature;
import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_L_ResetReading;

import org.bridj.Pointer;
import org.geotools.data.ogr.bridj.OgrLibrary;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;

/**
 * MapInfo - Tab file
 * 
 * This class test the capability to read and write Tab format.
 * 
 * The implementation uses the native interface.
 * 
 * @author mauro
 *
 */
public class TabFileReader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
 
		GdalInit.init();
        OGRRegisterAll();

        displayDrivers();
        
        readingShp();
 
        
	}

	private static void displayDrivers() {
		for (int i = 0; i < OGRGetDriverCount(); i++) {
            Pointer driver = OGRGetDriver(i);
            System.out.println(OGR_Dr_GetName(driver).getCString());
        }
	}

	private static void readingShp() {
		String path = "/home/mauro/Downloads/gis-test-data/10m_geography_marine_polys.shp";
        Pointer ds = OGROpen(Pointer.pointerToCString(path), 0, null);
        
        
        System.out.println(ds);
        Pointer layer = OGR_DS_GetLayerByName(ds, Pointer.pointerToCString("10m_geography_marine_polys"));
        
        Pointer hFDefn = OGR_L_GetLayerDefn(layer);
        int iField;
        
        Pointer hFeature;

        // presets the layer's features
        OGR_L_ResetReading(layer);
        while( (hFeature = OGR_L_GetNextFeature(layer)) != null)
        {
            for( iField = 0; iField < OGR_FD_GetFieldCount(hFDefn); iField++ )
            {
                Pointer hFieldDefn = OGR_FD_GetFieldDefn( hFDefn, iField );
    
                if( OGR_Fld_GetType(hFieldDefn) == OGRFieldType.OFTInteger )
                    System.out.println(OGR_F_GetFieldAsInteger( hFeature, iField ) );
                else if( OGR_Fld_GetType(hFieldDefn) == OGRFieldType.OFTReal )
                    System.out.println(OGR_F_GetFieldAsDouble( hFeature, iField) );
                else
                    System.out.println(OGR_F_GetFieldAsString( hFeature, iField).getCString() );
            }
            
            Pointer<?> geoRef = OgrLibrary.OGR_F_GetGeometryRef(hFeature);
            if(geoRef != null){
//            	double x = OgrLibrary.OGR_G_Ge(geoRef, 0);
//            	double y = OgrLibrary.OGR_G_GetY(geoRef, 0);
            	
            	System.out.println("has geometry ");
            }
            OGR_F_Destroy(hFeature);
        }
        // OGR_FD_Release(hFDefn);
        OGR_L_Dereference(layer);
        OGR_DS_Destroy(ds);
	}
	
	

}
