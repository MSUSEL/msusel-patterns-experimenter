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

import static org.bridj.Pointer.*;
import static org.geotools.data.ogr.OGRUtils.*;
import static org.geotools.data.ogr.bridj.OgrLibrary.*;

import java.io.IOException;

import org.bridj.Pointer;
import org.geotools.data.ogr.bridj.OgrLibrary.OGRwkbByteOrder;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Converts between JTS and OGR geometries
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/main/java/org/geotools
 *         /data/ogr/GeometryMapper.java $
 */
@SuppressWarnings("rawtypes")
abstract class GeometryMapper {

    abstract Geometry parseOgrGeometry(Pointer geom) throws IOException;

    abstract Pointer parseGTGeometry(Geometry geometry) throws IOException;

    static class WKB extends GeometryMapper {
        GeometryFactory geomFactory;

        WKBReader wkbReader;

        WKBWriter wkbWriter;

        public WKB(GeometryFactory geomFactory) {
            this.geomFactory = geomFactory;
            this.wkbReader = new WKBReader(geomFactory);
            this.wkbWriter = new WKBWriter();
        }

        /**
         * Reads the current feature's geometry using wkb encoding. A wkbReader should be provided
         * since it's not thread safe by design.
         * 
         * @throws IOException
         */
        Geometry parseOgrGeometry(Pointer geom) throws IOException {
            int wkbSize = OGR_G_WkbSize(geom);
            Pointer<Byte> ptrBytes = pointerToBytes(new byte[wkbSize]);
            checkError(OGR_G_ExportToWkb(geom, OGRwkbByteOrder.wkbXDR, ptrBytes));
            try {
                byte[] wkb = ptrBytes.getBytes();
                Geometry g = wkbReader.read(wkb);
                return g;
            } catch (ParseException pe) {
                throw new IOException("Could not parse the current Geometry in WKB format.", pe);
            }
        }

        Pointer parseGTGeometry(Geometry geometry) throws IOException {
            byte[] wkb = wkbWriter.write(geometry);
            Pointer<Pointer<?>> ptr = allocatePointer();
            checkError(OGR_G_CreateFromWkb(pointerToBytes(wkb), null, ptr, wkb.length));
            return ptr.getPointer(Pointer.class);
        }

    }

    static class WKT extends GeometryMapper {
        GeometryFactory geomFactory;

        WKTReader wktReader;

        WKTWriter wktWriter;

        public WKT(GeometryFactory geomFactory) {
            this.geomFactory = geomFactory;
            this.wktReader = new WKTReader(geomFactory);
            this.wktWriter = new WKTWriter();
        }

        /**
         * Reads the current feature's geometry using wkb encoding. A wkbReader should be provided
         * since it's not thread safe by design.
         * 
         * @throws IOException
         */
        Geometry parseOgrGeometry(Pointer geom) throws IOException {
            Pointer<Pointer<Byte>> wktPtr = allocatePointer(Byte.class);
            checkError(OGR_G_ExportToWkt(geom, wktPtr));
            try {
                String wkt = wktPtr.getPointer(Byte.class).getCString();
                return wktReader.read(wkt);
            } catch (ParseException pe) {
                throw new IOException("Could not parse the current Geometry in WKT format.", pe);
            }
        }

        Pointer parseGTGeometry(Geometry geometry) throws IOException {
            String wkt = wktWriter.write(geometry);
            Pointer<Pointer<Byte>> ptr = pointerToPointer(pointerToCString(wkt));
            Pointer<Pointer<?>> geom = allocatePointer();
            checkError(OGR_G_CreateFromWkt(ptr, null, geom));
            return geom.getPointer(Pointer.class);
        }

    }
}
