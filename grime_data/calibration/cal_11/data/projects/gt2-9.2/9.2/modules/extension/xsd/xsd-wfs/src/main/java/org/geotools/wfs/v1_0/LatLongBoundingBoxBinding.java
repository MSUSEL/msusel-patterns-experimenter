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
package org.geotools.wfs.v1_0;

import java.math.BigInteger;
import java.util.Arrays;

import javax.xml.namespace.QName;

import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.WGS84BoundingBoxType;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class LatLongBoundingBoxBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.LatLongBoundingBox;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return WGS84BoundingBoxType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        Double minx = Double.valueOf((String) node.getAttributeValue("minx"));
        Double miny = Double.valueOf((String) node.getAttributeValue("miny"));
        Double maxx = Double.valueOf((String) node.getAttributeValue("maxx"));
        Double maxy = Double.valueOf((String) node.getAttributeValue("maxy"));
        WGS84BoundingBoxType bbox = Ows10Factory.eINSTANCE.createWGS84BoundingBoxType();
        bbox.setCrs("EPSG:4326");
        bbox.setDimensions(BigInteger.valueOf(2));
        bbox.setLowerCorner(Arrays.asList(minx, miny));
        bbox.setUpperCorner(Arrays.asList(maxx, maxy));

        return bbox;

    }
}
