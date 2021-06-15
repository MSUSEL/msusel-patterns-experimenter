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
package org.geotools.xml.impl.jxpath;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathContextFactory;
import org.apache.commons.jxpath.JXPathIntrospector;
import java.util.Iterator;
import org.geotools.xml.Configuration;
import org.geotools.xml.Node;
import org.geotools.xml.impl.DocumentHandler;
import org.geotools.xml.impl.ElementHandler;
import org.geotools.xml.impl.ElementHandlerImpl;
import org.geotools.xml.impl.NodeImpl;
import org.geotools.xml.impl.StreamingParserHandler;


/**
 * 
 *
 * @source $URL$
 */
public class JXPathStreamingParserHandler extends StreamingParserHandler {
    /** xpath to stream **/
    String xpath;

    public JXPathStreamingParserHandler(Configuration config, String xpath) {
        super(config);

        this.xpath = xpath;
    }

    protected boolean stream(ElementHandler handler) {
        //create an xpath context from the root element
        // TODO: cache the context, should work just the same
        //        JXPathIntrospector.registerDynamicClass(ElementHandlerImpl.class,
        //            ElementHandlerPropertyHandler.class);
        JXPathIntrospector.registerDynamicClass(NodeImpl.class, NodePropertyHandler.class);

        //        ElementHandler rootHandler = 
        //        	((DocumentHandler) handlers.firstElement()).getDocumentElementHandler();
        Node root = ((DocumentHandler) handlers.firstElement()).getParseNode();
        JXPathContext jxpContext = JXPathContextFactory.newInstance().newContext(null, root);

        jxpContext.setLenient(true);

        Iterator itr = jxpContext.iterate(xpath);

        for (; itr.hasNext();) {
            Object obj = itr.next();

            if (handler.getParseNode().equals(obj)) {
                return true;
            }
        }

        return false;
    }
}
