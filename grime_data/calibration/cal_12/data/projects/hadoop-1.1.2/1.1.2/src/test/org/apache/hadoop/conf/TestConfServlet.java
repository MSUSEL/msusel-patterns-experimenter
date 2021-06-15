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
package org.apache.hadoop.conf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.mortbay.util.ajax.JSON;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Basic test case that the ConfServlet can write configuration
 * to its output in XML and JSON format.
 */
public class TestConfServlet {
  private static final String TEST_KEY = "testconfservlet.key";
  private static final String TEST_VAL = "testval";

  private Configuration getTestConf() {
    Configuration testConf = new Configuration();
    testConf.set(TEST_KEY, TEST_VAL);
    return testConf;
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testWriteJson() throws Exception {
    StringWriter sw = new StringWriter();
    ConfServlet.writeResponse(getTestConf(), sw, "json");
    String json = sw.toString();
    boolean foundSetting = false;
    Object parsed = JSON.parse(json);
    Object[] properties = ((Map<String, Object[]>)parsed).get("properties");
    for (Object o : properties) {
      Map<String, Object> propertyInfo = (Map<String, Object>)o;
      String key = (String)propertyInfo.get("key");
      String val = (String)propertyInfo.get("value");
      String resource = (String)propertyInfo.get("resource");
      System.err.println("k: " + key + " v: " + val + " r: " + resource);
      if (TEST_KEY.equals(key) && TEST_VAL.equals(val)
          && Configuration.UNKNOWN_RESOURCE.equals(resource)) {
        foundSetting = true;
      }
    }
    assertTrue(foundSetting);
  }

  @Test
  public void testWriteXml() throws Exception {
    StringWriter sw = new StringWriter();
    ConfServlet.writeResponse(getTestConf(), sw, "xml");
    String xml = sw.toString();

    DocumentBuilderFactory docBuilderFactory 
      = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
    Document doc = builder.parse(new InputSource(new StringReader(xml)));
    NodeList nameNodes = doc.getElementsByTagName("name");
    boolean foundSetting = false;
    for (int i = 0; i < nameNodes.getLength(); i++) {
      Node nameNode = nameNodes.item(i);
      String key = nameNode.getTextContent();
      System.err.println("xml key: " + key);
      if (TEST_KEY.equals(key)) {
        foundSetting = true;
        Element propertyElem = (Element)nameNode.getParentNode();
        String val = propertyElem.getElementsByTagName("value").item(0).getTextContent();
        assertEquals(TEST_VAL, val);
      }
    }
    assertTrue(foundSetting);
  }

  @Test
  public void testBadFormat() throws Exception {
    StringWriter sw = new StringWriter();
    try {
      ConfServlet.writeResponse(getTestConf(), sw, "not a format");
      fail("writeResponse with bad format didn't throw!");
    } catch (ConfServlet.BadFormatException bfe) {
      // expected
    }
    assertEquals("", sw.toString());
  }
}