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

import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import org.xml.sax.SAXException;

class ParsedConfigFile {
  private static final Pattern jobIDPattern =
      Pattern.compile("_(job_[0-9]+_[0-9]+)_");
  private static final Pattern heapPattern =
      Pattern.compile("-Xmx([0-9]+)([mMgG])");

  final int heapMegabytes;

  final String queue;
  final String jobName;

  final int clusterMapMB;
  final int clusterReduceMB;
  final int jobMapMB;
  final int jobReduceMB;

  final String jobID;

  final boolean valid;
  
  final Properties properties = new Properties();

  private int maybeGetIntValue(String propName, String attr, String value,
      int oldValue) {
    if (propName.equals(attr) && value != null) {
      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException e) {
        return oldValue;
      }
    }

    return oldValue;
  }

  @SuppressWarnings("hiding")
  @Deprecated
  ParsedConfigFile(String filenameLine, String xmlString) {
    super();

    int heapMegabytes = -1;

    String queue = null;
    String jobName = null;

    int clusterMapMB = -1;
    int clusterReduceMB = -1;
    int jobMapMB = -1;
    int jobReduceMB = -1;

    String jobID = null;

    boolean valid = true;

    Matcher jobIDMatcher = jobIDPattern.matcher(filenameLine);

    if (jobIDMatcher.find()) {
      jobID = jobIDMatcher.group(1);
    }

    try {
      InputStream is = new ByteArrayInputStream(xmlString.getBytes());

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

      DocumentBuilder db = dbf.newDocumentBuilder();

      Document doc = db.parse(is);

      Element root = doc.getDocumentElement();

      if (!"configuration".equals(root.getTagName())) {
        System.out.print("root is not a configuration node");
        valid = false;
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
        
        properties.setProperty(attr, value);

        if ("mapred.child.java.opts".equals(attr) && value != null) {
          Matcher matcher = heapPattern.matcher(value);
          if (matcher.find()) {
            String heapSize = matcher.group(1);

            heapMegabytes = Integer.parseInt(heapSize);

            if (matcher.group(2).equalsIgnoreCase("G")) {
              heapMegabytes *= 1024;
            }
          }
        }

        if ("mapred.queue.name".equals(attr) && value != null) {
          queue = value;
        }

        if ("mapred.job.name".equals(attr) && value != null) {
          jobName = value;
        }

        clusterMapMB =
            maybeGetIntValue("mapreduce.cluster.mapmemory.mb", attr, value, clusterMapMB);
        clusterReduceMB =
            maybeGetIntValue("mapred.cluster.reduce.memory.mb", attr, value,
                clusterReduceMB);
        jobMapMB =
            maybeGetIntValue("mapred.job.map.memory.mb", attr, value, jobMapMB);
        jobReduceMB =
            maybeGetIntValue("mapred.job.reduce.memory.mb", attr, value, jobReduceMB);
      }

      valid = true;
    } catch (ParserConfigurationException e) {
      valid = false;
    } catch (SAXException e) {
      valid = false;
    } catch (IOException e) {
      valid = false;
    }

    this.heapMegabytes = heapMegabytes;

    this.queue = queue;
    this.jobName = jobName;

    this.clusterMapMB = clusterMapMB;
    this.clusterReduceMB = clusterReduceMB;
    this.jobMapMB = jobMapMB;
    this.jobReduceMB = jobReduceMB;

    this.jobID = jobID;

    this.valid = valid;
  }
}
