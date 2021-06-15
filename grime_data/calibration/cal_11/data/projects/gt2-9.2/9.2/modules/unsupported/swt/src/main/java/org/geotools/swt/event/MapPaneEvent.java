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

package org.geotools.swt.event;

import java.util.EventObject;

import org.geotools.swt.SwtMapPane;

/**
 * An event class used by {@code SwtMapPane} to signal changes of
 * state to listeners.
 *
 * 
 * @author Michael Bedward
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public class MapPaneEvent extends EventObject {
    private static final long serialVersionUID = 1L;

    /**
     * Type of MapPane event
     */
    public static enum Type {
        /**
         * The map pane has set a new context.
         */
        NEW_CONTEXT,

        /**
         * The map pane has set a new renderer.
         */
        NEW_RENDERER,

        /**
         * The map pane has been resized.
         */
        PANE_RESIZED,

        /**
         * The display area has been changed. This can
         * include both changes in bounds and in the
         * coordinate reference system.
         */
        DISPLAY_AREA_CHANGED,

        /**
         * The map pane has started rendering features.
         */
        RENDERING_STARTED,

        /**
         * The map pane has stopped rendering features.
         */
        RENDERING_STOPPED,

        /**
         * The map pane is rendering features. The event
         * will carry data that can be retrieved as a floating
         * point value between 0 and 1.
         */
        RENDERING_PROGRESS;
    }

    /** Type of mappane event */
    private Type type;

    /** Data associated with some event types */
    private Object data;

    /**
     * Constructor for an event with no associated data.
     *
     * @param source the map pane issuing this event.
     * @param type the type of event.
     */
    public MapPaneEvent( SwtMapPane source, Type type ) {
        super(source);
        this.type = type;
    }

    /**
     * Constructor for an event with associated data. The new event
     * object takes ownership of the data object.
     *
     * @param source the map pane issuing this event.
     * @param type the type of event.
     * @param data the event data.
     */
    public MapPaneEvent( SwtMapPane source, Type type, Object data ) {
        super(source);
        this.type = type;
        this.data = data;
    }

    /**
     * Get the type of this event.
     * 
     * @return event type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the data associated with this event, if any.
     *
     * @return event data or <code>null</code> if not applicable.
     */
    public Object getData() {
        return data;
    }
}
