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
package org.geotools.xml;

import org.geotools.filter.FilterFilter;
import org.geotools.filter.FilterHandler;
import org.geotools.gml.GMLFilterDocument;
import org.geotools.gml.GMLFilterGeometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

@SuppressWarnings("unused")
public class FilterXMLExamples {

private void saxExample() throws Exception {
    SimpleFeatureType featureType = null;
    InputSource input = null;
    // saxExample start
    
    class SimpleFilterHandler extends DefaultHandler implements FilterHandler {
    public Filter filter;
    
    public void filter(Filter filter) {
        this.filter = filter;
    }
    }
    
    SimpleFilterHandler simpleFilterHandler = new SimpleFilterHandler();
    FilterFilter filterFilter = new FilterFilter(simpleFilterHandler, featureType);
    GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFilter);
    GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);
    
    // parse xml
    XMLReader reader = XMLReaderFactory.createXMLReader();
    reader.setContentHandler(filterDocument);
    reader.parse(input);
    Filter filter = simpleFilterHandler.filter;
    // saxExample end
}
}
