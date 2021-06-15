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
package org.hibernate.test.annotations.reflection;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.dom4j.io.SAXReader;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotSupportedException;

import org.hibernate.cfg.EJB3DTDEntityResolver;
import org.hibernate.cfg.annotations.reflection.XMLContext;
import org.hibernate.internal.util.xml.ErrorLogger;
import org.hibernate.internal.util.xml.XMLHelper;

/**
 * @author Emmanuel Bernard
 */
public class XMLContextTest {
	@Test
	public void testAll() throws Exception {
		XMLHelper xmlHelper = new XMLHelper();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream is = cl.getResourceAsStream(
				"org/hibernate/test/annotations/reflection/orm.xml"
		);
		Assert.assertNotNull( "ORM.xml not found", is );
		XMLContext context = new XMLContext();
		ErrorLogger errorLogger = new ErrorLogger();
		SAXReader saxReader = xmlHelper.createSAXReader( errorLogger, EJB3DTDEntityResolver.INSTANCE );
		//saxReader.setValidation( false );
		try {
			saxReader.setFeature( "http://apache.org/xml/features/validation/schema", true );
		}
		catch ( SAXNotSupportedException e ) {
			saxReader.setValidation( false );
		}
		org.dom4j.Document doc;
		try {
			doc = saxReader
					.read( new InputSource( new BufferedInputStream( is ) ) );
		}
		finally {
			try {
				is.close();
			}
			catch ( IOException ioe ) {
				//log.warn( "Could not close input stream", ioe );
			}
		}
		Assert.assertFalse( errorLogger.hasErrors() );
		context.addDocument( doc );
	}
}
