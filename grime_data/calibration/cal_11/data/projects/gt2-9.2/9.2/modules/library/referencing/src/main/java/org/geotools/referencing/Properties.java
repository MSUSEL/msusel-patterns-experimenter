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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.ReferenceSystem;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.operation.CoordinateOperation;

import org.geotools.util.MapEntry;
import org.geotools.referencing.operation.AbstractCoordinateOperation;


/**
 * An immutable map fetching all properties from the specified identified object. Calls
 * to {@code get} methods are forwarded to the appropriate {@link IdentifiedObject} method.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class Properties extends AbstractMap<String,Object> {
    /**
     * The object where all properties come from.
     */
    private final IdentifiedObject info;

    /**
     * The entries set. Will be constructed only when first needed.
     */
    private transient Set<Entry<String,Object>> entries;

    /**
     * Creates new properties from the specified identified object.
     */
    public Properties(final IdentifiedObject info) {
        this.info = info;
        AbstractIdentifiedObject.ensureNonNull("info", info);
    }

    /**
     * Returns true if this map contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(final Object key) {
        return get(key) != null;
    }

    /**
     * Returns the value to which this map maps the specified key.
     * Returns null if the map contains no mapping for this key.
     */
    @Override
    public Object get(final Object key) {
        if (key instanceof String) {
            final String s = ((String) key).trim();
            for (int i=0; i<KEYS.length; i++) {
                if (KEYS[i].equalsIgnoreCase(s)) {
                    return get(i);
                }
            }
        }
        return null;
    }

    /**
     * Returns the value to which this map maps the specified index.
     * Returns null if the map contains no mapping for this index.
     */
    private Object get(final int key) {
        switch (key) {
            case 0: return info.getName();
            case 1: return info.getIdentifiers().toArray(AbstractIdentifiedObject.EMPTY_IDENTIFIER_ARRAY);
            case 2: return info.getAlias().toArray(AbstractIdentifiedObject.EMPTY_ALIAS_ARRAY);
            case 3: return info.getRemarks();
            case 4: {
                if (info instanceof ReferenceSystem) {
                    return ((ReferenceSystem) info).getScope();
                } else if (info instanceof Datum) {
                    return ((Datum) info).getScope();
                } else if (info instanceof CoordinateOperation) {
                    return ((CoordinateOperation) info).getScope();
                }
                break;
            }
            case 5: {
                if (info instanceof ReferenceSystem) {
                    return ((ReferenceSystem) info).getDomainOfValidity();
                } else if (info instanceof Datum) {
                    return ((Datum) info).getDomainOfValidity();
                } else if (info instanceof CoordinateOperation) {
                    return ((CoordinateOperation) info).getDomainOfValidity();
                }
                break;
            }
            case 6: {
                if (info instanceof CoordinateOperation) {
                    return ((CoordinateOperation) info).getOperationVersion();
                }
                break;
            }
            case 7: {
                if (info instanceof CoordinateOperation) {
                    return ((CoordinateOperation) info).getCoordinateOperationAccuracy()
                            .toArray(AbstractCoordinateOperation.EMPTY_ACCURACY_ARRAY);
                }
                break;
            }
        }
        return null;
    }

    /**
     * The keys to search for. <STRONG>The index of each element in this array
     * must matches the index searched by {@link #get(int)}.</STRONG>
     *
     * @todo Add properties for {@link IdentifiedObject} sub-interfaces.
     */
    private static final String[] KEYS = {
        /*[0]*/IdentifiedObject    .NAME_KEY,
        /*[1]*/IdentifiedObject    .IDENTIFIERS_KEY,
        /*[2]*/IdentifiedObject    .ALIAS_KEY,
        /*[3]*/IdentifiedObject    .REMARKS_KEY,
        /*[4]*/CoordinateOperation .SCOPE_KEY,              // same in Datum and ReferenceSystem
        /*[5]*/CoordinateOperation .DOMAIN_OF_VALIDITY_KEY, // same in Datum and ReferenceSystem
        /*[6]*/CoordinateOperation .OPERATION_VERSION_KEY,
        /*[7]*/CoordinateOperation .COORDINATE_OPERATION_ACCURACY_KEY
    };

    /**
     * Returns a set view of the mappings contained in this map.
     */
    @Override
    public Set<Entry<String,Object>> entrySet() {
        if (entries == null) {
            entries = new HashSet<Entry<String,Object>>(Math.round(KEYS.length / 0.75f) + 1, 0.75f);
            for (int i=0; i<KEYS.length; i++) {
                final Object value = get(i);
                if (value != null) {
                    entries.add(new MapEntry<String,Object>(KEYS[i], value));
                }
            }
            entries = Collections.unmodifiableSet(entries);
        }
        return entries;
    }
}
