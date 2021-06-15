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
package org.geotools.caching.grid.featurecache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.caching.util.CacheUtil;
import org.geotools.caching.util.Generator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.filter.spatial.BBOXImpl;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Data Utilities for Caching.
 *
 *
 *
 * @source $URL$
 */
public class DataUtilities {
    static FilterFactoryImpl ff = new FilterFactoryImpl();

    public static DefaultFeatureCollection createUnitsquareDataSet(int numdata) {
        Generator gen = new Generator(1, 1);
        DefaultFeatureCollection dataset = new DefaultFeatureCollection("Test", Generator.type);

        for (int i = 0; i < numdata; i++) {
            dataset.add(gen.createFeature(i));
        }

        return dataset;
    }

    public static DefaultFeatureCollection createUnitsquareDataSet(int numdata, long seed) {
        Generator gen = new Generator(1, 1, seed);
        DefaultFeatureCollection dataset = new DefaultFeatureCollection("Test", Generator.type);

        for (int i = 0; i < numdata; i++) {
            dataset.add(gen.createFeature(i));
        }

        return dataset;
    }

    public static ArrayList<Filter> createUnitsquareFilterSet(int numfilters, double[] windows) {
        ArrayList<Filter> filterset = new ArrayList<Filter>(numfilters);

        //Coordinate p = Generator.pickRandomPoint(new Coordinate(0.5, 0.5), .950, .950);
        Coordinate p = new Coordinate(0.5, 0.5);

        for (int i = 0; i < numfilters; i += windows.length) {
            for (int j = 0; j < windows.length; j++) {
                filterset.add(Generator.createBboxFilter(p, windows[j], windows[j]));
                p = Generator.pickRandomPoint(p, windows[j], windows[j]);
            }
        }

        return filterset;
    }

    public static Filter convert(Envelope e) {
        return ff.bbox("", e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY(), "");
    }

    public static void saveFilters(List<Filter> filterset, File f)
        throws Exception {
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        dumpFilterSet(oos, filterset);
        oos.close();
        fos.close();
    }

    public static List<Filter> loadFilters(File f) throws Exception {
        System.out.println("Loading existing filters.");

        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream ois = new ObjectInputStream(fis);
        int n = ois.readInt();
        List<Filter> filterset = new ArrayList<Filter>();

        for (int i = 0; i < n; i++) {
            Envelope e = (Envelope) ois.readObject();
            filterset.add(DataUtilities.convert(e));
        }

        ois.close();
        fis.close();

        return filterset;
    }

    public static void dumpFilterSet(ObjectOutputStream oos, List<Filter> filterset)
        throws Exception {
        oos.writeInt(filterset.size());

        for (Iterator<Filter> it = filterset.iterator(); it.hasNext();) {
            Envelope e = CacheUtil.extractEnvelope((BBOXImpl) it.next());
            oos.writeObject(e);
        }
    }
}
