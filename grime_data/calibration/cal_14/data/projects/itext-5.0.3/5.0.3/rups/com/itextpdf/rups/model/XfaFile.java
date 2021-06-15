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
package com.itextpdf.rups.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.itextpdf.rups.io.OutputStreamResource;

/** Class that deals with the XFA file that can be inside a PDF file. */
public class XfaFile implements OutputStreamResource {

	/** The X4J Document object (XML). */
	protected Document xfaDocument;
	
	/**
	 * Constructs an XFA file from an OutputStreamResource.
	 * This resource can be an XML file or a node in a RUPS application.
	 * @param	resource	the XFA resource
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public XfaFile(OutputStreamResource resource) throws IOException, DocumentException {
		// Is there a way to avoid loading everything in memory?
		// Can we somehow get the XML from the PDF as an InputSource, Reader or InputStream?
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		resource.writeTo(baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		SAXReader reader = new SAXReader();
		xfaDocument = reader.read(bais);
	}

	/**
	 * Getter for the XFA Document object.
	 * @return	a Document object (X4J)
	 */
	public Document getXfaDocument() {
		return xfaDocument;
	}

	/**
	 * Writes a formatted XML file to the OutputStream.
	 * @see com.itextpdf.rups.io.OutputStreamResource#writeTo(java.io.OutputStream)
	 */
	public void writeTo(OutputStream os) throws IOException {
		if (xfaDocument == null)
			return;
		OutputFormat format = new OutputFormat("   ", true);
        XMLWriter writer = new XMLWriter(os, format);
        writer.write(xfaDocument);
	}
}
