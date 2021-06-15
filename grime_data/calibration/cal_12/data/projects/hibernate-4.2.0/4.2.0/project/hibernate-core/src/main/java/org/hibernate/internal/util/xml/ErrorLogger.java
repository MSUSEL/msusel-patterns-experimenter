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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import org.hibernate.internal.CoreMessageLogger;

/**
 * Implements an {@link ErrorHandler} that mainly just logs errors/warnings.  However, it does track
 * the errors it encounters and makes them available via {@link #getErrors}.
 *
 * @author Steve Ebersole
 * @author Hardy Ferentschik
 */
public class ErrorLogger implements ErrorHandler, Serializable {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger(
			CoreMessageLogger.class,
			ErrorLogger.class.getName()
	);

	// lazily initalized
	private List<SAXParseException> errors;
	private String file;

	public ErrorLogger() {
	}

	public ErrorLogger(String file) {
		this.file = file;
	}

	/**
	 * {@inheritDoc}
	 */
	public void error(SAXParseException error) {
		if ( this.errors == null ) {
			errors = new ArrayList<SAXParseException>();
		}
		errors.add( error );
	}

	/**
	 * {@inheritDoc}
	 */
	public void fatalError(SAXParseException error) {
		error( error );
	}

	/**
	 * {@inheritDoc}
	 */
	public void warning(SAXParseException warn) {
		LOG.parsingXmlWarning( warn.getLineNumber(), warn.getMessage() );
	}

	/**
	 * @return returns a list of encountered xml parsing errors, or the empty list if there was no error
	 */
	public List<SAXParseException> getErrors() {
		return errors;
	}

	public void reset() {
		errors = null;
	}

	public boolean hasErrors() {
		return errors != null && errors.size() > 0;
	}

	public void logErrors() {
		if ( errors != null ) {
			for ( SAXParseException e : errors ) {
				if ( file == null ) {
					LOG.parsingXmlError( e.getLineNumber(), e.getMessage() );
				}
				else {
					LOG.parsingXmlErrorForFile( file, e.getLineNumber(), e.getMessage() );
				}
			}
		}
	}
}
