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
 *    (C) 2003-2011, Open Source Geospatial Foundation (OSGeo)
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


// J2SE dependencies
import java.util.EventListener;

/**
 * The listener that's notified when some {@linkplain MapLayer layer} property changes.
 *
 * @author Andrea Aime
 *
 * @see org.geotools.map.MapLayer
 * @see MapLayerEvent
 *
 *
 * @source $URL$
 */
public interface MapLayerListener extends EventListener {
    /**
     * Invoked when some property of this layer has changed. May be data,  style, title,
     * visibility.
     *
     * @param event encapsulating the event information
     */
    void layerChanged(MapLayerEvent event);

    /**
     * Invoked when the component has been made visible.
     *
     * @param event encapsulating the event information
     */
    void layerShown(MapLayerEvent event);

    /**
     * nvoked when the component has been made invisible.
     *
     * @param event encapsulating the event information
     */
    void layerHidden(MapLayerEvent event);

    /**
     * Invoked when the component has been set as selected.
     *
     * @param event encapsulating the event information
     */
    void layerSelected(MapLayerEvent event);

    /**
     * Invoked when the component has been set as not selected.
     *
     * @param event encapsulating the event information
     */
    void layerDeselected(MapLayerEvent event);
    
    /**
     * Invoked when the layer is scheduled for disposal to give listeners
     * the chance to finish or cancel any tasks involving the layer.
     * 
     * @param event encapsulating the event information
     */
    void layerPreDispose(MapLayerEvent event);
}
