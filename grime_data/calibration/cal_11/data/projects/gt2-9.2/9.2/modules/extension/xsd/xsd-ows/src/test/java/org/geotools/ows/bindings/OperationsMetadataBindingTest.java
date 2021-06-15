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
package org.geotools.ows.bindings;

import net.opengis.ows10.OperationsMetadataType;

import org.geotools.ows.OWSTestSupport;

/**
 * 
 *
 * @source $URL$
 */
public class OperationsMetadataBindingTest extends OWSTestSupport {

    public void testParse() throws Exception {
        String xml = " <ows:OperationsMetadata xmlns:ows=\"http://www.opengis.net/ows\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">"+
        "  <ows:Operation name=\"GetCapabilities\">"+
        "   <ows:DCP>"+
        "    <ows:HTTP>"+
        "     <ows:Get xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer?\" />"+
        "     <ows:Post xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer\" />"+
        "    </ows:HTTP>"+
        "   </ows:DCP>"+
        "   <ows:Parameter name=\"AcceptVersions\">"+
        "    <ows:Value>1.1.0</ows:Value>"+
        "    <ows:Value>1.0.0</ows:Value>"+
        "   </ows:Parameter>"+
        "   <ows:Parameter name=\"AcceptFormats\">"+
        "    <ows:Value>text/xml</ows:Value>"+
        "   </ows:Parameter>"+
        "  </ows:Operation>"+
        "  <ows:Operation name=\"DescribeFeatureType\">"+
        "   <ows:DCP>"+
        "    <ows:HTTP>"+
        "     <ows:Get xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer?\" />"+
        "     <ows:Post xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer\" />"+
        "    </ows:HTTP>"+
        "   </ows:DCP>"+
        "   <ows:Parameter name=\"outputFormat\">"+
        "    <ows:Value>text/xml; subType=gml/3.1.1/profiles/gmlsf/1.0.0/0</ows:Value>"+
        "   </ows:Parameter>"+
        "  </ows:Operation>"+
        "  <ows:Operation name=\"GetFeature\">"+
        "   <ows:DCP>"+
        "    <ows:HTTP>"+
        "     <ows:Get xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer?\" />"+
        "     <ows:Post xlink:href=\"http://arcgisserver:8399/arcgis/services/wijken/MapServer/WFSServer\" />"+
        "    </ows:HTTP>"+
        "   </ows:DCP>"+
        "   <ows:Parameter name=\"resultType\">"+
        "    <ows:Value>results</ows:Value>"+
        "    <ows:Value>hits</ows:Value>"+
        "   </ows:Parameter>"+
        "   <ows:Parameter name=\"outputFormat\">"+
        "    <ows:Value>text/xml; subType=gml/3.1.1/profiles/gmlsf/1.0.0/0</ows:Value>"+
        "   </ows:Parameter>"+
        "  </ows:Operation>"+
        "  <ows:ExtendedCapabilities>"+
        "   <ows:Constraint name=\"serviceAxisOrderForSwappableSRS\">"+
        "    <ows:Value>latitude,longitude</ows:Value>"+
        "   </ows:Constraint>"+
        "  </ows:ExtendedCapabilities>"+
        " </ows:OperationsMetadata>";

        buildDocument(xml);
        OperationsMetadataType ops = (OperationsMetadataType) parse();
        assertNotNull(ops);
    }
}
