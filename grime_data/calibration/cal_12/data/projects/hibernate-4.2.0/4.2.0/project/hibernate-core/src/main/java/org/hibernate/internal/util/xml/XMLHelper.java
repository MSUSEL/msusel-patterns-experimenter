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
package org.hibernate.internal.util.xml;

import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.hibernate.internal.util.ClassLoaderHelper;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

/**
 * Small helper class that lazy loads DOM and SAX reader and keep them for fast use afterwards.
 */
public final class XMLHelper {

	public static final EntityResolver DEFAULT_DTD_RESOLVER = new DTDEntityResolver();

	private DOMReader domReader;
	private SAXReader saxReader;

	/**
	 * @param errorHandler the sax error handler
	 * @param entityResolver an xml entity resolver
	 *
	 * @return Create and return a dom4j {@code SAXReader} which will append all validation errors
	 *         to the passed error list
	 */
	public SAXReader createSAXReader(ErrorHandler errorHandler, EntityResolver entityResolver) {
		SAXReader saxReader = resolveSAXReader();
		saxReader.setEntityResolver( entityResolver );
		saxReader.setErrorHandler( errorHandler );
		return saxReader;
	}

	private SAXReader resolveSAXReader() {
		if ( saxReader == null ) {
			saxReader = new SAXReader();
			saxReader.setMergeAdjacentText( true );
			saxReader.setValidation( true );
		}
		return saxReader;
	}

	/**
	 * @return create and return a dom4j DOMReader
	 */
	public DOMReader createDOMReader() {
		if ( domReader == null ) {
			domReader = new DOMReader();
		}
		return domReader;
	}

	public static Element generateDom4jElement(String elementName) {
		return getDocumentFactory().createElement( elementName );
	}

	public static DocumentFactory getDocumentFactory() {

		ClassLoader cl = ClassLoaderHelper.getContextClassLoader();
		DocumentFactory factory;
		try {
			Thread.currentThread().setContextClassLoader( XMLHelper.class.getClassLoader() );
			factory = DocumentFactory.getInstance();
		}
		finally {
			Thread.currentThread().setContextClassLoader( cl );
		}
		return factory;
	}

	public static void dump(Element element) {
		try {
			// try to "pretty print" it
			OutputFormat outFormat = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter( System.out, outFormat );
			writer.write( element );
			writer.flush();
			System.out.println( "" );
		}
		catch ( Throwable t ) {
			// otherwise, just dump it
			System.out.println( element.asXML() );
		}

	}
}
