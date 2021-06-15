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
package org.geotools.data.wfs.internal.parsers;

import static org.geotools.data.wfs.impl.WFSTestData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.data.wfs.impl.WFSTestData;
import org.geotools.data.wfs.internal.parsers.EmfAppSchemaParser;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.geotools.wfs.v1_1.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Unit test suite for {@link EmfAppSchemaParser}
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.x
 * 
 * 
 * 
 * @source $URL$
 */
public class EmfAppSchemaParserTest {

    /**
     * Test method for {@link EmfAppSchemaParser#parse(javax.xml.namespace.QName, java.net.URL)}.
     * 
     * @throws IOException
     */
    @Test
    public void testParseGeoServerSimpleFeatureType() throws IOException {
        final QName featureTypeName = new QName("http://www.openplans.org/topp", "states");

        final URL schemaLocation = url("GeoServer_2.0/1.1.0/DescribeFeatureType_states.xsd");

        final int expectedAttributeCount = 23;

        SimpleFeatureType ftype = testParseDescribeSimpleFeatureType(featureTypeName,
                schemaLocation, expectedAttributeCount);
        assertNotNull(ftype);
        assertNotNull(ftype.getGeometryDescriptor());
        assertEquals("the_geom", ftype.getGeometryDescriptor().getLocalName());
    }

    @Test
    public void testParseCubeWerx_GML_Level1_FeatureType() throws IOException {
        final QName featureTypeName = new QName("http://www.fgdc.gov/framework/073004/gubs",
                "GovernmentalUnitCE");
        new WFSTestData();
        final URL schemaLocation = WFSTestData
                .url("CubeWerx_nsdi/1.1.0/DescribeFeatureType_GovernmentalUnitCE.xsd");

        // Expect only the subset of simple attributes:
        // {typeAbbreviation:String,instanceName:String,officialDescription:String,
        // instanceCode:String,codingSystemReference:String,geometry:"gml:SurfacePropertyType",
        // typeDefinition:String}.
        // Plus, the following ones are being assigned multiplicity 0:1 by the
        // parser when they're not:
        // {instanceAlternateName:String[0..*],codingSystemReference:String[0..*]}
        // And the last one: governmentalUnitType has a complex type, yet it
        // gets parsed as String
        // and I can't find out why (would be happier if it were bound to Object.class)
        // 2008-06-05 update: governmentalUnitType no longer gets parsed by the gtxml parser
        // at all, so reducing expectedAttributeCount from 10 to 9
        // 2011-08-21 update: governmentalUnitType gets parsed again by the gtxml parser, so
        // setting expectedAttributeCount back to 10
        final int expectedAttributeCount = 10;

        SimpleFeatureType ftype = testParseDescribeSimpleFeatureType(featureTypeName,
                schemaLocation, expectedAttributeCount);
        for (AttributeDescriptor descriptor : ftype.getAttributeDescriptors()) {
            System.out.print(descriptor.getName().getNamespaceURI());
            System.out.print("#");
            System.out.print(descriptor.getName().getLocalPart());
            System.out.print("[" + descriptor.getMinOccurs() + ":" + descriptor.getMaxOccurs()
                    + "]");
            System.out.print(" (" + descriptor.getType().getName() + ": "
                    + descriptor.getType().getBinding() + ")");
            System.out.println("");
        }
    }

    /**
     * @param featureTypeName
     * @param schemaLocation
     * @param expectedAttributeCount
     * @return
     * @throws IOException
     * @see {@link EmfAppSchemaParser#parseSimpleFeatureType(Configuration, QName, URL, CoordinateReferenceSystem)}
     */
    private SimpleFeatureType testParseDescribeSimpleFeatureType(final QName featureTypeName,
            final URL schemaLocation, int expectedAttributeCount) throws IOException {
        assertNotNull(schemaLocation);
        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

        Configuration configuration = new WFSConfiguration();

        SimpleFeatureType featureType;
        featureType = EmfAppSchemaParser.parseSimpleFeatureType(configuration, featureTypeName,
                schemaLocation, crs);

        assertNotNull(featureType);
        assertSame(crs, featureType.getCoordinateReferenceSystem());

        List<AttributeDescriptor> attributes = featureType.getAttributeDescriptors();
        List<String> names = new ArrayList<String>(attributes.size());
        for (AttributeDescriptor desc : attributes) {
            names.add(desc.getLocalName());
        }
        assertEquals(names.toString(), expectedAttributeCount, attributes.size());
        return featureType;
    }
}
