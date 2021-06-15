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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema.geometry;

import org.geotools.geometry.jts.JTSGeometry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Helper class that notifies the containing geometry when the list has changed
 * so that it can invalidate any cached JTS objects it had.
 *
 *
 *
 *
 * @source $URL$
 */
public class NotifyingArrayList<T> extends ArrayList<T> {
    private JTSGeometry parent;

    public NotifyingArrayList() {
        this( null );
    }
    public NotifyingArrayList(JTSGeometry parent) {
        this.parent = parent;
    }
    public void setJTSParent( JTSGeometry parent ){
        this.parent = parent;
    }
    public JTSGeometry getJTSParent(){
        return parent;
    }
    public void invalidateCachedJTSPeer(){
        if (parent != null) parent.invalidateCachedJTSPeer();
    }
    public void add(int index, T element) {
        super.add(index, element);
        if (parent != null) parent.invalidateCachedJTSPeer();
    }
    public boolean add(T o) {
        boolean result = super.add(o);
        if (parent != null) parent.invalidateCachedJTSPeer();
        return result;
    }
    public boolean addAll(Collection<? extends T> c) {
        boolean result = super.addAll(c);
        if (parent != null) parent.invalidateCachedJTSPeer();
        return result;
    }
    
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean result = super.addAll(index, c);
        if (parent != null) parent.invalidateCachedJTSPeer();
        return result;
    }
    public void clear() {
        super.clear();
        if (parent != null) parent.invalidateCachedJTSPeer();
    }
    public T remove(int index) {
        T result = super.remove(index);
        if (parent != null) parent.invalidateCachedJTSPeer();
        return result;
    }
    public T set(int index, T element) {
        T result = super.set(index, element);
        if (parent != null) parent.invalidateCachedJTSPeer();
        return result;
    }
}
