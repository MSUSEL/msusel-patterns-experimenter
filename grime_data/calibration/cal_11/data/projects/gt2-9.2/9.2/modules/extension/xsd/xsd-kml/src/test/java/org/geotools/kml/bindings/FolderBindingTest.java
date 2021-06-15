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

import java.util.List;
import java.util.Map;

import org.geotools.kml.Folder;
import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xml.Binding;
import org.opengis.feature.simple.SimpleFeature;

public class FolderBindingTest extends KMLTestSupport {

    public void testType() throws Exception {
        assertEquals(SimpleFeature.class, binding(KML.Folder).getType());
    }

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.AFTER, binding(KML.Folder).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml = "<Folder>" + "<name>foo</name>" + "</Folder>";
        buildDocument(xml);
        SimpleFeature folder = (SimpleFeature) parse();
        assertEquals("foo", folder.getAttribute("name"));
    }

    @SuppressWarnings("unchecked")
    public void testFolderHierarchy() throws Exception {
        String xml = "<Folder><name>foo</name>" + "<Folder><name>bar</name>"
                + "<Placemark><name>morx</name></Placemark>" + "</Folder>" + "</Folder>";
        buildDocument(xml);
        SimpleFeature folder1 = (SimpleFeature) parse();
        List<SimpleFeature> features = (List<SimpleFeature>) folder1.getAttribute("Feature");
        SimpleFeature folder2 = features.get(0);
        features = (List<SimpleFeature>) folder2.getAttribute("Feature");
        SimpleFeature placemark = features.get(0);
        Map<Object, Object> userData = placemark.getUserData();
        Object folderObject = userData.get("Folder");
        assertNotNull("No folder user data", folderObject);
        assertTrue("Unknown user data", folderObject instanceof List<?>);
        List<Folder> folders = (List<Folder>) folderObject;
        assertEquals(2, folders.size());
        assertEquals("foo", folders.get(0).getName());
        assertEquals("bar", folders.get(1).getName());
        assertEquals("morx", placemark.getAttribute("name"));
    }

}
