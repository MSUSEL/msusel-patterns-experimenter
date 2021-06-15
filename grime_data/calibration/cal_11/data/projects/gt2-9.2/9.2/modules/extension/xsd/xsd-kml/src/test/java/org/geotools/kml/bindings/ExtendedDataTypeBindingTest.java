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

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xml.Binding;

public class ExtendedDataTypeBindingTest extends KMLTestSupport {

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(KML.ExtendedDataType).getExecutionMode());
    }

    public void testGetType() {
        assertEquals(Map.class, binding(KML.ExtendedDataType).getType());
    }

    // to avoid warnings
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseExtendedData() throws Exception {
        return (Map<String, Object>) parse();
    }

    @SuppressWarnings("unchecked")
    public void testParseEmpty() throws Exception {
        String xml = "<ExtendedData></ExtendedData>";
        buildDocument(xml);
        Map<String, Object> document = parseExtendedData();
        assertEquals(3, document.size());
        List<URI> schemas = (List<URI>) document.get("schemas");
        Map<String, Object> typed = (Map<String, Object>) document.get("typed");
        Map<String, Object> untyped = (Map<String, Object>) document.get("untyped");
        assertTrue(schemas.isEmpty());
        assertTrue(typed.isEmpty());
        assertTrue(untyped.isEmpty());
    }

    public void testParseUntyped() throws Exception {
        String xml = "<ExtendedData>" + "<Data name=\"foo\"><value>bar</value></Data>"
                + "</ExtendedData>";
        buildDocument(xml);
        Map<String, Object> document = parseExtendedData();
        @SuppressWarnings("unchecked")
        Map<String, Object> untyped = (Map<String, Object>) document.get("untyped");
        assertEquals("bar", untyped.get("foo"));
    }

    @SuppressWarnings("unchecked")
    public void testParseTyped() throws Exception {
        String xml = "<ExtendedData>" + "<SchemaData schemaUrl=\"#foo\">"
                + "<SimpleData name=\"quux\">morx</SimpleData>" + "</SchemaData>"
                + "</ExtendedData>";
        buildDocument(xml);
        Map<String, Object> document = parseExtendedData();
        Map<String, Object> typed = (Map<String, Object>) document.get("typed");
        assertEquals("morx", typed.get("quux"));
        List<URI> schemaURLS = (List<URI>) document.get("schemas");
        assertEquals(1, schemaURLS.size());
        assertEquals("foo", schemaURLS.get(0).getFragment());
    }

    @SuppressWarnings("unchecked")
    public void testParseMultipleTypes() throws Exception {
        String xml = "<ExtendedData>" + "<SchemaData schemaUrl=\"#foo1\">"
                + "<SimpleData name=\"quux\">morx</SimpleData>" + "</SchemaData>"
                + "<SchemaData schemaUrl=\"#foo2\">"
                + "<SimpleData name=\"fleem\">zul</SimpleData>" + "</SchemaData>"
                + "</ExtendedData>";
        buildDocument(xml);
        Map<String, Object> document = parseExtendedData();
        Map<String, Object> typed = (Map<String, Object>) document.get("typed");
        assertEquals("morx", typed.get("quux"));
        List<URI> schemaURLS = (List<URI>) document.get("schemas");
        assertEquals(2, schemaURLS.size());
        assertEquals("foo1", schemaURLS.get(0).getFragment());
        assertEquals("foo2", schemaURLS.get(1).getFragment());
    }

}
