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
package org.geotools.styling.builder;

import static org.junit.Assert.*;

import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedStyle;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.junit.Test;
import org.opengis.filter.PropertyIsGreaterThan;

/**
 * 
 *
 * @source $URL$
 */
public class SLDTest extends AbstractStyleTest {

    @Test
    public void testSimpleNamed() {
        StyledLayerDescriptor sld = new NamedLayerBuilder().name("states").style()
                .featureTypeStyle().rule().point().buildSLD();
        // print(sld);

        StyleCollector collector = new StyleCollector();
        sld.accept(collector);
        assertSimpleStyle(collector);

        NamedLayer layer = (NamedLayer) collector.layers.get(0);
        assertEquals("states", layer.getName());
        assertTrue(collector.symbolizers.get(0) instanceof PointSymbolizer);
    }

    @Test
    public void testNamedStyle() {
        StyledLayerDescriptor sld = new NamedLayerBuilder().name("states").style()
                .name("population").buildSLD();
        // print(sld);

        StyleCollector collector = new StyleCollector();
        sld.accept(collector);
        assertEquals(0, collector.featureTypeStyles.size());
        assertEquals(0, collector.rules.size());
        assertEquals(0, collector.symbolizers.size());
        assertEquals(1, collector.styles.size());
        assertEquals(1, collector.layers.size());

        NamedLayer layer = (NamedLayer) collector.layers.get(0);
        assertEquals("states", layer.getName());
        NamedStyle ns = (NamedStyle) layer.getStyles()[0];
        assertEquals("population", ns.getName());
    }

    @Test
    public void testRemoteOWS() {
        PropertyIsGreaterThan tenMillionPeople = ff.greater(ff.property("PERSONS"), ff.literal(10000000));

        UserLayerBuilder lb = new UserLayerBuilder();
        lb.remoteOWS("http://geoserver.org/geoserver/ows", "WFS");
        lb.featureTypeConstraint().featureTypeName("states")
                .filter(tenMillionPeople);
        lb.style().featureTypeStyle().rule().polygon().fill();
        StyledLayerDescriptor sld = lb.buildSLD();
        // print(sld);
        
        StyleCollector collector = new StyleCollector();
        sld.accept(collector);
        assertSimpleStyle(collector);

        UserLayer layer = (UserLayer) collector.layers.get(0);
        assertEquals("http://geoserver.org/geoserver/ows", layer.getRemoteOWS().getOnlineResource());
        assertEquals("WFS", layer.getRemoteOWS().getService());
        FeatureTypeConstraint constraint = layer.getLayerFeatureConstraints()[0];
        assertEquals("states", constraint.getFeatureTypeName());
        assertEquals(tenMillionPeople, constraint.getFilter());
    }
}
