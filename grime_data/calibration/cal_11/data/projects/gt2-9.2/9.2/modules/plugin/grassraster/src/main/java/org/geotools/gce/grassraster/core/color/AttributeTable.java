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
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster.core.color;

import java.util.Enumeration;
import java.util.Vector;

/**
 *
 *
 *
 * @source $URL$
 */
public class AttributeTable {
    private Vector<CellAttribute> atts = null;

    /** Creates a new instance of AttributeTable */
    public AttributeTable() {
        atts = new Vector<CellAttribute>();
    }

    /**
     *
     */
    public int size() {
        return atts.size();
    }

    /**
     *
     */
    public Enumeration<CellAttribute> getCategories() {
        return atts.elements();
    }

    /**
     *
     */
    public void addAttribute( float cat, String value ) {
        if (get(cat) == null) {
            insertAttribute(cat, value);
        }
    }

    public void addAttribute( float cat0, float cat1, String value ) {
        // TODO implement
        //        System.out.println("Not yet implemented!"); //$NON-NLS-1$
    }

    /**
     *
     */
    private String get( float cat ) {
        int low = 0;
        int high = atts.size() - 1;

        while( low <= high ) {
            int i = (low + high) / 2;
            CellAttribute catt = (CellAttribute) atts.elementAt(i);
            int c = catt.compare(cat);
            if (c == 0) {
                return catt.getText();
            } else if (c < 0) {
                high = i - 1;
            } else {
                low = i++ + 1;
            }
        }
        return null;
    }

    private void insertAttribute( float cat, String value ) {
        int i = 0;
        int low = 0;
        int high = atts.size() - 1;

        while( low <= high ) {
            i = (low + high) / 2;
            CellAttribute catt = (CellAttribute) atts.elementAt(i);
            int c = catt.compare(cat);
            if (c == 0) {
                /*
                 * Attribute found with equal value so break and insert using this index.
                 */
                low = high + 1;
            } else if (c < 0) {
                high = i - 1;
            } else {
                low = i++ + 1;
            }
        }
        atts.insertElementAt(new CellAttribute(cat, value), i);
    }

    /**
     *
     */
    public class CellAttribute {
        private float low = 0f;

        private float range = 0f;

        private String catText = null;

        /**
         *
         */
        public CellAttribute( float cat, String text ) {
            low = cat;
            range = 0;
            catText = text;
        }

        /**
         *
         */
        public CellAttribute( float cat0, float cat1, String text ) {
            if (cat1 > cat0) {
                low = cat0;
                range = cat1 - cat0;
            } else {
                low = cat1;
                range = cat0 - cat1;
            }
            catText = text;
        }

        /**
         * Compare a value to the range of values in this attribute If the cat is below the renage
         * then return -1, if it is aboove the ramge then return +1, if it is equal return 0
         */
        public int compare( float cat ) {
            float diff = cat - low;
            if (diff < 0)
                return -1;
            else if (diff > range)
                return 1;

            return 0;
        }

        public String getText() {
            return catText;
        }

        public float getLowcategoryValue() {
            return low;
        }

        public float getCategoryRange() {
            return range;
        }

        public String toString() {
            if (range == 0f)
                return String.valueOf(low) + ":" + catText; //$NON-NLS-1$
            else
                return String.valueOf(low) + "-" + String.valueOf(low + range) + ":" + catText; //$NON-NLS-1$
        }
    }
}
