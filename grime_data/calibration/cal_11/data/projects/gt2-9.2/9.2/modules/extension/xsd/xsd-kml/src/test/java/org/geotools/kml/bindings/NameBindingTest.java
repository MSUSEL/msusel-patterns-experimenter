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
package org.geotools.kml.bindings;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.geotools.kml.Folder;
import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xml.Binding;
import org.opengis.feature.simple.SimpleFeature;

public class NameBindingTest extends KMLTestSupport {

    public void testType() throws Exception {
        assertEquals(String.class, binding(KML.name).getType());
    }

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(KML.name).getExecutionMode());
    }

    public void testParseName() throws Exception {
        String xml = "<name>fleem</name>";
        buildDocument(xml);

        String name = (String) parse();
        assertEquals("fleem", name);
    }

    public void testParseNameInFolder() throws Exception {
        String xml = "<kml><Folder>" + "<name>foo</name>" + "<Placemark>" + "<name>bar</name>"
                + "</Placemark>" + "</Folder></kml>";
        buildDocument(xml);

        SimpleFeature document = (SimpleFeature) parse();
        assertEquals("foo", document.getAttribute("name"));

        @SuppressWarnings("unchecked")
        Collection<SimpleFeature> features = (Collection<SimpleFeature>) document
                .getAttribute("Feature");
        assertEquals(1, features.size());
        SimpleFeature feature = features.iterator().next();
        Map<Object, Object> userData = feature.getUserData();
        Object folderObject = userData.get("Folder");
        assertNotNull("No folder user data", folderObject);
        assertTrue("Unknown folder object in user data", folderObject instanceof List<?>);
        @SuppressWarnings("unchecked")
        List<Folder> folders = (List<Folder>) folderObject;
        assertEquals(1, folders.size());
        assertEquals("foo", folders.get(0).getName());
    }
}
