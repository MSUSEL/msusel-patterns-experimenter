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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map.event;

import java.util.EventObject;

import org.geotools.map.DefaultMapLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;

/**
 * Event object used to report changes in the list of layers managed by a MapContext
 * 
 * @author wolf
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/library/render/src/main/java/org/geotools
 *         /map/event/MapLayerListEvent.java $
 */
public class MapLayerListEvent extends EventObject {
    /** Holds value of property layer. */
    private MapLayer mapLayer;

    private Layer layer;

    /** Holds value of property fromIndex. */
    private int fromIndex;

    /** Holds value of property toIndex. */
    private int toIndex;

    /** Holds value of property mapLayerEvent. */
    private MapLayerEvent mapLayerEvent;

    /**
     * Creates a new instance of MapLayerListEvent
     * 
     * @param source
     *            DOCUMENT ME!
     * @param layer
     *            DOCUMENT ME!
     * @param fromIndex
     *            DOCUMENT ME!
     * @param toIndex
     *            DOCUMENT ME!
     */
    public MapLayerListEvent(MapContext source, MapLayer layer, int fromIndex, int toIndex) {
        super(source);
        this.mapLayer = layer;
        this.layer = layer == null ? null : layer.toLayer();
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    /**
     * Creates a new instance of MapLayerListEvent
     * 
     * @param source
     *            Map issuing the event
     * @param layer
     *            Layer being reported against; may be null
     * @param position
     *            index modified in layer list
     */
    public MapLayerListEvent(MapContext source, MapLayer layer, int position) {
        super(source);
        this.mapLayer = layer;
        this.layer = layer == null ? null : layer.toLayer();
        this.fromIndex = position;
        this.toIndex = position;
    }

    /**
     * Creates a new instance of MapLayerListEvent passing on an event from a layer.
     * 
     * @param source
     *            Map issuing the event
     * @param layer
     *            Layer issuing the event
     * @param position
     *            Position in the layer list
     * @param mapLayerEvent
     *            Event provided from the layer
     */
    public MapLayerListEvent(MapContext source, MapLayer layer, int position,
            MapLayerEvent mapLayerEvent) {
        this(source, layer, position);
        this.mapLayerEvent = mapLayerEvent;
    }

    public MapLayerListEvent(MapContent map, Layer element, int index) {
        super(map);
        this.layer = element;
        this.mapLayer = element == null ? null : new DefaultMapLayer(element);
        this.fromIndex = index;
        this.toIndex = index;
    }

    public MapLayerListEvent(MapContent map, Layer element, int fromIndex, int toIndex) {
        super(map);
        this.layer = element;
        this.mapLayer = element == null ? null : new DefaultMapLayer(element);
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    public MapLayerListEvent(MapContent map, Layer element, int index, MapLayerEvent mapLayerEvent) {
        super(map);
        this.layer = element;
        this.mapLayer = element == null ? null : new DefaultMapLayer(element);
        this.fromIndex = index;
        this.toIndex = index;
        this.mapLayerEvent = mapLayerEvent;
    }

    /**
     * Returns the layer involved in the change
     * 
     * @return Value of property layer.
     */
    public Layer getElement() {
        return this.layer;
    }

    /**
     * Return the layer involved in the change.
     * @return
     */
    public MapLayer getLayer() {
        return mapLayer;
    }

    /**
     * Returns the index of the first layer involved in the change
     * 
     * @return The old index of the layer. -1 will be returned if the layer was not in the
     *         MapContext
     */
    public int getFromIndex() {
        return this.fromIndex;
    }

    /**
     * Returns the index of the last layer involved in the change
     * 
     * @return The old index of the layer. -1 will be returned if the layer is no more in the
     *         MapContext
     */
    public int getToIndex() {
        return this.toIndex;
    }

    /**
     * Returns the map layer event that originated this layer list event
     * 
     * @return Value of property mapLayerEvent.
     */
    public MapLayerEvent getMapLayerEvent() {
        return this.mapLayerEvent;
    }
}
