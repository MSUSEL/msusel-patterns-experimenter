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
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson;

import java.io.IOException;
import java.util.LinkedList;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

/**
 * Handler that records sequence of calls to be replayed layer.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class RecordingHandler implements ContentHandler {

    LinkedList<Action<?>> actions = new LinkedList<Action<?>>();

    @Override
    public void startJSON() throws ParseException, IOException {
        actions.add(new Action<Object>() {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.startJSON();
            }
        });
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        return actions.add(new Action<Object>() {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.startObject();
            }
        });
    }
    
    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        return actions.add(new Action<String>(key) {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.startObjectEntry(obj);
            }
        });
    }

    @Override
    public boolean startArray() throws ParseException, IOException {
        return actions.add(new Action<Object>() {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.startArray();
            }
        });
    }

    @Override
    public boolean primitive(Object obj) throws ParseException, IOException {
        return actions.add(new Action<Object>(obj) {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.primitive(obj);
            }
        });
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        return actions.add(new Action<Object>() {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.endArray();
            }
        });
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {
        return actions.add(new Action<Object>() {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.endObjectEntry();
            }
        });
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        return actions.add(new Action<Object>() {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.endObject();
            }
        });
    }

    @Override
    public void endJSON() throws ParseException, IOException {
        actions.add(new Action<Object>() {
            @Override
            protected void run(ContentHandler handler) throws ParseException, IOException {
                handler.endJSON();
            }
        });
    }

    public void replay(ContentHandler handler) throws ParseException, IOException{
        while(!actions.isEmpty()) {
            actions.removeFirst().run(handler);
        }
    }

    abstract class Action<T> {

        protected T obj;

        Action() {
            this(null);
        }

        Action(T obj) {
            this.obj = obj;
        }

        protected abstract void run(ContentHandler handler) throws ParseException, IOException;
    }
}
