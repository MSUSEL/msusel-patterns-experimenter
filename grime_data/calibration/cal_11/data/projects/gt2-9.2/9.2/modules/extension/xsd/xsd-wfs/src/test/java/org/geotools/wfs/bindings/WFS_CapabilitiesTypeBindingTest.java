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
package org.geotools.wfs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.GMLObjectTypeListType;
import net.opengis.wfs.GMLObjectTypeType;
import net.opengis.wfs.WFSCapabilitiesType;

import org.geotools.filter.v1_1.OGC;
import org.geotools.gml3.GML;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.ComparisonOperators;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.capability.Functions;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.IdCapabilities;
import org.opengis.filter.capability.Operator;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test suite for {@link WFS_CapabilitiesTypeBinding}
 * 
 * @author Justin Deoliveira
 * @version $Id: WFS_CapabilitiesTypeBindingTest.java 27749 2007-11-05 09:51:33Z
 *          groldan $
 * @since 2.5.x
 *
 *
 * @source $URL$
 */
public class WFS_CapabilitiesTypeBindingTest extends WFSTestSupport {
    public WFS_CapabilitiesTypeBindingTest() {
        super(WFS.WFS_CapabilitiesType, WFSCapabilitiesType.class, Binding.OVERRIDE);
    }

    public void testParse() throws Exception {
        String xml = "<WFS_Capabilities version=\"1.1.0\">" + "<FeatureTypeList/>"
                + "</WFS_Capabilities>";
        buildDocument(xml);

        WFSCapabilitiesType caps = (WFSCapabilitiesType) parse();
        assertEquals("1.1.0", caps.getVersion());

        assertNotNull(caps.getFeatureTypeList());
    }

    public void testEncode() throws Exception {
        WFSCapabilitiesType caps = factory.createWFSCapabilitiesType();
        caps.setVersion("1.1.0");
        {
            FeatureTypeListType ftl = factory.createFeatureTypeListType();
            caps.setFeatureTypeList(ftl);
        }
        {
            GMLObjectTypeListType servesGmlObj = factory.createGMLObjectTypeListType();
            GMLObjectTypeType gmlObj = factory.createGMLObjectTypeType();
            gmlObj.setAbstract("abstract");
            gmlObj.setName(GML._Feature);
            gmlObj.setTitle("title");
            servesGmlObj.getGMLObjectType().add(gmlObj);
            caps.setServesGMLObjectTypeList(servesGmlObj);
        }
        {
            GMLObjectTypeListType supportsGmlObj = factory.createGMLObjectTypeListType();
            GMLObjectTypeType type = factory.createGMLObjectTypeType();
            type.setName(GML._Feature);
            supportsGmlObj.getGMLObjectType().add(type);
            caps.setSupportsGMLObjectTypeList(supportsGmlObj);
        }
        FilterCapabilities filterCaps = createTestFilterCapabilities();
        caps.setFilterCapabilities(filterCaps);

        final Document dom = encode(caps, WFS.WFS_Capabilities);
        final Element root = dom.getDocumentElement();

        assertName(WFS.WFS_Capabilities, root);
        assertFeatureTypeList(root);
        assertServesGMLObjectType(root);
        assertSupportsGMLObjectType(root);
        assertFilterCapabilities(root);
    }

    private void assertFeatureTypeList(Element root) {
        Element ftl = getElementByQName(root, WFS.FeatureTypeList);
        assertNotNull(ftl);
        // NOTE the deep test of FeatureTypeList is done in its own test suite
    }

    private void assertServesGMLObjectType(Element root) {
        Element servesGmlTypeList = getElementByQName(root, WFS.ServesGMLObjectTypeList);
        assertNotNull(servesGmlTypeList);

        Element type = getElementByQName(servesGmlTypeList, new QName(WFS.NAMESPACE,
                "GMLObjectType"));
        assertNotNull(type);
        Element name = getElementByQName(type, new QName(WFS.NAMESPACE, "Name"));
        assertEquals("gml:_Feature", name.getFirstChild().getNodeValue());
    }

    private void assertSupportsGMLObjectType(Element root) {
        Element supportsGmlTypeList = getElementByQName(root, WFS.SupportsGMLObjectTypeList);
        assertNotNull(supportsGmlTypeList);

        Element type = getElementByQName(supportsGmlTypeList, new QName(WFS.NAMESPACE,
                "GMLObjectType"));
        assertNotNull(type);
        Element name = getElementByQName(type, new QName(WFS.NAMESPACE, "Name"));
        assertEquals("gml:_Feature", name.getFirstChild().getNodeValue());
    }

    private void assertFilterCapabilities(Element root) {
        Element filterCaps = getElementByQName(root, OGC.Filter_Capabilities);
        assertNotNull(filterCaps);
    }

    private FilterCapabilities createTestFilterCapabilities() {
        final String version = "1.1.0";
        final ScalarCapabilities scalarCaps;
        final SpatialCapabilities spatialCaps;
        final IdCapabilities idCaps;
        {
            Operator[] operators = { filterFac.operator("LessThan"),
                    filterFac.operator("GreaterThan") };
            ComparisonOperators comparisonOps = filterFac.comparisonOperators(operators);

            boolean simple = true;
            FunctionName[] functionNames = { filterFac.functionName("MIN", 2),
                    filterFac.functionName("ABS", 1) };

            Functions functions = filterFac.functions(functionNames);
            final ArithmeticOperators aritmeticOps = filterFac.arithmeticOperators(simple,
                    functions);

            final boolean logicalOps = true;
            scalarCaps = filterFac.scalarCapabilities(comparisonOps, aritmeticOps, logicalOps);
        }
        {
            GeometryOperand[] geomOperands = { GeometryOperand.Envelope, GeometryOperand.Point };
            String name = "Disjoint";
            GeometryOperand[] operands = {};
            SpatialOperator[] spatialOps = { filterFac.spatialOperator(name, operands) };
            SpatialOperators spatialOperators = filterFac.spatialOperators(spatialOps);
            spatialCaps = filterFac.spatialCapabilities(geomOperands, spatialOperators);
        }
        {
            boolean eid = true;
            boolean fid = true;
            idCaps = filterFac.idCapabilities(eid, fid);
        }

        FilterCapabilities filterCaps;
        filterCaps = filterFac.capabilities(version, scalarCaps, spatialCaps, idCaps);
        return filterCaps;
    }
}
