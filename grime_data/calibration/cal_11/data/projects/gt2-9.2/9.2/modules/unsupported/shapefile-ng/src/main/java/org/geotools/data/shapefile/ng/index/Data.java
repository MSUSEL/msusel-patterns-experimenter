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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.ng.index;

import java.util.ArrayList;

/**
 * Holds values (with associated DataDefinition)
 * 
 * @author Tommaso Nolli
 *
 *
 * @source $URL$
 */
public class Data {
    private DataDefinition def;
    private ArrayList values;

    /**
     * DOCUMENT ME!
     * 
     * @param def
     */
    public Data(DataDefinition def) {
        this.def = def;
        this.values = new ArrayList(def.getFieldsCount());
    }

    /**
     * Check to see if a <code>Data</code> respects its
     * <code>DataDefinition</code>
     * 
     */
    public final boolean isValid() {
        if (this.getValuesCount() != this.def.getFieldsCount()) {
            return false;
        }

        boolean ret = true;

        for (int i = 0; i < this.def.getFieldsCount(); i++) {
            if (!this.def.getField(i).getFieldClass().isInstance(
                    this.getValue(i))) {
                ret = false;

                break;
            }
        }

        return ret;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param val
     * 
     * @return - this Data object
     * 
     * @throws TreeException
     */
    public Data addValue(Object val) throws TreeException {
        if (this.values.size() == def.getFieldsCount()) {
            throw new TreeException("Max number of values reached!");
        }

        int pos = this.values.size();

        if (!val.getClass().equals(def.getField(pos).getFieldClass())) {
            throw new TreeException("Wrong class type, was expecting "
                    + def.getField(pos).getFieldClass());
        }

        this.values.add(val);

        return this;
    }

    /**
     * Return the KeyDefinition
     * 
     */
    public DataDefinition getDefinition() {
        return this.def;
    }

    /**
     * DOCUMENT ME!
     * 
     */
    public int getValuesCount() {
        return this.values.size();
    }

    /**
     * DOCUMENT ME!
     * 
     * @param i
     * 
     */
    public Object getValue(int i) {
        return this.values.get(i);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();

        for (int i = 0; i < this.values.size(); i++) {
            if (i > 0) {
                ret.append(" - ");
            }

            ret.append(this.def.getField(i).getFieldClass());
            ret.append(": ");
            ret.append(this.values.get(i));
        }

        return ret.toString();
    }
    
    public void clear() {
        values.clear();
    }
}
