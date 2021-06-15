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
package org.geotools.data.shapefile.shp.xml;

import java.io.IOException;
import java.io.InputStream;

import org.geotools.data.shapefile.FileReader;
import org.geotools.data.shapefile.ShpFileType;
import org.geotools.data.shapefile.ShpFiles;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.vividsolutions.jts.geom.Envelope;

/**
 * 
 *
 * @source $URL$
 */
public class ShpXmlFileReader implements FileReader {

    Document dom;

    /**
     * Parse metadataFile (currently for bounding box information).
     * <p>
     * 
     * </p>
     * 
     * @param shapefileFiles
     * @throws JDOMException
     * @throws IOException
     */
    public ShpXmlFileReader(ShpFiles shapefileFiles) throws JDOMException,
            IOException {
        SAXBuilder builder = new SAXBuilder(false);

        InputStream inputStream = shapefileFiles.getInputStream(
                ShpFileType.SHP_XML, this);
        try {
            dom = builder.build(inputStream);
        } finally {
            inputStream.close();
        }
    }

    public Metadata parse() {
        return parseMetadata(dom.getRootElement());
    }

    protected Metadata parseMetadata(Element root) {
        Metadata meta = new Metadata();
        meta.setIdinfo(parseIdInfo(root.getChild("idinfo")));

        return meta;
    }

    protected IdInfo parseIdInfo(Element element) {
        IdInfo idInfo = new IdInfo();

        Element bounding = element.getChild("spdom").getChild("bounding");
        idInfo.setBounding(parseBounding(bounding));

        Element lbounding = element.getChild("spdom").getChild("lbounding");
        idInfo.setLbounding(parseBounding(lbounding));

        return idInfo;
    }

    protected Envelope parseBounding(Element bounding) {
        if (bounding == null)
            return new Envelope();

        double minX = Double.parseDouble(bounding.getChildText("westbc"));
        double maxX = Double.parseDouble(bounding.getChildText("eastbc"));
        double minY = Double.parseDouble(bounding.getChildText("southbc"));
        double maxY = Double.parseDouble(bounding.getChildText("northbc"));

        return new Envelope(minX, maxX, minY, maxY);
    }

    public String id() {
        return "Shp Xml Reader";
    }

}
