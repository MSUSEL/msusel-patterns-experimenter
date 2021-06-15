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
package org.geotools.data.ogr;

import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import org.bridj.BridJ;
import org.bridj.Pointer;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRFieldType;

public class DumpShapefile {
    

    public static void main(String[] args) {
        GdalInit.init();
        OGRRegisterAll();
        for (int i = 0; i < OGRGetDriverCount(); i++) {
            Pointer driver = OGRGetDriver(i);
            System.out.println(OGR_Dr_GetName(driver).getCString());
        }
        
        String path = "/home/aaime/devel/gisData/world.shp";
        Pointer ds = OGROpen(Pointer.pointerToCString(path), 0, null);
        System.out.println(ds);
        Pointer layer = OGR_DS_GetLayerByName(ds, Pointer.pointerToCString("world"));
        
        Pointer hFDefn = OGR_L_GetLayerDefn(layer);
        int iField;
        
        Pointer hFeature;

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
            OGR_F_Destroy(hFeature);
        }
        // OGR_FD_Release(hFDefn);
        OGR_L_Dereference(layer);
        OGR_DS_Destroy(ds);
    }
}