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
package org.apache.hadoop.tools.rumen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * {@link JobConfigurationParser} parses the job configuration xml file, and
 * extracts configuration properties. It parses the file using a
 * stream-parser and thus is more memory efficient. [This optimization may be
 * postponed for a future release]
 */
public class JobConfigurationParser {

  /**
   * Parse the job configuration file (as an input stream) and return a
   * {@link Properties} collection. The input stream will not be closed after
   * return from the call.
   * 
   * @param input
   *          The input data.
   * @return A {@link Properties} collection extracted from the job
   *         configuration xml.
   * @throws IOException
   */
  static Properties parse(InputStream input) throws IOException {
    Properties result = new Properties();

    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      DocumentBuilder db = dbf.newDocumentBuilder();

      Document doc = db.parse(input);

      Element root = doc.getDocumentElement();

      if (!"configuration".equals(root.getTagName())) {
        System.out.print("root is not a configuration node");
        return null;
      }

      NodeList props = root.getChildNodes();

      for (int i = 0; i < props.getLength(); ++i) {
        Node propNode = props.item(i);
        if (!(propNode instanceof Element))
          continue;
        Element prop = (Element) propNode;
        if (!"property".equals(prop.getTagName())) {
          System.out.print("bad conf file: element not <property>");
        }
        NodeList fields = prop.getChildNodes();
        String attr = null;
        String value = null;
        @SuppressWarnings("unused")
        boolean finalParameter = false;
        for (int j = 0; j < fields.getLength(); j++) {
          Node fieldNode = fields.item(j);
          if (!(fieldNode instanceof Element)) {
            continue;
          }

          Element field = (Element) fieldNode;
          if ("name".equals(field.getTagName()) && field.hasChildNodes()) {
            attr = ((Text) field.getFirstChild()).getData().trim();
          }
          if ("value".equals(field.getTagName()) && field.hasChildNodes()) {
            value = ((Text) field.getFirstChild()).getData();
          }
          if ("final".equals(field.getTagName()) && field.hasChildNodes()) {
            finalParameter =
                "true".equals(((Text) field.getFirstChild()).getData());
          }
        }

        if (attr != null && value != null) {
          result.put(attr, value);
        }
      }
    } catch (ParserConfigurationException e) {
      return null;
    } catch (SAXException e) {
      return null;
    }

    return result;
  }
}
