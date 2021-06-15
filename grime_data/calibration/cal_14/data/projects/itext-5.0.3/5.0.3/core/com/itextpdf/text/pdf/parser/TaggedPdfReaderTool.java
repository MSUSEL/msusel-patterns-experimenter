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
package com.itextpdf.text.pdf.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;

/**
 * Converts a tagged PDF document into an XML file.
 * 
 * @since 5.0.2
 */
public class TaggedPdfReaderTool {

	/** The reader object from which the content streams are read. */
	PdfReader reader;
	/** The writer object to which the XML will be written */
	PrintWriter out;

	/**
	 * Parses a string with structured content.
	 * 
	 * @param reader
	 *            the PdfReader that has access to the PDF file
	 * @param os
	 *            the OutputStream to which the resulting xml will be written
	 */
	public void convertToXml(PdfReader reader, OutputStream os)
			throws IOException {
		this.reader = reader;
		out = new PrintWriter(os);
		// get the StructTreeRoot from the root object
		PdfDictionary catalog = reader.getCatalog();
		PdfDictionary struct = catalog.getAsDict(PdfName.STRUCTTREEROOT);
		// Inspect the child or children of the StructTreeRoot
		inspectChild(struct.getDirectObject(PdfName.K));
		out.flush();
		out.close();
	}

	/**
	 * Inspects a child of a structured element. This can be an array or a
	 * dictionary.
	 * 
	 * @param k
	 *            the child to inspect
	 * @throws IOException
	 */
	public void inspectChild(PdfObject k) throws IOException {
		if (k == null)
			return;
		if (k instanceof PdfArray)
			inspectChildArray((PdfArray) k);
		else if (k instanceof PdfDictionary)
			inspectChildDictionary((PdfDictionary) k);
	}

	/**
	 * If the child of a structured element is an array, we need to loop over
	 * the elements.
	 * 
	 * @param k
	 *            the child array to inspect
	 */
	public void inspectChildArray(PdfArray k) throws IOException {
		if (k == null)
			return;
		for (int i = 0; i < k.size(); i++) {
			inspectChild(k.getDirectObject(i));
		}
	}

	/**
	 * If the child of a structured element is a dictionary, we inspect the
	 * child; we may also draw a tag.
	 * 
	 * @param k
	 *            the child dictionary to inspect
	 */
	public void inspectChildDictionary(PdfDictionary k) throws IOException {
		if (k == null)
			return;
		PdfName s = k.getAsName(PdfName.S);
		if (s != null) {
			String tag = s.toString().substring(1);
			out.print("<");
			out.print(tag);
			out.print(">");
			PdfDictionary dict = k.getAsDict(PdfName.PG);
			if (dict != null)
				parseTag(tag, k.getDirectObject(PdfName.K), dict);
			inspectChild(k.get(PdfName.K));
			out.print("</");
			out.print(tag);
			out.println(">");
		} else
			inspectChild(k.get(PdfName.K));
	}

	/**
	 * Searches for a tag in a page.
	 * 
	 * @param tag
	 *            the name of the tag
	 * @param object
	 *            an identifier to find the marked content
	 * @param page
	 *            a page dictionary
	 * @throws IOException
	 */
	public void parseTag(String tag, PdfObject object, PdfDictionary page)
			throws IOException {
		PRStream stream = (PRStream) page.getAsStream(PdfName.CONTENTS);
		// if the identifier is a number, we can extract the content right away
		if (object instanceof PdfNumber) {
			PdfNumber mcid = (PdfNumber) object;
			RenderFilter filter = new MarkedContentRenderFilter(mcid.intValue());
			TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
			FilteredTextRenderListener listener = new FilteredTextRenderListener(
					strategy, filter);
			PdfContentStreamProcessor processor = new PdfContentStreamProcessor(
					listener);
			processor.processContent(PdfReader.getStreamBytes(stream), page
					.getAsDict(PdfName.RESOURCES));
			out.print(SimpleXMLParser.escapeXML(listener.getResultantText(), true));
		}
		// if the identifier is an array, we call the parseTag method
		// recursively
		else if (object instanceof PdfArray) {
			PdfArray arr = (PdfArray) object;
			int n = arr.size();
			for (int i = 0; i < n; i++) {
				parseTag(tag, arr.getPdfObject(i), page);
				if (i < n - 1)
					out.println();
			}
		}
		// if the identifier is a dictionary, we get the resources from the
		// dictionary
		else if (object instanceof PdfDictionary) {
			PdfDictionary mcr = (PdfDictionary) object;
			parseTag(tag, mcr.getDirectObject(PdfName.MCID), mcr
					.getAsDict(PdfName.PG));
		}
	}

}
