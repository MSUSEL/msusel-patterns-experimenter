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
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.ows10.DCPType;
import net.opengis.ows10.HTTPType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.RequestMethodType;
import net.opengis.ows10.WGS84BoundingBoxType;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class DCPTypeBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.DCPType;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return DCPType.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Ows10Factory ows10Factory = Ows10Factory.eINSTANCE;

        DCPType dcpType = ows10Factory.createDCPType();
        HTTPType httpType = ows10Factory.createHTTPType();
        dcpType.setHTTP(httpType);

        List<Node> httpChildren = node.getChildren("HTTP");
        for (Node http : httpChildren) {
            Node get = http.getChild("Get");
            if (get != null) {
                RequestMethodType methodType = createRequestMethodType(ows10Factory, get);
                httpType.getGet().add(methodType);
            }
            Node post = http.getChild("Post");
            if (post != null) {
                RequestMethodType methodType = createRequestMethodType(ows10Factory, post);
                httpType.getPost().add(methodType);
            }
        }

        return dcpType;

    }

    private RequestMethodType createRequestMethodType(Ows10Factory ows10Factory, Node getOrPostNode) {
        RequestMethodType methodType = ows10Factory.createRequestMethodType();
        String href = (String) getOrPostNode.getAttributeValue("onlineResource");
        methodType.setHref(href);
        return methodType;
    }
}
