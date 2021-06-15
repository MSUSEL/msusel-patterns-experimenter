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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;


/**
 * WriterUtils purpose.
 * 
 * <p>
 * Used to provide assitance writing xml to a Writer.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @source $URL$
 * @version $Id$
 */
class WriterUtils {
    /** Used internally to create log information to detect errors. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.vfny.geoserver.global");

    /** The output writer. */
    protected Writer writer;

    /**
     * WriterUtils constructor.
     * 
     * <p>
     * Should never be called.
     * </p>
     */
    protected WriterUtils() {
    }

    /**
     * WriterUtils constructor.
     * 
     * <p>
     * Stores the specified writer to use for output.
     * </p>
     *
     * @param writer the writer which will be used for outputing the xml.
     */
    public WriterUtils(Writer writer) {
        //LOGGER.finest("In constructor WriterHelper");
        this.writer = writer;
    }

    /**
     * write purpose.
     * 
     * <p>
     * Writes the String specified to the stored output writer.
     * </p>
     *
     * @param s The String to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void write(String s) throws IOException {
        writer.write(s);
        writer.flush();
    }

    /**
     * writeln purpose.
     * 
     * <p>
     * Writes the String specified to the stored output writer.
     * </p>
     *
     * @param s The String to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void writeln(String s) throws IOException {
        writer.write(s + "\n");
        writer.flush();
    }

    /**
     * openTag purpose.
     * 
     * <p>
     * Writes an open xml tag with the name specified to the stored output
     * writer.
     * </p>
     *
     * @param tagName The tag name to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void openTag(String tagName) throws IOException {
        writeln("<" + tagName + ">");
    }

    /**
     * openTag purpose.
     * 
     * <p>
     * Writes an open xml tag with the name and attributes specified to the
     * stored output writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param attributes The tag attributes to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void openTag(String tagName, Map attributes)
        throws IOException {
        write("<" + tagName + " ");

        Iterator i = attributes.keySet().iterator();

        while (i.hasNext()) {
            String s = (String) i.next();
            write(s + " = " + "\"" + (attributes.get(s)).toString() + "\" ");
        }

        writeln(">");
    }

    /**
     * closeTag purpose.
     * 
     * <p>
     * Writes an close xml tag with the name specified to the stored output
     * writer.
     * </p>
     *
     * @param tagName The tag name to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void closeTag(String tagName) throws IOException {
        writeln("</" + tagName + ">");
    }

    /**
     * textTag purpose.
     * 
     * <p>
     * Writes a text xml tag with the name and text specified to the stored
     * output writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param data The text data to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void textTag(String tagName, String data) throws IOException {
        writeln("<" + tagName + ">" + data + "</" + tagName + ">");
    }

    /**
     * valueTag purpose.
     * 
     * <p>
     * Writes an xml tag with the name and value specified to the stored output
     * writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param value The text data to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void valueTag(String tagName, String value)
        throws IOException {
        writeln("<" + tagName + " value = \"" + value + "\" />");
    }

    /**
     * attrTag purpose.
     * 
     * <p>
     * Writes an xml tag with the name and attributes specified to the stored
     * output writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param attributes The tag attributes to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void attrTag(String tagName, Map attributes)
        throws IOException {
        write("<" + tagName + " ");

        Iterator i = attributes.keySet().iterator();

        while (i.hasNext()) {
            String s = (String) i.next();
            write(s + " = " + "\"" + (attributes.get(s)).toString() + "\" ");
        }

        write(" />");
    }

    /**
     * textTag purpose.
     * 
     * <p>
     * Writes an xml tag with the name, text and attributes specified to the
     * stored output writer.
     * </p>
     *
     * @param tagName The tag name to write.
     * @param attributes The tag attributes to write.
     * @param data The tag text to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void textTag(String tagName, Map attributes, String data)
        throws IOException {
        write("<" + tagName + " ");

        Iterator i = attributes.keySet().iterator();

        while (i.hasNext()) {
            String s = (String) i.next();
            write(s + " = " + "\"" + (attributes.get(s)).toString() + "\" ");
        }

        write(">" + data + "</" + tagName + ">");
    }

    /**
     * comment purpose.
     * 
     * <p>
     * Writes an xml comment with the text specified to the stored output
     * writer.
     * </p>
     *
     * @param comment The comment text to write.
     *
     * @throws IOException When an IO exception occurs.
     */
    public void comment(String comment) throws IOException {
        writeln("<!--");
        writeln(comment);
        writeln("-->");
    }
}
