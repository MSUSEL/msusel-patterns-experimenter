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
package org.geotools.data.oracle.sdo;

/**
 * Mimics Oracle MDSYS functions for building geometries. Useful for creating test objects.
 * 
 * @author Martin Davis
 * 
 */
public class MDSYS {

    protected static final int NULL = -1;

    public static SDO_GEOMETRY SDO_GEOMETRY(int gType, int srid, int ptType, int[] elemInfo,
            double[] ordinates) {
        return new SDO_GEOMETRY(gType, srid, elemInfo, ordinates);
    }

    public static SDO_GEOMETRY SDO_GEOMETRY(int gType, int srid, double[] ptType, int null1,
            int null2) {
        return new SDO_GEOMETRY(gType, srid, ptType);
    }

    public static double[] SDO_POINT_TYPE(double x, double y, double z) {
        if (z == NULL)
            z = Double.NaN;
        return new double[] { x, y, z };
    }

    public static int[] SDO_ELEM_INFO_ARRAY(int... i) {
        return i;
    }

    public static double[] SDO_ORDINATE_ARRAY(double... d) {
        return d;
    }

    public static class SDO_GEOMETRY {

        int gType;

        int srid;

        double[] ptType;

        int[] elemInfo;

        double[] ordinates;

        public SDO_GEOMETRY(int gType, int srid, int[] elemInfo, double[] ordinates) {
            this.gType = gType;
            this.srid = srid;
            this.elemInfo = elemInfo;
            this.ordinates = ordinates;
        }

        public SDO_GEOMETRY(int gType, int srid, double[] ptType) {
            this.gType = gType;
            this.srid = srid;
            this.ptType = ptType;
        }

    }
}
