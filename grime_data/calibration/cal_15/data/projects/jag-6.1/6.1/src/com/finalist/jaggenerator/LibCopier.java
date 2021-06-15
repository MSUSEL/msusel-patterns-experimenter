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

package com.finalist.jaggenerator;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.net.URL;

/**
 * Helper class for copying jar files to the generated build file to prevent unnecessary downloads.
 *
 * User: Rudie Ekkelenkamp
 * Date: Apr 16, 2004
 * Time: 9:13:55 PM
 */
public class LibCopier {

    /**
     * Reads the libraries from the JAG generated libXmlFile and copy them if needed
     * from the local lib directory to the generated lib directory.
     * @throws javax.xml.parsers.ParserConfigurationException Indicates a serious configuration error.
     * @throws org.xml.sax.SAXException Encapsulate a general SAX error or warning.
     * @throws java.io.IOException Signals that an I/O exception of some sort has occurred
     * @return a Hashmap with filename/URL pairs as Strings that couldn't be copied .
     *
     */
    public static HashMap copyJars(String libXmlFile, String targetDir) throws ParserConfigurationException, SAXException, IOException {

       File target = new File(targetDir);
       HashMap failedCopies = new HashMap();
       if (!target.exists()) {
           System.out.println("Target directory to copy files to doesn't exist.");
           return null;
       }
       ArrayList libs = new ArrayList();
       DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
       dbf.setValidating(true);
       DocumentBuilder db = dbf.newDocumentBuilder();
       Document doc = db.parse(libXmlFile);
       NodeList nl = doc.getElementsByTagName("lib");
       for (int i = 0; i < nl.getLength(); i++) {
          Node node = nl.item(i);
          NamedNodeMap atts = node.getAttributes();
          String file;
          Node url = atts.getNamedItem("url");
          if (url != null) {
             file = url.getNodeValue();
             try {
                 URL theUrl = new URL(file);
                 String name = theUrl.getFile();
                 if (name.lastIndexOf("/") != -1) {
                     name = name.substring(name.lastIndexOf("/")+1);
                 }
                 libs.add(name);
             } catch (Exception e) {
                 e.printStackTrace();
             }
          }
       }

       for (Iterator iterator = libs.iterator(); iterator.hasNext();) {
           String s = (String) iterator.next();
           File sourceFile = new File(".."+File.separator+"lib"+File.separator+s);
           File targetFile = new File(targetDir+File.separator+s);
           if (sourceFile.exists()) {
              if (!targetFile.exists()) {
                 // Only copy if the target file doesn't exist yet.
                  copy(sourceFile, targetFile);
              } else {
              }
           } else {
               failedCopies.put(s, sourceFile.getCanonicalPath());
           }
       }
       return failedCopies;
    }

    // Copies src file to dst file.
    // If the dst file does not exist, it is created
    private static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void main(String[] args) {
        String lib = "lib.xml";
        try {
            copyJars(lib, "c:/temp");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
