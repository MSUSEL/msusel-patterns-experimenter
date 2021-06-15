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
package org.geotools.kml.v22;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.impl.ElementHandler;
import org.geotools.xml.impl.Handler;
import org.geotools.xml.impl.HandlerFactory;
import org.geotools.xml.impl.HandlerFactoryImpl;
import org.geotools.xml.impl.ParserHandler;

public class KMLCustomSchemaHandlerFactory extends HandlerFactoryImpl implements HandlerFactory {

    private final SchemaRegistry schemaRegistry;

    public KMLCustomSchemaHandlerFactory(SchemaRegistry schemaRegistry) {
        super();
        this.schemaRegistry = schemaRegistry;
    }

    @Override
    public ElementHandler createElementHandler(QName qName, Handler parent, ParserHandler parser) {
        String name = qName.getLocalPart();
        if (schemaRegistry.get(name) != null) {
            // we found a custom schema element
            // let's treat it as if we've found a placemark
            SchemaIndex schemaIndex = parser.getSchemaIndex();
            XSDElementDeclaration element = schemaIndex.getElementDeclaration(KML.Placemark);
            if (element != null) {
                return createElementHandler(element, parent, parser);
            }
        }
        return super.createElementHandler(qName, parent, parser);
    }
}
